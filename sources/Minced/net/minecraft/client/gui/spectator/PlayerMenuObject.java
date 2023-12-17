// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import com.mojang.authlib.GameProfile;

public class PlayerMenuObject implements ISpectatorMenuObject
{
    private final GameProfile profile;
    private final ResourceLocation resourceLocation;
    
    public PlayerMenuObject(final GameProfile profileIn) {
        this.profile = profileIn;
        AbstractClientPlayer.getDownloadImageSkin(this.resourceLocation = AbstractClientPlayer.getLocationSkin(profileIn.getName()), profileIn.getName());
    }
    
    @Override
    public void selectItem(final SpectatorMenu menu) {
        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketSpectate(this.profile.getId()));
    }
    
    @Override
    public ITextComponent getSpectatorName() {
        return new TextComponentString(this.profile.getName());
    }
    
    @Override
    public void renderIcon(final float brightness, final int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha / 255.0f);
        Gui.drawScaledCustomSizeModalRect(2.0f, 2.0f, 8.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
        Gui.drawScaledCustomSizeModalRect(2.0f, 2.0f, 40.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
