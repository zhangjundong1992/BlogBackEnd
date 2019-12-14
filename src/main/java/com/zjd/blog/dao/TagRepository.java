package com.zjd.blog.dao;

import com.zjd.blog.dao.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    /*获取文章数目最多的tag列表*/
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
