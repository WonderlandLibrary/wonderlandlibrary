package me.rhys.client.files;

import com.google.gson.Gson;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import me.rhys.base.file.IFile;
import me.rhys.base.util.LoginThread;
import me.rhys.client.Manager;
import org.json.JSONObject;

public class SettingsFile implements IFile {
  private File file;
  
  public void save(Gson gson) {
    Map<String, String> data = new HashMap<>();
    if (Manager.Data.lastAlt != null)
      data.put("lastAccount", Manager.Data.lastAlt); 
    writeFile(gson.toJson(data), this.file);
  }
  
  public void load(Gson gson) {
    if (!this.file.exists())
      return; 
    String data = readFile(this.file);
    JSONObject jsonObject = new JSONObject(data);
    if (jsonObject.has("lastAccount")) {
      Manager.Data.lastAlt = jsonObject.getString("lastAccount");
      (new LoginThread(Manager.Data.lastAlt.split(":")[0], Manager.Data.lastAlt.split(":")[1])).start();
    } 
  }
  
  public void setFile(File root) {
    this.file = new File(root, "/settings.json");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\files\SettingsFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */