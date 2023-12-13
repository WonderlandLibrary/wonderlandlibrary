// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.spectator.categories;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.gui.FontRenderer;
import java.util.Collection;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.util.Random;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import java.util.Iterator;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;

public class TeleportToTeam implements ISpectatorMenuView, ISpectatorMenuObject
{
    private final List field_178672_a;
    private static final String __OBFID = "CL_00001920";
    
    public TeleportToTeam() {
        this.field_178672_a = Lists.newArrayList();
        final Minecraft var1 = Minecraft.getMinecraft();
        for (final ScorePlayerTeam var3 : var1.theWorld.getScoreboard().getTeams()) {
            this.field_178672_a.add(new TeamSelectionObject(var3));
        }
    }
    
    @Override
    public List func_178669_a() {
        return this.field_178672_a;
    }
    
    @Override
    public IChatComponent func_178670_b() {
        return new ChatComponentText("Select a team to teleport to");
    }
    
    @Override
    public void func_178661_a(final SpectatorMenu p_178661_1_) {
        p_178661_1_.func_178647_a(this);
    }
    
    @Override
    public IChatComponent func_178664_z_() {
        return new ChatComponentText("Teleport to team member");
    }
    
    @Override
    public void func_178663_a(final float p_178663_1_, final int p_178663_2_) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 16.0f, 0.0f, 16, 16, 256.0f, 256.0f);
    }
    
    @Override
    public boolean func_178662_A_() {
        for (final ISpectatorMenuObject var2 : this.field_178672_a) {
            if (var2.func_178662_A_()) {
                return true;
            }
        }
        return false;
    }
    
    class TeamSelectionObject implements ISpectatorMenuObject
    {
        private final ScorePlayerTeam field_178676_b;
        private final ResourceLocation field_178677_c;
        private final List field_178675_d;
        private static final String __OBFID = "CL_00001919";
        
        public TeamSelectionObject(final ScorePlayerTeam p_i45492_2_) {
            this.field_178676_b = p_i45492_2_;
            this.field_178675_d = Lists.newArrayList();
            for (final String var4 : p_i45492_2_.getMembershipCollection()) {
                final NetworkPlayerInfo var5 = Minecraft.getMinecraft().getNetHandler().func_175104_a(var4);
                if (var5 != null) {
                    this.field_178675_d.add(var5);
                }
            }
            if (!this.field_178675_d.isEmpty()) {
                final String var6 = this.field_178675_d.get(new Random().nextInt(this.field_178675_d.size())).getGameProfile().getName();
                AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c = AbstractClientPlayer.getLocationSkin(var6), var6);
            }
            else {
                this.field_178677_c = DefaultPlayerSkin.func_177335_a();
            }
        }
        
        @Override
        public void func_178661_a(final SpectatorMenu p_178661_1_) {
            p_178661_1_.func_178647_a(new TeleportToPlayer(this.field_178675_d));
        }
        
        @Override
        public IChatComponent func_178664_z_() {
            return new ChatComponentText(this.field_178676_b.func_96669_c());
        }
        
        @Override
        public void func_178663_a(final float p_178663_1_, final int p_178663_2_) {
            int var3 = -1;
            final String var4 = FontRenderer.getFormatFromString(this.field_178676_b.getColorPrefix());
            if (var4.length() >= 2) {
                var3 = Minecraft.getMinecraft().fontRendererObj.func_175064_b(var4.charAt(1));
            }
            if (var3 >= 0) {
                final float var5 = (var3 >> 16 & 0xFF) / 255.0f;
                final float var6 = (var3 >> 8 & 0xFF) / 255.0f;
                final float var7 = (var3 & 0xFF) / 255.0f;
                Gui.drawRect(1, 1, 15, 15, MathHelper.func_180183_b(var5 * p_178663_1_, var6 * p_178663_1_, var7 * p_178663_1_) | p_178663_2_ << 24);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.field_178677_c);
            GlStateManager.color(p_178663_1_, p_178663_1_, p_178663_1_, p_178663_2_ / 255.0f);
            Gui.drawScaledCustomSizeModalRect(2, 2, 8.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
            Gui.drawScaledCustomSizeModalRect(2, 2, 40.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
        }
        
        @Override
        public boolean func_178662_A_() {
            return !this.field_178675_d.isEmpty();
        }
    }
}
