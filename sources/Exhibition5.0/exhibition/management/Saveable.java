// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management;

import exhibition.Client;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import exhibition.module.Module;
import java.io.FileWriter;
import com.google.gson.GsonBuilder;
import java.io.File;
import com.google.gson.Gson;

public abstract class Saveable
{
    private final Gson gson;
    private SubFolder folderType;
    private File folder;
    private File file;
    
    public Saveable() {
        this.gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    }
    
    public boolean save() {
        final String data = this.gson.toJson((Object)this);
        try {
            this.checkFile();
            final FileWriter writer = new FileWriter(this.getFile());
            writer.write(data);
            writer.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            Module.saveStatus();
            return false;
        }
    }
    
    public Saveable load() {
        try {
            this.checkFile();
            final BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            return (Saveable)this.gson.fromJson((Reader)br, (Class)this.getClass());
        }
        catch (RuntimeException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    protected void checkFile() throws IOException {
        final File file = this.getFile();
        if (!file.exists()) {
            final File folder = this.getFolder();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            file.createNewFile();
        }
    }
    
    public void setupFolder() {
        if (this.folderType == null) {
            this.folderType = SubFolder.Other;
        }
        final String basePath = Client.getDataDir().getAbsolutePath();
        final String newPath = basePath + (basePath.endsWith(File.separator) ? this.folderType.getFolderName() : (File.separator + this.folderType.getFolderName()));
        this.folder = new File(newPath);
    }
    
    public void setupFile() {
        if (this.folder == null) {
            this.setupFolder();
        }
        final String fileName = this.getFileName();
        final String basePath = this.folder.getAbsolutePath();
        final String newPath = basePath + (basePath.endsWith(File.separator) ? fileName : (File.separator + fileName));
        this.file = new File(newPath);
    }
    
    public File getFolder() {
        if (this.folder == null) {
            this.setupFolder();
        }
        return this.folder;
    }
    
    public File getFile() {
        if (this.file == null) {
            this.setupFile();
        }
        return this.file;
    }
    
    public SubFolder getFolderType() {
        return this.folderType;
    }
    
    public void setFolderType(final SubFolder folderType) {
        this.folderType = folderType;
    }
    
    public String getFileName() {
        return this.getClass().getSimpleName() + ".json";
    }
}
