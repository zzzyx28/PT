package com.example.test1.mapper;

import com.example.test1.entity.Ranking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RankingMapper {

    // 按用户等级排行
    @Select("SELECT username, magic_value, level FROM user ORDER BY level DESC LIMIT 10")
    List<Ranking> findTopUsersByLevel();

    // 按种子下载量排行
    @Select("SELECT t.ownerId AS userId, u.username, SUM(t.completions) AS downloadCount " +
            "FROM torrent t " +
            "JOIN user u ON t.ownerId = u.user_id " +
            "GROUP BY t.ownerId, u.username " +
            "ORDER BY downloadCount DESC LIMIT 10")
    List<Ranking> findTopUsersByDownloadCount();


    @Select("SELECT t.torrentId, t.name, t.completions AS downloadCount, u.username " +
            "FROM torrent t " +
            "JOIN user u ON t.owner_id = u.user_id " +
            "ORDER BY t.completions DESC " +
            "LIMIT 10")
    List<Ranking> findTopTorrentsByDownload();

}
