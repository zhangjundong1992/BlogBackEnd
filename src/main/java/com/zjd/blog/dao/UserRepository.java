package com.zjd.blog.dao;

import com.zjd.blog.dao.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findAllByUsernameAndPassword(String username, String password);
}
