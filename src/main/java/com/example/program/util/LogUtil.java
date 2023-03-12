package com.example.program.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Xurshidbek on 04.12.2017.
 */
public class LogUtil {

    private Class clazz;

    public LogUtil(Class clazz) {
        this.clazz = clazz;
    }

    public static LogUtil getLog(Class clazz) {
        return new LogUtil(clazz);
    }

    public void print(String logStr) {
        print(clazz, logStr);
    }
    private void print(Class clazz, String logStr) {
        toFile(clazz, logStr);
        toDatabase(clazz, logStr);
    }

    private void printFile(Class clazz, String logStr) {
        toFile(clazz, logStr);
    }

    private void toFile(Class clazz, String logStr) {
        File logFile = new File("my_logs_file");

        try {
            FileUtils.writeStringToFile(logFile, String.format("###%s %s = %s\n", Time.toDate(new Date()), clazz.getName(), logStr), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toDatabase(Class clazz, String logStr){

    }
}
