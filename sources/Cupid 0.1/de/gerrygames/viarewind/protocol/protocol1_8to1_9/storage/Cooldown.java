package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import de.gerrygames.viarewind.ViaRewind;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.utils.Tickable;
import java.util.ArrayList;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Pair;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.type.Type;

public class Cooldown extends StoredObject implements Tickable {
  private double attackSpeed;
  
  private long lastHit;
  
  private final ViaRewindConfig.CooldownIndicator cooldownIndicator;
  
  private UUID bossUUID;
  
  private boolean lastSend;
  
  private static final int max = 10;
  
  public Cooldown(UserConnection user) {
    super(user);
    ViaRewindConfig.CooldownIndicator indicator;
    this.attackSpeed = 4.0D;
    this.lastHit = 0L;
    try {
      indicator = ViaRewind.getConfig().getCooldownIndicator();
    } catch (IllegalArgumentException e) {
      ViaRewind.getPlatform().getLogger().warning("Invalid cooldown-indicator setting");
      indicator = ViaRewindConfig.CooldownIndicator.DISABLED;
    } 
    this.cooldownIndicator = indicator;
  }
  
  public void tick() {
    if (!hasCooldown()) {
      if (this.lastSend) {
        hide();
        this.lastSend = false;
      } 
      return;
    } 
    BlockPlaceDestroyTracker tracker = (BlockPlaceDestroyTracker)getUser().get(BlockPlaceDestroyTracker.class);
    if (tracker.isMining()) {
      this.lastHit = 0L;
      if (this.lastSend) {
        hide();
        this.lastSend = false;
      } 
      return;
    } 
    showCooldown();
    this.lastSend = true;
  }
  
  private void showCooldown() {
    if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.TITLE) {
      sendTitle("", getTitle(), 0, 2, 5);
    } else if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.ACTION_BAR) {
      sendActionBar(getTitle());
    } else if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.BOSS_BAR) {
      sendBossBar((float)getCooldown());
    } 
  }
  
  private void hide() {
    if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.ACTION_BAR) {
      sendActionBar("§r");
    } else if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.TITLE) {
      hideTitle();
    } else if (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.BOSS_BAR) {
      hideBossBar();
    } 
  }
  
  private void hideBossBar() {
    if (this.bossUUID == null)
      return; 
    PacketWrapper wrapper = new PacketWrapper(12, null, getUser());
    wrapper.write(Type.UUID, this.bossUUID);
    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(1));
    PacketUtil.sendPacket(wrapper, Protocol1_8TO1_9.class, false, true);
    this.bossUUID = null;
  }
  
  private void sendBossBar(float cooldown) {
    PacketWrapper wrapper = new PacketWrapper(12, null, getUser());
    if (this.bossUUID == null) {
      this.bossUUID = UUID.randomUUID();
      wrapper.write(Type.UUID, this.bossUUID);
      wrapper.write((Type)Type.VAR_INT, Integer.valueOf(0));
      wrapper.write(Type.STRING, "{\"text\":\"  \"}");
      wrapper.write((Type)Type.FLOAT, Float.valueOf(cooldown));
      wrapper.write((Type)Type.VAR_INT, Integer.valueOf(0));
      wrapper.write((Type)Type.VAR_INT, Integer.valueOf(0));
      wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)0));
    } else {
      wrapper.write(Type.UUID, this.bossUUID);
      wrapper.write((Type)Type.VAR_INT, Integer.valueOf(2));
      wrapper.write((Type)Type.FLOAT, Float.valueOf(cooldown));
    } 
    PacketUtil.sendPacket(wrapper, Protocol1_8TO1_9.class, false, true);
  }
  
  private void hideTitle() {
    PacketWrapper hide = new PacketWrapper(69, null, getUser());
    hide.write((Type)Type.VAR_INT, Integer.valueOf(3));
    PacketUtil.sendPacket(hide, Protocol1_8TO1_9.class);
  }
  
  private void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
    PacketWrapper timePacket = new PacketWrapper(69, null, getUser());
    timePacket.write((Type)Type.VAR_INT, Integer.valueOf(2));
    timePacket.write(Type.INT, Integer.valueOf(fadeIn));
    timePacket.write(Type.INT, Integer.valueOf(stay));
    timePacket.write(Type.INT, Integer.valueOf(fadeOut));
    PacketWrapper titlePacket = new PacketWrapper(69, null, getUser());
    titlePacket.write((Type)Type.VAR_INT, Integer.valueOf(0));
    titlePacket.write(Type.STRING, title);
    PacketWrapper subtitlePacket = new PacketWrapper(69, null, getUser());
    subtitlePacket.write((Type)Type.VAR_INT, Integer.valueOf(1));
    subtitlePacket.write(Type.STRING, subTitle);
    PacketUtil.sendPacket(titlePacket, Protocol1_8TO1_9.class);
    PacketUtil.sendPacket(subtitlePacket, Protocol1_8TO1_9.class);
    PacketUtil.sendPacket(timePacket, Protocol1_8TO1_9.class);
  }
  
  private void sendActionBar(String bar) {
    PacketWrapper actionBarPacket = new PacketWrapper(2, null, getUser());
    actionBarPacket.write(Type.STRING, bar);
    actionBarPacket.write(Type.BYTE, Byte.valueOf((byte)2));
    PacketUtil.sendPacket(actionBarPacket, Protocol1_8TO1_9.class);
  }
  
  public boolean hasCooldown() {
    long time = System.currentTimeMillis() - this.lastHit;
    double cooldown = restrain(time * this.attackSpeed / 1000.0D, 0.0D, 1.5D);
    return (cooldown > 0.1D && cooldown < 1.1D);
  }
  
  public double getCooldown() {
    long time = System.currentTimeMillis() - this.lastHit;
    return restrain(time * this.attackSpeed / 1000.0D, 0.0D, 1.0D);
  }
  
  private double restrain(double x, double a, double b) {
    if (x < a)
      return a; 
    if (x > b)
      return b; 
    return x;
  }
  
  private String getTitle() {
    String symbol = (this.cooldownIndicator == ViaRewindConfig.CooldownIndicator.ACTION_BAR) ? "■" : "˙";
    double cooldown = getCooldown();
    int green = (int)Math.floor(10.0D * cooldown);
    int grey = 10 - green;
    StringBuilder builder = new StringBuilder("§8");
    for (; green-- > 0; builder.append(symbol));
    builder.append("§7");
    for (; grey-- > 0; builder.append(symbol));
    return builder.toString();
  }
  
  public double getAttackSpeed() {
    return this.attackSpeed;
  }
  
  public void setAttackSpeed(double attackSpeed) {
    this.attackSpeed = attackSpeed;
  }
  
  public void setAttackSpeed(double base, ArrayList<Pair<Byte, Double>> modifiers) {
    this.attackSpeed = base;
    int j;
    for (j = 0; j < modifiers.size(); j++) {
      if (((Byte)((Pair)modifiers.get(j)).getKey()).byteValue() == 0) {
        this.attackSpeed += ((Double)((Pair)modifiers.get(j)).getValue()).doubleValue();
        modifiers.remove(j--);
      } 
    } 
    for (j = 0; j < modifiers.size(); j++) {
      if (((Byte)((Pair)modifiers.get(j)).getKey()).byteValue() == 1) {
        this.attackSpeed += base * ((Double)((Pair)modifiers.get(j)).getValue()).doubleValue();
        modifiers.remove(j--);
      } 
    } 
    for (j = 0; j < modifiers.size(); j++) {
      if (((Byte)((Pair)modifiers.get(j)).getKey()).byteValue() == 2) {
        this.attackSpeed *= 1.0D + ((Double)((Pair)modifiers.get(j)).getValue()).doubleValue();
        modifiers.remove(j--);
      } 
    } 
  }
  
  public void hit() {
    this.lastHit = System.currentTimeMillis();
  }
  
  public void setLastHit(long lastHit) {
    this.lastHit = lastHit;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\storage\Cooldown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */