package com.zjd.blog.service;

import com.zjd.blog.NotFoundException;
import com.zjd.blog.dao.TagRepository;
import com.zjd.blog.dao.po.Tag;
import com.zjd.blog.dao.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Tag updateTag(long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t == null)
            throw new NotFoundException("不存在该类型");

        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(long id) {
        tagRepository.deleteById(id);
    }
}
