package wtf.diablo.module.impl.Combat;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;
import wtf.diablo.events.impl.PacketEvent;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.NumberSetting;

@Getter@Setter
public class Velocity extends Module {
    public NumberSetting vertical = new NumberSetting("Vertical",0,1,0,100);
    public NumberSetting horizontal = new NumberSetting("Horizontal",0,1,0,100);
    public Velocity(){
        super("Velocity", "Removes player velocity", Category.COMBAT, ServerType.All);
        this.addSettings(vertical,horizontal);
    }

    @Subscribe
    public void onPacket(PacketEvent e) {
        this.setSuffix(vertical.getValue() + "% : " + horizontal.getValue() + "%");
        if (e.getPacket() instanceof S12PacketEntityVelocity) {
            if (((S12PacketEntityVelocity) e.getPacket()).getEntityID() == mc.thePlayer.getEntityId()) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();
                if(vertical.getValue() == 0 && vertical.getValue() ==0) {
                    e.setCanceled();
                } else {
                    packet.motionY = (int) (packet.motionY * (vertical.getValue()/100));
                    packet.motionX = (int) (packet.motionX * (horizontal.getValue()/100));
                    packet.motionZ = (int) (packet.motionZ * (horizontal.getValue()/100));
                }
            }
        }
        if (e.getPacket() instanceof S27PacketExplosion) {
            e.setCanceled();
        }
    }
}
