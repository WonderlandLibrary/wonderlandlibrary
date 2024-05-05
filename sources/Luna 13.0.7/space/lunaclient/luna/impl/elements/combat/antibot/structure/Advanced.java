package space.lunaclient.luna.impl.elements.combat.antibot.structure;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.combat.antibot.AntiBot;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.FriendManager;
import space.lunaclient.luna.util.PlayerUtils;
import space.lunaclient.luna.util.Timer;

public class Advanced
{
  public static ArrayList<Entity> invalidEntity = new ArrayList();
  public static ArrayList<Integer> invalidID = new ArrayList();
  Timer timer = new Timer();
  
  public Advanced() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if (this.timer.hasReached(1000L))
    {
      invalidID.clear();
      invalidEntity.clear();
      this.timer.reset();
    }
    for (Object o : Minecraft.theWorld.loadedEntityList)
    {
      Entity entity = (Entity)o;
      if ((entity.isInvisible() & !FriendManager.isFriend(entity.getName()))) {
        if (((entity != Minecraft.thePlayer ? 1 : 0) & (Minecraft.thePlayer.ticksExisted > 290 ? 1 : 0) & (entity.ticksExisted > 120 ? 1 : 0)) != 0)
        {
          invalidID.add(Integer.valueOf(entity.getEntityId()));
          if (AntiBot.notify.getValBoolean()) {
            PlayerUtils.tellPlayer(ChatFormatting.GRAY + "Removed a bot with the ID: " + entity.getEntityId() + ", name: " + entity.getName(), false);
          }
          if (AntiBot.remove.getValBoolean()) {
            Minecraft.theWorld.removeEntity(entity);
          } else {
            invalidID.add(Integer.valueOf(entity.getEntityId()));
          }
        }
      }
    }
  }
}
