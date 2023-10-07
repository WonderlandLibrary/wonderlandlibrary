/*
package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.FloatProperty;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.modules.configurations.property.object.PropertyFactory.booleanTrue;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createFloat;

public final class GameSpeed extends AbstractModule {

    */
/* properties @off *//*

    @Property("timer-speed")
    private final FloatProperty timerSpeed = createFloat(1.0F).minimum(0.3F).maximum(5.0F);
    @Property("lagback")
    private final BooleanProperty lagback = booleanTrue();

    */
/* constructors @on *//*

    public GameSpeed(@NonNull ModuleManager moduleManager) {
        super(moduleManager, EnumModuleType.MISC, "GameSpeed", "Game Speed");
        Manager.put(new Setting("TIMERSPEED", "Timer Speed", SettingType.SLIDER, this, this.timerSpeed, 0.05));
        Manager.put(new Setting("GS_LAGBACK", "Lagback check", SettingType.CHECKBOX, this, lagback));
    }

    */
/* methods *//*

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
    }

    */
/* events *//*

    @EventTarget
    public void onPreUpdate(MotionUpdateEvent event) {
        if(event.getState().equals(MotionUpdateEvent.State.PRE)) {
            mc.timer.timerSpeed = timerSpeed.get();
        }
    }

    @EventTarget
    public void onReceive(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                if (lagback.get()) {
                    checkModule(getClass());
                }
            }
        }
    }

}
*/
