package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.event.UIRenderEvent;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Movement.fly.Float;
import today.flux.module.implement.Movement.fly.*;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.ColorUtils;
import today.flux.utility.TimeHelper;

import java.math.BigDecimal;

public class Fly extends Module {
	public static BooleanValue lagbackCheck = new BooleanValue("Fly", "Lagback Check", true);
	public static BooleanValue speedDisplay = new BooleanValue("Fly", "Speed Display", true);
	public static FloatValue speed = new FloatValue("Fly", "Speed (Vanilla)", 1.0f, 1.0f, 10.0f, 1.0f);
	public static BooleanValue viewBobbing = new BooleanValue("Fly", "View Bobbing", false);
	public TimeHelper lagbacktimer = new TimeHelper();

	@EventTarget
	public void onUpdate(PreUpdateEvent e) {
		if (viewBobbing.getValueState()) mc.thePlayer.cameraYaw = 0.1f;
	}


	@EventTarget(Priority.LOW)
	public void onRender(UIRenderEvent e) {
		if (speedDisplay.getValueState()) {
			ScaledResolution sr = new ScaledResolution(mc);
			double xDiff = (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * 2;
			double zDiff = (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * 2;
			BigDecimal bg = new BigDecimal(MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff) * 10d);
			int speed = (int) (bg.intValue() * mc.timer.timerSpeed);
			String str = speed + "block/sec";
			FontManager.wqy18.drawString(str, (sr.getScaledWidth() - FontManager.wqy18.getStringWidth(str)) / 2, sr.getScaledHeight() / 2 - 20, ColorUtils.WHITE.c);
		}
	}

	@EventTarget
	public void onLagback(PacketReceiveEvent e) {
		if (lagbackCheck.getValueState() && e.getPacket() instanceof S08PacketPlayerPosLook) {
			lagbacktimer.reset();
				this.toggle();
			}
		}
    
	public Fly() {
		super("Fly", Category.Movement, true, new Vanilla(), new Hypixel(), new BlocksMC(), new Float(), new OldNCP(), new Motion(), new Boat());
	}
}
