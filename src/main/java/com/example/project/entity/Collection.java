package com.example.project.entity;

import com.example.project.HtmlMarcdown;
import lombok.Getter;
import lombok.Setter;

import lombok.With;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.sql.Timestamp;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
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

    @Column
    private String textfield1;

    @Column
    private String textfield2;

    @Column
    private String textfield3;

    @Column
    private String stringfield1;

    @Column
    private String stringfield2;

    @Column
    private String stringfield3;

    @Column
    private String numberfield1;

    @Column
    private String numberfield2;

    @Column
    private String numberfield3;

    @Column
    private String datefield1;

    @Column
    private String datefield2;

    @Column
    private String datefield3;

    @Column
    private String booleanfield1;

    @Column
    private String booleanfield2;

    @Column
    private String booleanfield3;

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

    public boolean checkTextfield1(){
        return textfield1 != null && !textfield1.isEmpty();
    }

    public boolean checkTextfield2(){
        return textfield2 != null && !textfield2.isEmpty();
    }

    public boolean checkTextfield3(){
        return textfield3 != null && !textfield3.isEmpty();
    }

    public boolean checkStringfield1(){
        return stringfield1 != null && !stringfield1.isEmpty();
    }

    public boolean checkStringfield2(){
        return stringfield2!= null && !stringfield2.isEmpty();
    }

    public boolean checkStringfield3(){
        return stringfield3!= null && !stringfield3.isEmpty();
    }
    public boolean checkNumberfield1(){
        return numberfield1 != null && !numberfield1.isEmpty();
    }

    public boolean checkNumberfield2(){
        return numberfield2 != null && !numberfield2.isEmpty();
    }

    public boolean checkNumberfield3(){
        return numberfield3 != null && !numberfield3.isEmpty();
    }

    public boolean checkDatefield1(){
        return datefield1 != null && !datefield1.isEmpty();
    }

    public boolean checkDatefield2(){
        return datefield2!= null && !datefield2.isEmpty();
    }

    public boolean checkDatefield3(){
        return datefield3!= null && !datefield3.isEmpty();
    }
    public boolean checkBooleanfield1(){
        return booleanfield1 != null && !booleanfield1.isEmpty();
    }

    public boolean checkBooleanfield2(){
        return booleanfield2!= null && !booleanfield2.isEmpty();
    }

    public boolean checkBooleanfield3(){
        return booleanfield3!= null && !booleanfield3.isEmpty();
    }

    public String getTextfield1() {
        return textfield1;
    }

    public void setTextfield1(String textfield1) { this.textfield1 = textfield1; }

    public String getTextfield2() {
        return textfield2;
    }

    public void setTextfield2(String textfield2) { this.textfield2 = textfield2; }

    public String getTextfield3() {
        return textfield3;
    }

    public void setTextfield3(String textfield3) { this.textfield3 = textfield3; }

    public String getStringfield1() {
        return stringfield1;
    }

    public void setStringfield1(String stringfield1) { this.stringfield1 = stringfield1; }

    public String getStringfield2() {
        return stringfield2;
    }

    public void setStringfield2(String stringfield2) { this.stringfield2 = stringfield2; }

    public String getStringfield3() {
        return stringfield3;
    }

    public void setStringfield3(String stringfield3) { this.stringfield3 = stringfield3; }

    public String getNumberfield1() {
        return numberfield1;
    }

    public void setNumberfield1(String numberfield1) { this.numberfield1 = numberfield1; }

    public String getNumberfield2() {
        return numberfield2;
    }

    public void setNumberfield2(String numberfield2) { this.numberfield2 = numberfield2; }

    public String getNumberfield3() {
        return numberfield3;
    }

    public void setNumberfield3(String numberfield3) { this.numberfield3 = numberfield3; }

    public String getDatefield1() {
        return datefield1;
    }

    public void setDatefield1(String datefield1) { this.datefield1 = datefield1; }

    public String getDatefield2() {
        return datefield2;
    }

    public void setDatefield2(String datefield2) { this.datefield2 = datefield2; }

    public String getDatefield3() { return datefield3; }

    public void setDatefield3(String datefield3) { this.datefield3 = datefield3; }

    public String getBooleanfield1() {
        return booleanfield1;
    }

    public void setBooleanfield1(String booleanfield1) { this.booleanfield1 = booleanfield1; }

    public String getBooleanfield2() {
        return booleanfield2;
    }

    public void setBooleanfield2(String booleanfield2) { this.booleanfield2 = booleanfield2; }

    public String getBooleanfield3() {
        return booleanfield3;
    }

    public void setBooleanfield3(String booleanfield3) { this.booleanfield3 = booleanfield3; }

    public void updateItems(){
        if(!checkTextfield1()&&items!=null){
            for(Item item: items){
                item.setTextField1(null);
            }
        }
        if(!checkTextfield2()&&items!=null){
            for(Item item: items){
                item.setTextField2(null);
            }
        }
        if(!checkTextfield3()&&items!=null){
            for(Item item: items){
                item.setTextField3(null);
            }
        }
        if(!checkStringfield1()&&items!=null){
            for(Item item: items){
                item.setStringField1(null);
            }
        }
        if(!checkStringfield2()&&items!=null){
            for(Item item: items){
                item.setStringField2(null);
            }
        }
        if(!checkStringfield3()&&items!=null){
            for(Item item: items){
                item.setStringField3(null);
            }
        }
        if(!checkNumberfield1()&&items!=null){
            for(Item item: items){
                item.setNumberField1(0);
            }
        }
        if(!checkNumberfield2()&&items!=null){
            for(Item item: items){
                item.setNumberField2(0);
            }
        }
        if(!checkNumberfield3()&&items!=null){
            for(Item item: items){
                item.setNumberField3(0);
            }
        }
        if(!checkBooleanfield1()&&items!=null){
            for(Item item: items){
                item.setBooleanField1(false);
            }
        }
        if(!checkBooleanfield2()&&items!=null){
            for(Item item: items){
                item.setBooleanField2(false);
            }
        }
        if(!checkBooleanfield3()&&items!=null){
            for(Item item: items){
                item.setBooleanField3(false);
            }
        }

        if(!checkDatefield1()&&items!=null){
            for(Item item: items){
                item.setDateField1(new java.sql.Date(System.currentTimeMillis()));
            }
        }
        if(!checkDatefield2()&&items!=null){
            for(Item item: items){
                item.setDateField2(new java.sql.Date(System.currentTimeMillis()));
            }
        }
        if(!checkDatefield3()&&items!=null){
            for(Item item: items){
                item.setDateField3(new java.sql.Date(System.currentTimeMillis()));
            }
        }
    }
}
