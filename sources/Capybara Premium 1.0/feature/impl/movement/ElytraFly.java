package fun.expensive.client.feature.impl.movement;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventSendPacket;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.Setting;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.movement.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

public class ElytraFly extends Feature {
    private final BooleanSetting boost = new BooleanSetting("Boost", false, () -> Boolean.valueOf(true));

    private final NumberSetting speedValue = new NumberSetting("Speed Value", 0.25F, 0.05F, 0.25F, 0.01F, () -> Boolean.valueOf(!this.boost.getBoolValue()));

    private int boostTicks;

    public ElytraFly() {
        super("ElytraFly", "Позволяет летать с элитрами в инвентаре", FeatureCategory.Movement);
        addSettings(new Setting[] { (Setting)this.boost, (Setting)this.speedValue });
    }

    public void onDisable() {
        this.boostTicks = 0;
        super.onDisable();
    }

    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer cPacketPlayer = (CPacketPlayer)event.getPacket();
            cPacketPlayer.onGround = false;
        }
    }

    public boolean attackCheck() {
        return (mc.player.isInLava() || mc.player.isInWater() || mc.player.isOnLadder() || mc.player.isInWeb);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (InventoryHelper.getItemIndex(Items.ELYTRA) == -1 || attackCheck())
            return;
        int emptySlot = InventoryHelper.getItemIndex(Item.getItemFromBlock(Blocks.AIR));
        if (emptySlot != -1 && mc.player.inventory.getItemStack().getItem() == Items.ELYTRA && InventoryHelper.getItemIndex(Items.ELYTRA) == -1)
            mc.playerController.windowClick(0, (emptySlot < 9) ? (emptySlot + 36) : emptySlot, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
        this.boostTicks++;
        if (mc.player.inventory.getItemStack().getItem() instanceof ItemArmor && mc.player.inventory.armorItemInSlot(2).isEmpty()) {
            ItemArmor armor = (ItemArmor)mc.player.inventory.getItemStack().getItem();
            if (armor.armorType == EntityEquipmentSlot.CHEST)
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
        }
        if (mc.player.inventory.getItemStack().getItem() instanceof ItemArmor && !mc.player.inventory.armorItemInSlot(2).isEmpty()) {
            mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
            int emptySlot2 = InventoryHelper.getItemIndex(Item.getItemFromBlock(Blocks.AIR));
            if (emptySlot2 != -1 && mc.player.inventory.getItemStack().getItem() == Items.ELYTRA && InventoryHelper.getItemIndex(Items.ELYTRA) == -1)
                mc.playerController.windowClick(0, (emptySlot2 < 9) ? (emptySlot2 + 36) : emptySlot2, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
        }
        if (mc.player.ticksExisted % 25 == 0)
            disabler();
        if (this.boost.getBoolValue() && this.boostTicks > 25 && MovementUtils.getSpeed() < 9.0F) {
            mc.player.motionX *= 1.1D;
            mc.player.motionZ *= 1.1D;
        } else {
            mc.player.jumpMovementFactor = this.speedValue.getNumberValue();
        }
        MovementUtils.setSpeed(MovementUtils.getSpeed());
        if (!MovementUtils.isMoving())
            this.boostTicks = 0;
        if (mc.gameSettings.keyBindJump.isKeyDown() || mc.player.isCollidedHorizontally) {
            mc.player.motionY = 0.25D;
        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY = -0.30000001192092896D;
        } else {
            mc.player.motionY = (mc.player.ticksExisted % 2 != 0) ? -0.05D : 0.05D;
        }
    }

    private void disabler() {
        int elytra = InventoryHelper.getItemIndex(Items.ELYTRA);
        if (mc.player.inventory.getItemStack().getItem() != Items.ELYTRA)
            mc.playerController.windowClick(0, (elytra < 9) ? (elytra + 36) : elytra, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
        mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
        mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
        mc.playerController.windowClick(0, (elytra < 9) ? (elytra + 36) : elytra, 1, ClickType.PICKUP, (EntityPlayer)mc.player);
    }
}
