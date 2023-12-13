package wtf.diablo.module.impl.Combat;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.play.client.C03PacketPlayer;
import wtf.diablo.events.impl.PacketEvent;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.ModuleManager;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.module.impl.Movement.Speed;
import wtf.diablo.settings.impl.ModeSetting;

@Getter
@Setter
public class Criticals extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Watchdog");

    public Criticals(){
        super("Criticals","Critical people!", Category.COMBAT, ServerType.All);
        this.addSettings(mode);
    }
    private boolean stage;
    private double offset;
    @Subscribe
    public void onUpdate(UpdateEvent e) {


    }

    @Subscribe
    public void onPacket(PacketEvent e){
        this.setSuffix(mode.getMode());

        if (e.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer c03 = (C03PacketPlayer) e.getPacket();
            if (mc.thePlayer.onGround) {
                if (KillAura.target != null) {
                    stage = !stage;
                    if (stage) {
                        offset = 0.01;
                    } else {
                        offset = 0.007;
                    }
                    c03.y = mc.thePlayer.posY + offset;
                    c03.onGround = false;
                } else {
                    stage = false;
                }
            }
        }
    }
}
