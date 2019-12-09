package com.zjd.blog.service;

import com.zjd.blog.dao.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTag(long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    Tag updateTag(long id, Tag tag);

    void deleteTag(long id);
}
