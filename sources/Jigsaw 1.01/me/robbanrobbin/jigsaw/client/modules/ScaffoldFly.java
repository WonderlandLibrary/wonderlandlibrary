package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;

public class ScaffoldFly extends Module {
	
	private boolean placing;
	
	private EnumFacing sideHit;
	private BlockPos targetBlockPos;
	private Vec3d hitVec;
	private RayTraceResult movingObjectPosition;
	
	private Vec3d vec1;
	private Vec3d vec2;
	
	private boolean shouldMotionDown;
	private boolean tower;
	private boolean down;
	private int towerStage = 0;
	
	private BlockPos lastPlacedBlockPos;
	
	private ItemStack serverSideItemStack;
	
	private boolean isBlockBeingHeldServerSide = false;
	private boolean attackPacketSent = false;
	
	private ArrayList<Packet> packetQueue = new ArrayList<Packet>();
	
	private int lastInInventory;
	
	/**
	uh
	motiony -1 efter placera
	sen jump på pretick
	typ som en stillastående yport
	 */
	
	public ScaffoldFly() {
		super("Scaffold", 0, Category.WORLD, "Places blocks under your feet automatically. Really useful in skywars.");
	}
	
	@Override
	public void onDisable() {
		if(isBlockBeingHeldServerSide) {
			resetServerSideHotbarIndex();
		}
		super.onDisable();
	}
	
	@Override
	public void onToggle() {
		lastInInventory = -1;
		serverSideItemStack = null;
		attackPacketSent = false;
		
		placing = false;
		
		sideHit = null;
		targetBlockPos = null;
		hitVec = null;
		movingObjectPosition = null;
		
		vec1 = null;
		vec2 = null;
		
		shouldMotionDown = false;
		tower = false;
		down = false;
		
		lastPlacedBlockPos = null;
		
		towerStage = 0;
		Utils.resetMcTimerTPS();
		super.onToggle();
	}
	
	@Override
	public void onPreMotion(PreMotionEvent event) {
		
		if(getBlockInHotbar() == -1) {
			return;
		}
		
		double moveSpeed = getBaseMoveSpeed();
		
		MovementInput movementInput = mc.player.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().player.rotationYaw;
		
		if(!movementInput.sneak) {
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				event.x = 0.0D;
				event.z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			
			double mx = Math.cos(Math.toRadians(yaw + 90.0F));
			double mz = Math.sin(Math.toRadians(yaw + 90.0F));
			event.x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
			event.z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);
		}
		
		if(tower) {
//			Jigsaw.chatMessage(towerStage);
			Timer.setTimerSpeed(1.5);
			if(towerStage == 0) {
//				event.y = 0.399399995803833D;
			}
			if(towerStage == 1) {
//				event.y = 0;
			}
			if(towerStage == 2) {
				event.y = -1;
			}
			if(towerStage == 3) {
				towerStage = -1;
			}
			towerStage++;
		}
		if(!tower) {
			towerStage = 0;
			shouldMotionDown = false;
			Utils.resetMcTimerTPS();
		}
		
		super.onPreMotion(event);
	}
	
	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.player.isPotionActive(MobEffects.SPEED)) {
			int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {

		for(Packet packet : packetQueue) {
			sendPacketFinal(packet);
		}
		packetQueue.clear();
		
		serverSideItemStack = null;
		
		placing = false;
		
		sideHit = null;
		targetBlockPos = null;
		hitVec = null;
		movingObjectPosition = null;
		
		tower = false;
		
		int blockInInventory = getBlockInHotbar();
		
		if(blockInInventory == -1) {
			if(isBlockBeingHeldServerSide) {
				resetServerSideHotbarIndex();
			}
			return;
		}
		if(mc.player.inventory.getStackInSlot(blockInInventory).stackSize <= 0) {
			blockInInventory = getBlockInHotbar(blockInInventory);
			if(blockInInventory == -1) {
				if(isBlockBeingHeldServerSide) {
					resetServerSideHotbarIndex();
				}
				return;
			}
		}
		if(lastInInventory != blockInInventory) {
			isBlockBeingHeldServerSide = false;
		}
		
		lastInInventory = blockInInventory;
		
		serverSideItemStack = mc.player.inventory.getStackInSlot(blockInInventory);
		
		MovementInput movementInput = mc.player.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		
		boolean movingXZ = (forward != 0 || strafe != 0);
		
		down = movementInput.sneak;
		
		boolean onGround = mc.player.onGround;
		
		RayTraceResult rayTraceBlockBelow = mc.world.rayTraceBlocks(
				new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
				new Vec3d(mc.player.posX, mc.player.posY - 10, mc.player.posZ), false, false, true);
		
		boolean blockBelow = rayTraceBlockBelow != null && rayTraceBlockBelow.getBlockPos() != null && Utils.getBlockState(rayTraceBlockBelow.getBlockPos()).isFullBlock();
		
		if(!down) {
			if(blockBelow && !mc.player.isMovingXZ(0.05) && mc.gameSettings.keyBindJump.pressed) {
				
				tower = true;
				
				if(mc.player.onGround) {
					mc.player.motionY = (double) mc.player.getJumpUpwardsMotion();

					mc.player.isAirBorne = true;
				}
			}
			
			if(movingXZ && mc.gameSettings.keyBindJump.pressed && onGround) {
				mc.player.motionY = (double) mc.player.getJumpUpwardsMotion();
			}
		}
		
		if(!tower && Utils.getBlockStateRelativeToEntity(mc.player, -0.5d).getMaterial() != Material.AIR) {
			return;
		}
		
		Vec3d under = new Vec3d(mc.player.posX, mc.player.posY - 0.5, mc.player.posZ );
		
		double rayTraceRange = 4.2;
		Vec3d rayTraceTargetRaw = new Vec3d(mc.player.posX - mc.player.lastTickPosX, mc.player.posY - mc.player.lastTickPosY, mc.player.posZ - mc.player.lastTickPosZ).normalize();
		Vec3d rayTraceTarget = new Vec3d(rayTraceTargetRaw.x * -rayTraceRange, rayTraceTargetRaw.y * -rayTraceRange, rayTraceTargetRaw.z * -rayTraceRange);
		rayTraceTarget = rayTraceTarget.add(under);
		
		vec1 = under;
		vec2 = rayTraceTarget;
		
		RayTraceResult rayTrace = rayTrace(vec1, vec2);
		
		
		if(didRayTraceFindValidBlock(rayTrace)) {
			
			setValuesForRayTrace(rayTrace, event);
			
			if(!isBlockBeingHeldServerSide) {
				sendPacketFinal(new CPacketHeldItemChange(blockInInventory));
				isBlockBeingHeldServerSide = true;
			}
			
		}
		else if(!tower && lastPlacedBlockPos != null && mc.player.getDistance(lastPlacedBlockPos.getX() + 0.5, lastPlacedBlockPos.getY() + 0.5, lastPlacedBlockPos.getZ() + 0.5) <= 4.2) {
			
			rayTrace = rayTrace(vec1, Utils.getVec3d(lastPlacedBlockPos).addVector(0.5, 0.5, 0.5));
			
			if(didRayTraceFindValidBlock(rayTrace)) {
				
				setValuesForRayTrace(rayTrace, event);
				
//				Jigsaw.chatMessage("LASTPLACED");
				
				if(!isBlockBeingHeldServerSide) {
					sendPacketFinal(new CPacketHeldItemChange(blockInInventory));
					isBlockBeingHeldServerSide = true;
				}
				
			}
			
		}
		
//		if(!blockBelow && placing) {
//			mc.player.motionY = -1;
//		}
		
		super.onUpdate(event);
	}
	
	private void setValuesForRayTrace(RayTraceResult rayTrace, UpdateEvent event) {
		this.movingObjectPosition = rayTrace;
		this.hitVec = rayTrace.hitVec;
		this.sideHit = rayTrace.sideHit;
		this.targetBlockPos = rayTrace.getBlockPos();
		
		this.placing = true;
		
		float rots[] = Utils.getFacePos(Utils.getVec3d(targetBlockPos));
		
		event.yaw = rots[0];
		event.pitch = rots[1];
	}
	
	private boolean didRayTraceFindValidBlock(RayTraceResult rayTrace) {
		if(rayTrace != null && rayTrace.getBlockPos() != null) {

			BlockPos hitBlockPos = rayTrace.getBlockPos();
			IBlockState hitBlock = Utils.getBlockState(hitBlockPos);
			
			if(hitBlock.getMaterial() != Material.AIR) {
				
				return true;
				
			}
		}
		return false;
	}
	
	private RayTraceResult rayTrace(Vec3d vec1, Vec3d vec2) {
		return mc.world.rayTraceBlocks(vec1, vec2, false, false, true);
	}
	
	@Override
	public void onLateUpdate() {
		
		if(!placing) {
			return;
		}
		placing = false;
		
		if(processRightClickBlock(mc.player, mc.world, targetBlockPos, sideHit, hitVec, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) {
			attackPacketSent = false;
			lastPlacedBlockPos = targetBlockPos;
			shouldMotionDown = true;
		}
		
		sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
		
		super.onLateUpdate();
	}
	
	public EnumActionResult processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos hitPos, EnumFacing pos, Vec3d facing, EnumHand vec)
    {
        mc.playerController.syncCurrentPlayItem();
        ItemStack itemstack = player.getHeldItem(vec);
        float f = (float)(facing.x - (double)hitPos.getX());
        float f1 = (float)(facing.y - (double)hitPos.getY());
        float f2 = (float)(facing.z - (double)hitPos.getZ());
        boolean flag = false;

        if (!this.mc.world.getWorldBorder().contains(hitPos))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            if (mc.playerController.currentGameType != GameType.SPECTATOR)
            {
                IBlockState iblockstate = worldIn.getBlockState(hitPos);

                if ((!player.isSneaking() || player.getHeldItemMainhand().func_190926_b() && player.getHeldItemOffhand().func_190926_b()) && iblockstate.getBlock().onBlockActivated(worldIn, hitPos, iblockstate, player, vec, pos, f, f1, f2))
                {
                    flag = true;
                }

                if (!flag && itemstack.getItem() instanceof ItemBlock)
                {
                    ItemBlock itemblock = (ItemBlock)itemstack.getItem();

                    if (!itemblock.canPlaceBlockOnSide(worldIn, hitPos, pos, player, itemstack))
                    {
                        return EnumActionResult.FAIL;
                    }
                }
            }

            mc.playerController.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(hitPos, pos, vec, f, f1, f2));

            if (!flag && mc.playerController.currentGameType != GameType.SPECTATOR)
            {
                if (itemstack.func_190926_b())
                {
                    return EnumActionResult.PASS;
                }
                else if (player.getCooldownTracker().hasCooldown(itemstack.getItem()))
                {
                    return EnumActionResult.PASS;
                }
                else
                {
                    if (itemstack.getItem() instanceof ItemBlock && !player.canUseCommandBlock())
                    {
                        Block block = ((ItemBlock)itemstack.getItem()).getBlock();

                        if (block instanceof BlockCommandBlock || block instanceof BlockStructure)
                        {
                            return EnumActionResult.FAIL;
                        }
                    }

                    if (mc.playerController.currentGameType.isCreative())
                    {
                        int i = itemstack.getMetadata();
                        int j = itemstack.func_190916_E();
                        EnumActionResult enumactionresult = itemstack.onItemUse(player, worldIn, hitPos, vec, pos, f, f1, f2);
                        itemstack.setItemDamage(i);
                        itemstack.func_190920_e(j);
                        return enumactionresult;
                    }
                    else
                    {
                        return itemstack.onItemUse(player, worldIn, hitPos, vec, pos, f, f1, f2);
                    }
                }
            }
            else
            {
                return EnumActionResult.SUCCESS;
            }
        }
    }
	
	@Override
	public void onRender() {
		
		if(targetBlockPos != null) {
			double xPos = targetBlockPos.getX();
			double yPos = targetBlockPos.getY();
			double zPos = targetBlockPos.getZ();
			
			Vec3d render = RenderTools.getRenderPos(xPos, yPos, zPos);
			
			RenderTools.drawOutlinedBlockESP(render.x, render.y, render.z, 1f, 1f, 1f, 1f, 2f);
		}
		
		if(lastPlacedBlockPos != null) {
			double xPos = lastPlacedBlockPos.getX();
			double yPos = lastPlacedBlockPos.getY();
			double zPos = lastPlacedBlockPos.getZ();
			
			Vec3d render = RenderTools.getRenderPos(xPos, yPos, zPos);
			
			RenderTools.drawSolidBlockESP(render.x, render.y, render.z, 0.8f, 0.2f, 0.2f, 0.2f);
		}
		
		if(vec1 != null && vec2 != null) {
			Vec3d render1 = RenderTools.getRenderPos(vec1.x, vec1.y, vec1.z);
			Vec3d render2 = RenderTools.getRenderPos(vec2.x, vec2.y, vec2.z);
			
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			RenderTools.draw3DLine(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z, 0.8f, 0.2f, 0.2f, 0.7f, 1f);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
		
		
		
		super.onRender();
	}
	
	private void resetServerSideHotbarIndex() {
		isBlockBeingHeldServerSide = false;
		sendPacketFinal(new CPacketHeldItemChange(mc.player.inventory.currentItem));
	}
	
	@Override
	public void onPacketSent(PacketEvent event) {
		Packet packet = event.getPacket();
		if(!isBlockBeingHeldServerSide) {
			return;
		}
		if(packet instanceof CPacketUseEntity) {
			if(((CPacketUseEntity) packet).action == CPacketUseEntity.Action.ATTACK) {
				resetServerSideHotbarIndex();
				attackPacketSent = true;
			}
		}
		if(packet instanceof CPacketHeldItemChange) {
			packetQueue.add(new CPacketHeldItemChange(((CPacketHeldItemChange)packet).getSlotId()));
			event.cancel();
			isBlockBeingHeldServerSide = false;
		}
		super.onPacketSent(event);
	}
	
	private int getBlockInHotbar() {
		return getBlockInHotbar(-1);
	}
	
	private int getBlockInHotbar(int ignore) {
		
		for(int i = 0; i < 9; i++) {
			if(i == ignore) {
				continue;
			}
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if(stack == null) {
				continue;
			}
			Item item = stack.getItem();
			if(item == null) {
				continue;
			}
			if(Block.getBlockFromItem(item) == null) {
				continue;
			}
			if(Block.getBlockFromItem(item).getDefaultState().isFullBlock()) {
				return i;
			}
			
		}
		
		return -1;
		
	}
	
	public static boolean doSafeWalk() {
		ScaffoldFly instance = (ScaffoldFly) Jigsaw.getModuleByName("Scaffold");
		
		return instance.isToggled() && (instance.attackPacketSent || instance.serverSideItemStack == null);
	}
	
}
