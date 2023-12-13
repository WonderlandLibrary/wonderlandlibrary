// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material.button;

import net.minecraft.util.ResourceLocation;
import net.augustus.utils.skid.tomorrow.RenderUtil;
import java.awt.Color;
import org.lwjgl.input.Mouse;
import net.augustus.utils.skid.tomorrow.AnimationUtils;
import net.minecraft.client.gui.Gui;

public class CButton extends Gui
{
    public boolean realized;
    private float circleR;
    public String name;
    public String image;
    private float x;
    private float y;
    private boolean n;
    private float xp;
    private float yp;
    private float width;
    private float height;
    private AnimationUtils animationUtils;
    
    public CButton(final String name, final String res, final float xp, final float yp, final float w, final float h) {
        this.circleR = 0.0f;
        this.animationUtils = new AnimationUtils();
        this.name = name;
        this.image = res;
        this.xp = xp;
        this.yp = yp;
        this.width = w;
        this.height = h;
    }
    
    public void onMouseClicked(final float mouseX, final float mouseY) {
        if (isHovered(this.x + 1.0f, this.y + 1.0f, this.x + 17.0f, this.y + 17.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            this.realized = !this.realized;
            this.n = !this.n;
        }
    }
    
    public void onRender(final float x, final float y, final float mouseX, final float mouseY) {
        this.x = x;
        this.y = y;
        this.onUpdate(mouseX, mouseY);
        if (isHovered(x + 1.0f, y + 1.0f, x + 17.0f, y + 17.0f, mouseX, mouseY)) {
            RenderUtil.drawCircle(x + 8.0f, y + 8.0f, 10.0f, new Color(30, 30, 30, 30).getRGB());
        }
        if (this.n) {
            RenderUtil.drawCircle(x + 8.0f, y + 8.0f, this.circleR, new Color(245, 245, 245, (int)(100.0f * this.circleR / 10.0f)).getRGB());
        }
        else {
            RenderUtil.drawCircle(x + 8.0f, y + 8.0f, 10.0f, new Color(245, 245, 245, (int)(100.0f * this.circleR / 10.0f)).getRGB());
        }
        RenderUtil.drawCustomImage(x + this.xp, y + this.yp, this.width, this.height, new ResourceLocation(this.image));
    }
    
    public void onUpdate(final float mouseX, final float mouseY) {
        if (this.circleR == 10.0f) {
            this.n = false;
        }
        if (this.n) {
            this.circleR = this.animationUtils.animate(10.0f, this.circleR, 0.1f);
        }
        else {
            this.circleR = this.animationUtils.animate(0.0f, this.circleR, 0.1f);
        }
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final float mouseX, final float mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
