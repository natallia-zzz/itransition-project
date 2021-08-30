package com.example.project.entity;

import com.example.project.HtmlMarcdown;

import javax.persistence.*;
import java.sql.Timestamp;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Entity
@Indexed
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(nullable = false,length = 500)
    @FullTextField
    private String content;

    public Comment(){}
    public Comment(String content){
        this.content = content;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false,cascade = {CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @ManyToOne(optional = false,cascade  = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @IndexedEmbedded(includeDepth = 2,includePaths = {"collection.description", "name"})
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column
    private java.sql.Timestamp creationDate;

    public String markdownToHtml() {
        HtmlMarcdown htmlMarcdown =new HtmlMarcdown();
        return htmlMarcdown.toHtml(this.content);
    }
}
