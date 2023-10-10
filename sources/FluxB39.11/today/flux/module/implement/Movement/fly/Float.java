package today.flux.module.implement.Movement.fly;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.event.PreUpdateEvent;
import today.flux.module.implement.Combat.Criticals;
import today.flux.module.implement.Movement.Speed;
import today.flux.event.MoveEvent;
import today.flux.module.SubModule;
import today.flux.utility.EntityUtils;
import today.flux.utility.PlayerUtils;

import java.util.Random;

/**
 * Created by John on 2017/06/27.
 */
public class Float extends SubModule {
    public Float() {
        super("Float", "Fly");
    }

    @EventTarget
    public void onMove2(MoveEvent event) {
        event.setOnGround(true);
        mc.thePlayer.motionY = 0;
        event.setY(0);
        Speed.setMoveSpeed(event, Speed.getBaseMoveSpeed());
    }

    @EventTarget
    public void onPre(PreUpdateEvent e) {
        e.setOnGround(true);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

    }
}