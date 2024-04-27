/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.potion.Potion
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0017\u001a\u00020\u00062\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\fR\u0014\u0010\u000f\u001a\u00020\u00108VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006\u001a"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/PotionImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "wrapped", "Lnet/minecraft/potion/Potion;", "(Lnet/minecraft/potion/Potion;)V", "hasStatusIcon", "", "getHasStatusIcon", "()Z", "id", "", "getId", "()I", "liquidColor", "getLiquidColor", "name", "", "getName", "()Ljava/lang/String;", "statusIconIndex", "getStatusIconIndex", "getWrapped", "()Lnet/minecraft/potion/Potion;", "equals", "other", "", "LiKingSense"})
public final class PotionImpl
implements IPotion {
    @NotNull
    private final Potion wrapped;

    @Override
    public int getLiquidColor() {
        return this.wrapped.func_76401_j();
    }

    @Override
    public int getId() {
        return Potion.func_188409_a((Potion)this.wrapped);
    }

    @Override
    @NotNull
    public String getName() {
        String string = this.wrapped.func_76393_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.name");
        return string;
    }

    @Override
    public boolean getHasStatusIcon() {
        return this.wrapped.func_76400_d();
    }

    @Override
    public int getStatusIconIndex() {
        return this.wrapped.func_76392_e();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof PotionImpl && Intrinsics.areEqual((Object)((PotionImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Potion getWrapped() {
        return this.wrapped;
    }

    public PotionImpl(@NotNull Potion wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

