/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.NoRender;

public class GuiBossOverlay
extends Gui {
    private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
    private final Minecraft client;
    private final Map<UUID, BossInfoClient> mapBossInfos = Maps.newLinkedHashMap();

    public GuiBossOverlay(Minecraft clientIn) {
        this.client = clientIn;
    }

    public void renderBossHealth() {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.bossBar.getCurrentValue()) {
            return;
        }
        if (!this.mapBossInfos.isEmpty()) {
            ScaledResolution scaledresolution = new ScaledResolution(this.client);
            int i = scaledresolution.getScaledWidth();
            int j = 12;
            for (BossInfoClient bossinfoclient : this.mapBossInfos.values()) {
                int k = i / 2 - 91;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.client.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
                this.render(k, j, bossinfoclient);
                String s = bossinfoclient.getName().getFormattedText();
                this.client.fontRendererObj.drawStringWithShadow(s, i / 2 - this.client.fontRendererObj.getStringWidth(s) / 2, j - 9, 0xFFFFFF);
                if ((j += 10 + this.client.fontRendererObj.FONT_HEIGHT) < scaledresolution.getScaledHeight() / 3) continue;
                break;
            }
        }
    }

    private void render(int x, int y, BossInfo info) {
        int i;
        this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2, 182, 5);
        if (info.getOverlay() != BossInfo.Overlay.PROGRESS) {
            this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
        }
        if ((i = (int)(info.getPercent() * 183.0f)) > 0) {
            this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2 + 5, i, 5);
            if (info.getOverlay() != BossInfo.Overlay.PROGRESS) {
                this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2 + 5, i, 5);
            }
        }
    }

    public void read(SPacketUpdateBossInfo packetIn) {
        if (packetIn.getOperation() == SPacketUpdateBossInfo.Operation.ADD) {
            this.mapBossInfos.put(packetIn.getUniqueId(), new BossInfoClient(packetIn));
        } else if (packetIn.getOperation() == SPacketUpdateBossInfo.Operation.REMOVE) {
            this.mapBossInfos.remove(packetIn.getUniqueId());
        } else {
            this.mapBossInfos.get(packetIn.getUniqueId()).updateFromPacket(packetIn);
        }
    }

    public void clearBossInfos() {
        this.mapBossInfos.clear();
    }

    public boolean shouldPlayEndBossMusic() {
        if (!this.mapBossInfos.isEmpty()) {
            for (BossInfo bossInfo : this.mapBossInfos.values()) {
                if (!bossInfo.shouldPlayEndBossMusic()) continue;
                return true;
            }
        }
        return false;
    }

    public boolean shouldDarkenSky() {
        if (!this.mapBossInfos.isEmpty()) {
            for (BossInfo bossInfo : this.mapBossInfos.values()) {
                if (!bossInfo.shouldDarkenSky()) continue;
                return true;
            }
        }
        return false;
    }

    public boolean shouldCreateFog() {
        if (!this.mapBossInfos.isEmpty()) {
            for (BossInfo bossInfo : this.mapBossInfos.values()) {
                if (!bossInfo.shouldCreateFog()) continue;
                return true;
            }
        }
        return false;
    }
}

