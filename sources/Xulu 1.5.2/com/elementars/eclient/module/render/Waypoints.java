// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import java.util.UUID;
import java.util.HashSet;
import com.elementars.eclient.util.XuluTessellator;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.event.events.RenderEvent;
import com.elementars.eclient.util.Plane;
import java.awt.Color;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.VectorUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.elementars.eclient.module.Category;
import java.util.Set;
import dev.xulu.settings.Value;
import com.elementars.eclient.util.RenderUtils;
import com.elementars.eclient.module.Module;

public class Waypoints extends Module
{
    private final RenderUtils renderUtils;
    private final Value<Boolean> cf;
    private final Value<Boolean> render;
    private final Value<String> mode;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private final Value<Integer> alpha;
    public static final Set<Waypoint> WAYPOINTS;
    
    public Waypoints() {
        super("Waypoints", "Shows locations of waypoints", 0, Category.RENDER, true);
        this.renderUtils = new RenderUtils();
        this.cf = this.register(new Value<Boolean>("Custom Font", this, false));
        this.render = this.register(new Value<Boolean>("Render", this, true));
        this.mode = this.register(new Value<String>("Mode", this, "Coordinates", new String[] { "Coordinates", "Distance", "Safe" }));
        this.red = this.register(new Value<Integer>("Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 0, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 0, 0, 255));
        this.alpha = this.register(new Value<Integer>("Alpha", this, 150, 0, 255));
    }
    
    @Override
    public void onEnable() {
        Waypoints.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        Waypoints.EVENT_BUS.unregister((Object)this);
    }
    
    private void renderNametag2(final Waypoint waypoint) {
        final String name = waypoint.getName() + (this.mode.getValue().equalsIgnoreCase("Safe") ? "" : (" " + (this.mode.getValue().equalsIgnoreCase("Coordinates") ? (ChatFormatting.GRAY + "(" + waypoint.getPos().x + ", " + waypoint.getPos().y + ", " + waypoint.getPos().z + ")") : (ChatFormatting.GRAY + "" + Math.round(Waypoints.mc.player.getDistance((double)waypoint.getPos().x, (double)waypoint.getPos().y, (double)waypoint.getPos().z))))));
        final Plane pos = VectorUtils.toScreen(waypoint.getPos().getX() + 0.5, waypoint.getPos().getY() + 1.5, waypoint.getPos().getZ() + 0.5);
        if (this.cf.getValue()) {
            Xulu.cFontRenderer.drawStringWithShadow(name, (float)pos.getX() - Xulu.cFontRenderer.getStringWidth(name) / 2, (float)pos.getY() - Xulu.cFontRenderer.getHeight() / 2.0f, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        }
        else {
            Waypoints.fontRenderer.drawStringWithShadow(name, (float)pos.getX() - Waypoints.fontRenderer.getStringWidth(name) / 2, (float)pos.getY() - Waypoints.fontRenderer.FONT_HEIGHT / 2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        }
    }
    
    @Override
    public void onRender() {
        synchronized (Waypoints.WAYPOINTS) {
            Waypoints.WAYPOINTS.forEach(waypoint -> {
                if (Waypoints.mc.player.dimension == waypoint.dimension) {
                    this.renderNametag2(waypoint);
                }
            });
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (!this.render.getValue()) {
            return;
        }
        GlStateManager.pushMatrix();
        synchronized (Waypoints.WAYPOINTS) {
            Waypoints.WAYPOINTS.forEach(waypoint -> {
                if (Waypoints.mc.player.dimension == waypoint.dimension) {
                    XuluTessellator.prepare(7);
                    XuluTessellator.drawBox(waypoint.getPos(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()).getRGB(), 63);
                    XuluTessellator.release();
                }
                return;
            });
        }
        GlStateManager.popMatrix();
    }
    
    static {
        WAYPOINTS = new HashSet<Waypoint>();
    }
    
    public static class Waypoint
    {
        final UUID id;
        final String name;
        final BlockPos pos;
        final AxisAlignedBB bb;
        final int dimension;
        
        public Waypoint(final UUID id, final String name, final BlockPos pos, final AxisAlignedBB bb, final int dimension) {
            this.id = id;
            this.name = name;
            this.pos = pos;
            this.bb = bb;
            this.dimension = dimension;
        }
        
        public AxisAlignedBB getBb() {
            return this.bb;
        }
        
        public UUID getId() {
            return this.id;
        }
        
        public BlockPos getPos() {
            return this.pos;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getDimension() {
            return this.dimension;
        }
        
        @Override
        public boolean equals(final Object other) {
            return this == other || (other instanceof Waypoint && this.getId().equals(((Waypoint)other).getId()));
        }
        
        @Override
        public int hashCode() {
            return this.getId().hashCode();
        }
    }
}
