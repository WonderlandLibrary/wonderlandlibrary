/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.LiquidBounce;
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

import java.util.Random;

@ModuleInfo(name = "Spammer", description = "Spams the chat with a given message.", category = ModuleCategory.MISC)
public class Spammer extends Module {

        private final IntegerValue maxDelayValue = new IntegerValue("Kyino | SKIDDER GANG", 0, 0, 0) {
                @Override
                protected void onChanged(final Integer oldValue, final Integer newValue) {
                        final int minDelayValueObject = 3200;

                        if(minDelayValueObject > newValue)
                                set(minDelayValueObject);
                        delay = TimeUtils.randomDelay(3200, 3600);
                }
        };

        private final IntegerValue minDelayValue = new IntegerValue("Kyino | SKIDDER GANG", 0, 0, 0) {

                @Override
                protected void onChanged(final Integer oldValue, final Integer newValue) {
                        final int maxDelayValueObject = 3600;

                        if(maxDelayValueObject < newValue)
                                set(maxDelayValueObject);
                        delay = TimeUtils.randomDelay(3200, 3600);
                }
        };

        private final TextValue messageValue = new TextValue("Message", "ＤｉｎｅＴｉｒｅ Ｃｌｉｅｎｔ | Ｉｎｖｅｎｔｏｒｓ: Ｓｏｖｅｒｙ ＢＲＩＺ ａｎｄ _Ｚ１ ＳｏｆｔＷａｒｅ ｏｎｌｙ ｆｏｒ ＡＡＣ");
        private final BoolValue customValue = new BoolValue("Custom", false);

        private final MSTimer msTimer = new MSTimer();
        private long delay = TimeUtils.randomDelay(3200, 3600);

        @EventTarget
        public void onUpdate(UpdateEvent event) {
                if(msTimer.hasTimePassed(delay)) {
                        mc.thePlayer.sendChatMessage(customValue.get() ? replace(messageValue.get()) : messageValue.get() + " >" + RandomUtils.randomString(1 + new Random().nextInt(1)) + "<");
                        msTimer.reset();
                        delay = TimeUtils.randomDelay(3200, 3600);
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
