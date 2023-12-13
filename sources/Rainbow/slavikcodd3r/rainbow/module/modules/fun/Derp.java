package slavikcodd3r.rainbow.module.modules.fun;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.utils.ClientUtils;
@Mod (displayName = "Derp")
public class Derp extends Module
{
    @Option.Op(name = "Spinny")
    private boolean spinny;
    @Option.Op(name = "Headless")
    private boolean headless;
    @Option.Op(name = "SpinIncrement", min = 1.0, max = 170.0, increment = 1.0)
    private double spinincrement;
    private double serverYaw;
    Minecraft mc = Minecraft.getMinecraft();
    
    public Derp() {
        this.spinincrement = 25.0;
    }
    
    @EventTarget(0)
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.PRE) {
            if (this.spinny) {
                this.serverYaw += this.spinincrement;
                event.setYaw((float)this.serverYaw);
                mc.thePlayer.rotationYawHead = (float)this.serverYaw;
                mc.thePlayer.renderYawOffset = (float)this.serverYaw;
            }
            if (this.headless) {
                event.setPitch(180.0f);
            }
            else if (!this.headless && !this.spinny) {
            	final float random = (float)(Math.random() * 360.0);
                event.setYaw(random);
                event.setPitch(random);
                mc.thePlayer.rotationYawHead = random;
                mc.thePlayer.renderYawOffset = random;
            }
        }
    }
}
