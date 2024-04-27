package me.aquavit.liquidsense.module.modules.misc;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.misc.RandomUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.utils.timer.TimeUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.TextValue;

import java.util.Random;

@ModuleInfo(name = "SpammerMessage", description = "Spams the chat with a given message.", category = ModuleCategory.MISC)
public class Spammer extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 1000, 0, 5000) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int minDelayValueObject = minDelayValue.get();

            if(minDelayValueObject > newValue)
                set(minDelayValueObject);
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
        }
    };

    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 500, 0, 5000) {

        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int maxDelayValueObject = maxDelayValue.get();

            if(maxDelayValueObject < newValue)
                set(maxDelayValueObject);
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
        }
    };

    private final TextValue messageValue = new TextValue("Message", LiquidSense.CLIENT_NAME + " Client | "+LiquidSense.CLIENT_WEB+" | By AquaVit");
    private final BoolValue customValue = new BoolValue("Custom", false);

    private final MSTimer msTimer = new MSTimer();
    private long delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(msTimer.hasTimePassed(delay)) {
            mc.thePlayer.sendChatMessage(customValue.get() ? replace(messageValue.get()) : messageValue.get() + " >" + RandomUtils.randomString(5 + new Random().nextInt(5)) + "<");
            msTimer.reset();
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
        }
    }

    private String replace(String object) {
        final Random r = new Random();

        while(object.contains("%f"))
            object = object.substring(0, object.indexOf("%f")) + r.nextFloat() + object.substring(object.indexOf("%f") + "%f".length());

        while(object.contains("%i"))
            object = object.substring(0, object.indexOf("%i")) + r.nextInt(10000) + object.substring(object.indexOf("%i") + "%i".length());

        while(object.contains("%s"))
            object = object.substring(0, object.indexOf("%s")) + RandomUtils.randomString(r.nextInt(8) + 1) + object.substring(object.indexOf("%s") + "%s".length());

        while(object.contains("%ss"))
            object = object.substring(0, object.indexOf("%ss")) + RandomUtils.randomString(r.nextInt(4) + 1) + object.substring(object.indexOf("%ss") + "%ss".length());

        while(object.contains("%ls"))
            object = object.substring(0, object.indexOf("%ls")) + RandomUtils.randomString(r.nextInt(15) + 1) + object.substring(object.indexOf("%ls") + "%ls".length());
        return object;
    }
}
