package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.Torrent;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TorrentMapper extends BaseMapper<Torrent> {

    @Delete("DELETE FROM torrent WHERE torrent_id = #{torrentId}")
    int deleteById(String torrentId);

    @Select("SELECT * FROM torrent WHERE torrent_id = #{torrentId}")
    @Results(id = "torrentResultMap", value = {
            @Result(property = "torrentId", column = "torrent_id"),
            @Result(property = "infoHash", column = "info_hash"),
            @Result(property = "ownerId", column = "owner_id"),
            @Result(property = "fileCount", column = "file_count"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    Torrent selectById(String torrentId);

    @Select("SELECT * FROM torrent WHERE owner_id = #{ownerId} ORDER BY create_time DESC")
    @ResultMap("torrentResultMap")
    List<Torrent> selectByOwnerId(String ownerId);

    @Select("SELECT * FROM torrent WHERE info_hash = #{infoHash}")
    @ResultMap("torrentResultMap")
    Torrent selectByInfoHash(byte[] infoHash);
}
