package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.test1.entity.Torrent;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TorrentMapper extends BaseMapper<Torrent> {

    @Delete("DELETE FROM torrent WHERE torrent_id = #{torrentId}")
    int deleteById(String torrentId);

    @Select("SELECT * FROM torrent WHERE torrentId = #{torrentId}")
    @Results(id = "torrentResultMap", value = {
            @Result(property = "torrentId", column = "torrent_id"),
            @Result(property = "infoHash", column = "info_hash"),
            @Result(property = "ownerId", column = "owner_id"),
            @Result(property = "fileCount", column = "file_count"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "storagePath", column = "storage_path")
    })
    Torrent selectById(String torrentId);

    @Select("SELECT * FROM torrent WHERE owner_id = #{ownerId} ORDER BY create_time DESC")
    @ResultMap("torrentResultMap")
    List<Torrent> selectByOwnerId(String ownerId);

    @Select("SELECT * FROM torrent WHERE info_hash = #{infoHash}")
    @ResultMap("torrentResultMap")
    Torrent selectByInfoHash(byte[] infoHash);

    @Update("UPDATE torrent SET views = views + 1 WHERE torrentId = #{torrentId}")
    int incrementViews(@Param("torrentId") String torrentId);

    @Update("UPDATE torrent SET completions = completions + 1 WHERE torrentId = #{torrentId}")
    int incrementCompletions(@Param("torrentId") String torrentId);

    Page<Torrent> selectByOwnerId(Page<Torrent> page, @Param("ownerId") String ownerId);

    /**
     * XML方式查询（可选）
     */
    List<Torrent> selectByOwnerIdXml(@Param("ownerId") String ownerId);
}
