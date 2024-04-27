/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.potion.Potion
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.injection.backend.PotionImpl;
import net.minecraft.potion.Potion;

public final class PotionImplKt {
    public static final Potion unwrap(IPotion iPotion) {
        boolean bl = false;
        return ((PotionImpl)iPotion).getWrapped();
    }

    public static final IPotion wrap(Potion potion) {
        boolean bl = false;
        return new PotionImpl(potion);
    }
}

