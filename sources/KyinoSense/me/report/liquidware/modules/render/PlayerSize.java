/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="PlayerSize", description="PlayerSize", category=ModuleCategory.RENDER, array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lme/report/liquidware/modules/render/PlayerSize;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "playerSizeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getPlayerSizeValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "KyinoClient"})
public final class PlayerSize
extends Module {
    @NotNull
    private final FloatValue playerSizeValue = new FloatValue("PlayerSize", 0.5f, 0.01f, 5.0f);

    @NotNull
    public final FloatValue getPlayerSizeValue() {
        return this.playerSizeValue;
    }
}

