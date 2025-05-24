package com.example.test1.entity;

public class Ranking {
    private String username;
    private int level;
    private long downloadCount;

    private int magic_value;

    public int getMagic_value() {
        return magic_value;
    }

    public void setMagic_value(int magic_value) {
        this.magic_value = magic_value;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(long downloadCount) {
        this.downloadCount = downloadCount;
    }
}
