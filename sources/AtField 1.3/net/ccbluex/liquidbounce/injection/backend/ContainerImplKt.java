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
    public static final IContainer wrap(Container container) {
        boolean bl = false;
        return new ContainerImpl(container);
    }

    public static final Container unwrap(IContainer iContainer) {
        boolean bl = false;
        return ((ContainerImpl)iContainer).getWrapped();
    }
}

