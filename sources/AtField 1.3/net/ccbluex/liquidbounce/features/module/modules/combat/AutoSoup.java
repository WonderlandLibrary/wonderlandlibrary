/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AutoSoup", description="Makes you automatically eat soup whenever your health is low.", category=ModuleCategory.COMBAT)
public final class AutoSoup
extends Module {
    private final FloatValue healthValue = new FloatValue("Health", 15.0f, 0.0f, 20.0f);
    private final BoolValue simulateInventoryValue;
    private final BoolValue openInventoryValue;
    private final IntegerValue delayValue = new IntegerValue("Delay", 150, 0, 500);
    private final MSTimer timer;
    private final ListValue bowlValue;

    public AutoSoup() {
        this.openInventoryValue = new BoolValue("OpenInv", false);
        this.simulateInventoryValue = new BoolValue("SimulateInventory", true);
        this.bowlValue = new ListValue("Bowl", new String[]{"Drop", "Move", "Stay"}, "Drop");
        this.timer = new MSTimer();
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        IPacket iPacket;
        IINetHandlerPlayClient iINetHandlerPlayClient;
        int n;
        int n2;
        int n3;
        if (!this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        int n4 = InventoryUtils.findItem(36, 45, MinecraftInstance.classProvider.getItemEnum(ItemType.MUSHROOM_STEW));
        if (iEntityPlayerSP2.getHealth() <= ((Number)this.healthValue.get()).floatValue() && n4 != -1) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(n4 - 36));
            IItemStack iItemStack = iEntityPlayerSP2.getInventory().getStackInSlot(n4);
            WEnumHand wEnumHand = WEnumHand.MAIN_HAND;
            IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
            boolean bl = false;
            IPacket iPacket2 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
            iINetHandlerPlayClient2.addToSendQueue(iPacket2);
            if (StringsKt.equals((String)((String)this.bowlValue.get()), (String)"Drop", (boolean)true)) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.DROP_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP2.getInventory().getCurrentItem()));
            this.timer.reset();
            return;
        }
        int n5 = InventoryUtils.findItem(36, 45, MinecraftInstance.classProvider.getItemEnum(ItemType.BOWL));
        if (StringsKt.equals((String)((String)this.bowlValue.get()), (String)"Move", (boolean)true) && n5 != -1) {
            if (((Boolean)this.openInventoryValue.get()).booleanValue() && !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen())) {
                return;
            }
            n3 = 0;
            n2 = 36;
            for (n = 9; n <= n2; ++n) {
                IItemStack iItemStack = iEntityPlayerSP2.getInventory().getStackInSlot(n);
                if (iItemStack == null) {
                    n3 = 1;
                    break;
                }
                if (!iItemStack.getItem().equals(MinecraftInstance.classProvider.getItemEnum(ItemType.BOWL)) || iItemStack.getStackSize() >= 64) continue;
                n3 = 1;
                break;
            }
            if (n3 != 0) {
                int n6 = n = !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen()) && (Boolean)this.simulateInventoryValue.get() != false ? 1 : 0;
                if (n != 0) {
                    iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    n2 = 0;
                    IClassProvider iClassProvider = WrapperImpl.INSTANCE.getClassProvider();
                    IEntityPlayerSP iEntityPlayerSP3 = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    iPacket = iClassProvider.createCPacketEntityAction(iEntityPlayerSP3, ICPacketEntityAction.WAction.OPEN_INVENTORY);
                    iINetHandlerPlayClient.addToSendQueue(iPacket);
                }
                MinecraftInstance.mc.getPlayerController().windowClick(0, n5, 0, 1, iEntityPlayerSP2);
            }
        }
        if ((n3 = InventoryUtils.findItem(9, 36, MinecraftInstance.classProvider.getItemEnum(ItemType.MUSHROOM_STEW))) != -1 && InventoryUtils.hasSpaceHotbar()) {
            if (((Boolean)this.openInventoryValue.get()).booleanValue() && !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen())) {
                return;
            }
            int n7 = n = !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen()) && (Boolean)this.simulateInventoryValue.get() != false ? 1 : 0;
            if (n != 0) {
                iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                n2 = 0;
                IClassProvider iClassProvider = WrapperImpl.INSTANCE.getClassProvider();
                IEntityPlayerSP iEntityPlayerSP4 = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                iPacket = iClassProvider.createCPacketEntityAction(iEntityPlayerSP4, ICPacketEntityAction.WAction.OPEN_INVENTORY);
                iINetHandlerPlayClient.addToSendQueue(iPacket);
            }
            MinecraftInstance.mc.getPlayerController().windowClick(0, n3, 0, 1, iEntityPlayerSP2);
            if (n != 0) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
            }
            this.timer.reset();
        }
    }

    @Override
    public String getTag() {
        return String.valueOf(((Number)this.healthValue.get()).floatValue());
    }
}

