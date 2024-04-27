/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.IInventory
 */
package net.ccbluex.liquidbounce.injection.backend;

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

public final class GuiChestImpl<T extends GuiChest>
extends GuiContainerImpl<T>
implements IGuiChest {
    @Override
    public int getInventoryRows() {
        return ((GuiChest)this.getWrapped()).field_147018_x;
    }

    @Override
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

    public GuiChestImpl(T wrapped) {
        super((GuiContainer)wrapped);
    }
}

