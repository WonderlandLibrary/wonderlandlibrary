package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventBlockBounds;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventPushBlock;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

/*
 * Created by cool1 on 1/17/2017.
 */

public class Phase extends Module {

    private int delay;

    private String PM = "MODE";
    private String DIST = "DIST";

    public Phase(ModuleData data) {
        super(data);
        settings.put(PM, new Setting<>(PM, new Options("Phase Mode", "Normal", new String[]{"Spider", "Skip", "Normal", "FullBlock", "Silent", "HCF", "Debug"}), "Phase exploit method."));
        settings.put(DIST, new Setting<>(DIST, 2, "Distance for HCF mode.", 0.1, 0.1, 3));
    }

    @Override
    @RegisterEvent(events = {EventBlockBounds.class, EventUpdate.class, EventPushBlock.class, EventPacket.class})
    public void onEvent(Event event) {
        String currentPhase = ((Options) settings.get(PM).getValue()).getSelected();
        this.setSuffix(currentPhase);
        if (event instanceof EventPushBlock) {
            event.setCancelled(true);
        }
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            if (ep.isOutgoing()) {
                if (isInsideBlock()) {
                    return;
                }
                final double multiplier = 0.2;
                final double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
                final double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
                final double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
                final double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
                if (mc.thePlayer.isCollidedHorizontally && ep.getPacket() instanceof C03PacketPlayer) {
                    delay++;
                    final C03PacketPlayer player = (C03PacketPlayer) ep.getPacket();
                    if (this.delay >= 5) {
                        player.x += x;
                        player.z += z;
                        --player.y;
                        this.delay = 0;
                    }
                }
            }
        }
        if (event instanceof EventBlockBounds) {
            EventBlockBounds ebb = (EventBlockBounds) event;
            assert mc.thePlayer != null;
            if (currentPhase.equalsIgnoreCase("HCF")) {
                if ((ebb.getBounds() != null) && (isInsideBlock() || (ebb.getBounds().maxY > mc.thePlayer.boundingBox.minY)) && (mc.thePlayer.isSneaking())) {
                    ebb.setCancelled(true);
                }
            } else if (currentPhase.equalsIgnoreCase("Debug") && ebb.getBounds() != null) {
                ebb.setCancelled(true);
            } else if ((ebb.getBounds() != null) && (ebb.getBounds().maxY > mc.thePlayer.boundingBox.minY)) {
                ebb.setCancelled(true);
            }
        }
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre() && currentPhase == "HCF" && isInsideBlock() && mc.thePlayer.isSneaking()) {
                final float yaw = mc.thePlayer.rotationYaw;
                float sped = ((Number) settings.get(DIST).getValue()).floatValue();
                mc.thePlayer.boundingBox.offsetAndUpdate(sped * Math.cos(Math.toRadians(yaw + 90.0f)), 0.0, sped * Math.sin(Math.toRadians(yaw + 90.0f)));
            }
            if (em.isPost()) {
                double multiplier = 0.3;
                final double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
                final double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
                if (currentPhase.equals("FullBlock")) {
                    multiplier = 0.31;
                }
                final double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
                final double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
                switch (currentPhase) {
                    case "FullBlock": {
                        if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && !isInsideBlock()) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
                            for (int i = 1; i < 11; ++i) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, Double.MAX_VALUE * i, mc.thePlayer.posZ, false));
                            }
                            final double posX = mc.thePlayer.posX;
                            final double posY = mc.thePlayer.posY;
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY - (PlayerUtil.isOnLiquid() ? 9000.0 : 0.1), mc.thePlayer.posZ, false));
                            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                            break;
                        }
                        break;
                    }
                    case "Normal": {
                        if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && !isInsideBlock()) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
                            final double posX2 = mc.thePlayer.posX;
                            final double posY2 = mc.thePlayer.posY;
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX2, posY2 - (PlayerUtil.isOnLiquid() ? 9000.0 : 0.09), mc.thePlayer.posZ, false));
                            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                            break;
                        }
                        break;
                    }
                    case "Silent": {
                        if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && !isInsideBlock()) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
                            for (int i = 1; i < 10; ++i) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 8.988465674311579E307, mc.thePlayer.posZ, false));
                            }
                            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                            break;
                        }
                        break;
                    }
                    case "Skip": {
                        if (!mc.thePlayer.isCollidedHorizontally) {
                            break;
                        }
                        mc.thePlayer.motionX *= 0.5;
                        mc.thePlayer.motionZ *= 0.5;
                        final double[] OPOP = {-0.02500000037252903, -0.028571428997176036, -0.033333333830038704, -0.04000000059604645, -0.05000000074505806, -0.06666666766007741, -0.10000000149011612, 0.0, -0.20000000298023224, -0.04000000059604645, -0.033333333830038704, -0.028571428997176036, -0.02500000037252903};
                        for (int j = 0; j < OPOP.length; ++j) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + OPOP[j], mc.thePlayer.posZ, false));
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x * j, mc.thePlayer.boundingBox.minY, mc.thePlayer.posZ + z * j, false));
                        }
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, mc.thePlayer.posZ, false));
                        break;
                    }
                    case "Spider": {
                        if (!isInsideBlock()) {
                            break;
                        }
                        mc.thePlayer.posY += 0.1;
                        mc.thePlayer.motionY = 0.065;
                        mc.thePlayer.resetHeight();
                        break;
                    }
                    case "Debug": {

                    }
                }
            }
        }
    }

    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if ((block != null) && (!(block instanceof BlockAir))) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if ((block instanceof BlockHopper)) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null) {
                            if (mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
