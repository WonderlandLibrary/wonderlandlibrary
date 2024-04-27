/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.render;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import org.jetbrains.annotations.NotNull;

@Info(name="Chams", description="Allows you to see targets through blocks.", category=Category.RENDER, cnName="\u7a7f\u5899\u900f\u89c6")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0015\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\nR\u0011\u0010\u0017\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0006R\u0011\u0010\u0019\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u0011\u0010\u001b\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0011\u0010\u001d\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0006R\u0011\u0010\u001f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0006R\u0011\u0010!\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0010R\u0011\u0010#\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0014R\u0011\u0010%\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0014\u00a8\u0006'"}, d2={"Lnet/dev/important/modules/module/modules/render/Chams;", "Lnet/dev/important/modules/module/Module;", "()V", "alphaValue", "Lnet/dev/important/value/IntegerValue;", "getAlphaValue", "()Lnet/dev/important/value/IntegerValue;", "behindColorModeValue", "Lnet/dev/important/value/ListValue;", "getBehindColorModeValue", "()Lnet/dev/important/value/ListValue;", "blueValue", "getBlueValue", "brightnessValue", "Lnet/dev/important/value/FloatValue;", "getBrightnessValue", "()Lnet/dev/important/value/FloatValue;", "chestsValue", "Lnet/dev/important/value/BoolValue;", "getChestsValue", "()Lnet/dev/important/value/BoolValue;", "colorModeValue", "getColorModeValue", "greenValue", "getGreenValue", "itemsValue", "getItemsValue", "legacyMode", "getLegacyMode", "mixerSecondsValue", "getMixerSecondsValue", "redValue", "getRedValue", "saturationValue", "getSaturationValue", "targetsValue", "getTargetsValue", "texturedValue", "getTexturedValue", "LiquidBounce"})
public final class Chams
extends Module {
    @NotNull
    private final BoolValue targetsValue = new BoolValue("Targets", true);
    @NotNull
    private final BoolValue chestsValue = new BoolValue("Chests", true);
    @NotNull
    private final BoolValue itemsValue = new BoolValue("Items", true);
    @NotNull
    private final BoolValue legacyMode = new BoolValue("Legacy-Mode", false);
    @NotNull
    private final BoolValue texturedValue = new BoolValue("Textured", true, new Function0<Boolean>(this){
        final /* synthetic */ Chams this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)this.this$0.getLegacyMode().get() == false;
        }
    });
    @NotNull
    private final ListValue colorModeValue;
    @NotNull
    private final ListValue behindColorModeValue;
    @NotNull
    private final IntegerValue redValue;
    @NotNull
    private final IntegerValue greenValue;
    @NotNull
    private final IntegerValue blueValue;
    @NotNull
    private final IntegerValue alphaValue;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue mixerSecondsValue;

    public Chams() {
        String[] stringArray = new String[]{"Custom", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Mixer"};
        this.colorModeValue = new ListValue("Color", stringArray, "Custom", new Function0<Boolean>(this){
            final /* synthetic */ Chams this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getLegacyMode().get() == false;
            }
        });
        stringArray = new String[]{"Same", "Opposite", "Red"};
        this.behindColorModeValue = new ListValue("Behind-Color", stringArray, "Same", new Function0<Boolean>(this){
            final /* synthetic */ Chams this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getLegacyMode().get() == false;
            }
        });
        this.redValue = new IntegerValue("Red", 255, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ Chams this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getLegacyMode().get() == false;
            }
        });
        this.greenValue = new IntegerValue("Green", 255, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ Chams this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getLegacyMode().get() == false;
            }
        });
        this.blueValue = new IntegerValue("Blue", 255, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ Chams this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getLegacyMode().get() == false;
            }
        });
        this.alphaValue = new IntegerValue("Alpha", 255, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ Chams this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getLegacyMode().get() == false;
            }
        });
        this.saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f, new Function0<Boolean>(this){
            final /* synthetic */ Chams this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getLegacyMode().get() == false;
            }
        });
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f, new Function0<Boolean>(this){
            final /* synthetic */ Chams this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getLegacyMode().get() == false;
            }
        });
        this.mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10, new Function0<Boolean>(this){
            final /* synthetic */ Chams this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getLegacyMode().get() == false;
            }
        });
    }

    @NotNull
    public final BoolValue getTargetsValue() {
        return this.targetsValue;
    }

    @NotNull
    public final BoolValue getChestsValue() {
        return this.chestsValue;
    }

    @NotNull
    public final BoolValue getItemsValue() {
        return this.itemsValue;
    }

    @NotNull
    public final BoolValue getLegacyMode() {
        return this.legacyMode;
    }

    @NotNull
    public final BoolValue getTexturedValue() {
        return this.texturedValue;
    }

    @NotNull
    public final ListValue getColorModeValue() {
        return this.colorModeValue;
    }

    @NotNull
    public final ListValue getBehindColorModeValue() {
        return this.behindColorModeValue;
    }

    @NotNull
    public final IntegerValue getRedValue() {
        return this.redValue;
    }

    @NotNull
    public final IntegerValue getGreenValue() {
        return this.greenValue;
    }

    @NotNull
    public final IntegerValue getBlueValue() {
        return this.blueValue;
    }

    @NotNull
    public final IntegerValue getAlphaValue() {
        return this.alphaValue;
    }

    @NotNull
    public final FloatValue getSaturationValue() {
        return this.saturationValue;
    }

    @NotNull
    public final FloatValue getBrightnessValue() {
        return this.brightnessValue;
    }

    @NotNull
    public final IntegerValue getMixerSecondsValue() {
        return this.mixerSecondsValue;
    }
}

