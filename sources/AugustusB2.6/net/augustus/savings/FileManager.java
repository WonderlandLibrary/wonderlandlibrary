// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.savings;

import com.google.gson.GsonBuilder;
import java.io.Reader;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.nio.file.Path;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.List;
import com.google.gson.Gson;

public class FileManager<T>
{
    public static final Gson GSON;
    public static final Gson NORMALGSON;
    
    public void saveFile(final String filePath, final String fileName, final List<T> list) {
        final Path path = Paths.get(filePath, new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            try {
                Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            final String s = FileManager.GSON.toJson(list);
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath, fileName)));
            bufferedWriter.write(s);
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveAllFile(final String filePath, final String fileName, final List<T> list) {
        final Path path = Paths.get(filePath, new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            try {
                Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            final String s = FileManager.NORMALGSON.toJson(list);
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath, fileName)));
            bufferedWriter.write(s);
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveAllFile(final String filePath, final String fileName, final T object) {
        final Path path = Paths.get(filePath, new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            try {
                Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            final String s = FileManager.GSON.toJson(object);
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath, fileName)));
            bufferedWriter.write(s);
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveFile(final String filePath, final String fileName, final T object) {
        final Path path = Paths.get(filePath, new String[0]);
        if (!Files.exists(path, new LinkOption[0])) {
            try {
                Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            final String s = FileManager.GSON.toJson(object);
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath, fileName)));
            bufferedWriter.write(s);
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<T> readFile(final String filePath, final String fileName) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(filePath + "/" + fileName, new String[0]));
            list = FileManager.GSON.fromJson(reader, new TypeToken<List<T>>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public ArrayList<T> readFileAll(final String filePath, final String fileName) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(filePath + "/" + fileName, new String[0]));
            list = FileManager.NORMALGSON.fromJson(reader, new TypeToken<List<T>>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public T readSingleFile(final String filePath, final String fileName) {
        T list = null;
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(filePath + "/" + fileName, new String[0]));
            list = FileManager.GSON.fromJson(reader, new TypeToken<T>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public T readSingleFileAll(final String filePath, final String fileName) {
        T list = null;
        try {
            final Reader reader = Files.newBufferedReader(Paths.get(filePath + "/" + fileName, new String[0]));
            list = FileManager.NORMALGSON.fromJson(reader, new TypeToken<T>() {}.getType());
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        NORMALGSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
