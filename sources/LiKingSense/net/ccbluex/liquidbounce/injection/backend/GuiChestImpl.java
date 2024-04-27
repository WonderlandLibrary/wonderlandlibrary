/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.IInventory
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiChest;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IIInventory;
import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.injection.backend.ContainerImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiContainerImpl;
import net.ccbluex.liquidbounce.injection.backend.IInventoryImpl;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0016\u0010\u000b\u001a\u0004\u0018\u00010\f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u000f\u001a\u0004\u0018\u00010\u00108VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/GuiChestImpl;", "T", "Lnet/minecraft/client/gui/inventory/GuiChest;", "Lnet/ccbluex/liquidbounce/injection/backend/GuiContainerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IGuiChest;", "wrapped", "(Lnet/minecraft/client/gui/inventory/GuiChest;)V", "inventoryRows", "", "getInventoryRows", "()I", "inventorySlots", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "getInventorySlots", "()Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "lowerChestInventory", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IIInventory;", "getLowerChestInventory", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IIInventory;", "LiKingSense"})
public final class GuiChestImpl<T extends GuiChest>
extends GuiContainerImpl<T>
implements IGuiChest {
    @Override
    public int getInventoryRows() {
        return ((GuiChest)this.getWrapped()).field_147018_x;
    }

    @Override
    @Nullable
    public IIInventory getLowerChestInventory() {
        IIInventory iIInventory;
        IInventory iInventory = ((GuiChest)this.getWrapped()).field_147015_w;
        if (iInventory != null) {
            IInventory $this$wrap$iv = iInventory;
            boolean $i$f$wrap = false;
            iIInventory = new IInventoryImpl($this$wrap$iv);
        } else {
            iIInventory = null;
        }
        return iIInventory;
    }

    @Override
    @Nullable
    public IContainer getInventorySlots() {
        IContainer iContainer;
        Container container = ((GuiChest)this.getWrapped()).field_147002_h;
        if (container != null) {
            Container $this$wrap$iv = container;
            boolean $i$f$wrap = false;
            iContainer = new ContainerImpl($this$wrap$iv);
        } else {
            iContainer = null;
        }
        return iContainer;
    }

    public GuiChestImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((GuiContainer)wrapped);
    }
}

