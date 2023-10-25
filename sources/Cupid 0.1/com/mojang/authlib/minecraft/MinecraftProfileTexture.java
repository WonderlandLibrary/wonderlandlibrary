package com.mojang.authlib.minecraft;

import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MinecraftProfileTexture {
  private final String url;
  
  private final Map<String, String> metadata;
  
  public enum Type {
    SKIN, CAPE;
  }
  
  public MinecraftProfileTexture(String url, Map<String, String> metadata) {
    this.url = url;
    this.metadata = metadata;
  }
  
  public String getUrl() {
    return this.url;
  }
  
  @Nullable
  public String getMetadata(String key) {
    if (this.metadata == null)
      return null; 
    return this.metadata.get(key);
  }
  
  public String getHash() {
    return FilenameUtils.getBaseName(this.url);
  }
  
  public String toString() {
    return (new ToStringBuilder(this)).append("url", this.url).append("hash", getHash()).toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\minecraft\MinecraftProfileTexture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */