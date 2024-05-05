package tech.drainwalk.client.module.modules.overlay;

import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.option.options.SelectOption;
import tech.drainwalk.client.option.options.SelectOptionValue;
import tech.drainwalk.events.EventTarget;
import tech.drainwalk.events.UpdateEvent;

public class DayModule extends Module {
    private long spinTime = 0;
    private final SelectOption mode = new SelectOption("Mode", 0,
            new SelectOptionValue("Day"),
            new SelectOptionValue("Night"),
            new SelectOptionValue("Morning"),
            new SelectOptionValue("Sunset"),
            new SelectOptionValue("Spin"));
    public final FloatOption Speed = new FloatOption("Speed", 500, 100 , 500)
            .addSettingDescription("Скорость");

    public DayModule(){
        super("DayModule", Category.OVERLAY);
        addType(Type.SECONDARY);
        register(
                mode,
                Speed
        );
    }
    @EventTarget
public void onEnable() {
        if (mode.getValueByIndex(0)) {
            mc.world.setTotalWorldTime(500);
        }
        if (mode.getValueByIndex(1)) {
            mc.world.setTotalWorldTime(17000);
        }
        if (mode.getValueByIndex(2)) {
            mc.world.setTotalWorldTime(0);
        }
        if (mode.getValueByIndex(3)) {
            mc.world.setTotalWorldTime(13000);
        }
    }
        @EventTarget
                public void onUpdate(UpdateEvent updateEvent) {
            if(mode.getValueByIndex(4)){
                mc.world.setWorldTime(spinTime);
                this.spinTime = (long) (spinTime + Speed.getValue());

                }
            }
        }


