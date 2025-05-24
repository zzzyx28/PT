package com.example.test1.service;

import com.example.test1.entity.Ranking;
import com.example.test1.mapper.RankingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    @Autowired
    private RankingMapper rankingMapper;

    // 获取按等级排行的用户
    public List<Ranking> getTopUsersByLevel() {
        return rankingMapper.findTopUsersByLevel();
    }

    // 获取按下载量排行的用户
    public List<Ranking> getTopUsersByDownloadCount() {
        return rankingMapper.findTopUsersByDownloadCount();
    }

    public List<Ranking> getTopTorrentsByDownload() {
        return rankingMapper.findTopTorrentsByDownload();
    }
}
