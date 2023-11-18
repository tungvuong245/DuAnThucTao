package com.example.lasthope.model;

public class Chat {
    private String content;
    private String id ;
    private User user ;
    private Boolean seen ;
    private String time ;
    private String adminRep;

    public Chat() {
    }

    public Chat(String content, String id, User user, Boolean seen, String time, String adminRep) {
        this.content = content;
        this.id = id;
        this.user = user;
        this.seen = seen;
        this.time = time;
        this.adminRep = adminRep;
    }

    public String getAdminRep() {
        return adminRep;
    }

    public void setAdminRep(String adminRep) {
        this.adminRep = adminRep;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
