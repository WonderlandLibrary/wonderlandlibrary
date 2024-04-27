/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.TabGUI$WhenMappings;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ElementInfo(name="TabGUI")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001:\u0002:;B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\n\u0010/\u001a\u0004\u0018\u000100H\u0016J\u0018\u00101\u001a\u0002022\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u00020#H\u0016J\u0010\u00106\u001a\u0002022\u0006\u00107\u001a\u000208H\u0002J\b\u00109\u001a\u000202H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0018\u0010'\u001a\f\u0012\b\u0012\u00060)R\u00020\u00000(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006<"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/TabGUI;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "(DD)V", "alphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "arrowsValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "backgroundAlphaValue", "backgroundBlueValue", "backgroundGreenValue", "backgroundRedValue", "blueValue", "borderAlphaValue", "borderBlueValue", "borderGreenValue", "borderRainbow", "borderRedValue", "borderStrength", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "borderValue", "categoryMenu", "", "fontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "greenValue", "itemY", "", "rainbowX", "rainbowY", "rectangleRainbow", "redValue", "selectedCategory", "", "selectedModule", "tabHeight", "tabY", "tabs", "", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/TabGUI$Tab;", "textFade", "textPositionY", "textShadow", "upperCaseValue", "width", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "handleKey", "", "c", "", "keyCode", "parseAction", "action", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/TabGUI$Action;", "updateAnimation", "Action", "Tab", "LiKingSense"})
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
     * Exception decompiling
     */
    @Override
    @Nullable
    public Border drawElement() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl479 : INVOKESPECIAL - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
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
                break;
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
                break;
            }
        }
        if (toggle) {
            int sel = this.selectedModule;
            this.tabs.get(this.selectedCategory).getModules().get(sel).toggle();
        }
    }

    /*
     * Exception decompiling
     */
    public TabGUI(double x, double y) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl33 : PUTFIELD - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
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

    public static final /* synthetic */ FloatValue access$getTabHeight$p(TabGUI $this) {
        return $this.tabHeight;
    }

    public static final /* synthetic */ BoolValue access$getBorderValue$p(TabGUI $this) {
        return $this.borderValue;
    }

    public static final /* synthetic */ FloatValue access$getRainbowX$p(TabGUI $this) {
        return $this.rainbowX;
    }

    public static final /* synthetic */ FloatValue access$getRainbowY$p(TabGUI $this) {
        return $this.rainbowY;
    }

    public static final /* synthetic */ float access$getItemY$p(TabGUI $this) {
        return $this.itemY;
    }

    public static final /* synthetic */ void access$setItemY$p(TabGUI $this, float f) {
        $this.itemY = f;
    }

    public static final /* synthetic */ FloatValue access$getTextPositionY$p(TabGUI $this) {
        return $this.textPositionY;
    }

    public static final /* synthetic */ BoolValue access$getTextShadow$p(TabGUI $this) {
        return $this.textShadow;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004JV\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017\u00a8\u0006&"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/TabGUI$Tab;", "", "tabName", "", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/TabGUI;Ljava/lang/String;)V", "menuWidth", "", "getMenuWidth", "()I", "setMenuWidth", "(I)V", "modules", "", "Lnet/ccbluex/liquidbounce/features/module/Module;", "getModules", "()Ljava/util/List;", "getTabName", "()Ljava/lang/String;", "textFade", "", "getTextFade", "()F", "setTextFade", "(F)V", "drawTab", "", "x", "y", "color", "backgroundColor", "borderColor", "borderStrength", "upperCase", "", "fontRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "borderRainbow", "rectRainbow", "LiKingSense"})
    private final class Tab {
        @NotNull
        private final List<Module> modules;
        private int menuWidth;
        private float textFade;
        @NotNull
        private final String tabName;

        @NotNull
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
         * Exception decompiling
         */
        public final void drawTab(float x, float y, int color, int backgroundColor, int borderColor, float borderStrength, boolean upperCase, @NotNull IFontRenderer fontRenderer, boolean borderRainbow, boolean rectRainbow) {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl425 : INVOKESPECIAL - null : Stack underflow
             *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        }

        @NotNull
        public final String getTabName() {
            return this.tabName;
        }

        public Tab(String tabName) {
            List list;
            Intrinsics.checkParameterIsNotNull((Object)tabName, (String)"tabName");
            this.tabName = tabName;
            Tab tab = this;
            boolean bl = false;
            tab.modules = list = (List)new ArrayList();
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/TabGUI$Action;", "", "(Ljava/lang/String;I)V", "UP", "DOWN", "LEFT", "RIGHT", "TOGGLE", "LiKingSense"})
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

