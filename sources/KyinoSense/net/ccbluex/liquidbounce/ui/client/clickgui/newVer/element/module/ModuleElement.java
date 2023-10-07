/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.ColorManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.components.ToggleSwitch;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.value.ValueElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.value.impl.BooleanElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.value.impl.FloatElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.value.impl.IntElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element.module.value.impl.ListElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.newVer.extensions.AnimHelperKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.ccbluex.liquidbounce.utils.render.BlendUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0006\u0018\u0000 .2\u00020\u0001:\u0001.B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J>\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u00062\u0006\u0010#\u001a\u00020$J6\u0010%\u001a\u00020&2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u0006J\u0016\u0010'\u001a\u00020\r2\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u001dJ6\u0010+\u001a\u00020&2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\u0006J\u0006\u0010,\u001a\u00020\rJ\u0006\u0010-\u001a\u00020&R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0018\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u001a0\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/module/ModuleElement;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "module", "Lnet/ccbluex/liquidbounce/features/module/Module;", "(Lnet/ccbluex/liquidbounce/features/module/Module;)V", "animHeight", "", "getAnimHeight", "()F", "setAnimHeight", "(F)V", "animPercent", "expanded", "", "getExpanded", "()Z", "setExpanded", "(Z)V", "fadeKeybind", "listeningToKey", "getModule", "()Lnet/ccbluex/liquidbounce/features/module/Module;", "toggleSwitch", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/components/ToggleSwitch;", "valueElements", "", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/module/value/ValueElement;", "drawElement", "mouseX", "", "mouseY", "x", "y", "width", "height", "accentColor", "Ljava/awt/Color;", "handleClick", "", "handleKeyTyped", "typed", "", "code", "handleRelease", "listeningKeybind", "resetState", "Companion", "KyinoClient"})
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
    private static final ResourceLocation expandIcon;
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

    public final float drawElement(int mouseX, int mouseY, float x, float y, float width, float height, @NotNull Color accentColor) {
        Intrinsics.checkParameterIsNotNull(accentColor, "accentColor");
        this.animPercent = AnimHelperKt.animSmooth(this.animPercent, this.expanded ? 100.0f : 0.0f, 0.5f);
        float expectedHeight = 0.0f;
        for (ValueElement<?> ve : this.valueElements) {
            if (!ve.isDisplayable()) continue;
            expectedHeight += ve.getValueHeight();
        }
        this.animHeight = this.animPercent / 100.0f * (expectedHeight + 10.0f);
        RenderUtils.originalRoundedRect(x + 9.5f, y + 4.5f, x + width - 9.5f, y + height + this.animHeight - 4.5f, 4.0f, ColorManager.INSTANCE.getButtonOutline().getRGB());
        Stencil.write(true);
        RenderUtils.originalRoundedRect(x + 10.0f, y + 5.0f, x + width - 10.0f, y + height + this.animHeight - 5.0f, 4.0f, ColorManager.INSTANCE.getModuleBackground().getRGB());
        Stencil.erase(true);
        RenderUtils.newDrawRect(x + 10.0f, y + height - 5.0f, x + width - 10.0f, y + height - 4.5f, (int)0xFF303030L);
        Fonts.font40.drawString(this.module.getName(), x + 20.0f, y + height / 2.0f - (float)Fonts.font40.field_78288_b + 3.0f, -1);
        Fonts.fontTiny.drawString(this.module.getDescription(), x + 20.0f, y + height / 2.0f + 4.0f, (int)0xA0A0A0L);
        String keyName = this.listeningToKey ? "Listening" : Keyboard.getKeyName((int)this.module.getKeyBind());
        float f = x + 25.0f + (float)Fonts.font40.func_78256_a(this.module.getName());
        float f2 = y + height / 2.0f - (float)Fonts.font40.field_78288_b + 2.0f;
        float f3 = x + 35.0f + (float)Fonts.font40.func_78256_a(this.module.getName());
        String string = keyName;
        Intrinsics.checkExpressionValueIsNotNull(string, "keyName");
        this.fadeKeybind = MouseUtils.mouseWithinBounds(mouseX, mouseY, f, f2, f3 + (float)Fonts.fontTiny.func_78256_a(string), y + height / 2.0f) ? RangesKt.coerceIn(this.fadeKeybind + 0.1f * (float)RenderUtils.deltaTime * 0.025f, 0.0f, 1.0f) : RangesKt.coerceIn(this.fadeKeybind - 0.1f * (float)RenderUtils.deltaTime * 0.025f, 0.0f, 1.0f);
        float f4 = x + 25.0f + (float)Fonts.font40.func_78256_a(this.module.getName());
        float f5 = y + height / 2.0f - (float)Fonts.font40.field_78288_b + 2.0f;
        float f6 = x + 35.0f + (float)Fonts.font40.func_78256_a(this.module.getName()) + (float)Fonts.fontTiny.func_78256_a(keyName);
        float f7 = y + height / 2.0f;
        Color color = BlendUtils.blend(new Color((int)0xFF454545L), new Color((int)0xFF353535L), this.fadeKeybind);
        Intrinsics.checkExpressionValueIsNotNull(color, "BlendUtils.blend(Color(4\u2026, fadeKeybind.toDouble())");
        RenderUtils.originalRoundedRect(f4, f5, f6, f7, 2.0f, color.getRGB());
        Fonts.fontTiny.drawString(keyName, x + 30.5f + (float)Fonts.font40.func_78256_a(this.module.getName()), y + height / 2.0f - (float)Fonts.font40.field_78288_b + 5.5f, -1);
        this.toggleSwitch.setState(this.module.getState());
        if (this.module.getValues().size() > 0) {
            RenderUtils.newDrawRect(x + width - 40.0f, y + 5.0f, x + width - 39.5f, y + height - 5.0f, (int)0xFF303030L);
            GlStateManager.func_179117_G();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(x + width - 25.0f), (float)(y + height / 2.0f), (float)0.0f);
            GL11.glPushMatrix();
            GL11.glRotatef((float)(180.0f * (this.animHeight / (expectedHeight + 10.0f))), (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderUtils.drawImage(expandIcon, -4, -4, 8, 8);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            this.toggleSwitch.onDraw(x + width - 70.0f, y + height / 2.0f - 5.0f, 20.0f, 10.0f, new Color((int)0xFF252525L), accentColor);
        } else {
            this.toggleSwitch.onDraw(x + width - 40.0f, y + height / 2.0f - 5.0f, 20.0f, 10.0f, new Color((int)0xFF252525L), accentColor);
        }
        if (this.expanded || this.animHeight > 0.0f) {
            float startYPos = y + height;
            for (ValueElement<?> ve : this.valueElements) {
                if (!ve.isDisplayable()) continue;
                startYPos += ve.drawElement(mouseX, mouseY, x + 10.0f, startYPos, width - 20.0f, new Color((int)0xFF252525L), accentColor);
            }
        }
        Stencil.dispose();
        return height + this.animHeight;
    }

    public final void handleClick(int mouseX, int mouseY, float x, float y, float width, float height) {
        if (this.listeningToKey) {
            this.resetState();
            return;
        }
        String keyName = this.listeningToKey ? "Listening" : Keyboard.getKeyName((int)this.module.getKeyBind());
        float f = x + 25.0f + (float)Fonts.font40.func_78256_a(this.module.getName());
        float f2 = y + height / 2.0f - (float)Fonts.font40.field_78288_b + 2.0f;
        float f3 = x + 35.0f + (float)Fonts.font40.func_78256_a(this.module.getName());
        String string = keyName;
        Intrinsics.checkExpressionValueIsNotNull(string, "keyName");
        if (MouseUtils.mouseWithinBounds(mouseX, mouseY, f, f2, f3 + (float)Fonts.fontTiny.func_78256_a(string), y + height / 2.0f)) {
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
        Intrinsics.checkParameterIsNotNull(module, "module");
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
        expandIcon = new ResourceLocation("liquidbounce/expand.png");
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/module/ModuleElement$Companion;", "", "()V", "expandIcon", "Lnet/minecraft/util/ResourceLocation;", "getExpandIcon", "()Lnet/minecraft/util/ResourceLocation;", "KyinoClient"})
    public static final class Companion {
        @NotNull
        protected final ResourceLocation getExpandIcon() {
            return expandIcon;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

