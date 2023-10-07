/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoFOV", description="Disables FOV changes caused by speed effect, etc.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/NoFOV;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "fovValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getFovValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "KyinoClient"})
public final class NoFOV
extends Module {
    @NotNull
    private final FloatValue fovValue = new FloatValue("FOV", 1.0f, 0.0f, 1.5f);

    @NotNull
    public final FloatValue getFovValue() {
        return this.fovValue;
    }
}

