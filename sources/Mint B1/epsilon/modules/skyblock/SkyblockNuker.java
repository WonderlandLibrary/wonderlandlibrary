package epsilon.modules.skyblock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventRenderGUI;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.render.EventRender3d;
import epsilon.modules.Module;
import epsilon.modules.render.Theme;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.EpsilonColorUtils;
import epsilon.util.ShapeUtils;
import epsilon.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import optifine.Config;
import shadersmod.client.Shaders;

public class SkyblockNuker extends Module {
	
	private final ArrayList<BlockPos> skiplist = new ArrayList<BlockPos>();
	private final static ShapeUtils r = new ShapeUtils();
	private BlockPos currentBlock;
	
	private final Timer timer = new Timer();
	private final Timer timer2 = new Timer();
	
	private BlockPos render;
	
	private float[] rotationsToDo;
	public ModeSetting mode = new ModeSetting("Mode", "Crop", "Crop", "Block");
	private BlockPos target;
	
	
	public NumberSetting range = new NumberSetting ("Range", 3.0, 0.5, 6.0, 0.1);
	public NumberSetting cpt = new NumberSetting ("CropsPerTick", 1, 1, 10, 1);
	

	public BooleanSetting vert = new BooleanSetting ("YAxis", false);
	public BooleanSetting packetmine = new BooleanSetting ("PacketMine", true);
	public BooleanSetting sprintlegit = new BooleanSetting ("LegitSprint", true);
	public BooleanSetting bypass = new BooleanSetting ("Bypass", false);
	public BooleanSetting renderB = new BooleanSetting ("RenderBrokenBlocks", true);
	public BooleanSetting replenish = new BooleanSetting ("ReplenishCheck", true);
	
	public SkyblockNuker() {
		super("SkyblockNuker", Keyboard.KEY_NONE, Category.DEV, "Breaks blocks");
		this.addSettings(mode, vert, range, cpt, replenish ,packetmine, sprintlegit, renderB, bypass);
	}

	private ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
	
	private double pX, pY, pZ;
	private boolean rotate;
	
	private int cropsMinedThisTick, cropsMined, ticks, airTicks;
    
	public void onDisable() {
		if(mode.getMode()=="Crop")
		Epsilon.addChatMessage("You broke " + cropsMined + " crops with crop nuker");
		
	}
	
    public void onEnable() {
    	if(this.skiplist!=null)
    	skiplist.clear();

		currentBlock = null;
    	
    	cropsMinedThisTick = cropsMined = 0;
    	
    	pX = mc.thePlayer.posX;
        pY = mc.thePlayer.posY;
        pZ = mc.thePlayer.posZ;
    	
    	
    }
    
    
    
	public void onEvent(Event e) {

		
		if(e instanceof EventRender3d) {
			
			if(currentBlock!=null) {
				
				if(airTicks<5)
					renderBpMC(currentBlock, EpsilonColorUtils.getColor(0, Theme.getTheme(), 0.5f), 22);
				if(renderB.isEnabled()) {
					for(BlockPos skip : skiplist) {
						if(skip == currentBlock) continue;
						int alpha = 5;
						
						
						
						renderBpMC(skip, new Color(255, 255, 255, alpha), 22);
						
						alpha+=2.5f;
					}
				}
			}
			
			
		}
	
		
		
		
		if(e instanceof EventRenderGUI) {
			final int height = sr.getScaledHeight();
            mc.fontRendererObj.drawStringWithShadow(String.valueOf(cropsMined), sr.getScaledWidth() / 2F, height/2, -1);
            
		}
		
		if(e instanceof EventUpdate) {
			this.displayInfo = mode.getMode() + (mode.getMode()=="Crop" ? (" :CropsMined: "+ cropsMined + "") : "");
			
			if(currentBlock!=null) {
				if(mc.theWorld.getBlock(currentBlock) instanceof BlockAir) {
					airTicks++;
				}
			}
			
			switch(mode.getMode()) {
			
			case "Crop":
			
				if((bypass.isEnabled() && timer.hasTimeElapsed((long) (50+((Math.random()+1)*5)), true)) || !bypass.isEnabled()) {
					if(!vert.isEnabled())
						weedWacker(range.getValue(), mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, packetmine.isEnabled());
					else
						weedWackerVert(range.getValue(), mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, packetmine.isEnabled());
						
					
					if(cropsMinedThisTick>0)
						rotate = true;
					else
						rotate = false;
					
					
				}
				
				break;
				

			case "Block":


				List<BlockPos> targetsBp = new ArrayList<BlockPos>();
				
				for (double x = -range.getValue(); x < range.getValue(); x++) {
		            for (double y = -range.getValue()*2; y < range.getValue()*2; y++) {
		                for (double z = -range.getValue(); z < range.getValue(); z++) {
		                	
		                	final BlockPos blockPos = new BlockPos(mc.thePlayer.posX+x, mc.thePlayer.posY+y, mc.thePlayer.posZ+z);
		                    final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

		                    if(block.getBlockHardness()>0 && !(block instanceof BlockAir)&& !(block instanceof BlockSandStone) && !(block instanceof BlockChest) && !(block instanceof BlockAnvil) && !(block instanceof BlockLeaves)) {

		                    	if(mc.thePlayer.inventory.getCurrentItem()!=null) {
									if(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe) {
					                	
										if(block instanceof BlockLog) {
	
						                	targetsBp.add(blockPos);
										}
					                	
									}else if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemPickaxe) {
										if(block instanceof BlockOre || block instanceof BlockRedstoneOre || block instanceof BlockObsidian)
						                	targetsBp.add(blockPos);
									}
		                    	}
								
								else 
				                	targetsBp.add(blockPos);
		                    	
		                    }
		                    
		                	
		                }
		            }
				}
				
				if(!targetsBp.isEmpty()) {
					mineBlockSmart(targetsBp.get(targetsBp.size()-1));
				
					
					rotate = true;
				}    else rotate = false;
				break;
			}
			
			
			long d = skiplist.size()>10 ? 1500/skiplist.size() : 1675;
			if(timer.hasTimeElapsed(d, true) && !skiplist.isEmpty()) {
				skiplist.remove(0);
			}
			
			cropsMinedThisTick = 0;

			ticks++;
			
			
			
		}
		
		if(e instanceof EventMotion) {

			EventMotion event = (EventMotion)e;

			if(this.rotationsToDo!=null) {
				if(rotate && mc.gameSettings.thirdPersonView!=0) {
					
					event.setYaw(rotationsToDo[0]);
					event.setPitch(rotationsToDo[1]);
					mc.thePlayer.rotationYawHead = rotationsToDo[0];
					mc.thePlayer.renderYawOffset = rotationsToDo[0];
					mc.thePlayer.rotationPitchHead = rotationsToDo[1];
					
					 
					
				}
			}
			
			if(e.isPre()) {
				
				pX = mc.thePlayer.posX;
		        pY = mc.thePlayer.posY;
		        pZ = mc.thePlayer.posZ;
		       
			}
			
			
			if(e.isPost()) {
				
			}
			
		}
	}
	
	
	private BlockPos getPriority(final List<BlockPos> targets) {

		for(BlockPos list : targets) {
            
            IBlockState blockState = mc.theWorld.getBlockState(list);
            
            if(blockState == Blocks.diamond_block) {
            	Epsilon.addChatMessage("Add");
            	return list;
            }
            if(blockState == Blocks.diamond_ore) {
            	Epsilon.addChatMessage("Add");
            	return list;
            }
            if(blockState == Blocks.redstone_ore)
            	return list;
            if(blockState == Blocks.lapis_ore)
            	return list;
            if(blockState == Blocks.gold_ore)
            	return list;
            if(blockState == Blocks.iron_ore)
            	return list;
            if(blockState == Blocks.coal_ore)
            	return list;
            if(blockState == Blocks.obsidian)
            	return list;
            if(blockState == Blocks.stone)
            	return list;
            if(blockState == Blocks.cobblestone)
            	return list;
            
		}
		
		return null;
	}

	private BlockPos closestBlock(final int range) {
    
		BlockPos playerPos = new BlockPos (mc.thePlayer.posX, mc.thePlayer.posY+1, mc.thePlayer.posZ);
		
		Vec3 playerVec = mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(range, range, range);
        
        ArrayList<Vec3> blocks = new ArrayList<Vec3>();
        
        
        
		
    
		
        return null;
    }
	
	
	
	private void weedWacker(final double range, final double xPos, final double yPos, final double zPos, final boolean packet) {
		for (double x = -range; x < range; x++) {
            for (double z = -range; z < range; z++) {
                
            	double yOther = 0;
            	
                BlockPos blockPos = new BlockPos(xPos + x, yPos + yOther, zPos + z);
                final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                
                

                if (block instanceof BlockAir || block.getBlockHardness() > 0) {
                	if(block instanceof BlockPumpkin || block instanceof BlockMelon) {
                		mineBlockSmart(blockPos);
                	}
                    continue;
                }
                    
                if(this.skiplist!=null && skiplist.contains(blockPos))
                    	continue;
                
                if(block instanceof BlockReed) {
                	final BlockPos bp = new BlockPos(xPos + x, yPos + 1, zPos + z);
                    final Block b = mc.theWorld.getBlockState(bp).getBlock();
                    
                    if(b instanceof BlockReed)
                    	blockPos = bp;
                    else
                    	skiplist.add(blockPos);
                    
                    
                }
                
        		
        		if(cropsMinedThisTick<cpt.getValue() && !skiplist.contains(blockPos)) {
        		
        			mineCrop(blockPos, packet);
        			cropsMined++; 
        			cropsMinedThisTick++;
            		rotationsToDo = getBlockRotations(blockPos);
        			skiplist.add(blockPos);
        			render = blockPos;
        			currentBlock = blockPos;
        			airTicks = 0;
        			
        		}
    			
            }
        
        }
		
	}
	
	private void weedWackerVert(final double range, final double xPos, final double yPos, final double zPos, final boolean packet) {
		for (double x = -range; x < range; x++) {
            for (double y = -range; y < range; y++) {
                for (double z = -range; z < range; z++) {
	                
	            	
	                BlockPos blockPos = new BlockPos(xPos + x, yPos + y, zPos + z);
	                final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
	                
	                
	
	                if (block instanceof BlockAir || block.getBlockHardness() > 0) {
	                	if(block instanceof BlockPumpkin || block instanceof BlockMelon) {
	                		mineBlockSmart(blockPos);
	                	}
	                    continue;
	                }
	                    
	                if(this.skiplist!=null && skiplist.contains(blockPos))
	                    	continue;
	                
	                if(block instanceof BlockReed) {
	                	final BlockPos bp = new BlockPos(xPos + x, yPos + 1, zPos + z);
	                    final Block b = mc.theWorld.getBlockState(bp).getBlock();
	                    
	                    if(b instanceof BlockReed)
	                    	blockPos = bp;
	                    else
	                    	skiplist.add(blockPos);
	                    
	                    
	                }
	                
	        		
	        		if(cropsMinedThisTick<cpt.getValue() && !skiplist.contains(blockPos)) {
	        		
	        			mineCrop(blockPos, packet);
	        			cropsMined++; 
	        			cropsMinedThisTick++;
	            		rotationsToDo = getBlockRotations(blockPos);
	        			skiplist.add(blockPos);
	        			render = blockPos;
	        			currentBlock = blockPos;
	        			airTicks = 0;
	        			
	        		}
    			
	            }
			}
        
        }
		
	}
	
	//blockNuker can also be used for pumpkins/melons
	/*
	private void nukerMine(final double range, final double xPos, final double yPos, final double zPos, final boolean packet) {
		if (range == 0) {
            final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
            final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

            if (block instanceof BlockAir|| block.getBlockHardness()<-0.1)
                return;
            
            rotationsToDo = getBlockRotations(blockPos);
            mineCrop(blockPos, true);

        } else {
            for (double x = -range; x < range; x++) {
                for (double y = range; y > -range; y--) {
                    for (double z = -range; z < range; z++) {
                        
                        final BlockPos blockPos = new BlockPos(xPos + x, yPos + y, zPos + z);
                        final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

                        if (block instanceof BlockAir || block.getBlockHardness()<-0.1) {
                        	rotate = false;
                            continue;
                        }
                        
                        currentBlock = blockPos;
                        
                        rotationsToDo = getBlockRotations(blockPos);
                        mineCrop(blockPos, true);
                    }
                }
            }
        }
	}*/
	
	private void blockNuker(final float range, final int maxBlocksPerTick, final float fastMineValue) {
		
	}
	
	private void mineCrop(final BlockPos bp, final boolean packet) {
		
		mc.getNetHandler().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bp, EnumFacing.UP));
		mc.getNetHandler().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, bp, EnumFacing.UP));
		
		if(packet) mc.getNetHandler().sendPacketNoEvent(new C0APacketAnimation()); else mc.thePlayer.swingItem();
		
	}

	private double adididasx, adididoz, adididy;
	
	private float[] getBlockRotations(BlockPos blockPos) {
        if (blockPos == null) {
            return null;
        } else {
            Vec3 positionEyes = mc.thePlayer.getPositionEyes(2.0F);
        	adididasx = this.randomNumber(0.1D, -0.1D);
        	adididoz = this.randomNumber(0.1D, -0.1D);
       
            Vec3 add = (new Vec3((double) blockPos.getX() +adididasx, (double) blockPos.getY() + adididy, (double) blockPos.getZ() +adididoz));
            double n = add.xCoord - positionEyes.xCoord;
            double n2 = add.yCoord - positionEyes.yCoord;
            double n3 = add.zCoord - positionEyes.zCoord;
            return new float[]{(float) (Math.atan2(n3, n) * 180.0D / 3.141592653589793D - 90.0D), -((float) (Math.atan2(n2, (float) Math.hypot(n, n3)) * 180.0D / 3.141592653589793D))};
        }
    }
	

	public static float[] getBlockRotationsP(final BlockPos blockPos, final boolean random) {
        if (blockPos == null) {
            return null;
        } else {
        	double xD = 0, zD = 0;
        	final Vec3 positionEyes = mc.thePlayer.getPositionEyes(2.0F);
        	if(random) {
	        	xD = Math.random() * (0.1D - -0.1D) + 0.1D;
	            zD = Math.random() * (0.1D - -0.1D) + 0.1D;
        	}
            Vec3 add = (new Vec3((double) blockPos.getX() +xD, (double) blockPos.getY(), (double) blockPos.getZ() +zD));
            final double n = add.xCoord - positionEyes.xCoord;
            final double n2 = add.yCoord - positionEyes.yCoord;
            final double n3 = add.zCoord - positionEyes.zCoord;
            return new float[]{(float) (Math.atan2(n3, n) * 180.0D / 3.141592653589793D - 90.0D), -((float) (Math.atan2(n2, (float) Math.hypot(n, n3)) * 180.0D / 3.141592653589793D))};
        }
    }
	
	private double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }
	
	private void mineBlockSmart(final BlockPos t) {
		

        final Block block = mc.theWorld.getBlockState(t).getBlock();

        mc.getNetHandler().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, t, EnumFacing.UP));
        mc.getNetHandler().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, t, EnumFacing.UP));

        if (!packetmine.isEnabled())
            mc.thePlayer.swingItem();
        else
        	mc.getNetHandler().sendPacketNoEvent(new C0APacketAnimation());

		rotationsToDo = getBlockRotations(t);
		currentBlock = t;
		airTicks = 0;
		
	}
	
	
	
	
	public static void renderBpMC(final BlockPos p, final Color c, final float w) {

        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        r.color(c, c.getAlpha());
        GL11.glLineWidth(3.0F);
        GlStateManager.func_179090_x();

        if (Config.isShaders())
        {
            Shaders.disableTexture2D();
        }

        GlStateManager.depthMask(false);
        
        Block b = mc.theWorld.getBlockState(p).getBlock();

        b.setBlockBoundsBasedOnState(mc.theWorld, p);
        double var8 = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double)mc.timer.renderPartialTicks;
        double var10 = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double)mc.timer.renderPartialTicks;
        double var12 = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double)mc.timer.renderPartialTicks;
        
        AxisAlignedBB axis = b.getSelectedBoundingBox(mc.theWorld, p).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-var8, -var10, -var12);
        
        
        
		RenderGlobal.drawOutlinedBoundingBox(axis, -1);
		

        GlStateManager.depthMask(true);
        GlStateManager.func_179098_w();

        if (Config.isShaders())
        {
            Shaders.enableTexture2D();
        }

        GlStateManager.disableBlend();
        
        GL11.glPopMatrix();
	}
	
	

	

}
