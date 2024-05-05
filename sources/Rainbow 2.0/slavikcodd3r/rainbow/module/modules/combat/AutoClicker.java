package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;

@Module.Mod(displayName = "AutoClicker")
public class AutoClicker extends Module
{   
    @Option.Op(name = "Right")
    public boolean right;
    @Option.Op(name = "Delay", min = 1, max = 20, increment = 1)
    public long delay;
	Minecraft mc = Minecraft.getMinecraft();
	
	public AutoClicker() {
		this.delay = 1;
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (!this.right && mc.gameSettings.keyBindAttack.pressed && mc.thePlayer.ticksExisted % delay == 0.0) {
        	mc.clickMouse();
        }
        else if (mc.gameSettings.keyBindUseItem.pressed && mc.thePlayer.ticksExisted % delay == 0.0) {	
        	mc.rightClickMouse();
        }
	}
}
