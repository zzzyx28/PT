package com.example.test1.controller;

import com.example.test1.entity.Ranking;
import com.example.test1.entity.Torrent;
import com.example.test1.entity.User;
import com.example.test1.mapper.RankingMapper;
import com.example.test1.service.RankingService;
import com.example.test1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private UserService userService;

    // 获取按等级排行的用户
    @GetMapping("/top-magic_value")
    public ResponseEntity<List<Ranking>> getTopUsersByLevel() {
        List<Ranking> rankings = rankingService.getTopUsersByLevel();
        return ResponseEntity.ok(rankings);
    }

    // 获取按下载量排行的用户
    @GetMapping("/top-download")
    public ResponseEntity<List<Ranking>> getTopUsersByDownloadCount() {
        List<Ranking> rankings = rankingService.getTopUsersByDownloadCount();
        return ResponseEntity.ok(rankings);
    }
    @GetMapping("/topTorrent-download")
    public ResponseEntity<List<Ranking>> getTopTorrentsByDownload() {
        List<Ranking> rankings = rankingService.getTopTorrentsByDownload();
        return ResponseEntity.ok(rankings);
    }
}
