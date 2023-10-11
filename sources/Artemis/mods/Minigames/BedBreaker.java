package artemis.mods.Minigames;

import artemis.Category;
import artemis.module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import artemis.util.BlockUtils;
import artemis.util.RayTraceUtils;
import artemis.util.TimeUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;


//Credit to GA • Let's codes for code
public class BedBreaker extends module{

	public BedBreaker() {
		super("BedBreaker", "Breaks beds in bedwars", 0, 0, Category.OTHER);
	}
	
	
    
	
	protected TimeUtils time = new TimeUtils();
	protected long lastFail;
	protected boolean failed;
	protected BlockPos failedBlock, lastArround;
	protected Block lastArroundBlock;
	protected int ftrys, blockID = 26;
	protected float yaw, pitch;

	public void onDisable() {
		this.time.reset();
		this.lastFail = 0;
		this.ftrys = 0;
		this.failed = false;
		this.lastArround = null;
		this.lastArroundBlock = null;
		this.failedBlock = null;
	}
	
	
	public void onUpdate() {
		if(this.isToggled()) {
			
			if (this.mc.theWorld != null && this.mc.thePlayer != null) {

				if (this.failedBlock != null && this.getBlocksArround(this.failedBlock).isEmpty()) {
					this.lastArround = null;
					this.lastArroundBlock = null;
					this.failed = false;
				}

				try {

					if(this.failed && !getBlocksArround(this.failedBlock).isEmpty()) {
						this.checkFailed();
					}
					
					for(int y = -5; y < 5; y++) {
						for(int x = -5; x < 5; x++) {
							for(int z = -5; z < 5; z++) {
								
								BlockPos blockPos = this.mc.thePlayer.getPosition().add((double) x, (double) y, (double) z);
								Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
								
								if(block == Block.getBlockById(this.blockID)) {
									
									this.setRotation(blockPos);
									
									if(this.time.hasTimerReached(4)) {
										
										this.destroyBlock(blockPos);
										this.ftrys++;
										this.time.reset();
										
										if(this.failedDestroy(blockPos, block) && !this.getBlocksArround(blockPos).isEmpty() && (double) this.ftrys > 5) {
											
											this.lastFail = System.currentTimeMillis();
											this.failed = true;
											this.failedBlock = blockPos;
											this.ftrys = 0;
											
										}
									}
								}
								
							}
						}
					}
					
				} catch (Exception e) {

				}

			}
			
		}
	}


	
		
	protected void checkFailed() {
		long sinceLastFail = System.currentTimeMillis() - this.lastFail;
		if(!this.getBlocksArround(this.failedBlock).isEmpty()) {
			BlockPos blockPos = this.getClosetBlock(this.getBlocksArround(this.failedBlock));
			this.setRotation(blockPos);
			if(sinceLastFail > 0) {
				this.lastArround = blockPos;
				this.lastArroundBlock = this.mc.theWorld.getBlockState(blockPos).getBlock();
				this.destroyBlock(blockPos);
			}
		}
		
		if(this.lastArround != null) {
			this.lastArround = null;
			this.lastArroundBlock = null;
			this.failed = false;
		}
	}
	protected BlockPos getClosetBlock(List list) {
		BlockPos currentPos = null;
		double currentRange = Double.MAX_VALUE;
		Iterator i = list.iterator();

		while (i.hasNext()) {
			BlockPos blockPos = (BlockPos) i.next();
			if (this.mc.thePlayer.getDistance((double) blockPos.getX(), (double) blockPos.getY(),
					(double) blockPos.getZ()) < currentRange) {
				currentRange = this.mc.thePlayer.getDistance(blockPos.getX(), (double) blockPos.getY(),
						(double) blockPos.getZ());
				currentPos = blockPos;
			}
		}
		return currentPos;
	}



		protected List getBlocksArround(BlockPos mainPos) {
			ArrayList arroundBlocks = new ArrayList();
			Block mainBlock = this.mc.theWorld.getBlockState(mainPos).getBlock();

			for (int y = 0; y < 2; y++) {
				for (int x = -5; x < 5; x++) {
					for (int z = -5; z < 5; z++) {
						BlockPos blockPos = mainPos.add(x, y, z);
						Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
						if (!(block == Blocks.air)) {
							arroundBlocks.add(blockPos);
						}
					}
				}
			}
			return arroundBlocks;
		
		
	}
		protected boolean failedDestroy(BlockPos blockPos, Block block) {
			return this.mc.theWorld.getBlockState(blockPos).getBlock() == block;
		}
		protected void setRotation(BlockPos blockPos) {
			BlockUtils sLoc = new BlockUtils(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.6D, this.mc.thePlayer.posZ);
			BlockUtils eLoc = new BlockUtils(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			RayTraceUtils rayTrace = new RayTraceUtils(sLoc, eLoc);
			this.pitch = (float) rayTrace.getPitch();
			this.yaw = (float) rayTrace.getYaw();
		}
		protected void destroyBlock(BlockPos blockPos) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
			this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
			this.mc.thePlayer.swingItem();
		}

}
