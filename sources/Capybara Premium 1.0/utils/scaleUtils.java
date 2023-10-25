package fun.rich.client.utils;

import fun.rich.client.utils.other.Vec2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class scaleUtils {

    private int scale;

    public scaleUtils(int scale){
        this.scale = scale;
    }

    public void pushScale(){
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        double scale = rs.getScaleFactor() / Math.pow(rs.getScaleFactor(), 2);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale * this.scale, scale * this.scale, scale * this.scale);
    }

    public int calc(int value) {
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return value * rs.getScaleFactor() / this.scale;
    }

    public void popScale(){
        GlStateManager.scale(this.scale, this.scale, this.scale);
        GlStateManager.popMatrix();
    }

    public Vec2i getMouse(int mouseX, int mouseY, ScaledResolution rs){
        return new Vec2i(mouseX * rs.getScaleFactor() / this.scale, mouseY * rs.getScaleFactor() / this.scale);
    }

    public int getScale(){
        return this.scale;
    }
    public void setScale(int scale){
        this.scale = scale;
    }
}
