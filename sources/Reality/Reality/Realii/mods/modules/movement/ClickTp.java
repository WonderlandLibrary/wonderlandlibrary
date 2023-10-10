
package Reality.Realii.mods.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;

public class ClickTp
extends Module {
	 public Mode mode = new Mode("Mode", "Mode", new String[]{"Vannila", "Verus","Vulcan"}, "Vannila");
	  private List<Packet> packetList = new ArrayList<Packet>();
    public ClickTp(){
        super("ClickTp", ModuleType.Movement);
        this.addValues(mode);
    }
    @Override
    public void onEnable() {
    if (this.mode.getValue().equals("Verus")) {
 
    mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
    mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
    mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    	}
    }
    
    @Override
    public void onDisable() {
    	if (this.mode.getValue().equals("Vulcan")) {
    	 for (Packet packet : this.packetList) {
             this.mc.getNetHandler().addToSendQueue(packet);
         }
         this.packetList.clear();
    	}
     	}

    


    @EventHandler
    private void onPacketSend(EventPacketSend event) {
    	if (this.mode.getValue().equals("Vulcan")) {
    		//event.getPacket() instanceof  C01PacketChatMessage
            if (event.getPacket() instanceof C00PacketKeepAlive || event.getPacket() instanceof  C01PacketChatMessage ||  event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C04PacketPlayerPosition || event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C08PacketPlayerBlockPlacement || event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C07PacketPlayerDigging ) {
                this.packetList.add(event.getPacket());
                event.setCancelled(true);
                
                
            }
            
        	}
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
    	
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix(mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    	if (this.mode.getValue().equals("Verus")) {
            MovingObjectPosition ray = this.rayTrace(500.0);
            if (ray == null) {
                return;
            }
            if (mc.thePlayer.hurtTime > 1) {
            	 // mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + -1, mc.thePlayer.posZ);
            	
                double x_new = (double)ray.getBlockPos().getX() + 0.5;
                double y_new = ray.getBlockPos().getY() + 1;
                double z_new = (double)ray.getBlockPos().getZ() + 0.5;
                double distance = this.mc.thePlayer.getDistance(x_new, y_new, z_new);
                double d = 0.0;
                while (d < distance) {
                    this.setPos(this.mc.thePlayer.posX + (x_new - (double)this.mc.thePlayer.getHorizontalFacing().getFrontOffsetX() - this.mc.thePlayer.posX) * d / distance, this.mc.thePlayer.posY + (y_new - this.mc.thePlayer.posY) * d / distance, this.mc.thePlayer.posZ + (z_new - (double)this.mc.thePlayer.getHorizontalFacing().getFrontOffsetZ() - this.mc.thePlayer.posZ) * d / distance);
                    d += 2.0;
                }
                this.setPos(x_new, y_new, z_new);
                this.mc.renderGlobal.loadRenderers();
            }
        	}
    	if (this.mode.getValue().equals("Vannila")) {
        MovingObjectPosition ray = this.rayTrace(500.0);
        if (ray == null) {
            return;
        }
        if (Mouse.isButtonDown((int)1)) {
            double x_new = (double)ray.getBlockPos().getX() + 0.5;
            double y_new = ray.getBlockPos().getY() + 1;
            double z_new = (double)ray.getBlockPos().getZ() + 0.5;
            double distance = this.mc.thePlayer.getDistance(x_new, y_new, z_new);
            double d = 0.0;
            while (d < distance) {
                this.setPos(this.mc.thePlayer.posX + (x_new - (double)this.mc.thePlayer.getHorizontalFacing().getFrontOffsetX() - this.mc.thePlayer.posX) * d / distance, this.mc.thePlayer.posY + (y_new - this.mc.thePlayer.posY) * d / distance, this.mc.thePlayer.posZ + (z_new - (double)this.mc.thePlayer.getHorizontalFacing().getFrontOffsetZ() - this.mc.thePlayer.posZ) * d / distance);
                d += 2.0;
            }
            this.setPos(x_new, y_new, z_new);
            this.mc.renderGlobal.loadRenderers();
        }
    	}
    	
    	if (this.mode.getValue().equals("Vulcan")) {
            MovingObjectPosition ray = this.rayTrace(500.0);
            if (ray == null) {
                return;
            }
            
            if (Mouse.isButtonDown((int)1)) {
                double x_new = (double)ray.getBlockPos().getX() + 0.5;
                double y_new = ray.getBlockPos().getY() + 1;
                double z_new = (double)ray.getBlockPos().getZ() + 0.5;
                double distance = this.mc.thePlayer.getDistance(x_new, y_new, z_new);
                double d = 0.0;
                while (d < distance) {
                    this.setPos(this.mc.thePlayer.posX + (x_new - (double)this.mc.thePlayer.getHorizontalFacing().getFrontOffsetX() - this.mc.thePlayer.posX) * d / distance, this.mc.thePlayer.posY + (y_new - this.mc.thePlayer.posY) * d / distance, this.mc.thePlayer.posZ + (z_new - (double)this.mc.thePlayer.getHorizontalFacing().getFrontOffsetZ() - this.mc.thePlayer.posZ) * d / distance);
                    d += 2.0;
                }
                this.setPos(x_new, y_new, z_new);
                this.mc.renderGlobal.loadRenderers();
            }
        	}
    }

    public MovingObjectPosition rayTrace(double blockReachDistance) {
        Vec3 vec3 = this.mc.thePlayer.getPositionEyes(1.0f);
        Vec3 vec4 = this.mc.thePlayer.getLookVec();
        Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return this.mc.theWorld.rayTraceBlocks(vec3, vec5, !this.mc.thePlayer.isInWater(), false, false);
    }

    public void setPos(double x, double y, double z) {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
        this.mc.thePlayer.setPosition(x, y, z);
    }
}

