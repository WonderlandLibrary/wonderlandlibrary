package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.BlockCullEvent;
import slavikcodd3r.rainbow.event.events.BoundingBoxEvent;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PushOutOfBlocksEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.PhaseMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Phase")
public class Phase extends Module
{
    @Op(name = "Speed", min = 0.0, max = 10.0, increment = 0.1)
    private double speed;
	private PhaseMode vanilla;
	private PhaseMode matrix;
	private PhaseMode ncp;
	private PhaseMode aac;
	private PhaseMode position;
	private PhaseMode teleport;
	private PhaseMode nullmod;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public Phase() {
        this.vanilla = new PhaseMode("Vanilla", true, this);
        this.matrix = new PhaseMode("Matrix", false, this);
        this.ncp = new PhaseMode("NCP", false, this);
        this.aac = new PhaseMode("AAC", false, this);
        this.position = new PhaseMode("Position", false, this);
        this.teleport = new PhaseMode("Teleport", false, this);
        this.nullmod = new PhaseMode("Null", false, this);
        this.speed = 1.2;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.matrix);
        OptionManager.getOptionList().add(this.ncp);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.position);
        OptionManager.getOptionList().add(this.teleport);
        OptionManager.getOptionList().add(this.nullmod);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Vanilla")).toString());
        }
        else if (this.matrix.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Matrix")).toString());
        }
        else if (this.ncp.getValue()) {
        	this.setSuffix("NCP");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.position.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Position")).toString());
        }
        else if (this.teleport.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Teleport")).toString());
        }
        else if (this.nullmod.getValue()) {
        	this.setSuffix("Null");
        }
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
    	Timer.timerSpeed = 1.0f;
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.teleport.getValue()) {
    		if (this.isInsideBlock()) {
    			double yaw = Math.toRadians((double)mc.thePlayer.rotationYaw);
    			double x = -Math.sin(yaw) * speed;
				double z = Math.cos(yaw) * speed;
				mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
    		}
    	}
    	if (this.position.getValue()) {
    		if (this.isInsideBlock()) {
    			double yaw = Math.toRadians((double)mc.thePlayer.rotationYaw);
    			double x = -Math.sin(yaw) * speed;
				double z = Math.cos(yaw) * speed;
				mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
    		}
    	}
    	if (this.aac.getValue()) {
    		if (this.isInsideBlock()) {
    	         this.mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(100));
    	         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2.0D, this.mc.thePlayer.posZ, true));
    	         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2.0D, this.mc.thePlayer.posZ, false));
    	         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2.0D, this.mc.thePlayer.posZ, true));
    	         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2.0D, this.mc.thePlayer.posZ, false));
    	         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2.0D, this.mc.thePlayer.posZ, true));
    	         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2.0D, this.mc.thePlayer.posZ, false));
    	         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2.0D, this.mc.thePlayer.posZ, true));
    		}
    	}
    	if (this.ncp.getValue()) {
    		mc.thePlayer.noClip = true;
    		final double multiplier = 0.3;
            final double mx = Math.cos(Math.toRadians(ClientUtils.player().rotationYaw + 90.0f));
            final double mz = Math.sin(Math.toRadians(ClientUtils.player().rotationYaw + 90.0f));
            final double x = ClientUtils.player().movementInput.moveForward * multiplier * mx + ClientUtils.player().movementInput.moveStrafe * multiplier * mz;
            final double z = ClientUtils.player().movementInput.moveForward * multiplier * mz - ClientUtils.player().movementInput.moveStrafe * multiplier * mx;
            if (ClientUtils.player().isCollidedHorizontally && !ClientUtils.player().isOnLadder() && !this.isInsideBlock()) {
                ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX + x, ClientUtils.player().posY, ClientUtils.player().posZ + z, false));
                for (int i = 1; i < 10; ++i) {
                    ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, 8.988465674311579E307, ClientUtils.player().posZ, false));
                }
                ClientUtils.player().setPosition(ClientUtils.player().posX + x, ClientUtils.player().posY, ClientUtils.player().posZ + z);
            }
    	}
    	if (this.matrix.getValue()) {
    		if (this.isInsideBlock()) {
    			Timer.timerSpeed = 0.05f;
                final float yaw = ClientUtils.yaw();
                ClientUtils.player().boundingBox.offsetAndUpdate(this.speed * Math.cos(Math.toRadians(yaw + 90.0f)), 0.0, this.speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            } else {
            	Timer.timerSpeed = 1.0f;
            }
    	}
    	if (this.vanilla.getValue()) {
    		if (this.isInsideBlock()) {
                final float yaw = ClientUtils.yaw();
                ClientUtils.player().boundingBox.offsetAndUpdate(this.speed * Math.cos(Math.toRadians(yaw + 90.0f)), 0.0, this.speed * Math.sin(Math.toRadians(yaw + 90.0f)));
    		}
    	}
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    	if (this.ncp.getValue()) {
    		if (this.isInsideBlock()) {
    			event.setY(ClientUtils.player().motionY = 0.0);
                ClientUtils.setMoveSpeed(event, 0.2);
    		}
    	}
    }
    
    @EventTarget
    private void onBoundingBox(final BoundingBoxEvent event) {
    	if (this.ncp.getValue()) {
    		if (this.isInsideBlock()) {
                event.setBoundingBox(null);
            }
    	}
    	if (event.getBoundingBox() != null && event.getBoundingBox().maxY > ClientUtils.player().boundingBox.minY) {
    		event.setBoundingBox(null);
    	}
    }
    
    @EventTarget
    private void onPushOutOfBlocks(final PushOutOfBlocksEvent event) {
    	if (this.nullmod.getValue()) {
            event.setCancelled(true);
        }
    	if (this.aac.getValue()) {
            event.setCancelled(true);
        }
    	if (this.ncp.getValue()) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    private void onBlockCull(final BlockCullEvent event) {
    	if (this.nullmod.getValue()) {
            event.setCancelled(true);
        }
    	if (this.aac.getValue()) {
            event.setCancelled(true);
        }
    	if (this.ncp.getValue()) {
            event.setCancelled(true);
        }
    }
    
    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(ClientUtils.player().boundingBox.minX); x < MathHelper.floor_double(ClientUtils.player().boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(ClientUtils.player().boundingBox.minY); y < MathHelper.floor_double(ClientUtils.player().boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(ClientUtils.player().boundingBox.minZ); z < MathHelper.floor_double(ClientUtils.player().boundingBox.maxZ) + 1; ++z) {
                    final Block block = ClientUtils.world().getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(ClientUtils.world(), new BlockPos(x, y, z), ClientUtils.world().getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && ClientUtils.player().boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
