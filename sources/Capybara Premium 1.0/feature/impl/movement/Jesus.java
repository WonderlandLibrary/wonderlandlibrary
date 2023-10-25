package fun.expensive.client.feature.impl.movement;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventLiquidSolid;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.Setting;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.movement.MovementUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;


public class Jesus extends Feature {
    public static ListSetting mode = new ListSetting("Jesus Mode", "Matrix new", () -> {
        return true;
    }, new String[]{"Matrix new", "Matrix old", "Matrix Zoom", "NCP"});
    public static NumberSetting speed = new NumberSetting("Speed", 0.65F, 0.1F, 10.0F, 0.01F, () -> {
        return !mode.currentMode.equals("NCP") && !mode.currentMode.equals("Matrix new") && !mode.currentMode.equals("Matrix old");
    });
    public static NumberSetting NCPSpeed = new NumberSetting("NCP Speed", 0.25F, 0.01F, 0.5F, 0.01F, () -> {
        return mode.currentMode.equals("NCP");
    });
    public static BooleanSetting useTimer = new BooleanSetting("Use Timer", false, () -> {
        return true;
    });
    private final NumberSetting timerSpeed = new NumberSetting("Timer Speed", 1.05F, 1.01F, 1.5F, 0.01F, () -> {
        return useTimer.getBoolValue();
    });
    private int waterTicks = 0;

    public Jesus() {
        super("Jesus", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u043e\u0434\u0438\u0442\u044c \u043f\u043e \u0432\u043e\u0434\u0435", FeatureCategory.Movement);
        this.addSettings(new Setting[]{mode, speed, NCPSpeed, useTimer, this.timerSpeed});
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        this.waterTicks = 0;
        super.onDisable();
    }

    @EventTarget
    public void onLiquidBB(EventLiquidSolid event) {
        if (mode.currentMode.equals("NCP")) {
            event.setCancelled(true);
        } else if (mode.currentMode.equalsIgnoreCase("Matrix new")) {
            if (mc.player.isInWater()) {
                return;
            }

            if (mc.player.posY > (double)event.getPos().getY() + 0.999991) {
                event.setColision(Block.FULL_BLOCK_AABB.expand(0.0, -9.0E-6, 0.0));
            }
        }

    }

    private boolean isWater() {
        BlockPos bp1 = new BlockPos(mc.player.posX - 0.5, mc.player.posY - 0.5, mc.player.posZ - 0.5);
        BlockPos bp2 = new BlockPos(mc.player.posX - 0.5, mc.player.posY - 0.5, mc.player.posZ + 0.5);
        BlockPos bp3 = new BlockPos(mc.player.posX + 0.5, mc.player.posY - 0.5, mc.player.posZ + 0.5);
        BlockPos bp4 = new BlockPos(mc.player.posX + 0.5, mc.player.posY - 0.5, mc.player.posZ - 0.5);
        return mc.player.world.getBlockState(bp1).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp2).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp3).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp4).getBlock() == Blocks.WATER || mc.player.world.getBlockState(bp1).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp2).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp3).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp4).getBlock() == Blocks.LAVA;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        this.setSuffix(mode.getCurrentMode());
        BlockPos blockPos = new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 0.1, Minecraft.getMinecraft().player.posZ);
        Block block = Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock();
        if (useTimer.getBoolValue()) {
            mc.timer.timerSpeed = this.timerSpeed.getNumberValue();
        }

        BlockPos blockPos2;
        Block block2;
        if (mode.currentMode.equalsIgnoreCase("Matrix new")) {
            blockPos2 = new BlockPos(mc.player.posX, mc.player.posY - 0.02, mc.player.posZ);
            block2 = mc.world.getBlockState(blockPos2).getBlock();
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.5, mc.player.posZ)).getBlock() == Blocks.WATER && mc.player.onGround) {
                mc.player.motionY = 0.2;
            }

            if (mc.player.isInWater()) {
                mc.player.setVelocity(0.0, 0.0, 0.0);
                mc.player.motionY = mc.world.handleMaterialAcceleration(mc.player.getEntityBoundingBox().expand(0.0, -1.1, 0.0).contract(0.06), Material.WATER, mc.player) ? 0.12 : mc.player.motionY + 0.18;
            }

            if (block2 instanceof BlockLiquid && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0311, mc.player.posZ)).getBlock() instanceof BlockAir && !mc.player.onGround) {
                mc.player.motionY = 0.0;
                mc.player.fallDistance = 1.0F;
                if (this.waterTicks == 1) {
                    event.setPosY(event.getPosY() - 0.0114);
                    if (MovementUtils.isMoving()) {
                        MovementUtils.setSpeed((float) 1.1);
                    }

                    this.waterTicks = 0;
                } else {
                    this.waterTicks = 1;
                }
            }
        } else if (mode.currentMode.equalsIgnoreCase("NCP")) {
            if (block instanceof BlockLiquid && !Minecraft.getMinecraft().player.onGround && Minecraft.getMinecraft().world.getBlockState(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ)).getBlock() == Blocks.WATER) {
                Minecraft.getMinecraft().player.motionX = 0.0;
                Minecraft.getMinecraft().player.motionY = 0.035999998450279236;
                Minecraft.getMinecraft().player.motionZ = 0.0;
            }

            if (this.isWater() && block instanceof BlockLiquid) {
                if (this.timerHelper.hasReached(400.0F)) {
                    mc.player.motionY = 0.12;
                    this.timerHelper.reset();
                }

                mc.player.onGround = false;
                mc.player.isAirBorne = true;
                MovementUtils.setSpeed((float) NCPSpeed.getNumberValue());
                event.setPosY(mc.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.042 : event.getPosY() - 0.053);
                event.setOnGround(false);
            }
        } else if (mode.currentMode.equalsIgnoreCase("Matrix old")) {
            blockPos2 = new BlockPos(mc.player.posX, mc.player.posY - 0.02, mc.player.posZ);
            block2 = mc.world.getBlockState(blockPos2).getBlock();
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.5, mc.player.posZ)).getBlock() == Blocks.WATER && mc.player.onGround) {
                mc.player.motionY = 0.2;
            }

            if (block2 instanceof BlockLiquid && !mc.player.onGround) {
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.12, mc.player.posZ)).getBlock() == Blocks.WATER) {
                    mc.player.motionX = 0.0;
                    mc.player.motionY = 0.10000000149011612;
                    mc.player.motionZ = 0.0;
                } else {
                    MovementUtils.setSpeed(1.100000023841858F);
                    mc.player.motionY = -0.10000000149011612;
                }

                if (mc.player.isCollidedHorizontally) {
                    mc.player.motionY = 0.0;
                }
            }

            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.2, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
                mc.player.motionY = 0.18;
            }
        } else if (mode.currentMode.equalsIgnoreCase("Matrix Zoom")) {
            if (mc.gameSettings.keyBindForward.pressed && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.9999999747378752, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
                mc.player.motionY = 0.9;
            }

            if (block instanceof BlockLiquid && !Minecraft.getMinecraft().player.onGround) {
                if (Minecraft.getMinecraft().world.getBlockState(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ)).getBlock() == Blocks.WATER) {
                    Minecraft.getMinecraft().player.motionY = 0.035999998450279236;
                    Minecraft.getMinecraft().player.motionX = 0.0;
                    Minecraft.getMinecraft().player.motionZ = 0.0;
                } else {
                    MovementUtils.setSpeed((float) speed.getNumberValue());
                }

                if (Minecraft.getMinecraft().player.isCollidedHorizontally) {
                    Minecraft.getMinecraft().player.motionY = 0.2;
                }
            }
        }

    }
}
