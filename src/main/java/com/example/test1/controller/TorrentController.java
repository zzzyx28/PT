package com.example.test1.controller;

import com.example.test1.entity.Torrent;
import com.example.test1.service.TorrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/torrents")
public class TorrentController {

    @Autowired
    private TorrentService torrentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadTorrent(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", required = false) Integer category,
            @RequestParam(value = "description", required = false) String description,
            // 其他需要的参数...
            Principal principal) { // 获取当前登录用户信息
        try {
            // 调用解析服务
            Torrent torrent = torrentService.parseAndSaveTorrent(file, category, description, principal);
            return ResponseEntity.ok(torrent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件读取失败");
        } catch (BencodeException e) {
            return ResponseEntity.badRequest().body("无效的种子文件格式");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("服务器错误");
        }
    }
}