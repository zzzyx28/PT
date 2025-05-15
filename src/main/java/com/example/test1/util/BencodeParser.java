package com.example.test1.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BencodeParser {

    private final InputStream in;
    private int lookahead = -1;

    public BencodeParser(InputStream in) {
        this.in = in;
    }

    public Object parse() throws IOException {
        lookahead = in.read();
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
        }
        throw new IOException("Invalid bencode format");
    }

    private Map<String, Object> parseDictionary() throws IOException {
        Map<String, Object> dict = new HashMap<>();
        in.read(); // consume 'd'
        while ((lookahead = in.read()) != 'e') {
            String key = parseString();
            Object value = parseValue();
            dict.put(key, value);
        }
        in.read(); // consume 'e'
        return dict;
    }

    private List<Object> parseList() throws IOException {
        List<Object> list = new ArrayList<>();
        in.read(); // consume 'l'
        while ((lookahead = in.read()) != 'e') {
            list.add(parseValue());
        }
        in.read(); // consume 'e'
        return list;
    }

    private long parseNumber() throws IOException {
        in.read(); // consume 'i'
        StringBuilder sb = new StringBuilder();
        while ((lookahead = in.read()) != 'e') {
            sb.append((char) lookahead);
        }
        in.read(); // consume 'e'
        return Long.parseLong(sb.toString());
    }

    private String parseString() throws IOException {
        int length = 0;
        while (Character.isDigit(lookahead)) {
            length = length * 10 + (lookahead - '0');
            lookahead = in.read();
        }
        if (lookahead != ':') {
            throw new IOException("Invalid string length");
        }
        in.read(); // consume ':'
        byte[] bytes = new byte[length];
        int read = in.read(bytes);
        if (read != length) {
            throw new IOException("Unexpected end of string");
        }
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    public static byte[] calculateSHA1(byte[] data) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return sha1.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 not available", e);
        }
    }
}