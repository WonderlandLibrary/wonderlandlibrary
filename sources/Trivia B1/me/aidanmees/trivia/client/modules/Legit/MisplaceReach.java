/*package me.aidanmees.trivia.client.modules.Legit;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.client.tools.MathUtil;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MisplaceReach extends Module {

	private Field packetXField;
    private Field packetZField;
    private Map<Integer, Double[]> coordinatesMap;
    private Map<Integer, Float[]> rotationsMap;

	public MisplaceReach() {
		super("MisplaceReach", Keyboard.KEY_NONE, Category.LEGIT,
				"Places the player closer to you for reach");
		 this.coordinatesMap = new HashMap<Integer, Double[]>();
	        this.rotationsMap = new HashMap<Integer, Float[]>();
	        try {
	            this.packetXField = S18PacketEntityTeleport.class.getDeclaredField("posX");
	            this.packetZField = S18PacketEntityTeleport.class.getDeclaredField("posZ");
	        }
	        catch (NoSuchFieldException e) {
	            e.printStackTrace();
	        }
	}
	@Override
	public ModSetting[] getModSettings() {
		ModSetting slider2 = new SliderSetting<Number>("Distance", ClientSettings.distanceMis, 0.1, 3.0, 0.0, ValueFormat.DECIMAL);
	
		CheckBtnSetting box3 = new CheckBtnSetting("Disadvantage", "Disadvantage");
		
		
		return new ModSetting[] {  slider2,box3 };
	}

@Override
	    public S18PacketEntityTeleport onEntityTeleport(final S18PacketEntityTeleport packet) {
	        final Entity entity = mc.theWorld.getEntityByID(packet.getEntityId());
	        if (entity instanceof EntityLivingBase) {
	            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
	           
	            double packetX = packet.getX() / 32.0;
	            double packetZ = packet.getZ() / 32.0;
	            this.coordinatesMap.put(entity.getEntityId(), new Double[] { packetX, packet.getY() / 32.0, packetZ });
	            this.rotationsMap.put(entity.getEntityId(), new Float[] { packet.getYaw() * 360.0f / 256.0f, packet.getPitch() * 360.0f / 256.0f });
	            double distance = ClientSettings.distanceMis;
	            final boolean disadvantageMode = ClientSettings.Disadvantage;
	            if (disadvantageMode) {
	                distance = -distance;
	            }
	            final float entityYaw = this.getAngle(packetX, packetZ);
	            if (MathUtil.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, entityYaw) > 90.0) {
	                return packet;
	            }
	            final double addX = Math.cos(Math.toRadians((double)(entityYaw + 90.0f))) * distance;
	            final double addZ = Math.sin(Math.toRadians((double)(entityYaw + 90.0f))) * distance;
	            packetX -= addX;
	            packetZ -= addZ;
	            try {
	                this.packetXField.setAccessible(true);
	                this.packetXField.set(packet, MathHelper.floor_double(packetX * 32.0));
	                this.packetXField.setAccessible(false);
	                this.packetZField.setAccessible(true);
	                this.packetZField.set(packet, MathHelper.floor_double(packetZ * 32.0));
	                this.packetZField.setAccessible(false);
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        return packet;
	    }

	@Override
	public void onDisable() {
		 this.coordinatesMap.clear();
	        this.rotationsMap.clear();
		super.onDisable();
	}
@Override
	 public boolean onRelativeMove(final S14PacketEntity packet) {
	        final Entity entity = packet.getEntity((World)mc.theWorld);
	        if (entity != null && this.coordinatesMap.containsKey(entity.getEntityId())) {
	            final Double[] coordinates = (Double[])this.coordinatesMap.get(entity.getEntityId());
	            final Float[] rotations = (Float[])this.rotationsMap.get(entity.getEntityId());
	            double packetX = packet.func_149062_c() / 32.0;
	            double packetY = packet.func_149061_d() / 32.0;
	            double packetZ = packet.func_149064_e() / 32.0;
	            float packetYaw = packet.func_149066_f() * 360.0f / 256.0f;
	            float packetPitch = packet.func_149063_g() * 360.0f / 256.0f;
	            if (!packet.func_149060_h()) {
	                packetYaw = rotations[0];
	                packetPitch = rotations[1];
	            }
	            packetX += coordinates[0];
	            packetY += coordinates[1];
	            packetZ += coordinates[2];
	            mc.thePlayer.sendQueue.handleEntityTeleport(this.onEntityTeleport(new S18PacketEntityTeleport(entity.getEntityId(), MathHelper.floor_double(packetX * 32.0), MathHelper.floor_double(packetY * 32.0), MathHelper.floor_double(packetZ * 32.0), (byte)(packetYaw * 256.0f / 360.0f), (byte)(packetPitch * 256.0f / 360.0f), true)));
	            return true;
	        }
	        return false;
	    }
	
@Override
    public void onDestroyEntities(final S13PacketDestroyEntities packet) {
        for (final Integer id : packet.getEntityIDs()) {
            this.coordinatesMap.remove(id);
            this.rotationsMap.remove(id);
        }
    }

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}
	private float getAngle(final double posX, final double posZ) {
        final double x = posX - mc.thePlayer.posX;
        final double z = posZ - mc.thePlayer.posZ;
        float newYaw = (float)Math.toDegrees(-Math.atan(x / z));
        if (z < 0.0 && x < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        else if (z < 0.0 && x > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        return newYaw;
    }

}*/