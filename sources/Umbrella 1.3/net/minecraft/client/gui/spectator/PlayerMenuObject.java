/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.client.gui.spectator;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public class PlayerMenuObject
implements ISpectatorMenuObject {
    private final GameProfile field_178668_a;
    private final ResourceLocation field_178667_b;
    private static final String __OBFID = "CL_00001929";

    public PlayerMenuObject(GameProfile p_i45498_1_) {
        this.field_178668_a = p_i45498_1_;
        this.field_178667_b = AbstractClientPlayer.getLocationSkin(p_i45498_1_.getName());
        AbstractClientPlayer.getDownloadImageSkin(this.field_178667_b, p_i45498_1_.getName());
    }

    @Override
    public void func_178661_a(SpectatorMenu p_178661_1_) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C18PacketSpectate(this.field_178668_a.getId()));
    }

    @Override
    public IChatComponent func_178664_z_() {
        return new ChatComponentText(this.field_178668_a.getName());
    }

    @Override
    public void func_178663_a(float p_178663_1_, int p_178663_2_) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.field_178667_b);
        GlStateManager.color(1.0f, 1.0f, 1.0f, (float)p_178663_2_ / 255.0f);
        Gui.drawScaledCustomSizeModalRect(2, 2, 8.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
        Gui.drawScaledCustomSizeModalRect(2, 2, 40.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
    }

    @Override
    public boolean func_178662_A_() {
        return true;
    }
}

