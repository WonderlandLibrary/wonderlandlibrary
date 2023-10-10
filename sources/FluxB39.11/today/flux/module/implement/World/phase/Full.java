package today.flux.module.implement.World.phase;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import today.flux.event.BBSetEvent;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.PacketSendEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.module.SubModule;

public class Full extends SubModule {
	double posX,posY,posZ;//玩家坐标
	boolean canspeed;
	int delay;
	
    public Full() {
        super("Full", "Phase");
    }

    @EventTarget
    public void onPacket(PacketReceiveEvent e) {
    	Packet packet = e.getPacket();
    	if(packet instanceof S08PacketPlayerPosLook) {
    		S08PacketPlayerPosLook s08packet = (S08PacketPlayerPosLook)packet;
    		s08packet.setYaw(mc.thePlayer.rotationYaw);
    		s08packet.setPitch(mc.thePlayer.rotationPitch);
    		canspeed = true;
    	}
    }
    
    @EventTarget
    public void onPacket(PacketSendEvent e) {
        if (isInsideBlock()) {
            return;
        }
        final double multiplier = 0.2;
        final double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
        final double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
        final double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
        Packet packet = e.getPacket();
        if (mc.thePlayer.isCollidedHorizontally && packet instanceof C03PacketPlayer) {
            delay++;
            final C03PacketPlayer player = (C03PacketPlayer) packet;
            if (this.delay >= 5) {
            	player.setPositionX(x + player.getPositionX());
            	player.setPositionY(player.getPositionY() - 1);
            	player.setPositionZ(z + player.getPositionZ());
                this.delay = 0;
            }
        }
    }
    
    @EventTarget
    public void onBBSet(BBSetEvent event) {
    	if(event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY) {
    		event.setCancelled(true);
    	}
    }
    
    @EventTarget
    public void onUpdate(PostUpdateEvent event) {
    	double multiplier = 0.4;
        final double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
        final double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
        final double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
        if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && !isInsideBlock()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
            for (int i = 1; i < 11; ++i) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, Double.MAX_VALUE * i, mc.thePlayer.posZ, false));
            }
            final double posX = mc.thePlayer.posX;
            final double posY = mc.thePlayer.posY;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY - (isOnLiquid() ? 9000.0 : 0.1), mc.thePlayer.posZ, false));
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
        }else if(isInsideBlock()){
        	mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
        }
    }
    
    public boolean isOnLiquid() {
        AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox();
        if (boundingBox == null) {
            return false;
        }
        boundingBox = boundingBox.contract(0.01D, 0.0D, 0.01D).offset(0.0D, -0.01D, 0.0D);
        boolean onLiquid = false;
        int y = (int) boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper
                .floor_double(boundingBox.maxX + 1.0D); x++) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper
                    .floor_double(boundingBox.maxZ + 1.0D); z++) {
                Block block = mc.theWorld.getBlockState((new BlockPos(x, y, z))).getBlock();
                if (block != Blocks.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    
    public boolean isInsideBlock() {
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