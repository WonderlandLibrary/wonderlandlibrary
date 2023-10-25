/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TeleportToTeam
implements ISpectatorMenuView,
ISpectatorMenuObject {
    private final List<ISpectatorMenuObject> field_178672_a = Lists.newArrayList();

    @Override
    public List<ISpectatorMenuObject> func_178669_a() {
        return this.field_178672_a;
    }

    @Override
    public boolean func_178662_A_() {
        for (ISpectatorMenuObject iSpectatorMenuObject : this.field_178672_a) {
            if (!iSpectatorMenuObject.func_178662_A_()) continue;
            return true;
        }
        return false;
    }

    @Override
    public void func_178661_a(SpectatorMenu spectatorMenu) {
        spectatorMenu.func_178647_a(this);
    }

    @Override
    public IChatComponent getSpectatorName() {
        return new ChatComponentText("Teleport to team member");
    }

    public TeleportToTeam() {
        Minecraft minecraft = Minecraft.getMinecraft();
        for (ScorePlayerTeam scorePlayerTeam : Minecraft.theWorld.getScoreboard().getTeams()) {
            this.field_178672_a.add(new TeamSelectionObject(scorePlayerTeam));
        }
    }

    @Override
    public void func_178663_a(float f, int n) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 16.0f, 0.0f, 16, 16, 256.0f, 256.0f);
    }

    @Override
    public IChatComponent func_178670_b() {
        return new ChatComponentText("Select a team to teleport to");
    }

    class TeamSelectionObject
    implements ISpectatorMenuObject {
        private final List<NetworkPlayerInfo> field_178675_d;
        private final ResourceLocation field_178677_c;
        private final ScorePlayerTeam field_178676_b;

        @Override
        public IChatComponent getSpectatorName() {
            return new ChatComponentText(this.field_178676_b.getTeamName());
        }

        @Override
        public void func_178663_a(float f, int n) {
            int n2 = -1;
            String string = FontRenderer.getFormatFromString(this.field_178676_b.getColorPrefix());
            if (string.length() >= 2) {
                Minecraft.getMinecraft();
                n2 = Minecraft.fontRendererObj.getColorCode(string.charAt(1));
            }
            if (n2 >= 0) {
                float f2 = (float)(n2 >> 16 & 0xFF) / 255.0f;
                float f3 = (float)(n2 >> 8 & 0xFF) / 255.0f;
                float f4 = (float)(n2 & 0xFF) / 255.0f;
                Gui.drawRect(1.0, 1.0, 15.0, 15.0, MathHelper.func_180183_b(f2 * f, f3 * f, f4 * f) | n << 24);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.field_178677_c);
            GlStateManager.color(f, f, f, (float)n / 255.0f);
            Gui.drawScaledCustomSizeModalRect(2, 2, 8.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
            Gui.drawScaledCustomSizeModalRect(2, 2, 40.0f, 8.0f, 8, 8, 12, 12, 64.0f, 64.0f);
        }

        public TeamSelectionObject(ScorePlayerTeam scorePlayerTeam) {
            this.field_178676_b = scorePlayerTeam;
            this.field_178675_d = Lists.newArrayList();
            for (String string : scorePlayerTeam.getMembershipCollection()) {
                NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(string);
                if (networkPlayerInfo == null) continue;
                this.field_178675_d.add(networkPlayerInfo);
            }
            if (!this.field_178675_d.isEmpty()) {
                String string;
                string = this.field_178675_d.get(new Random().nextInt(this.field_178675_d.size())).getGameProfile().getName();
                this.field_178677_c = AbstractClientPlayer.getLocationSkin(string);
                AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c, string);
            } else {
                this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
            }
        }

        @Override
        public void func_178661_a(SpectatorMenu spectatorMenu) {
            spectatorMenu.func_178647_a(new TeleportToPlayer(this.field_178675_d));
        }

        @Override
        public boolean func_178662_A_() {
            return !this.field_178675_d.isEmpty();
        }
    }
}

