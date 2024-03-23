package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.api.mixin.IPlayerInteractEntityC2SPacket;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.PacketSendEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.PacketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

public class TotemHit extends Module {

    public TotemHit() {
        super("Totem Hit", Category.Combat, "More knockback when hitting players with totems");
    }

    @EventListener
    public void event(PacketSendEvent event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket && ((IPlayerInteractEntityC2SPacket)event.getPacket()).getType().getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
            Entity entity = getMc().world.getEntityById(((IPlayerInteractEntityC2SPacket)event.getPacket()).getEntityId());
            if (entity == null) {
                return;
            }
            if (entity instanceof PlayerEntity && getMc().player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
                for (int i = 0; i < 9; ++i) {
                    ItemStack itemStack = getMc().player.getInventory().getStack(i);
                    if (itemStack.getItem() instanceof SwordItem) {
                        int slot = getMc().player.getInventory().selectedSlot;
                        InventoryUtil.INSTANCE.setCurrentSlot(i);
                        PacketUtil.INSTANCE.sendPacket(event.getPacket());
                        InventoryUtil.INSTANCE.setCurrentSlot(slot);
                        return;
                    }
                }
            }
        }
    }
}
