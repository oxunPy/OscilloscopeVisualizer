package com.example.program.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    public static double[] readFromFile(InputStream is, final int maxWaveData) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        double[] osciData = new double[maxWaveData];
        String line;
        int lineNumber = 0;
        while ((line = nextLine(reader)) != null && lineNumber < maxWaveData) {
            if (ValidUtils.isValidNumber(line)) {
                osciData[lineNumber++] = Double.parseDouble(line);
            }
        }

        reader.close();
        return osciData;
    }

    private static String nextLine(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
        } while (!ValidUtils.isValidLine(line));
        return line;
    }
}
