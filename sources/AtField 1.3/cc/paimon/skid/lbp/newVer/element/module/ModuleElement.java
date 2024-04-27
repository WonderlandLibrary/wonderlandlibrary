/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.ranges.RangesKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package cc.paimon.skid.lbp.newVer.element.module;

import cc.paimon.skid.lbp.newVer.ColorManager;
import cc.paimon.skid.lbp.newVer.MouseUtils;
import cc.paimon.skid.lbp.newVer.element.components.ToggleSwitch;
import cc.paimon.skid.lbp.newVer.element.module.value.ValueElement;
import cc.paimon.skid.lbp.newVer.element.module.value.impl.BooleanElement;
import cc.paimon.skid.lbp.newVer.element.module.value.impl.FloatElement;
import cc.paimon.skid.lbp.newVer.element.module.value.impl.IntElement;
import cc.paimon.skid.lbp.newVer.element.module.value.impl.ListElement;
import cc.paimon.skid.lbp.newVer.extensions.AnimHelperKt;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.BlendUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public final class ModuleElement
extends MinecraftInstance {
    private float fadeKeybind;
    private boolean listeningToKey;
    private static final IResourceLocation expandIcon;
    private float animPercent;
    private boolean expanded;
    private float animHeight;
    private final Module module;
    private final List valueElements;
    private final ToggleSwitch toggleSwitch;
    public static final Companion Companion;

    public final void handleRelease(int n, int n2, float f, float f2, float f3, float f4) {
        if (this.expanded) {
            float f5 = f2 + f4;
            for (ValueElement valueElement : this.valueElements) {
                if (!valueElement.isDisplayable()) continue;
                valueElement.onRelease(n, n2, f + 10.0f, f5, f3 - 20.0f);
                f5 += valueElement.getValueHeight();
            }
        }
    }

    public final boolean listeningKeybind() {
        return this.listeningToKey;
    }

    public final boolean handleKeyTyped(char c, int n) {
        if (this.listeningToKey) {
            if (n == 1) {
                this.module.setKeyBind(0);
                this.listeningToKey = false;
            } else {
                this.module.setKeyBind(n);
                this.listeningToKey = false;
            }
            return true;
        }
        if (this.expanded) {
            for (ValueElement valueElement : this.valueElements) {
                if (!valueElement.isDisplayable() || !valueElement.onKeyPress(c, n)) continue;
                return true;
            }
        }
        return false;
    }

    public ModuleElement(Module module) {
        List list;
        this.module = module;
        this.toggleSwitch = new ToggleSwitch();
        ModuleElement moduleElement = this;
        boolean bl = false;
        moduleElement.valueElements = list = (List)new ArrayList();
        for (Value value : this.module.getValues()) {
            if (value instanceof BoolValue) {
                this.valueElements.add(new BooleanElement((BoolValue)value));
            }
            if (value instanceof ListValue) {
                this.valueElements.add(new ListElement((ListValue)value));
            }
            if (value instanceof IntegerValue) {
                this.valueElements.add(new IntElement((IntegerValue)value));
            }
            if (!(value instanceof FloatValue)) continue;
            this.valueElements.add(new FloatElement((FloatValue)value));
        }
    }

    public final void resetState() {
        this.listeningToKey = false;
    }

    public final void handleClick(int n, int n2, float f, float f2, float f3, float f4) {
        String string;
        if (this.listeningToKey) {
            this.resetState();
            return;
        }
        String string2 = string = this.listeningToKey ? "Listening" : Keyboard.getKeyName((int)this.module.getKeyBind());
        if (MouseUtils.mouseWithinBounds(n, n2, f + 25.0f + (float)Fonts.posterama40.getStringWidth(this.module.getName()), f2 + f4 / 2.0f - (float)Fonts.posterama40.getFontHeight() + 2.0f, f + 35.0f + (float)Fonts.posterama40.getStringWidth(this.module.getName()) + (float)Fonts.posterama30.getStringWidth(string), f2 + f4 / 2.0f)) {
            this.listeningToKey = true;
            return;
        }
        if (MouseUtils.mouseWithinBounds(n, n2, f + f3 - (this.module.getValues().size() > 0 ? 70.0f : 40.0f), f2, f + f3 - (this.module.getValues().size() > 0 ? 50.0f : 20.0f), f2 + f4)) {
            this.module.toggle();
        }
        if (this.module.getValues().size() > 0 && MouseUtils.mouseWithinBounds(n, n2, f + f3 - 40.0f, f2, f + f3 - 10.0f, f2 + f4)) {
            boolean bl = this.expanded = !this.expanded;
        }
        if (this.expanded) {
            float f5 = f2 + f4;
            for (ValueElement valueElement : this.valueElements) {
                if (!valueElement.isDisplayable()) continue;
                valueElement.onClick(n, n2, f + 10.0f, f5, f3 - 20.0f);
                f5 += valueElement.getValueHeight();
            }
        }
    }

    public final float drawElement(int n, int n2, float f, float f2, float f3, float f4, Color color) {
        Object object2;
        this.animPercent = AnimHelperKt.animSmooth(this.animPercent, this.expanded ? 100.0f : 0.0f, 0.5f);
        float f5 = 0.0f;
        for (Object object2 : this.valueElements) {
            if (!((ValueElement)object2).isDisplayable()) continue;
            f5 += ((ValueElement)object2).getValueHeight();
        }
        this.animHeight = this.animPercent / 100.0f * (f5 + 10.0f);
        RenderUtils.drawRoundedRect(f + 9.5f, f2 + 4.5f, f + f3 - 9.5f, f2 + f4 + this.animHeight - 4.5f, 4.0f, ColorManager.INSTANCE.getButtonOutline().getRGB());
        Stencil.write(true);
        RenderUtils.drawRoundedRect(f + 10.0f, f2 + 5.0f, f + f3 - 10.0f, f2 + f4 + this.animHeight - 5.0f, 4.0f, ColorManager.INSTANCE.getModuleBackground().getRGB());
        Stencil.erase(true);
        Fonts.posterama40.drawString(this.module.getName(), f + 20.0f, f2 + f4 / 2.0f - (float)Fonts.posterama40.getFontHeight() + 3.0f, new Color(26, 26, 26).getRGB());
        object2 = this.listeningToKey ? "Listening" : Keyboard.getKeyName((int)this.module.getKeyBind());
        this.fadeKeybind = MouseUtils.mouseWithinBounds(n, n2, f + 25.0f + (float)Fonts.posterama40.getStringWidth(this.module.getName()), f2 + f4 / 2.0f - (float)Fonts.posterama40.getFontHeight() + 2.0f, f + 35.0f + (float)Fonts.posterama40.getStringWidth(this.module.getName()) + (float)Fonts.posterama30.getStringWidth((String)object2), f2 + f4 / 2.0f) ? RangesKt.coerceIn((float)(this.fadeKeybind + 0.1f * (float)RenderUtils.deltaTime * 0.025f), (float)0.0f, (float)1.0f) : RangesKt.coerceIn((float)(this.fadeKeybind - 0.1f * (float)RenderUtils.deltaTime * 0.025f), (float)0.0f, (float)1.0f);
        RenderUtils.drawRoundedRect(f + 25.0f + (float)Fonts.posterama40.getStringWidth(this.module.getName()), f2 + f4 / 2.0f - (float)Fonts.posterama40.getFontHeight() + 1.0f, f + 35.0f + (float)Fonts.posterama40.getStringWidth(this.module.getName()) + (float)Fonts.posterama30.getStringWidth((String)object2), f2 + f4 / 2.0f + 4.0f, 2.0f, BlendUtils.blend(new Color((int)0xFF454545L), new Color(219, 219, 219), this.fadeKeybind).getRGB());
        Fonts.posterama30.drawString((String)object2, f + 30.5f + (float)Fonts.posterama40.getStringWidth(this.module.getName()), f2 + f4 / 2.0f - (float)Fonts.posterama40.getFontHeight() + 5.5f, new Color(26, 26, 26).getRGB());
        this.toggleSwitch.setState(this.module.getState());
        if (this.module.getValues().size() > 0) {
            RenderUtils.drawRect(f + f3 - 40.0f, f2 + 5.0f, f + f3 - 39.5f, f2 + f4 - 5.0f, (int)0xFF303030L);
            GlStateManager.func_179117_G();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(f + f3 - 25.0f), (float)(f2 + f4 / 2.0f), (float)0.0f);
            GL11.glPushMatrix();
            GL11.glRotatef((float)(180.0f * (this.animHeight / (f5 + 10.0f))), (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderUtils.drawImage(expandIcon, -4, -4, 8, 8);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            this.toggleSwitch.onDraw(f + f3 - 70.0f, f2 + f4 / 2.0f - 5.0f, 20.0f, 10.0f, new Color(241, 243, 247), color);
        } else {
            this.toggleSwitch.onDraw(f + f3 - 40.0f, f2 + f4 / 2.0f - 5.0f, 20.0f, 10.0f, new Color(241, 243, 247), color);
        }
        if (this.expanded || this.animHeight > 0.0f) {
            float f6 = f2 + f4;
            for (ValueElement valueElement : this.valueElements) {
                if (!valueElement.isDisplayable()) continue;
                f6 += valueElement.drawElement(n, n2, f + 10.0f, f6, f3 - 20.0f, new Color(241, 243, 247), color);
            }
        }
        Stencil.dispose();
        return f4 + this.animHeight;
    }

    public final void setExpanded(boolean bl) {
        this.expanded = bl;
    }

    public final float getAnimHeight() {
        return this.animHeight;
    }

    public static final IResourceLocation access$getExpandIcon$cp() {
        return expandIcon;
    }

    static {
        Companion = new Companion(null);
        expandIcon = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/expand.png");
    }

    public final void setAnimHeight(float f) {
        this.animHeight = f;
    }

    public final boolean getExpanded() {
        return this.expanded;
    }

    public final Module getModule() {
        return this.module;
    }

    public static final class Companion {
        private Companion() {
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        protected final IResourceLocation getExpandIcon() {
            return ModuleElement.access$getExpandIcon$cp();
        }
    }
}

