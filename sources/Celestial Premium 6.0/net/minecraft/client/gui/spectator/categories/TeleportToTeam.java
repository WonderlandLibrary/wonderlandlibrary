/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TeleportToTeam
implements ISpectatorMenuView,
ISpectatorMenuObject {
    private final List<ISpectatorMenuObject> items = Lists.newArrayList();

    public TeleportToTeam() {
        Minecraft minecraft = Minecraft.getMinecraft();
        for (ScorePlayerTeam scoreplayerteam : minecraft.world.getScoreboard().getTeams()) {
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
    public void selectItem(SpectatorMenu menu) {
        menu.selectCategory(this);
    }

    @Override
    public ITextComponent getSpectatorName() {
        return new TextComponentTranslation("spectatorMenu.team_teleport", new Object[0]);
    }

    @Override
    public void renderIcon(float p_178663_1_, int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
        Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 16.0f, 0.0f, 16.0f, 16.0f, 256.0f, 256.0f);
    }

    @Override
    public boolean isEnabled() {
        for (ISpectatorMenuObject ispectatormenuobject : this.items) {
            if (!ispectatormenuobject.isEnabled()) continue;
            return true;
        }
        return false;
    }

    class TeamSelectionObject
    implements ISpectatorMenuObject {
        private final ScorePlayerTeam team;
        private final ResourceLocation location;
        private final List<NetworkPlayerInfo> players;

        public TeamSelectionObject(ScorePlayerTeam p_i45492_2_) {
            this.team = p_i45492_2_;
            this.players = Lists.newArrayList();
            for (String s : p_i45492_2_.getMembershipCollection()) {
                NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(s);
                if (networkplayerinfo == null) continue;
                this.players.add(networkplayerinfo);
            }
            if (this.players.isEmpty()) {
                this.location = DefaultPlayerSkin.getDefaultSkinLegacy();
            } else {
                String s1 = this.players.get(new Random().nextInt(this.players.size())).getGameProfile().getName();
                this.location = AbstractClientPlayer.getLocationSkin(s1);
                AbstractClientPlayer.getDownloadImageSkin(this.location, s1);
            }
        }

        @Override
        public void selectItem(SpectatorMenu menu) {
            menu.selectCategory(new TeleportToPlayer(this.players));
        }

        @Override
        public ITextComponent getSpectatorName() {
            return new TextComponentString(this.team.getTeamName());
        }

        @Override
        public void renderIcon(float p_178663_1_, int alpha) {
            int i = -1;
            String s = FontRenderer.getFormatFromString(this.team.getColorPrefix());
            if (s.length() >= 2) {
                i = Minecraft.getMinecraft().fontRendererObj.getColorCode(s.charAt(1));
            }
            if (i >= 0) {
                float f = (float)(i >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(i & 0xFF) / 255.0f;
                Gui.drawRect(1, 1, 15, 15, MathHelper.rgb(f * p_178663_1_, f1 * p_178663_1_, f2 * p_178663_1_) | alpha << 24);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.location);
            GlStateManager.color(p_178663_1_, p_178663_1_, p_178663_1_, (float)alpha / 255.0f);
            Gui.drawScaledCustomSizeModalRect(2.0f, 2.0f, 8.0f, 8.0f, 8.0f, 8.0f, 12.0f, 12.0f, 64.0f, 64.0f);
            Gui.drawScaledCustomSizeModalRect(2.0f, 2.0f, 40.0f, 8.0f, 8.0f, 8.0f, 12.0f, 12.0f, 64.0f, 64.0f);
        }

        @Override
        public boolean isEnabled() {
            return !this.players.isEmpty();
        }
    }
}

