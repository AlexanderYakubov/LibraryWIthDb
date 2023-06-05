package com.example.demo;

import javax.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bio;
    private String image;

    // getters and setters
    public Long getId() {
    	return id;
    }
    public void setId(Long id) {
    	this.id = id;
    }
    public String getBio() {
    	return bio;
    }
    public void setBio(String bio) {
    	this.bio = bio;
    }
}
