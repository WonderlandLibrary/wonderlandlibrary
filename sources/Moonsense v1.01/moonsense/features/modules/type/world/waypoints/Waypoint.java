// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.waypoints;

import net.minecraft.client.Minecraft;
import java.awt.Color;
import moonsense.MoonsenseClient;
import moonsense.utils.ColorObject;
import net.minecraft.util.Vec3;

public class Waypoint
{
    public String name;
    public Vec3 location;
    public String world;
    public int dimension;
    public boolean visible;
    public boolean forced;
    public ColorObject color;
    
    public Waypoint(final String name, final Vec3 location, final String world, final int dimension, final boolean visible, final boolean forced) {
        this.color = new ColorObject(new Color(MoonsenseClient.INSTANCE.RANDOM.nextFloat(), MoonsenseClient.INSTANCE.RANDOM.nextFloat(), MoonsenseClient.INSTANCE.RANDOM.nextFloat(), 1.0f).getRGB(), false, 0);
        this.name = name;
        this.location = location;
        this.world = world;
        this.dimension = dimension;
        this.visible = visible;
        this.forced = forced;
    }
    
    public boolean canRenderWaypoint() {
        return Minecraft.getMinecraft().theWorld != null && WaypointManager.getWaypointWorld().equals(this.world) && (this.dimension == -999 || Minecraft.getMinecraft().thePlayer.dimension == this.dimension);
    }
    
    public String getName() {
        return this.name;
    }
    
    public Vec3 getLocation() {
        return this.location;
    }
    
    public String getWorld() {
        return this.world;
    }
    
    public int getDimension() {
        return this.dimension;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public boolean isForced() {
        return this.forced;
    }
    
    public ColorObject getColor() {
        return this.color;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLocation(final Vec3 location) {
        this.location = location;
    }
    
    public void setColor(final ColorObject color) {
        this.color = color;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public void toggleVisibility() {
        this.visible = !this.visible;
    }
}
