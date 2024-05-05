package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;

@Module.Mod(displayName = "Fucker")
public class Fucker extends Module
{   
	@Op(name = "Bed")
	private boolean bed;
	@Op(name = "Egg")
	private boolean egg;
	@Op(name = "Cake")
	private boolean cake;
	@Op(name = "Core")
	private boolean core;
	@Op(name = "EnderStone")
	private boolean enderstone;
	@Op(name = "Chest")
	private boolean chest;
	@Op(name = "Ore")
	private boolean ore;
    float xPos;
    float yPos;
    float zPos;
	Minecraft mc = Minecraft.getMinecraft();
	
	public Fucker() {
		this.bed = true;
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		if (this.bed) {
			for (int radius = 5, x = -radius; x < radius; ++x) {
	            for (int y = radius; y > -radius; --y) {
	                for (int z = -radius; z < radius; ++z) {
	                    this.xPos = (float)((int)mc.thePlayer.posX + x);
	                    this.yPos = (float)((int)mc.thePlayer.posY + y);
	                    this.zPos = (float)((int)mc.thePlayer.posZ + z);
	                    final BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
	                    final net.minecraft.block.Block block = mc.theWorld.getBlockState(blockPos).getBlock();
	                    if (block.getBlockState().getBlock() == Blocks.stone) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                    }
	                }
	            }
			}
		}
		if (this.egg) {
			for (int radius = 5, x = -radius; x < radius; ++x) {
	            for (int y = radius; y > -radius; --y) {
	                for (int z = -radius; z < radius; ++z) {
	                    this.xPos = (float)((int)mc.thePlayer.posX + x);
	                    this.yPos = (float)((int)mc.thePlayer.posY + y);
	                    this.zPos = (float)((int)mc.thePlayer.posZ + z);
	                    final BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
	                    final net.minecraft.block.Block block = mc.theWorld.getBlockState(blockPos).getBlock();
	                    if (block.getBlockState().getBlock() == Blocks.dragon_egg) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                    }
	                }
	            }
			}
		}
		if (this.cake) {
			for (int radius = 5, x = -radius; x < radius; ++x) {
	            for (int y = radius; y > -radius; --y) {
	                for (int z = -radius; z < radius; ++z) {
	                    this.xPos = (float)((int)mc.thePlayer.posX + x);
	                    this.yPos = (float)((int)mc.thePlayer.posY + y);
	                    this.zPos = (float)((int)mc.thePlayer.posZ + z);
	                    final BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
	                    final net.minecraft.block.Block block = mc.theWorld.getBlockState(blockPos).getBlock();
	                    if (block.getBlockState().getBlock() == Blocks.cake) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                    }
	                }
	            }	            
			}
		}
		if (this.enderstone) {
			for (int radius = 5, x = -radius; x < radius; ++x) {
	            for (int y = radius; y > -radius; --y) {
	                for (int z = -radius; z < radius; ++z) {
	                    this.xPos = (float)((int)mc.thePlayer.posX + x);
	                    this.yPos = (float)((int)mc.thePlayer.posY + y);
	                    this.zPos = (float)((int)mc.thePlayer.posZ + z);
	                    final BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
	                    final net.minecraft.block.Block block = mc.theWorld.getBlockState(blockPos).getBlock();
	                    if (block.getBlockState().getBlock() == Blocks.end_stone) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                    }
	                }
	            }
			}
		}
		if (this.chest) {
			for (int radius = 5, x = -radius; x < radius; ++x) {
	            for (int y = radius; y > -radius; --y) {
	                for (int z = -radius; z < radius; ++z) {
	                    this.xPos = (float)((int)mc.thePlayer.posX + x);
	                    this.yPos = (float)((int)mc.thePlayer.posY + y);
	                    this.zPos = (float)((int)mc.thePlayer.posZ + z);
	                    final BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
	                    final net.minecraft.block.Block block = mc.theWorld.getBlockState(blockPos).getBlock();
	                    if (block.getBlockState().getBlock() == Blocks.chest) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                    }
	                }
	            }
			}
		}
		if (this.ore) {
			for (int radius = 5, x = -radius; x < radius; ++x) {
	            for (int y = radius; y > -radius; --y) {
	                for (int z = -radius; z < radius; ++z) {
	                    this.xPos = (float)((int)mc.thePlayer.posX + x);
	                    this.yPos = (float)((int)mc.thePlayer.posY + y);
	                    this.zPos = (float)((int)mc.thePlayer.posZ + z);
	                    final BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
	                    final net.minecraft.block.Block block = mc.theWorld.getBlockState(blockPos).getBlock();
	                    if (block.getBlockState().getBlock() == Blocks.coal_ore || block.getBlockState().getBlock() == Blocks.diamond_ore || block.getBlockState().getBlock() == Blocks.emerald_ore || block.getBlockState().getBlock() == Blocks.gold_ore || block.getBlockState().getBlock() == Blocks.iron_ore || block.getBlockState().getBlock() == Blocks.lapis_ore || block.getBlockState().getBlock() == Blocks.lit_redstone_ore || block.getBlockState().getBlock() == Blocks.quartz_ore || block.getBlockState().getBlock() == Blocks.redstone_ore) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                    }
	                }
	            }
			}
		}
		if (this.core) {
			for (int radius = 5, x = -radius; x < radius; ++x) {
	            for (int y = radius; y > -radius; --y) {
	                for (int z = -radius; z < radius; ++z) {
	                    this.xPos = (float)((int)mc.thePlayer.posX + x);
	                    this.yPos = (float)((int)mc.thePlayer.posY + y);
	                    this.zPos = (float)((int)mc.thePlayer.posZ + z);
	                    final BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
	                    final net.minecraft.block.Block block = mc.theWorld.getBlockState(blockPos).getBlock();
	                    if (block.getBlockState().getBlock() == Blocks.beacon) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
	                    }
	                }
	            }
			}
		}
	}
}
