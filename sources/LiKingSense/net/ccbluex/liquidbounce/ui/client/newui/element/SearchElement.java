/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.newui.element;

import java.awt.Color;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.ui.client.newui.ColorManager;
import net.ccbluex.liquidbounce.ui.client.newui.IconManager;
import net.ccbluex.liquidbounce.ui.client.newui.element.CategoryElement;
import net.ccbluex.liquidbounce.ui.client.newui.element.SearchBox;
import net.ccbluex.liquidbounce.ui.client.newui.element.module.ModuleElement;
import net.ccbluex.liquidbounce.ui.client.newui.extensions.AnimHelperKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\f\n\u0002\b\u0003\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\u001e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0018JT\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u00152\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#2\u0006\u0010\u0017\u001a\u00020\u0018J(\u0010%\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003H\u0002JL\u0010&\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00152\u0006\u0010'\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\u00032\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#JL\u0010(\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00152\u0006\u0010'\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\u00032\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#J\u0018\u0010)\u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0003H\u0002JD\u0010*\u001a\u00020\u00132\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\u00032\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#J\u0006\u0010.\u001a\u00020\u0013R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\n\u00a8\u0006/"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/newui/element/SearchElement;", "", "xPos", "", "yPos", "width", "height", "(FFFF)V", "animScrollHeight", "getHeight", "()F", "lastHeight", "scrollHeight", "searchBox", "Lnet/ccbluex/liquidbounce/ui/client/newui/element/SearchBox;", "getWidth", "getXPos", "getYPos", "drawBox", "", "mouseX", "", "mouseY", "accentColor", "Ljava/awt/Color;", "drawPanel", "", "mX", "mY", "x", "y", "w", "h", "wheel", "ces", "Ljava/util/List;", "Lnet/ccbluex/liquidbounce/ui/client/newui/element/CategoryElement;", "drawScroll", "handleMouseClick", "mouseButton", "handleMouseRelease", "handleScrolling", "handleTyping", "typedChar", "", "keyCode", "isTyping", "LiKingSense"})
public final class SearchElement {
    private float scrollHeight;
    private float animScrollHeight;
    private float lastHeight;
    private final SearchBox searchBox;
    private final float xPos;
    private final float yPos;
    private final float width;
    private final float height;

    public final boolean drawBox(int mouseX, int mouseY, @NotNull Color accentColor) {
        Intrinsics.checkParameterIsNotNull((Object)accentColor, (String)"accentColor");
        RenderUtils.drawRoundedRect(this.xPos - 0.5f, this.yPos - 0.5f, this.xPos + this.width + 0.5f, this.yPos + this.height + 0.5f, 4.0f, ColorManager.INSTANCE.getButtonOutline().getRGB());
        Stencil.write(true);
        RenderUtils.drawRoundedRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, 4.0f, ColorManager.INSTANCE.getTextBox().getRGB());
        Stencil.erase(true);
        if (this.searchBox.func_146206_l()) {
            RenderUtils.drawRect(this.xPos, this.yPos + this.height - 1.0f, this.xPos + this.width, this.yPos + this.height, accentColor.getRGB());
            this.searchBox.func_146194_f();
        } else if (this.searchBox.func_146179_b().length() <= 0) {
            this.searchBox.func_146180_a("Search");
            this.searchBox.func_146194_f();
            this.searchBox.func_146180_a("");
        } else {
            this.searchBox.func_146194_f();
        }
        Stencil.dispose();
        GlStateManager.func_179118_c();
        IResourceLocation iResourceLocation = IconManager.INSTANCE.getSearch();
        int n = (int)(this.xPos + this.width - 15.0f);
        int n2 = (int)(this.yPos + 5.0f);
        int n3 = 0;
        GlStateManager.func_179141_d();
        return this.searchBox.func_146179_b().length() > 0;
    }

    public final void drawPanel(int mX, int mY, float x, float y, float w, float h, int wheel, @NotNull List<CategoryElement> ces, @NotNull Color accentColor) {
        Intrinsics.checkParameterIsNotNull(ces, (String)"ces");
        Intrinsics.checkParameterIsNotNull((Object)accentColor, (String)"accentColor");
        int mouseX = mX;
        int mouseY = mY;
        this.lastHeight = 0.0f;
        for (CategoryElement ce : ces) {
            for (ModuleElement me : ce.getModuleElements()) {
                String string = me.getModule().getName();
                String string2 = this.searchBox.func_146179_b();
                Intrinsics.checkExpressionValueIsNotNull((Object)string2, (String)"searchBox.text");
                if (!StringsKt.startsWith((String)string, (String)string2, (boolean)true)) continue;
                this.lastHeight += me.getAnimHeight() + 40.0f;
            }
        }
        if (this.lastHeight >= 10.0f) {
            this.lastHeight -= 10.0f;
        }
        this.handleScrolling(wheel, h);
        this.drawScroll(x, y + 50.0f, w, h);
        Fonts.misans35.drawString("Search", x + 10.0f, y + 10.0f, -1);
        Fonts.font20.drawString("Search", x - 170.0f, y - 12.0f, -1);
        IResourceLocation iResourceLocation = IconManager.INSTANCE.getBack();
        int n = (int)(x - 190.0f);
        int n2 = (int)(y - 15.0f);
        int n3 = 0;
        float startY = y + 50.0f;
        if ((float)mouseY < y + 50.0f || (float)mouseY >= y + h) {
            mouseY = -1;
        }
        RenderUtils.makeScissorBox(x, y + 50.0f, x + w, y + h);
        GL11.glEnable((int)3089);
        for (CategoryElement ce : ces) {
            for (ModuleElement me : ce.getModuleElements()) {
                String string = me.getModule().getName();
                String string3 = this.searchBox.func_146179_b();
                Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"searchBox.text");
                if (!StringsKt.startsWith((String)string, (String)string3, (boolean)true)) continue;
                if (startY + this.animScrollHeight > y + h || startY + this.animScrollHeight + 40.0f + me.getAnimHeight() < y + 50.0f) {
                    startY += 40.0f + me.getAnimHeight();
                    continue;
                }
                startY += me.drawElement(mouseX, mouseY, x, startY + this.animScrollHeight, w, 40.0f, accentColor);
            }
        }
        GL11.glDisable((int)3089);
    }

    private final void handleScrolling(int wheel, float height) {
        if (wheel != 0) {
            this.scrollHeight = wheel > 0 ? (this.scrollHeight += 50.0f) : (this.scrollHeight -= 50.0f);
        }
        this.scrollHeight = this.lastHeight > height - 60.0f ? RangesKt.coerceIn((float)this.scrollHeight, (float)(-this.lastHeight + height - (float)60), (float)0.0f) : 0.0f;
        this.animScrollHeight = AnimHelperKt.animSmooth(this.animScrollHeight, this.scrollHeight, 0.5f);
    }

    private final void drawScroll(float x, float y, float width, float height) {
        if (this.lastHeight > height - 60.0f) {
            float last = height - 60.0f - (height - 60.0f) * ((height - 60.0f) / this.lastHeight);
            float f = this.animScrollHeight / (-this.lastHeight + height - (float)60);
            float f2 = last;
            boolean bl = false;
            float f3 = Math.abs(f);
            float multiply = f2 * RangesKt.coerceIn((float)f3, (float)0.0f, (float)1.0f);
            RenderUtils.drawRoundedRect(x + width - 6.0f, y + 5.0f + multiply, x + width - 4.0f, y + 5.0f + (height - 60.0f) * ((height - 60.0f) / this.lastHeight) + multiply, 1.0f, (int)0x50FFFFFFL);
        }
    }

    public final void handleMouseClick(int mX, int mY, int mouseButton, float x, float y, float w, float h, @NotNull List<CategoryElement> ces) {
        Intrinsics.checkParameterIsNotNull(ces, (String)"ces");
        if (MouseUtils.mouseWithinBounds(mX, mY, x - 200.0f, y - 20.0f, x - 170.0f, y)) {
            this.searchBox.func_146180_a("");
            return;
        }
        int mouseX = mX;
        int mouseY = mY;
        this.searchBox.func_146192_a(mouseX, mouseY, mouseButton);
        if (this.searchBox.func_146179_b().length() <= 0) {
            return;
        }
        if ((float)mouseY < y + 50.0f || (float)mouseY >= y + h) {
            mouseY = -1;
        }
        float startY = y + 50.0f;
        for (CategoryElement ce : ces) {
            for (ModuleElement me : ce.getModuleElements()) {
                String string = me.getModule().getName();
                String string2 = this.searchBox.func_146179_b();
                Intrinsics.checkExpressionValueIsNotNull((Object)string2, (String)"searchBox.text");
                if (!StringsKt.startsWith((String)string, (String)string2, (boolean)true)) continue;
                me.handleClick(mouseX, mouseY, x, startY + this.animScrollHeight, w, 40.0f);
                startY += 40.0f + me.getAnimHeight();
            }
        }
    }

    public final void handleMouseRelease(int mX, int mY, int mouseButton, float x, float y, float w, float h, @NotNull List<CategoryElement> ces) {
        Intrinsics.checkParameterIsNotNull(ces, (String)"ces");
        int mouseX = mX;
        int mouseY = mY;
        if (this.searchBox.func_146179_b().length() <= 0) {
            return;
        }
        if ((float)mouseY < y + 50.0f || (float)mouseY >= y + h) {
            mouseY = -1;
        }
        float startY = y + 50.0f;
        for (CategoryElement ce : ces) {
            for (ModuleElement me : ce.getModuleElements()) {
                String string = me.getModule().getName();
                String string2 = this.searchBox.func_146179_b();
                Intrinsics.checkExpressionValueIsNotNull((Object)string2, (String)"searchBox.text");
                if (!StringsKt.startsWith((String)string, (String)string2, (boolean)true)) continue;
                me.handleRelease(mouseX, mouseY, x, startY + this.animScrollHeight, w, 40.0f);
                startY += 40.0f + me.getAnimHeight();
            }
        }
    }

    public final boolean handleTyping(char typedChar, int keyCode, float x, float y, float w, float h, @NotNull List<CategoryElement> ces) {
        Intrinsics.checkParameterIsNotNull(ces, (String)"ces");
        this.searchBox.func_146201_a(typedChar, keyCode);
        if (this.searchBox.func_146179_b().length() <= 0) {
            return false;
        }
        for (CategoryElement ce : ces) {
            for (ModuleElement me : ce.getModuleElements()) {
                String string = me.getModule().getName();
                String string2 = this.searchBox.func_146179_b();
                Intrinsics.checkExpressionValueIsNotNull((Object)string2, (String)"searchBox.text");
                if (!StringsKt.startsWith((String)string, (String)string2, (boolean)true) || !me.handleKeyTyped(typedChar, keyCode)) continue;
                return true;
            }
        }
        return false;
    }

    public final boolean isTyping() {
        return this.searchBox.func_146179_b().length() > 0;
    }

    public final float getXPos() {
        return this.xPos;
    }

    public final float getYPos() {
        return this.yPos;
    }

    public final float getWidth() {
        return this.width;
    }

    public final float getHeight() {
        return this.height;
    }

    public SearchElement(float xPos, float yPos, float width, float height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.searchBox = new SearchBox(0, (int)this.xPos + 2, (int)this.yPos + 2, (int)this.width - 4, (int)this.height - 2);
    }
}

