// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.config;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import com.google.gson.Gson;

public interface IFile
{
    void save(final Gson p0);
    
    void load(final Gson p0);
    
    void setFile(final File p0);
    
    default void writeFile(final String content, final File file) {
        try {
            Throwable t = null;
            try {
                final FileWriter fileWriter = new FileWriter(file);
                try {
                    fileWriter.write(content);
                }
                finally {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable exception;
                    t = exception;
                }
                else {
                    final Throwable exception;
                    if (t != exception) {
                        t.addSuppressed(exception);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    default String readFile(final File file) {
        final StringBuilder builder = new StringBuilder();
        try {
            Throwable t = null;
            try {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                    }
                }
                finally {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable exception;
                    t = exception;
                }
                else {
                    final Throwable exception;
                    if (t != exception) {
                        t.addSuppressed(exception);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
