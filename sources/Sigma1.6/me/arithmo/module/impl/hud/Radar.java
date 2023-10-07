/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.arithmo.module.impl.hud;

import java.awt.Color;
import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.event.impl.EventTick;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.StringConversions;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import org.lwjgl.input.Mouse;

public class Radar
extends Module {
    private static final String SCALE = "SCALE";
    private static final String X = "X";
    private static final String Y = "Y";
    private String SIZE = "SIZE";
    private me.arithmo.util.Timer timer = new me.arithmo.util.Timer();
    private boolean dragging;
    float hue;

    public Radar(ModuleData data) {
        super(data);
        this.settings.put("SCALE", new Setting<Double>("SCALE", 2.0, "Scales the radar.", 0.1, 1.0, 5.0));
        this.settings.put("X", new Setting<Integer>("X", 1000, "X position for radar.", 5.0, 1.0, 1920.0));
        this.settings.put("Y", new Setting<Integer>("Y", 2, "Y position for radar.", 5.0, 1.0, 1080.0));
        this.settings.put(this.SIZE, new Setting<Integer>(this.SIZE, 125, "Size of the radar.", 5.0, 50.0, 500.0));
    }

    @RegisterEvent(events={EventRenderGui.class, EventTick.class})
    public void onEvent(Event event) {
        if (event instanceof EventRenderGui) {
            EventRenderGui er = (EventRenderGui)event;
            int size = ((Number)((Setting)this.settings.get(this.SIZE)).getValue()).intValue();
            float xOffset = ((Number)((Setting)this.settings.get("X")).getValue()).floatValue();
            float yOffset = ((Number)((Setting)this.settings.get("Y")).getValue()).floatValue();
            float playerOffsetX = (float)Radar.mc.thePlayer.posX;
            float playerOffSetZ = (float)Radar.mc.thePlayer.posZ;
            String XDD = "\u00a7cX: \u00a7f" + (int)Radar.mc.thePlayer.posX + " \u00a7cY: \u00a7f" + (int)Radar.mc.thePlayer.posY + " \u00a7cZ:\u00a7f " + (int)Radar.mc.thePlayer.posZ;
            int strWidth = Radar.mc.fontRendererObj.getStringWidth(XDD);
            Radar.mc.fontRendererObj.drawStringWithShadow(XDD, er.getResolution().getScaledWidth() - strWidth - 2, er.getResolution().getScaledHeight() - Radar.mc.fontRendererObj.FONT_HEIGHT - 2, -1);
            int var141 = er.getResolution().getScaledWidth();
            int var151 = er.getResolution().getScaledHeight();
            int mouseX = Mouse.getX() * var141 / Radar.mc.displayWidth;
            int mouseY = var151 - Mouse.getY() * var151 / Radar.mc.displayHeight - 1;
            if ((float)mouseX >= xOffset && (float)mouseX <= xOffset + (float)size && (float)mouseY >= yOffset - 3.0f && (float)mouseY <= yOffset + 10.0f && Mouse.getEventButton() == 0 && this.timer.delay(20.0f)) {
                this.timer.reset();
                boolean bl = this.dragging = !this.dragging;
            }
            if (this.dragging && Radar.mc.currentScreen instanceof GuiChat) {
                Object newValue = StringConversions.castNumber(Double.toString(mouseX - size / 2), 5);
                ((Setting)this.settings.get("X")).setValue(newValue);
                Object newValueY = StringConversions.castNumber(Double.toString(mouseY - 2), 5);
                ((Setting)this.settings.get("Y")).setValue(newValueY);
            } else {
                this.dragging = false;
            }
            if (this.hue > 255.0f) {
                this.hue = 0.0f;
            }
            float h = this.hue;
            float h2 = this.hue + 85.0f;
            float h3 = this.hue + 170.0f;
            if (h > 255.0f) {
                h = 0.0f;
            }
            if (h2 > 255.0f) {
                h2 -= 255.0f;
            }
            if (h3 > 255.0f) {
                h3 -= 255.0f;
            }
            Color color33 = Color.getHSBColor(h / 255.0f, 0.9f, 1.0f);
            Color color332 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
            Color color333 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
            int color1 = color33.getRGB();
            int color2 = color332.getRGB();
            int color3 = color333.getRGB();
            this.hue = (float)((double)this.hue + 0.1);
            RenderingUtil.rectangleBordered(xOffset, yOffset, xOffset + (float)size, yOffset + (float)size, 0.5, Colors.getColor(90), Colors.getColor(0));
            RenderingUtil.rectangleBordered(xOffset + 1.0f, yOffset + 1.0f, xOffset + (float)size - 1.0f, yOffset + (float)size - 1.0f, 1.0, Colors.getColor(90), Colors.getColor(61));
            RenderingUtil.rectangleBordered((double)xOffset + 2.5, (double)yOffset + 2.5, (double)(xOffset + (float)size) - 2.5, (double)(yOffset + (float)size) - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
            RenderingUtil.rectangleBordered(xOffset + 3.0f, yOffset + 3.0f, xOffset + (float)size - 3.0f, yOffset + (float)size - 3.0f, 0.5, Colors.getColor(27), Colors.getColor(61));
            RenderingUtil.drawGradientSideways(xOffset + 3.0f, yOffset + 3.0f, xOffset + (float)(size / 2), (double)yOffset + 3.6, color1, color2);
            RenderingUtil.drawGradientSideways(xOffset + (float)(size / 2), yOffset + 3.0f, xOffset + (float)size - 3.0f, (double)yOffset + 3.6, color2, color3);
            RenderingUtil.rectangle((double)xOffset + ((double)(size / 2) - 0.5), (double)yOffset + 3.5, (double)xOffset + ((double)(size / 2) + 0.5), (double)(yOffset + (float)size) - 3.5, Colors.getColor(255, 80));
            RenderingUtil.rectangle((double)xOffset + 3.5, (double)yOffset + ((double)(size / 2) - 0.5), (double)(xOffset + (float)size) - 3.5, (double)yOffset + ((double)(size / 2) + 0.5), Colors.getColor(255, 80));
            for (Object o : Radar.mc.theWorld.getLoadedEntityList()) {
                EntityPlayer ent;
                if (!(o instanceof EntityPlayer) || !(ent = (EntityPlayer)o).isEntityAlive() || ent == Radar.mc.thePlayer || ent.isInvisible() || ent.isInvisibleToPlayer(Radar.mc.thePlayer)) continue;
                float pTicks = Radar.mc.timer.renderPartialTicks;
                float posX = (float)((ent.posX + (ent.posX - ent.lastTickPosX) * (double)pTicks - (double)playerOffsetX) * ((Number)((Setting)this.settings.get("SCALE")).getValue()).doubleValue());
                float posZ = (float)((ent.posZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - (double)playerOffSetZ) * ((Number)((Setting)this.settings.get("SCALE")).getValue()).doubleValue());
                int color = FriendManager.isFriend(ent.getName()) ? (Radar.mc.thePlayer.canEntityBeSeen(ent) ? Colors.getColor(0, 195, 255) : Colors.getColor(0, 195, 255)) : (Radar.mc.thePlayer.canEntityBeSeen(ent) ? Colors.getColor(255, 0, 0) : Colors.getColor(255, 255, 0));
                float cos = (float)Math.cos((double)Radar.mc.thePlayer.rotationYaw * 0.017453292519943295);
                float sin = (float)Math.sin((double)Radar.mc.thePlayer.rotationYaw * 0.017453292519943295);
                float rotY = - posZ * cos - posX * sin;
                float rotX = - posX * cos + posZ * sin;
                if (rotY > (float)(size / 2 - 5)) {
                    rotY = (float)(size / 2) - 5.0f;
                } else if (rotY < (float)(- size / 2 - 5)) {
                    rotY = - size / 2 - 5;
                }
                if (rotX > (float)(size / 2) - 5.0f) {
                    rotX = size / 2 - 5;
                } else if (rotX < (float)(- size / 2 - 5)) {
                    rotX = - (float)(size / 2) - 5.0f;
                }
                RenderingUtil.rectangleBordered((double)(xOffset + (float)(size / 2) + rotX) - 1.5, (double)(yOffset + (float)(size / 2) + rotY) - 1.5, (double)(xOffset + (float)(size / 2) + rotX) + 1.5, (double)(yOffset + (float)(size / 2) + rotY) + 1.5, 0.5, color, Colors.getColor(46));
            }
        }
    }
}

