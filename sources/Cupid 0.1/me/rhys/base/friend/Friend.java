package me.rhys.base.friend;

public class Friend {
  private String name;
  
  private String alias;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getAlias() {
    return this.alias;
  }
  
  public Friend(String name, String alias) {
    this.name = name;
    this.alias = alias;
  }
  
  public Friend(String name) {
    this.name = name;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\friend\Friend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */