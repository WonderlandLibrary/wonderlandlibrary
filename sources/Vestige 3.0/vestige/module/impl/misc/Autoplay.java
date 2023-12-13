package vestige.module.impl.misc;

import net.minecraft.network.play.server.S02PacketChat;
import vestige.event.Listener;
import vestige.event.impl.PacketReceiveEvent;
import vestige.event.impl.TickEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.misc.TimerUtil;

public class Autoplay extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Solo insane", "Solo normal", "Solo insane");

    private final IntegerSetting delay = new IntegerSetting("Delay", 1500, 0, 4000, 50);

    private final String winMessage = "You won! Want to play again? Click here!",
            loseMessage = "You died! Want to play again? Click here!";

    private final TimerUtil timer = new TimerUtil();

    private boolean waiting;

    public Autoplay() {
        super("Autoplay", Category.MISC);
        this.addSettings(mode, delay);
    }

    @Override
    public void onEnable() {
        waiting = false;
        timer.reset();
    }

    @Listener
    public void onTick(TickEvent event) {
        if(waiting && timer.getTimeElapsed() >= delay.getValue()) {
            String command = "";

            switch (mode.getMode()) {
                case "Solo normal":
                    command = "/play solo_normal";
                    break;
                case "Solo insane":
                    command = "/play solo_insane";
                    break;
            }

            mc.thePlayer.sendChatMessage(command);

            timer.reset();
            waiting = false;
        }
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if(event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = event.getPacket();

            String message = packet.getChatComponent().getUnformattedText();

            if((message.contains(winMessage) && message.length() < winMessage.length() + 3) || (message.contains(loseMessage) && message.length() < loseMessage.length() + 3)) {
                waiting = true;
                timer.reset();
            }
        }
    }

}
