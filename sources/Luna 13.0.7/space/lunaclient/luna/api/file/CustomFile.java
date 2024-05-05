package space.lunaclient.luna.api.file;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;

public abstract class CustomFile
{
  private Gson gson;
  private File file;
  
  public CustomFile(Gson gson, File file)
  {
    this.gson = gson;
    this.file = file;
    makeDirectory();
  }
  
  private void makeDirectory()
  {
    if ((this.file != null) && (!this.file.exists())) {
      try
      {
        this.file.createNewFile();
      }
      catch (IOException localIOException) {}
    }
  }
  
  public abstract void loadFile()
    throws IOException;
  
  public abstract void saveFile()
    throws IOException;
  
  public File getFile()
  {
    return this.file;
  }
  
  public Gson getGson()
  {
    return this.gson;
  }
}
