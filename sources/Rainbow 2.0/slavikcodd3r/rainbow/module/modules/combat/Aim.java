package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MathHelper;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.friend.FriendManager;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;

@Module.Mod(displayName = "Aim")
public class Aim extends Module
{   
    @Op(name = "Range", min = 1.0, max = 1000.0, increment = 0.01)
	public double range;
	Minecraft mc = Minecraft.getMinecraft();
	
	public Aim() {
		this.range = 20;
	}
	
	public float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffYRand = 1.0;
        final double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() * diffYRand);
        }
        else {
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() * diffYRand);
        }
        final double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        if (pitch > 90.0f) {
            pitch = 90.0f;
        }
        if (pitch < -90.0f) {
            pitch = -90.0f;
        }
        if (!AutoPotion.potting) {
            return new float[] { Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
        }
        return new float[] { mc.thePlayer.rotationYaw, 90.0f };
    }
	
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		for (final Object theObject : Minecraft.theWorld.loadedEntityList) {
            if (theObject instanceof EntityLivingBase) {
                final EntityLivingBase e = (EntityLivingBase)theObject;
                if (e == mc.thePlayer) {
                    continue;
                }
                if (mc.thePlayer.getDistanceToEntity(e) > this.range) {
                    continue;
                }
                if (FriendManager.isFriend(e.getName())) {
                	continue;
                }
                final float[] rot = this.getRotationsNeeded(e);
                event.setYaw(rot[0]);;
                event.setPitch(rot[1]);
                mc.thePlayer.rotationYawHead = rot[0];
                mc.thePlayer.renderYawOffset = rot[0];
                }
            }
        }
    }

