package Velo.impl.Modules.visuals;

import Velo.api.Module.Module;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.MovementUtil;
import Velo.impl.Event.EventReceivePacket;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.world.WorldSettings.GameType;

public class Freecam extends Module{

	
    public double oldX;
    public double oldY;
    public double oldZ;
    public float oldYaw;
    public float oldPitch;
    public EntityOtherPlayerMP player;
    
    
	public Freecam() {
		super("Freecam", "Freecam", 0, Category.PLAYER);
        this.oldX = 0.0;
        this.oldY = 0.0;
        this.oldZ = 0.0;
        this.oldYaw = 0.0f;
        this.oldPitch = 0.0f;
	}
	
	
	  @Override
	    public void onEnable() {
	        this.oldX = this.mc.thePlayer.posX;
	        this.oldY = this.mc.thePlayer.posY;
	        this.oldZ = this.mc.thePlayer.posZ;
	        this.oldYaw = this.mc.thePlayer.rotationYaw;
	        this.oldPitch = this.mc.thePlayer.rotationPitch;
	        (this.player = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile())).copyLocationAndAnglesFrom(this.mc.thePlayer);
	 
	        this.player.rotationYawHead = this.mc.thePlayer.rotationYawHead;
	        this.player.renderYawOffset = this.mc.thePlayer.renderYawOffset;
	        this.mc.theWorld.addEntityToWorld(-6969, this.player);
	  //      this.mc.thePlayer.noClip = true;
	    }
	    
	    @Override
	    public void onDisable() {
	        this.mc.thePlayer.setPosition(this.oldX, this.oldY, this.oldZ);
	        this.mc.thePlayer.rotationYaw = this.oldYaw;
	        this.mc.thePlayer.rotationPitch = this.oldPitch;
	        this.mc.thePlayer.motionX = 0.0;
	        this.mc.thePlayer.motionY = 0.0;
	        this.mc.thePlayer.motionZ = 0.0;
	        this.mc.theWorld.removeEntity(this.player);
	        this.player = null;
	    }
	    
	    
	    @Override
	    public void onUpdate(EventUpdate event) {
	    //	  mc.thePlayer.noClip = true;

	    //	  mc.thePlayer.setGameType(GameType.SPECTATOR.SPECTATOR);
	            this.mc.thePlayer.fallDistance = 0.0f;
	     //       this.mc.thePlayer.motionX = 0.0;
	        //    this.mc.thePlayer.motionY = 0.0;
	         //   this.mc.thePlayer.motionZ = 0.0;
	          //  MovementUtil.strafe(1.0f);
	            if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
	                final EntityPlayerSP thePlayer = this.mc.thePlayer;
	    //            thePlayer.motionY += 0.5;
	            }
	            if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
	                final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
	       //         thePlayer2.motionY -= 0.5;
	            }
	
	        if (this.mc.theWorld == null || mc.thePlayer == null) {
	            this.isToggled = false;
	        }
	    }


@Override
public void onEventSendPacket(EventSendPacket event) {
    if ( (((EventSendPacket)event).getPacket() instanceof C03PacketPlayer || ((EventSendPacket)event).getPacket() instanceof C0BPacketEntityAction)) {
        ((EventSendPacket)event).setCancelled(true);
    }

}

@Override
public void onEventReceivePacket(EventReceivePacket event) {
	
	  if (((EventReceivePacket)event).getPacket() instanceof S07PacketRespawn) {
          this.isToggled = false;
      }
	  
	  if (((EventReceivePacket)event).getPacket() instanceof S08PacketPlayerPosLook) {
          this.oldX = ((S08PacketPlayerPosLook)((EventReceivePacket)event).getPacket()).getX();
          this.oldY = ((S08PacketPlayerPosLook)((EventReceivePacket)event).getPacket()).getY();
          this.oldZ = ((S08PacketPlayerPosLook)((EventReceivePacket)event).getPacket()).getZ();
          this.oldYaw = ((S08PacketPlayerPosLook)((EventReceivePacket)event).getPacket()).yaw;
          this.oldPitch = ((S08PacketPlayerPosLook)((EventReceivePacket)event).getPacket()).pitch;
          this.player.posX = this.oldX;
          this.player.posY = this.oldY;
          this.player.posZ = this.oldZ;
          this.player.rotationYaw = this.oldYaw;
          this.player.rotationPitch = this.oldPitch;
          ((EventReceivePacket)event).setCancelled(true);
          this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.oldX, this.oldY, this.oldZ, this.oldYaw, this.oldPitch, false));
      }
	  
      if (((EventReceivePacket)event).getPacket() instanceof S12PacketEntityVelocity) {
          final EntityOtherPlayerMP player = this.player;
          player.motionY += 4.0;
      }
	  
	super.onEventReceivePacket(event);
}



	    

}
