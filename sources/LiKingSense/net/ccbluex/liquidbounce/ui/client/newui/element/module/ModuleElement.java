/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.newui.element.module;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.clickgui.lbplus.element.module.value.impl.IntElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.lbplus.element.module.value.impl.ListElement;
import net.ccbluex.liquidbounce.ui.client.newui.element.components.ToggleSwitch;
import net.ccbluex.liquidbounce.ui.client.newui.element.module.value.ValueElement;
import net.ccbluex.liquidbounce.ui.client.newui.element.module.value.impl.BooleanElement;
import net.ccbluex.liquidbounce.ui.client.newui.element.module.value.impl.FloatElement;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0006\u0018\u0000 .2\u00020\u0001:\u0001.B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J>\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u00062\u0006\u0010#\u001a\u00020$J6\u0010%\u001a\u00020&2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u0006J\u0016\u0010'\u001a\u00020\r2\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u001dJ6\u0010+\u001a\u00020&2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u0006J\u0006\u0010,\u001a\u00020\rJ\u0006\u0010-\u001a\u00020&R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0018\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u001a0\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/newui/element/module/ModuleElement;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "module", "Lnet/ccbluex/liquidbounce/features/module/Module;", "(Lnet/ccbluex/liquidbounce/features/module/Module;)V", "animHeight", "", "getAnimHeight", "()F", "setAnimHeight", "(F)V", "animPercent", "expanded", "", "getExpanded", "()Z", "setExpanded", "(Z)V", "fadeKeybind", "listeningToKey", "getModule", "()Lnet/ccbluex/liquidbounce/features/module/Module;", "toggleSwitch", "Lnet/ccbluex/liquidbounce/ui/client/newui/element/components/ToggleSwitch;", "valueElements", "", "Lnet/ccbluex/liquidbounce/ui/client/newui/element/module/value/ValueElement;", "drawElement", "mouseX", "", "mouseY", "x", "y", "width", "height", "accentColor", "Ljava/awt/Color;", "handleClick", "", "handleKeyTyped", "typed", "", "code", "handleRelease", "listeningKeybind", "resetState", "Companion", "LiKingSense"})
public final class ModuleElement
extends MinecraftInstance {
    private final ToggleSwitch toggleSwitch;
    private final List<ValueElement<?>> valueElements;
    private float animHeight;
    private float fadeKeybind;
    private float animPercent;
    private boolean listeningToKey;
    private boolean expanded;
    @NotNull
    private final Module module;
    @NotNull
    private static final IResourceLocation expandIcon;
    public static final Companion Companion;

    public final float getAnimHeight() {
        return this.animHeight;
    }

    public final void setAnimHeight(float f) {
        this.animHeight = f;
    }

    public final boolean getExpanded() {
        return this.expanded;
    }

    public final void setExpanded(boolean bl) {
        this.expanded = bl;
    }

    /*
     * Exception decompiling
     */
    public final float drawElement(int mouseX, int mouseY, float x, float y, float width, float height, @NotNull Color accentColor) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl431 : ALOAD_0 - null : trying to set 0 previously set to 2
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
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

    public final void handleClick(int mouseX, int mouseY, float x, float y, float width, float height) {
        if (this.listeningToKey) {
            this.resetState();
            return;
        }
        String keyName = this.listeningToKey ? "Listening" : Keyboard.getKeyName((int)this.module.getKeyBind());
        float f = x + 25.0f + (float)Fonts.font40.getStringWidth(this.module.getName());
        float f2 = y + height / 2.0f - (float)Fonts.font40.getFontHeight() + 2.0f;
        float f3 = x + 35.0f + (float)Fonts.font40.getStringWidth(this.module.getName());
        String string = keyName;
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"keyName");
        if (MouseUtils.mouseWithinBounds(mouseX, mouseY, f, f2, f3 + (float)Fonts.font35.getStringWidth(string), y + height / 2.0f)) {
            this.listeningToKey = true;
            return;
        }
        if (MouseUtils.mouseWithinBounds(mouseX, mouseY, x + width - (this.module.getValues().size() > 0 ? 70.0f : 40.0f), y, x + width - (this.module.getValues().size() > 0 ? 50.0f : 20.0f), y + height)) {
            this.module.toggle();
        }
        if (this.module.getValues().size() > 0 && MouseUtils.mouseWithinBounds(mouseX, mouseY, x + width - 40.0f, y, x + width - 10.0f, y + height)) {
            boolean bl = this.expanded = !this.expanded;
        }
        if (this.expanded) {
            float startY = y + height;
            for (ValueElement<?> ve : this.valueElements) {
                if (!ve.isDisplayable()) continue;
                ve.onClick(mouseX, mouseY, x + 10.0f, startY, width - 20.0f);
                startY += ve.getValueHeight();
            }
        }
    }

    public final void handleRelease(int mouseX, int mouseY, float x, float y, float width, float height) {
        if (this.expanded) {
            float startY = y + height;
            for (ValueElement<?> ve : this.valueElements) {
                if (!ve.isDisplayable()) continue;
                ve.onRelease(mouseX, mouseY, x + 10.0f, startY, width - 20.0f);
                startY += ve.getValueHeight();
            }
        }
    }

    public final boolean handleKeyTyped(char typed, int code) {
        if (this.listeningToKey) {
            if (code == 1) {
                this.module.setKeyBind(0);
                this.listeningToKey = false;
            } else {
                this.module.setKeyBind(code);
                this.listeningToKey = false;
            }
            return true;
        }
        if (this.expanded) {
            for (ValueElement<?> ve : this.valueElements) {
                if (!ve.isDisplayable() || !ve.onKeyPress(typed, code)) continue;
                return true;
            }
        }
        return false;
    }

    public final boolean listeningKeybind() {
        return this.listeningToKey;
    }

    public final void resetState() {
        this.listeningToKey = false;
    }

    @NotNull
    public final Module getModule() {
        return this.module;
    }

    public ModuleElement(@NotNull Module module) {
        List list;
        Intrinsics.checkParameterIsNotNull((Object)module, (String)"module");
        this.module = module;
        this.toggleSwitch = new ToggleSwitch();
        ModuleElement moduleElement = this;
        boolean bl = false;
        moduleElement.valueElements = list = (List)new ArrayList();
        for (Value<?> value : this.module.getValues()) {
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

    static {
        Companion = new Companion(null);
        expandIcon = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/expand.png");
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/newui/element/module/ModuleElement$Companion;", "", "()V", "expandIcon", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getExpandIcon", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "LiKingSense"})
    public static final class Companion {
        @NotNull
        protected final IResourceLocation getExpandIcon() {
            return expandIcon;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

