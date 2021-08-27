package com.example.project.entity;

import com.example.project.HtmlMarcdown;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;


import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


import javax.persistence.*;
import java.util.Set;

@Entity
@Indexed
@Table(name = "collections")
public class Collection{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String name;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    private Set<Item> items;

    @ManyToOne(fetch = FetchType.LAZY, optional = false,cascade  = {CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "user_id",nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = true,cascade = CascadeType.DETACH)
    @JoinColumn(name = "topic_id",nullable = true)
    private Topic topic;

    @FullTextField
    @Column
    private String description;


    public Collection(){}
    public Collection(String name){
        this.name = name;
    }


    public String markdownToHtml() {
        HtmlMarcdown htmlMarcdown =new HtmlMarcdown();
        return htmlMarcdown.toHtml(this.description);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public User getOwner() {
        return owner;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
