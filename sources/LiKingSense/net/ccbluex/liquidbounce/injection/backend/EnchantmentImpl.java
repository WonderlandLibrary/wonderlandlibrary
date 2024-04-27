/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.enchantment.Enchantment
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.minecraft.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0006H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/EnchantmentImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/enchantments/IEnchantment;", "wrapped", "Lnet/minecraft/enchantment/Enchantment;", "(Lnet/minecraft/enchantment/Enchantment;)V", "effectId", "", "getEffectId", "()I", "getWrapped", "()Lnet/minecraft/enchantment/Enchantment;", "getTranslatedName", "", "level", "LiKingSense"})
public final class EnchantmentImpl
implements IEnchantment {
    @NotNull
    private final Enchantment wrapped;

    @Override
    public int getEffectId() {
        return Enchantment.func_185258_b((Enchantment)this.wrapped);
    }

    @Override
    @NotNull
    public String getTranslatedName(int level) {
        String string = this.wrapped.func_77316_c(level);
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.getTranslatedName(level)");
        return string;
    }

    @NotNull
    public final Enchantment getWrapped() {
        return this.wrapped;
    }

    public EnchantmentImpl(@NotNull Enchantment wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

