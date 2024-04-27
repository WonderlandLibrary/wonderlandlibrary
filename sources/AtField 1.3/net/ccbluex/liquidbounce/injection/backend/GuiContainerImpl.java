/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiContainer;
import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.api.minecraft.inventory.ISlot;
import net.ccbluex.liquidbounce.injection.backend.ContainerImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiScreenImpl;
import net.ccbluex.liquidbounce.injection.backend.SlotImpl;
import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class GuiContainerImpl
extends GuiScreenImpl
implements IGuiContainer {
    public GuiContainerImpl(GuiContainer guiContainer) {
        super((GuiScreen)guiContainer);
    }

    @Override
    public IContainer getInventorySlots() {
        IContainer iContainer;
        Container container = ((GuiContainer)this.getWrapped()).field_147002_h;
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
    public void handleMouseClick(ISlot iSlot, int n, int n2, int n3) {
        ClickType clickType;
        Gui gui = this.getWrapped();
        if (gui == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer");
        }
        ISlot iSlot2 = iSlot;
        IMixinGuiContainer iMixinGuiContainer = (IMixinGuiContainer)gui;
        boolean bl = false;
        Slot slot = ((SlotImpl)iSlot2).getWrapped();
        int n4 = n3;
        int n5 = n2;
        int n6 = n;
        bl = false;
        switch (n4) {
            case 0: {
                clickType = ClickType.PICKUP;
                break;
            }
            case 1: {
                clickType = ClickType.QUICK_MOVE;
                break;
            }
            case 2: {
                clickType = ClickType.SWAP;
                break;
            }
            case 3: {
                clickType = ClickType.CLONE;
                break;
            }
            case 4: {
                clickType = ClickType.THROW;
                break;
            }
            case 5: {
                clickType = ClickType.QUICK_CRAFT;
                break;
            }
            case 6: {
                clickType = ClickType.PICKUP_ALL;
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Invalid mode " + n4);
            }
        }
        ClickType clickType2 = clickType;
        iMixinGuiContainer.publicHandleMouseClick(slot, n6, n5, clickType2);
    }
}

