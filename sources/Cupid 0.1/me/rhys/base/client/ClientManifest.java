package me.rhys.base.client;

public class ClientManifest {
  private String name;
  
  private String version;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setVersion(String version) {
    this.version = version;
  }
  
  public ClientManifest(String name, String version) {
    this.name = name;
    this.version = version;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getVersion() {
    return this.version;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\client\ClientManifest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */