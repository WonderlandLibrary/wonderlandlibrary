package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.FastUseMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "FastUse")
public class FastUse extends Module
{
	private FastUseMode vanilla;
	private FastUseMode ncp;
	private FastUseMode aac;
	private FastUseMode guardian;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public FastUse() {
        this.vanilla = new FastUseMode("Vanilla", true, this);
        this.ncp = new FastUseMode("NCP", false, this);
        this.aac = new FastUseMode("AAC", false, this);
        this.guardian = new FastUseMode("Guardian", false, this);
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.ncp);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.guardian);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix("Vanilla");
        }
        else if (this.ncp.getValue()) {
        	this.setSuffix("NCP");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.guardian.getValue()) {
        	this.setSuffix("Guardian");
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
    	if (this.vanilla.getValue()) {
    		if (event.getState() == Event.State.PRE && ClientUtils.player().getItemInUseDuration() == 1 && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemBow) && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemSword)) {
                for (int i = 0; i < 35; ++i) {
                    ClientUtils.packet(new C03PacketPlayer(true));
                }
                ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
    	if (this.ncp.getValue()) {
    		if (event.getState() == Event.State.PRE && ClientUtils.player().getItemInUseDuration() == 16 && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemBow) && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemSword)) {
                for (int i = 0; i < 17; ++i) {
                    ClientUtils.packet(new C03PacketPlayer(true));
                }
                ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
    	if (this.aac.getValue()) {
    		if (event.getState() == Event.State.PRE && ClientUtils.player().getItemInUseDuration() == 1 && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemBow) && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemSword)) {
    			Timer.timerSpeed = 1.22f;
    		} else {
    			Timer.timerSpeed = 1.0f;	
    		}
    	}
    	if (this.guardian.getValue()) {
    		if (event.getState() == Event.State.PRE && ClientUtils.player().getItemInUseDuration() == 1 && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemBow) && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemSword)) {
    			for (int i = 0; i < 35; ++i) {
    				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, -999999999, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
    			}
    			ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    		}
    	}
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
