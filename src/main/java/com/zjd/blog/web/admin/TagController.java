package com.zjd.blog.web.admin;

import com.zjd.blog.dao.po.Tag;
import com.zjd.blog.dao.po.Type;
import com.zjd.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    //标签页面
    @GetMapping("/tags")
    public String types(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    //新增标签跳转
    @GetMapping("tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tag-input";
    }

    //新增标签提交
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        /*重复校验*/
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name", "nameError", "标签已存在");
        }

        /*自动非空校验*/
        if (result.hasErrors()) {
            return "admin/tag-input";
        }

        /*保存成功与否的校验*/
        Tag tag2 = tagService.saveTag(tag);
        if (tag2 == null) {
            attributes.addFlashAttribute("message", "新增失败");
        }
        else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }

    //编辑标签
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tag-input";
    }

    //编辑标签提交
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        /*重复校验*/
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name", "nameError", "标签已存在");
        }

        /*自动非空校验*/
        if (result.hasErrors()) {
            return "admin/tag-input";
        }

        /*更新成功与否的校验*/
        Tag tag2 = tagService.updateTag(id, tag);
        if (tag2 == null) {
            attributes.addFlashAttribute("message", "更新失败");
        }
        else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }

    /*删除标签*/
    @GetMapping("tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

}
