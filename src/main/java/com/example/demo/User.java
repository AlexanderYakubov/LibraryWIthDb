package com.example.demo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// cущность - entity
// таблица - table
// schema
@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // getters and setters
    public Long getId() {
    	return id;
    }
    public void setId(Long id) {
    	this.id = id;
    }

    public String getName() {
    	return name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public String getEmail() {
    	return email;
    }

    public void setEmail(String email) {
    	this.email = email;
    }

    public Profile getProfile() {
    	return profile;
    }

    public void setProfile(Profile profile) {
    	this.profile = profile;
    }

    public List<Post> getPosts() {
    	return posts;
    }

    public void setPosts(List<Post> posts) {
    	this.posts = posts;
    }
}
