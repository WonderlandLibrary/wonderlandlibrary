package me.dev.legacy.features.modules.combat;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.*;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class SelfAnvil extends Module {
    public SelfAnvil() {
        super("SelfAnvil", "Drops anvil on you.", Module.Category.COMBAT, true, false, false);
    }

    Setting<Integer> ammount = this.register(new Setting<Integer>("Ammount", 1, 1, 2));
    Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));

    private int placedAmmount;

    @Override
    public void onEnable() {
        this.placedAmmount = 0;
        if (InventoryUtil.findHotbarBlock(BlockAnvil.class) == -1 || PlayerUtil.findObiInHotbar() == -1) {
            this.disable();
        }
    }

    @Override
    public void onTick() {
        EntityPlayer target =  mc.player;
        if (target == null || placedAmmount >= ammount.getValue()) return;
        BlockPos anvilPos = EntityUtil.getFlooredPos(target).up(2);
        if (BlockUtil.canPlaceBlock(anvilPos)) {
            placeAnvil(anvilPos);
            placedAmmount++;
        } else {
            placeObi(anvilPos.down().east());
            placeObi(anvilPos.east());
        }
    }

    private void placeAnvil(BlockPos pos) {
        int old = mc.player.inventory.currentItem;
        this.switchToSlot(InventoryUtil.findHotbarBlock(BlockAnvil.class));
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);
        this.switchToSlot(old);
        toggle();
    }

    private void placeObi(BlockPos pos) {
        int old = mc.player.inventory.currentItem;
        this.switchToSlot(PlayerUtil.findObiInHotbar());
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);
        this.switchToSlot(old);
    }


    private void switchToSlot(final int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }
}
