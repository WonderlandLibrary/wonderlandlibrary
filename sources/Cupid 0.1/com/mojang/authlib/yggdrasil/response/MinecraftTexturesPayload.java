package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import java.util.UUID;

public class MinecraftTexturesPayload {
  private long timestamp;
  
  private UUID profileId;
  
  private String profileName;
  
  private boolean isPublic;
  
  private Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures;
  
  public long getTimestamp() {
    return this.timestamp;
  }
  
  public UUID getProfileId() {
    return this.profileId;
  }
  
  public String getProfileName() {
    return this.profileName;
  }
  
  public boolean isPublic() {
    return this.isPublic;
  }
  
  public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures() {
    return this.textures;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\response\MinecraftTexturesPayload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */