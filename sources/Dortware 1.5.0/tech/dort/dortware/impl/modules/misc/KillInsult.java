package tech.dort.dortware.impl.modules.misc;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.server.S02PacketChat;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.impl.BooleanValue;
import tech.dort.dortware.impl.events.PacketEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KillInsult extends Module {

    private final List<String> insults = new ArrayList<>();

    private final BooleanValue shout = new BooleanValue("Shout", this, false);

    public KillInsult(ModuleData moduleData) {
        super(moduleData);
        register(shout);
        File file = new File("dw-killsults.txt");
        if (!file.exists() || file.getTotalSpace() == 0 /*in case something goes wrong */) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(KillInsult.class.getResourceAsStream("/dw-killsults.txt")));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    this.insults.add(line);
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String str : insults) {
                    try {
                        writer.write(str + "\n");
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
                writer.close();
                bufferedReader.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    insults.add(line);
                }
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    private String randomMessage() {
        return insults.get(ThreadLocalRandom.current().nextInt(0, insults.size() - 1));
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.thePlayer != null && event.getPacket() instanceof S02PacketChat) {
            final String look = "killed by " + mc.thePlayer.getName();
            final String look33 = mc.thePlayer.getName() + " killed ";
            final String look2 = "slain by " + mc.thePlayer.getName();
            final String look3 = "void while escaping " + mc.thePlayer.getName();
            final String look67 = "You have killed ";
            final String look4 = "was killed with magic while fighting " + mc.thePlayer.getName();
            final String look5 = "couldn't fly while escaping " + mc.thePlayer.getName();
            final String look6 = "fell to their death while escaping " + mc.thePlayer.getName();
            final String look7 = "You recieved a reward for killing ";
            final String look8 = "was thrown into the void by " + mc.thePlayer.getName();
            final String look9 = "foi morto por " + mc.thePlayer.getName();
            final String look10 = "was thrown off a cliff by " + mc.thePlayer.getName();
            final S02PacketChat cp = event.getPacket();
            final String cp21 = cp.getChatComponent().getUnformattedText();
            if (cp21.startsWith(look67) || cp21.startsWith(look33) || (cp21.startsWith(mc.thePlayer.getName() + "(") && cp21.contains("asesino ha")) || cp21.startsWith("Has ganado ") || cp21.contains(look)
                    || cp21.contains(look2) || cp21.contains(look3)
                    || cp21.contains("You have been rewarded $50 and 2 point(s)!") || cp21.contains(look4) || cp21.contains(look5) || cp21.contains(look6) || cp21.contains(look7) || cp21.contains(look8) || cp21.contains(look9) || cp21.contains(look10)) {
                mc.thePlayer.sendChatMessage(shout.getValue() ? "/shout " + randomMessage() : randomMessage());
            }
        }
    }
}
