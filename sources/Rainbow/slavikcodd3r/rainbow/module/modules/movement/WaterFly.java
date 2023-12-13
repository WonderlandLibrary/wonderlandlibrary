package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.LiquidUtils;

@Module.Mod(displayName = "WaterFly")
public class WaterFly extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	    private boolean wasWater;
	    private int ticks;
	    
	    public WaterFly() {
	        this.wasWater = false;
	        this.ticks = 0;
	    }
	    
	    @Override
	    public void enable() {
	        this.wasWater = false;
	        super.enable();
	    }
	    
	    @EventTarget(0)
	    public void onMove(final UpdateEvent event) {
	        if (event.getState() == Event.State.POST) {
	            return; 
	    }
	        if (ClientUtils.player().onGround || ClientUtils.player().isOnLadder()) {
	            this.wasWater = false;
	        }
	        if (ClientUtils.player().motionY > 0.0 && this.wasWater) {
	            if (ClientUtils.player().motionY <= 0.11) {
	                final EntityPlayerSP player = ClientUtils.player();
	                player.motionY *= 1.2671;
	            }
	            final EntityPlayerSP player2 = ClientUtils.player();
	            player2.motionY += 0.05172;
	        }
	        if (LiquidUtils.isInLiquid() && !ClientUtils.player().isSneaking()) {
	            if (this.ticks < 3) {
	                ClientUtils.player().motionY = 0.01;
	                ++this.ticks;
	                this.wasWater = false;
	            }
	            else {
	                ClientUtils.player().motionY = 0.5;
	                this.ticks = 0;
	                this.wasWater = true;
	            }
	        }
	    }
	}

