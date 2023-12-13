package me.nyan.flush.module.impl.misc;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.utils.other.MathUtils;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KillSults extends Module {
    private static final File dataFile = new File(Flush.getClientPath(), "killsults.txt");

    private static final List<String> killsultList = new ArrayList<>();

    private final ModeSetting mode = new ModeSetting("Mode", this, "Custom", "Custom", "Sigma");
    private final BooleanSetting mentionPlayerName = new BooleanSetting("Mention player name", this, true);

    public KillSults() {
        super("KillSults", Category.MISC);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        load();
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if(e.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = e.getPacket();
            String text = packet.getChatComponent().getUnformattedText();
            String serverIP = mc.getCurrentServerData().serverIP.toLowerCase();
            String username = mc.thePlayer.getName();

            if (serverIP.contains("brwserv")) {
                if(text.contains("a été tué par " + username)) {
                    String targetname = text.split(" a été tué par ")[0];
                    insult(targetname);
                }
            } else if (serverIP.contains("cubecraft")) {
                if(text.contains("was slain by " + username)) {
                    String targetname = text.split(" was slain by ")[0];
                    insult(targetname);
                }
            } else if (serverIP.contains("redesky")) {
                if(text.contains("foi morto por " + username)) {
                    String targetname = text.split(" foi morto por ")[0];
                    insult(targetname);
                }
            } else if (serverIP.contains("funcraft")) {
                if (text.contains(" a été tué par " + username)) {
                    String targetname = text.split(" a été tué par ")[0].replace("\uEEEE", "").replace("⚔", "");
                    insult(targetname);
                }
                if (text.contains(" a été tué par le vide et " + username)) {
                    String targetname = text.split(" a été tué par le vide et ")[0].replace("\uEEEE", "").replace("⚔", "");
                    insult(targetname);
                }
            } else if (serverIP.contains("blocksmc")) {
                if (text.contains(" was killed by " + username)) {
                    String targetname = text.split(" was killed by ")[0].replace("\uEEEE", "")
                            .replace("⚔", "");
                    insult(targetname);
                }
            }
        }
    }

    private void insult(String targetName) {
        List<String> killsults = mode.is("custom") ? killsultList : getSigmaKillsults();
        String killSult = (mc.getCurrentServerData().serverIP.toLowerCase().contains("funcraft") ? "@" : "") +
                (killsults.isEmpty() ? "L" + (mentionPlayerName.getValue() ? " -" : "") :
                killsults.get(MathUtils.getRandomNumber(0, killsults.size())));

        mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(killSult + (mentionPlayerName.getValue()
                ? " " + targetName : "")));
    }

    private List<String> getSigmaKillsults() {
        return Arrays.asList("Learn your alphabet with the sigma client: Omikron, Sigma, Epsilon, Alpha!",
                "Download Sigma to kick ass while listening to some badass music!",
                "Why Sigma? Cause it is the addition of pure skill and incredible intellectual abilities",
                "Want some skills? Check out sigma client.Info!",
                "You have been oofed by Sigma oof oof",
                "I am not racist, but I only like Sigma users. so git gut noobs",
                "Quick Quiz: I am zeus's son, who am I? SIGMA",
                "Wow! My combo is Sigma'n!",
                "What should I choose? Sigma or Sigma?",
                "Bigmama and Sigmama",
                "I don't hack I just sigma",
                "Sigma client . Info is your new home",
                "Look a divinity! He definitely must use sigma!",
                "In need of a cute present for Christmas? Sigma is all you need!",
                "I have a good sigma config, don't blame me",
                "Don't piss me off or you will discover the true power of Sigma's inf reach",
                "Sigma never dies",
                "Maybe I will be Sigma, I am already Sigma",
                "Sigma will help you! Oops, i killed you instead.",
                "NoHaxJustSigma",
                "Do like Tenebrous, subscribe to LeakedPvP!",
                "Did I really just forget that melody? Si sig sig sig Sigma",
                "Sigma. The only client run by speakers of Breton",
                "Order free baguettes with Sigma client",
                "Another Sigma user? Awww man",
                "Sigma utility client no hax 100%",
                "Hypixel wants to know Sigma owner's location [Accept] [Deny]",
                "I am a sig-magician, thats how I am able to do all those block game tricks",
                "Stop it, get some help! Get Sigma",
                "Sigma users belike: Hit or miss I guess I never miss!",
                "I dont hack i just have Sigma Gaming Chair",
                "Stop Hackustation me cuz im just Sigma",
                "S. I. G. M. A. Hack with me today!",
                "Subscribe to MentalFrostbyte on youtube and discover Jello for Sigma!",
                "Beauty is not in the face; beauty is in Jello for Sigma",
                "Imagine using anything but Sigma",
                "No hax just beta testing the anti-cheat with Sigma",
                "Don't forget to report me for Sigma on the forums!",
                "Search sigmaclient , info to get the best mineman skills!",
                "don't use Sigma? ok boomer",
                "Sigma is better than Optifine",
                "It's not Scaffold it's BlockFly in Jello for Sigma!",
                "How come a noob like you not use Sigma?",
                "A mother becomes a true grandmother the day she gets Sigma 5.0",
                "Fly faster than light, only available in Sigma™",
                "Behind every Sigma user, is an incredibly cool human being. Trust me, cooler than you.",
                "Hello Sigma my old friend...",
                "#SwitchToSigma5",
                "What? You've never downloaded Jello for Sigma? You know it's the best right?",
                "Your client sucks, just get Sigma",
                "Sigma made this world a better place, killing you with it even more");
    }

    public void save() {
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            PrintWriter printWriter = new PrintWriter(dataFile);

            for (String str : killsultList)
                printWriter.println(str);

            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!dataFile.exists()) {
            return;
        }
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        killsultList.clear();
        killsultList.addAll(lines);
    }

    public List<String> getKillsults() {
        return killsultList;
    }
}
