package com.example;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class FileInOut {

    public void writeOutput(String path, List<String> lines, List<List<Integer>> groups) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            long multiGroups = groups.stream().filter(g -> g.size() > 1).count();
            writer.println("Количество групп с более чем одним элементом: " + multiGroups);

            int count = 1;
            for (List<Integer> group : groups) {
                writer.println("Группа " + count++);
                for (int idx : group) {
                    writer.println(lines.get(idx));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка вывода "  + e.getMessage());
        }
    }

    public InputStream openStream(String path) throws IOException {
        InputStream fis = new FileInputStream(path);
        return path.endsWith(".gz") ? new GZIPInputStream(fis) : new BufferedInputStream(fis);
    }
}