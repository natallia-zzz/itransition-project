package com.example.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "collections")
public class Collection{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String name;

    @OneToMany(mappedBy = "collection",
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Item> items;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User owner;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Collection(){}
    public Collection(String name){
        this.name = name;
    }

}
