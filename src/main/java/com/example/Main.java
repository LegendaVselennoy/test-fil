package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.err.println("Использование: java -jar grouper.jar <путь к файлу> [выходной файл]");
            return;
        }

        long startTime = System.currentTimeMillis();

        String inputPath = args[0];
        String outFile = args.length > 1 ? args[1] : "output.txt";


        Set<String> stringSet = new HashSet<>();
        List<String[]> rows = new ArrayList<>();
        FileInOut file = new FileInOut();
        DataProcessor data = new DataProcessor();

        try (InputStream fis = file.openStream(inputPath);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(fis, StandardCharsets.UTF_8), 1 << 16)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                if (!stringSet.add(line)) continue;

                String[] parts = line.split(";", -1);
                for (int c = 0; c < parts.length; c++) {
                    parts[c] = parts[c].trim().replace("\"", "");
                }
                rows.add(parts);
            }
        }

        int n = rows.size();
        System.out.println("Уникальных строк: " + n);
        UnionFind uf = data.getUnionFind(n, rows);

        Map<Integer, List<Integer>> groupMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            groupMap.computeIfAbsent(uf.find(i), _ -> new ArrayList<>()).add(i);
        }

        List<List<Integer>> sortedGroups = new ArrayList<>(groupMap.values());
        sortedGroups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));

        long multiGroupCount = sortedGroups.stream().filter(g -> g.size() > 1).count();


        file.writeOutput(outFile, rows, sortedGroups, multiGroupCount);


        System.out.printf("Групп с более чем одним элементом: %d%n", multiGroupCount);
        System.out.printf("Время выполнения: %.2f сек%n",
                (System.currentTimeMillis() - startTime) / 1000.0);
    }
}