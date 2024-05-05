/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.Container
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.injection.backend.ContainerImpl;
import net.minecraft.inventory.Container;

public final class ContainerImplKt {
    public static final Container unwrap(IContainer $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ContainerImpl)$this$unwrap).getWrapped();
    }

    public static final IContainer wrap(Container $this$wrap) {
        int $i$f$wrap = 0;
        return new ContainerImpl($this$wrap);
    }
}

