/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.potion.Potion
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.Nullable;

public final class PotionImpl
implements IPotion {
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
    public String getName() {
        return this.wrapped.func_76393_a();
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
        return other instanceof PotionImpl && ((PotionImpl)other).wrapped.equals(this.wrapped);
    }

    public final Potion getWrapped() {
        return this.wrapped;
    }

    public PotionImpl(Potion wrapped) {
        this.wrapped = wrapped;
    }
}

