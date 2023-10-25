package me.rhys.base.event.impl.init;

import java.util.Arrays;
import me.rhys.base.event.Event;
import me.rhys.base.file.FileFactory;
import me.rhys.base.file.IFile;

public class FileInitializeEvent extends Event {
  private final FileFactory factory;
  
  public FileInitializeEvent(FileFactory factory) {
    this.factory = factory;
  }
  
  public void register(IFile file) {
    this.factory.add(file);
  }
  
  public void register(IFile... files) {
    Arrays.<IFile>stream(files).forEach(this::register);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\init\FileInitializeEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */