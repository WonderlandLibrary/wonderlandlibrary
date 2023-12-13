 package vestige.impl.module.movement;

import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import vestige.api.event.Listener;
import vestige.api.event.impl.SlowdownEvent;
import vestige.api.event.impl.PostMotionEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.util.movement.MovementUtils;
import vestige.util.network.PacketUtil;

@ModuleInfo(name = "Noslow", category = Category.MOVEMENT)
public class Noslow extends Module {
	
	private final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "NCP", "AAC5", "Hypixel");
	
	private final NumberSetting forward = new NumberSetting("Forward", this, 1, 0.2, 1, 0.05, false);
	private final NumberSetting strafe = new NumberSetting("Strafe", this, 1, 0.2, 1, 0.05, false);
	
	public Noslow() {
		this.registerSettings(mode, forward, strafe);
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		this.setSuffix(mode.getMode());
		switch (mode.getMode()) {
			case "NCP":
				if(mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
					PacketUtil.releaseUseItem(true);
				}
				break;
			case "AAC5":
				if(mc.thePlayer.isUsingItem()) {
					PacketUtil.sendBlocking(true, false);
				}
				break;
			case "Hypixel":
				if(MovementUtils.isMoving() && mc.thePlayer.isUsingItem()) {
                    PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                }
				break;
		}
	}
	
	@Listener
	public void onPostMotion(PostMotionEvent event) {
		switch (mode.getMode()) {
			case "NCP":
				if(mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
					PacketUtil.sendBlocking(true, false);
				}
				break;
		}
	}
		
	@Listener
	public void onSlowdown(SlowdownEvent event) {
		event.setForward((float) forward.getCurrentValue());
		event.setStrafe((float) strafe.getCurrentValue());
	}
	
}
