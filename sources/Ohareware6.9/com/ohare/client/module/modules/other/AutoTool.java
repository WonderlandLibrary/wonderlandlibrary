package com.ohare.client.module.modules.other;

import java.awt.Color;
import java.util.Collection;

import com.google.common.collect.Multimap;
import com.ohare.client.Client;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.value.impl.BooleanValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

/**
 * made by oHare for oHareWare
 *
 * @since 7/19/2019
 **/
public class AutoTool extends Module {

    private BooleanValue mouseCheck = new BooleanValue("MouseCheck",true);
    public AutoTool() {
        super("AutoTool", Category.OTHER,  new Color(0x82C295).getRGB());
        setRenderlabel("Auto Tool");
        setDescription("Selects the best tool when mining.");
        addValues(mouseCheck);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.isSending()) {
            if (event.getPacket() instanceof C07PacketPlayerDigging) {
                C07PacketPlayerDigging packetPlayerDigging = (C07PacketPlayerDigging) event.getPacket();
                if ((packetPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    if ((mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK || !mouseCheck.isEnabled()) && !mc.thePlayer.capabilities.isCreativeMode) {
                        BlockPos blockPosHit = mouseCheck.isEnabled() ? mc.objectMouseOver.getBlockPos() : packetPlayerDigging.getPosition();
                        if (blockPosHit != null || !mouseCheck.isEnabled()) {
                            mc.thePlayer.inventory.currentItem = getBestTool(blockPosHit);
                            mc.playerController.updateController();
                        }
                    }
                }
            }
            if (event.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packetUseEntity = (C02PacketUseEntity) event.getPacket();
                if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY || ! mouseCheck.isEnabled() ) {
                        final Entity entity = mouseCheck.isEnabled() ? mc.objectMouseOver.entityHit : packetUseEntity.getEntityFromWorld(mc.theWorld);
                        if (entity != null) {
                            if (!Client.INSTANCE.getFriendManager().isFriend(entity.getName())){
                                mc.thePlayer.inventory.currentItem = getBestWeapon(entity);
                                mc.playerController.updateController();
                            }
                        }
                    }
                }
            }
        }
    }

    private int getBestWeapon(Entity target) {
        int originalSlot = mc.thePlayer.inventory.currentItem;
        int weaponSlot = -1;
        float weaponDamage = 1.0F;
        for (byte slot = 0; slot < 9; slot = (byte)(slot + 1)) {
            mc.thePlayer.inventory.currentItem = slot;
            ItemStack itemStack = mc.thePlayer.getHeldItem();
            if (itemStack != null) {
                float damage = getItemDamage(itemStack);

                damage += EnchantmentHelper.func_152377_a(itemStack, ((EntityLivingBase) target).getCreatureAttribute());
                if (damage > weaponDamage) {
                    weaponDamage = damage;
                    weaponSlot = slot;
                }
            }
        }
        if (weaponSlot != -1) {
            return weaponSlot;
        }
        return originalSlot;
    }

    private float getItemDamage(ItemStack itemStack) {
        float damage = 1.0F;
        Multimap<String, AttributeModifier> attributes = itemStack.getAttributeModifiers();
        if (!attributes.isEmpty()) {
            Collection<AttributeModifier> attackModifier = attributes.get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
            for (AttributeModifier modifier : attackModifier) {
                damage += modifier.getAmount();
            }
        }
        return damage;
    }

    private int getBestTool(BlockPos pos) {
        final Block block = mc.theWorld.getBlockState(pos).getBlock();
        int slot = 0;
        float dmg = 0.1F;
        for (int index = 36; index < 45; index++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer
                    .getSlot(index).getStack();
            if (itemStack != null
                    && block != null
                    && itemStack.getItem().getStrVsBlock(itemStack, block) > dmg) {
                slot = index - 36;
                dmg = itemStack.getItem().getStrVsBlock(itemStack, block);
            }
        }
        if (dmg > 0.1F) {
            return slot;
        }
        return mc.thePlayer.inventory.currentItem;
    }
}
