package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.PacketSendEvent;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.managers.DamageManager;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.setting.impl.MultipleSetting;
import dev.zprestige.prestige.client.util.impl.BlockUtil;
import dev.zprestige.prestige.client.util.impl.EntityUtil;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import java.util.ArrayList;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

public class PredictDoubleHand extends Module {

    public DragSetting delay;
    public MultipleSetting triggers;
    public FloatSetting sensitivity;
    public BooleanSetting ignorWhenShield;
    public TimerUtil timer;

    public PredictDoubleHand() {
        super("Predict Double Hand", Category.Combat, "Predicts when you will pop and mainhand totems for you");
        delay = setting("Delay", 30.0f, 50.0f, 0.0f, 300.0f).description("Delay between each action");
        triggers = setting("Triggers", new String[]{"Anchors", "Crystals", "Obsidian", "Sword Pop", "Pearl"}, new Boolean[]{true, true, true, true, true}).description("Choose what will trigger a double hand");
        sensitivity = setting("Sensitivity", 1.0f, 0.5f, 1.5f);
        ignorWhenShield = setting("Ignore When Shield", true).description("Will not double hand if you are using a shield");
        timer = new TimerUtil();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (timer.delay(delay)) {
            delay.setValue();
            timer.reset();
            Item item = Items.TOTEM_OF_UNDYING;
            Integer n = InventoryUtil.INSTANCE.findItemInHotbar(item);
            if (n == null) {
                return;
            }
            PlayerEntity playerEntity = EntityUtil.INSTANCE.getPlayer();
            if (playerEntity == null) {
                return;
            }
            PlayerEntity playerEntity2 = playerEntity;
            ClientPlayerEntity clientPlayerEntity = this.getMc().player;
            if (clientPlayerEntity.distanceTo(playerEntity2) > 10.0f) {
                return;
            }
            if (ignorWhenShield.getObject()) {
                Item item2 = Items.SHIELD;
                if (InventoryUtil.INSTANCE.isHoldingItem(item2)) {
                    ClientPlayerEntity clientPlayerEntity2 = this.getMc().player;
                    if (clientPlayerEntity2.isUsingItem()) {
                        return;
                    }
                }
            }
            if (triggers.getValue("Anchors")) {
                ArrayList<BlockPos> arrayList = BlockUtil.INSTANCE.method1010(5, arg_0 -> Math.sqrt(playerEntity.squaredDistanceTo(((BlockPos)arg_0).toCenterPos())) < 5.0 && getMc().world.getBlockState((BlockPos)arg_0).getBlock() == Blocks.RESPAWN_ANCHOR && getMc().world.getBlockState((BlockPos)arg_0).get(Properties.CHARGES) == 0);
                for (BlockPos blockPos : arrayList) {
                    double d = Prestige.Companion.getDamageManager().anchorDamage(getMc().player, blockPos.toCenterPos()) / 2 * sensitivity.getObject();
                    if (playerEntity2.getHealth() + getMc().player.getAbsorptionAmount() - d < 0) {
                        InventoryUtil.INSTANCE.setCurrentSlot(n);
                        return;
                    }
                }
            }
            if (triggers.getValue("Crystals")) {
                for (Entity object : this.getMc().world.getEntities()) {
                    if (object instanceof EndCrystalEntity) {
                        double d = Prestige.Companion.getDamageManager().crystalDamage(getMc().player, object.getBlockPos().toCenterPos()) / 2.0f * sensitivity.getObject();
                        if (playerEntity2.getHealth() + getMc().player.getAbsorptionAmount() - d < 0) {
                            InventoryUtil.INSTANCE.setCurrentSlot(n);
                            return;
                        }
                    }
                }
            }
            if (triggers.getValue("Obsidian")) {
                ArrayList<BlockPos> arrayList = BlockUtil.INSTANCE.method1010(5, arg_0 -> ((BlockPos)arg_0).getY() < getMc().player.getY() && Math.sqrt(playerEntity.squaredDistanceTo(((BlockPos)arg_0).toCenterPos())) < 5.0 && getMc().world.getBlockState((BlockPos)arg_0).getBlock() == Blocks.OBSIDIAN && getMc().world.getBlockState(((BlockPos)arg_0).up()).getBlock() == Blocks.AIR);
                for (BlockPos blockPos : arrayList) {
                    DamageManager damageManager = Prestige.Companion.getDamageManager();
                    double d = damageManager.crystalDamage(getMc().player, blockPos.up().toCenterPos()) / 2 * sensitivity.getObject();
                    if (playerEntity2.getHealth() + getMc().player.getAbsorptionAmount() - d < 0) {
                        InventoryUtil.INSTANCE.setCurrentSlot(n);
                        return;
                    }
                }
            }
            if (triggers.getValue("Sword Pop") && (playerEntity2.getMainHandStack().getItem() instanceof SwordItem || playerEntity2.getMainHandStack().getItem() instanceof AxeItem) && playerEntity2.getHealth() + this.getMc().player.getAbsorptionAmount() < 1.0f) {
                InventoryUtil.INSTANCE.setCurrentSlot(n);
            }
        }
    }

    @EventListener
    public void event(PacketSendEvent packetSendEvent) {
        if (triggers.getValue("Pearl") && packetSendEvent.getPacket() instanceof PlayerInteractItemC2SPacket) {
            if (getMc().player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
                Item item = Items.TOTEM_OF_UNDYING;
                Integer n = InventoryUtil.INSTANCE.findItemInHotbar(item);
                if (n == null) {
                    return;
                }
                InventoryUtil.INSTANCE.setCurrentSlot(n);
            }
        }
    }


    static boolean method703(PredictDoubleHand predictDoubleHand, PlayerEntity playerEntity, BlockPos blockPos) {
        return blockPos.getY() < predictDoubleHand.getMc().player.getY() && Math.sqrt(playerEntity.squaredDistanceTo(blockPos.toCenterPos())) < 5.0 && predictDoubleHand.getMc().world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN && predictDoubleHand.getMc().world.getBlockState(blockPos.up()).getBlock() == Blocks.AIR;
    }
}