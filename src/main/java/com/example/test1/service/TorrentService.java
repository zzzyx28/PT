package com.example.test1.service;

import com.example.test1.entity.Torrent;
import com.example.test1.entity.Owner;
import com.example.test1.exception.TorrentProcessingException;
import com.example.test1.mapper.TorrentMapper;
import com.example.test1.mapper.OwnerMapper;
import com.example.test1.util.TorrentFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TorrentService {

    @Autowired
    private TorrentMapper torrentMapper;

    @Autowired
    private OwnerMapper ownerMapper;

    /**
     * 处理种子上传并保存到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public Torrent uploadAndSaveTorrent(MultipartFile file,
                                        Integer category,
                                        String description,
                                        Principal principal) {
        try {
            // 1. 解析种子文件
            TorrentFileParser.TorrentMeta meta = parseTorrentFile(file);

            // 2. 构建Torrent对象
            Torrent torrent = buildTorrentEntity(file, meta, category, description, principal);

            // 3. 保存到数据库
            saveTorrentWithOwner(torrent);

            return torrent;
        } catch (IOException e) {
            throw new TorrentProcessingException("种子文件读取失败", e);
        } catch (BencodeException e) {
            throw new TorrentProcessingException("无效的种子文件格式", e);
        }
    }

    /**
     * 解析种子文件元数据
     */
    private TorrentFileParser.TorrentMeta parseTorrentFile(MultipartFile file)
            throws IOException, BencodeException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        return TorrentFileParser.parse(file.getBytes());
    }

    /**
     * 构建Torrent实体对象
     */
    private Torrent buildTorrentEntity(MultipartFile file,
                                       TorrentFileParser.TorrentMeta meta,
                                       Integer category,
                                       String description,
                                       Principal principal) {
        Torrent torrent = new Torrent();

        // 基础信息
        torrent.setTorrentId(UUID.randomUUID().toString());
        torrent.setInfoHash(meta.getInfoHash());
        torrent.setName(meta.getName());
        torrent.setFilename(file.getOriginalFilename());
        torrent.setDescription(description);

        // 分类与状态
        torrent.setCategory(category);
        torrent.setStatus(Torrent.Status.CANDIDATE);
        torrent.setFileStatus(0); // 0=未上传

        // 文件信息
        torrent.setSize(meta.getTotalSize());
        torrent.setFileCount(meta.getFileCount());
        torrent.setType(meta.isSingleFile() ? 1 : 2);

        // 时间信息
        torrent.setCreateTime(LocalDateTime.now());
        torrent.setUpdateTime(LocalDateTime.now());

        // 用户关联
        String userId = getUserIdFromPrincipal(principal);
        torrent.setOwnerId(userId);

        // 初始化统计字段
        initStatisticsFields(torrent);

        return torrent;
    }

    /**
     * 初始化统计字段
     */
    private void initStatisticsFields(Torrent torrent) {
        torrent.setComments(0);
        torrent.setViews(0);
        torrent.setHits(0);
        torrent.setLeechers(0);
        torrent.setSeeders(0);
        torrent.setCompletions(0);
    }

    /**
     * 保存Torrent并关联Owner
     */
    private void saveTorrentWithOwner(Torrent torrent) {
        // 1. 保存Torrent
        int affectedRows = torrentMapper.insert(torrent); // 使用MyBatis-Plus的insert方法
        if (affectedRows != 1) {
            throw new TorrentProcessingException("种子保存失败");
        }

        // 2. 关联Owner对象
        if (torrent.getOwnerId() != null) {
            Owner owner = ownerMapper.selectById(torrent.getOwnerId());
            torrent.setOwner(owner);
        }
    }

    /**
     * 从Principal中提取用户ID
     */
    private String getUserIdFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("未获取到用户身份信息");
        }
        // 根据你的认证系统实现调整
        return principal.getName(); // 示例：假设principal.getName()返回用户ID
    }
}