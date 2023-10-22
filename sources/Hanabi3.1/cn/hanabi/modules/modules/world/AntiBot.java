package cn.hanabi.modules.modules.world;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

@ObfuscationClass
public class AntiBot extends Mod {
   private static final Value mode = new Value("AntiBot", "Mode", 0);
   private static final List invalid = new CopyOnWriteArrayList();
   private static final List whitelist = new CopyOnWriteArrayList();
   public Value remove = new Value("AntiBot", "Removed", true);
   public int count = 0;

   public AntiBot() {
      super("AntiBot", Category.COMBAT);
      mode.LoadValue(new String[]{"Hypixel", "Mineplex", "Advanced", "MineLand", "HuaYuTing"});
      this.setState(true);
   }

   public static boolean isBot(Entity e) {
      if (e instanceof EntityPlayer && ModManager.getModule("AntiBot").isEnabled()) {
         EntityPlayer player = (EntityPlayer)e;
         if (mode.isCurrentMode("Hypixel")) {
            return !inTab(player) && !whitelist.contains(player);
         } else {
            return mode.isCurrentMode("Mineplex") && !Float.isNaN(player.getHealth());
         }
      } else {
         return false;
      }
   }

   private static boolean inTab(EntityLivingBase entity) {
      Iterator var1 = mc.getNetHandler().getPlayerInfoMap().iterator();

      NetworkPlayerInfo info;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         info = (NetworkPlayerInfo)var1.next();
      } while(info == null || info.getGameProfile() == null || !info.getGameProfile().getName().contains(entity.getName()));

      return true;
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onReceivePacket(EventPacket event) {
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.setDisplayName(mode.getModeAt(mode.getCurrentMode()));
      if (!mc.isSingleplayer()) {
         Iterator var2;
         Entity ent;
         if (mode.isCurrentMode("MineLand") && !mc.theWorld.getLoadedEntityList().isEmpty()) {
            var2 = mc.theWorld.getLoadedEntityList().iterator();

            while(var2.hasNext()) {
               ent = (Entity)var2.next();
               if (ent instanceof EntityPlayer) {
                  if (!invalid.contains(ent) && mc.thePlayer.getDistanceToEntity(ent) > 20.0F) {
                     invalid.add(ent);
                  }

                  if (ent != mc.thePlayer && !invalid.contains(ent) && mc.thePlayer.getDistanceToEntity(ent) < 10.0F) {
                     mc.theWorld.removeEntity(ent);
                  }
               }
            }
         }

         if (mc.thePlayer.ticksExisted < 5) {
            whitelist.clear();
         }

         if (mc.thePlayer.ticksExisted % 60 == 0) {
            whitelist.clear();
         }

         if (mode.isCurrentMode("Hypixel") && !mc.theWorld.getLoadedEntityList().isEmpty()) {
            var2 = mc.theWorld.getLoadedEntityList().iterator();

            while(var2.hasNext()) {
               ent = (Entity)var2.next();
               if (ent instanceof EntityPlayer && !whitelist.contains(ent)) {
                  String formatted = ent.getDisplayName().getFormattedText();
                  if (formatted.startsWith("§r§8[NPC]")) {
                     return;
                  }

                  if (!ent.isInvisible()) {
                     whitelist.add(ent);
                  }

                  if (ent.hurtResistantTime == 8) {
                     whitelist.add(ent);
                  }
               }
            }
         }

      }
   }
}
