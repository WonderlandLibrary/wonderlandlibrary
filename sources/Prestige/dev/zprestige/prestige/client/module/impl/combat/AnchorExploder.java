package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AnchorExploder extends Module {

    public DragSetting delay;
    public IntSetting switchTo;
    public TimerUtil timer;

    public AnchorExploder() {
        super("Anchor Exploder", Category.Combat, "Explodes anchors with glowstone in them when looking at them");
        delay = setting("Delay", 30, 50, 0, 200).description("Delay between each action");
        switchTo = setting("Switch To", 1, 1, 9).description("Slot that will be switched to to explode the anchor");
        timer = new TimerUtil();
    }

    @EventListener
    public void event(MoveEvent event) {
        if (getMc().currentScreen == null || getMc().isWindowFocused() || timer.delay(delay)) {
            if (InventoryUtil.INSTANCE.isHoldingItem(Items.SHIELD) && getMc().player.isUsingItem()) {
                return;
            }
            HitResult result = getMc().crosshairTarget;
            if (result != null && result.getType() == HitResult.Type.BLOCK && result instanceof BlockHitResult blockHitResult) {
                BlockState blockState = getMc().world.getBlockState(blockHitResult.getBlockPos());
                if (blockState.getBlock() == Blocks.RESPAWN_ANCHOR && blockState.get(Properties.CHARGES) != 0) {
                    if (InventoryUtil.INSTANCE.isHoldingItem(Items.GLOWSTONE)) {
                        InventoryUtil.INSTANCE.setCurrentSlot(switchTo.getObject() - 1);
                        delay.setValue();
                        timer.reset();
                        return;
                    }
                    getMc().interactionManager.interactBlock(getMc().player, Hand.MAIN_HAND, blockHitResult);
                    getMc().player.swingHand(Hand.MAIN_HAND);
                    delay.setValue();
                    timer.reset();
                }
            } else {
                delay.setValue();
            }
        }
    }
}
