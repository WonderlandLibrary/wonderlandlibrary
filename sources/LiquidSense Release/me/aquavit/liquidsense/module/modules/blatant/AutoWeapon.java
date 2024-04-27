package me.aquavit.liquidsense.module.modules.blatant;

import me.aquavit.liquidsense.utils.item.ItemUtils;
import me.aquavit.liquidsense.event.events.AttackEvent;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

@ModuleInfo(name = "AutoWeapon", description = "Automatically selects the best weapon in your hotbar.", category = ModuleCategory.BLATANT)
public class AutoWeapon extends Module {
    private BoolValue silentValue = new BoolValue("SpoofItem", false);

    private C02PacketUseEntity packetUseEntity;
    private boolean spoofedSlot = false;
    private boolean gotIt = false;
    private int tick;

    @EventTarget
    public void onAttack(AttackEvent event){
        gotIt = true;
    }

    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK && this.gotIt) {
            gotIt = false;

            int slot = -1;
            double bestDamage = 0.0;

            for (int i=0; i<=8; i++){
                ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
                if (itemStack != null && (itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool)) {
                    for (AttributeModifier attributeModifier : itemStack.getAttributeModifiers().get("generic.attackDamage")){
                        double damage = attributeModifier.getAmount() + 1.25 * (double) ItemUtils.getEnchantment(itemStack, Enchantment.sharpness);

                        if (damage > bestDamage) {
                            bestDamage = damage;
                            slot = i;
                        }
                    }
                }
            }

            if (slot != -1 && slot != mc.thePlayer.inventory.currentItem) {
                if (silentValue.get()) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(slot));
                    spoofedSlot = true;
                } else {
                    mc.thePlayer.inventory.currentItem = slot;
                    mc.playerController.updateController();
                }

                event.cancelEvent();
                packetUseEntity = (C02PacketUseEntity) event.getPacket();
                tick = 0;
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (tick < 1) {
            tick++;
            return;
        }

        if (packetUseEntity != null) {
            mc.getNetHandler().getNetworkManager().sendPacket(packetUseEntity);

            if (spoofedSlot)
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

            packetUseEntity = null;
        }
    }

}
