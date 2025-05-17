package com.example.test1.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TorrentFileParser {

    public static class TorrentMeta {
        private byte[] infoHash;
        private String name;
        private long totalSize;
        private int fileCount;
        private boolean singleFile;

        // Getters and setters
        public byte[] getInfoHash() { return infoHash; }
        public void setInfoHash(byte[] infoHash) { this.infoHash = infoHash; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getTotalSize() { return totalSize; }
        public void setTotalSize(long totalSize) { this.totalSize = totalSize; }
        public int getFileCount() { return fileCount; }
        public void setFileCount(int fileCount) { this.fileCount = fileCount; }
        public boolean isSingleFile() { return singleFile; }
        public void setSingleFile(boolean singleFile) { this.singleFile = singleFile; }
    }

    public static TorrentMeta parse(byte[] torrentData) throws IOException {
        // 1. 解析整个文件
        BencodeParser parser = new BencodeParser(new ByteArrayInputStream(torrentData));
        Map<String, Object> root = (Map<String, Object>) parser.parse();
        Map<String, Object> info = (Map<String, Object>) root.get("info");

        // 2. 提取 info 字节范围
        int infoStart = indexOf(torrentData, "4:info".getBytes(StandardCharsets.ISO_8859_1)) + 6;
        int infoEnd = findEndOfBencode(torrentData, infoStart);
        byte[] infoBytes = Arrays.copyOfRange(torrentData, infoStart, infoEnd);

        // 3. 计算 infoHash
        byte[] infoHash = BencodeParser.calculateSHA1(infoBytes);

        // 4. 提取基础信息
        TorrentMeta meta = new TorrentMeta();
        meta.setInfoHash(infoHash);
        meta.setName((String) info.get("name"));

        // 5. 文件信息
        if (info.containsKey("length")) {
            meta.setTotalSize((Long) info.get("length"));
            meta.setFileCount(1);
            meta.setSingleFile(true);
        } else if (info.containsKey("files")) {
            List<Map<String, Object>> files = (List<Map<String, Object>>) info.get("files");
            long totalSize = 0;
            for (Map<String, Object> file : files) {
                totalSize += (Long) file.get("length");
            }
            meta.setTotalSize(totalSize);
            meta.setFileCount(files.size());
            meta.setSingleFile(false);
        }

        return meta;
    }

    // 在 data 中查找 pattern 的起始位置
    private static int indexOf(byte[] data, byte[] pattern) {
        outer:
        for (int i = 0; i <= data.length - pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    // 从某个位置开始，找到完整 Bencode 的结束位置（只针对 info 字典）
    private static int findEndOfBencode(byte[] data, int start) {
        int i = start;
        int depth = 0;
        while (i < data.length) {
            byte b = data[i];
            if (b == 'd' || b == 'l') {
                depth++;
                i++;
            } else if (b == 'e') {
                depth--;
                i++;
                if (depth == 0) break;
            } else if (b == 'i') {
                i++;
                while (data[i] != 'e') i++;
                i++; // consume 'e'
            } else if (b >= '0' && b <= '9') {
                int len = 0;
                while (data[i] >= '0' && data[i] <= '9') {
                    len = len * 10 + (data[i] - '0');
                    i++;
                }
                i++; // skip ':'
                i += len; // skip string content
            } else {
                throw new RuntimeException("Unknown bencode format at: " + i);
            }
        }
        return i;
    }
}
