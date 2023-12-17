// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.ISound;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.GlStateManager;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISoundEventListener;

public class GuiSubtitleOverlay extends Gui implements ISoundEventListener
{
    private final Minecraft client;
    private final List<Subtitle> subtitles;
    private boolean enabled;
    
    public GuiSubtitleOverlay(final Minecraft clientIn) {
        this.subtitles = (List<Subtitle>)Lists.newArrayList();
        this.client = clientIn;
    }
    
    public void renderSubtitles(final ScaledResolution resolution) {
        if (!this.enabled && this.client.gameSettings.showSubtitles) {
            this.client.getSoundHandler().addListener(this);
            this.enabled = true;
        }
        else if (this.enabled && !this.client.gameSettings.showSubtitles) {
            this.client.getSoundHandler().removeListener(this);
            this.enabled = false;
        }
        if (this.enabled && !this.subtitles.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            final Minecraft client = this.client;
            final double posX = Minecraft.player.posX;
            final Minecraft client2 = this.client;
            final double posY = Minecraft.player.posY;
            final Minecraft client3 = this.client;
            final double yIn = posY + Minecraft.player.getEyeHeight();
            final Minecraft client4 = this.client;
            final Vec3d vec3d = new Vec3d(posX, yIn, Minecraft.player.posZ);
            final Vec3d vec3d6 = new Vec3d(0.0, 0.0, -1.0);
            final Minecraft client5 = this.client;
            final Vec3d rotatePitch = vec3d6.rotatePitch(-Minecraft.player.rotationPitch * 0.017453292f);
            final Minecraft client6 = this.client;
            final Vec3d vec3d2 = rotatePitch.rotateYaw(-Minecraft.player.rotationYaw * 0.017453292f);
            final Vec3d vec3d7 = new Vec3d(0.0, 1.0, 0.0);
            final Minecraft client7 = this.client;
            final Vec3d rotatePitch2 = vec3d7.rotatePitch(-Minecraft.player.rotationPitch * 0.017453292f);
            final Minecraft client8 = this.client;
            final Vec3d vec3d3 = rotatePitch2.rotateYaw(-Minecraft.player.rotationYaw * 0.017453292f);
            final Vec3d vec3d4 = vec3d2.crossProduct(vec3d3);
            int i = 0;
            int j = 0;
            final Iterator<Subtitle> iterator = this.subtitles.iterator();
            while (iterator.hasNext()) {
                final Subtitle guisubtitleoverlay$subtitle = iterator.next();
                if (guisubtitleoverlay$subtitle.getStartTime() + 3000L <= Minecraft.getSystemTime()) {
                    iterator.remove();
                }
                else {
                    j = Math.max(j, this.client.fontRenderer.getStringWidth(guisubtitleoverlay$subtitle.getString()));
                }
            }
            j = j + this.client.fontRenderer.getStringWidth("<") + this.client.fontRenderer.getStringWidth(" ") + this.client.fontRenderer.getStringWidth(">") + this.client.fontRenderer.getStringWidth(" ");
            for (final Subtitle guisubtitleoverlay$subtitle2 : this.subtitles) {
                final int k = 255;
                final String s = guisubtitleoverlay$subtitle2.getString();
                final Vec3d vec3d5 = guisubtitleoverlay$subtitle2.getLocation().subtract(vec3d).normalize();
                final double d0 = -vec3d4.dotProduct(vec3d5);
                final double d2 = -vec3d2.dotProduct(vec3d5);
                final boolean flag = d2 > 0.5;
                final int l = j / 2;
                final int i2 = this.client.fontRenderer.FONT_HEIGHT;
                final int j2 = i2 / 2;
                final float f = 1.0f;
                final int k2 = this.client.fontRenderer.getStringWidth(s);
                final int l2 = MathHelper.floor(MathHelper.clampedLerp(255.0, 75.0, (Minecraft.getSystemTime() - guisubtitleoverlay$subtitle2.getStartTime()) / 3000.0f));
                final int i3 = l2 << 16 | l2 << 8 | l2;
                GlStateManager.pushMatrix();
                GlStateManager.translate(resolution.getScaledWidth() - l * 1.0f - 2.0f, resolution.getScaledHeight() - 30 - i * (i2 + 1) * 1.0f, 0.0f);
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                Gui.drawRect((float)(-l - 1), (float)(-j2 - 1), (float)(l + 1), (float)(j2 + 1), -872415232);
                GlStateManager.enableBlend();
                if (!flag) {
                    if (d0 > 0.0) {
                        this.client.fontRenderer.drawString(">", l - this.client.fontRenderer.getStringWidth(">"), -j2, i3 - 16777216);
                    }
                    else if (d0 < 0.0) {
                        this.client.fontRenderer.drawString("<", -l, -j2, i3 - 16777216);
                    }
                }
                this.client.fontRenderer.drawString(s, -k2 / 2, -j2, i3 - 16777216);
                GlStateManager.popMatrix();
                ++i;
            }
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void soundPlay(final ISound soundIn, final SoundEventAccessor accessor) {
        if (accessor.getSubtitle() != null) {
            final String s = accessor.getSubtitle().getFormattedText();
            if (!this.subtitles.isEmpty()) {
                for (final Subtitle guisubtitleoverlay$subtitle : this.subtitles) {
                    if (guisubtitleoverlay$subtitle.getString().equals(s)) {
                        guisubtitleoverlay$subtitle.refresh(new Vec3d(soundIn.getXPosF(), soundIn.getYPosF(), soundIn.getZPosF()));
                        return;
                    }
                }
            }
            this.subtitles.add(new Subtitle(s, new Vec3d(soundIn.getXPosF(), soundIn.getYPosF(), soundIn.getZPosF())));
        }
    }
    
    public class Subtitle
    {
        private final String subtitle;
        private long startTime;
        private Vec3d location;
        
        public Subtitle(final String subtitleIn, final Vec3d locationIn) {
            this.subtitle = subtitleIn;
            this.location = locationIn;
            this.startTime = Minecraft.getSystemTime();
        }
        
        public String getString() {
            return this.subtitle;
        }
        
        public long getStartTime() {
            return this.startTime;
        }
        
        public Vec3d getLocation() {
            return this.location;
        }
        
        public void refresh(final Vec3d locationIn) {
            this.location = locationIn;
            this.startTime = Minecraft.getSystemTime();
        }
    }
}
