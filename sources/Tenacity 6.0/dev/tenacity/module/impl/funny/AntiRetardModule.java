package dev.tenacity.module.impl.funny;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.util.misc.ChatUtil;
import dev.tenacity.util.misc.TimerUtil;

import java.util.ArrayList;

public final class AntiRetardModule extends Module {

    private final TimerUtil timerUtil = new TimerUtil();

    private float lastMovementYaw;

    private final ModeSetting mode = new ModeSetting("Mode", "Thoq Insult", "Thoq Kill");

    public AntiRetardModule() {
        super("Anti-Retard", "Harasses Retards", ModuleCategory.FUNNY);
        initializeSettings(mode);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if (event.isPre()) {
            switch (mode.getCurrentMode()) {
                case "Thoq Insult":
                    ArrayList<String> insults = new ArrayList<String>();
                        if (timerUtil.hasTimeElapsed(3000, true)) {
                            mc.thePlayer.sendChatMessage("Some babies were dropped on their heads, but Thoq was clearly thrown at a wall!");

                            mc.thePlayer.sendChatMessage("Thoq's skill is more disappointing than an unsalted pretzel.");

                            mc.thePlayer.sendChatMessage("Thoq, don’t be ashamed of who you are. That’s your parents’ job.");

                            if (timerUtil.hasTimeElapsed(5000, true)) {

                                mc.thePlayer.sendChatMessage("Roses are red, violets are blue, God made me amazing, what happened to Thoq?");

                                mc.thePlayer.sendChatMessage("Thoq is not as bad as people say, he's much, much worse.");

                                mc.thePlayer.sendChatMessage("How old are you Thoq? - Wait, I shouldn't ask, you can't count that high.");

                                mc.thePlayer.sendChatMessage("How did you get here Thoq? Did someone leave your cage open?");

                                if (timerUtil.hasTimeElapsed(5000, true)) {

                                    mc.thePlayer.sendChatMessage("I was today years old when I realized I didn’t like Thoq.");

                                    mc.thePlayer.sendChatMessage("Thoq is the reason God created the middle finger.");

                                    mc.thePlayer.sendChatMessage("Thoq brings everyone so much joy! You know, when they leave. But, still!");

                                    mc.thePlayer.sendChatMessage("People clap when they see Thoq. They clap their hands over their eyes.");

                                    if (timerUtil.hasTimeElapsed(5000, true)) {

                                        mc.thePlayer.sendChatMessage("I’d say Thoq is ‘dumb as a giraffe,’ but at least a giraffe has a brain.");

                                        mc.thePlayer.sendChatMessage("Thoq should carry a plant around with them to replace the oxygen they waste.");

                                        mc.thePlayer.sendChatMessage("Aww, it’s so cute when Thoq tries to talk about things they don’t understand.");

                                        ChatUtil.print("Sent Message");
                                    }
                                }
                            }
                        }
            }
        }
        switch (mode.getCurrentMode()) {
            case "Thoq Kill":
                mc.thePlayer.sendChatMessage("Thoq is retarded, kill him");
                ChatUtil.print("Sent Message");
        }
    };

}