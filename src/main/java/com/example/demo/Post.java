package com.example.demo;

import javax.persistence.*;

@Entity
@Table(name = "posts")
class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    // getters and setters...
    public Long getId() {
    	return id;
    }
    public void setId(Long id) {
    	this.id = id;
    }
    public String getTitle() {
    	return title;
    }
    public void setTitle(String title) {
    	this.title = title;
    }
    public String getContent() {
    	return content;
    }
    public void setContent(String content) {
    	this.content = content;
    }
    public User getUser() {
    	return user;
    }
    public void setUser(User user) {
    	this.user = user;
    }
}