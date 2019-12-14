package com.zjd.blog.service;

import com.zjd.blog.util.exception.NotFoundException;
import com.zjd.blog.dao.BlogRepository;
import com.zjd.blog.dao.po.Blog;
import com.zjd.blog.dao.po.Type;
import com.zjd.blog.util.MarkdownUtils;
import com.zjd.blog.util.MyBeanUtils;
import com.zjd.blog.dao.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    /*将markdown转换为html*/
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }

        /*更新浏览次数*/
        blog.setCountView(blog.getCountView() + 1);
        blogRepository.save(blog);

        Blog b = new Blog();/*复制一份新的，防止改变原数据*/
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return b;
    }

    /*筛选，根据题目、分类、标签等条件查询*/
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            /*处理动态查询条件*/
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (blog.getTitle() != null && !blog.getTitle().equals("")) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }

                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }

                if (blog.getRecommended() != null && blog.getRecommended()) {
                    predicates.add(cb.<Boolean>equal(root.get("recommended"), blog.getRecommended()));
                }

                cq.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        }, pageable);
    }

    /*分页查询*/
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    /*全局搜索*/
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    /*根据tag查询博客*/
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll((Specification<Blog>) (root, cq, cb) -> {
            Join join = root.join("tags");
            return cb.equal(join.get("id"), tagId);
        }, pageable);
    }

    /*按更新时间查询博客*/
    @Override
    public List<Blog> listRecommendTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {/*新增*//*todo，不晓得可以不可以*/
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setCountView(0);
        }
        else {/*修改*/
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);

    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }


    /*得到所有的年份，同时按年份分类博客*/
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();/*查询年份*/
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));/*查询年份对应的博客*/
        }
        return map;
    }

    /*博客总数*/
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
