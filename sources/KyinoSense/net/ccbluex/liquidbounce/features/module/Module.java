/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.script.api.ScriptModule;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.flux.AnimationHelper;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Translate;
import net.ccbluex.liquidbounce.utils.Translate3;
import net.ccbluex.liquidbounce.utils.render.Animation;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SideOnly(value=Side.CLIENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0015\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0007\b'\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u000e\u0010y\u001a\u00020&2\u0006\u0010z\u001a\u00020\u0005J\u000e\u0010%\u001a\u00020&2\u0006\u0010{\u001a\u00020\u0005J\u000e\u0010\u0006\u001a\u00020&2\u0006\u0010z\u001a\u00020\u0005J\u0006\u0010|\u001a\u00020\u0005J\u0016\u0010}\u001a\b\u0012\u0002\b\u0003\u0018\u00010O2\u0006\u0010~\u001a\u00020&H\u0016J\b\u0010\u007f\u001a\u00020\u0005H\u0016J\n\u0010\u0080\u0001\u001a\u00030\u0081\u0001H\u0016J\n\u0010\u0082\u0001\u001a\u00030\u0081\u0001H\u0016J\n\u0010\u0083\u0001\u001a\u00030\u0081\u0001H\u0016J\u0012\u0010\u0084\u0001\u001a\u00030\u0081\u00012\u0006\u0010`\u001a\u00020\u0005H\u0016J\u0011\u0010\u0085\u0001\u001a\u00030\u0081\u00012\u0007\u0010\u0086\u0001\u001a\u00020\u0005J\u000e\u0010e\u001a\u00020&2\u0006\u0010{\u001a\u00020\u0005J\b\u0010\u0087\u0001\u001a\u00030\u0081\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR$\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0005@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0007\"\u0004\b\u0012\u0010\tR\u001a\u0010\u0013\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a8F\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u000e\u0010\u001e\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001f\u001a\u00020 X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u0011\u0010%\u001a\u00020&8F\u00a2\u0006\u0006\u001a\u0004\b'\u0010(R\u001a\u0010)\u001a\u00020&X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010(\"\u0004\b+\u0010,R\u001a\u0010-\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0007\"\u0004\b/\u0010\tR\u001a\u00100\u001a\u00020&X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u0010(\"\u0004\b2\u0010,R\u001a\u00103\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0016\"\u0004\b5\u0010\u0018R\u001a\u00106\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\u0016\"\u0004\b8\u0010\u0018R\u0011\u00109\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010\u0016R$\u0010;\u001a\u00020<2\u0006\u0010;\u001a\u00020<@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b=\u0010>\"\u0004\b?\u0010@R\u0017\u0010A\u001a\b\u0012\u0004\u0012\u00020B0\u001a8F\u00a2\u0006\u0006\u001a\u0004\bC\u0010\u001dR\u001c\u0010D\u001a\u00020&8FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bE\u0010(\"\u0004\bF\u0010,R\u0011\u0010G\u001a\u00020H\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010JR\u001a\u0010K\u001a\u00020&X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bL\u0010(\"\u0004\bM\u0010,R\u001b\u0010N\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030O0\u001a8F\u00a2\u0006\u0006\u001a\u0004\bP\u0010\u001dR\u000e\u0010Q\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010R\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010S\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bT\u0010\u0016\"\u0004\bU\u0010\u0018R\u001a\u0010V\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bW\u0010\u0016\"\u0004\bX\u0010\u0018R\u001a\u0010Y\u001a\u00020&X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bZ\u0010(\"\u0004\b[\u0010,R\u001a\u0010\\\u001a\u00020&X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b]\u0010(\"\u0004\b^\u0010,R$\u0010`\u001a\u00020\u00052\u0006\u0010_\u001a\u00020\u0005@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\ba\u0010\u0007\"\u0004\bb\u0010\tR\u0016\u0010c\u001a\u0004\u0018\u00010&8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bd\u0010(R\u0011\u0010e\u001a\u00020&8F\u00a2\u0006\u0006\u001a\u0004\bf\u0010(R\u0011\u0010g\u001a\u00020h\u00a2\u0006\b\n\u0000\u001a\u0004\bi\u0010jR\u0011\u0010k\u001a\u00020H\u00a2\u0006\b\n\u0000\u001a\u0004\bl\u0010JR\u001e\u0010m\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030O0\u001a8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bn\u0010\u001dR\u001a\u0010o\u001a\u00020<X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bp\u0010>\"\u0004\bq\u0010@R\u0012\u0010r\u001a\u00020\u00148\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010s\u001a\u0004\u0018\u00010tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bu\u0010v\"\u0004\bw\u0010x\u00a8\u0006\u0088\u0001"}, d2={"Lnet/ccbluex/liquidbounce/features/module/Module;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "BreakName", "", "getBreakName", "()Z", "setBreakName", "(Z)V", "animation", "Lnet/ccbluex/liquidbounce/ui/client/clickgui/style/styles/flux/AnimationHelper;", "getAnimation", "()Lnet/ccbluex/liquidbounce/ui/client/clickgui/style/styles/flux/AnimationHelper;", "animationHelper", "getAnimationHelper", "array", "getArray", "setArray", "arrayY", "", "getArrayY", "()F", "setArrayY", "(F)V", "booleanValues", "", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getBooleanValues", "()Ljava/util/List;", "canEnable", "category", "Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "getCategory", "()Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "setCategory", "(Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;)V", "colorlessTagName", "", "getColorlessTagName", "()Ljava/lang/String;", "description", "getDescription", "setDescription", "(Ljava/lang/String;)V", "expanded", "getExpanded", "setExpanded", "fakename", "getFakename", "setFakename", "higt", "getHigt", "setHigt", "hoverOpacity", "getHoverOpacity", "setHoverOpacity", "hue", "getHue", "keyBind", "", "getKeyBind", "()I", "setKeyBind", "(I)V", "listValues", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getListValues", "localizedName", "getLocalizedName", "setLocalizedName", "moduleTranslate", "Lnet/ccbluex/liquidbounce/utils/Translate3;", "getModuleTranslate", "()Lnet/ccbluex/liquidbounce/utils/Translate3;", "name", "getName", "setName", "numberValues", "Lnet/ccbluex/liquidbounce/value/Value;", "getNumberValues", "openList", "showSettings", "slide", "getSlide", "setSlide", "slideStep", "getSlideStep", "setSlideStep", "spacedName", "getSpacedName", "setSpacedName", "splicedName", "getSplicedName", "setSplicedName", "value", "state", "getState", "setState", "tag", "getTag", "tagName", "getTagName", "translate", "Lnet/ccbluex/liquidbounce/utils/Translate;", "getTranslate", "()Lnet/ccbluex/liquidbounce/utils/Translate;", "valueTranslate", "getValueTranslate", "values", "getValues", "width", "getWidth", "setWidth", "yPos1", "yPosAnimation", "Lnet/ccbluex/liquidbounce/utils/render/Animation;", "getYPosAnimation", "()Lnet/ccbluex/liquidbounce/utils/render/Animation;", "setYPosAnimation", "(Lnet/ccbluex/liquidbounce/utils/render/Animation;)V", "NameModules22", "breakValue", "nameBreakValue", "getOpenList", "getValue", "valueName", "handleEvents", "onDisable", "", "onEnable", "onInitialize", "onToggle", "setOpenList", "b", "toggle", "KyinoClient"})
public abstract class Module
extends MinecraftInstance
implements Listenable {
    @NotNull
    private final Translate3 valueTranslate = new Translate3(0.0f, 0.0f);
    @NotNull
    private final Translate3 moduleTranslate = new Translate3(0.0f, 0.0f);
    @JvmField
    public boolean showSettings;
    @JvmField
    public float yPos1 = 30.0f;
    @NotNull
    private final AnimationHelper animationHelper;
    @NotNull
    private final AnimationHelper animation;
    @NotNull
    private String name;
    @NotNull
    private String spacedName;
    @NotNull
    private String description;
    @NotNull
    private String fakename;
    private boolean expanded;
    @NotNull
    private String localizedName = "";
    private float hoverOpacity;
    @NotNull
    private ModuleCategory category;
    @NotNull
    private final Translate translate = new Translate(0.0f, 0.0f);
    private int keyBind;
    private boolean array = true;
    private final boolean canEnable;
    @NotNull
    private String splicedName = "";
    private boolean openList;
    private float slideStep;
    private boolean state;
    private final float hue;
    private float slide;
    private float arrayY;
    private float higt;
    private boolean BreakName;
    @Nullable
    private Animation yPosAnimation;
    private int width;

    @NotNull
    public final Translate3 getValueTranslate() {
        return this.valueTranslate;
    }

    @NotNull
    public final Translate3 getModuleTranslate() {
        return this.moduleTranslate;
    }

    @NotNull
    public final AnimationHelper getAnimationHelper() {
        return this.animationHelper;
    }

    @NotNull
    public final AnimationHelper getAnimation() {
        return this.animation;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final void setName(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        this.name = string;
    }

    @NotNull
    public final String getSpacedName() {
        return this.spacedName;
    }

    public final void setSpacedName(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        this.spacedName = string;
    }

    @NotNull
    public final String getDescription() {
        return this.description;
    }

    public final void setDescription(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        this.description = string;
    }

    @NotNull
    public final String getFakename() {
        return this.fakename;
    }

    public final void setFakename(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        this.fakename = string;
    }

    public final boolean getExpanded() {
        return this.expanded;
    }

    public final void setExpanded(boolean bl) {
        this.expanded = bl;
    }

    @NotNull
    public final String getLocalizedName() {
        CharSequence charSequence;
        CharSequence charSequence2 = this.localizedName;
        boolean bl = false;
        CharSequence charSequence3 = charSequence2;
        boolean bl2 = false;
        if (charSequence3.length() == 0) {
            boolean bl3 = false;
            charSequence = this.name;
        } else {
            charSequence = charSequence2;
        }
        return (String)charSequence;
    }

    public final void setLocalizedName(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        this.localizedName = string;
    }

    public final float getHoverOpacity() {
        return this.hoverOpacity;
    }

    public final void setHoverOpacity(float f) {
        this.hoverOpacity = f;
    }

    @NotNull
    public final ModuleCategory getCategory() {
        return this.category;
    }

    public final void setCategory(@NotNull ModuleCategory moduleCategory) {
        Intrinsics.checkParameterIsNotNull((Object)moduleCategory, "<set-?>");
        this.category = moduleCategory;
    }

    @NotNull
    public final Translate getTranslate() {
        return this.translate;
    }

    public final int getKeyBind() {
        return this.keyBind;
    }

    public final void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
        if (!LiquidBounce.INSTANCE.isStarting()) {
            LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().modulesConfig);
        }
    }

    public final boolean getArray() {
        return this.array;
    }

    public final void setArray(boolean array) {
        this.array = array;
        if (!LiquidBounce.INSTANCE.isStarting()) {
            LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().modulesConfig);
        }
    }

    @NotNull
    public final String getSplicedName() {
        return this.splicedName;
    }

    public final void setSplicedName(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
        this.splicedName = string;
    }

    public final float getSlideStep() {
        return this.slideStep;
    }

    public final void setSlideStep(float f) {
        this.slideStep = f;
    }

    public final boolean getState() {
        return this.state;
    }

    public final void setState(boolean value) {
        if (this.state == value) {
            return;
        }
        this.onToggle(value);
        if (!((Boolean)HUD.notify.get()).booleanValue() && !LiquidBounce.INSTANCE.isStarting()) {
            if (value) {
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification(this.name + " Enabled", Notification.Type.SUCCESS));
            } else {
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification(this.name + " Disabled", Notification.Type.ERROR));
            }
        }
        if (!((Boolean)HUD.notify233.get()).booleanValue() && !LiquidBounce.INSTANCE.isStarting()) {
            Minecraft minecraft = Module.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("random.click"), (float)1.0f));
        }
        if (value) {
            this.onEnable();
            if (this.canEnable) {
                this.state = true;
            }
        } else {
            this.onDisable();
            this.state = false;
        }
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().modulesConfig);
    }

    public final float getHue() {
        return this.hue;
    }

    public final float getSlide() {
        return this.slide;
    }

    public final void setSlide(float f) {
        this.slide = f;
    }

    public final float getArrayY() {
        return this.arrayY;
    }

    public final void setArrayY(float f) {
        this.arrayY = f;
    }

    public final float getHigt() {
        return this.higt;
    }

    public final void setHigt(float f) {
        this.higt = f;
    }

    public final boolean getBreakName() {
        return this.BreakName;
    }

    public final void setBreakName(boolean bl) {
        this.BreakName = bl;
    }

    @Nullable
    public final Animation getYPosAnimation() {
        return this.yPosAnimation;
    }

    public final void setYPosAnimation(@Nullable Animation animation) {
        this.yPosAnimation = animation;
    }

    @Nullable
    public String getTag() {
        return null;
    }

    @NotNull
    public final String tagName(boolean nameBreakValue) {
        return this.getBreakName(nameBreakValue) + (this.getTag() == null ? "" : " \u00a77" + this.getTag());
    }

    @NotNull
    public final String colorlessTagName(boolean nameBreakValue) {
        return this.getBreakName(nameBreakValue) + (this.getTag() == null ? "" : " " + ColorUtils.stripColor(this.getTag()));
    }

    @NotNull
    public final String getTagName() {
        return this.fakename + (this.getTag() == null ? "" : "\u00a77 " + this.getTag());
    }

    @NotNull
    public final String getColorlessTagName() {
        return this.fakename + (this.getTag() == null ? "" : "\u00a77 " + ColorUtils.stripColor(this.getTag()));
    }

    public final int getWidth() {
        return this.width;
    }

    public final void setWidth(int n) {
        this.width = n;
    }

    public final void toggle() {
        this.setState(!this.state);
    }

    public final boolean getOpenList() {
        return this.openList;
    }

    public final void setOpenList(boolean b) {
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final String getBreakName(boolean breakValue) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!breakValue) {
            return this.name;
        }
        if (Intrinsics.areEqual(this.name, "AAC5Disabler")) {
            return "AAC5 Disabler";
        }
        if (Intrinsics.areEqual(this.name, "2DNameTags")) {
            return "2D NameTags";
        }
        if (Intrinsics.areEqual(this.name, "PlayerESP")) {
            return "Player ESP";
        }
        if (Intrinsics.areEqual(this.name, "AuraAAC")) {
            return "Aura AAC";
        }
        int n = 0;
        int n2 = ((CharSequence)this.name).length();
        while (n < n2) {
            void i;
            if (i + 2 < this.name.length() && i > true && Character.isUpperCase(this.name.charAt((int)(i + 2))) && Character.isUpperCase(this.name.charAt((int)i))) {
                stringBuilder.append("" + ' ' + this.name.charAt((int)i));
            } else if (i + true < this.name.length()) {
                if (!Character.isUpperCase(this.name.charAt((int)(i + true)))) {
                    stringBuilder.append(Character.isUpperCase(this.name.charAt((int)i)) && i > 0 ? "" + ' ' + this.name.charAt((int)i) : Character.valueOf(this.name.charAt((int)i)));
                } else {
                    stringBuilder.append(this.name.charAt((int)i));
                }
            } else {
                stringBuilder.append(this.name.charAt((int)i));
            }
            ++i;
        }
        String string = stringBuilder.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "stringBuilder.toString()");
        return string;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final String NameModules22(boolean breakValue) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!breakValue) {
            return this.name;
        }
        if (Intrinsics.areEqual(this.name, "AAC5Disabler")) {
            return "AAC5 Disabler";
        }
        if (Intrinsics.areEqual(this.name, "2DNameTags")) {
            return "2D NameTags";
        }
        if (Intrinsics.areEqual(this.name, "PlayerESP")) {
            return "Player ESP";
        }
        int n = 0;
        int n2 = ((CharSequence)this.name).length();
        while (n < n2) {
            void i;
            if (i + 2 < this.name.length() && i > true && Character.isUpperCase(this.name.charAt((int)(i + 2))) && Character.isUpperCase(this.name.charAt((int)i))) {
                stringBuilder.append("" + ' ' + this.name.charAt((int)i));
            } else if (i + true < this.name.length()) {
                if (!Character.isUpperCase(this.name.charAt((int)(i + true)))) {
                    stringBuilder.append(Character.isUpperCase(this.name.charAt((int)i)) && i > 0 ? "" + ' ' + this.name.charAt((int)i) : Character.valueOf(this.name.charAt((int)i)));
                } else {
                    stringBuilder.append(this.name.charAt((int)i));
                }
            } else {
                stringBuilder.append(this.name.charAt((int)i));
            }
            ++i;
        }
        String string = stringBuilder.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "stringBuilder.toString()");
        return string;
    }

    public void onToggle(boolean state) {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    @Nullable
    public Value<?> getValue(@NotNull String valueName) {
        Object v0;
        block1: {
            Intrinsics.checkParameterIsNotNull(valueName, "valueName");
            Iterable iterable = this.getValues();
            boolean bl = false;
            Iterable iterable2 = iterable;
            boolean bl2 = false;
            for (Object t : iterable2) {
                Value it = (Value)t;
                boolean bl3 = false;
                if (!StringsKt.equals(it.getName(), valueName, true)) continue;
                v0 = t;
                break block1;
            }
            v0 = null;
        }
        return v0;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final List<Value<?>> getNumberValues() {
        void $this$filterTo$iv$iv;
        Iterable $this$filter$iv = this.getValues();
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Value it = (Value)element$iv$iv;
            boolean bl = false;
            if (!(it instanceof IntegerValue || it instanceof FloatValue)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final List<BoolValue> getBooleanValues() {
        void $this$filterIsInstanceTo$iv$iv;
        Iterable $this$filterIsInstance$iv = this.getValues();
        boolean $i$f$filterIsInstance = false;
        Iterable iterable = $this$filterIsInstance$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterIsInstanceTo = false;
        for (Object element$iv$iv : $this$filterIsInstanceTo$iv$iv) {
            if (!(element$iv$iv instanceof BoolValue)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final List<ListValue> getListValues() {
        void $this$filterIsInstanceTo$iv$iv;
        Iterable $this$filterIsInstance$iv = this.getValues();
        boolean $i$f$filterIsInstance = false;
        Iterable iterable = $this$filterIsInstance$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterIsInstanceTo = false;
        for (Object element$iv$iv : $this$filterIsInstanceTo$iv$iv) {
            if (!(element$iv$iv instanceof ListValue)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public List<Value<?>> getValues() {
        void $this$filterIsInstanceTo$iv$iv;
        Iterable $this$mapTo$iv$iv;
        Field[] fieldArray = this.getClass().getDeclaredFields();
        Intrinsics.checkExpressionValueIsNotNull(fieldArray, "javaClass.declaredFields");
        Field[] $this$map$iv = fieldArray;
        boolean $i$f$map = false;
        Field[] fieldArray2 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList($this$map$iv.length);
        boolean $i$f$mapTo = false;
        Iterator iterator2 = $this$mapTo$iv$iv;
        int n = ((void)iterator2).length;
        for (int i = 0; i < n; ++i) {
            void valueField;
            void item$iv$iv;
            void var10_11 = item$iv$iv = iterator2[i];
            Collection collection = destination$iv$iv;
            boolean bl = false;
            void v1 = valueField;
            Intrinsics.checkExpressionValueIsNotNull(v1, "valueField");
            v1.setAccessible(true);
            Object object = valueField.get(this);
            collection.add(object);
        }
        Iterable $this$filterIsInstance$iv = (List)destination$iv$iv;
        boolean $i$f$filterIsInstance = false;
        $this$mapTo$iv$iv = $this$filterIsInstance$iv;
        destination$iv$iv = new ArrayList();
        boolean $i$f$filterIsInstanceTo = false;
        for (Object element$iv$iv : $this$filterIsInstanceTo$iv$iv) {
            if (!(element$iv$iv instanceof Value)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    @Override
    public boolean handleEvents() {
        return this.state;
    }

    public void onInitialize() {
    }

    public Module() {
        this.animation = new AnimationHelper(this);
        ModuleInfo moduleInfo = this.getClass().getAnnotation(ModuleInfo.class);
        if (moduleInfo == null) {
            Intrinsics.throwNpe();
        }
        ModuleInfo moduleInfo2 = moduleInfo;
        CharSequence charSequence = moduleInfo2.fakeName();
        Module module = this;
        boolean bl = false;
        boolean bl2 = charSequence.length() == 0;
        module.fakename = bl2 || this instanceof ScriptModule ? moduleInfo2.name() : moduleInfo2.fakeName();
        this.name = moduleInfo2.name();
        this.description = moduleInfo2.description();
        this.category = moduleInfo2.category();
        this.spacedName = Intrinsics.areEqual(moduleInfo2.spacedName(), "") ? this.name : moduleInfo2.spacedName();
        this.setKeyBind(moduleInfo2.keyBind());
        this.setArray(moduleInfo2.array());
        this.canEnable = moduleInfo2.canEnable();
        this.animationHelper = new AnimationHelper(this);
        this.hue = (float)Math.random();
        this.width = 10;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

