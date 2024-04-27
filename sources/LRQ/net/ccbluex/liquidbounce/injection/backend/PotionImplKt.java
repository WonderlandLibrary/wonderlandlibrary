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
    public static final Potion unwrap(IPotion $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((PotionImpl)$this$unwrap).getWrapped();
    }

    public static final IPotion wrap(Potion $this$wrap) {
        int $i$f$wrap = 0;
        return new PotionImpl($this$wrap);
    }
}

