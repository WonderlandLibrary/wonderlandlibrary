// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.objects;

import java.util.List;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.Tenacity;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.module.impl.render.ArrayListMod;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.misc.HoveringUtil;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.Module;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import dev.tenacity.utils.Utils;

public class Dragging implements Utils
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
    public Animation hoverAnimation;
    private String longestModule;
    
    public Dragging(final Module module, final String name, final float initialXVal, final float initialYVal) {
        this.hoverAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
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
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getWidth() {
        return this.width;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setWidth(final float width) {
        this.width = width;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getHeight() {
        return this.height;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setHeight(final float height) {
        this.height = height;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getX() {
        return this.xPos;
    }
    
    public void setX(final float x) {
        this.xPos = x;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getY() {
        return this.yPos;
    }
    
    public void setY(final float y) {
        this.yPos = y;
    }
    
    public final void onDraw(final int mouseX, final int mouseY) {
        final boolean hovering = HoveringUtil.isHovering(this.xPos, this.yPos, this.width, this.height, mouseX, mouseY);
        if (this.dragging) {
            this.xPos = mouseX - this.startX;
            this.yPos = mouseY - this.startY;
        }
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!this.hoverAnimation.isDone() || this.hoverAnimation.finished(Direction.FORWARDS)) {
            RoundedUtil.drawRoundOutline(this.xPos - 4.0f, this.yPos - 4.0f, this.width + 8.0f, this.height + 8.0f, 10.0f, 2.0f, ColorUtil.applyOpacity(Color.WHITE, 0.0f), ColorUtil.applyOpacity(Color.WHITE, this.hoverAnimation.getOutput().floatValue()));
        }
    }
    
    public final void onDrawArraylist(final ArrayListMod arraylistMod, final int mouseX, final int mouseY) {
        final ScaledResolution sr = new ScaledResolution(Dragging.mc);
        final List<Module> modules = Tenacity.INSTANCE.getModuleCollection().getArraylistModules(arraylistMod, arraylistMod.modules);
        final String longest = this.getLongestModule(arraylistMod);
        this.width = (float)MathUtils.roundToHalf(arraylistMod.font.getStringWidth(longest) + 5.0f);
        this.height = (float)MathUtils.roundToHalf((arraylistMod.height.getValue() + 1.0) * modules.size());
        final float textVal = arraylistMod.font.getStringWidth(longest);
        float xVal = sr.getScaledWidth() - (textVal + 8.0f + this.xPos);
        if (sr.getScaledWidth() - this.xPos <= sr.getScaledWidth() / 2.0f) {
            xVal += textVal - 2.0f;
        }
        final boolean hovering = HoveringUtil.isHovering(xVal, this.yPos - 8.0f, this.width + 20.0f, this.height + 16.0f, mouseX, mouseY);
        if (this.dragging) {
            this.xPos = -(mouseX - this.startX);
            this.yPos = mouseY - this.startY;
        }
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!this.hoverAnimation.isDone() || this.hoverAnimation.finished(Direction.FORWARDS)) {
            RoundedUtil.drawRoundOutline(xVal, this.yPos - 8.0f, this.width + 20.0f, this.height + 16.0f, 10.0f, 2.0f, ColorUtil.applyOpacity(Color.BLACK, 0.0f * this.hoverAnimation.getOutput().floatValue()), ColorUtil.applyOpacity(Color.WHITE, this.hoverAnimation.getOutput().floatValue()));
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
    
    public final void onClickArraylist(final ArrayListMod arraylistMod, final int mouseX, final int mouseY, final int button) {
        final ScaledResolution sr = new ScaledResolution(Dragging.mc);
        final String longest = this.getLongestModule(arraylistMod);
        final float textVal = arraylistMod.font.getStringWidth(longest);
        float xVal = sr.getScaledWidth() - (textVal + 8.0f + this.xPos);
        if (sr.getScaledWidth() - this.xPos <= sr.getScaledWidth() / 2.0f) {
            xVal += textVal - 2.0f;
        }
        final boolean canDrag = HoveringUtil.isHovering(xVal, this.yPos - 8.0f, this.width + 20.0f, this.height + 16.0f, mouseX, mouseY);
        if (button == 0 && canDrag) {
            this.dragging = true;
            this.startX = (float)(int)(mouseX + this.xPos);
            this.startY = (float)(int)(mouseY - this.yPos);
        }
    }
    
    public final void onRelease(final int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }
    
    private String getLongestModule(final ArrayListMod arraylistMod) {
        return arraylistMod.longest;
    }
}
