package net.minecraft.client.gui.spectator;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public class PlayerMenuObject implements ISpectatorMenuObject {
   private final GameProfile profile;
   private final ResourceLocation resourceLocation;

   public void func_178663_a(float var1, int var2) {
      Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
      GlStateManager.color(1.0F, 1.0F, 1.0F, (float)var2 / 255.0F);
      Gui.drawScaledCustomSizeModalRect(2.0D, 2.0D, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
      Gui.drawScaledCustomSizeModalRect(2.0D, 2.0D, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
   }

   public IChatComponent getSpectatorName() {
      return new ChatComponentText(this.profile.getName());
   }

   public boolean func_178662_A_() {
      return true;
   }

   public PlayerMenuObject(GameProfile var1) {
      this.profile = var1;
      this.resourceLocation = AbstractClientPlayer.getLocationSkin(var1.getName());
      AbstractClientPlayer.getDownloadImageSkin(this.resourceLocation, var1.getName());
   }

   public void func_178661_a(SpectatorMenu var1) {
      Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C18PacketSpectate(this.profile.getId()));
   }
}
