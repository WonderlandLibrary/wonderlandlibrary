/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 *  com.google.gson.annotations.SerializedName
 *  net.minecraft.client.gui.ScaledResolution
 */
package ad;

import ad.ArraylistMod;
import ad.HoveringUtil;
import ad.StringUtils;
import ad.novoline.font.Fonts;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import java.util.List;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Animation;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Direction;
import net.ccbluex.liquidbounce.utils.render.miku.animations.impl.DecelerateAnimation;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.normal.Utils;
import net.minecraft.client.gui.ScaledResolution;

public class Dragging
implements Utils {
    @Expose
    @SerializedName(value="x")
    private float xPos;
    @Expose
    @SerializedName(value="y")
    private float yPos;
    public float initialXVal;
    public float initialYVal;
    private float startX;
    private float startY;
    private boolean dragging;
    private float width;
    private float height;
    @Expose
    @SerializedName(value="name")
    private String name;
    private final Module module;
    public Animation hoverAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
    private String longestModule;

    public Dragging(Module module, String name, float initialXVal, float initialYVal) {
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

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return this.xPos;
    }

    public void setX(float x) {
        this.xPos = x;
    }

    public float getY() {
        return this.yPos;
    }

    public void setY(float y) {
        this.yPos = y;
    }

    public final void onDraw(int mouseX, int mouseY) {
        boolean hovering = HoveringUtil.isHovering(this.xPos, this.yPos, this.width, this.height, mouseX, mouseY);
        if (this.dragging) {
            this.xPos = (float)mouseX - this.startX;
            this.yPos = (float)mouseY - this.startY;
        }
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!this.hoverAnimation.isDone() || this.hoverAnimation.finished(Direction.FORWARDS)) {
            RoundedUtil.drawRoundOutline(this.xPos - 4.0f, this.yPos - 4.0f, this.width + 8.0f, this.height + 8.0f, 10.0f, 2.0f, ColorUtil.applyOpacity(Color.WHITE, 0.0f), ColorUtil.applyOpacity(Color.WHITE, (float)this.hoverAnimation.getOutput()));
        }
    }

    public final void onDrawArraylist(ArraylistMod arraylistMod, int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(MinecraftInstance.mc2);
        List<Module> modules2 = StringUtils.getToggledModules(arraylistMod.modules);
        String longest = this.getLongestModule(arraylistMod);
        this.width = Fonts.posterama.posterama20.posterama20.stringWidth(longest) + 5;
        this.height = (((Float)arraylistMod.height.getValue()).floatValue() + 1.0f) * (float)modules2.size();
        float textVal = Fonts.posterama.posterama20.posterama20.stringWidth(longest);
        float xVal = (float)sr.func_78326_a() - (textVal + 8.0f + this.xPos);
        if ((float)sr.func_78326_a() - this.xPos <= (float)sr.func_78326_a() / 2.0f) {
            xVal += textVal - 2.0f;
        }
        boolean hovering = HoveringUtil.isHovering(xVal, this.yPos, this.width, this.height, mouseX, mouseY);
        if (this.dragging) {
            this.xPos = -((float)mouseX - this.startX);
            this.yPos = (float)mouseY - this.startY;
        }
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!this.hoverAnimation.isDone() || this.hoverAnimation.finished(Direction.FORWARDS)) {
            RoundedUtil.drawRoundOutline(xVal, this.yPos - 8.0f, this.width + 20.0f, this.height + 16.0f, 10.0f, 2.0f, ColorUtil.applyOpacity(Color.BLACK, (float)(0.0 * this.hoverAnimation.getOutput())), ColorUtil.applyOpacity(Color.WHITE, (float)this.hoverAnimation.getOutput()));
        }
    }

    public final void onClick(int mouseX, int mouseY, int button) {
        boolean canDrag = HoveringUtil.isHovering(this.xPos, this.yPos, this.width, this.height, mouseX, mouseY);
        if (button == 0 && canDrag) {
            this.dragging = true;
            this.startX = (int)((float)mouseX - this.xPos);
            this.startY = (int)((float)mouseY - this.yPos);
        }
    }

    public final void onClickArraylist(ArraylistMod arraylistMod, int mouseX, int mouseY, int button) {
        ScaledResolution sr = new ScaledResolution(MinecraftInstance.mc2);
        String longest = this.getLongestModule(arraylistMod);
        float textVal = Fonts.posterama.posterama20.posterama20.stringWidth(longest);
        float xVal = (float)sr.func_78326_a() - (textVal + 8.0f + this.xPos);
        if ((float)sr.func_78326_a() - this.xPos <= (float)sr.func_78326_a() / 2.0f) {
            xVal += textVal - 16.0f;
        }
        boolean canDrag = HoveringUtil.isHovering(xVal, this.yPos, this.width, this.height, mouseX, mouseY);
        if (button == 0 && canDrag) {
            this.dragging = true;
            this.startX = (int)((float)mouseX + this.xPos);
            this.startY = (int)((float)mouseY - this.yPos);
        }
    }

    public final void onRelease(int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }

    private String getLongestModule(ArraylistMod arraylistMod) {
        return arraylistMod.longest;
    }
}

