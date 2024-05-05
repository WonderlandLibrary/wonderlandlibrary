package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.friend.FriendManager;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "RainbowAntiCheat")
public class RainbowAntiCheat extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
    EntityLivingBase entity;
    public float machinelearning;
    
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		for (final Object theObject : Minecraft.theWorld.loadedEntityList) {
            if (theObject instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)theObject;
                if (entityplayer == mc.thePlayer) {
                    continue;
                }
                this.entity = entityplayer;
                if (this.entity.ticksExisted <= 40 || mc.thePlayer.ticksExisted % 1 != 0) {
                    continue;
                }
                if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && this.entity.rotationPitch > 90.0f || this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && this.entity.rotationPitch < -90.0f) {
                    ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (Unlegit Head Position)");
                }
                if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && this.entity.stepHeight > 0.5f) {
                    ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (Step)");
                }
                if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && Math.abs(this.entity.posX - this.entity.lastTickPosX) > 1.0 || this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && Math.abs(this.entity.posZ - this.entity.lastTickPosZ) > 1.0) {
                    ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (Speed)");
                }
                if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && this.entity.fallDistance >= 3.5f && this.entity.onGround && this.entity.hurtTime == 0) {
                    ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (NoFall)");
                }
                if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && this.entity.noClip && this.entity.onGround) {
                    ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (Phase)");
                }
                final double y = Math.abs(Math.abs(this.entity.posY) - Math.abs(this.entity.prevPosY));
                if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && y > 1.0 && !this.entity.onGround && !this.entity.isCollidedVertically && this.entity.posY > this.entity.prevPosY) {
                    ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (Fly)");
                }
                if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && !this.entity.isMoving() && this.entity.moveForward > 0.0f) {
                    ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (Multi-Direction Sprint)");
                }
                if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && this.entity.moveStrafing > 0.0f) {
                	ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (Strafe)");
                }
        		if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && this.entity.hurtTime > 0
        				&& this.entity.lastTickPosX == this.entity.posX && this.entity.posZ == this.entity.lastTickPosZ
        				&& !mc.theWorld.checkBlockCollision(this.entity.boundingBox.expand(0.05, 0, 0.05))) {
                    ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (Velocity)");
        		}
                final double pitch = Math.abs(Math.abs(this.entity.rotationPitch) - Math.abs(this.entity.prevRotationPitch));
                if (this.entity.rotationPitch != 0.1384174174194174919581119481401384140135401383158513185475485491305371719231325984f && this.entity.rotationPitch == Math.round(this.entity.rotationPitch) && this.entity.rotationPitch != 90.0f && this.entity.rotationPitch != -90.0f && pitch > 10.0) {
                    ClientUtils.sendMessage("[RAC] " + String.valueOf(this.entity.getName()) + " is using hacks! (KillAura)");
                }
            }
		}
	}
}
