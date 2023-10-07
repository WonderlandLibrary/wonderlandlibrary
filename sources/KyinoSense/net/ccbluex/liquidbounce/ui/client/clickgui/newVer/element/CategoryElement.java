/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.ColorManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.ModuleElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.extensions.AnimHelperKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J6\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u0006JF\u0010#\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u001d2\u0006\u0010%\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u00062\u0006\u0010&\u001a\u00020\u001d2\u0006\u0010'\u001a\u00020(J(\u0010)\u001a\u00020\u001b2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u0006H\u0002J\u0016\u0010*\u001a\u00020\n2\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u001dJ>\u0010.\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u001d2\u0006\u0010%\u001a\u00020\u001d2\u0006\u0010/\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u0006J>\u00100\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u001d2\u0006\u0010%\u001a\u00020\u001d2\u0006\u0010/\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u0006J\u0018\u00101\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\u001d2\u0006\u0010\"\u001a\u00020\u0006H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/CategoryElement;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "category", "Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "(Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;)V", "animScrollHeight", "", "getCategory", "()Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "focused", "", "getFocused", "()Z", "setFocused", "(Z)V", "lastHeight", "moduleElements", "", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/module/ModuleElement;", "getModuleElements", "()Ljava/util/List;", "name", "", "getName", "()Ljava/lang/String;", "scrollHeight", "drawLabel", "", "mouseX", "", "mouseY", "x", "y", "width", "height", "drawPanel", "mX", "mY", "wheel", "accentColor", "Ljava/awt/Color;", "drawScroll", "handleKeyTyped", "keyTyped", "", "keyCode", "handleMouseClick", "mouseButton", "handleMouseRelease", "handleScrolling", "KyinoClient"})
public final class CategoryElement
extends MinecraftInstance {
    @NotNull
    private final String name;
    private boolean focused;
    private float scrollHeight;
    private float animScrollHeight;
    private float lastHeight;
    @NotNull
    private final List<ModuleElement> moduleElements;
    @NotNull
    private final ModuleCategory category;

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final boolean getFocused() {
        return this.focused;
    }

    public final void setFocused(boolean bl) {
        this.focused = bl;
    }

    @NotNull
    public final List<ModuleElement> getModuleElements() {
        return this.moduleElements;
    }

    public final void drawLabel(int mouseX, int mouseY, float x, float y, float width, float height) {
        if (this.focused) {
            RenderUtils.originalRoundedRect(x + 3.0f, y + 3.0f, x + width - 3.0f, y + height - 3.0f, 3.0f, ColorManager.INSTANCE.getDropDown().getRGB());
        } else if (MouseUtils.mouseWithinBounds(mouseX, mouseY, x, y, x + width, y + height)) {
            RenderUtils.originalRoundedRect(x + 3.0f, y + 3.0f, x + width - 3.0f, y + height - 3.0f, 3.0f, ColorManager.INSTANCE.getBorder().getRGB());
        }
        Fonts.font40.drawString(this.name, x + 10.0f, y + height / 2.0f - (float)Fonts.font40.field_78288_b / 2.0f + 2.0f, -1);
    }

    public final void drawPanel(int mX, int mY, float x, float y, float width, float height, int wheel, @NotNull Color accentColor) {
        Intrinsics.checkParameterIsNotNull(accentColor, "accentColor");
        int mouseX = mX;
        int mouseY = mY;
        this.lastHeight = 0.0f;
        for (ModuleElement me : this.moduleElements) {
            this.lastHeight += 40.0f + me.getAnimHeight();
        }
        if (this.lastHeight >= 10.0f) {
            this.lastHeight -= 10.0f;
        }
        this.handleScrolling(wheel, height);
        this.drawScroll(x, y + 50.0f, width, height);
        Fonts.fontLarge.drawString(ChatFormatting.GRAY + "Modules > " + ChatFormatting.RESET + this.name, x + 10.0f, y + 10.0f, -1);
        Fonts.fontSmall.drawString(String.valueOf(this.name), x - 190.0f, y - 12.0f, -1);
        if ((float)mouseY < y + 50.0f || (float)mouseY >= y + height) {
            mouseY = -1;
        }
        RenderUtils.makeScissorBox(x, y + 50.0f, x + width, y + height);
        GL11.glEnable((int)3089);
        float startY = y + 50.0f;
        for (ModuleElement moduleElement : this.moduleElements) {
            if (startY + this.animScrollHeight > y + height || startY + this.animScrollHeight + 40.0f + moduleElement.getAnimHeight() < y + 50.0f) {
                startY += 40.0f + moduleElement.getAnimHeight();
                continue;
            }
            startY += moduleElement.drawElement(mouseX, mouseY, x, startY + this.animScrollHeight, width, 40.0f, accentColor);
        }
        GL11.glDisable((int)3089);
    }

    private final void handleScrolling(int wheel, float height) {
        if (wheel != 0) {
            this.scrollHeight = wheel > 0 ? (this.scrollHeight += 50.0f) : (this.scrollHeight -= 50.0f);
        }
        this.scrollHeight = this.lastHeight > height - 60.0f ? RangesKt.coerceIn(this.scrollHeight, -this.lastHeight + height - 60.0f, 0.0f) : 0.0f;
        this.animScrollHeight = AnimHelperKt.animSmooth(this.animScrollHeight, this.scrollHeight, 0.5f);
    }

    private final void drawScroll(float x, float y, float width, float height) {
        if (this.lastHeight > height - 60.0f) {
            float last = height - 60.0f - (height - 60.0f) * ((height - 60.0f) / this.lastHeight);
            float f = this.animScrollHeight / (-this.lastHeight + height - 60.0f);
            float f2 = last;
            boolean bl = false;
            float f3 = Math.abs(f);
            float multiply = f2 * RangesKt.coerceIn(f3, 0.0f, 1.0f);
            RenderUtils.originalRoundedRect(x + width - 6.0f, y + 5.0f + multiply, x + width - 4.0f, y + 5.0f + (height - 60.0f) * ((height - 60.0f) / this.lastHeight) + multiply, 1.0f, 0x50FFFFFF);
        }
    }

    public final void handleMouseClick(int mX, int mY, int mouseButton, float x, float y, float width, float height) {
        int mouseX = mX;
        int mouseY = mY;
        if ((float)mouseY < y + 50.0f || (float)mouseY >= y + height) {
            mouseY = -1;
        }
        float startY = y + 50.0f;
        if (mouseButton == 0) {
            for (ModuleElement moduleElement : this.moduleElements) {
                moduleElement.handleClick(mouseX, mouseY, x, startY + this.animScrollHeight, width, 40.0f);
                startY += 40.0f + moduleElement.getAnimHeight();
            }
        }
    }

    public final void handleMouseRelease(int mX, int mY, int mouseButton, float x, float y, float width, float height) {
        int mouseX = mX;
        int mouseY = mY;
        if ((float)mouseY < y + 50.0f || (float)mouseY >= y + height) {
            mouseY = -1;
        }
        float startY = y + 50.0f;
        if (mouseButton == 0) {
            for (ModuleElement moduleElement : this.moduleElements) {
                moduleElement.handleRelease(mouseX, mouseY, x, startY + this.animScrollHeight, width, 40.0f);
                startY += 40.0f + moduleElement.getAnimHeight();
            }
        }
    }

    public final boolean handleKeyTyped(char keyTyped, int keyCode) {
        for (ModuleElement moduleElement : this.moduleElements) {
            if (!moduleElement.handleKeyTyped(keyTyped, keyCode)) continue;
            return true;
        }
        return false;
    }

    @NotNull
    public final ModuleCategory getCategory() {
        return this.category;
    }

    /*
     * WARNING - void declaration
     */
    public CategoryElement(@NotNull ModuleCategory category) {
        void $this$filterTo$iv$iv;
        List list;
        Intrinsics.checkParameterIsNotNull((Object)category, "category");
        this.category = category;
        this.name = this.category.getDisplayName();
        CategoryElement categoryElement = this;
        boolean bl = false;
        categoryElement.moduleElements = list = (List)new ArrayList();
        Iterable $this$filter$iv = LiquidBounce.INSTANCE.getModuleManager().getModules();
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl2 = false;
            if (!(it.getCategory() == this.category)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEach$iv = (List)destination$iv$iv;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Module it = (Module)element$iv;
            boolean bl3 = false;
            this.moduleElements.add(new ModuleElement(it));
        }
    }
}

