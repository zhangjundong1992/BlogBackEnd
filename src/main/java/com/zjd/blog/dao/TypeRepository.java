package com.zjd.blog.dao;

import com.zjd.blog.dao.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Type findByName(String name);

    /*获取文章数目最多的type列表*/
    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
