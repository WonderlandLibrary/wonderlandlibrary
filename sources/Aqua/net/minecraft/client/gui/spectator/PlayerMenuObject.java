package net.minecraft.client.gui.spectator;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public class PlayerMenuObject
implements ISpectatorMenuObject {
    private final GameProfile profile;
    private final ResourceLocation resourceLocation;

    public PlayerMenuObject(GameProfile profileIn) {
        this.profile = profileIn;
        this.resourceLocation = AbstractClientPlayer.getLocationSkin((String)profileIn.getName());
        AbstractClientPlayer.getDownloadImageSkin((ResourceLocation)this.resourceLocation, (String)profileIn.getName());
    }

    public void func_178661_a(SpectatorMenu menu) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C18PacketSpectate(this.profile.getId()));
    }

    public IChatComponent getSpectatorName() {
        return new ChatComponentText(this.profile.getName());
    }

    public void func_178663_a(float p_178663_1_, int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)((float)alpha / 255.0f));
        Gui.drawScaledCustomSizeModalRect((int)2, (int)2, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)12, (int)12, (float)64.0f, (float)64.0f);
        Gui.drawScaledCustomSizeModalRect((int)2, (int)2, (float)40.0f, (float)8.0f, (int)8, (int)8, (int)12, (int)12, (float)64.0f, (float)64.0f);
    }

    public boolean func_178662_A_() {
        return true;
    }
}
