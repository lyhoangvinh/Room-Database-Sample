package com.lyhoangvinh.testroomdb2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.lyhoangvinh.testroomdb2.model.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    Flowable<List<User>> getAllUserFlowable();

    @Query("SELECT * FROM user")
    Single<List<User>> getAllUserSingle();

    @Query("SELECT * FROM user WHERE age = :ageIn ")
    Maybe<User> getUserByAgeMayBe(String ageIn);

    @Query("SELECT * FROM user LIMIT 1")
    Single<User> getOneUserSingle();

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user where first_name LIKE  :firstName AND last_name LIKE :lastName")
    User findByName(String firstName, String lastName);

    @Query("SELECT COUNT(*) from user")
    int countUsers();

    @Insert
    void insertAll(User... users);

    @Update
    void update(User... users);

    @Insert
    void insertList(List<User> users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void clearUser();
}
