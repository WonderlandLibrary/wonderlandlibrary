package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.hit.BlockHitResult;

public class AutoPickaxe extends Module {

    public DragSetting delay;
    public TimerUtil timer;
    public int originalSlot = -1;


    public AutoPickaxe() {
        super("Auto Pickaxe", Category.Misc, "Automatically switches to a pickaxe in your hotbar when mining the block below you");
        delay = setting("Delay", 30.0f, 50.0f, 0.0f, 300.0f).description("Delay between each action");
        timer = new TimerUtil();
    }

    @EventListener
    public void event(Render2DEvent event) {
        if (getMc().crosshairTarget instanceof BlockHitResult && getMc().world.getBlockState(getMc().player.getBlockPos().down()).isReplaceable()) {
            if (!idk()) {
                reset();
            }
            return;
        }
        if (getMc().world.getBlockState(getMc().player.getBlockPos().down()).getBlock() == Blocks.BEDROCK) {
            if (!idk()) {
                reset();
            }
            return;
        }
        if (getMc().player.getMainHandStack().getItem() instanceof PickaxeItem) {
            reset();
            return;
        }
        if (!timer.delay(delay)) {
            return;
        }
        int slot = InventoryUtil.INSTANCE.findItemInHotbar(Items.NETHERITE_PICKAXE);
        if (slot == 0) {
            slot = InventoryUtil.INSTANCE.findItemInHotbar(Items.DIAMOND_PICKAXE);
            if (slot == 0) {
                return;
            }
        }
        originalSlot = getMc().player.getInventory().selectedSlot;
        getMc().player.getInventory().selectedSlot = slot;
    }

    boolean idk() {
        if (originalSlot != -1) {
            if (timer.delay(this.delay)) {
                getMc().player.getInventory().selectedSlot = originalSlot;
                originalSlot = -1;
            }
            return true;
        }
        return false;
    }

    void reset() {
        this.timer.reset();
        this.delay.setValue();
    }
}