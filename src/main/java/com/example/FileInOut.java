package com.example;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class FileInOut {

    public void writeOutput(String outFile,
                            List<String[]> rows,
                            List<List<Integer>> sortedGroups,
                            long multiGroupCount) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8), 1 << 16)) {

            writer.write("Групп с более чем одним элементом: " + multiGroupCount);
            writer.newLine();
            writer.newLine();

            int groupNum = 1;
            for (List<Integer> group : sortedGroups) {
                writer.write("Группа " + groupNum++);
                writer.newLine();
                for (int idx : group) {
                    writer.write(String.join(";", rows.get(idx)));
                    writer.newLine();
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing output file: " + outFile);
        }
    }

    public InputStream openStream(String path) throws IOException {
        InputStream fis = new BufferedInputStream(new FileInputStream(path), 1 << 16);
        if (path.endsWith(".gz")) {
            return new GZIPInputStream(fis, 1 << 16);
        }
        return fis;
    }
}