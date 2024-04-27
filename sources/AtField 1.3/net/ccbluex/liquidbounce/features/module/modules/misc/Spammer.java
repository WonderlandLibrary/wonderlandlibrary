/*
 * Decompiled with CFR 0.152.
 */
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
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 1000, 0, 5000){
        final Spammer this$0;
        {
            this.this$0 = spammer;
            super(string, n, n2, n3);
        }

        protected void onChanged(Integer n, Integer n2) {
            int n3 = (Integer)Spammer.access$000(this.this$0).get();
            if (n3 > n2) {
                this.set((Object)n3);
            }
            Spammer.access$102(this.this$0, TimeUtils.randomDelay((Integer)Spammer.access$000(this.this$0).get(), (Integer)Spammer.access$200(this.this$0).get()));
        }

        @Override
        protected void onChanged(Object object, Object object2) {
            this.onChanged((Integer)object, (Integer)object2);
        }
    };
    private final BoolValue customValue;
    private long delay;
    private final MSTimer msTimer;
    private final TextValue messageValue;
    private final IntegerValue minDelayValue = new IntegerValue(this, "MinDelay", 500, 0, 5000){
        final Spammer this$0;

        @Override
        protected void onChanged(Object object, Object object2) {
            this.onChanged((Integer)object, (Integer)object2);
        }
        {
            this.this$0 = spammer;
            super(string, n, n2, n3);
        }

        protected void onChanged(Integer n, Integer n2) {
            int n3 = (Integer)Spammer.access$200(this.this$0).get();
            if (n3 < n2) {
                this.set((Object)n3);
            }
            Spammer.access$102(this.this$0, TimeUtils.randomDelay((Integer)Spammer.access$000(this.this$0).get(), (Integer)Spammer.access$200(this.this$0).get()));
        }
    };

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (this.msTimer.hasTimePassed(this.delay)) {
            mc.getThePlayer().sendChatMessage((Boolean)this.customValue.get() != false ? this.replace((String)this.messageValue.get()) : (String)this.messageValue.get() + " >" + RandomUtils.randomString(5 + new Random().nextInt(5)) + "<");
            this.msTimer.reset();
            this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
        }
    }

    static long access$102(Spammer spammer, long l) {
        spammer.delay = l;
        return spammer.delay;
    }

    public Spammer() {
        this.messageValue = new TextValue("Message", "AtField Client | liquidbounce(.net) | CCBlueX on yt");
        this.customValue = new BoolValue("Custom", false);
        this.msTimer = new MSTimer();
        this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
    }

    static IntegerValue access$000(Spammer spammer) {
        return spammer.minDelayValue;
    }

    static IntegerValue access$200(Spammer spammer) {
        return spammer.maxDelayValue;
    }

    private String replace(String string) {
        Random random = new Random();
        while (string.contains("%f")) {
            string = string.substring(0, string.indexOf("%f")) + random.nextFloat() + string.substring(string.indexOf("%f") + "%f".length());
        }
        while (string.contains("%i")) {
            string = string.substring(0, string.indexOf("%i")) + random.nextInt(10000) + string.substring(string.indexOf("%i") + "%i".length());
        }
        while (string.contains("%s")) {
            string = string.substring(0, string.indexOf("%s")) + RandomUtils.randomString(random.nextInt(8) + 1) + string.substring(string.indexOf("%s") + "%s".length());
        }
        while (string.contains("%ss")) {
            string = string.substring(0, string.indexOf("%ss")) + RandomUtils.randomString(random.nextInt(4) + 1) + string.substring(string.indexOf("%ss") + "%ss".length());
        }
        while (string.contains("%ls")) {
            string = string.substring(0, string.indexOf("%ls")) + RandomUtils.randomString(random.nextInt(15) + 1) + string.substring(string.indexOf("%ls") + "%ls".length());
        }
        return string;
    }
}

