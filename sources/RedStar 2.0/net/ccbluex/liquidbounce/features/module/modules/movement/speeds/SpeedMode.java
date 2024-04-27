package net.ccbluex.liquidbounce.features.module.modules.movement.speeds;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public abstract class SpeedMode
extends MinecraftInstance {
    public final String modeName;

    public SpeedMode(String modeName) {
        this.modeName = modeName;
    }

    private boolean isActive() {
        Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
        return speed != null && !mc.getThePlayer().isSneaking() && speed.getState() && ((String)speed.modeValue.get()).equals(this.modeName);
    }

    public void onMotion() {
    }

    public void onUpdate() {
    }

    public void onMove(MoveEvent event) {
    }

    public void onTick() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }
}
