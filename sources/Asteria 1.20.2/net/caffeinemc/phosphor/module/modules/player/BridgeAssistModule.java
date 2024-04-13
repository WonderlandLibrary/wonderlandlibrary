package net.caffeinemc.phosphor.module.modules.player;

import com.google.common.collect.Streams;
import net.caffeinemc.phosphor.api.event.events.TickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;

import java.util.stream.Stream;

public class BridgeAssistModule extends Module {
    public final NumberSetting minAngle = new NumberSetting("Min Angle", this, 50, 45, 75, 1);
    public final NumberSetting checkHeight = new NumberSetting("Min Height", this, 5, 0, 10, 1);
    private final NumberSetting edgeDistance = new NumberSetting("Edge Distance", this, 0.2, 0.2, 0.3, 0.01);

    public BridgeAssistModule() {
        super("BridgeAssist", "Automatically shifts on edge of block when you hold block in hand.", Category.PLAYER);
    }

    private boolean wasActivated;

    @Override
    public void onEnable() {
        wasActivated = false;
    }

    private void reset() {
        if (mc.options.sneakKey.isPressed() && wasActivated) {
            mc.options.sneakKey.setPressed(false);
            wasActivated = false;
        }
    }

    public boolean checkHands() {
        return mc.player.getMainHandStack().getItem() instanceof BlockItem || mc.player.getOffHandStack().getItem() instanceof BlockItem;
    }

    private Box setMinY(Box box, double minY) {
        return new Box(box.minX, box.minY - minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (mc.player == null) return;

        if (mc.currentScreen != null) return;

        if (!checkHands()) {
            reset();
            return;
        }

        if (!mc.player.isOnGround()) {
            reset();
            return;
        }

        if (MathHelper.wrapDegrees(mc.player.getPitch()) < minAngle.getFValue()) {
            reset();
            return;
        }

        Box box = mc.player.getBoundingBox();
        Box adjustedBox = setMinY(box.offset(0, -0.5, 0).expand(-edgeDistance.getValue(), 0, -edgeDistance.getValue()), checkHeight.getValue());

        Stream<VoxelShape> blockCollisions = Streams.stream(mc.world.getBlockCollisions(mc.player, adjustedBox));

        if (blockCollisions.findAny().isPresent()) {
            if (mc.options.sneakKey.isPressed() && wasActivated) {
                mc.options.sneakKey.setPressed(false);
                wasActivated = false;
            }
        } else {
            if (!mc.options.sneakKey.isPressed()) {
                if (mc.options.sprintKey.isPressed()) mc.options.sprintKey.setPressed(false);
                mc.options.sneakKey.setPressed(true);
                wasActivated = true;
            }
        }
    }
}
