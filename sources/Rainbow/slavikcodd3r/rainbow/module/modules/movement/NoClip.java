package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.BlockCullEvent;
import slavikcodd3r.rainbow.event.events.BoundingBoxEvent;
import slavikcodd3r.rainbow.event.events.InsideBlockRenderEvent;
import slavikcodd3r.rainbow.event.events.PushOutOfBlocksEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "NoClip")
public class NoClip extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
    @EventTarget
    private void onBoundingBox(final BoundingBoxEvent event) {
        event.setBoundingBox(null);
    }
    
    @EventTarget
    private void onPushOutOfBlocks(final PushOutOfBlocksEvent event) {
        event.setCancelled(true);
    }
    
    @EventTarget
    private void onInsideBlockRender(final InsideBlockRenderEvent event) {
        event.setCancelled(true);
    }
    
    @EventTarget
    private void onBlockCull(final BlockCullEvent event) {
        event.setCancelled(true);
    }
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
        mc.thePlayer.noClip = true;
        mc.thePlayer.motionY = 0.0;
        mc.thePlayer.onGround = false;
        mc.thePlayer.capabilities.isFlying = false;
        if (mc.gameSettings.keyBindJump.pressed) {
            mc.thePlayer.motionY += 0.3;
        }
        if (mc.gameSettings.keyBindSneak.pressed) {
            mc.thePlayer.motionY -= 0.3;
        }
	}
}