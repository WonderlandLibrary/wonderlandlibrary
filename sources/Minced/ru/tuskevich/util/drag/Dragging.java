// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.drag;

import ru.tuskevich.util.math.HoveringUtil;
import ru.tuskevich.modules.Module;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Dragging
{
    @Expose
    @SerializedName("x")
    private float xPos;
    @Expose
    @SerializedName("y")
    private float yPos;
    public float initialXVal;
    public float initialYVal;
    private float startX;
    private float startY;
    private boolean dragging;
    private float width;
    private float height;
    @Expose
    @SerializedName("name")
    private String name;
    private final Module module;
    private String longestModule;
    
    public Dragging(final Module module, final String name, final float initialXVal, final float initialYVal) {
        this.module = module;
        this.name = name;
        this.xPos = initialXVal;
        this.yPos = initialYVal;
        this.initialXVal = initialXVal;
        this.initialYVal = initialYVal;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public String getName() {
        return this.name;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public void setWidth(final float width) {
        this.width = width;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
    
    public float getX() {
        return this.xPos;
    }
    
    public void setX(final float x) {
        this.xPos = x;
    }
    
    public float getY() {
        return this.yPos;
    }
    
    public void setY(final float y) {
        this.yPos = y;
    }
    
    public final void onDraw(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.xPos = mouseX - this.startX;
            this.yPos = mouseY - this.startY;
        }
    }
    
    public final void onClick(final int mouseX, final int mouseY, final int button) {
        final boolean canDrag = HoveringUtil.isHovering(this.xPos, this.yPos, this.width, this.height, mouseX, mouseY);
        if (button == 0 && canDrag) {
            this.dragging = true;
            this.startX = (float)(int)(mouseX - this.xPos);
            this.startY = (float)(int)(mouseY - this.yPos);
        }
    }
    
    public final void onRelease(final int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }
}
