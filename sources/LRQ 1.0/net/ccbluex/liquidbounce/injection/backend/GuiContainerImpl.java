/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class GuiContainerImpl<T extends GuiContainer>
extends GuiScreenImpl<T>
implements IGuiContainer {
    /*
     * WARNING - void declaration
     */
    @Override
    public void handleMouseClick(ISlot slot, int slotNumber, int clickedButton, int clickType) {
        ClickType clickType2;
        void $this$toClickType$iv;
        void $this$unwrap$iv22;
        Object t = this.getWrapped();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer");
        }
        ISlot iSlot = slot;
        IMixinGuiContainer iMixinGuiContainer = (IMixinGuiContainer)t;
        boolean $i$f$unwrap = false;
        Slot slot2 = ((SlotImpl)$this$unwrap$iv22).getWrapped();
        int $this$unwrap$iv22 = clickType;
        int n = clickedButton;
        int n2 = slotNumber;
        boolean $i$f$toClickType = false;
        switch ($this$toClickType$iv) {
            case 0: {
                clickType2 = ClickType.PICKUP;
                break;
            }
            case 1: {
                clickType2 = ClickType.QUICK_MOVE;
                break;
            }
            case 2: {
                clickType2 = ClickType.SWAP;
                break;
            }
            case 3: {
                clickType2 = ClickType.CLONE;
                break;
            }
            case 4: {
                clickType2 = ClickType.THROW;
                break;
            }
            case 5: {
                clickType2 = ClickType.QUICK_CRAFT;
                break;
            }
            case 6: {
                clickType2 = ClickType.PICKUP_ALL;
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Invalid mode " + (int)$this$toClickType$iv);
            }
        }
        ClickType clickType3 = clickType2;
        iMixinGuiContainer.publicHandleMouseClick(slot2, n2, n, clickType3);
    }

    @Override
    public IContainer getInventorySlots() {
        IContainer iContainer;
        Container container = ((GuiContainer)this.getWrapped()).field_147002_h;
        if (container != null) {
            Container $this$wrap$iv = container;
            boolean $i$f$wrap = false;
            iContainer = new ContainerImpl($this$wrap$iv);
        } else {
            iContainer = null;
        }
        return iContainer;
    }

    public GuiContainerImpl(T wrapped) {
        super((GuiScreen)wrapped);
    }
}

