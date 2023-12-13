// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import com.elementars.eclient.event.EventTarget;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import com.elementars.eclient.event.events.EventSendPacket;
import com.elementars.eclient.module.Category;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import com.elementars.eclient.module.Module;

public final class ItemSpoof extends Module
{
    private boolean send;
    private Entity entity;
    public BlockPos position;
    public EnumFacing placedBlockDirection;
    public EnumHand hand;
    public float facingX;
    public float facingY;
    public float facingZ;
    
    public ItemSpoof() {
        super("ItemSpoof", "Allows you to display a different item server-side(Use the top left slot in your inventory)", 0, Category.PLAYER, true);
    }
    
    @EventTarget
    public void sendPacket(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            if (this.send) {
                this.send = false;
                return;
            }
            final CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            this.position = packet.getPos();
            this.placedBlockDirection = packet.getDirection();
            this.hand = packet.getHand();
            this.facingX = packet.getFacingX();
            this.facingY = packet.getFacingY();
            this.facingZ = packet.getFacingZ();
            if (this.position != null) {
                event.setCancelled(true);
                final Minecraft mc = Minecraft.getMinecraft();
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)mc.player);
                this.send = true;
                mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.position, this.placedBlockDirection, this.hand, this.facingX, this.facingY, this.facingZ));
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)mc.player);
            }
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItem) {
            if (this.send) {
                this.send = false;
                return;
            }
            final CPacketPlayerTryUseItem packet2 = (CPacketPlayerTryUseItem)event.getPacket();
            this.hand = packet2.getHand();
            if (this.hand != null) {
                event.setCancelled(true);
                final Minecraft mc = Minecraft.getMinecraft();
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)mc.player);
                this.send = true;
                mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(this.hand));
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)mc.player);
            }
        }
        if (event.getPacket() instanceof CPacketUseEntity) {
            if (this.send) {
                this.send = false;
                return;
            }
            final CPacketUseEntity packet3 = (CPacketUseEntity)event.getPacket();
            if (packet3.getAction() == CPacketUseEntity.Action.ATTACK) {
                final Minecraft mc = Minecraft.getMinecraft();
                this.entity = packet3.getEntityFromWorld((World)mc.world);
                if (this.entity != null) {
                    event.setCancelled(true);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)mc.player);
                    this.send = true;
                    mc.player.connection.sendPacket((Packet)new CPacketUseEntity(this.entity));
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)mc.player);
                }
            }
        }
    }
}
