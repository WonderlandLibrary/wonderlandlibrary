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

public final class GuiChestImpl
extends GuiContainerImpl
implements IGuiChest {
    @Override
    public int getInventoryRows() {
        return ((GuiChest)this.getWrapped()).field_147018_x;
    }

    public GuiChestImpl(GuiChest guiChest) {
        super((GuiContainer)guiChest);
    }

    @Override
    public IContainer getInventorySlots() {
        IContainer iContainer;
        Container container = ((GuiChest)this.getWrapped()).field_147002_h;
        if (container != null) {
            Container container2 = container;
            boolean bl = false;
            iContainer = new ContainerImpl(container2);
        } else {
            iContainer = null;
        }
        return iContainer;
    }

    @Override
    public IIInventory getLowerChestInventory() {
        IIInventory iIInventory;
        IInventory iInventory = ((GuiChest)this.getWrapped()).field_147015_w;
        if (iInventory != null) {
            IInventory iInventory2 = iInventory;
            boolean bl = false;
            iIInventory = new IInventoryImpl(iInventory2);
        } else {
            iIInventory = null;
        }
        return iIInventory;
    }
}

