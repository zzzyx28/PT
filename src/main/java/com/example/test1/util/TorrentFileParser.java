package com.example.test1.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
        BencodeParser parser = new BencodeParser(new ByteArrayInputStream(torrentData));
        Map<String, Object> root = (Map<String, Object>) parser.parse();
        Map<String, Object> info = (Map<String, Object>) root.get("info");

        // 计算 info_hash (SHA-1 hash of the info dictionary)
        byte[] infoBytes = bencodeEncode(info);
        byte[] infoHash = BencodeParser.calculateSHA1(infoBytes);

        TorrentMeta meta = new TorrentMeta();
        meta.setInfoHash(infoHash);
        meta.setName((String) info.get("name"));

        // 计算总大小和文件数量
        if (info.containsKey("length")) {
            // 单文件
            meta.setTotalSize((Long) info.get("length"));
            meta.setFileCount(1);
            meta.setSingleFile(true);
        } else {
            // 多文件
            List<Map<String, Object>> files = (List<Map<String, Object>>) info.get("files");
            long totalSize = files.stream()
                    .mapToLong(file -> (Long) file.get("length"))
                    .sum();
            meta.setTotalSize(totalSize);
            meta.setFileCount(files.size());
            meta.setSingleFile(false);
        }

        return meta;
    }

    // 简单的 Bencode 编码实现（仅用于编码 info 字典）
    private static byte[] bencodeEncode(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("d");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key.length()).append(":").append(key);
            if (value instanceof Long) {
                sb.append("i").append(value).append("e");
            } else if (value instanceof String) {
                String s = (String) value;
                sb.append(s.length()).append(":").append(s);
            } else if (value instanceof byte[]) {
                byte[] bytes = (byte[]) value;
                sb.append(bytes.length).append(":").append(new String(bytes, StandardCharsets.ISO_8859_1));
            } else if (value instanceof List) {
                sb.append("l");
                for (Object item : (List<?>) value) {
                    if (item instanceof Map) {
                        sb.append(new String(bencodeEncode((Map<String, Object>) item), StandardCharsets.ISO_8859_1));
                    }
                }
                sb.append("e");
            } else if (value instanceof Map) {
                sb.append(new String(bencodeEncode((Map<String, Object>) value), StandardCharsets.ISO_8859_1));
            }
        }
        sb.append("e");
        return sb.toString().getBytes(StandardCharsets.ISO_8859_1);
    }
}