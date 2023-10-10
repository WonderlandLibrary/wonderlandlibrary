package client.module.impl.combat;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketSendEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.liquidbounce.ItemUtils;
import javafx.util.Pair;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

@ModuleInfo(name = "Auto Weapon", description = "", category = Category.COMBAT)
public class AutoWeapon extends Module {
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C02PacketUseEntity) {
            final C02PacketUseEntity packetUseEntity = (C02PacketUseEntity) packet;
            if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                Pair<Integer, ItemStack> result = Arrays.stream(IntStream.rangeClosed(0, 8).toArray())
                        .mapToObj(i -> new Pair<>(i, mc.thePlayer.inventory.getStackInSlot(i)))
                        .filter(pair -> pair.getValue() != null && (pair.getValue().getItem() instanceof ItemSword || pair.getValue().getItem() instanceof ItemTool))
                        .max(Comparator.comparingDouble(pair -> ((pair.getValue().getAttributeModifiers().get("generic.attackDamage").stream().findFirst().orElse(null) != null ? pair.getValue().getAttributeModifiers().get("generic.attackDamage").stream().findFirst().get().getAmount() : 0.0) + 1.25 * ItemUtils.getEnchantment(pair.getValue(), Enchantment.sharpness))))
                        .orElse(null);
                if (result == null || result.getKey() == mc.thePlayer.inventory.currentItem) return;
                mc.thePlayer.inventory.currentItem = result.getKey();
                mc.playerController.updateController();
                mc.getNetHandler().addToSendQueueUnregistered(packet);
                event.setCancelled(true);
            }
        }
    };
}