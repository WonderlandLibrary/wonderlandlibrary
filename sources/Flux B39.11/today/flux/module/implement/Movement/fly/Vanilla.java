package today.flux.module.implement.Movement.fly;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.world.WorldSettings;
import today.flux.module.implement.Movement.Fly;
import today.flux.event.PostUpdateEvent;
import today.flux.module.SubModule;

/**
 * Created by John on 2016/12/18.
 */
public class Vanilla extends SubModule {
    public Vanilla(){
        super("Vanilla", "Fly");
    }

    @Override
    public void onEnable() {
        if(this.mc.theWorld == null)
            return;

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if(this.mc.theWorld == null)
            return;

        if (this.mc.playerController.getCurrentGameType() != WorldSettings.GameType.CREATIVE && this.mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR) {
            this.mc.thePlayer.capabilities.allowFlying = false;
            this.mc.thePlayer.capabilities.isFlying = false;
        }
    }

    @EventTarget
    private void onUpdate(PostUpdateEvent eb){
        if (this.mc.playerController.getCurrentGameType() != WorldSettings.GameType.CREATIVE && this.mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR) {
            this.mc.thePlayer.capabilities.allowFlying = true;
            this.mc.thePlayer.capabilities.isFlying = true;

            this.mc.thePlayer.capabilities.setFlySpeed(Fly.speed.getValue() * 0.05f);
        }
    }
}
