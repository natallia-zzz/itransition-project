package com.example.project.entity;

import com.example.project.HtmlMarcdown;

import javax.persistence.*;

@Entity
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
    private String content;

    public Comment(){}
    public Comment(String content){
        this.content = content;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public String markdownToHtml() {
        HtmlMarcdown htmlMarcdown =new HtmlMarcdown();
        return htmlMarcdown.toHtml(this.content);
    }
}
