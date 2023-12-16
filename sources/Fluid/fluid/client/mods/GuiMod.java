// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods;

import com.darkmagician6.eventapi.EventManager;
import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public class GuiMod
{
    public Minecraft mc;
    public FontRenderer fr;
    public String name;
    public String description;
    public int x;
    public int y;
    public int height;
    public int width;
    public boolean enabled;
    public DraggableComponent drag;
    
    public GuiMod(final String name, final String description) {
        this.mc = Minecraft.getMinecraft();
        this.fr = this.mc.fontRendererObj;
        this.y = 0;
        this.width = 0;
        this.enabled = false;
        this.name = name;
        this.description = description;
        this.drag = new DraggableComponent(this.x, this.y, this.width, this.height, new Color(0, 0, 0, 0).getRGB());
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getX() {
        return this.drag.getxPosition();
    }
    
    public void setX(final int x1) {
        this.x = x1;
        this.drag.setxPosition(x1);
    }
    
    public void setY(final int y1) {
        this.y = y1;
        this.drag.setyPosition(y1);
    }
    
    public int getY() {
        return this.drag.getyPosition();
    }
    
    public void onEnable() {
        EventManager.register(this);
    }
    
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    public void toggle() {
        this.enabled = !this.enabled;
        if (this.enabled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
}
