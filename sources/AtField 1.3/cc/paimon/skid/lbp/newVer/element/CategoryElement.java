/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  kotlin.ranges.RangesKt
 *  org.lwjgl.opengl.GL11
 */
package cc.paimon.skid.lbp.newVer.element;

import cc.paimon.skid.lbp.newVer.ColorManager;
import cc.paimon.skid.lbp.newVer.MouseUtils;
import cc.paimon.skid.lbp.newVer.element.module.ModuleElement;
import cc.paimon.skid.lbp.newVer.extensions.AnimHelperKt;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;

public final class CategoryElement
extends MinecraftInstance {
    private final ModuleCategory category;
    private final String name;
    private boolean focused;
    private float animScrollHeight;
    private final List moduleElements;
    private float scrollHeight;
    private float lastHeight;

    public final String getName() {
        return this.name;
    }

    public final List getModuleElements() {
        return this.moduleElements;
    }

    public final boolean getFocused() {
        return this.focused;
    }

    public final void handleMouseClick(int n, int n2, int n3, float f, float f2, float f3, float f4) {
        int n4 = n;
        int n5 = n2;
        if ((float)n5 < f2 + 50.0f || (float)n5 >= f2 + f4) {
            n5 = -1;
        }
        float f5 = f2 + 50.0f;
        if (n3 == 0) {
            for (ModuleElement moduleElement : this.moduleElements) {
                moduleElement.handleClick(n4, n5, f, f5 + this.animScrollHeight, f3, 40.0f);
                f5 += 40.0f + moduleElement.getAnimHeight();
            }
        }
    }

    private final void handleScrolling(int n, float f) {
        if (n != 0) {
            this.scrollHeight = n > 0 ? (this.scrollHeight += 50.0f) : (this.scrollHeight -= 50.0f);
        }
        this.scrollHeight = this.lastHeight > f - 60.0f ? RangesKt.coerceIn((float)this.scrollHeight, (float)(-this.lastHeight + f - 60.0f), (float)0.0f) : 0.0f;
        this.animScrollHeight = AnimHelperKt.animSmooth(this.animScrollHeight, this.scrollHeight, 0.5f);
    }

    public final void handleMouseRelease(int n, int n2, int n3, float f, float f2, float f3, float f4) {
        int n4 = n;
        int n5 = n2;
        if ((float)n5 < f2 + 50.0f || (float)n5 >= f2 + f4) {
            n5 = -1;
        }
        float f5 = f2 + 50.0f;
        if (n3 == 0) {
            for (ModuleElement moduleElement : this.moduleElements) {
                moduleElement.handleRelease(n4, n5, f, f5 + this.animScrollHeight, f3, 40.0f);
                f5 += 40.0f + moduleElement.getAnimHeight();
            }
        }
    }

    public final boolean handleKeyTyped(char c, int n) {
        for (ModuleElement moduleElement : this.moduleElements) {
            if (!moduleElement.handleKeyTyped(c, n)) continue;
            return true;
        }
        return false;
    }

    public final ModuleCategory getCategory() {
        return this.category;
    }

    public CategoryElement(ModuleCategory moduleCategory) {
        List list;
        this.category = moduleCategory;
        this.name = this.category.getDisplayName();
        CategoryElement categoryElement = this;
        boolean bl = false;
        categoryElement.moduleElements = list = (List)new ArrayList();
        Iterable iterable = LiquidBounce.INSTANCE.getModuleManager().getModules();
        boolean bl2 = false;
        Iterable iterable2 = iterable;
        Collection collection2 = new ArrayList();
        boolean bl3 = false;
        Iterator iterator2 = iterable2.iterator();
        while (iterator2.hasNext()) {
            Object t = iterator2.next();
            Module module = (Module)t;
            boolean bl4 = false;
            if (!(module.getCategory() == this.category)) continue;
            collection2.add(t);
        }
        iterable = (List)collection2;
        bl2 = false;
        for (Collection collection2 : iterable) {
            Module module = (Module)((Object)collection2);
            boolean bl5 = false;
            this.moduleElements.add(new ModuleElement(module));
        }
    }

    public final void drawLabel(int n, int n2, float f, float f2, float f3, float f4) {
        if (this.focused) {
            RenderUtils.drawRoundedRect(f + 3.0f, f2 + 3.0f, f + f3 - 3.0f, f2 + f4 - 3.0f, 3.0f, ColorManager.INSTANCE.getDropDown().getRGB());
        } else if (MouseUtils.mouseWithinBounds(n, n2, f, f2, f + f3, f2 + f4)) {
            RenderUtils.drawRoundedRect(f + 3.0f, f2 + 3.0f, f + f3 - 3.0f, f2 + f4 - 3.0f, 3.0f, new Color(241, 243, 247).getRGB());
        }
        Fonts.posterama40.drawString(this.name, f + 10.0f, f2 + f4 / 2.0f - (float)Fonts.posterama40.getFontHeight() / 2.0f + 2.0f, new Color(26, 26, 26).getRGB());
    }

    private final void drawScroll(float f, float f2, float f3, float f4) {
        if (this.lastHeight > f4 - 60.0f) {
            float f5 = f4 - 60.0f - (f4 - 60.0f) * ((f4 - 60.0f) / this.lastHeight);
            float f6 = this.animScrollHeight / (-this.lastHeight + f4 - 60.0f);
            float f7 = f5;
            boolean bl = false;
            float f8 = Math.abs(f6);
            float f9 = f7 * RangesKt.coerceIn((float)f8, (float)0.0f, (float)1.0f);
            RenderUtils.drawRoundedRect(f + f3 - 6.0f, f2 + 5.0f + f9, f + f3 - 4.0f, f2 + 5.0f + (f4 - 60.0f) * ((f4 - 60.0f) / this.lastHeight) + f9, 1.0f, 0x50FFFFFF);
        }
    }

    public final void setFocused(boolean bl) {
        this.focused = bl;
    }

    public final void drawPanel(int n, int n2, float f, float f2, float f3, float f4, int n3, Color color) {
        int n4 = n;
        int n5 = n2;
        this.lastHeight = 0.0f;
        for (ModuleElement moduleElement : this.moduleElements) {
            this.lastHeight += 40.0f + moduleElement.getAnimHeight();
        }
        if (this.lastHeight >= 10.0f) {
            this.lastHeight -= 10.0f;
        }
        this.handleScrolling(n3, f4);
        this.drawScroll(f, f2 + 50.0f, f3, f4);
        Fonts.posterama35.drawString(ChatFormatting.GRAY + "Modules > " + ChatFormatting.RESET + this.name, f + 10.0f, f2 + 10.0f, new Color(26, 26, 26).getRGB());
        Fonts.posterama20.drawString(String.valueOf(this.name), f - 190.0f, f2 - 12.0f, new Color(26, 26, 26).getRGB());
        if ((float)n5 < f2 + 50.0f || (float)n5 >= f2 + f4) {
            n5 = -1;
        }
        RenderUtils.makeScissorBox(f, f2 + 50.0f, f + f3, f2 + f4);
        GL11.glEnable((int)3089);
        float f5 = f2 + 50.0f;
        for (Object object : this.moduleElements) {
            if (f5 + this.animScrollHeight > f2 + f4 || f5 + this.animScrollHeight + 40.0f + ((ModuleElement)object).getAnimHeight() < f2 + 50.0f) {
                f5 += 40.0f + ((ModuleElement)object).getAnimHeight();
                continue;
            }
            f5 += ((ModuleElement)object).drawElement(n4, n5, f, f5 + this.animScrollHeight, f3, 40.0f, color);
        }
        GL11.glDisable((int)3089);
    }
}

