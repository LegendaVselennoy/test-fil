package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        UnionFind unionFind = new UnionFind();
        FileInOut file = new FileInOut();
        String outFile = "output.txt";

        if (args.length < 1) {
            System.err.println("Использование: java -jar line-grouper.jar <путь к файлу>");
            return;
        }

        String inputPath = args[0];
        long startTime = System.currentTimeMillis();

        List<String> lines = new ArrayList<>();
        Map<String, Integer> columnValues = new HashMap<>();

        try (InputStream fis = file.openStream(inputPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                lines.add(line);
            }

            int n = lines.size();
            unionFind.setParent(new int[n]);
            for (int i = 0; i < n; i++) unionFind.getParent()[i] = i;

            for (int i = 0; i < n; i++) {
                String[] parts = lines.get(i).split(";", -1);
                for (int col = 0; col < parts.length; col++) {
                    String val = parts[col].trim().replace("\"", "");
                    if (val.isEmpty()) continue;

                    String key = col + ":" + val;
                    Integer existingLineIdx = columnValues.get(key);

                    if (existingLineIdx != null) {
                         unionFind.union(i, existingLineIdx);
                    } else {
                        columnValues.put(key, i);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла " + e.getMessage());
            return;
        }

        Map<Integer, List<Integer>> groupMap = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            groupMap.computeIfAbsent(unionFind.find(i), _ -> new ArrayList<>()).add(i);
        }

        List<List<Integer>> sortedGroups = new ArrayList<>(groupMap.values());
        sortedGroups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));

        file.writeOutput(outFile, lines, sortedGroups);

        System.out.printf("Finished. Time: %.2f s. Groups: %d%n",
                (System.currentTimeMillis() - startTime) / 1000.0, sortedGroups.size());
    }
}