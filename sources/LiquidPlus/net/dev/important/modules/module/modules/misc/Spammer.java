/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.misc;

import java.util.Random;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.misc.RandomUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.utils.timer.TimeUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.TextValue;

@Info(name="Spammer", description="Spams the chat with a given message.", category=Category.MISC, cnName="\u5ba3\u4f20\u5668")
public class Spammer
extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 1000, 0, 5000, "ms"){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int minDelayValueObject = (Integer)Spammer.this.minDelayValue.get();
            if (minDelayValueObject > newValue) {
                this.set(minDelayValueObject);
            }
            Spammer.this.delay = TimeUtils.randomDelay((Integer)Spammer.this.minDelayValue.get(), (Integer)Spammer.this.maxDelayValue.get());
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 500, 0, 5000, "ms"){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int maxDelayValueObject = (Integer)Spammer.this.maxDelayValue.get();
            if (maxDelayValueObject < newValue) {
                this.set(maxDelayValueObject);
            }
            Spammer.this.delay = TimeUtils.randomDelay((Integer)Spammer.this.minDelayValue.get(), (Integer)Spammer.this.maxDelayValue.get());
        }
    };
    private final TextValue messageValue = new TextValue("Message", "LiquidPlus Client | liquidbounce(.net) | CCBlueX on yt");
    private final BoolValue customValue = new BoolValue("Custom", false);
    private final MSTimer msTimer = new MSTimer();
    private long delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.msTimer.hasTimePassed(this.delay)) {
            Spammer.mc.field_71439_g.func_71165_d((Boolean)this.customValue.get() != false ? this.replace((String)this.messageValue.get()) : (String)this.messageValue.get() + " >" + RandomUtils.randomString(5 + new Random().nextInt(5)) + "<");
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

