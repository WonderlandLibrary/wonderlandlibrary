/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.script.api.global.Chat;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.TextValue;

@ModuleInfo(name="HytAutoLeos", description="SB", category=ModuleCategory.HYT)
public final class HytAutoLeos
extends Module {
    private final FloatValue healths = new FloatValue("Health", 5.0f, 1.0f, 20.0f);
    private final BoolValue loa = new BoolValue("MessageA", false);
    private final TextValue lobbyValue = new TextValue("Message", "[LRQ]Bye~");
    private boolean check = true;
    private boolean keepArmor;

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getHealth() <= ((Number)this.healths.get()).floatValue()) {
            int n = 3;
            boolean bl = false;
            while (n >= 0) {
                void i;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                IItemStack stack = iEntityPlayerSP2.getInventory().getArmorInventory().get((int)i);
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP3.getInventory().getArmorInventory().get(3) != null) {
                    this.move(5, true);
                }
                if (!(stack == null || stack.getUnlocalizedName().equals("item.chestplateChain") || stack.getUnlocalizedName().equals("item.chestplateChain") || stack.getUnlocalizedName().equals("item.leggingsChain") || stack.getUnlocalizedName().equals("item.chestplateIron") || stack.getUnlocalizedName().equals("item.leggingsIron") || stack.getUnlocalizedName().equals("item.bootsIron"))) {
                    this.move(8 - i, true);
                }
                if (i == false) {
                    this.keepArmor = false;
                }
                --i;
            }
            if (((Boolean)this.loa.get()).booleanValue() && this.check) {
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP4.sendChatMessage((String)this.lobbyValue.get());
            }
            if (this.check && !this.keepArmor) {
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP5.sendChatMessage("/hub");
                LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class).setState(false);
                LiquidBounce.INSTANCE.getModuleManager().get(Velocity.class).setState(false);
                this.check = false;
            }
            Chat.print("\u00a7b[LRQ] \u00a7d\u4e3a\u4f60\u7684\u88c5\u5907\u4fdd\u9a7e\u62a4\u822a");
        }
    }

    private final void move(int item, boolean isArmorSlot) {
        if (item != -1) {
            boolean openInventory;
            boolean bl = openInventory = !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen());
            if (openInventory) {
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.OPEN_INVENTORY));
            }
            IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            int n = iEntityPlayerSP.getInventoryContainer().getWindowId();
            int n2 = isArmorSlot ? item : (item < 9 ? item + 36 : item);
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iPlayerControllerMP.windowClick(n, n2, 0, 1, iEntityPlayerSP2);
            if (openInventory) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
            }
        }
    }

    @EventTarget
    public final void onWorld(WorldEvent event) {
        this.check = true;
        this.keepArmor = true;
    }

    @Override
    public String getTag() {
        return "Health " + ((Number)this.healths.get()).floatValue();
    }
}

