/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

public class FileUtil {
    public static String read(String path) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(path), "UTF8"));
            String out = "";
            while (true) {
                String str;
                if ((str = in.readLine()) == null) {
                    in.close();
                    return out;
                }
                out = out + (out == "" ? "" : "\n") + str;
            }
        }
        catch (IOException e) {
            return null;
        }
    }

    public static ArrayList<String> listFolderForFolder(String dir) {
        File folder = new File(dir);
        ArrayList<String> s = new ArrayList<String>();
        if (folder.listFiles() == null) return s;
        File[] fileArray = folder.listFiles();
        int n = fileArray.length;
        int n2 = 0;
        while (n2 < n) {
            File fileEntry = fileArray[n2];
            if (fileEntry.isDirectory()) {
                s.add(fileEntry.getName());
            }
            ++n2;
        }
        return s;
    }

    public static boolean exists(String dir) {
        File folder = new File(dir);
        return folder.exists();
    }

    public static boolean deleteDirectory(String dir) {
        File folder = new File(dir);
        return FileUtil.deleteDirectoryRecursion(folder);
    }

    public static boolean deleteDirectoryRecursion(File file) {
        if (!file.isDirectory()) return file.delete();
        File[] entries = file.listFiles();
        if (entries == null) return file.delete();
        File[] fileArray = entries;
        int n = fileArray.length;
        int n2 = 0;
        while (n2 < n) {
            File entry = fileArray[n2];
            if (!FileUtil.deleteDirectoryRecursion(entry)) {
                return false;
            }
            ++n2;
        }
        return file.delete();
    }

    public static boolean renameDirectory(String dir, String newDir) {
        File folder = new File(dir);
        if (!folder.exists()) {
            return false;
        }
        File newFolder = new File(newDir);
        if (!newFolder.exists()) return folder.renameTo(newFolder);
        return false;
    }

    public static boolean write(String path, String content) {
        File targetFile = new File(path);
        try {
            Files.createParentDirs(targetFile);
            Files.touch(targetFile);
            targetFile.delete();
            Files.touch(targetFile);
            File fileDir = new File(path);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(fileDir), StandardCharsets.UTF_8));
            out.append(content);
            ((Writer)out).close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public static void deleteFile(String Path2) {
        new File(Path2).delete();
    }

    public static String[] listFilesForFolder(String dir, String ex) {
        File folder = new File(dir);
        if (folder.listFiles() == null) return new String[0];
        ArrayList<String> s = new ArrayList<String>();
        File[] fileArray = folder.listFiles();
        int n = fileArray.length;
        int n2 = 0;
        while (n2 < n) {
            File fileEntry = fileArray[n2];
            try {
                if (fileEntry.getName().substring(fileEntry.getName().lastIndexOf(".")).equalsIgnoreCase(ex)) {
                    s.add(fileEntry.getName());
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            ++n2;
        }
        return s.toArray(new String[s.size()]);
    }

    public static void copy(String from, String to) {
        File srcDir = new File(from);
        File destDir = new File(to);
        try {
            FileUtils.copyDirectory(srcDir, destDir);
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

