package com.example.test1.service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.test1.entity.Torrent;
import com.example.test1.entity.User;
import com.example.test1.exception.BencodeException;
import com.example.test1.exception.TorrentProcessingException;
import com.example.test1.mapper.TorrentMapper;
import com.example.test1.mapper.UserMapper;
import com.example.test1.util.TorrentFileParser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TorrentService {
    // 硬编码存储路径 (可根据需要修改)
    private static final String TORRENT_STORAGE_PATH = "./torrent-storage";
    private final Path storagePath = Paths.get(TORRENT_STORAGE_PATH);

    @Autowired
    private TorrentPersistenceService torrentPersistenceService;
    @Autowired
    private TorrentMapper torrentMapper;
    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void init() throws IOException {
        // 确保存储目录存在
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
    }

    public Resource downloadTorrent(String torrentId) throws IOException {
        // 1. 查询种子信息
        Torrent torrent = torrentMapper.selectById(torrentId);
        if (torrent == null) {
            throw new TorrentProcessingException("种子不存在");
        }

        // 2. 构建文件路径
        Path filePath = Paths.get(torrent.getStoragePath());
        if (!Files.exists(filePath)) {
            throw new TorrentProcessingException("种子文件不存在");
        }

        // 3. 返回可下载的资源
        torrent.setCompletions(torrent.getCompletions() + 1);
        torrentMapper.incrementCompletions(torrentId);
        return new InputStreamResource(Files.newInputStream(filePath));
    }

    public String getTorrentContentType() {
        return "application/x-bittorrent"; // 标准种子文件类型
    }
    /**
     * 处理种子上传并保存到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public Torrent uploadAndSaveTorrent(MultipartFile file,
                                        Integer category,
                                        String description,
                                        Principal principal,
                                        String uid) {
        try {
            // 1. 验证文件
            validateFile(file);

            // 2. 解析种子文件
            TorrentFileParser.TorrentMeta meta = parseTorrentFile(file);

            // 3. 保存文件到本地存储
            String path=saveTorrentFile(file);

            // 4. 构建并保存Torrent实体
            System.out.println(path);
            Torrent torrent = buildTorrentEntity(file, meta, category, description, principal,path,uid);
            torrentPersistenceService.saveTorrent(torrent);
            return torrent;
        } catch (IOException e) {
            throw new TorrentProcessingException("种子文件处理失败", e);
        } catch (BencodeException e) {
            throw new TorrentProcessingException("无效的种子文件格式", e);
        }
    }

    /**
     * 验证上传文件
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        if (!file.getOriginalFilename().toLowerCase().endsWith(".torrent")) {
            throw new IllegalArgumentException("仅支持.torrent文件");
        }
    }

    /**
     * 保存种子文件到本地存储
     */
    private String saveTorrentFile(MultipartFile file) throws IOException {
        // 生成唯一文件名（避免冲突）
        String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetPath = storagePath.resolve(uniqueFilename);

        Files.copy(file.getInputStream(), targetPath);

        // 返回相对路径或绝对路径（根据需求选择）
        return targetPath.toString(); // 返回绝对路径
        // 或者 return uniqueFilename; // 只返回文件名
    }

    /**
     * 解析种子文件元数据
     */
    private TorrentFileParser.TorrentMeta parseTorrentFile(MultipartFile file)
            throws IOException, BencodeException {
        return TorrentFileParser.parse(file.getBytes());
    }

    /**
     * 构建Torrent实体对象
     */
    public Torrent buildTorrentEntity(MultipartFile file,
                                      TorrentFileParser.TorrentMeta meta,
                                      Integer category,
                                      String description,
                                      Principal principal,
                                      String storagePath,
                                      String uid) {
        System.out.println(storagePath);
        Torrent torrent = new Torrent();

        // 设置基础信息
        torrent.setTorrentId(UUID.randomUUID().toString());
        torrent.setName(meta.getName());
        torrent.setFilename(file.getOriginalFilename());
        torrent.setTitle(meta.getName());
        torrent.setDescription(description);
        torrent.setCategory(category);
        torrent.setStoragePath(storagePath);
        System.out.println(torrent.getStoragePath());

        // 设置状态信息
        torrent.setStatus(Torrent.Status.CANDIDATE);
        torrent.setType(meta.isSingleFile() ? 1 : 2);
        torrent.setOwnerId(uid);
        System.out.println(torrent.getOwnerId());

        // 设置文件信息
        torrent.setSize(meta.getTotalSize());
        torrent.setFileCount(meta.getFileCount());
        torrent.setInfoHash(meta.getInfoHash());

        // 设置时间戳
        torrent.setCreateTime(LocalDateTime.now());
        torrent.setUpdateTime(LocalDateTime.now());

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
        torrent.setLeechers(0);
        torrent.setSeeders(0);
        torrent.setCompletions(0);
    }

    @Transactional
   public void saveTorrentWithUser(Torrent torrent) {
        // 1. 保存Torrent
        int affectedRows = torrentMapper.insert(torrent);
        if (affectedRows != 1) {
            throw new TorrentProcessingException("种子保存失败");
        }

        // 2. 关联User对象
        if (torrent.getOwnerId() != null) {
            User user = userMapper.selectByUserId(torrent.getOwnerId());
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

    public Torrent getTorrentById(String torrentId) {
        // 使用MyBatis-Plus的selectById方法
        Torrent torrent = torrentMapper.selectById(torrentId);

        // 可选：增加统计字段（如浏览次数+1）
        if (torrent != null) {
            torrent.setViews(torrent.getViews() + 1);
            torrentMapper.incrementViews(torrentId);
        }

        return torrent;
    }

    public Page<Torrent> listByOwnerId(String ownerId, int pageNum, int pageSize) {
        Page<Torrent> page = new Page<>(pageNum, pageSize);
        return torrentMapper.selectByOwnerId(page, ownerId);
    }

    public List<Torrent> listByOwnerWithCondition(String ownerId, String category, String orderBy) {
        return torrentMapper.selectByOwnerIdXml(ownerId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTorrent(String torrentId) {
        Torrent torrent = torrentMapper.selectById(torrentId);
        if (torrent == null) {
            throw new TorrentProcessingException("种子不存在");
        }

        // 1. 删除本地文件
        Path filePath = Paths.get(torrent.getStoragePath());
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new TorrentProcessingException("无法删除种子文件", e);
            }
        }

        // 删除数据库记录
        int rowsAffected = torrentMapper.deleteById(torrentId);
        if (rowsAffected != 1) {
            throw new TorrentProcessingException("种子删除失败");
        }
    }

}