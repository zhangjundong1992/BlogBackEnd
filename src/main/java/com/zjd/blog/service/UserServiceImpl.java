package com.zjd.blog.service;

import com.zjd.blog.dao.UserRepository;
import com.zjd.blog.dao.po.User;
import com.zjd.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findAllByUsernameAndPassword(username, MD5Utils.code(password));
    }
}
