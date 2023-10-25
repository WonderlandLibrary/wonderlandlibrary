package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.properties.PropertyMap;

public class User {
  private String id;
  
  private PropertyMap properties;
  
  public String getId() {
    return this.id;
  }
  
  public PropertyMap getProperties() {
    return this.properties;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\response\User.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */