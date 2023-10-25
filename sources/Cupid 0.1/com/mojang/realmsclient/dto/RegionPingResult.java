package com.mojang.realmsclient.dto;

public class RegionPingResult {
  private String regionName;
  
  private int ping;
  
  public RegionPingResult(String regionName, int ping) {
    this.regionName = regionName;
    this.ping = ping;
  }
  
  public int ping() {
    return this.ping;
  }
  
  public String toString() {
    return String.format("%s --> %.2f ms", new Object[] { this.regionName, Integer.valueOf(this.ping) });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\dto\RegionPingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */