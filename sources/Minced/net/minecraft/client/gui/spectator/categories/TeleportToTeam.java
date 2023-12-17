// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator.categories;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextComponentString;
import java.util.Collection;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.util.Random;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.ITextComponent;
import java.util.Iterator;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;

public class TeleportToTeam implements ISpectatorMenuView, ISpectatorMenuObject
{
    private final List<ISpectatorMenuObject> items;
    
    public TeleportToTeam() {
        this.items = (List<ISpectatorMenuObject>)Lists.newArrayList();
        final Minecraft minecraft = Minecraft.getMinecraft();
        for (final ScorePlayerTeam scoreplayerteam : minecraft.world.getScoreboard().getTeams()) {
            this.items.add(new TeamSelectionObject(scoreplayerteam));
        }
    }
    
    @Override
    public List<ISpectatorMenuObject> getItems() {
        return this.items;
    }
    
    @Override
    public ITextComponent getPrompt() {
        return new TextComponentTranslation("spectatorMenu.team_teleport.prompt", new Object[0]);
    }
    
    @Override
    public void selectItem(final SpectatorMenu menu) {
        menu.selectCategory(this);
    }
    
    @Override
    public ITextComponent getSpectatorName() {
        return new TextComponentTranslation("spectatorMenu.team_teleport", new Object[0]);
    }
    
    @Override
    public void renderIcon(final float brightness, final int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
        Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 16.0f, 0.0f, 16.0f, 16.0f, 256.0f, 256.0f);
    }
    
    @Override
    public boolean isEnabled() {
        for (final ISpectatorMenuObject ispectatormenuobject : this.items) {
            if (ispectatormenuobject.isEnabled()) {
                return true;
            }
        }
        return false;
    }
    
    class TeamSelectionObject implements ISpectatorMenuObject
    {
        private final ScorePlayerTeam team;
        private final ResourceLocation location;
        private final List<NetworkPlayerInfo> players;
        
        public TeamSelectionObject(final ScorePlayerTeam teamIn) {
            this.team = teamIn;
            this.players = (List<NetworkPlayerInfo>)Lists.newArrayList();
            for (final String s : teamIn.getMembershipCollection()) {
                final NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(s);
                if (networkplayerinfo != null) {
                    this.players.add(networkplayerinfo);
                }
            }
            if (this.players.isEmpty()) {
                this.location = DefaultPlayerSkin.getDefaultSkinLegacy();
            }
            else {
                final String s2 = this.players.get(new Random().nextInt(this.players.size())).getGameProfile().getName();
                AbstractClientPlayer.getDownloadImageSkin(this.location = AbstractClientPlayer.getLocationSkin(s2), s2);
            }
        }
        
        @Override
        public void selectItem(final SpectatorMenu menu) {
            menu.selectCategory(new TeleportToPlayer(this.players));
        }
        
        @Override
        public ITextComponent getSpectatorName() {
            return new TextComponentString(this.team.getDisplayName());
        }
        
        @Override
        public void renderIcon(final float brightness, final int alpha) {
            int i = -1;
            final String s = FontRenderer.getFormatFromString(this.team.getPrefix());
            if (s.length() >= 2) {
                i = Minecraft.getMinecraft().fontRenderer.getColorCode(s.charAt(1));
            }
            if (i >= 0) {
                final float f = (i >> 16 & 0xFF) / 255.0f;
                final float f2 = (i >> 8 & 0xFF) / 255.0f;
                final float f3 = (i & 0xFF) / 255.0f;
                Gui.drawRect(1.0f, 1.0f, 15.0f, 15.0f, MathHelper.rgb(f * brightness, f2 * brightness, f3 * brightness) | alpha << 24);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.location);
            GlStateManager.color(brightness, brightness, brightness, alpha / 255.0f);
            Gui.drawScaledCustomSizeModalRect(2.0f, 2.0f, 8.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
            Gui.drawScaledCustomSizeModalRect(2.0f, 2.0f, 40.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
        }
        
        @Override
        public boolean isEnabled() {
            return !this.players.isEmpty();
        }
    }
}
