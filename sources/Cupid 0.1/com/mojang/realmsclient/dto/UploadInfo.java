package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UploadInfo {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private boolean worldClosed;
  
  private String token = "";
  
  private String uploadEndpoint = "";
  
  public static UploadInfo parse(String json) {
    UploadInfo uploadInfo = new UploadInfo();
    try {
      JsonParser parser = new JsonParser();
      JsonObject jsonObject = parser.parse(json).getAsJsonObject();
      uploadInfo.worldClosed = JsonUtils.getBooleanOr("worldClosed", jsonObject, false);
      uploadInfo.token = JsonUtils.getStringOr("token", jsonObject, null);
      uploadInfo.uploadEndpoint = JsonUtils.getStringOr("uploadEndpoint", jsonObject, null);
    } catch (Exception e) {
      LOGGER.error("Could not parse UploadInfo: " + e.getMessage());
    } 
    return uploadInfo;
  }
  
  public String getToken() {
    return this.token;
  }
  
  public String getUploadEndpoint() {
    return this.uploadEndpoint;
  }
  
  public boolean isWorldClosed() {
    return this.worldClosed;
  }
  
  public void setToken(String token) {
    this.token = token;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\dto\UploadInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */