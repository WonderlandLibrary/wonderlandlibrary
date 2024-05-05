package tech.atani.client.feature.module.impl.chat;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.listener.event.game.GameEndEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.math.time.TimeHelper;

@ModuleData(name = "AutoGG", identifier = "AutoGG", description = "Automatically types gg", category = Category.CHAT)
public class AutoGG extends Module {

    private final SliderValue<Long> delay = new SliderValue<>("Delay", "What will be the delay between gg's?", this, 1000L, 0L, 5000L, 0);

    private final TimeHelper timeHelper = new TimeHelper();

    @Listen
    public void onEnd(GameEndEvent gameEndEvent) {
        if(this.timeHelper.hasReached(delay.getValue())) {
            mc.thePlayer.sendChatMessage("gg");
            timeHelper.reset();
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
