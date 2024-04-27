/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.combat;

import kotlin.Metadata;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@Info(name="HitBox", spacedName="Hit Box", description="Makes hitboxes of targets bigger.", category=Category.COMBAT, cnName="\u78b0\u649e\u7bb1")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/combat/HitBox;", "Lnet/dev/important/modules/module/Module;", "()V", "sizeValue", "Lnet/dev/important/value/FloatValue;", "getSizeValue", "()Lnet/dev/important/value/FloatValue;", "LiquidBounce"})
public final class HitBox
extends Module {
    @NotNull
    private final FloatValue sizeValue = new FloatValue("Size", 0.4f, 0.0f, 1.0f);

    @NotNull
    public final FloatValue getSizeValue() {
        return this.sizeValue;
    }
}

