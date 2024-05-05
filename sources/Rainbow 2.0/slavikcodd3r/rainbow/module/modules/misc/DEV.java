package slavikcodd3r.rainbow.module.modules.misc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.BlockCullEvent;
import slavikcodd3r.rainbow.event.events.BoundingBoxEvent;
import slavikcodd3r.rainbow.event.events.InsideBlockRenderEvent;
import slavikcodd3r.rainbow.event.events.ItemSlowEvent;
import slavikcodd3r.rainbow.event.events.JumpEvent;
import slavikcodd3r.rainbow.event.events.KeyPressEvent;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.NametagRenderEvent;
import slavikcodd3r.rainbow.event.events.PacketEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.PushOutOfBlocksEvent;
import slavikcodd3r.rainbow.event.events.ReachEvent;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.event.events.Render3DEvent;
import slavikcodd3r.rainbow.event.events.RunLoopEvent;
import slavikcodd3r.rainbow.event.events.SprintEvent;
import slavikcodd3r.rainbow.event.events.StepEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modules.movement.Speed;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;
import slavikcodd3r.rainbow.utils.NetUtil;
import slavikcodd3r.rainbow.utils.SpeedUtils;

@Module.Mod(displayName = "DEV")
public class DEV extends Module
{   
	int counter = 0;
	int stage = 0;
	Minecraft mc = Minecraft.getMinecraft();
	
	public DEV() {
		
	}
	
    public void enable() {

        super.enable();
    }
	
    public void disable() {
    	MoveUtils.setMotion(0);
        super.disable();
    }
	
	@EventTarget
    private void onUpdate(final BlockCullEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final BoundingBoxEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final InsideBlockRenderEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final ItemSlowEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final JumpEvent event) {
	}
	
	@EventTarget
    private void onUpdate(final KeyPressEvent event) {
		
	}
	
    @EventTarget
    private void onMove(final MoveEvent event) {

    }
	
	@EventTarget
    private void onUpdate(final NametagRenderEvent event) {

	}
	
	@EventTarget
    private void onUpdate(final PacketEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final PacketReceiveEvent event) {
		
	}
	
    @EventTarget
    private void onPacketSend(final PacketSendEvent event) {

    }
	
	@EventTarget
    private void onUpdate(final PushOutOfBlocksEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final ReachEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final Render2DEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final Render3DEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final RunLoopEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final SprintEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final StepEvent event) {
		
	}
	
	@EventTarget
    private void onUpdate(final TickEvent event) {

	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		
	}
}
