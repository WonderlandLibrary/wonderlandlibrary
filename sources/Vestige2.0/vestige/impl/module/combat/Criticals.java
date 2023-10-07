package vestige.impl.module.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.PacketSendEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.impl.module.movement.Fly;
import vestige.impl.module.movement.Speed;
import vestige.util.movement.MovementUtils;
import vestige.util.network.PacketUtil;
import vestige.util.network.ServerUtils;

@ModuleInfo(name = "Criticals", category = Category.COMBAT)
public class Criticals extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", this, "Packet", "Packet", "Edit", "NCP", "Hypixel", "NoGround", "Redesky", "Visual", "Custom MiniJump");
	
	private final NumberSetting motionY = new NumberSetting("Motion Y", this, 0.08, 0.005, 0.2, 0.0025, false) {
		@Override
		public boolean isShown() {
			return mode.is("Custom MiniJump");
		}
	};
	
	private final NumberSetting xzMotionMult = new NumberSetting("XZ Motion mult", this, 1, 0, 1.5, 0.05, false) {
		@Override
		public boolean isShown() {
			return mode.is("Custom MiniJump");
		}
	};
	
	private final NumberSetting hurtTime = new NumberSetting("HurtTime", this, 2, 1, 10, 1, true) {
		@Override
		public boolean isShown() {
			return mode.is("Custom MiniJump");
		}
	};
	
	private final NumberSetting randomisation = new NumberSetting("Randomisation", this, 0.001, 0, 0.01, 0.0001, false) {
		@Override
		public boolean isShown() {
			return mode.is("Custom MiniJump");
		}
	};
	
	private int counter, ticks;
	private double y;
	
	public Criticals() {
		this.registerSettings(mode, motionY, xzMotionMult, hurtTime);
	}

	@Listener
	public void onMotion(MotionEvent e) {
		this.setSuffix(mode.getMode());
		
		Killaura killaura = (Killaura) Vestige.getInstance().getModuleManager().getModule(Killaura.class);
		
		if(mode.is("NoGround") && killaura.isEnabled() && killaura.getTarget() != null && !Vestige.getInstance().getModuleManager().getModule(Fly.class).isEnabled()) {
			e.setOnGround(!mc.thePlayer.onGround && mc.thePlayer.motionY >= 0 && mc.thePlayer.motionY <= 0.16);
		}

		if(killaura.isEnabled() && killaura.getTarget() != null) {
			Entity entity = killaura.getTarget();
			if(entity instanceof EntityPlayer) {
				EntityPlayer target = (EntityPlayer) entity;

				if(mode.is("Edit") || mode.is("Hypixel")) {
					if(ServerUtils.isOnHypixel() || mode.is("Hypixel")) {
						if(!Vestige.getInstance().getModuleManager().getModule(Speed.class).isEnabled()) {
							hypixelEditCrits(e, target);
						}
					} else {
						EditCrits(e, target);
					}
				} else if(mode.is("Redesky")) {
					RedeskyCrits(e, target);
				} else if(mode.is("Custom MiniJump")) {
					customMiniJumpCrits(e, target);
				}
			} else {
				ticks = 0;
			}
			
			mc.thePlayer.onCriticalHit(killaura.getTarget());
		} else {
			ticks = 0;
		}
	}

	@Listener
	public void onSend(PacketSendEvent e) {
		if(e.getPacket() instanceof C02PacketUseEntity) {
			C02PacketUseEntity packet = (C02PacketUseEntity) e.getPacket();
			if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
				if(mode.is("Packet")) {
					PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, false));
					PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.02, mc.thePlayer.posZ, false));
				} else if(mode.is("NCP")) {
					if(packet.getEntity() instanceof EntityPlayer && mc.thePlayer.onGround) {
						EntityPlayer target = (EntityPlayer) packet.getEntity();
						if(target.hurtTime < 3) {
							PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, false));
							PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1E-4, mc.thePlayer.posZ, false));
						}
					}
				} else if(mode.is("Visual")) {
					mc.thePlayer.onCriticalHit(packet.getEntity());
				}
			}
		}
	}
	
	private void hypixelEditCrits(MotionEvent e, EntityPlayer target) {
		if(target.hurtTime < 3 && target.hurtTime > 0 && mc.thePlayer.ticksExisted % 2 == 0) {
			mc.thePlayer.motionY = 0.034536 + Math.random() * 0.001;
			MovementUtils.motionMult(0.9);
		}
	}
	
	private void customMiniJumpCrits(MotionEvent e, EntityPlayer target) {
		if(target.hurtTime == (int) hurtTime.getCurrentValue()) {
			mc.thePlayer.motionY = motionY.getCurrentValue();
			MovementUtils.motionMult(xzMotionMult.getCurrentValue());
		}
	}
	
	private void EditCrits(MotionEvent e, EntityPlayer target) {
		if(target.hurtTime > 3) {
			
		} else if(target.hurtTime == 3) {
			e.setY(e.getY() + 0.08);
			e.setOnGround(false);
		} else {
			e.setY(e.getY() + 1E-3);
			e.setOnGround(false);
		}
	}

	private void RedeskyCrits(MotionEvent e, EntityPlayer target) {
		if(mc.thePlayer.onGround) {
			if(ticks < 20) {
				e.setOnGround(false);
			}
			ticks++;
		} else {
			if(target.hurtTime > 7) {
				e.setOnGround(true);
			}
			ticks = 0;
		}
	}
	
}