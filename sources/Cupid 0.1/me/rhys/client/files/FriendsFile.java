package me.rhys.client.files;

import com.google.gson.Gson;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import me.rhys.base.Lite;
import me.rhys.base.file.IFile;
import me.rhys.base.friend.Friend;
import org.json.JSONArray;
import org.json.JSONObject;

public class FriendsFile implements IFile {
  private File file;
  
  public void save(Gson gson) {
    Map<String, String> data = new HashMap<>();
    Lite.FRIEND_MANAGER.getFriendList()
      .forEach(friend -> (String)data.put(friend.getName(), friend.getAlias()));
    writeFile(gson.toJson(data), this.file);
  }
  
  public void load(Gson gson) {
    if (!this.file.exists())
      return; 
    String data = readFile(this.file);
    JSONObject jsonObject = new JSONObject(data);
    if (jsonObject.names() != null) {
      JSONArray jsonArray = jsonObject.names();
      if (jsonArray.length() > 0)
        for (int i = 0; i < jsonArray.length(); i++) {
          String name = jsonArray.getString(i).replace(" ", "");
          if (name.length() > 0) {
            boolean hasAlias = false;
            try {
              hasAlias = (jsonObject.getString(name) != null && !jsonObject.getString(name).equalsIgnoreCase("null"));
            } catch (Exception exception) {}
            Lite.FRIEND_MANAGER.addFriend(name, hasAlias ? jsonObject.getString(name) : null);
          } 
        }  
    } 
  }
  
  public void setFile(File root) {
    this.file = new File(root, "/friends.json");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\files\FriendsFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */