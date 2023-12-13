// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import java.util.UUID;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import com.elementars.eclient.event.events.LocalPlayerUpdateEvent;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.event.events.RenderEvent;
import net.minecraft.client.gui.FontRenderer;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.Xulu;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.elementars.eclient.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.util.Pair;
import com.elementars.eclient.command.Command;
import com.elementars.eclient.event.events.EventPlayerConnect;
import com.elementars.eclient.util.ColorTextUtils;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.util.SpotSet;
import dev.xulu.settings.Value;
import com.elementars.eclient.util.RenderUtils;
import com.elementars.eclient.module.Module;

public class LogoutSpots extends Module
{
    private final RenderUtils renderUtils;
    private final Value<Boolean> cf;
    private final Value<Boolean> render;
    private final Value<Boolean> box;
    private final Value<Boolean> coords;
    private final Value<Integer> max_distance;
    private final Value<Boolean> print_message;
    private final Value<Boolean> watermark;
    private final Value<String> color;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private final SpotSet spots;
    
    public LogoutSpots() {
        super("LogoutSpot", "show where a player logs out", 0, Category.RENDER, true);
        this.renderUtils = new RenderUtils();
        this.cf = this.register(new Value<Boolean>("Custom Font", this, false));
        this.render = this.register(new Value<Boolean>("Render", this, true));
        this.box = this.register(new Value<Boolean>("Box", this, false));
        this.coords = this.register(new Value<Boolean>("Coordinates", this, true));
        this.max_distance = this.register(new Value<Integer>("Max Distance", this, 320, 0, 1000));
        this.print_message = this.register(new Value<Boolean>("Print Message", this, true));
        this.watermark = this.register(new Value<Boolean>("Watermark", this, true));
        this.color = this.register(new Value<String>("Color", this, "White", ColorTextUtils.colors));
        this.red = this.register(new Value<Integer>("Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 0, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 0, 0, 255));
        this.spots = new SpotSet();
    }
    
    private void reset() {
        synchronized (this.spots) {
            this.spots.clear();
        }
    }
    
    @Override
    public void onEnable() {
        LogoutSpots.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        LogoutSpots.EVENT_BUS.unregister((Object)this);
        this.reset();
    }
    
    @EventTarget
    public void onPlayerConnect(final EventPlayerConnect.Join event) {
        synchronized (this.spots) {
            final Pair<Boolean, LogoutPos> check = this.spots.removeIfReturn(spot -> spot.getId().equals(event.getUuid()));
            if (check.getKey() && this.print_message.getValue()) {
                final double x = check.getValue().player.lastTickPosX;
                final double y = check.getValue().player.lastTickPosY;
                final double z = check.getValue().player.lastTickPosZ;
                if (this.watermark.getValue()) {
                    Command.sendChatMessage(ColorTextUtils.getColor(this.color.getValue()) + String.format("%s has joined (%s, %s, %s)!", event.getName(), (int)x, (int)y, (int)z));
                }
                else {
                    Command.sendRawChatMessage(ColorTextUtils.getColor(this.color.getValue()) + String.format("%s has joined (%s, %s, %s)!", event.getName(), (int)x, (int)y, (int)z));
                }
            }
        }
    }
    
    @EventTarget
    public void onPlayerDisconnect(final EventPlayerConnect.Leave event) {
        if (LogoutSpots.mc.world == null) {
            return;
        }
        final EntityPlayer player = LogoutSpots.mc.world.getPlayerEntityByUUID(event.getUuid());
        if (player != null && LogoutSpots.mc.player != null && !LogoutSpots.mc.player.equals((Object)player)) {
            final AxisAlignedBB bb = player.getEntityBoundingBox();
            synchronized (this.spots) {
                if (this.spots.add(new LogoutPos(event.getUuid(), player.getName(), new Vec3d(bb.maxX, bb.maxY, bb.maxZ), new Vec3d(bb.minX, bb.minY, bb.minZ), bb, player)) && this.print_message.getValue()) {
                    if (this.watermark.getValue()) {
                        Command.sendChatMessage(ColorTextUtils.getColor(this.color.getValue()) + String.format("%s has disconnected!", player.getName()));
                    }
                    else {
                        Command.sendRawChatMessage(ColorTextUtils.getColor(this.color.getValue()) + String.format("%s has disconnected!", player.getName()));
                    }
                }
            }
        }
    }
    
    public void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        GlStateManager.pushMatrix();
        final FontRenderer var13 = Wrapper.getMinecraft().fontRenderer;
        String name = player.getName() + " " + (this.coords.getValue() ? (ChatFormatting.GRAY + "(" + (int)player.posX + ", " + (int)player.posY + ", " + (int)player.posZ + ")") : (ChatFormatting.GRAY + "" + Math.round(LogoutSpots.mc.player.getDistance((Entity)player))));
        name = name.replace(".0", "");
        final float distance = LogoutSpots.mc.player.getDistance((Entity)player);
        final float var14 = ((distance / 5.0f <= 2.0f) ? 2.0f : (distance / 5.0f * 1.5075471f)) * 2.5f * 0.005075472f;
        GL11.glTranslated((double)(float)x, (float)y + 2.5 + ((distance / 5.0f > 2.0f) ? (distance / 12.0f - 0.7) : 0.0), (double)(float)z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-LogoutSpots.mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(LogoutSpots.mc.renderManager.playerViewX, (LogoutSpots.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-var14, -var14, var14);
        this.renderUtils.disableGlCap(2896, 2929);
        this.renderUtils.enableGlCap(3042);
        GL11.glBlendFunc(770, 771);
        int width;
        if (this.cf.getValue()) {
            width = Xulu.cFontRenderer.getStringWidth(name) / 2 + 1;
        }
        else {
            width = var13.getStringWidth(name) / 2;
        }
        if (this.box.getValue()) {
            Gui.drawRect(-width - 2, 10, width + 1, 20, ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 120));
        }
        if (this.cf.getValue()) {
            Xulu.cFontRenderer.drawStringWithShadow(name, -width, 10.0, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        }
        else {
            LogoutSpots.mc.fontRenderer.drawStringWithShadow(name, (float)(-width), 11.0f, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        }
        this.renderUtils.resetCaps();
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (!this.render.getValue()) {
            return;
        }
        GlStateManager.pushMatrix();
        synchronized (this.spots) {
            final double x;
            final double y;
            final double z;
            final AxisAlignedBB entityBox;
            final AxisAlignedBB axisAlignedBB;
            this.spots.forEach(logoutPos -> {
                x = logoutPos.lastTickPosX + (logoutPos.posX - logoutPos.lastTickPosX) * LogoutSpots.mc.timer.renderPartialTicks - LogoutSpots.mc.renderManager.renderPosX;
                y = logoutPos.lastTickPosY + (logoutPos.posY - logoutPos.lastTickPosY) * LogoutSpots.mc.timer.renderPartialTicks - LogoutSpots.mc.renderManager.renderPosY;
                z = logoutPos.lastTickPosZ + (logoutPos.posZ - logoutPos.lastTickPosZ) * LogoutSpots.mc.timer.renderPartialTicks - LogoutSpots.mc.renderManager.renderPosZ;
                entityBox = logoutPos.bb;
                axisAlignedBB = new AxisAlignedBB(entityBox.minX - logoutPos.posX + x - 0.05, entityBox.minY - logoutPos.posY + y, entityBox.minZ - logoutPos.posZ + z - 0.05, entityBox.maxX - logoutPos.posX + x + 0.05, entityBox.maxY - logoutPos.posY + y + 0.15, entityBox.maxZ - logoutPos.posZ + z + 0.05);
                XuluTessellator.drawBoundingBox(axisAlignedBB, 1.5f, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
                this.renderNametag(logoutPos.player, x, y, z);
                return;
            });
        }
        GlStateManager.popMatrix();
    }
    
    @EventTarget
    public void onPlayerUpdate(final LocalPlayerUpdateEvent event) {
        if (this.max_distance.getValue() > 0) {
            synchronized (this.spots) {
                this.spots.removeIf(pos -> LogoutSpots.mc.player.getPositionVector().distanceTo(pos.getTopVec()) > this.max_distance.getValue());
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        this.reset();
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        this.reset();
    }
    
    public class LogoutPos
    {
        final UUID id;
        final String name;
        final Vec3d maxs;
        final Vec3d mins;
        final AxisAlignedBB bb;
        final EntityPlayer player;
        final double posX;
        final double posY;
        final double posZ;
        final double lastTickPosX;
        final double lastTickPosY;
        final double lastTickPosZ;
        
        private LogoutPos(final UUID uuid, final String name, final Vec3d maxs, final Vec3d mins, final AxisAlignedBB bb, final EntityPlayer player) {
            this.id = uuid;
            this.name = name;
            this.maxs = maxs;
            this.mins = mins;
            this.bb = bb;
            this.player = player;
            this.posX = player.posX;
            this.posY = player.posY;
            this.posZ = player.posZ;
            this.lastTickPosX = player.lastTickPosX;
            this.lastTickPosY = player.lastTickPosY;
            this.lastTickPosZ = player.lastTickPosZ;
        }
        
        public EntityPlayer getPlayer() {
            return this.player;
        }
        
        public AxisAlignedBB getBb() {
            return this.bb;
        }
        
        public UUID getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Vec3d getMaxs() {
            return this.maxs;
        }
        
        public Vec3d getMins() {
            return this.mins;
        }
        
        public Vec3d getTopVec() {
            return new Vec3d((this.getMins().x + this.getMaxs().x) / 2.0, this.getMaxs().y, (this.getMins().z + this.getMaxs().z) / 2.0);
        }
        
        @Override
        public boolean equals(final Object other) {
            return this == other || (other instanceof LogoutPos && this.getId().equals(((LogoutPos)other).getId()));
        }
        
        @Override
        public int hashCode() {
            return this.getId().hashCode();
        }
    }
}
