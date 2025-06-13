// CreateForumRequest.java
package com.example.test1.entity;

import com.example.test1.entity.Forum;

public class CreateForumRequest {
    private String ownerId;
    private Forum forum;

    // Getters and Setters
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }
}
