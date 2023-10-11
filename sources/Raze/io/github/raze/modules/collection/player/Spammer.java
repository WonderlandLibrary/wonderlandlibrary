package io.github.raze.modules.collection.player;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.math.TimeUtil;

import java.awt.*;

public class Spammer extends AbstractModule {

    private final ArraySetting spamMode;
    private final NumberSetting delay;

    private final TimeUtil timer;
    private final List razeMsg = new List();

    public Spammer() {
        super("Spammer", "Spams Messages In Chat.", ModuleCategory.PLAYER);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                delay = new NumberSetting(this, "Delay", 200, 5000, 3050),
                spamMode = new ArraySetting(this, "Spam Mode", "Raze", "Raze", "Trolled")

        );
        timer = new TimeUtil();
    }



    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            if(timer.elapsed(delay.get().intValue(), true)) {
                switch(spamMode.get()) {
                    case "Raze":
                        razeMsg.add("Raze is the BEST minecraft client! discord.gg/f9HasrqnBe");
                        razeMsg.add("discord.gg/f9HasrqnBe");
                        razeMsg.add("Just get it! discord.gg/f9HasrqnBe");
                        razeMsg.add("Better than Lunar & Badlion combined! discord.gg/f9HasrqnBe");
                        razeMsg.add(mc.thePlayer.getName() + " is smart, he uses raze!");
                        double number = Math.round(Math.random() * razeMsg.getItemCount());
                        mc.thePlayer.sendChatMessage(razeMsg.getItem((int) number));
                        break;
                    case "Trolled":
                        mc.thePlayer.sendChatMessage("You have been trolled! TROLLED BY RAZE CLIENT!");
                        break;
                }
            }
        }
    }

}
