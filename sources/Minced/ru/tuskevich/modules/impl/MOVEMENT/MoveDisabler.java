// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemElytra;
import ru.tuskevich.util.world.InventoryUtility;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Iterator;
import ru.tuskevich.util.movement.MoveUtility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "MoveDisabler", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class MoveDisabler extends Module
{
    TimerUtility timerHelper;
    private boolean disabled;
    
    public MoveDisabler() {
        this.timerHelper = new TimerUtility();
        this.add(new Setting[0]);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        final Minecraft mc = MoveDisabler.mc;
        for (final ItemStack stack : Minecraft.player.inventory.armorInventory) {
            if (stack.getItem() == Items.ELYTRA) {
                swapElytraToChestplate();
                this.disabled = true;
            }
        }
        if (this.disabled && this.timerHelper.hasTimeElapsed(300L)) {
            MoveUtility.setMotion(0.8);
            final Minecraft mc2 = MoveDisabler.mc;
            Minecraft.player.motionY = 0.0;
            if (MoveDisabler.mc.gameSettings.keyBindJump.isKeyDown()) {
                final Minecraft mc3 = MoveDisabler.mc;
                final EntityPlayerSP player = Minecraft.player;
                player.motionY += 0.5;
            }
            else if (MoveDisabler.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final Minecraft mc4 = MoveDisabler.mc;
                final EntityPlayerSP player2 = Minecraft.player;
                player2.motionY -= 0.5;
            }
            else {
                final Minecraft mc5 = MoveDisabler.mc;
                final EntityPlayerSP player3 = Minecraft.player;
                final Minecraft mc6 = MoveDisabler.mc;
                player3.motionY = ((Minecraft.player.ticksExisted % 2 != 0) ? -0.05 : 0.05);
            }
        }
        final Minecraft mc7 = MoveDisabler.mc;
        if (Minecraft.player.ticksExisted % 20 == 0) {
            swapChestplateToElytra();
        }
    }
    
    @EventTarget
    @Override
    public void onEnable() {
        super.onEnable();
        this.timerHelper.reset();
        if (InventoryUtility.getItemIndex(Items.ELYTRA) == -1) {
            return;
        }
        final Minecraft mc = MoveDisabler.mc;
        if (!(Minecraft.player.inventory.getItemStack().getItem() instanceof ItemElytra)) {
            swapChestplateToElytra();
        }
    }
    
    @EventTarget
    @Override
    public void onDisable() {
        super.onDisable();
        this.disabled = false;
        this.timerHelper.reset();
        if (InventoryUtility.getItemIndex(Items.ELYTRA) == -1) {
            return;
        }
        swapElytraToChestplate();
    }
    
    public static int getSlotWithElytra() {
        for (int i = 0; i < 45; ++i) {
            final Minecraft mc = MoveDisabler.mc;
            final ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.ELYTRA) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static int getSlowWithArmor() {
        for (int i = 0; i < 45; ++i) {
            final Minecraft mc = MoveDisabler.mc;
            final ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE || itemStack.getItem() == Items.IRON_LEGGINGS) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static void swapChestplateToElytra() {
        final Minecraft mc = MoveDisabler.mc;
        for (final ItemStack stack : Minecraft.player.inventory.armorInventory) {
            if (stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.GOLDEN_CHESTPLATE || stack.getItem() == Items.LEATHER_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE || stack.getItem() == Items.IRON_LEGGINGS) {
                final int slot = (getSlotWithElytra() < 9) ? (getSlotWithElytra() + 36) : getSlotWithElytra();
                if (getSlotWithElytra() == -1) {
                    continue;
                }
                final PlayerControllerMP playerController = MoveDisabler.mc.playerController;
                final int windowId = 0;
                final int slotId = slot;
                final int mouseButton = 1;
                final ClickType pickup = ClickType.PICKUP;
                final Minecraft mc2 = MoveDisabler.mc;
                playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                final PlayerControllerMP playerController2 = MoveDisabler.mc.playerController;
                final int windowId2 = 0;
                final int slotId2 = 6;
                final int mouseButton2 = 0;
                final ClickType pickup2 = ClickType.PICKUP;
                final Minecraft mc3 = MoveDisabler.mc;
                playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
                final PlayerControllerMP playerController3 = MoveDisabler.mc.playerController;
                final int windowId3 = 0;
                final int slotId3 = slot;
                final int mouseButton3 = 1;
                final ClickType pickup3 = ClickType.PICKUP;
                final Minecraft mc4 = MoveDisabler.mc;
                playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
                final NetHandlerPlayClient connection = MoveDisabler.mc.getConnection();
                final Minecraft mc5 = MoveDisabler.mc;
                connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
    }
    
    public static void swapElytraToChestplate() {
        final Minecraft mc = MoveDisabler.mc;
        for (final ItemStack stack : Minecraft.player.inventory.armorInventory) {
            if (stack.getItem() == Items.ELYTRA) {
                final int slot = (getSlowWithArmor() < 9) ? (getSlowWithArmor() + 36) : getSlowWithArmor();
                if (getSlowWithArmor() == -1) {
                    continue;
                }
                final PlayerControllerMP playerController = MoveDisabler.mc.playerController;
                final int windowId = 0;
                final int slotId = slot;
                final int mouseButton = 1;
                final ClickType pickup = ClickType.PICKUP;
                final Minecraft mc2 = MoveDisabler.mc;
                playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                final PlayerControllerMP playerController2 = MoveDisabler.mc.playerController;
                final int windowId2 = 0;
                final int slotId2 = 6;
                final int mouseButton2 = 0;
                final ClickType pickup2 = ClickType.PICKUP;
                final Minecraft mc3 = MoveDisabler.mc;
                playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
                final PlayerControllerMP playerController3 = MoveDisabler.mc.playerController;
                final int windowId3 = 0;
                final int slotId3 = slot;
                final int mouseButton3 = 1;
                final ClickType pickup3 = ClickType.PICKUP;
                final Minecraft mc4 = MoveDisabler.mc;
                playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
            }
        }
    }
}
