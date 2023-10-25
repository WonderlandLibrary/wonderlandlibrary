package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
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

class TeleportToTeam.TeamSelectionObject
implements ISpectatorMenuObject {
    private final ScorePlayerTeam field_178676_b;
    private final ResourceLocation field_178677_c;
    private final List<NetworkPlayerInfo> field_178675_d;

    public TeleportToTeam.TeamSelectionObject(ScorePlayerTeam p_i45492_2_) {
        this.field_178676_b = p_i45492_2_;
        this.field_178675_d = Lists.newArrayList();
        for (String s : p_i45492_2_.getMembershipCollection()) {
            NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(s);
            if (networkplayerinfo == null) continue;
            this.field_178675_d.add((Object)networkplayerinfo);
        }
        if (!this.field_178675_d.isEmpty()) {
            String s1 = ((NetworkPlayerInfo)this.field_178675_d.get(new Random().nextInt(this.field_178675_d.size()))).getGameProfile().getName();
            this.field_178677_c = AbstractClientPlayer.getLocationSkin((String)s1);
            AbstractClientPlayer.getDownloadImageSkin((ResourceLocation)this.field_178677_c, (String)s1);
        } else {
            this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
        }
    }

    public void func_178661_a(SpectatorMenu menu) {
        menu.func_178647_a((ISpectatorMenuView)new TeleportToPlayer(this.field_178675_d));
    }

    public IChatComponent getSpectatorName() {
        return new ChatComponentText(this.field_178676_b.getTeamName());
    }

    public void func_178663_a(float p_178663_1_, int alpha) {
        int i = -1;
        String s = FontRenderer.getFormatFromString((String)this.field_178676_b.getColorPrefix());
        if (s.length() >= 2) {
            i = Minecraft.getMinecraft().fontRendererObj.getColorCode(s.charAt(1));
        }
        if (i >= 0) {
            float f = (float)(i >> 16 & 0xFF) / 255.0f;
            float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
            float f2 = (float)(i & 0xFF) / 255.0f;
            Gui.drawRect((int)1, (int)1, (int)15, (int)15, (int)(MathHelper.func_180183_b((float)(f * p_178663_1_), (float)(f1 * p_178663_1_), (float)(f2 * p_178663_1_)) | alpha << 24));
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.field_178677_c);
        GlStateManager.color((float)p_178663_1_, (float)p_178663_1_, (float)p_178663_1_, (float)((float)alpha / 255.0f));
        Gui.drawScaledCustomSizeModalRect((int)2, (int)2, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)12, (int)12, (float)64.0f, (float)64.0f);
        Gui.drawScaledCustomSizeModalRect((int)2, (int)2, (float)40.0f, (float)8.0f, (int)8, (int)8, (int)12, (int)12, (float)64.0f, (float)64.0f);
    }

    public boolean func_178662_A_() {
        return !this.field_178675_d.isEmpty();
    }
}
