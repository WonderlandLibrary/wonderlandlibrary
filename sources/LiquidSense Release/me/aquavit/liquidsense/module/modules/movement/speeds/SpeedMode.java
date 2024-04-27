package me.aquavit.liquidsense.module.modules.movement.speeds;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.JumpEvent;
import me.aquavit.liquidsense.event.events.MotionEvent;
import me.aquavit.liquidsense.event.events.MoveEvent;
import me.aquavit.liquidsense.module.modules.movement.Speed;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class SpeedMode extends MinecraftInstance {

    public final String modeName;

    public SpeedMode(final String modeName) {
        this.modeName = modeName;
    }

    public boolean isActive() {
        final Speed speed = ((Speed) LiquidSense.moduleManager.getModule(Speed.class));

        return speed != null && !mc.thePlayer.isSneaking() && speed.getState() && speed.modeValue.get().equals(modeName);
    }

    public abstract void onJump(final JumpEvent event);

    public abstract void onUpdate();

    public abstract void onMove(final MoveEvent event);
    public abstract void onMotion(final MotionEvent event);

    public void onTick() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }
}
