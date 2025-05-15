package com.example.test1.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.test1.entity.Torrent;
import com.example.test1.entity.User;
import com.example.test1.exception.BencodeException;
import com.example.test1.exception.TorrentProcessingException;
import com.example.test1.mapper.TorrentMapper;
import com.example.test1.mapper.UserMapper;
import com.example.test1.util.TorrentFileParser;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TorrentService {

    @Autowired
    private TorrentMapper torrentMapper;

    @Autowired
    private UserMapper userMapper;

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
            saveTorrentWithUser(torrent);

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
    public Torrent buildTorrentEntity(MultipartFile file,
                                      TorrentFileParser.TorrentMeta meta,
                                      Integer category,
                                      String description,
                                      Principal principal) {
        Torrent torrent = new Torrent();

        // 设置数据库存在的字段
        torrent.setTorrentId(UUID.randomUUID().toString());
        torrent.setName(meta.getName());
        torrent.setFilename(file.getOriginalFilename());
        torrent.setTitle(meta.getName()); // 如果没有标题，用name代替
        torrent.setDescription(description);
        torrent.setStatus(Torrent.Status.CANDIDATE);
        torrent.setType(meta.isSingleFile() ? 1 : 2);
        torrent.setOwnerId(getUserIdFromPrincipal(principal));

        // 设置时间（自动转换为String）
        torrent.setCreateTime(LocalDateTime.now());
        torrent.setUpdateTime(LocalDateTime.now());

        // 初始化统计字段
        torrent.setComments(0);
        torrent.setViews(0);
        torrent.setLeechers(0);
        torrent.setSeeders(0);
        torrent.setCompletions(0);

        // 非数据库字段（可选存储到其他表或忽略）
        torrent.setInfoHash(meta.getInfoHash()); // @TableField(exist=false)
        torrent.setSize(meta.getTotalSize());    // @TableField(exist=false)
        torrent.setFileCount(meta.getFileCount());// @TableField(exist=false)

        return torrent;
    }

    /**
     * 初始化统计字段
     */
    private void initStatisticsFields(Torrent torrent) {
        torrent.setComments(0);
        torrent.setViews(0);
        torrent.setLeechers(0);
        torrent.setSeeders(0);
        torrent.setCompletions(0);
    }

    /**
     * 保存Torrent并关联User
     */
    private void saveTorrentWithUser(Torrent torrent) {
        // 1. 保存Torrent
        int affectedRows = torrentMapper.insert(torrent);
        if (affectedRows != 1) {
            throw new TorrentProcessingException("种子保存失败");
        }

        // 2. 关联User对象
        if (torrent.getOwnerId() != null) {
            Optional<User> user = userMapper.selectByUserId(torrent.getOwnerId());
            // 这里可以根据需要设置User对象到Torrent中
            // 如果Torrent类中有User字段可以设置
            // torrent.setUser(user);
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


    public List<Torrent> listTorrents(int page, int size) {
        Page<Torrent> pageRequest = new Page<>(page, size);
        Page<Torrent> resultPage = torrentMapper.selectPage(pageRequest, new QueryWrapper<>());
        return resultPage.getRecords();
    }
}