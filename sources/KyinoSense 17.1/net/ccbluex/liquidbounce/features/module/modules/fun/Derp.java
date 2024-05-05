/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Derp", description="Makes it look like you were derping around.", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/fun/Derp;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "currentSpin", "", "headlessValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "incrementValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rotation", "", "getRotation", "()[F", "spinnyValue", "KyinoClient"})
public final class Derp
extends Module {
    private final BoolValue headlessValue = new BoolValue("Headless", false);
    private final BoolValue spinnyValue = new BoolValue("Spinny", false);
    private final FloatValue incrementValue = new FloatValue("Increment", 1.0f, 0.0f, 50.0f);
    private float currentSpin;

    @NotNull
    public final float[] getRotation() {
        float[] derpRotations = new float[]{Derp.access$getMc$p$s1046033730().field_71439_g.field_70177_z + (float)(Math.random() * (double)360 - (double)180), (float)(Math.random() * (double)180 - (double)90)};
        if (((Boolean)this.headlessValue.get()).booleanValue()) {
            derpRotations[1] = 180.0f;
        }
        if (((Boolean)this.spinnyValue.get()).booleanValue()) {
            derpRotations[0] = this.currentSpin + ((Number)this.incrementValue.get()).floatValue();
            this.currentSpin = derpRotations[0];
        }
        return derpRotations;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

