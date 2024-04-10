package info.sigmaclient.sigma.utils;

import net.minecraft.util.math.BlockPos;

import static info.sigmaclient.sigma.modules.Module.mc;

public class UnusedRtation {
    public static boolean shouldBuild(BlockPos targetBlock) {
        double add1 = 1.282;
        double add2 = 0.282;
        double x = mc.player.getPosX();
        double z = mc.player.getPosZ();
        double maX = (double) targetBlock.getX() + add1;
        double miX = (double) targetBlock.getX() - add2;
        double maZ = (double) targetBlock.getZ() + add1;
        double miZ = (double) targetBlock.getZ() - add2;
        return x > maX || x < miX || z > maZ || z < miZ;
    }

//        switch (rotation.getValue()) {
//        case "Intave":
//            BlockPos bbbb = new BlockPos(mc.player.getPosX(), mc.player.getPosY() - 0.5, mc.player.getPosZ());
//            if (mc.world.getBlockState(bbbb).getBlock() instanceof BlockAir) {
//                this.rots[0] = mc.player.rotationYaw - 179f;
//                this.rots[1] = 80.4f;
//            } else {
//                this.rots[1] = mc.player.lastReportedPitch;
//            }
//            float yaw = mc.player.rotationYaw - 179f;
//            this.getYawBasedPitch(yaw);
//            break;
//        case "Test":
//            BlockPos bbbb2 = new BlockPos(mc.player.getPosX(), mc.player.getPosY() - 0.5, mc.player.getPosZ());
//            this.rots[0] = MathHelper.wrapAngleTo180_float(mc.player.rotationYaw - 180.1337f + RandomUtil.nextFloat(0, 2));
//            if(mc.player.ticksExisted % 4 == 0 || mc.world.getBlockState(bbbb2).getBlock() instanceof BlockAir) {
//                this.getYawBasedPitch(this.rots[0]);
//            }
//            break;
//        case "NCP":
//            this.rots = new float[2];
//            this.rots[0] = mc.player.rotationYaw + 179;
//            this.rots[0] += (mc.player.ticksExisted % 20 - 10) / 10f;
//            this.rots[0] = MathHelper.wrapAngleTo180_float(this.rots[0]);
//            getYawBasedPitch(this.rots[0]);
//            this.rots[1] += RandomUtil.nextFloat(0, 0.111);
//            break;
//        case "Mineblaze":
//            this.rots = new float[2];
//            this.rots[0] = mc.player.rotationYaw + 180;
//            this.rots[0] += (mc.player.ticksExisted % 20 - 10) / 10f + RandomUtil.nextFloat(0, 0.111);
//            this.rots[0] = MathHelper.wrapAngleTo180_float(this.rots[0]);
//            getYawBasedPitch(this.rots[0]);
//            this.rots[1] += RandomUtil.nextFloat(0, 0.111);
//            this.rots[1] = Math.min(Math.max(this.rots[1], -90), 90);
//            break;
//        case "OnlyPitch":
//            this.rots = new float[2];
//            this.rots[0] = mc.player.rotationYaw + 180;
//            this.rots[0] = MathHelper.wrapAngleTo180_float(this.rots[0]);
//            getYawBasedPitch(this.rots[0]);
//            this.rots[1] = Math.min(Math.max(this.rots[1], -90), 90);
//            // bypass check
//            break;
//        case "Static":
//            this.rots[0] = mc.player.rotationYaw + 179;
//            this.rots[1] = 80.31f;
//            break;
//        case "AAC":
//            calcRotation = scaffoldRots(
//                    blockPos.getPosition().getX() + 0.5 + blockPos.getFacing().getFrontOffsetX() / 2f,
//                    blockPos.getPosition().getY() + 0.5 + blockPos.getFacing().getFrontOffsetY() / 2f,
//                    blockPos.getPosition().getZ() + 0.5 + blockPos.getFacing().getFrontOffsetZ() / 2f,
//                    mc.player.lastReportedYaw,
//                    mc.player.lastReportedPitch,
//                    this.yawSpeed.getValue().floatValue(),
//                    this.pitchSpeed.getValue().floatValue(),
//                    false);
//            this.rots = calcRotation;
//            // do find nearest pitch
//            // bypass check
//            break;
//        case "Grimac":
//            calcRotation = scaffoldRots(
//                    blockPos.getPosition().getX() + 0.5 + blockPos.getFacing().getFrontOffsetX() / 2f,
//                    blockPos.getPosition().getY() + 0.5 + blockPos.getFacing().getFrontOffsetY() / 2f,
//                    blockPos.getPosition().getZ() + 0.5 + blockPos.getFacing().getFrontOffsetZ() / 2f,
//                    mc.player.lastReportedYaw,
//                    mc.player.lastReportedPitch,
//                    this.yawSpeed.getValue().floatValue(),
//                    this.pitchSpeed.getValue().floatValue(),
//                    false);
//            this.rots = calcRotation;
//            this.rots[0] = mc.player.rotationYaw + 180;
//            break;
//        case "CenterR":
//            calcRotation = scaffoldRots(
//                    blockPos.getPosition().getX() + 0.5 + blockPos.getFacing().getFrontOffsetX() / 2f,
//                    blockPos.getPosition().getY() + 0.5 + blockPos.getFacing().getFrontOffsetY() / 2f,
//                    blockPos.getPosition().getZ() + 0.5 + blockPos.getFacing().getFrontOffsetZ() / 2f,
//                    mc.player.lastReportedYaw,
//                    mc.player.lastReportedPitch,
//                    this.yawSpeed.getValue().floatValue(),
//                    this.pitchSpeed.getValue().floatValue(),
//                    true);
//            this.rots = calcRotation;
//            this.rots[0] += RandomUtil.nextFloat(0, 0.111);
//            this.rots[1] += RandomUtil.nextFloat(0, 0.111);
//            break;
//    }
}
