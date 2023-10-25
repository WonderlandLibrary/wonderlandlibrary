package com.mojang.authlib;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
  LEGACY("legacy"),
  MOJANG("mojang");
  
  private static final Map<String, UserType> BY_NAME;
  
  private final String name;
  
  static {
    BY_NAME = new HashMap<String, UserType>();
    for (UserType type : values())
      BY_NAME.put(type.name, type); 
  }
  
  UserType(String name) {
    this.name = name;
  }
  
  public static UserType byName(String name) {
    return BY_NAME.get(name.toLowerCase());
  }
  
  public String getName() {
    return this.name;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\UserType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */