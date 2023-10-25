package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;

public class HasJoinedMinecraftServerResponse extends Response {
  private UUID id;
  
  private PropertyMap properties;
  
  public UUID getId() {
    return this.id;
  }
  
  public PropertyMap getProperties() {
    return this.properties;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\response\HasJoinedMinecraftServerResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */