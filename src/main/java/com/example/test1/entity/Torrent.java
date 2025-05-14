package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zyx
 * @email
 * @date 2025-04-20
 */
@Entity
@Table(name = "torrent")
public class Torrent {
    public String getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(String torrentId) {
        this.torrentId = torrentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(Integer fileStatus) {
        this.fileStatus = fileStatus;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getLeechers() {
        return leechers;
    }

    public void setLeechers(Integer leechers) {
        this.leechers = leechers;
    }

    public Integer getSeeders() {
        return seeders;
    }

    public void setSeeders(Integer seeders) {
        this.seeders = seeders;
    }

    public Integer getCompletions() {
        return completions;
    }

    public void setCompletions(Integer completions) {
        this.completions = completions;
    }

    /**
     *
     */
    @Id
    private String torrentId;
    /**
     * 种子哈希
     */
    private byte[] infoHash;
    /**
     * 名称
     */
    private String name;
    /**
     * 上传文件名
     */
    private String filename;
    /**
     * 标题
     */
    private String title;
    /**
     * 简介副标题
     */
    private String subheading;
    /**
     * 封面
     */
    private String cover;
    /**
     * 描述
     */
    private String description;

    /**
     * 类别
     */
    private Integer category;

    /**
     * 状态
     *
     * @see Status
     */
    private Integer status;

    /**
     * 文件状态 0 未上传 1 已上传
     */
    private Integer fileStatus;
    /**
     * 审核人
     */
    private Integer reviewer;


    /**
     * 添加日期
     */
    private LocalDateTime createTime;


    /**
     * 修改日期
     */
    private LocalDateTime updateTime;

    /**
     * 拥有者
     */
    private String ownerId;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 类型
     * single(1)
     * multi(2)
     */
    private Integer type;
    /**
     * 文件数量
     */
    private Integer fileCount;

    /**
     * 评论数
     */
    private Integer comments;
    /**
     * 浏览次数
     */
    private Integer views;
    /**
     * 点击次数
     */
    private Integer hits;

    /**
     * 可见性
     */
    private Integer visible;

    /**
     * 是否匿名
     */
    private Integer anonymous;


    /**
     * 下载数
     */
    private Integer leechers;
    /**
     * 做种数
     */
    private Integer seeders;

    /**
     * 完成次数
     */
    private Integer completions;

    /**
     *
     */
    private String remark;

    /**
     * 种子状态
     * 0 候选中 1 已发布 2 审核不通过 3 已上架修改重审中 10 已下架
     */
    public interface Status {

        int CANDIDATE = 0;

        int PUBLISHED = 1;

        int AUDIT_NOT_PASSED = 2;

        int RETRIAL = 3;

        int REMOVED = 10;

    }

//    @RequiredArgsConstructor
//    public enum Type {
//        single(1),
//        multi(2);
//
//        @Getter
//        @EnumValue
//        private final int code;
//    }

    public boolean isStatusOK() {


        return status == 1;
    }

}

