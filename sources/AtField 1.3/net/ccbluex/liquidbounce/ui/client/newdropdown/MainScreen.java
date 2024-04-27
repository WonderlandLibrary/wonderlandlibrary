/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import liying.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.newdropdown.impl.ModuleRect;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.DecelerateAnimation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.Main;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.Screen;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.DrRenderUtils;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.StencilUtil;
import net.ccbluex.liquidbounce.utils.MathUtils;

public class MainScreen
implements Screen {
    private final ModuleCategory category;
    public HashMap moduleAnimMap = new HashMap();
    private final float categoryRectHeight = 18.0f;
    private final float rectWidth = 110.0f;
    public Animation animation;
    private List moduleRects;
    public Animation openingAnimation;

    @Override
    public void keyTyped(char c, int n) {
        if (this.moduleRects != null) {
            this.moduleRects.forEach(arg_0 -> MainScreen.lambda$keyTyped$0(c, n, arg_0));
        }
    }

    @Override
    public void initGui() {
        if (this.moduleRects == null) {
            this.moduleRects = new ArrayList();
            for (Module module : Main.getModulesInCategory(this.category, LiquidBounce.moduleManager).stream().sorted(Comparator.comparing(Module::getName)).collect(Collectors.toList())) {
                ModuleRect moduleRect = new ModuleRect(module);
                this.moduleRects.add(moduleRect);
                this.moduleAnimMap.put(moduleRect, new DecelerateAnimation(250, 1.0));
            }
        }
        if (this.moduleRects != null) {
            this.moduleRects.forEach(ModuleRect::initGui);
        }
    }

    @Override
    public void drawScreen(int n, int n2) {
        float f = (float)Math.max(0.0, Math.min(255.0, 255.0 * this.animation.getOutput()));
        int n3 = (int)f;
        int n4 = new Color(29, 29, 29, n3).getRGB();
        int n5 = new Color(255, 255, 255, n3).getRGB();
        this.category.getDrag().onDraw(n, n2);
        float f2 = this.category.getDrag().getX();
        float f3 = this.category.getDrag().getY();
        DrRenderUtils.drawRect2(f2, f3, 110.0, 18.0, n4);
        DrRenderUtils.setAlphaLimit(0.0f);
        Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.drawString((CharSequence)this.category.name(), f2 + 5.0f, f3 + Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.getMiddleOfBox(18.0f), n5);
        String string = "";
        if (this.category.name().equalsIgnoreCase("Combat")) {
            string = "D";
        } else if (this.category.name().equalsIgnoreCase("Movement")) {
            string = "A";
        } else if (this.category.name().equalsIgnoreCase("Player")) {
            string = "B";
        } else if (this.category.name().equalsIgnoreCase("Render")) {
            string = "C";
        } else if (this.category.name().equalsIgnoreCase("Exploit")) {
            string = "G";
        } else if (this.category.name().equalsIgnoreCase("Misc")) {
            string = "F";
        }
        DrRenderUtils.setAlphaLimit(0.0f);
        DrRenderUtils.resetColor();
        Fonts.ICONFONT.ICONFONT_20.ICONFONT_20.drawString((CharSequence)string, f2 + 110.0f - (float)(Fonts.ICONFONT.ICONFONT_20.ICONFONT_20.stringWidth(string) + 5), f3 + Fonts.ICONFONT.ICONFONT_20.ICONFONT_20.getMiddleOfBox(18.0f), n5);
        if (this.category.name().equalsIgnoreCase("World")) {
            Fonts.CheckFont.CheckFont_20.CheckFont_20.drawString((CharSequence)"b", f2 + 110.0f - (float)(Fonts.CheckFont.CheckFont_20.CheckFont_20.stringWidth("b") + 5), f3 + Fonts.ICONFONT.ICONFONT_20.ICONFONT_20.getMiddleOfBox(18.0f), n5);
        }
        if (((String)ClickGUI.scrollMode.get()).equals("Value")) {
            Main.allowedClickGuiHeight = ((Integer)ClickGUI.clickHeight.get()).floatValue();
        } else {
            IScaledResolution iScaledResolution = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createScaledResolution(mc);
            Main.allowedClickGuiHeight = (float)(2 * iScaledResolution.getScaledHeight()) / 3.0f;
        }
        float f4 = Main.allowedClickGuiHeight;
        boolean bl = DrRenderUtils.isHovering(f2, f3 + 18.0f, 110.0f, f4, n, n2);
        float f5 = Math.max(1.0f, (float)this.openingAnimation.getOutput() + 0.7f);
        float f6 = 110.0f;
        StencilUtil.initStencilToWrite();
        DrRenderUtils.drawRect2(f2 - 100.0f, f3 + 18.0f, 260.0, f4, -1);
        StencilUtil.readStencilBuffer(1);
        double d = this.category.getScroll().getScroll();
        double d2 = 0.0;
        for (ModuleRect moduleRect : this.moduleRects) {
            Animation animation = (Animation)this.moduleAnimMap.get(moduleRect);
            animation.setDirection(moduleRect.module.getExpanded() ? Direction.FORWARDS : Direction.BACKWARDS);
            moduleRect.settingAnimation = animation;
            moduleRect.alphaAnimation = n3;
            moduleRect.x = f2;
            moduleRect.height = 17.0f;
            moduleRect.panelLimitY = f3;
            moduleRect.openingAnimation = this.openingAnimation;
            moduleRect.y = (float)((double)(f3 + 18.0f) + d2 * 17.0 + MathUtils.roundToHalf(d));
            moduleRect.width = 110.0f;
            moduleRect.drawScreen(n, n2);
            d2 += 1.0 + moduleRect.getSettingSize();
        }
        if (bl) {
            this.category.getScroll().onScroll(30);
            float f7 = (float)(d2 * 17.0 - (double)f4);
            this.category.getScroll().setMaxScroll(Math.max(0.0f, f7));
        }
        StencilUtil.uninitStencilBuffer();
    }

    private static void lambda$mouseClicked$1(int n, int n2, int n3, ModuleRect moduleRect) {
        moduleRect.mouseClicked(n, n2, n3);
    }

    public MainScreen(ModuleCategory moduleCategory) {
        this.category = moduleCategory;
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        this.category.getDrag().onRelease(n3);
        this.moduleRects.forEach(arg_0 -> MainScreen.lambda$mouseReleased$2(n, n2, n3, arg_0));
    }

    private static void lambda$mouseReleased$2(int n, int n2, int n3, ModuleRect moduleRect) {
        moduleRect.mouseReleased(n, n2, n3);
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        boolean bl = DrRenderUtils.isHovering(this.category.getDrag().getX(), this.category.getDrag().getY(), 110.0f, 18.0f, n, n2);
        this.category.getDrag().onClick(n, n2, n3, bl);
        this.moduleRects.forEach(arg_0 -> MainScreen.lambda$mouseClicked$1(n, n2, n3, arg_0));
    }

    private static void lambda$keyTyped$0(char c, int n, ModuleRect moduleRect) {
        moduleRect.keyTyped(c, n);
    }
}

