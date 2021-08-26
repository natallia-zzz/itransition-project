package com.example.project.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.engine.backend.types.TermVector;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.jboss.logging.annotations.Field;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @IndexedEmbedded
    @JoinColumn(name = "user_id",nullable = false)
    private User owner;

    @Field(termVector = TermVector.YES)
    private String description;


    public Collection(){}
    public Collection(String name){
        this.name = name;
    }

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Set<Item> getItems() {
//        return items;
//    }
//
//    public void setItems(Set<Item> items) {
//        this.items = items;
//    }
//
//    public User getOwner() {
//        return owner;
//    }
//
//    public void setOwner(User owner) {
//        this.owner = owner;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
