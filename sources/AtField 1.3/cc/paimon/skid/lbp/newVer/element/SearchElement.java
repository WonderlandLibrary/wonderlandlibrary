/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package cc.paimon.skid.lbp.newVer.element;

import cc.paimon.skid.lbp.newVer.ColorManager;
import cc.paimon.skid.lbp.newVer.IconManager;
import cc.paimon.skid.lbp.newVer.MouseUtils;
import cc.paimon.skid.lbp.newVer.element.CategoryElement;
import cc.paimon.skid.lbp.newVer.element.SearchBox;
import cc.paimon.skid.lbp.newVer.element.module.ModuleElement;
import cc.paimon.skid.lbp.newVer.extensions.AnimHelperKt;
import java.awt.Color;
import java.util.List;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public final class SearchElement {
    private float animScrollHeight;
    private final float height;
    private float lastHeight;
    private final float width;
    private final float xPos;
    private final float yPos;
    private final SearchBox searchBox;
    private float scrollHeight;

    public final boolean handleTyping(char c, int n, float f, float f2, float f3, float f4, List list) {
        this.searchBox.func_146201_a(c, n);
        if (this.searchBox.func_146179_b().length() <= 0) {
            return false;
        }
        for (CategoryElement categoryElement : list) {
            for (ModuleElement moduleElement : categoryElement.getModuleElements()) {
                if (!StringsKt.startsWith((String)moduleElement.getModule().getName(), (String)this.searchBox.func_146179_b(), (boolean)true) || !moduleElement.handleKeyTyped(c, n)) continue;
                return true;
            }
        }
        return false;
    }

    public final void handleMouseClick(int n, int n2, int n3, float f, float f2, float f3, float f4, List list) {
        if (MouseUtils.mouseWithinBounds(n, n2, f - 200.0f, f2 - 20.0f, f - 170.0f, f2)) {
            this.searchBox.func_146180_a("");
            return;
        }
        int n4 = n;
        int n5 = n2;
        this.searchBox.func_146192_a(n4, n5, n3);
        if (this.searchBox.func_146179_b().length() <= 0) {
            return;
        }
        if ((float)n5 < f2 + 50.0f || (float)n5 >= f2 + f4) {
            n5 = -1;
        }
        float f5 = f2 + 50.0f;
        for (CategoryElement categoryElement : list) {
            for (ModuleElement moduleElement : categoryElement.getModuleElements()) {
                if (!StringsKt.startsWith((String)moduleElement.getModule().getName(), (String)this.searchBox.func_146179_b(), (boolean)true)) continue;
                moduleElement.handleClick(n4, n5, f, f5 + this.animScrollHeight, f3, 40.0f);
                f5 += 40.0f + moduleElement.getAnimHeight();
            }
        }
    }

    public final boolean isTyping() {
        return this.searchBox.func_146179_b().length() > 0;
    }

    public final float getHeight() {
        return this.height;
    }

    public final boolean drawBox(int n, int n2, Color color) {
        RenderUtils.drawRoundedRect(this.xPos - 0.5f, this.yPos - 0.5f, this.xPos + this.width + 0.5f, this.yPos + this.height + 0.5f, 4.0f, ColorManager.INSTANCE.getButtonOutline().getRGB());
        Stencil.write(true);
        RenderUtils.drawRoundedRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, 4.0f, ColorManager.INSTANCE.getTextBox().getRGB());
        Stencil.erase(true);
        if (this.searchBox.func_146206_l()) {
            RenderUtils.drawRect(this.xPos, this.yPos + this.height - 1.0f, this.xPos + this.width, this.yPos + this.height, color.getRGB());
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
        RenderUtils.drawImage(IconManager.INSTANCE.getSearch(), (int)(this.xPos + this.width - 15.0f), (int)(this.yPos + 5.0f), 10, 10);
        GlStateManager.func_179141_d();
        return this.searchBox.func_146179_b().length() > 0;
    }

    public SearchElement(float f, float f2, float f3, float f4) {
        this.xPos = f;
        this.yPos = f2;
        this.width = f3;
        this.height = f4;
        this.searchBox = new SearchBox(0, (int)this.xPos + 2, (int)this.yPos + 2, (int)this.width - 4, (int)this.height - 2);
    }

    public final void drawPanel(int n, int n2, float f, float f2, float f3, float f4, int n3, List list, Color color) {
        int n4 = n;
        int n5 = n2;
        this.lastHeight = 0.0f;
        for (CategoryElement categoryElement : list) {
            for (Object object : categoryElement.getModuleElements()) {
                if (!StringsKt.startsWith((String)((ModuleElement)object).getModule().getName(), (String)this.searchBox.func_146179_b(), (boolean)true)) continue;
                this.lastHeight += ((ModuleElement)object).getAnimHeight() + 40.0f;
            }
        }
        if (this.lastHeight >= 10.0f) {
            this.lastHeight -= 10.0f;
        }
        this.handleScrolling(n3, f4);
        this.drawScroll(f, f2 + 50.0f, f3, f4);
        Fonts.posterama35.drawString("Search", f + 10.0f, f2 + 10.0f, new Color(26, 26, 26).getRGB());
        Fonts.posterama20.drawString("Search", f - 170.0f, f2 - 12.0f, new Color(26, 26, 26).getRGB());
        RenderUtils.drawImage(IconManager.INSTANCE.getBack(), (int)(f - 190.0f), (int)(f2 - 15.0f), 10, 10);
        float f5 = f2 + 50.0f;
        if ((float)n5 < f2 + 50.0f || (float)n5 >= f2 + f4) {
            n5 = -1;
        }
        RenderUtils.makeScissorBox(f, f2 + 50.0f, f + f3, f2 + f4);
        GL11.glEnable((int)3089);
        for (Object object : list) {
            for (Object object2 : ((CategoryElement)object).getModuleElements()) {
                if (!StringsKt.startsWith((String)((ModuleElement)object2).getModule().getName(), (String)this.searchBox.func_146179_b(), (boolean)true)) continue;
                if (f5 + this.animScrollHeight > f2 + f4 || f5 + this.animScrollHeight + 40.0f + ((ModuleElement)object2).getAnimHeight() < f2 + 50.0f) {
                    f5 += 40.0f + ((ModuleElement)object2).getAnimHeight();
                    continue;
                }
                f5 += ((ModuleElement)object2).drawElement(n4, n5, f, f5 + this.animScrollHeight, f3, 40.0f, color);
            }
        }
        GL11.glDisable((int)3089);
    }

    public final float getYPos() {
        return this.yPos;
    }

    public final float getXPos() {
        return this.xPos;
    }

    private final void handleScrolling(int n, float f) {
        if (n != 0) {
            this.scrollHeight = n > 0 ? (this.scrollHeight += 50.0f) : (this.scrollHeight -= 50.0f);
        }
        this.scrollHeight = this.lastHeight > f - 60.0f ? RangesKt.coerceIn((float)this.scrollHeight, (float)(-this.lastHeight + f - (float)60), (float)0.0f) : 0.0f;
        this.animScrollHeight = AnimHelperKt.animSmooth(this.animScrollHeight, this.scrollHeight, 0.5f);
    }

    public final float getWidth() {
        return this.width;
    }

    private final void drawScroll(float f, float f2, float f3, float f4) {
        if (this.lastHeight > f4 - 60.0f) {
            float f5 = f4 - 60.0f - (f4 - 60.0f) * ((f4 - 60.0f) / this.lastHeight);
            float f6 = this.animScrollHeight / (-this.lastHeight + f4 - (float)60);
            float f7 = f5;
            boolean bl = false;
            float f8 = Math.abs(f6);
            float f9 = f7 * RangesKt.coerceIn((float)f8, (float)0.0f, (float)1.0f);
            RenderUtils.drawRoundedRect(f + f3 - 6.0f, f2 + 5.0f + f9, f + f3 - 4.0f, f2 + 5.0f + (f4 - 60.0f) * ((f4 - 60.0f) / this.lastHeight) + f9, 1.0f, (int)0x50FFFFFFL);
        }
    }

    public final void handleMouseRelease(int n, int n2, int n3, float f, float f2, float f3, float f4, List list) {
        int n4 = n;
        int n5 = n2;
        if (this.searchBox.func_146179_b().length() <= 0) {
            return;
        }
        if ((float)n5 < f2 + 50.0f || (float)n5 >= f2 + f4) {
            n5 = -1;
        }
        float f5 = f2 + 50.0f;
        for (CategoryElement categoryElement : list) {
            for (ModuleElement moduleElement : categoryElement.getModuleElements()) {
                if (!StringsKt.startsWith((String)moduleElement.getModule().getName(), (String)this.searchBox.func_146179_b(), (boolean)true)) continue;
                moduleElement.handleRelease(n4, n5, f, f5 + this.animScrollHeight, f3, 40.0f);
                f5 += 40.0f + moduleElement.getAnimHeight();
            }
        }
    }
}

