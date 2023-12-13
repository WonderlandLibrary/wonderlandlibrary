package me.arithmo.management;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import me.arithmo.Client;
import me.arithmo.module.Module;

public abstract class Saveable
{
  private final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
  private SubFolder folderType;
  private File folder;
  private File file;
  
  public boolean save()
  {
    String data = this.gson.toJson(this);
    try
    {
      checkFile();
      FileWriter writer = new FileWriter(getFile());
      writer.write(data);
      writer.close();
      return true;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      
      Module.saveStatus();
    }
    return false;
  }
  
  public Saveable load()
  {
    try
    {
      checkFile();
      BufferedReader br = new BufferedReader(new FileReader(getFile()));
      return (Saveable)this.gson.fromJson(br, getClass());
    }
    catch (RuntimeException|IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  protected void checkFile()
    throws IOException
  {
    File file = getFile();
    if (!file.exists())
    {
      File folder = getFolder();
      if (!folder.exists()) {
        folder.mkdirs();
      }
      file.createNewFile();
    }
  }
  
  public void setupFolder()
  {
    if (this.folderType == null) {
      this.folderType = SubFolder.Other;
    }
    String basePath = Client.getDataDir().getAbsolutePath();
    String newPath = basePath + (basePath.endsWith(File.separator) ? this.folderType.getFolderName() : new StringBuilder().append(File.separator).append(this.folderType.getFolderName()).toString());
    this.folder = new File(newPath);
  }
  
  public void setupFile()
  {
    if (this.folder == null) {
      setupFolder();
    }
    String fileName = getFileName();
    String basePath = this.folder.getAbsolutePath();
    String newPath = basePath + (basePath.endsWith(File.separator) ? fileName : new StringBuilder().append(File.separator).append(fileName).toString());
    this.file = new File(newPath);
  }
  
  public File getFolder()
  {
    if (this.folder == null) {
      setupFolder();
    }
    return this.folder;
  }
  
  public File getFile()
  {
    if (this.file == null) {
      setupFile();
    }
    return this.file;
  }
  
  public SubFolder getFolderType()
  {
    return this.folderType;
  }
  
  public void setFolderType(SubFolder folderType)
  {
    this.folderType = folderType;
  }
  
  public String getFileName()
  {
    return getClass().getSimpleName() + ".json";
  }
}
