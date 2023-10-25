package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.io.File;
import java.net.SocketAddress;

public class BanList extends UserList<String, IPBanEntry> {
  public BanList(File bansFile) {
    super(bansFile);
  }
  
  protected UserListEntry<String> createEntry(JsonObject entryData) {
    return new IPBanEntry(entryData);
  }
  
  public boolean isBanned(SocketAddress address) {
    String s = addressToString(address);
    return hasEntry(s);
  }
  
  public IPBanEntry getBanEntry(SocketAddress address) {
    String s = addressToString(address);
    return getEntry(s);
  }
  
  private String addressToString(SocketAddress address) {
    String s = address.toString();
    if (s.contains("/"))
      s = s.substring(s.indexOf('/') + 1); 
    if (s.contains(":"))
      s = s.substring(0, s.indexOf(':')); 
    return s;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\server\management\BanList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */