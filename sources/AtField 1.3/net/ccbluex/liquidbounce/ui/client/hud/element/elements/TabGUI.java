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
import java.util.Iterator;
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
    private final FloatValue rainbowY;
    private final FontValue fontValue;
    private final FloatValue borderStrength;
    private final IntegerValue greenValue;
    private int selectedModule;
    private final BoolValue upperCaseValue;
    private final IntegerValue backgroundRedValue;
    private final IntegerValue alphaValue;
    private final FloatValue rainbowX;
    private final IntegerValue borderBlueValue;
    private float tabY;
    private final BoolValue borderValue;
    private final IntegerValue backgroundGreenValue;
    private final IntegerValue blueValue;
    private final FloatValue tabHeight;
    private final BoolValue textFade;
    private final FloatValue width;
    private final List tabs;
    private int selectedCategory;
    private final BoolValue rectangleRainbow;
    private final IntegerValue backgroundBlueValue;
    private final IntegerValue backgroundAlphaValue;
    private final BoolValue textShadow;
    private final BoolValue borderRainbow;
    private final BoolValue arrowsValue;
    private final IntegerValue borderRedValue;
    private final FloatValue textPositionY;
    private float itemY;
    private boolean categoryMenu;
    private final IntegerValue borderGreenValue;
    private final IntegerValue redValue;
    private final IntegerValue borderAlphaValue;

    public TabGUI() {
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }

    public TabGUI(double d, double d2, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 5.0;
        }
        if ((n & 2) != 0) {
            d2 = 25.0;
        }
        this(d, d2);
    }

    public static final float access$getItemY$p(TabGUI tabGUI) {
        return tabGUI.itemY;
    }

    private final void updateAnimation() {
        int n = RenderUtils.deltaTime;
        float f = ((Number)this.tabHeight.get()).floatValue() * (float)this.selectedCategory;
        this.tabY = (int)this.tabY != (int)f ? (f > this.tabY ? (this.tabY += 0.1f * (float)n) : (this.tabY -= 0.1f * (float)n)) : f;
        float f2 = ((Number)this.tabHeight.get()).floatValue() * (float)this.selectedModule;
        this.itemY = (int)this.itemY != (int)f2 ? (f2 > this.itemY ? (this.itemY += 0.1f * (float)n) : (this.itemY -= 0.1f * (float)n)) : f2;
        if (this.categoryMenu) {
            this.itemY = 0.0f;
        }
        if (((Boolean)this.textFade.get()).booleanValue()) {
            Iterable iterable = this.tabs;
            boolean bl = false;
            int n2 = 0;
            for (Object t : iterable) {
                int n3 = n2++;
                boolean bl2 = false;
                if (n3 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n4 = n3;
                Tab tab = (Tab)t;
                int n5 = n4;
                boolean bl3 = false;
                if (n5 == this.selectedCategory) {
                    if (tab.getTextFade() < (float)4) {
                        Tab tab2 = tab;
                        tab2.setTextFade(tab2.getTextFade() + 0.05f * (float)n);
                    }
                    if (!(tab.getTextFade() > (float)4)) continue;
                    tab.setTextFade(4.0f);
                    continue;
                }
                if (tab.getTextFade() > 0.0f) {
                    Tab tab3 = tab;
                    tab3.setTextFade(tab3.getTextFade() - 0.05f * (float)n);
                }
                if (!(tab.getTextFade() < 0.0f)) continue;
                tab.setTextFade(0.0f);
            }
        } else {
            for (Tab tab : this.tabs) {
                if (tab.getTextFade() > 0.0f) {
                    Tab tab4 = tab;
                    tab4.setTextFade(tab4.getTextFade() - 0.05f * (float)n);
                }
                if (!(tab.getTextFade() < 0.0f)) continue;
                tab.setTextFade(0.0f);
            }
        }
    }

    public static final BoolValue access$getTextShadow$p(TabGUI tabGUI) {
        return tabGUI.textShadow;
    }

    public static final FloatValue access$getTextPositionY$p(TabGUI tabGUI) {
        return tabGUI.textPositionY;
    }

    @Override
    public void handleKey(char c, int n) {
        switch (n) {
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
                break;
            }
        }
    }

    @Override
    public Border drawElement() {
        RainbowShader rainbowShader4;
        boolean bl;
        Object object;
        this.updateAnimation();
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        IFontRenderer iFontRenderer = (IFontRenderer)this.fontValue.get();
        boolean bl2 = (Boolean)this.rectangleRainbow.get();
        Color color = new Color(((Number)this.backgroundRedValue.get()).intValue(), ((Number)this.backgroundGreenValue.get()).intValue(), ((Number)this.backgroundBlueValue.get()).intValue(), ((Number)this.backgroundAlphaValue.get()).intValue());
        Color color2 = (Boolean)this.borderRainbow.get() == false ? new Color(((Number)this.borderRedValue.get()).intValue(), ((Number)this.borderGreenValue.get()).intValue(), ((Number)this.borderBlueValue.get()).intValue(), ((Number)this.borderAlphaValue.get()).intValue()) : Color.black;
        float f = (float)this.tabs.size() * ((Number)this.tabHeight.get()).floatValue();
        RenderUtils.drawRect(1.0f, 0.0f, ((Number)this.width.get()).floatValue(), f, color.getRGB());
        if (((Boolean)this.borderValue.get()).booleanValue()) {
            object = RainbowShader.Companion;
            boolean bl3 = (Boolean)this.borderRainbow.get();
            float f2 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
            float f3 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
            float f4 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
            bl = false;
            rainbowShader4 = RainbowShader.INSTANCE;
            if (bl3) {
                rainbowShader4.setStrengthX(f2);
                rainbowShader4.setStrengthY(f3);
                rainbowShader4.setOffset(f4);
                rainbowShader4.startShader();
            }
            object = rainbowShader4;
            bl3 = false;
            Throwable throwable = null;
            try {
                RainbowShader rainbowShader2 = (RainbowShader)object;
                boolean bl4 = false;
                RenderUtils.drawBorder(1.0f, 0.0f, ((Number)this.width.get()).floatValue(), f, ((Number)this.borderStrength.get()).floatValue(), color2.getRGB());
                rainbowShader2 = Unit.INSTANCE;
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
            CloseableKt.closeFinally((Closeable)object, (Throwable)throwable);
        }
        object = !bl2 ? new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue()) : Color.black;
        Object object2 = RainbowShader.Companion;
        float f5 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
        float f6 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
        float f7 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
        bl = false;
        rainbowShader4 = RainbowShader.INSTANCE;
        if (bl2) {
            rainbowShader4.setStrengthX(f5);
            rainbowShader4.setStrengthY(f6);
            rainbowShader4.setOffset(f7);
            rainbowShader4.startShader();
        }
        object2 = rainbowShader4;
        boolean bl5 = false;
        Throwable throwable = null;
        try {
            RainbowShader rainbowShader3 = (RainbowShader)object2;
            bl = false;
            RenderUtils.drawRect(1.0f, 1.0f + this.tabY - 1.0f, ((Number)this.width.get()).floatValue(), this.tabY + ((Number)this.tabHeight.get()).floatValue(), (Color)object);
            rainbowShader3 = Unit.INSTANCE;
        }
        catch (Throwable throwable3) {
            throwable = throwable3;
            throw throwable3;
        }
        CloseableKt.closeFinally((Closeable)object2, (Throwable)throwable);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f8 = 1.0f;
        Iterable iterable = this.tabs;
        boolean bl6 = false;
        int n = 0;
        for (RainbowShader rainbowShader4 : iterable) {
            String string;
            int n2 = n++;
            boolean bl7 = false;
            if (n2 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n3 = n2;
            Tab tab = (Tab)((Object)rainbowShader4);
            int n4 = n3;
            boolean bl8 = false;
            if (((Boolean)this.upperCaseValue.get()).booleanValue()) {
                String string2 = tab.getTabName();
                boolean bl9 = false;
                String string3 = string2;
                if (string3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string = string3.toUpperCase();
            } else {
                string = tab.getTabName();
            }
            String string4 = string;
            float f9 = this.getSide().getHorizontal() == Side.Horizontal.RIGHT ? ((Number)this.width.get()).floatValue() - (float)iFontRenderer.getStringWidth(string4) - tab.getTextFade() - (float)3 : tab.getTextFade() + (float)5;
            float f10 = f8 + ((Number)this.textPositionY.get()).floatValue();
            int n5 = this.selectedCategory == n4 ? 0xFFFFFF : new Color(210, 210, 210).getRGB();
            iFontRenderer.drawString(string4, f9, f10, n5, (Boolean)this.textShadow.get());
            if (((Boolean)this.arrowsValue.get()).booleanValue()) {
                if (this.getSide().getHorizontal() == Side.Horizontal.RIGHT) {
                    iFontRenderer.drawString(!this.categoryMenu && this.selectedCategory == n4 ? ">" : "<", 3.0f, f8 + 2.0f, 0xFFFFFF, (Boolean)this.textShadow.get());
                } else {
                    iFontRenderer.drawString(!this.categoryMenu && this.selectedCategory == n4 ? "<" : ">", ((Number)this.width.get()).floatValue() - 8.0f, f8 + 2.0f, 0xFFFFFF, (Boolean)this.textShadow.get());
                }
            }
            if (n4 == this.selectedCategory && !this.categoryMenu) {
                float f11 = this.getSide().getHorizontal() == Side.Horizontal.RIGHT ? 1.0f - (float)tab.getMenuWidth() : ((Number)this.width.get()).floatValue() + (float)5;
                tab.drawTab(f11, f8, ((Color)object).getRGB(), color.getRGB(), color2.getRGB(), ((Number)this.borderStrength.get()).floatValue(), (Boolean)this.upperCaseValue.get(), iFontRenderer, (Boolean)this.borderRainbow.get(), bl2);
            }
            f8 += ((Number)this.tabHeight.get()).floatValue();
        }
        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
        return new Border(1.0f, 0.0f, ((Number)this.width.get()).floatValue(), f);
    }

    public static final FloatValue access$getRainbowY$p(TabGUI tabGUI) {
        return tabGUI.rainbowY;
    }

    public static final FloatValue access$getRainbowX$p(TabGUI tabGUI) {
        return tabGUI.rainbowX;
    }

    public TabGUI(double d, double d2) {
        super(d, d2, 0.0f, null, 12, null);
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
        this.fontValue = new FontValue("Font", Fonts.roboto35);
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
        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
            Tab tab = new Tab(this, moduleCategory.getDisplayName());
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
                if (!(moduleCategory == module.getCategory())) continue;
                collection2.add(t);
            }
            iterable = (List)collection2;
            bl2 = false;
            for (Collection collection2 : iterable) {
                Module module = (Module)((Object)collection2);
                boolean bl5 = false;
                tab.getModules().add(module);
            }
            this.tabs.add(tab);
        }
    }

    private final void parseAction(Action action) {
        boolean bl = false;
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
                this.selectedModule = ((Tab)this.tabs.get(this.selectedCategory)).getModules().size() - 1;
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
                if (this.selectedModule <= ((Tab)this.tabs.get(this.selectedCategory)).getModules().size() - 1) break;
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
                    bl = true;
                    break;
                }
                this.categoryMenu = false;
                this.selectedModule = 0;
                break;
            }
            case 5: {
                if (this.categoryMenu) break;
                bl = true;
                break;
            }
        }
        if (bl) {
            int n = this.selectedModule;
            ((Module)((Tab)this.tabs.get(this.selectedCategory)).getModules().get(n)).toggle();
        }
    }

    public static final FloatValue access$getTabHeight$p(TabGUI tabGUI) {
        return tabGUI.tabHeight;
    }

    public static final BoolValue access$getBorderValue$p(TabGUI tabGUI) {
        return tabGUI.borderValue;
    }

    public static final void access$setItemY$p(TabGUI tabGUI, float f) {
        tabGUI.itemY = f;
    }

    public static final class Action
    extends Enum {
        public static final /* enum */ Action LEFT;
        public static final /* enum */ Action DOWN;
        public static final /* enum */ Action TOGGLE;
        public static final /* enum */ Action RIGHT;
        public static final /* enum */ Action UP;
        private static final Action[] $VALUES;

        static {
            Action[] actionArray = new Action[5];
            Action[] actionArray2 = actionArray;
            actionArray[0] = UP = new Action("UP", 0);
            actionArray[1] = DOWN = new Action("DOWN", 1);
            actionArray[2] = LEFT = new Action("LEFT", 2);
            actionArray[3] = RIGHT = new Action("RIGHT", 3);
            actionArray[4] = TOGGLE = new Action("TOGGLE", 4);
            $VALUES = actionArray;
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private Action() {
            void var2_-1;
            void var1_-1;
        }

        public static Action valueOf(String string) {
            return Enum.valueOf(Action.class, string);
        }

        public static Action[] values() {
            return (Action[])$VALUES.clone();
        }
    }

    private final class Tab {
        private final List modules;
        private final String tabName;
        private int menuWidth;
        private float textFade;
        final TabGUI this$0;

        public final void setMenuWidth(int n) {
            this.menuWidth = n;
        }

        public final void drawTab(float f, float f2, int n, int n2, int n3, float f3, boolean bl, IFontRenderer iFontRenderer, boolean bl2, boolean bl3) {
            Object object;
            RainbowShader rainbowShader;
            boolean bl4;
            Object object2;
            int n4 = 0;
            for (Module module : this.modules) {
                String string;
                String string2;
                String string3;
                boolean bl5;
                IFontRenderer iFontRenderer2;
                String string4;
                IFontRenderer iFontRenderer3 = iFontRenderer;
                if (bl) {
                    string4 = module.getName();
                    iFontRenderer2 = iFontRenderer3;
                    bl5 = false;
                    String string5 = string4;
                    if (string5 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    string3 = string5.toUpperCase();
                    iFontRenderer3 = iFontRenderer2;
                    string2 = string3;
                } else {
                    string2 = module.getName();
                }
                if (iFontRenderer3.getStringWidth(string2) + 4 <= n4) continue;
                IFontRenderer iFontRenderer4 = iFontRenderer;
                if (bl) {
                    string4 = module.getName();
                    iFontRenderer2 = iFontRenderer4;
                    bl5 = false;
                    String string6 = string4;
                    if (string6 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    string3 = string6.toUpperCase();
                    iFontRenderer4 = iFontRenderer2;
                    string = string3;
                } else {
                    string = module.getName();
                }
                n4 = (int)((float)iFontRenderer4.getStringWidth(string) + 7.0f);
            }
            this.menuWidth = n4;
            float f4 = (float)this.modules.size() * ((Number)TabGUI.access$getTabHeight$p(this.this$0).get()).floatValue();
            if (((Boolean)TabGUI.access$getBorderValue$p(this.this$0).get()).booleanValue()) {
                object2 = RainbowShader.Companion;
                float f5 = ((Number)TabGUI.access$getRainbowX$p(this.this$0).get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)TabGUI.access$getRainbowX$p(this.this$0).get()).floatValue();
                float f6 = ((Number)TabGUI.access$getRainbowY$p(this.this$0).get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)TabGUI.access$getRainbowY$p(this.this$0).get()).floatValue();
                float f7 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                bl4 = false;
                rainbowShader = RainbowShader.INSTANCE;
                if (bl2) {
                    rainbowShader.setStrengthX(f5);
                    rainbowShader.setStrengthY(f6);
                    rainbowShader.setOffset(f7);
                    rainbowShader.startShader();
                }
                object2 = rainbowShader;
                boolean bl6 = false;
                Throwable throwable = null;
                try {
                    RainbowShader rainbowShader2 = (RainbowShader)object2;
                    bl4 = false;
                    RenderUtils.drawBorder(f - 1.0f, f2 - 1.0f, f + (float)this.menuWidth - 2.0f, f2 + f4 - 1.0f, f3, n3);
                    rainbowShader2 = Unit.INSTANCE;
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                    throw throwable2;
                }
                CloseableKt.closeFinally((Closeable)object2, (Throwable)throwable);
            }
            RenderUtils.drawRect(f - 1.0f, f2 - 1.0f, f + (float)this.menuWidth - 2.0f, f2 + f4 - 1.0f, n2);
            object2 = RainbowShader.Companion;
            float f8 = ((Number)TabGUI.access$getRainbowX$p(this.this$0).get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)TabGUI.access$getRainbowX$p(this.this$0).get()).floatValue();
            float f9 = ((Number)TabGUI.access$getRainbowY$p(this.this$0).get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)TabGUI.access$getRainbowY$p(this.this$0).get()).floatValue();
            float f10 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
            bl4 = false;
            rainbowShader = RainbowShader.INSTANCE;
            if (bl3) {
                rainbowShader.setStrengthX(f8);
                rainbowShader.setStrengthY(f9);
                rainbowShader.setOffset(f10);
                rainbowShader.startShader();
            }
            object2 = rainbowShader;
            boolean bl7 = false;
            Throwable throwable = null;
            try {
                object = (RainbowShader)object2;
                bl4 = false;
                RenderUtils.drawRect(f - 1.0f, f2 + TabGUI.access$getItemY$p(this.this$0) - 1.0f, f + (float)this.menuWidth - 2.0f, f2 + TabGUI.access$getItemY$p(this.this$0) + ((Number)TabGUI.access$getTabHeight$p(this.this$0).get()).floatValue() - 1.0f, n);
                object = Unit.INSTANCE;
            }
            catch (Throwable throwable3) {
                throwable = throwable3;
                throw throwable3;
            }
            CloseableKt.closeFinally((Closeable)object2, (Throwable)throwable);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            object2 = this.modules;
            bl7 = false;
            int n5 = 0;
            object = object2.iterator();
            while (object.hasNext()) {
                String string;
                Object e = object.next();
                int n6 = n5++;
                boolean bl8 = false;
                if (n6 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                int n7 = n6;
                Module module = (Module)e;
                int n8 = n7;
                boolean bl9 = false;
                int n9 = module.getState() ? 0xFFFFFF : new Color(205, 205, 205).getRGB();
                IFontRenderer iFontRenderer5 = iFontRenderer;
                if (bl) {
                    String string7 = module.getName();
                    IFontRenderer iFontRenderer6 = iFontRenderer5;
                    boolean bl10 = false;
                    String string8 = string7;
                    if (string8 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string9 = string8.toUpperCase();
                    iFontRenderer5 = iFontRenderer6;
                    string = string9;
                } else {
                    string = module.getName();
                }
                iFontRenderer5.drawString(string, f + 2.0f, f2 + ((Number)TabGUI.access$getTabHeight$p(this.this$0).get()).floatValue() * (float)n8 + ((Number)TabGUI.access$getTextPositionY$p(this.this$0).get()).floatValue(), n9, (Boolean)TabGUI.access$getTextShadow$p(this.this$0).get());
            }
        }

        public Tab(TabGUI tabGUI, String string) {
            List list;
            this.this$0 = tabGUI;
            this.tabName = string;
            Tab tab = this;
            boolean bl = false;
            tab.modules = list = (List)new ArrayList();
        }

        public final List getModules() {
            return this.modules;
        }

        public final void setTextFade(float f) {
            this.textFade = f;
        }

        public final String getTabName() {
            return this.tabName;
        }

        public final float getTextFade() {
            return this.textFade;
        }

        public final int getMenuWidth() {
            return this.menuWidth;
        }
    }
}

