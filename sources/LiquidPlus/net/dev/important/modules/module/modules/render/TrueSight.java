/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.render;

import kotlin.Metadata;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@Info(name="TrueSight", spacedName="True Sight", description="Allows you to see invisible entities and barriers.", category=Category.RENDER, cnName="\u65e0\u5f62\u969c\u788d")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/module/modules/render/TrueSight;", "Lnet/dev/important/modules/module/Module;", "()V", "barriersValue", "Lnet/dev/important/value/BoolValue;", "getBarriersValue", "()Lnet/dev/important/value/BoolValue;", "entitiesValue", "getEntitiesValue", "LiquidBounce"})
public final class TrueSight
extends Module {
    @NotNull
    private final BoolValue barriersValue = new BoolValue("Barriers", true);
    @NotNull
    private final BoolValue entitiesValue = new BoolValue("Entities", true);

    @NotNull
    public final BoolValue getBarriersValue() {
        return this.barriersValue;
    }

    @NotNull
    public final BoolValue getEntitiesValue() {
        return this.entitiesValue;
    }
}

