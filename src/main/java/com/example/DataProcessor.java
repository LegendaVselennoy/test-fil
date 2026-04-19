package com.example;

import java.util.HashMap;
import java.util.List;

public class DataProcessor {

    public UnionFind getUnionFind(int n, List<String[]> rows) {
        UnionFind uf = new UnionFind(n);

        HashMap<String, Integer> columnValues = new HashMap<>(n * 2);

        for (int i = 0; i < n; i++) {
            String[] parts = rows.get(i);
            for (int col = 0; col < parts.length; col++) {
                String val = parts[col];
                if (val.isEmpty()) continue;

                String key = col + ":" + val;
                Integer existingLineIdx = columnValues.get(key);
                if (existingLineIdx != null) {
                    uf.union(i, existingLineIdx);
                } else {
                    columnValues.put(key, i);
                }
            }
        }
        return uf;
    }
}