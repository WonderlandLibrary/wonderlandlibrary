package vestige.impl.module.combat;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.PacketReceiveEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.impl.module.movement.Fly;
import vestige.impl.module.movement.Longjump;
import vestige.impl.module.movement.Speed;
import vestige.util.movement.MovementUtils;
import vestige.util.network.PacketUtil;

@ModuleInfo(name = "Velocity", category = Category.COMBAT)
public class Velocity extends Module {
	
	private final ModeSetting mode = new ModeSetting("Mode", this, "Packet", "Packet", "MoveCheck", "Reduce", "AGC", "Zonecraft", "Dragclick");
	
	private final NumberSetting horizontal = new NumberSetting("Horizontal", this, 0, 0, 100, 5, true) {
		@Override
		public boolean isShown() {
			return mode.is("Packet") || mode.is("Reduce");
		}
	};
	
	private final NumberSetting vertical = new NumberSetting("Vertical", this, 0, 0, 100, 5, true) {
		@Override
		public boolean isShown() {
			return mode.is("Packet");
		}
	};
	
	private final BooleanSetting explosions = new BooleanSetting("Explosions", this, true) {
		@Override
		public boolean isShown() {
			return mode.is("Packet");
		}
	};
	
	private final NumberSetting reduceHorizontal = new NumberSetting("reduce-horizontal", this, 0.5, -1, 1, 0.05, false) {
		@Override
		public boolean isShown() {
			return mode.is("Reduce");
		}
		@Override
		public String getDisplayName() {
			return "Horizontal";
		}
	};
	
	private final NumberSetting strength = new NumberSetting("Strength", this, 2, 1, 10, 1, true) {
		@Override
		public boolean isShown() {
			return mode.is("Dragclick");
		}
	};
	
	private final NumberSetting minHurtTime = new NumberSetting("Min hurtTime", this, 7, 1, 10, 1, true) {
		@Override
		public boolean isShown() {
			return mode.is("Reduce");
		}
	};
	
	private final NumberSetting maxHurtTime = new NumberSetting("Max hurtTime", this, 10, 1, 10, 1, true) {
		@Override
		public boolean isShown() {
			return mode.is("Reduce");
		}
	};
	
	private boolean receivedVelocity;
	
	public Velocity() {
		this.registerSettings(mode, horizontal, vertical, explosions, strength, minHurtTime, maxHurtTime);
	}
	
	public void onEnable() {
		receivedVelocity = false;
	}
	
	@Listener
	public void onReceive(PacketReceiveEvent event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity) {
			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
			
			if (packet.getEntityID() != mc.thePlayer.getEntityId()) return;
			
			switch (mode.getMode()) {
				case "Packet":
					if (horizontal.getCurrentValue() == 0 && vertical.getCurrentValue() == 0) {
						event.setCancelled(true);
					} else {
						packet.setMotionX((int) (packet.getMotionX() * horizontal.getCurrentValue() / 100));
						packet.setMotionZ((int) (packet.getMotionZ() * horizontal.getCurrentValue() / 100));
						packet.setMotionY((int) (packet.getMotionY() * vertical.getCurrentValue() / 100));
					}
					break;
				case "AGC":
					if(mc.thePlayer.onGround && !Vestige.getInstance().getModuleManager().getModule(Speed.class).isEnabled()) {
						event.setCancelled(true);
					}
					break;
				case "MoveCheck":
					if(MovementUtils.isMoving()) {
						event.setCancelled(true);
					}  
					break;
			}
			
			receivedVelocity = true;
		} else if (event.getPacket() instanceof S27PacketExplosion) {
			switch (mode.getMode()) {
				case "Packet":
					if (explosions.isEnabled()) event.setCancelled(true);
					break;
			}
		}
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		setSuffix(mode.getMode());
		if (mc.thePlayer.hurtTime == 0) {
			receivedVelocity = false;
		}
		
		switch (mode.getMode()) {
			case "Dragclick":
				if(mc.thePlayer.hurtTime == 10) {
					for(int i = 0; i < (int) strength.getCurrentValue(); i++) {
						PacketUtil.attackFakePlayer();
						MovementUtils.motionMult(0.6D);
					}
				}
				break;
			case "Zonecraft":
				if(mc.thePlayer.hurtTime == 9 && !Vestige.getInstance().getModuleManager().getModule(Fly.class).isEnabled()) {
					MovementUtils.strafe(0);
				}
				break;
			case "Reduce":
				if(receivedVelocity && !Vestige.getInstance().getModuleManager().getModule(Speed.class).isEnabled()
						&& !Vestige.getInstance().getModuleManager().getModule(Fly.class).isEnabled()
						&& !Vestige.getInstance().getModuleManager().getModule(Longjump.class).isEnabled()) {
					if(mc.thePlayer.hurtTime >= minHurtTime.getCurrentValue() && mc.thePlayer.hurtTime <= maxHurtTime.getCurrentValue()) {
						MovementUtils.motionMult(reduceHorizontal.getCurrentValue());
					}
				}
				break;
		}
	}
	
	@Listener
	public void onMotion(MotionEvent event) {
		switch (mode.getMode()) {
			case "AGC":
				if (receivedVelocity && mc.thePlayer.hurtTime == 9 && mc.thePlayer.onGround && !Vestige.getInstance().getModuleManager().getModule(Speed.class).isEnabled()) {
					event.setY(event.getY() - 0.08);
				}
				break;
		}
	}

}
