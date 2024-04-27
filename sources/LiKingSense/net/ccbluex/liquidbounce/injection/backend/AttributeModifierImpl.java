/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.entity.ai.attributes.IAttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/AttributeModifierImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/ai/attributes/IAttributeModifier;", "wrapped", "Lnet/minecraft/entity/ai/attributes/AttributeModifier;", "(Lnet/minecraft/entity/ai/attributes/AttributeModifier;)V", "amount", "", "getAmount", "()D", "getWrapped", "()Lnet/minecraft/entity/ai/attributes/AttributeModifier;", "LiKingSense"})
public final class AttributeModifierImpl
implements IAttributeModifier {
    @NotNull
    private final AttributeModifier wrapped;

    @Override
    public double getAmount() {
        return this.wrapped.func_111164_d();
    }

    @NotNull
    public final AttributeModifier getWrapped() {
        return this.wrapped;
    }

    public AttributeModifierImpl(@NotNull AttributeModifier wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

