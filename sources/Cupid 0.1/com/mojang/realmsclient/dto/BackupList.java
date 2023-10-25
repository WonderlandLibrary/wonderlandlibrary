package com.mojang.realmsclient.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackupList {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public List<Backup> backups;
  
  public static BackupList parse(String json) {
    JsonParser jsonParser = new JsonParser();
    BackupList backupList = new BackupList();
    backupList.backups = new ArrayList<Backup>();
    try {
      JsonElement node = jsonParser.parse(json).getAsJsonObject().get("backups");
      if (node.isJsonArray()) {
        Iterator<JsonElement> iterator = node.getAsJsonArray().iterator();
        while (iterator.hasNext())
          backupList.backups.add(Backup.parse(iterator.next())); 
      } 
    } catch (Exception e) {
      LOGGER.error("Could not parse BackupList: " + e.getMessage());
    } 
    return backupList;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\dto\BackupList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */