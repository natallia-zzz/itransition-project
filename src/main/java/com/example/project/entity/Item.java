package com.example.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Entity
@Indexed
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @FullTextField
    @Column(nullable = false, length = 45)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER,cascade  = CascadeType.ALL)
    @JoinTable(
            name = "item_tags",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(optional = false)
    @IndexedEmbedded
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    @Column
    private String textField1;

    @Column
    private String textField2;

    @Column
    private String textField3;

    @Column
    private String stringField1;

    @Column
    private String stringField2;

    @Column
    private String stringField3;

    @Column
    private float numberField1;

    @Column
    private float numberField2;

    @Column
    private float numberField3;

    @Column
    private Date dateField1;

    @Column
    private Date dateField2;

    @Column
    private Date dateField3;

    @Column
    private Boolean booleanField1 = false;

    @Column
    private Boolean booleanField2 = false;

    @Column
    private Boolean booleanField3 = false;

    public Item(){};

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<String> getTagNames(){
        Set<String> tagNames = new HashSet<>();
        for(Tag tag:tags){
            tagNames.add(tag.getName());
        }
        return tagNames;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public String getTextField1() {
        return textField1;
    }

    public void setTextField1(String textField1) {
        this.textField1 = textField1;
    }

    public String getTextField2() {
        return textField2;
    }

    public void setTextField2(String textField2) {
        this.textField2 = textField2;
    }

    public String getTextField3() {
        return textField3;
    }

    public void setTextField3(String textField3) {
        this.textField3 = textField3;
    }

    public String getStringField1() {
        return stringField1;
    }

    public void setStringField1(String stringField1) {
        this.stringField1 = stringField1;
    }

    public String getStringField2() {
        return stringField2;
    }

    public void setStringField2(String stringField2) {
        this.stringField2 = stringField2;
    }

    public String getStringField3() {
        return stringField3;
    }

    public void setStringField3(String stringField3) {
        this.stringField3 = stringField3;
    }

    public float getNumberField1() {
        return numberField1;
    }

    public void setNumberField1(float numberField1) {
        this.numberField1 = numberField1;
    }

    public float getNumberField2() {
        return numberField2;
    }

    public void setNumberField2(float numberField2) {
        this.numberField2 = numberField2;
    }

    public float getNumberField3() {
        return numberField3;
    }

    public void setNumberField3(float numberField3) {
        this.numberField3 = numberField3;
    }

    public Date getDateField1() {
        return dateField1;
    }

    public void setDateField1(Date dateField1) {
        this.dateField1 = dateField1;
    }

    public Date getDateField2() {
        return dateField2;
    }

    public void setDateField2(Date dateField2) {
        this.dateField2 = dateField2;
    }

    public Date getDateField3() {
        return dateField3;
    }

    public void setDateField3(Date dateField3) {
        this.dateField3 = dateField3;
    }

    public Boolean getBooleanField1() {
        return booleanField1;
    }

    public void setBooleanField1(Boolean booleanField1) {
        this.booleanField1 = booleanField1;
    }

    public Boolean getBooleanField2() {
        return booleanField2;
    }

    public void setBooleanField2(Boolean booleanField2) {
        this.booleanField2 = booleanField2;
    }

    public Boolean getBooleanField3() {
        return booleanField3;
    }

    public void setBooleanField3(Boolean booleanField3) {
        this.booleanField3 = booleanField3;
    }
}
