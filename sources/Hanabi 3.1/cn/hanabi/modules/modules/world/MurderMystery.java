package cn.hanabi.modules.modules.world;

import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventTick;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.render.ESP;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.WorldUtil;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmorStand;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemMap;
import net.minecraft.util.EnumChatFormatting;

public class MurderMystery extends Mod {
   private static EntityPlayer murder;
   private final List alartedPlayers = new ArrayList();

   public MurderMystery() {
      super("MurderMystery", Category.WORLD);
   }

   public static boolean isMurder(EntityPlayer player) {
      if (player != null && murder != null) {
         return !player.isDead && !player.isInvisible() ? player.equals(murder) : false;
      } else {
         return false;
      }
   }

   public void onDisable() {
      if (this.alartedPlayers != null) {
         this.alartedPlayers.clear();
      }

      super.onDisable();
   }

   @EventTarget
   public void onRespawn(EventWorldChange event) {
      if (this.alartedPlayers != null) {
         this.alartedPlayers.clear();
      }

   }

   @EventTarget
   public void onRender(EventRender event) {
      if (isMurder(murder)) {
         ((ESP)ModManager.getModule(ESP.class)).renderBox(murder, murder.hurtTime > 1 ? 0.800000011920929 : 0.0, murder.hurtTime > 1 ? 0.0 : 0.4000000059604645, murder.hurtTime > 1 ? 0.0 : 1.0);
      }

   }

   @EventTarget
   public void onTick(EventTick event) {
      if (mc.theWorld != null && this.alartedPlayers != null) {
         try {
            Iterator var2 = WorldUtil.getLivingPlayers().iterator();

            while(var2.hasNext()) {
               EntityPlayer player = (EntityPlayer)var2.next();
               if (!this.alartedPlayers.contains(player.getName()) && player.getCurrentEquippedItem() != null && this.CheckItem(player.getCurrentEquippedItem().getItem())) {
                  PlayerUtil.tellPlayer(EnumChatFormatting.GOLD + player.getName() + EnumChatFormatting.RESET + " is the murderer!!!");
                  this.alartedPlayers.add(player.getName());
                  murder = player;
               }
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }

      }
   }

   public boolean CheckItem(Item item) {
      return !(item instanceof ItemMap) && !(item instanceof ItemArmorStand) && !item.getUnlocalizedName().equalsIgnoreCase("item.ingotGold") && !(item instanceof ItemBow) && !item.getUnlocalizedName().equalsIgnoreCase("item.arrow") && !item.getUnlocalizedName().equalsIgnoreCase("item.potion") && !item.getUnlocalizedName().equalsIgnoreCase("item.paper") && !item.getUnlocalizedName().equalsIgnoreCase("tile.tnt") && !item.getUnlocalizedName().equalsIgnoreCase("item.web") && !item.getUnlocalizedName().equalsIgnoreCase("item.bed") && !item.getUnlocalizedName().equalsIgnoreCase("item.compass") && !item.getUnlocalizedName().equalsIgnoreCase("item.comparator") && !item.getUnlocalizedName().equalsIgnoreCase("item.shovelWood");
   }
}
