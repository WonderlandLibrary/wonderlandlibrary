// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.hud;

import java.util.HashMap;
import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import exhibition.management.ColorManager;
import net.minecraft.entity.Entity;
import exhibition.management.friend.FriendManager;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import java.awt.Color;
import exhibition.util.StringConversions;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Mouse;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Radar extends Module
{
    private static final String SCALE = "SCALE";
    private static final String X = "X";
    private static final String Y = "Y";
    private String SIZE;
    private boolean dragging;
    float hue;
    
    public Radar(final ModuleData data) {
        super(data);
        this.SIZE = "SIZE";
        ((HashMap<String, Setting<Double>>)this.settings).put("SCALE", new Setting<Double>("SCALE", 2.0, "Scales the radar.", 0.1, 1.0, 5.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put("X", new Setting<Integer>("X", 1000, "X position for radar.", 5.0, 1.0, 1920.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put("Y", new Setting<Integer>("Y", 2, "Y position for radar.", 5.0, 1.0, 1080.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.SIZE, new Setting<Integer>(this.SIZE, 125, "Size of the radar.", 5.0, 50.0, 500.0));
    }
    
    @RegisterEvent(events = { EventRenderGui.class, EventTick.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventRenderGui) {
            final EventRenderGui er = (EventRenderGui)event;
            final int size = ((HashMap<K, Setting<Number>>)this.settings).get(this.SIZE).getValue().intValue();
            final float xOffset = ((HashMap<K, Setting<Number>>)this.settings).get("X").getValue().floatValue();
            final float yOffset = ((HashMap<K, Setting<Number>>)this.settings).get("Y").getValue().floatValue();
            final float playerOffsetX = (float)Radar.mc.thePlayer.posX;
            final float playerOffSetZ = (float)Radar.mc.thePlayer.posZ;
            final String XDD = "§cX: §f" + (int)Radar.mc.thePlayer.posX + " §cY: §f" + (int)Radar.mc.thePlayer.posY + " §cZ:§f " + (int)Radar.mc.thePlayer.posZ;
            final int strWidth = Radar.mc.fontRendererObj.getStringWidth(XDD);
            Radar.mc.fontRendererObj.drawStringWithShadow(XDD, er.getResolution().getScaledWidth() - strWidth - 2, er.getResolution().getScaledHeight() - Radar.mc.fontRendererObj.FONT_HEIGHT - 2, -1);
            final int var141 = er.getResolution().getScaledWidth();
            final int var142 = er.getResolution().getScaledHeight();
            final int mouseX = Mouse.getX() * var141 / Radar.mc.displayWidth;
            final int mouseY = var142 - Mouse.getY() * var142 / Radar.mc.displayHeight - 1;
            if (mouseX >= xOffset && mouseX <= xOffset + size && mouseY >= yOffset - 3.0f && mouseY <= yOffset + 10.0f && Mouse.getEventButton() == 0) {
                this.dragging = !this.dragging;
            }
            if (this.dragging && Radar.mc.currentScreen instanceof GuiChat) {
                final Object newValue = StringConversions.castNumber(Double.toString(mouseX - size / 2), 5);
                ((HashMap<K, Setting<Object>>)this.settings).get("X").setValue(newValue);
                final Object newValueY = StringConversions.castNumber(Double.toString(mouseY - 2), 5);
                ((HashMap<K, Setting<Object>>)this.settings).get("Y").setValue(newValueY);
            }
            else {
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
            final Color color33 = Color.getHSBColor(h / 255.0f, 0.9f, 1.0f);
            final Color color34 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
            final Color color35 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
            final int color36 = color33.getRGB();
            final int color37 = color34.getRGB();
            final int color38 = color35.getRGB();
            this.hue += 0.1;
            RenderingUtil.rectangleBordered(xOffset, yOffset, xOffset + size, yOffset + size, 0.5, Colors.getColor(90), Colors.getColor(0));
            RenderingUtil.rectangleBordered(xOffset + 1.0f, yOffset + 1.0f, xOffset + size - 1.0f, yOffset + size - 1.0f, 1.0, Colors.getColor(90), Colors.getColor(61));
            RenderingUtil.rectangleBordered(xOffset + 2.5, yOffset + 2.5, xOffset + size - 2.5, yOffset + size - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
            RenderingUtil.rectangleBordered(xOffset + 3.0f, yOffset + 3.0f, xOffset + size - 3.0f, yOffset + size - 3.0f, 0.5, Colors.getColor(27), Colors.getColor(61));
            RenderingUtil.drawGradientSideways(xOffset + 3.0f, yOffset + 3.0f, xOffset + size / 2, yOffset + 3.6, color36, color37);
            RenderingUtil.drawGradientSideways(xOffset + size / 2, yOffset + 3.0f, xOffset + size - 3.0f, yOffset + 3.6, color37, color38);
            RenderingUtil.rectangle(xOffset + (size / 2 - 0.5), yOffset + 3.5, xOffset + (size / 2 + 0.5), yOffset + size - 3.5, Colors.getColor(255, 80));
            RenderingUtil.rectangle(xOffset + 3.5, yOffset + (size / 2 - 0.5), xOffset + size - 3.5, yOffset + (size / 2 + 0.5), Colors.getColor(255, 80));
            for (final Object o : Radar.mc.theWorld.getLoadedEntityList()) {
                if (o instanceof EntityPlayer) {
                    final EntityPlayer ent = (EntityPlayer)o;
                    if (!ent.isEntityAlive() || ent == Radar.mc.thePlayer || ent.isInvisible() || ent.isInvisibleToPlayer(Radar.mc.thePlayer)) {
                        continue;
                    }
                    final float pTicks = Radar.mc.timer.renderPartialTicks;
                    final float posX = (float)((ent.posX + (ent.posX - ent.lastTickPosX) * pTicks - playerOffsetX) * ((HashMap<K, Setting<Number>>)this.settings).get("SCALE").getValue().doubleValue());
                    final float posZ = (float)((ent.posZ + (ent.posZ - ent.lastTickPosZ) * pTicks - playerOffSetZ) * ((HashMap<K, Setting<Number>>)this.settings).get("SCALE").getValue().doubleValue());
                    int color39;
                    if (FriendManager.isFriend(ent.getName())) {
                        color39 = (Radar.mc.thePlayer.canEntityBeSeen(ent) ? Colors.getColor(0, 195, 255) : Colors.getColor(0, 195, 255));
                    }
                    else {
                        color39 = (Radar.mc.thePlayer.canEntityBeSeen(ent) ? ColorManager.getEnemyVisible().getColorInt() : ColorManager.getEnemyInvisible().getColorInt());
                    }
                    final float cos = (float)Math.cos(Radar.mc.thePlayer.rotationYaw * 0.017453292519943295);
                    final float sin = (float)Math.sin(Radar.mc.thePlayer.rotationYaw * 0.017453292519943295);
                    float rotY = -(posZ * cos - posX * sin);
                    float rotX = -(posX * cos + posZ * sin);
                    if (rotY > size / 2 - 5) {
                        rotY = size / 2 - 5.0f;
                    }
                    else if (rotY < -(size / 2 - 5)) {
                        rotY = -(size / 2 - 5);
                    }
                    if (rotX > size / 2 - 5.0f) {
                        rotX = size / 2 - 5;
                    }
                    else if (rotX < -(size / 2 - 5)) {
                        rotX = -(size / 2 - 5.0f);
                    }
                    RenderingUtil.rectangleBordered(xOffset + size / 2 + rotX - 1.5, yOffset + size / 2 + rotY - 1.5, xOffset + size / 2 + rotX + 1.5, yOffset + size / 2 + rotY + 1.5, 0.5, color39, Colors.getColor(46));
                }
            }
        }
    }
}
