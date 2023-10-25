package com.github.creeper123123321.viafabric.platform;

import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;

public class VRViaAPI implements ViaAPI<UUID> {
  public int getPlayerVersion(UUID uuid) {
    UserConnection con = Via.getManager().getConnection(uuid);
    if (con != null)
      return con.getProtocolInfo().getProtocolVersion(); 
    try {
      return Via.getManager().getInjector().getServerProtocolVersion();
    } catch (Exception e) {
      throw new AssertionError(e);
    } 
  }
  
  public boolean isInjected(UUID uuid) {
    return Via.getManager().isClientConnected(uuid);
  }
  
  public String getVersion() {
    return Via.getPlatform().getPluginVersion();
  }
  
  public void sendRawPacket(UUID uuid, ByteBuf byteBuf) throws IllegalArgumentException {
    UserConnection ci = Via.getManager().getConnection(uuid);
    ci.sendRawPacket(byteBuf);
  }
  
  public BossBar<Void> createBossBar(String s, BossColor bossColor, BossStyle bossStyle) {
    return (BossBar<Void>)new VRBossBar(s, 1.0F, bossColor, bossStyle);
  }
  
  public BossBar<Void> createBossBar(String s, float v, BossColor bossColor, BossStyle bossStyle) {
    return (BossBar<Void>)new VRBossBar(s, v, bossColor, bossStyle);
  }
  
  public SortedSet<Integer> getSupportedVersions() {
    SortedSet<Integer> outputSet = new TreeSet<>(ProtocolRegistry.getSupportedVersions());
    outputSet.removeAll((Collection<?>)Via.getPlatform().getConf().getBlockedProtocols());
    return outputSet;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\platform\VRViaAPI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */