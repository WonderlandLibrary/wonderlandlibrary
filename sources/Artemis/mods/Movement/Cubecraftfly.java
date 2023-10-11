package artemis.mods.Movement;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;
import artemis.util.MSTimer;
import artemis.util.MoveUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class Cubecraftfly extends module{
	   private final MSTimer time = new MSTimer();
	  
	   MoveUtil move;
	private boolean damaged;

	private double tvalue;

	private int state;
	public Cubecraftfly() {
		super("CubecraftFly", "Cubecraft fly", Keyboard.KEY_NONE, 56, Category.MOVEMENT);
	}
	
	
	 public void onDisable() {
		 this.mc.timer.timerSpeed = 1.0F;
		    super.onDisable();
		  }
		  
		  public void onUpdate() {
		    if (isToggled()) {
		    	if (!(this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 4, this.mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
		    		this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 2, this.mc.thePlayer.posZ);
		    		
		    		this.mc.thePlayer.addChatMessage(new ChatComponentText("Artemis: Place a block before landing!"));
		    	} else {
		    		this.mc.thePlayer.addChatMessage(new ChatComponentText("Artemis: You have to have 4 Solid Blocks under you!"));
		    	}
		    	
		    	
		    	if (this.mc.thePlayer.hurtTime > 0) {
		    		this.damaged = true;
		    	}
		    	
		    	if (this.damaged) {
		    		this.tvalue = 2.1;
		    		double posX = move.instance.getPosForSetPosX(this.tvalue);
		    		double posZ = move.instance.getPosForSetPosZ(this.tvalue);
		    		if (this.mc.gameSettings.keyBindForward.pressed) {
		    			if (this.time.hasTimePassed(135)) {
		    				if (this.state == 0) {
		    					this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + posX, this.mc.thePlayer.posY - 0.0025,
		    							this.mc.thePlayer.posZ + posZ);
		    					this.state++;
		    				} else {
		    					this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + posX, this.mc.thePlayer.posY + 0.0026,
		    							this.mc.thePlayer.posZ + posZ);
		    					this.state = 0;
		    				}
		    				this.time.reset();
		    			}
		    			this.mc.thePlayer.motionY = .0025;
		    			this.mc.timer.timerSpeed = 0.3F;
		    		}
		    	}
		    	
		    					
		    }
	            }

		    
     protected void cctesting() {
	

		  }
}


