/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.inventory.InventoryUtil;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class AutoPot
extends Module {
    private int slot;
    private int item;
    private int oldSlot;

    public AutoPot() {
        super("AutoPot", Category.Combat);
        ArrayList<String> options = new ArrayList<String>();
        options.add("Packet");
        options.add("Client");
        Main.instance.setmgr.rSetting(new Setting("AutoPot Mode", this, "Packet", options));
    }

    @Override
    public void onEnable() {
        this.slot = Minecraft.player.inventory.currentItem;
        this.oldSlot = Minecraft.player.inventory.currentItem;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Minecraft.player.inventory.currentItem = this.slot;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("AutoPot Mode").getValString();
        this.setSuffix(mode);
        if (!this.doesNextSlotHavePot()) {
            return;
        }
        this.item = InventoryUtil.getSlotWithPot();
        if (this.slot != -1) {
            if (Minecraft.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                if (!Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                    if (mode.equalsIgnoreCase("Packet")) {
                        this.sendPotPacket(event);
                    } else if (mode.equalsIgnoreCase("Client")) {
                        Minecraft.player.rotationPitch = 90.0f;
                    }
                    this.slot = this.item;
                    Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.slot));
                    Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
                }
                if (Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                    if (!Minecraft.player.isPotionActive(MobEffects.STRENGTH)) {
                        if (mode.equalsIgnoreCase("Packet")) {
                            this.sendPotPacket(event);
                        } else if (mode.equalsIgnoreCase("Client")) {
                            Minecraft.player.rotationPitch = 90.0f;
                        }
                        this.slot = this.item + 1;
                        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.slot));
                        Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
                    }
                    if (Minecraft.player.isPotionActive(MobEffects.STRENGTH)) {
                        if (!Minecraft.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                            if (mode.equalsIgnoreCase("Packet")) {
                                this.sendPotPacket(event);
                            } else if (mode.equalsIgnoreCase("Client")) {
                                Minecraft.player.rotationPitch = 90.0f;
                            }
                            this.slot = this.item + 2;
                            Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.slot));
                            Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                            Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
                        }
                    }
                }
            } else if (Minecraft.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                if (!Minecraft.player.isPotionActive(MobEffects.STRENGTH)) {
                    if (mode.equalsIgnoreCase("Packet")) {
                        this.sendPotPacket(event);
                    } else if (mode.equalsIgnoreCase("Client")) {
                        Minecraft.player.rotationPitch = 90.0f;
                    }
                    this.slot = this.item;
                    Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.slot));
                    Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
                }
                if (Minecraft.player.isPotionActive(MobEffects.STRENGTH)) {
                    if (!Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                        if (mode.equalsIgnoreCase("Packet")) {
                            this.sendPotPacket(event);
                        } else if (mode.equalsIgnoreCase("Client")) {
                            Minecraft.player.rotationPitch = 90.0f;
                        }
                        this.slot = this.item + 1;
                        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.slot));
                        Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
                    }
                    if (Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                        if (!Minecraft.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                            if (mode.equalsIgnoreCase("Packet")) {
                                this.sendPotPacket(event);
                            } else if (mode.equalsIgnoreCase("Client")) {
                                Minecraft.player.rotationPitch = 90.0f;
                            }
                            this.slot = this.item + 2;
                            Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.slot));
                            Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                            Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
                        }
                    }
                }
            }
        }
    }

    private void sendPotPacket(EventPreMotionUpdate event) {
        double xDist = Minecraft.player.posX - Minecraft.player.lastTickPosX;
        double zDist = Minecraft.player.posZ - Minecraft.player.lastTickPosZ;
        double speed = Math.sqrt(xDist * xDist + zDist * zDist);
        boolean shouldPredict = speed > 0.38;
        boolean shouldJump = speed < 0.221;
        boolean onGround = MovementUtil.isOnGround();
        if (shouldJump && onGround && !MovementUtil.isBlockAbove() && MovementUtil.getJumpBoostModifier() == 0) {
            Minecraft.player.motionX *= 0.0;
            Minecraft.player.motionZ *= 0.0;
            event.setPitch(90.0f);
            Minecraft.player.rotationPitchHead = 90.0f;
        } else if (shouldPredict || onGround) {
            event.setYaw(MovementUtil.getMovementDirection());
            event.setPitch(shouldPredict ? 0.0f : 45.0f);
        } else {
            return;
        }
    }

    private boolean doesNextSlotHavePot() {
        for (int i = 0; i < 9; ++i) {
            Minecraft.player.inventory.getStackInSlot(i);
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() != Items.SPLASH_POTION) continue;
            return true;
        }
        return false;
    }
}

