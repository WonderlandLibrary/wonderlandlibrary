/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.world;

import kotlin.Metadata;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.IntegerValue;
import org.jetbrains.annotations.NotNull;

@Info(name="FastPlace", spacedName="Fast Place", description="Allows you to place blocks faster.", category=Category.WORLD, cnName="\u5feb\u901f\u653e\u7f6e")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/world/FastPlace;", "Lnet/dev/important/modules/module/Module;", "()V", "speedValue", "Lnet/dev/important/value/IntegerValue;", "getSpeedValue", "()Lnet/dev/important/value/IntegerValue;", "LiquidBounce"})
public final class FastPlace
extends Module {
    @NotNull
    private final IntegerValue speedValue = new IntegerValue("Speed", 0, 0, 4);

    @NotNull
    public final IntegerValue getSpeedValue() {
        return this.speedValue;
    }
}

