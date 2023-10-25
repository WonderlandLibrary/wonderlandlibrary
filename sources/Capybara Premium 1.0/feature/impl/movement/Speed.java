package fun.expensive.client.feature.impl.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.Helper;
import fun.rich.client.utils.math.TimerHelper;
import fun.rich.client.utils.movement.MovementUtils;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class Speed extends Feature {
    public static float ticks = 0;
    public TimerHelper timerHelper = new TimerHelper();
    private final ListSetting speedMode = new ListSetting("Speed Mode", "Matrix", () -> true, "Matrix", "Matrix New", "Damage", "Matrix Elytra");

    public Speed() {
        super("Speed", "Увеличивает вашу скорость", FeatureCategory.Movement);
        addSettings(speedMode);

    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String modezxc = speedMode.getOptions();
        if (modezxc.equalsIgnoreCase("Matrix Elytra")) {
            if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid()) {
                return;
            }

            int eIndex = -1;
            int elytraCount = 0;

            for (int i = 0; i < 45; i++) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                    eIndex = i;
                }
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA) {
                    elytraCount++;
                }
            }
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            if (mc.player.ticksExisted % 20 == 0) {
                mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, mc.player);
            }

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            mc.player.motionZ *= 1.8D;
            mc.player.motionX *= 1.8D;
            MovementUtils.strafe();
            if (elytraCount == 0 && eIndex == -1) {
                if (mc.player.getHeldItemOffhand().getItem() != Items.ELYTRA) {
                    NotificationRenderer.queue("§6Matrix Elytra", "§cВозьмите элитры в инвентарь!", 6, NotificationMode.WARNING);
                    toggle();
                }
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix")) {
            if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid()) {
                return;
            }

            final double x = mc.player.posX;
            final double y = mc.player.posY;
            final double z = mc.player.posZ;
            final double yaw = mc.player.rotationYaw * 0.017453292D;
            if (mc.player.onGround && !mc.gameSettings.keyBindJump.pressed) {
                mc.player.jump();
                mc.timer.timerSpeed = 1.3f;
                this.ticks = 11;
            } else if (this.ticks < 11) {
                ++this.ticks;
            }
            if (mc.player.motionY == -0.4448259643949201D && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.9D, mc.player.posZ)).getBlock() != Blocks.AIR) {
                mc.player.motionX *= 2.05D;
                mc.player.motionZ *= 2.05D;
                mc.player.setPosition(x - Math.sin(yaw) * 0.003D, y, z + Math.cos(yaw) * 0.003D);
            }
            this.ticks = 0;

        } else if (mode.equalsIgnoreCase("Matrix New")) {
            if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid()) {
                return;
            }

            if (mc.player.onGround && !mc.gameSettings.keyBindJump.pressed) {
                mc.player.jump();
            }
            if (mc.player.ticksExisted % 3 == 0) {
                mc.timer.timerSpeed = 1.3f;
            } else {
                mc.timer.timerSpeed = 1.f;
            }
            if (mc.player.motionY == -0.4448259643949201D && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.9D, mc.player.posZ)).getBlock() != Blocks.AIR) {
                mc.player.jumpMovementFactor = 0.05F;
                if(mc.player.ticksExisted % 2 == 0) {
                   mc.player.motionX *= 2.D;
                    mc.player.motionZ *= 2.D;
                } else {
                    MovementUtils.setMotion(MovementUtils.getSpeed() * 1 + (0.22f));

                }
            }
        } else if (mode.equalsIgnoreCase("Damage")) {
            if (MovementUtils.isMoving()) {
                if (mc.player.onGround) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5D / 24.5D, 0, Math.cos(MovementUtils.getAllDirection()) * 9.5D / 24.5D);
                    MovementUtils.strafe();
                } else if (mc.player.isInWater()) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5D / 24.5D, 0, Math.cos(MovementUtils.getAllDirection()) * 9.5D / 24.5D);
                    MovementUtils.strafe();
                } else if (!mc.player.onGround) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.11D / 24.5D, 0, Math.cos(MovementUtils.getAllDirection()) * 0.11D / 24.5D);
                    MovementUtils.strafe();
                } else {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.005D * MovementUtils.getSpeed(), 0, Math.cos(MovementUtils.getAllDirection()) * 0.005 * MovementUtils.getSpeed());
                    MovementUtils.strafe();

                }
            }
        }
    }

    private int findarmor() {
        for (int i = 0; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getStack() != null && mc.player.inventoryContainer.getSlot(i).getStack().getUnlocalizedName().contains("chestplate")) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onDisable() {

        mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}