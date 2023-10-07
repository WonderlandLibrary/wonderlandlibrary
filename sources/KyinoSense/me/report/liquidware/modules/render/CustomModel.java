/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="CustomModel", spacedName="Custom Model", category=ModuleCategory.RENDER, description="Changes your player's model.", array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f\u00a8\u0006\r"}, d2={"Lme/report/liquidware/modules/render/CustomModel;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "mode2", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getMode2", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "onlySelf", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getOnlySelf", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "setOnlySelf", "(Lnet/ccbluex/liquidbounce/value/BoolValue;)V", "KyinoClient"})
public final class CustomModel
extends Module {
    @NotNull
    private final ListValue mode2 = new ListValue("Mode", new String[]{"Rabbit", "Freddy", "No"}, "Rabbit");
    @NotNull
    private BoolValue onlySelf = new BoolValue("OnlySelf", false);

    @NotNull
    public final ListValue getMode2() {
        return this.mode2;
    }

    @NotNull
    public final BoolValue getOnlySelf() {
        return this.onlySelf;
    }

    public final void setOnlySelf(@NotNull BoolValue boolValue) {
        Intrinsics.checkParameterIsNotNull(boolValue, "<set-?>");
        this.onlySelf = boolValue;
    }
}

