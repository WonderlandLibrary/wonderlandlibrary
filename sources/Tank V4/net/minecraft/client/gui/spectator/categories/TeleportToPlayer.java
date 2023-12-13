package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.PlayerMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class TeleportToPlayer implements ISpectatorMenuView, ISpectatorMenuObject {
   private static final Ordering field_178674_a = Ordering.from(new Comparator() {
      public int compare(NetworkPlayerInfo var1, NetworkPlayerInfo var2) {
         return ComparisonChain.start().compare(var1.getGameProfile().getId(), var2.getGameProfile().getId()).result();
      }

      public int compare(Object var1, Object var2) {
         return this.compare((NetworkPlayerInfo)var1, (NetworkPlayerInfo)var2);
      }
   });
   private final List field_178673_b;

   public boolean func_178662_A_() {
      return !this.field_178673_b.isEmpty();
   }

   public void func_178663_a(float var1, int var2) {
      Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
      Gui.drawModalRectWithCustomSizedTexture(0.0D, 0.0D, 0.0F, 0.0F, 16.0D, 16.0D, 256.0D, 256.0D);
   }

   public IChatComponent getSpectatorName() {
      return new ChatComponentText("Teleport to player");
   }

   public void func_178661_a(SpectatorMenu var1) {
      var1.func_178647_a(this);
   }

   public TeleportToPlayer() {
      this(field_178674_a.sortedCopy(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()));
   }

   public List func_178669_a() {
      return this.field_178673_b;
   }

   public IChatComponent func_178670_b() {
      return new ChatComponentText("Select a player to teleport to");
   }

   public TeleportToPlayer(Collection var1) {
      this.field_178673_b = Lists.newArrayList();
      Iterator var3 = field_178674_a.sortedCopy(var1).iterator();

      while(var3.hasNext()) {
         NetworkPlayerInfo var2 = (NetworkPlayerInfo)var3.next();
         if (var2.getGameType() != WorldSettings.GameType.SPECTATOR) {
            this.field_178673_b.add(new PlayerMenuObject(var2.getGameProfile()));
         }
      }

   }
}
