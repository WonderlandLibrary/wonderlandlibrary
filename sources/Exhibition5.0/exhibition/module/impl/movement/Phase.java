// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import exhibition.event.impl.EventPushBlock;
import exhibition.event.RegisterEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import exhibition.util.PlayerUtil;
import net.minecraft.network.Packet;
import exhibition.util.misc.ChatUtil;
import exhibition.event.impl.EventMotion;
import net.minecraft.util.AxisAlignedBB;
import exhibition.event.impl.EventBlockBounds;
import net.minecraft.network.play.client.C03PacketPlayer;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.Options;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Phase extends Module
{
    private int delay;
    private String PM;
    
    public Phase(final ModuleData data) {
        super(data);
        this.PM = "PHASEMODE";
        ((HashMap<String, Setting<Options>>)this.settings).put(this.PM, new Setting<Options>(this.PM, new Options("Phase Mode", "Normal", new String[] { "Spider", "Skip", "Normal", "FullBlock", "Silent", "HCF" }), "Phase exploit method."));
    }
    
    @RegisterEvent(events = { EventBlockBounds.class, EventMotion.class, EventPushBlock.class, EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        final String currentPhase = ((HashMap<K, Setting<Options>>)this.settings).get(this.PM).getValue().getSelected();
        this.setSuffix(currentPhase);
        if (event instanceof EventPacket) {
            final EventPacket ep = (EventPacket)event;
            if (ep.isOutgoing()) {
                if (this.isInsideBlock()) {
                    return;
                }
                final double multiplier = 0.2;
                final double mx = Math.cos(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
                final double mz = Math.sin(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
                final double x = Phase.mc.thePlayer.movementInput.moveForward * 0.2 * mx + Phase.mc.thePlayer.movementInput.moveStrafe * 0.2 * mz;
                final double z = Phase.mc.thePlayer.movementInput.moveForward * 0.2 * mz - Phase.mc.thePlayer.movementInput.moveStrafe * 0.2 * mx;
                if (Phase.mc.thePlayer.isCollidedHorizontally && ep.getPacket() instanceof C03PacketPlayer) {
                    ++this.delay;
                    final C03PacketPlayer player = (C03PacketPlayer)ep.getPacket();
                    if (this.delay >= 5) {
                        final C03PacketPlayer c03PacketPlayer = player;
                        c03PacketPlayer.x += x;
                        final C03PacketPlayer c03PacketPlayer2 = player;
                        c03PacketPlayer2.z += z;
                        final C03PacketPlayer c03PacketPlayer3 = player;
                        --c03PacketPlayer3.y;
                        this.delay = 0;
                    }
                }
            }
        }
        if (event instanceof EventBlockBounds) {
            final EventBlockBounds ebb = (EventBlockBounds)event;
            if (ebb.getBounds() != null && ebb.getBounds().maxY > Phase.mc.thePlayer.boundingBox.minY && Phase.mc.thePlayer.isSneaking()) {
                ebb.setBounds(null);
            }
            if (Phase.mc.thePlayer == null) {
                return;
            }
            Phase.mc.thePlayer.noClip = true;
            if (ebb.getPos().getY() > Phase.mc.thePlayer.posY + (this.isInsideBlock() ? 0 : 1)) {
                ebb.setBounds(null);
            }
            if (Phase.mc.thePlayer.isCollidedHorizontally && ebb.getPos().getY() > Phase.mc.thePlayer.boundingBox.minY - 0.4) {
                ebb.setBounds(null);
            }
        }
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre() && currentPhase == "HCF" && this.isInsideBlock() && Phase.mc.thePlayer.isSneaking()) {
                ChatUtil.printChat("HUH?");
                final float yaw = Phase.mc.thePlayer.rotationYaw;
                Phase.mc.thePlayer.boundingBox.offset(1.2 * Math.cos(Math.toRadians(yaw + 90.0f)), 0.0, 1.2 * Math.sin(Math.toRadians(yaw + 90.0f)));
            }
            if (em.isPost()) {
                double multiplier = 0.3;
                final double mx = Math.cos(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
                final double mz = Math.sin(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
                if (currentPhase.equals("FullBlock")) {
                    multiplier = 0.31;
                }
                final double x = Phase.mc.thePlayer.movementInput.moveForward * multiplier * mx + Phase.mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
                final double z = Phase.mc.thePlayer.movementInput.moveForward * multiplier * mz - Phase.mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
                final String s = currentPhase;
                switch (s) {
                    case "FullBlock": {
                        if (Phase.mc.thePlayer.isCollidedHorizontally && !Phase.mc.thePlayer.isOnLadder() && !this.isInsideBlock()) {
                            Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + z, false));
                            for (int i = 1; i < 11; ++i) {
                                Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Double.MAX_VALUE * i, Phase.mc.thePlayer.posZ, false));
                            }
                            final double posX = Phase.mc.thePlayer.posX;
                            final double posY = Phase.mc.thePlayer.posY;
                            Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY - (PlayerUtil.isOnLiquid() ? 9000.0 : 0.1), Phase.mc.thePlayer.posZ, false));
                            Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + z);
                            break;
                        }
                        break;
                    }
                    case "Normal": {
                        if (Phase.mc.thePlayer.isCollidedHorizontally && !Phase.mc.thePlayer.isOnLadder() && !this.isInsideBlock()) {
                            Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + z, false));
                            final double posX2 = Phase.mc.thePlayer.posX;
                            final double posY2 = Phase.mc.thePlayer.posY;
                            Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX2, posY2 - (PlayerUtil.isOnLiquid() ? 9000.0 : 0.09), Phase.mc.thePlayer.posZ, false));
                            Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + z);
                            break;
                        }
                        break;
                    }
                    case "Silent": {
                        if (Phase.mc.thePlayer.isCollidedHorizontally && !Phase.mc.thePlayer.isOnLadder() && !this.isInsideBlock()) {
                            Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + z, false));
                            for (int i = 1; i < 10; ++i) {
                                Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, 8.988465674311579E307, Phase.mc.thePlayer.posZ, false));
                            }
                            Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + z);
                            break;
                        }
                        break;
                    }
                    case "Skip": {
                        if (!Phase.mc.thePlayer.isCollidedHorizontally) {
                            break;
                        }
                        final EntityPlayerSP thePlayer = Phase.mc.thePlayer;
                        thePlayer.motionX *= 0.5;
                        final EntityPlayerSP thePlayer2 = Phase.mc.thePlayer;
                        thePlayer2.motionZ *= 0.5;
                        final double[] OPOP = { -0.02500000037252903, -0.028571428997176036, -0.033333333830038704, -0.04000000059604645, -0.05000000074505806, -0.06666666766007741, -0.10000000149011612, 0.0, -0.20000000298023224, -0.04000000059604645, -0.033333333830038704, -0.028571428997176036, -0.02500000037252903 };
                        for (int j = 0; j < OPOP.length; ++j) {
                            Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY + OPOP[j], Phase.mc.thePlayer.posZ, false));
                            Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + x * j, Phase.mc.thePlayer.boundingBox.minY, Phase.mc.thePlayer.posZ + z * j, false));
                        }
                        Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ, true));
                        Phase.mc.thePlayer.setPosition(Phase.mc.thePlayer.posX + x, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + z);
                        Phase.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.boundingBox.minY, Phase.mc.thePlayer.posZ, false));
                        break;
                    }
                    case "Spider": {
                        if (!this.isInsideBlock()) {
                            break;
                        }
                        final EntityPlayerSP thePlayer3 = Phase.mc.thePlayer;
                        thePlayer3.posY += 0.1;
                        Phase.mc.thePlayer.motionY = 0.065;
                        Phase.mc.thePlayer.resetHeight();
                        break;
                    }
                }
            }
        }
    }
    
    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Phase.mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(Phase.mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Phase.mc.thePlayer.boundingBox.minY); y < MathHelper.floor_double(Phase.mc.thePlayer.boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Phase.mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Phase.mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                    final Block block = Phase.mc.thePlayer.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Phase.mc.theWorld, new BlockPos(x, y, z), Phase.mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && Phase.mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
