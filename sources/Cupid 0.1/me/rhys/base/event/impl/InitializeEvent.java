package me.rhys.base.event.impl;

import me.rhys.base.event.Event;

public class InitializeEvent extends Event {
  private String name;
  
  private String version;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setVersion(String version) {
    this.version = version;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getVersion() {
    return this.version;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\InitializeEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */