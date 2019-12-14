package com.zjd.blog.service;

import com.zjd.blog.exception.NotFoundException;
import com.zjd.blog.dao.TagRepository;
import com.zjd.blog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    public Tag getTag(long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    /*分页查询*/
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    /*根据id列表查询*/
    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!ids.isEmpty() && !ids.isBlank()) {
            String[] idArray = ids.split(",");
            for (String s : idArray) {
                list.add(Long.valueOf(s));
            }
        }
        return list;
    }

    /*获取文章数目最多的tag列表*/
    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
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
