/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.collections.CollectionsKt
 *  kotlin.io.CloseableKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.TabGUI$WhenMappings;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="TabGUI")
public final class TabGUI
extends Element {
    private final FloatValue rainbowX;
    private final FloatValue rainbowY;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue alphaValue;
    private final BoolValue rectangleRainbow;
    private final IntegerValue backgroundRedValue;
    private final IntegerValue backgroundGreenValue;
    private final IntegerValue backgroundBlueValue;
    private final IntegerValue backgroundAlphaValue;
    private final BoolValue borderValue;
    private final FloatValue borderStrength;
    private final IntegerValue borderRedValue;
    private final IntegerValue borderGreenValue;
    private final IntegerValue borderBlueValue;
    private final IntegerValue borderAlphaValue;
    private final BoolValue borderRainbow;
    private final BoolValue arrowsValue;
    private final FontValue fontValue;
    private final BoolValue textShadow;
    private final BoolValue textFade;
    private final FloatValue textPositionY;
    private final FloatValue width;
    private final FloatValue tabHeight;
    private final BoolValue upperCaseValue;
    private final List<Tab> tabs;
    private boolean categoryMenu;
    private int selectedCategory;
    private int selectedModule;
    private float tabY;
    private float itemY;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    @Override
    public Border drawElement() {
        RainbowShader instance$iv;
        boolean $i$f$begin;
        this.updateAnimation();
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        IFontRenderer fontRenderer = (IFontRenderer)this.fontValue.get();
        boolean rectangleRainbowEnabled = (Boolean)this.rectangleRainbow.get();
        Color backgroundColor = new Color(((Number)this.backgroundRedValue.get()).intValue(), ((Number)this.backgroundGreenValue.get()).intValue(), ((Number)this.backgroundBlueValue.get()).intValue(), ((Number)this.backgroundAlphaValue.get()).intValue());
        Color borderColor = (Boolean)this.borderRainbow.get() == false ? new Color(((Number)this.borderRedValue.get()).intValue(), ((Number)this.borderGreenValue.get()).intValue(), ((Number)this.borderBlueValue.get()).intValue(), ((Number)this.borderAlphaValue.get()).intValue()) : Color.black;
        float guiHeight = (float)this.tabs.size() * ((Number)this.tabHeight.get()).floatValue();
        RenderUtils.drawRect(1.0f, 0.0f, ((Number)this.width.get()).floatValue(), guiHeight, backgroundColor.getRGB());
        if (((Boolean)this.borderValue.get()).booleanValue()) {
            boolean enable$iv;
            RainbowShader.Companion companion = RainbowShader.Companion;
            boolean bl = (Boolean)this.borderRainbow.get();
            float f = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
            float f2 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
            float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
            $i$f$begin = false;
            instance$iv = RainbowShader.INSTANCE;
            if (enable$iv) {
                void y$iv;
                void x$iv;
                instance$iv.setStrengthX((float)x$iv);
                instance$iv.setStrengthY((float)y$iv);
                instance$iv.setOffset(offset$iv);
                instance$iv.startShader();
            }
            Closeable this_$iv = instance$iv;
            enable$iv = false;
            Throwable x$iv = null;
            try {
                RainbowShader it = (RainbowShader)this_$iv;
                boolean bl2 = false;
                RenderUtils.drawBorder(1.0f, 0.0f, ((Number)this.width.get()).floatValue(), guiHeight, ((Number)this.borderStrength.get()).floatValue(), borderColor.getRGB());
                it = Unit.INSTANCE;
            }
            catch (Throwable it) {
                x$iv = it;
                throw it;
            }
            finally {
                CloseableKt.closeFinally((Closeable)this_$iv, (Throwable)x$iv);
            }
        }
        Color rectColor = !rectangleRainbowEnabled ? new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue()) : Color.black;
        RainbowShader.Companion enable$iv = RainbowShader.Companion;
        float x$iv = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
        float it = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
        float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
        $i$f$begin = false;
        instance$iv = RainbowShader.INSTANCE;
        if (rectangleRainbowEnabled) {
            void y$iv;
            instance$iv.setStrengthX(x$iv);
            instance$iv.setStrengthY((float)y$iv);
            instance$iv.setOffset(offset$iv);
            instance$iv.startShader();
        }
        Closeable this_$iv = instance$iv;
        boolean x$iv2 = false;
        Throwable y$iv = null;
        try {
            RainbowShader it2 = (RainbowShader)this_$iv;
            boolean bl = false;
            RenderUtils.drawRect(1.0f, 1.0f + this.tabY - 1.0f, ((Number)this.width.get()).floatValue(), this.tabY + ((Number)this.tabHeight.get()).floatValue(), rectColor);
            it2 = Unit.INSTANCE;
        }
        catch (Throwable it2) {
            y$iv = it2;
            throw it2;
        }
        finally {
            CloseableKt.closeFinally((Closeable)this_$iv, (Throwable)y$iv);
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float y = 1.0f;
        Iterable $this$forEachIndexed$iv = this.tabs;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            String string;
            void tab;
            int n = index$iv++;
            boolean bl = false;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n2 = n;
            Tab tab2 = (Tab)item$iv;
            int index = n2;
            boolean bl3 = false;
            if (((Boolean)this.upperCaseValue.get()).booleanValue()) {
                String string2 = tab.getTabName();
                boolean bl4 = false;
                String string3 = string2;
                if (string3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string = string3.toUpperCase();
            } else {
                string = tab.getTabName();
            }
            String tabName = string;
            float textX = this.getSide().getHorizontal() == Side.Horizontal.RIGHT ? ((Number)this.width.get()).floatValue() - (float)fontRenderer.getStringWidth(tabName) - tab.getTextFade() - (float)3 : tab.getTextFade() + (float)5;
            float textY = y + ((Number)this.textPositionY.get()).floatValue();
            int textColor = this.selectedCategory == index ? 0xFFFFFF : new Color(210, 210, 210).getRGB();
            fontRenderer.drawString(tabName, textX, textY, textColor, (Boolean)this.textShadow.get());
            if (((Boolean)this.arrowsValue.get()).booleanValue()) {
                if (this.getSide().getHorizontal() == Side.Horizontal.RIGHT) {
                    fontRenderer.drawString(!this.categoryMenu && this.selectedCategory == index ? ">" : "<", 3.0f, y + 2.0f, 0xFFFFFF, (Boolean)this.textShadow.get());
                } else {
                    fontRenderer.drawString(!this.categoryMenu && this.selectedCategory == index ? "<" : ">", ((Number)this.width.get()).floatValue() - 8.0f, y + 2.0f, 0xFFFFFF, (Boolean)this.textShadow.get());
                }
            }
            if (index == this.selectedCategory && !this.categoryMenu) {
                float tabX = this.getSide().getHorizontal() == Side.Horizontal.RIGHT ? 1.0f - (float)tab.getMenuWidth() : ((Number)this.width.get()).floatValue() + (float)5;
                tab.drawTab(tabX, y, rectColor.getRGB(), backgroundColor.getRGB(), borderColor.getRGB(), ((Number)this.borderStrength.get()).floatValue(), (Boolean)this.upperCaseValue.get(), fontRenderer, (Boolean)this.borderRainbow.get(), rectangleRainbowEnabled);
            }
            y += ((Number)this.tabHeight.get()).floatValue();
        }
        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
        return new Border(1.0f, 0.0f, ((Number)this.width.get()).floatValue(), guiHeight);
    }

    @Override
    public void handleKey(char c, int keyCode) {
        switch (keyCode) {
            case 200: {
                this.parseAction(Action.UP);
                break;
            }
            case 208: {
                this.parseAction(Action.DOWN);
                break;
            }
            case 205: {
                this.parseAction(this.getSide().getHorizontal() == Side.Horizontal.RIGHT ? Action.LEFT : Action.RIGHT);
                break;
            }
            case 203: {
                this.parseAction(this.getSide().getHorizontal() == Side.Horizontal.RIGHT ? Action.RIGHT : Action.LEFT);
                break;
            }
            case 28: {
                this.parseAction(Action.TOGGLE);
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void updateAnimation() {
        int delta = RenderUtils.deltaTime;
        float xPos = ((Number)this.tabHeight.get()).floatValue() * (float)this.selectedCategory;
        this.tabY = (int)this.tabY != (int)xPos ? (xPos > this.tabY ? (this.tabY += 0.1f * (float)delta) : (this.tabY -= 0.1f * (float)delta)) : xPos;
        float xPos2 = ((Number)this.tabHeight.get()).floatValue() * (float)this.selectedModule;
        this.itemY = (int)this.itemY != (int)xPos2 ? (xPos2 > this.itemY ? (this.itemY += 0.1f * (float)delta) : (this.itemY -= 0.1f * (float)delta)) : xPos2;
        if (this.categoryMenu) {
            this.itemY = 0.0f;
        }
        if (((Boolean)this.textFade.get()).booleanValue()) {
            Iterable $this$forEachIndexed$iv = this.tabs;
            boolean $i$f$forEachIndexed = false;
            int index$iv = 0;
            for (Object item$iv : $this$forEachIndexed$iv) {
                void tab;
                int n = index$iv++;
                boolean bl = false;
                if (n < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n2 = n;
                Tab tab2 = (Tab)item$iv;
                int index = n2;
                boolean bl2 = false;
                if (index == this.selectedCategory) {
                    if (tab.getTextFade() < (float)4) {
                        void v0 = tab;
                        v0.setTextFade(v0.getTextFade() + 0.05f * (float)delta);
                    }
                    if (!(tab.getTextFade() > (float)4)) continue;
                    tab.setTextFade(4.0f);
                    continue;
                }
                if (tab.getTextFade() > 0.0f) {
                    void v1 = tab;
                    v1.setTextFade(v1.getTextFade() - 0.05f * (float)delta);
                }
                if (!(tab.getTextFade() < 0.0f)) continue;
                tab.setTextFade(0.0f);
            }
        } else {
            for (Tab tab : this.tabs) {
                if (tab.getTextFade() > 0.0f) {
                    Tab tab3 = tab;
                    tab3.setTextFade(tab3.getTextFade() - 0.05f * (float)delta);
                }
                if (!(tab.getTextFade() < 0.0f)) continue;
                tab.setTextFade(0.0f);
            }
        }
    }

    private final void parseAction(Action action) {
        boolean toggle = false;
        switch (TabGUI$WhenMappings.$EnumSwitchMapping$0[action.ordinal()]) {
            case 1: {
                if (this.categoryMenu) {
                    TabGUI tabGUI = this;
                    tabGUI.selectedCategory += -1;
                    int cfr_ignored_0 = tabGUI.selectedCategory;
                    if (this.selectedCategory >= 0) break;
                    this.selectedCategory = this.tabs.size() - 1;
                    this.tabY = ((Number)this.tabHeight.get()).floatValue() * (float)this.selectedCategory;
                    break;
                }
                TabGUI tabGUI = this;
                tabGUI.selectedModule += -1;
                int cfr_ignored_1 = tabGUI.selectedModule;
                if (this.selectedModule >= 0) break;
                this.selectedModule = this.tabs.get(this.selectedCategory).getModules().size() - 1;
                this.itemY = ((Number)this.tabHeight.get()).floatValue() * (float)this.selectedModule;
                break;
            }
            case 2: {
                if (this.categoryMenu) {
                    TabGUI tabGUI = this;
                    ++tabGUI.selectedCategory;
                    int cfr_ignored_2 = tabGUI.selectedCategory;
                    if (this.selectedCategory <= this.tabs.size() - 1) break;
                    this.selectedCategory = 0;
                    this.tabY = ((Number)this.tabHeight.get()).floatValue() * (float)this.selectedCategory;
                    break;
                }
                TabGUI tabGUI = this;
                ++tabGUI.selectedModule;
                int cfr_ignored_3 = tabGUI.selectedModule;
                if (this.selectedModule <= this.tabs.get(this.selectedCategory).getModules().size() - 1) break;
                this.selectedModule = 0;
                this.itemY = ((Number)this.tabHeight.get()).floatValue() * (float)this.selectedModule;
                break;
            }
            case 3: {
                if (this.categoryMenu) break;
                this.categoryMenu = true;
                break;
            }
            case 4: {
                if (!this.categoryMenu) {
                    toggle = true;
                    break;
                }
                this.categoryMenu = false;
                this.selectedModule = 0;
                break;
            }
            case 5: {
                if (this.categoryMenu) break;
                toggle = true;
            }
        }
        if (toggle) {
            int sel = this.selectedModule;
            this.tabs.get(this.selectedCategory).getModules().get(sel).toggle();
        }
    }

    /*
     * WARNING - void declaration
     */
    public TabGUI(double x, double y) {
        super(x, y, 0.0f, null, 12, null);
        List list;
        this.rainbowX = new FloatValue("Rainbow-X", -1000.0f, -2000.0f, 2000.0f);
        this.rainbowY = new FloatValue("Rainbow-Y", -1000.0f, -2000.0f, 2000.0f);
        this.redValue = new IntegerValue("Rectangle Red", 0, 0, 255);
        this.greenValue = new IntegerValue("Rectangle Green", 148, 0, 255);
        this.blueValue = new IntegerValue("Rectangle Blue", 255, 0, 255);
        this.alphaValue = new IntegerValue("Rectangle Alpha", 140, 0, 255);
        this.rectangleRainbow = new BoolValue("Rectangle Rainbow", false);
        this.backgroundRedValue = new IntegerValue("Background Red", 0, 0, 255);
        this.backgroundGreenValue = new IntegerValue("Background Green", 0, 0, 255);
        this.backgroundBlueValue = new IntegerValue("Background Blue", 0, 0, 255);
        this.backgroundAlphaValue = new IntegerValue("Background Alpha", 150, 0, 255);
        this.borderValue = new BoolValue("Border", true);
        this.borderStrength = new FloatValue("Border Strength", 2.0f, 1.0f, 5.0f);
        this.borderRedValue = new IntegerValue("Border Red", 0, 0, 255);
        this.borderGreenValue = new IntegerValue("Border Green", 0, 0, 255);
        this.borderBlueValue = new IntegerValue("Border Blue", 0, 0, 255);
        this.borderAlphaValue = new IntegerValue("Border Alpha", 150, 0, 255);
        this.borderRainbow = new BoolValue("Border Rainbow", false);
        this.arrowsValue = new BoolValue("Arrows", true);
        this.fontValue = new FontValue("Font", Fonts.font35);
        this.textShadow = new BoolValue("TextShadow", false);
        this.textFade = new BoolValue("TextFade", true);
        this.textPositionY = new FloatValue("TextPosition-Y", 2.0f, 0.0f, 5.0f);
        this.width = new FloatValue("Width", 60.0f, 55.0f, 100.0f);
        this.tabHeight = new FloatValue("TabHeight", 12.0f, 10.0f, 15.0f);
        this.upperCaseValue = new BoolValue("UpperCase", false);
        TabGUI tabGUI = this;
        boolean bl = false;
        tabGUI.tabs = list = (List)new ArrayList();
        this.categoryMenu = true;
        for (ModuleCategory category : ModuleCategory.values()) {
            void $this$filterTo$iv$iv;
            Tab tab = new Tab(category.getDisplayName());
            Iterable $this$filter$iv = LiquidBounce.INSTANCE.getModuleManager().getModules();
            boolean $i$f$filter = false;
            Iterable iterable = $this$filter$iv;
            Collection destination$iv$iv = new ArrayList();
            boolean $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                Module module = (Module)element$iv$iv;
                boolean bl2 = false;
                if (!(category == module.getCategory())) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            Iterable $this$forEach$iv = (List)destination$iv$iv;
            boolean $i$f$forEach = false;
            for (Object element$iv : $this$forEach$iv) {
                Module e = (Module)element$iv;
                boolean bl3 = false;
                tab.getModules().add(e);
            }
            this.tabs.add(tab);
        }
    }

    public /* synthetic */ TabGUI(double d, double d2, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 5.0;
        }
        if ((n & 2) != 0) {
            d2 = 25.0;
        }
        this(d, d2);
    }

    public TabGUI() {
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }

    public static final /* synthetic */ void access$setItemY$p(TabGUI $this, float f) {
        $this.itemY = f;
    }

    private final class Tab {
        private final List<Module> modules;
        private int menuWidth;
        private float textFade;
        private final String tabName;

        public final List<Module> getModules() {
            return this.modules;
        }

        public final int getMenuWidth() {
            return this.menuWidth;
        }

        public final void setMenuWidth(int n) {
            this.menuWidth = n;
        }

        public final float getTextFade() {
            return this.textFade;
        }

        public final void setTextFade(float f) {
            this.textFade = f;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         * WARNING - void declaration
         */
        public final void drawTab(float x, float y, int color, int backgroundColor, int borderColor, float borderStrength, boolean upperCase, IFontRenderer fontRenderer, boolean borderRainbow, boolean rectRainbow) {
            Object this_$iv;
            RainbowShader instance$iv;
            boolean $i$f$begin;
            int maxWidth = 0;
            for (Module module : this.modules) {
                String string;
                String string2;
                String string3;
                boolean bl;
                IFontRenderer iFontRenderer;
                String string4;
                IFontRenderer iFontRenderer2 = fontRenderer;
                if (upperCase) {
                    string4 = module.getName();
                    iFontRenderer = iFontRenderer2;
                    bl = false;
                    String string5 = string4;
                    if (string5 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    string3 = string5.toUpperCase();
                    iFontRenderer2 = iFontRenderer;
                    string2 = string3;
                } else {
                    string2 = module.getName();
                }
                if (iFontRenderer2.getStringWidth(string2) + 4 <= maxWidth) continue;
                IFontRenderer iFontRenderer3 = fontRenderer;
                if (upperCase) {
                    string4 = module.getName();
                    iFontRenderer = iFontRenderer3;
                    bl = false;
                    String string6 = string4;
                    if (string6 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    string3 = string6.toUpperCase();
                    iFontRenderer3 = iFontRenderer;
                    string = string3;
                } else {
                    string = module.getName();
                }
                maxWidth = (int)((float)iFontRenderer3.getStringWidth(string) + 7.0f);
            }
            this.menuWidth = maxWidth;
            float menuHeight = (float)this.modules.size() * ((Number)TabGUI.this.tabHeight.get()).floatValue();
            if (((Boolean)TabGUI.this.borderValue.get()).booleanValue()) {
                RainbowShader.Companion companion = RainbowShader.Companion;
                float f = ((Number)TabGUI.this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)TabGUI.this.rainbowX.get()).floatValue();
                float f2 = ((Number)TabGUI.this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)TabGUI.this.rainbowY.get()).floatValue();
                float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                $i$f$begin = false;
                instance$iv = RainbowShader.INSTANCE;
                if (borderRainbow) {
                    void y$iv;
                    void x$iv;
                    instance$iv.setStrengthX((float)x$iv);
                    instance$iv.setStrengthY((float)y$iv);
                    instance$iv.setOffset(offset$iv);
                    instance$iv.startShader();
                }
                this_$iv = instance$iv;
                boolean x$iv = false;
                Throwable y$iv = null;
                try {
                    RainbowShader it = (RainbowShader)this_$iv;
                    boolean bl = false;
                    RenderUtils.drawBorder(x - 1.0f, y - 1.0f, x + (float)this.menuWidth - 2.0f, y + menuHeight - 1.0f, borderStrength, borderColor);
                    it = Unit.INSTANCE;
                }
                catch (Throwable it) {
                    y$iv = it;
                    throw it;
                }
                finally {
                    CloseableKt.closeFinally((Closeable)this_$iv, (Throwable)y$iv);
                }
            }
            RenderUtils.drawRect(x - 1.0f, y - 1.0f, x + (float)this.menuWidth - 2.0f, y + menuHeight - 1.0f, backgroundColor);
            this_$iv = RainbowShader.Companion;
            float x$iv = ((Number)TabGUI.this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)TabGUI.this.rainbowX.get()).floatValue();
            float y$iv = ((Number)TabGUI.this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)TabGUI.this.rainbowY.get()).floatValue();
            float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
            $i$f$begin = false;
            instance$iv = RainbowShader.INSTANCE;
            if (rectRainbow) {
                instance$iv.setStrengthX(x$iv);
                instance$iv.setStrengthY(y$iv);
                instance$iv.setOffset(offset$iv);
                instance$iv.startShader();
            }
            this_$iv = instance$iv;
            boolean x$iv2 = false;
            Throwable y$iv2 = null;
            try {
                RainbowShader it = (RainbowShader)this_$iv;
                boolean bl = false;
                RenderUtils.drawRect(x - 1.0f, y + TabGUI.this.itemY - 1.0f, x + (float)this.menuWidth - 2.0f, y + TabGUI.this.itemY + ((Number)TabGUI.this.tabHeight.get()).floatValue() - 1.0f, color);
                Unit unit = Unit.INSTANCE;
            }
            catch (Throwable throwable) {
                y$iv2 = throwable;
                throw throwable;
            }
            finally {
                CloseableKt.closeFinally((Closeable)this_$iv, (Throwable)y$iv2);
            }
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Iterable $this$forEachIndexed$iv = this.modules;
            boolean $i$f$forEachIndexed = false;
            int index$iv = 0;
            for (Object item$iv : $this$forEachIndexed$iv) {
                String string;
                void module;
                int n = index$iv++;
                boolean bl = false;
                if (n < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n2 = n;
                Module module2 = (Module)item$iv;
                int index = n2;
                boolean bl2 = false;
                int moduleColor = module.getState() ? 0xFFFFFF : new Color(205, 205, 205).getRGB();
                IFontRenderer iFontRenderer = fontRenderer;
                if (upperCase) {
                    String string7 = module.getName();
                    IFontRenderer iFontRenderer4 = iFontRenderer;
                    boolean bl3 = false;
                    String string8 = string7;
                    if (string8 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string9 = string8.toUpperCase();
                    iFontRenderer = iFontRenderer4;
                    string = string9;
                } else {
                    string = module.getName();
                }
                iFontRenderer.drawString(string, x + 2.0f, y + ((Number)TabGUI.this.tabHeight.get()).floatValue() * (float)index + ((Number)TabGUI.this.textPositionY.get()).floatValue(), moduleColor, (Boolean)TabGUI.this.textShadow.get());
            }
        }

        public final String getTabName() {
            return this.tabName;
        }

        public Tab(String tabName) {
            List list;
            this.tabName = tabName;
            Tab tab = this;
            boolean bl = false;
            tab.modules = list = (List)new ArrayList();
        }
    }

    public static final class Action
    extends Enum<Action> {
        public static final /* enum */ Action UP;
        public static final /* enum */ Action DOWN;
        public static final /* enum */ Action LEFT;
        public static final /* enum */ Action RIGHT;
        public static final /* enum */ Action TOGGLE;
        private static final /* synthetic */ Action[] $VALUES;

        static {
            Action[] actionArray = new Action[5];
            Action[] actionArray2 = actionArray;
            actionArray[0] = UP = new Action();
            actionArray[1] = DOWN = new Action();
            actionArray[2] = LEFT = new Action();
            actionArray[3] = RIGHT = new Action();
            actionArray[4] = TOGGLE = new Action();
            $VALUES = actionArray;
        }

        public static Action[] values() {
            return (Action[])$VALUES.clone();
        }

        public static Action valueOf(String string) {
            return Enum.valueOf(Action.class, string);
        }
    }
}

