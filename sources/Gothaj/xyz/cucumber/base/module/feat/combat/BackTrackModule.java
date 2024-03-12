package xyz.cucumber.base.module.feat.combat;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.AxisAlignedBB;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.Event;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventGameLoop;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.module.feat.player.ScaffoldModule;
import xyz.cucumber.base.module.feat.visuals.ChamsModule;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.render.ColorUtils;

@ModuleInfo(category = Category.COMBAT, description = "Allows you to hit player in old position", name = "Back Track", key = Keyboard.KEY_NONE, priority = ArrayPriority.HIGH)

public class BackTrackModule extends Mod {
	public static ArrayList<Packet> packets = new ArrayList<Packet>();

	private WorldClient lastWorld;
	private EntityLivingBase entity;

	public Timer timer = new Timer();
	public Timer timer2 = new Timer();
	
	private EntityOtherPlayerMP entityReal;
	
	private KillAuraModule killAura;

	public double delayValue = 300;

	public NumberSettings delay = new NumberSettings("Delay", 400, 0, 1000, 10);
	public NumberSettings hitRange = new NumberSettings("Hit Range", 3, 0, 10, 0.1);
	public BooleanSettings onlyIfNeed = new BooleanSettings("Only If Need", true);

	public BooleanSettings esp = new BooleanSettings("ESP", true);
	public ModeSettings mode = new ModeSettings("ESP Mode", new String[] {
			"Hitbox", "Player"
	});
	public ColorSettings mainColor = new ColorSettings("Color", "Still", -1, -1, 255);
	public ColorSettings outlineColor = new ColorSettings("Outline Color", "Still", -1, -1, 255);

	public BackTrackModule() {
		this.addSettings(delay, hitRange, onlyIfNeed, esp, mode, mainColor, outlineColor);
	}

	public void onEnable() {
		killAura = ((KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class));
		packets.clear();
		
		setInfo(delay.getValue()+"");
	}
	@EventListener
	public void onReceivePacket(EventReceivePacket e) {
		info = (int)(delay.getValue())+" ms";
		if (mc.thePlayer == null || mc.theWorld == null || killAura == null
				|| mc.getNetHandler().getNetworkManager().getNetHandler() == null) {
			packets.clear();
			return;
		}
		if (Client.INSTANCE.getModuleManager().getModule(ScaffoldModule.class).isEnabled()) {
			packets.clear();
			return;
		}

		if (e.getPacket() instanceof S14PacketEntity) {
			S14PacketEntity packet = (S14PacketEntity) e.getPacket();
			Entity entity = mc.theWorld.getEntityByID(packet.entityId);

			if (entity instanceof EntityLivingBase) {
				EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
				entityLivingBase.realPosX += packet.func_149062_c();
				entityLivingBase.realPosY += packet.func_149061_d();
				entityLivingBase.realPosZ += packet.func_149064_e();
			}
		}

		if (e.getPacket() instanceof S18PacketEntityTeleport) {
			S18PacketEntityTeleport packet = (S18PacketEntityTeleport) e.getPacket();
			final Entity entity = mc.theWorld.getEntityByID(packet.getEntityId());

			if (entity instanceof EntityLivingBase) {
				EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
				entityLivingBase.realPosX = packet.getX();
				entityLivingBase.realPosY = packet.getY();
				entityLivingBase.realPosZ = packet.getZ();
			}
		}
		entity = null;
		if (killAura.isEnabled()) {
			entity = EntityUtils.getTarget(hitRange.getValue(), "Players", "Single", 500,
					Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled(), killAura.TroughWalls.isEnabled(), killAura.attackDead.isEnabled(),
					killAura.attackInvisible.isEnabled());
		}

		if (mc.theWorld != null) {
			if (lastWorld != mc.theWorld) {
				resetPackets(mc.getNetHandler().getNetworkManager().getNetHandler());
				lastWorld = mc.theWorld;
				return;
			}
		}

		if (entity == null) {
			resetPackets(mc.getNetHandler().getNetworkManager().getNetHandler());
		} else {
			addPackets(e.getPacket(), e);
		}
	}

	@EventListener
	public void onGameLoop(EventGameLoop e) {
		if (entity != null && entity.getEntityBoundingBox() != null && mc.thePlayer != null && mc.theWorld != null
				&& entity.realPosX != 0 && entity.realPosY != 0 && entity.realPosZ != 0 && entity.width != 0
				&& entity.height != 0) {
			
			boolean work = false;
			double realX = entity.realPosX / 32;
			double realY = entity.realPosY / 32;
			double realZ = entity.realPosZ / 32;

			if(!onlyIfNeed.isEnabled()) {
				if(mc.thePlayer.getDistance(entity.posX, entity.posY, entity.posZ) > 3) {
					if (mc.thePlayer.getDistance(entity.posX, entity.posY, entity.posZ) >= mc.thePlayer.getDistance(realX,
							realY, realZ)) {
						resetPackets(mc.getNetHandler().getNetworkManager().getNetHandler());
					}
				}
			}else {
				if (mc.thePlayer.getDistance(entity.posX, entity.posY, entity.posZ) >= mc.thePlayer.getDistance(realX,
						realY, realZ)) {
					resetPackets(mc.getNetHandler().getNetworkManager().getNetHandler());
				}
			}

			if (mc.thePlayer.getDistanceToEntity(entity) > 3)
				work = true;
			
			if (!onlyIfNeed.isEnabled())
				work = true;

			if (!work) {
				if (mc.thePlayer.getDistance(realX, realY, realZ) <= 3) {
					resetPackets(mc.getNetHandler().getNetworkManager().getNetHandler());
				}
				releasePacketsToDistance(mc.getNetHandler().getNetworkManager().getNetHandler());
			}

			if (mc.thePlayer.getDistance(realX, realY, realZ) > hitRange.getValue()
					|| timer.hasTimeElapsed(delay.getValue(), true)) {
				resetPackets(mc.getNetHandler().getNetworkManager().getNetHandler());
			}
		}
	}

	@EventListener
	public void onRender3D(EventRender3D e) {
		if (entity != null && entity.getEntityBoundingBox() != null && mc.thePlayer != null && mc.theWorld != null
				&& entity.realPosX != 0 && entity.realPosY != 0 && entity.realPosZ != 0 && entity.width != 0
				&& entity.height != 0 && esp.isEnabled()) {

			boolean render = true;
			boolean work = false;
			double realX = (double) entity.realPosX / 32;
			double realY = (double) entity.realPosY / 32;
			double realZ = (double) entity.realPosZ / 32;

			if (mc.thePlayer.getDistance(entity.posX, entity.posY, entity.posZ) >= mc.thePlayer.getDistance(realX,
					realY, realZ)) {
				render = false;
			}

			if (mc.thePlayer.getDistanceToEntity(entity) > 3)
				work = true;
			if (!onlyIfNeed.isEnabled())
				work = true;

			if (!work) {
				if (mc.thePlayer.getDistance(realX, realY, realZ) <= 3) {
					render = false;
				}
				if (mc.thePlayer.getDistanceToEntity(entity) < 3) {
					render = false;
				}
			}

			if (onlyIfNeed.isEnabled() && mc.thePlayer.hurtTime > 3) {
				render = false;
			}

			if (mc.thePlayer.getDistance(realX, realY, realZ) > hitRange.getValue()
					|| timer.hasTimeElapsed(delay.getValue(), false)) {
				render = false;
			}

			if (entity != null && entity != mc.thePlayer && !entity.isInvisible() && render) {
				if(entity == null || entity.width == 0 || entity.height == 0)return;
				
				int color = ColorUtils.getColor(mainColor, System.nanoTime()/10000000, 1, 5);
				double x = entity.realPosX / 32D - mc.getRenderManager().renderPosX;
				double y = entity.realPosY / 32D - mc.getRenderManager().renderPosY;
				double z = entity.realPosZ / 32D - mc.getRenderManager().renderPosZ;
				switch(mode.getMode().toLowerCase()) {
				case "hitbox":
					GlStateManager.pushMatrix();
					
					RenderUtils.start3D();
					
					RenderUtils.color(color);
					
					RenderUtils.renderHitbox(new AxisAlignedBB(x-entity.width/2, y, z-entity.width/2, x+entity.width/2, y+entity.height, z+entity.width/2), GL11.GL_QUADS);
					
					RenderUtils.color( ColorUtils.getColor(outlineColor, System.nanoTime()/10000000, 1, 5));
					RenderUtils.renderHitbox(new AxisAlignedBB(x-entity.width/2, y, z-entity.width/2, x+entity.width/2, y+entity.height, z+entity.width/2), GL11.GL_LINE_LOOP);
					RenderUtils.stop3D();
					
					GlStateManager.popMatrix();
					break;
				case "player":
					float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * e.getPartialTicks();
					EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(this.mc.theWorld, new GameProfile(EntityPlayer.getUUID(mc.thePlayer.getGameProfile()),  ""));
	                entityOtherPlayerMP.setPosition( entity.realPosX / 32D,  entity.realPosY / 32D,  entity.realPosZ / 32D);
	                entityOtherPlayerMP.inventory = ((EntityOtherPlayerMP)entity).inventory;
	                entityOtherPlayerMP.inventoryContainer = ((EntityOtherPlayerMP)entity).inventoryContainer;
	                entityOtherPlayerMP.rotationYawHead =entity.rotationYawHead;
	                entityOtherPlayerMP.rotationYaw = entity.rotationYaw;
	                entityOtherPlayerMP.rotationPitch = entity.rotationPitch;
	                this.mc.theWorld.addEntityToWorld(-42069, entityOtherPlayerMP);
					break;
				}
				
				
			}
		}
	}

	private void releasePacketsToDistance(INetHandler netHandler) {
		if(entity == null)return;
		
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;

		if (packets.size() > 0) {
			synchronized (packets) {
				while (mc.thePlayer.getDistance(x, y, z) < 3 && packets.size() != 0) {
					try {
						packets.get(0).processPacket(netHandler);
						if (packets.get(0) instanceof S14PacketEntity) {
							S14PacketEntity packet = (S14PacketEntity) packets.get(0);
							final Entity entity = mc.theWorld.getEntityByID(packet.entityId);

							if (entity instanceof EntityLivingBase) {
								EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
								x += packet.func_149062_c();
								y += packet.func_149061_d();
								z += packet.func_149064_e();
							}
						}

						if (packets.get(0) instanceof S18PacketEntityTeleport) {
							S18PacketEntityTeleport packet = (S18PacketEntityTeleport) packets.get(0);
							final Entity entity = mc.theWorld.getEntityByID(packet.getEntityId());

							if (entity instanceof EntityLivingBase) {
								EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
								x = packet.getX();
								y = packet.getY();
								z = packet.getZ();
							}
						}
					} catch (Exception ex) {
					}
					packets.remove(packets.get(0));
				}
			}
		}
	}

	private void resetPackets(INetHandler netHandler) {
		if (packets.size() > 0) {
			synchronized (packets) {
				while (packets.size() != 0) {
					try {
						packets.get(0).processPacket(netHandler);
					} catch (Exception ex) {
					}
					packets.remove(packets.get(0));
				}
			}
		}
	}

	private void addPackets(Packet packet, Event event) {
		if (event == null || packet == null)
			return;
		synchronized (packets) {
			if (this.blockPacket(packet)) {
				packets.add(packet);
				event.setCancelled(true);
			}
		}
	}

	private boolean isEntityPacket(Packet packet) {
		return (packet instanceof S14PacketEntity
				|| packet instanceof net.minecraft.network.play.server.S19PacketEntityHeadLook
				|| packet instanceof S18PacketEntityTeleport
				|| packet instanceof net.minecraft.network.play.server.S0FPacketSpawnMob);
	}

	private boolean blockPacket(Packet packet) {
		if (packet instanceof net.minecraft.network.play.server.S03PacketTimeUpdate)
			return true;
		if (packet instanceof net.minecraft.network.play.server.S00PacketKeepAlive)
			return true;
		if (packet instanceof net.minecraft.network.play.server.S12PacketEntityVelocity)
			return true;
		if (packet instanceof net.minecraft.network.play.server.S27PacketExplosion)
			return true;
		if (packet instanceof net.minecraft.network.play.server.S32PacketConfirmTransaction) {
			return true;
		}
		return (packet instanceof S14PacketEntity
				|| packet instanceof net.minecraft.network.play.server.S19PacketEntityHeadLook
				|| packet instanceof S18PacketEntityTeleport
				|| packet instanceof net.minecraft.network.play.server.S0FPacketSpawnMob
				|| packet instanceof net.minecraft.network.play.server.S08PacketPlayerPosLook);
	}
}
