package best.azura.client.impl.module.impl.other;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.network.play.server.S02PacketChat;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;


@ModuleInfo(name = "Kill Sults", description = "Kill sults make insults", category = Category.OTHER, keyBind = 0)
public class KillSults extends Module {
    private static final ArrayList<String> killSults = new ArrayList<>(), messageQueue = new ArrayList<>();
    private final DelayUtil delay = new DelayUtil();
    private final NumberValue<Long> delayValue = new NumberValue<>("Delay", "The delay between each kill insult", 0L, 250L, 0L, 10000L);
    private final long initTime;

    public KillSults() {
        super();
        initTime = System.currentTimeMillis();
        loadKillInsults();
    }

    public static void loadKillInsults() {
        killSults.clear();
        final File killInsultsFile = new File(Client.INSTANCE.getConfigManager().getClientDirectory(), "kill_insults.txt");
        if (!killInsultsFile.exists()) {
            try {
                PrintStream ps = new PrintStream(killInsultsFile);
                ps.println("//ADD KILL INSULTS HERE");
                ps.println("//ENEMY NAME PREFIX: %e% or %enemy%");
                ps.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BufferedReader bf = new BufferedReader(new FileReader(killInsultsFile));
                for (String s; (s = bf.readLine()) != null;) {
                    if (s.startsWith("//")) continue;
                    killSults.add(s);
                }
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getKillInsult() {
        Random random = new Random();
        return killSults.get(random.nextInt(killSults.size()));
    }

    @EventHandler
    public Listener<Event> eventListener = this::handle;

    public void handle(Event event) {
        if (System.currentTimeMillis() - initTime < 1000) return;
        if (event instanceof EventUpdate) {
            if (delay.hasReached(delayValue.getObject()) && messageQueue.size() != 0) {
                mc.thePlayer.sendChatMessage(messageQueue.remove(0));
                delay.reset();
            }
        }
        if (event instanceof EventReceivedPacket) {
            EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S02PacketChat) {
                S02PacketChat s = e.getPacket();
                String a = s.getChatComponent().getUnformattedText();
                if (mc.thePlayer == null || a == null || mc.theWorld == null) return;
                if (killSults.size() == 0) return;
                if (a.contains("was killed by " + mc.thePlayer.getName()) || a.contains("foi morto por " + mc.thePlayer.getName())
                        || a.contains("foi abatido(a) por " + mc.thePlayer.getName())
                        || a.contains("a été tué par " + mc.thePlayer.getName())
                        || a.contains("was thrown into the void by " + mc.thePlayer.getName())
                        || a.contains("was thrown off a cliff by " + mc.thePlayer.getName())
                        || a.contains("was toasted by " + mc.thePlayer.getName())
                        || a.contains("was slain by " + mc.thePlayer.getName())
                        || a.contains("killed by " + mc.thePlayer.getName())) {
                    String[] split = a.replace(" was killed by " + mc.thePlayer.getName(), "")
                            .replace(" foi morto por " + mc.thePlayer.getName(), "")
                            .replace(" foi abatido(a) por " + mc.thePlayer.getName(), "")
                            .replace(" a été tué par " + mc.thePlayer.getName(), "")
                            .replace(" was thrown into the void by " + mc.thePlayer.getName(), "")
                            .replace(" was thrown off a cliff by " + mc.thePlayer.getName(), "")
                            .replace(" was slain by " + mc.thePlayer.getName(), "")
                            .replace(" was toasted by " + mc.thePlayer.getName(), "")
                            .replace(" killed by " + mc.thePlayer.getName(), "")
                            .replace("FINAL KILL!", "")
                            .replace("FINAL KILL", "")
                            .replace(".", "")
                            .replace("!", "")
                            .replace(mc.thePlayer.getName() + " ", "").split("\\+")[0].split("with")[0].split(" ");
                    String name = split[split.length - 1];
                    messageQueue.add(getKillInsult().replace("%player%", "%enemy%").replace("%enemy%", name).replace("%e%", name)
                            .replace("%user%", Client.INSTANCE.getUsername()));
                }
            }
        }
    }
}
