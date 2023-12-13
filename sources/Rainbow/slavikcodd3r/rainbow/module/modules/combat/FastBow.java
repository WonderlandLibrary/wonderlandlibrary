package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.FastBowMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "FastBow")
public class FastBow extends Module
{
    private boolean canVoid;
	private FastBowMode vanilla;
	private FastBowMode timer;
	private FastBowMode guardian;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public FastBow() {
        this.vanilla = new FastBowMode("Vanilla", true, this);
        this.timer = new FastBowMode("Timer", false, this);
        this.guardian = new FastBowMode("Guardian", false, this);
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.timer);
        OptionManager.getOptionList().add(this.guardian);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix("Vanilla");
        }
        else if (this.timer.getValue()) {
        	this.setSuffix("Timer");
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
    	if (this.guardian.getValue()) {
    		if (this.canVoid && mc.thePlayer.getHealth() > 0.0f && (mc.thePlayer.isCollidedVertically || Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) && mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                mc.thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(mc.thePlayer.inventory.getCurrentItem(), mc.theWorld, mc.thePlayer);
                for (int i = 0; i < 20; ++i) {
                	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, -999999999, mc.thePlayer.posZ, true));
                	this.canVoid = true;
                }
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                mc.thePlayer.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), mc.theWorld, mc.thePlayer, 0);
            }
            this.canVoid = !this.canVoid;
    	}
    	if (this.timer.getValue()) {
    		 if (mc.thePlayer.isDead) {
    	            this.mc.timer.timerSpeed = 1.0f;
    	        }
    	        if (mc.thePlayer.onGround && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && this.mc.gameSettings.keyBindUseItem.pressed) {
    	            this.mc.timer.timerSpeed = 20.0f;
    	            if (mc.thePlayer.getItemInUseDuration() >= 21) {
    	            	mc.playerController.onStoppedUsingItem(mc.thePlayer);
    	            }
    	            if (mc.thePlayer.ticksExisted % 6 == 0) {
    	            	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, false));
    	            }
    	        }
    	        if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && !this.mc.gameSettings.keyBindUseItem.pressed) {
    	            this.mc.timer.timerSpeed = 1.0f;
    	        }
    	    }
    	if (this.vanilla.getValue()) {
    		if (this.canVoid && mc.thePlayer.getHealth() > 0.0f && (mc.thePlayer.isCollidedVertically || Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) && mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                mc.thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(mc.thePlayer.inventory.getCurrentItem(), mc.theWorld, mc.thePlayer);
                for (int i = 0; i < 20; ++i) {
                	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                	this.canVoid = true;
                }
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                mc.thePlayer.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), mc.theWorld, mc.thePlayer, 0);
            }
            this.canVoid = !this.canVoid;
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
