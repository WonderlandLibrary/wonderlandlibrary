package me.rhys.base.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import me.rhys.base.Lite;
import me.rhys.base.util.container.Container;
import net.minecraft.client.Minecraft;

public class FileFactory extends Container<IFile> {
  private final Gson GSON = (new GsonBuilder()).setPrettyPrinting().serializeNulls().create();
  
  private File root;
  
  public File getRoot() {
    return this.root;
  }
  
  public void add(IFile item) {
    item.setFile(this.root);
    super.add(item);
  }
  
  public void saveFile(Class<? extends IFile> iFile) {
    IFile file = (IFile)findByClass(iFile);
    if (file != null)
      file.save(this.GSON); 
  }
  
  public void loadFile(Class<? extends IFile> iFile) {
    IFile file = (IFile)findByClass(iFile);
    if (file != null)
      file.load(this.GSON); 
  }
  
  public void save() {
    forEach(file -> file.save(this.GSON));
  }
  
  public void load() {
    forEach(file -> file.load(this.GSON));
  }
  
  public void setupRoot() {
    this.root = new File((Minecraft.getMinecraft()).mcDataDir, Lite.MANIFEST.getName());
    if (!this.root.exists())
      this.root.mkdirs(); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\file\FileFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */