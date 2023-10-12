package wtf.diablo.module.impl.Player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;
import wtf.diablo.events.impl.PacketEvent;
import wtf.diablo.module.ModuleManager;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.ModeSetting;

public class NoFall extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Watchdog");

    public NoFall() {
        super("NoFall", "Eliminates Fall Damage.", Category.PLAYER, ServerType.All);
        this.addSettings(mode);
    }

    @Subscribe
    public void onPacket(PacketEvent e){
        this.setSuffix(mode.getMode());
        if(mc.thePlayer == null) return;
        if(mc.thePlayer.fallDistance >= 3 && e.getPacket() instanceof C03PacketPlayer && !ModuleManager.getModule(Scaffold.class).isToggled()){
            ((C03PacketPlayer) e.getPacket()).onGround = true;
            mc.thePlayer.fallDistance = 0;
        }
    }
}
