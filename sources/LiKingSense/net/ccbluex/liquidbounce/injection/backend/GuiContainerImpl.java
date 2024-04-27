/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006J(\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010H\u0016R\u0016\u0010\u0007\u001a\u0004\u0018\u00010\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/GuiContainerImpl;", "T", "Lnet/minecraft/client/gui/inventory/GuiContainer;", "Lnet/ccbluex/liquidbounce/injection/backend/GuiScreenImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IGuiContainer;", "wrapped", "(Lnet/minecraft/client/gui/inventory/GuiContainer;)V", "inventorySlots", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "getInventorySlots", "()Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "handleMouseClick", "", "slot", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/ISlot;", "slotNumber", "", "clickedButton", "clickType", "LiKingSense"})
public class GuiContainerImpl<T extends GuiContainer>
extends GuiScreenImpl<T>
implements IGuiContainer {
    /*
     * WARNING - void declaration
     */
    @Override
    public void handleMouseClick(@NotNull ISlot slot, int slotNumber, int clickedButton, int clickType) {
        ClickType clickType2;
        void $this$toClickType$iv;
        void $this$unwrap$iv22;
        Intrinsics.checkParameterIsNotNull((Object)slot, (String)"slot");
        Object t2 = this.getWrapped();
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer");
        }
        ISlot iSlot = slot;
        IMixinGuiContainer iMixinGuiContainer = (IMixinGuiContainer)t2;
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
    @Nullable
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

    public GuiContainerImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((GuiScreen)wrapped);
    }
}

