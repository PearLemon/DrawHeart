package com.lemon.drawheart.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Present implements Serializable {
    private static final long serialVersionUID = -1;
    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String content;
    @Generated(hash = 1520274012)
    public Present(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    @Generated(hash = 605316690)
    public Present() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
