package com.lyhoangvinh.testroomdb2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lyhoangvinh.testroomdb2.R;
import com.lyhoangvinh.testroomdb2.listener.OnClickItemUserListener;
import com.lyhoangvinh.testroomdb2.listener.OnLongClickUserListener;
import com.lyhoangvinh.testroomdb2.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Setter;
import lyhoangvinh.com.myutil.adapter.BaseAdapter;

public class UserAdapter extends BaseAdapter<User, UserAdapter.UserViewHolder> {

    public UserAdapter(@NonNull List<User> data) {
        super(data);
    }

    @Override
    public int getItemLayoutResource() {
        return R.layout.item;
    }

    @Override
    public UserViewHolder createViewHolder(View view) {
        return new UserViewHolder(view);
    }

    @Setter
    private Context context;

    @Setter
    private OnClickItemUserListener onClickItemUserListener;

    @Setter
    private OnLongClickUserListener onLongClickUserListener;

    @Override
    protected void onBindViewHolder(UserViewHolder holder, @NonNull final User user) {
        holder.tvName.setText(user.getFullName());
        holder.tvAge.setText(user.getAgeString());
        holder.tvUid.setText(String.valueOf(user.getUid()));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(context).load(user.getAvatar()).apply(options).into(holder.imageBackground);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemUserListener != null) {
                    onClickItemUserListener.onClick(user);
                }
            }
        });

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickUserListener != null) {
                    onLongClickUserListener.onClick(user);
                }
                return true;
            }
        });
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_background)
        ImageView imageBackground;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvUid)
        TextView tvUid;
        @BindView(R.id.tvAge)
        TextView tvAge;
        @BindView(R.id.card)
        CardView card;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
