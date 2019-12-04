package com.zjd.blog.service;

import com.zjd.blog.po.User;


public interface UserService {
    User checkUser(String username, String password);
}
