package com.example.roomdatabaseex.Callbacks;

import com.example.roomdatabaseex.Model.User;

public interface IUpdateUserListener {
    void updateUser(User user);

    void deleteUser(User user);
}
