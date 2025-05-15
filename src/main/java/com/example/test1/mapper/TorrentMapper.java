package com.example.test1.mapper;

import com.example.test1.entity.Torrent;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TorrentMapper {

    @Insert("INSERT INTO torrent (" +
            "torrent_id, info_hash, name, filename, title, subheading, cover, description, " +
            "category, status, file_status, reviewer, create_time, update_time, owner_id, " +
            "size, type, file_count, comments, views, hits, visible, anonymous, " +
            "leechers, seeders, completions, remark" +
            ") VALUES (" +
            "#{torrentId}, #{infoHash}, #{name}, #{filename}, #{title}, #{subheading}, #{cover}, #{description}, " +
            "#{category}, #{status}, #{fileStatus}, #{reviewer}, #{createTime}, #{updateTime}, #{ownerId}, " +
            "#{size}, #{type}, #{fileCount}, #{comments}, #{views}, #{hits}, #{visible}, #{anonymous}, " +
            "#{leechers}, #{seeders}, #{completions}, #{remark}" +
            ")")
    @Options(useGeneratedKeys = false, keyProperty = "torrentId")
    int insert(Torrent torrent);

    @Update("UPDATE torrent SET " +
            "name = #{name}, filename = #{filename}, title = #{title}, subheading = #{subheading}, " +
            "cover = #{cover}, description = #{description}, category = #{category}, status = #{status}, " +
            "file_status = #{fileStatus}, reviewer = #{reviewer}, update_time = #{updateTime}, " +
            "owner_id = #{ownerId}, size = #{size}, type = #{type}, file_count = #{fileCount}, " +
            "comments = #{comments}, views = #{views}, hits = #{hits}, visible = #{visible}, " +
            "anonymous = #{anonymous}, leechers = #{leechers}, seeders = #{seeders}, " +
            "completions = #{completions}, remark = #{remark} " +
            "WHERE torrent_id = #{torrentId}")
    int update(Torrent torrent);

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
            // 其他字段自动映射（名称相同）
    })
    Torrent selectById(String torrentId);

    @Select("SELECT * FROM torrent WHERE owner_id = #{ownerId} ORDER BY create_time DESC")
    @ResultMap("torrentResultMap")
    List<Torrent> selectByOwnerId(String ownerId);

    @Select("SELECT * FROM torrent WHERE info_hash = #{infoHash}")
    @ResultMap("torrentResultMap")
    Torrent selectByInfoHash(byte[] infoHash);
}