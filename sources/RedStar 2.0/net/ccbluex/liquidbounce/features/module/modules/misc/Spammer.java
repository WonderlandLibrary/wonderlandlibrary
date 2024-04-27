package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.Random;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.TextValue;

@ModuleInfo(name="Spammer", description="Spams the chat with a given message.", category=ModuleCategory.MISC)
public class Spammer
extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 1000, 0, 5000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int minDelayValueObject = (Integer)Spammer.this.minDelayValue.get();
            if (minDelayValueObject > newValue) {
                this.set(minDelayValueObject);
            }
            Spammer.this.delay = TimeUtils.randomDelay((Integer)Spammer.this.minDelayValue.get(), (Integer)Spammer.this.maxDelayValue.get());
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 500, 0, 5000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int maxDelayValueObject = (Integer)Spammer.this.maxDelayValue.get();
            if (maxDelayValueObject < newValue) {
                this.set(maxDelayValueObject);
            }
            Spammer.this.delay = TimeUtils.randomDelay((Integer)Spammer.this.minDelayValue.get(), (Integer)Spammer.this.maxDelayValue.get());
        }
    };
    private final TextValue messageValue = new TextValue("Message", "RedStar Client | liquidbounce(.net) | CCBlueX on yt");
    private final BoolValue customValue = new BoolValue("Custom", false);
    private final MSTimer msTimer = new MSTimer();
    private long delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.msTimer.hasTimePassed(this.delay)) {
            mc.getThePlayer().sendChatMessage((Boolean)this.customValue.get() != false ? this.replace((String)this.messageValue.get()) : (String)this.messageValue.get() + " >" + RandomUtils.randomString(5 + new Random().nextInt(5)) + "<");
            this.msTimer.reset();
            this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
        }
    }

    private String replace(String object) {
        Random r = new Random();
        while (object.contains("%f")) {
            object = object.substring(0, object.indexOf("%f")) + r.nextFloat() + object.substring(object.indexOf("%f") + "%f".length());
        }
        while (object.contains("%i")) {
            object = object.substring(0, object.indexOf("%i")) + r.nextInt(10000) + object.substring(object.indexOf("%i") + "%i".length());
        }
        while (object.contains("%s")) {
            object = object.substring(0, object.indexOf("%s")) + RandomUtils.randomString(r.nextInt(8) + 1) + object.substring(object.indexOf("%s") + "%s".length());
        }
        while (object.contains("%ss")) {
            object = object.substring(0, object.indexOf("%ss")) + RandomUtils.randomString(r.nextInt(4) + 1) + object.substring(object.indexOf("%ss") + "%ss".length());
        }
        while (object.contains("%ls")) {
            object = object.substring(0, object.indexOf("%ls")) + RandomUtils.randomString(r.nextInt(15) + 1) + object.substring(object.indexOf("%ls") + "%ls".length());
        }
        return object;
    }
}
