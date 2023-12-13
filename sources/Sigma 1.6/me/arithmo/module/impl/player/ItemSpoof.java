/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import java.io.PrintStream;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventDamageBlock;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSpoof
extends Module {
    private boolean send;
    private BlockPos block;
    private EnumFacing facing;
    private int placeDir;
    private ItemStack stack;
    private float facingX;
    private float facingY;
    private float facingZ;
    public static Entity target;

    public ItemSpoof(ModuleData data) {
        super(data);
    }

    private int getSlotForTool(Block pos) {
        int slot = 9;
        float damage = 0.0f;
        for (int i = 9; i <= 35; ++i) {
            ItemStack stack;
            float curDamage;
            if (!ItemSpoof.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || (curDamage = (stack = ItemSpoof.mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getStrVsBlock(pos)) < damage) continue;
            damage = curDamage;
            slot = i;
            System.out.println(String.valueOf(stack.getDisplayName()) + " " + slot);
        }
        return slot;
    }

    public float damage(Block p_180471_1_) {
        int var6;
        ItemStack var4 = ItemSpoof.mc.thePlayer.inventoryContainer.getSlot(this.getSlotForTool(ItemSpoof.mc.theWorld.getBlockState(this.block).getBlock())).getStack();
        float var5 = var4.getStrVsBlock(ItemSpoof.mc.theWorld.getBlockState(this.block).getBlock());
        if (var5 > 1.0f && (var6 = EnchantmentHelper.getEfficiencyModifier(ItemSpoof.mc.thePlayer)) > 0 && var4 != null) {
            var5 += (float)(var6 * var6 + 1);
        }
        if (ItemSpoof.mc.thePlayer.isPotionActive(Potion.digSpeed)) {
            var5 *= 1.0f + (float)(ItemSpoof.mc.thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2f;
        }
        if (ItemSpoof.mc.thePlayer.isPotionActive(Potion.digSlowdown)) {
            float var7 = 1.0f;
            switch (ItemSpoof.mc.thePlayer.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
                case 0: {
                    var7 = 0.3f;
                    break;
                }
                case 1: {
                    var7 = 0.09f;
                    break;
                }
                case 2: {
                    var7 = 0.0027f;
                    break;
                }
                default: {
                    var7 = 8.1E-4f;
                }
            }
            var5 *= var7;
        }
        if (ItemSpoof.mc.thePlayer.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(ItemSpoof.mc.thePlayer)) {
            var5 /= 5.0f;
        }
        if (!ItemSpoof.mc.thePlayer.onGround) {
            var5 /= 5.0f;
        }
        return var5;
    }

    @RegisterEvent(events={EventPacket.class, EventDamageBlock.class})
    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            EventPacket e = (EventPacket)event;
            if (e.isOutgoing() && this.send) {
                this.send = false;
                return;
            }
            if (e.isOutgoing() && e.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity attack = (C02PacketUseEntity)e.getPacket();
                attack.getAction();
                if (attack.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if (target == null) {
                        target = attack.getEntityFromWorld(ItemSpoof.mc.theWorld);
                    }
                    if (target != null) {
                        ItemSpoof.mc.playerController.windowClick(ItemSpoof.mc.thePlayer.inventoryContainer.windowId, 9, ItemSpoof.mc.thePlayer.inventory.currentItem, 2, ItemSpoof.mc.thePlayer);
                        ItemSpoof.mc.playerController.updateController();
                        this.send = true;
                        ItemSpoof.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                        ItemSpoof.mc.playerController.windowClick(ItemSpoof.mc.thePlayer.inventoryContainer.windowId, 9, ItemSpoof.mc.thePlayer.inventory.currentItem, 2, ItemSpoof.mc.thePlayer);
                        ItemSpoof.mc.playerController.updateController();
                        target = null;
                    }
                }
            }
        }
    }
}

