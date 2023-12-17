// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import ru.tuskevich.util.movement.MoveUtility;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.util.chat.ChatUtility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemElytra;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import ru.tuskevich.util.world.InventoryUtility;
import net.minecraft.client.Minecraft;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "ElytraFireWork", desc = "", type = Type.MOVEMENT)
public class SunElytra extends Module
{
    private int disable;
    
    @EventTarget
    @Override
    public void onDisable() {
        this.disable = 0;
        final Minecraft mc = SunElytra.mc;
        Minecraft.player.motionZ = 0.0;
        final Minecraft mc2 = SunElytra.mc;
        Minecraft.player.motionX = 0.0;
        final Minecraft mc3 = SunElytra.mc;
        Minecraft.player.motionY = 0.0;
        if (InventoryUtility.getFireworks() >= 0) {
            final int armor = InventoryUtility.getSlotWithArmor();
            final PlayerControllerMP playerController = SunElytra.mc.playerController;
            final int windowId = 0;
            final int slotId = (armor < 9) ? (armor + 36) : armor;
            final int mouseButton = 1;
            final ClickType pickup = ClickType.PICKUP;
            final Minecraft mc4 = SunElytra.mc;
            playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
            final PlayerControllerMP playerController2 = SunElytra.mc.playerController;
            final int windowId2 = 0;
            final int slotId2 = 6;
            final int mouseButton2 = 1;
            final ClickType pickup2 = ClickType.PICKUP;
            final Minecraft mc5 = SunElytra.mc;
            playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
            final PlayerControllerMP playerController3 = SunElytra.mc.playerController;
            final int windowId3 = 0;
            final int slotId3 = (armor < 9) ? (armor + 36) : armor;
            final int mouseButton3 = 1;
            final ClickType pickup3 = ClickType.PICKUP;
            final Minecraft mc6 = SunElytra.mc;
            playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
        }
        super.onDisable();
    }
    
    @EventTarget
    @Override
    public void onEnable() {
        if (InventoryUtility.getFireworks() >= 0) {
            final Minecraft mc = SunElytra.mc;
            if (!(Minecraft.player.inventory.getItemStack().getItem() instanceof ItemElytra)) {
                final int elytra = InventoryUtility.getItemIndex(Items.ELYTRA);
                final PlayerControllerMP playerController = SunElytra.mc.playerController;
                final int windowId = 0;
                final int slotId = (elytra < 9) ? (elytra + 36) : elytra;
                final int mouseButton = 1;
                final ClickType pickup = ClickType.PICKUP;
                final Minecraft mc2 = SunElytra.mc;
                playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                final PlayerControllerMP playerController2 = SunElytra.mc.playerController;
                final int windowId2 = 0;
                final int slotId2 = 6;
                final int mouseButton2 = 1;
                final ClickType pickup2 = ClickType.PICKUP;
                final Minecraft mc3 = SunElytra.mc;
                playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
                final PlayerControllerMP playerController3 = SunElytra.mc.playerController;
                final int windowId3 = 0;
                final int slotId3 = (elytra < 9) ? (elytra + 36) : elytra;
                final int mouseButton3 = 1;
                final ClickType pickup3 = ClickType.PICKUP;
                final Minecraft mc4 = SunElytra.mc;
                playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
            }
        }
        else {
            ChatUtility.addChatMessage("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u0444\u0435\u0435\u0440\u0432\u0435\u0440\u043a\u043e\u0432 \u0434\u043b\u044f \u0440\u0430\u0431\u043e\u0442\u044b \u044d\u0442\u043e\u0433\u043e \u0444\u043b\u0430\u044f");
            this.toggle();
        }
        super.onEnable();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.disable == 3) {
            this.onDisable();
        }
        final Minecraft mc = SunElytra.mc;
        if (Minecraft.player.wasFallFlying) {
            if (InventoryUtility.getFireworks() >= 0) {
                final Minecraft mc2 = SunElytra.mc;
                Minecraft.player.motionY = 0.023;
                MoveUtility.setMotion(MoveUtility.getSpeed());
                if (SunElytra.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final Minecraft mc3 = SunElytra.mc;
                    Minecraft.player.motionY = 0.4;
                }
                else if (SunElytra.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final Minecraft mc4 = SunElytra.mc;
                    Minecraft.player.motionY = -0.6;
                }
                final Minecraft mc5 = SunElytra.mc;
                if (Minecraft.player.ticksExisted % 35 == 0) {
                    this.useFireworks();
                }
            }
            else {
                final Minecraft mc6 = SunElytra.mc;
                if (Minecraft.player.ticksExisted % 15 == 0) {
                    ++this.disable;
                    ChatUtility.addChatMessage("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u0444\u0435\u0435\u0440\u0432\u0435\u0440\u043a\u043e\u0432! \u0412\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435 \u0447\u0435\u0440\u0435\u0437 " + this.disable);
                }
            }
        }
        else if (InventoryUtility.getFireworks() >= 0) {
            final Minecraft mc7 = SunElytra.mc;
            if (Minecraft.player.ticksExisted % 8 == 0) {
                final Minecraft mc8 = SunElytra.mc;
                Minecraft.player.jump();
                final NetHandlerPlayClient connection = SunElytra.mc.getConnection();
                final Minecraft mc9 = SunElytra.mc;
                connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
                this.useFireworks();
            }
        }
    }
    
    public void useFireworks() {
        final int fireworks = InventoryUtility.getItemIndex(Items.FIREWORKS);
        final Minecraft mc = SunElytra.mc;
        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(fireworks));
        SunElytra.mc.playerController.updateController();
        final Minecraft mc2 = SunElytra.mc;
        Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        SunElytra.mc.playerController.updateController();
        final Minecraft mc3 = SunElytra.mc;
        final NetHandlerPlayClient connection = Minecraft.player.connection;
        final Minecraft mc4 = SunElytra.mc;
        connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
        SunElytra.mc.playerController.updateController();
    }
}
