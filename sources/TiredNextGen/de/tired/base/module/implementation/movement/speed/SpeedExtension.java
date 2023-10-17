package de.tired.base.module.implementation.movement.speed;

import de.tired.util.math.TimerUtil;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.interfaces.IHook;
import lombok.Getter;

public abstract class SpeedExtension implements IHook {

    @Getter
    public final String modeName = getClass().getAnnotation(SpeedModeAnnotation.class).name();

    public int count, count1, count2, count3;

    public TimerUtil timerUtil;

    public SpeedExtension() {
        this.timerUtil = new TimerUtil();
    }

    public abstract void onPre(EventPreMotion eventPreMotion);
    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onPacket(PacketEvent eventPacket);
    public abstract void onUpdate(UpdateEvent eventUpdate);


}