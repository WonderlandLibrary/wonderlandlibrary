package space.lunaclient.luna.impl.elements.combat.antibot.structure;

import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import space.lunaclient.luna.api.event.Event.Type;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.combat.antibot.AntiBot;
import space.lunaclient.luna.impl.events.EventMotion;
import space.lunaclient.luna.impl.events.EventPacketReceive;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.gui.notifications.ClientNotification.Type;
import space.lunaclient.luna.impl.gui.notifications.NotificationUtil;
import space.lunaclient.luna.util.PlayerUtils;
import space.lunaclient.luna.util.Timer;

public class GWEN
{
  Timer timer = new Timer();
  
  public GWEN() {}
  
  public boolean isReal(EntityPlayer player)
  {
    for (NetworkPlayerInfo npi : Minecraft.thePlayer.sendQueue.getPlayerInfoMap()) {
      if ((npi != null) && (npi.func_178845_a() != null) && (player.getGameProfile() != null) && (npi.func_178845_a().getId().toString().equals(player.getGameProfile().getId().toString())) && (player.getEntityId() <= 1000000000) && (!player.getName().startsWith("§c"))) {
        return true;
      }
    }
    return false;
  }
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    for (Object entity : Minecraft.theWorld.loadedEntityList) {
      if ((((Entity)entity).isInvisible()) && (((Entity)entity).ticksExisted < 55) && (((Entity)entity).getInventory() != null) && (entity != Minecraft.thePlayer)) {
        Minecraft.theWorld.removeEntity((Entity)entity);
      }
    }
    for (??? = Minecraft.theWorld.loadedEntityList.iterator(); ???.hasNext();)
    {
      entity = ???.next();
      if (((entity instanceof EntityPlayer)) && 
        (PlayerUtils.hasArmor((EntityPlayer)entity)) && (((EntityPlayer)entity).ticksExisted < 400) && (entity != Minecraft.thePlayer) && 
        (((EntityPlayer)entity).getTotalArmorValue() > 1)) {
        AntiBot.invalid.add((EntityPlayer)entity);
      }
    }
    Minecraft.getMinecraft();
    EntityPlayerSP p = Minecraft.thePlayer;
    for (Object entity = Minecraft.theWorld.loadedEntityList.iterator(); ((Iterator)entity).hasNext();)
    {
      Object entity = ((Iterator)entity).next();
      if ((!((Entity)entity).getDisplayName().getFormattedText().contains("§a")) && (!((Entity)entity).getDisplayName().getFormattedText().contains("§9")) && (!((Entity)entity).getDisplayName().getFormattedText().contains("§c"))) {
        ((Entity)entity).getDisplayName().getFormattedText();
      }
    }
  }
  
  @EventRegister
  public void onMotion(EventMotion event)
  {
    if (event.getType() == Event.Type.PRE) {
      if ((!AntiBot.invalid.isEmpty()) && (this.timer.hasReached(5000L)))
      {
        AntiBot.invalid.clear();
        this.timer.reset();
      }
    }
  }
  
  @EventRegister
  public void onPacketReceive(EventPacketReceive event)
  {
    double posX;
    S0CPacketSpawnPlayer packet;
    double entX;
    double var7;
    double posY;
    double entY;
    double var9;
    double entZ;
    double var11;
    float distance;
    if (((event.getPacket() instanceof S0CPacketSpawnPlayer)) && ((distance = MathHelper.sqrt_double((var7 = (posX = Minecraft.thePlayer.posX) - (entX = (packet = (S0CPacketSpawnPlayer)event.getPacket()).func_148942_f() / 32)) * var7 + (var9 = (posY = Minecraft.thePlayer.posY) - (entY = packet.func_148949_g() / 32)) * var9 + (var11 = Minecraft.thePlayer.posZ - (entZ = packet.func_148946_h() / 32)) * var11)) <= 17.0F) && (entY > Minecraft.thePlayer.posY + 1.0D) && (Minecraft.thePlayer.posX != entX) && (Minecraft.thePlayer.posY != entY) && (Minecraft.thePlayer.posZ != entZ))
    {
      if (AntiBot.notify.getValBoolean()) {
        NotificationUtil.sendClientMessage("Removed a bot, Distance: " + (int)distance + "b/m", 3500, ClientNotification.Type.INFO);
      }
      event.setCancelled(true);
    }
  }
}
