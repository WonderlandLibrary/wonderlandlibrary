package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.SpeedUtils2;

@Module.Mod(displayName = "TargetStrafe")
public class TargetStrafe extends Module
{   
    public float angle;
    public boolean left;
    public boolean right;
    public boolean back;
    public boolean forward;
    public boolean x;
    public boolean z;
	Minecraft mc = Minecraft.getMinecraft();
	
	public void enable() {
		SpeedUtils2.tsStuff = null;
		super.enable();
	}
	
	public void disable() {
		mc.gameSettings.keyBindLeft.pressed = false;
		SpeedUtils2.tsStuff = null;
		super.disable();
	}
	
    @EventTarget
    public void onPacket(final PacketSendEvent event) {
        if (event.getPacket() instanceof C02PacketUseEntity) {
            final C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                mc.gameSettings.keyBindLeft.pressed = true;
            }
        }
    }
}
