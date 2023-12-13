package me.earth.phobos.features.modules.combat;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.DamageUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ArmorMessage
        extends Module {
    private final Setting<Integer> armorThreshhold = this.register(new Setting<Integer>("Armor%", 20, 1, 100));
    private final Setting<Boolean> notifySelf = this.register(new Setting<Boolean>("NotifySelf", true));
    private final Setting<Boolean> notification = this.register(new Setting<Boolean>("Notification", true));
    private final Map<EntityPlayer, Integer> entityArmorArraylist = new HashMap<EntityPlayer, Integer>();
    private final Timer timer = new Timer();

    public ArmorMessage() {
        super("ArmorMessage", "Message friends when their armor is low", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onUpdate(UpdateWalkingPlayerEvent event) {
        for (EntityPlayer player : ArmorMessage.mc.world.playerEntities) {
            if (player.isDead || !Phobos.friendManager.isFriend(player.getName())) continue;
            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack == ItemStack.EMPTY) continue;
                int percent = DamageUtil.getRoundedDamage(stack);
                if (percent <= this.armorThreshhold.getValue() && !this.entityArmorArraylist.containsKey(player)) {
                    if (player == ArmorMessage.mc.player && this.notifySelf.getValue().booleanValue()) {
                        Command.sendMessage(player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low dura!", this.notification.getValue());
                    } else {
                        ArmorMessage.mc.player.sendChatMessage("/msg " + player.getName() + " " + player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low dura!");
                    }
                    this.entityArmorArraylist.put(player, player.inventory.armorInventory.indexOf(stack));
                }
                if (!this.entityArmorArraylist.containsKey(player) || this.entityArmorArraylist.get(player).intValue() != player.inventory.armorInventory.indexOf(stack) || percent <= this.armorThreshhold.getValue())
                    continue;
                this.entityArmorArraylist.remove(player);
            }
            if (!this.entityArmorArraylist.containsKey(player) || player.inventory.armorInventory.get(this.entityArmorArraylist.get(player).intValue()) != ItemStack.EMPTY)
                continue;
            this.entityArmorArraylist.remove(player);
        }
    }

    private String getArmorPieceName(ItemStack stack) {
        if (stack.getItem() == Items.DIAMOND_HELMET || stack.getItem() == Items.GOLDEN_HELMET || stack.getItem() == Items.IRON_HELMET || stack.getItem() == Items.CHAINMAIL_HELMET || stack.getItem() == Items.LEATHER_HELMET) {
            return "helmet is";
        }
        if (stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.GOLDEN_CHESTPLATE || stack.getItem() == Items.IRON_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE || stack.getItem() == Items.LEATHER_CHESTPLATE) {
            return "chestplate is";
        }
        if (stack.getItem() == Items.DIAMOND_LEGGINGS || stack.getItem() == Items.GOLDEN_LEGGINGS || stack.getItem() == Items.IRON_LEGGINGS || stack.getItem() == Items.CHAINMAIL_LEGGINGS || stack.getItem() == Items.LEATHER_LEGGINGS) {
            return "leggings are";
        }
        return "boots are";
    }
}

