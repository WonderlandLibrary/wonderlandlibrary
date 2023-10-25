package me.rhys.client.files;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import me.rhys.base.file.IFile;
import me.rhys.base.ui.element.Element;
import me.rhys.base.util.LoginThread;
import me.rhys.client.Manager;
import me.rhys.client.ui.alt.element.AccountItem;

public class AccountsFile implements IFile {
  private File file;
  
  public void save(Gson gson) {
    if (Manager.UI.ALT.altPanel != null) {
      JsonObject mainObj = new JsonObject();
      JsonArray array = new JsonArray();
      Manager.UI.ALT.altPanel.getContainer().stream().filter(element -> element instanceof AccountItem).forEach(element -> {
            JsonObject object = new JsonObject();
            AccountItem item = (AccountItem)element;
            object.addProperty("email", item.getEmail());
            object.addProperty("password", item.getPassword());
            array.add((JsonElement)object);
          });
      mainObj.add("accounts", (JsonElement)array);
      writeFile(gson.toJson((JsonElement)mainObj), this.file);
    } 
  }
  
  public void load(Gson gson) {
    if (!this.file.exists())
      return; 
    JsonObject mainObj = (JsonObject)gson.fromJson(readFile(this.file), JsonObject.class);
    if (mainObj.has("lastAccount")) {
      JsonObject account = mainObj.get("lastAccount").getAsJsonObject();
      if (account.has("email") && account
        .has("password"))
        (new LoginThread(account
            .get("email").getAsString(), account
            .get("password").getAsString()))
          .start(); 
    } 
    if (mainObj.has("accounts")) {
      JsonArray array = mainObj.get("accounts").getAsJsonArray();
      array.forEach(jsonElement -> {
            JsonObject object = jsonElement.getAsJsonObject();
            if (object.has("email") && object.has("password"))
              Manager.UI.ALT.queue.put(object.get("email").getAsString(), object.get("password").getAsString()); 
          });
    } 
  }
  
  public void setFile(File root) {
    this.file = new File(root, "/accounts.json");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\files\AccountsFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */