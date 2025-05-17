package com.example.test1.service;

import com.example.test1.entity.Torrent;
import com.example.test1.mapper.TorrentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TorrentPersistenceService {

    @Autowired
    private TorrentMapper torrentMapper;

    @Transactional
    public void saveTorrent(Torrent torrent) {
        int result = torrentMapper.insert(torrent);
        if (result != 1) {
            throw new RuntimeException("插入失败！");
        }
    }
}