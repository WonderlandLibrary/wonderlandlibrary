// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.impl.EventDamageBlock;
import exhibition.event.RegisterEvent;
import net.minecraft.network.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.network.play.client.C02PacketUseEntity;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.Block;
import exhibition.module.data.ModuleData;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import exhibition.module.Module;

public class ItemSpoof extends Module
{
    private boolean send;
    private BlockPos block;
    private EnumFacing facing;
    private int placeDir;
    private ItemStack stack;
    private float facingX;
    private float facingY;
    private float facingZ;
    public static Entity target;
    
    public ItemSpoof(final ModuleData data) {
        super(data);
    }
    
    private int getSlotForTool(final Block pos) {
        int slot = 9;
        float damage = 0.0f;
        for (int i = 9; i <= 35; ++i) {
            if (ItemSpoof.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack stack = ItemSpoof.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final float curDamage = stack.getStrVsBlock(pos);
                if (curDamage >= damage) {
                    damage = curDamage;
                    slot = i;
                    System.out.println(String.valueOf(stack.getDisplayName()) + " " + slot);
                }
            }
        }
        return slot;
    }
    
    public float damage(final Block p_180471_1_) {
        final ItemStack var4 = ItemSpoof.mc.thePlayer.inventoryContainer.getSlot(this.getSlotForTool(ItemSpoof.mc.theWorld.getBlockState(this.block).getBlock())).getStack();
        float var5 = var4.getStrVsBlock(ItemSpoof.mc.theWorld.getBlockState(this.block).getBlock());
        if (var5 > 1.0f) {
            final int var6 = EnchantmentHelper.getEfficiencyModifier(ItemSpoof.mc.thePlayer);
            if (var6 > 0 && var4 != null) {
                var5 += var6 * var6 + 1;
            }
        }
        if (ItemSpoof.mc.thePlayer.isPotionActive(Potion.digSpeed)) {
            var5 *= 1.0f + (ItemSpoof.mc.thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2f;
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
                    break;
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
    
    @RegisterEvent(events = { EventPacket.class, EventDamageBlock.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventPacket) {
            final EventPacket e = (EventPacket)event;
            if (e.isOutgoing() && this.send) {
                this.send = false;
                return;
            }
            if (e.isOutgoing() && e.getPacket() instanceof C02PacketUseEntity) {
                final C02PacketUseEntity attack = (C02PacketUseEntity)e.getPacket();
                attack.getAction();
                if (attack.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if (ItemSpoof.target == null) {
                        ItemSpoof.target = attack.getEntityFromWorld(ItemSpoof.mc.theWorld);
                    }
                    if (ItemSpoof.target != null) {
                        ItemSpoof.mc.playerController.windowClick(ItemSpoof.mc.thePlayer.inventoryContainer.windowId, 9, ItemSpoof.mc.thePlayer.inventory.currentItem, 2, ItemSpoof.mc.thePlayer);
                        ItemSpoof.mc.playerController.updateController();
                        this.send = true;
                        ItemSpoof.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ItemSpoof.target, C02PacketUseEntity.Action.ATTACK));
                        ItemSpoof.mc.playerController.windowClick(ItemSpoof.mc.thePlayer.inventoryContainer.windowId, 9, ItemSpoof.mc.thePlayer.inventory.currentItem, 2, ItemSpoof.mc.thePlayer);
                        ItemSpoof.mc.playerController.updateController();
                        ItemSpoof.target = null;
                    }
                }
            }
        }
    }
}
