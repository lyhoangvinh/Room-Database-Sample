package com.lyhoangvinh.testroomdb2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lyhoangvinh.testroomdb2.adapter.UserAdapter;
import com.lyhoangvinh.testroomdb2.customview.CropImageUtil;
import com.lyhoangvinh.testroomdb2.database.DatabaseManager;
import com.lyhoangvinh.testroomdb2.listener.OnClickItemUserListener;
import com.lyhoangvinh.testroomdb2.listener.OnLongClickUserListener;
import com.lyhoangvinh.testroomdb2.model.User;
import com.lyhoangvinh.testroomdb2.presenter.UserPresenter;
import com.lyhoangvinh.testroomdb2.view.UserView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class MainActivity extends ActivityManagePermission implements UserView {

    @BindView(R.id.rcv)
    RecyclerView rcv;

    private UserAdapter adapter;
    private List<User> userList;
    private Uri resultUri;
    private UserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new UserPresenter(this, this);
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
        adapter.setContext(this);

        adapter.setOnClickItemUserListener(new OnClickItemUserListener() {
            @Override
            public void onClick(User user) {
                dialogUser(user).show();
            }
        });

        adapter.setOnLongClickUserListener(new OnLongClickUserListener() {
            @Override
            public void onClick(User user) {
                showAlertDialog(user);
            }
        });
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcv.setAdapter(adapter);
        presenter.getAddData();
    }

    private void showAlertDialog(final User user) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Thông báo");
        alertDialogBuilder
                .setMessage("Bạn có muốn xóa user này?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.deleteUser(user);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseManager.destroyInstance();
    }

    @OnClick(R.id.iBtnAdd)
    public void showDialogAdd() {
        dialogUser(null).show();
    }

    private Dialog dialogUser(final User user) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_custom);
        final EditText edtFisrtName = dialog.findViewById(R.id.edtFirstName);
        final EditText edtLastName = dialog.findViewById(R.id.edtLastName);
        final EditText edtAge = dialog.findViewById(R.id.edtAge);
        ImageView img = dialog.findViewById(R.id.img);
        Button btn = dialog.findViewById(R.id.btnAddOrEdit);

        if (user == null) {
            btn.setText("Add");
        } else {
            btn.setText("Edit");
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCompactPermissions();
            }
        });
        if (user == null) {
            edtFisrtName.setText("");
            edtLastName.setText("");
            edtAge.setText("");
        } else {
            edtFisrtName.setText(user.getFirstName());
            edtLastName.setText(user.getLastName());
            edtAge.setText(String.valueOf(user.getAge()));
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(this).load(user.getAvatar()).apply(options).into(img);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = edtFisrtName.getText().toString();
                String lastName = edtLastName.getText().toString();
                int age = 9;
                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(MainActivity.this, "Please enter first name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(lastName)) {
                    Toast.makeText(MainActivity.this, "Please enter last name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(edtAge.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter age", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    age = Integer.parseInt(edtAge.getText().toString());
                    if (user == null) {
                        User userAdd = new User();
                        userAdd.setAge(age);
                        userAdd.setFirstName(firstName);
                        userAdd.setLastName(lastName);
                        userAdd.setAvatar("http://www.phunungaynay.vn/wp-content/uploads/2015/11/zmc-324x160.jpg");
                        presenter.addUser(userAdd);
                    } else {
                        user.setAge(age);
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        presenter.editUser(user);
                    }
                }

                dialog.dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CropImageUtil.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                Uri selectedUri = CropImageUtil.getPickImageResultUri(this, data);
                if (selectedUri != null) {
                    startCropActivity(selectedUri);
                } else {
                    Toast.makeText(this, getString(R.string.toast_cannot_retrieve_selected_image), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.toast_unexpected_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // TODO: upload image to server
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = "avatar_crop";
        destinationFileName += ".jpg";

        UCrop.Options options = new UCrop.Options();
//        options.setMaxBitmapSize();
        options.setHideBottomControls(true);
        options.setToolbarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarTitle("Crop Photo");
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)))
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .withOptions(options);
        uCrop.start(this);
    }

    public void askCompactPermissions() {
        askCompactPermissions(new String[]{PermissionUtils.Manifest_CAMERA,
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE}, new PermissionResult() {
            @Override
            public void permissionGranted() {
                CropImageUtil.startPickImageActivity(MainActivity.this, R.string.select_image);
            }

            @Override
            public void permissionDenied() {
                Toast.makeText(MainActivity.this, getString(R.string.warn_access_grant), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void permissionForeverDenied() {
                Toast.makeText(MainActivity.this, getString(R.string.warn_access_grant), Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void getAllData(List<User> list) {
        if (list != null) {
            userList.clear();
            userList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
