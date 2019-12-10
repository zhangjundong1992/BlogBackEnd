package com.zjd.blog.vo;

public class BlogQuery {
    private String title;
    private Long typeId;
    private Boolean recommended;

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }
}
