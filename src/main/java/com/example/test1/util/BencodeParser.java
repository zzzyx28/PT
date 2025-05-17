package com.example.test1.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class BencodeParser {
    private final InputStream in;
    private int lookahead = -1;

    public BencodeParser(InputStream in) {
        this.in = in;
    }

    public Object parse() throws IOException {
        lookahead = in.read();

        // 跳过 UTF-8 BOM（EF BB BF）
        if (lookahead == 0xEF) {
            if (in.read() == 0xBB && in.read() == 0xBF) {
                lookahead = in.read();
            } else {
                throw new IOException("Invalid BOM in file");
            }
        }

        return parseValue();
    }

    private Object parseValue() throws IOException {
        if (lookahead == 'd') {
            return parseDictionary();
        } else if (lookahead == 'l') {
            return parseList();
        } else if (lookahead == 'i') {
            return parseNumber();
        } else if (Character.isDigit(lookahead)) {
            return parseString();
        } else {
            throw new IOException("Invalid bencode format at lookahead: " + lookahead + " ('" + (char) lookahead + "')");
        }
    }

    private Map<String, Object> parseDictionary() throws IOException {
        Map<String, Object> dict = new LinkedHashMap<>();
        if (lookahead != 'd') {
            throw new IOException("Expected 'd' at beginning of dictionary");
        }
        lookahead = in.read(); // consume 'd'

        while (lookahead != 'e') {
            String key = parseString();
            Object value = parseValue();
            dict.put(key, value);
        }

        lookahead = in.read(); // consume 'e'
        return dict;
    }

    private List<Object> parseList() throws IOException {
        List<Object> list = new ArrayList<>();
        if (lookahead != 'l') {
            throw new IOException("Expected 'l' at beginning of list");
        }
        lookahead = in.read(); // consume 'l'

        while (lookahead != 'e') {
            list.add(parseValue());
        }

        lookahead = in.read(); // consume 'e'
        return list;
    }

    private long parseNumber() throws IOException {
        if (lookahead != 'i') {
            throw new IOException("Expected 'i' at beginning of number");
        }
        lookahead = in.read(); // consume 'i'

        StringBuilder sb = new StringBuilder();
        while (lookahead != 'e') {
            if (lookahead == -1) {
                throw new EOFException("Unexpected end of input while parsing number");
            }
            sb.append((char) lookahead);
            lookahead = in.read();
        }

        lookahead = in.read(); // consume 'e'
        return Long.parseLong(sb.toString());
    }

    private String parseString() throws IOException {
        int length = 0;
        while (Character.isDigit(lookahead)) {
            length = length * 10 + (lookahead - '0');
            lookahead = in.read();
        }

        if (lookahead != ':') {
            throw new IOException("Expected ':' after string length");
        }

        byte[] buf = new byte[length];
        int bytesRead = in.read(buf);
        if (bytesRead != length) {
            throw new IOException("Unexpected end of stream when reading string");
        }

        lookahead = in.read(); // prepare for next token
        return new String(buf, StandardCharsets.ISO_8859_1); // 原始字节序列（非UTF-8）
    }

    // 获取 SHA-1（例如计算 info hash）
    public static byte[] calculateSHA1(byte[] data) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return sha1.digest(data);
        } catch (Exception e) {
            throw new RuntimeException("SHA-1 not supported", e);
        }
    }

    // 把 byte[] 转为 hex 字符串
    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
