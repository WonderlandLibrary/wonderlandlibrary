package artemis.mods.World;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.events.Event;
import artemis.events.events.MotionUpdateEvent;
import artemis.events.listeners.EventMotion;
import artemis.settings.Setting;
import artemis.util.BlockUtil;
import artemis.util.TimeUtils;
import artemis.util.TimerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends module{

	public Scaffold() {
		super("Scaffold", "Places blocks under you", 0, 4, Category.WORLD);
		
	}
	
	@Override
	  public void setup() {
	      Artemis.instance.settingsManager.rSetting(new Setting("Delay", this, 5, 0, 15, true));
	      Artemis.instance.settingsManager.rSetting(new Setting("Eagle", this, false));
	  }
	
	
	TimerUtils timer;
  private BlockPos currentPos;
	    private EnumFacing currentFacing;
	    private boolean rotated = false;
	    
	    @Override
	    public void onEvent(Event e){
	    	if(e instanceof EventMotion) {
	    		if(e.isPre()) {
	    			EventMotion event = (EventMotion)e;
			  if(Artemis.instance.settingsManager.getSettingByName("Eagle").getValBoolean()) {
	            if (rotated) {
	                setSneaking(true);
	            } else {
	                setSneaking(false);
	            }
	        }

	        
	            rotated = false;
	            currentPos = null;
	            currentFacing = null;

	            BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
	            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
	                setBlockAndFacing(pos);

	                if (currentPos != null) {
	                    float facing[] = BlockUtil.getDirectionToBlock(currentPos.getX(), currentPos.getY(), currentPos.getZ(), currentFacing);

	                    float yaw = facing[0];
	                    float pitch = Math.min(90, facing[1] + 9);

	                    
	                    
	                    rotated = true;

	                    event.setYaw(yaw);
	                    event.setPitch(pitch);
	                    
	                  //  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, true));

	                   // e.setYaw(yaw);
	                   // mc.thePlayer.cameraYaw = yaw;
	                    //e.setPitch(pitch);
	                    
	                    //mc.thePlayer.cameraPitch = pitch;
	                }
	            }
	        }
	      //  if (e.getState() == MotionUpdateEvent.State.POST) {
	            if (currentPos != null) {
	                //if (timer.hasReached(Artemis.instance.settingsManager.getSettingByName("Delay").getValDouble())) {
	                    if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
	                        if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), currentPos, currentFacing, new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()))) {
	                          
	                            mc.thePlayer.swingItem();
	                            Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.5f, 0.5f);
	                            mc.thePlayer.motionX = 0;
	                            mc.thePlayer.motionZ = 0;
	                        }
	                    //}
	                //}
	            }
		}
	    	}
		}
	    @Override
	    public void onRender() {
	    	if(this.isToggled()) {
	    	 FontRenderer fr = mc.fontRendererObj;
	    	 ScaledResolution sr = new ScaledResolution(mc,  mc.displayWidth, mc.displayHeight);
	         fr.func_175063_a(Integer.toString(getBlockCount()),
	        		 (sr.getScaledWidth() >> 1)  - fr.getStringWidth(Integer.toString(getBlockCount())) / 2, 
	        		 (sr.getScaledHeight() >> 1) - 3, -1);
	    }

	    }
	    
	private void setSneaking(boolean b) {
        KeyBinding sneakBinding = mc.gameSettings.keyBindSneak;
        try {
            Field field = sneakBinding.getClass().getDeclaredField("pressed");
            field.setAccessible(true);
            field.setBoolean(sneakBinding, b);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        setSneaking(false);
    }


    private void setBlockAndFacing(BlockPos var1) {
        if (mc.theWorld.getBlockState(var1.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, -1, 0);
            currentFacing = EnumFacing.UP;
        } else if (mc.theWorld.getBlockState(var1.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(-1, 0, 0);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(1, 0, 0);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, 0, -1);
            currentFacing = EnumFacing.SOUTH;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, 0, 1);
            currentFacing = EnumFacing.NORTH;
        } else {
            currentPos = null;
            currentFacing = null;
        }
    }
    
    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock)) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }
	}
	
	

