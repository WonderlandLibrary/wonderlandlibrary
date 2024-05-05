package tech.atani.client.feature.module.impl.server.loyisa;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.math.time.TimeHelper;

@ModuleData(name = "AutoHeal", identifier = "eu.loyisa.cn AutoHeal", description = "Automatically /heal-s", category = Category.SERVER, supportedIPs = {"eu.loyisa.cn"})
public class AutoHeal extends Module {

    public SliderValue<Integer> minHealth = new SliderValue<>("Min Health", "What will the minimum health before healing?", this, 5, 0, 20, 0);
    public SliderValue<Long> delay = new SliderValue<>("Delay", "What will be the delay between heals?", this, 500L, 0L, 10000L, 0);

    private final TimeHelper timeHelper = new TimeHelper();

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        if(mc.thePlayer.getHealth() <= minHealth.getValue()) {
            if(timeHelper.hasReached(this.delay.getValue())) {
                mc.thePlayer.sendChatMessage("/heal");
                this.timeHelper.reset();
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}
