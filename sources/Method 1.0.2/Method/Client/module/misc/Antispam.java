package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

import static Method.Client.Main.setmgr;

public class Antispam extends Module {
    Setting unicode = setmgr.add(new Setting("Unicode", this, true));
    Setting broadcasts = setmgr.add(new Setting("Server Broadcasts", this, false));
    Setting spam = setmgr.add(new Setting("Repeated messages", this, true));
    Setting Similarity = setmgr.add(new Setting("Similarity %", this, .85, 0, 1, false, spam));


    public static List<Priorchatlist> priorchatlists = Lists.newArrayList();
    public static int line;

    public Antispam() {
        super("Antispam", Keyboard.KEY_NONE, Category.MISC, "Antispam");
    }

    @Subscribe
    public void ClientChatReceivedEvent(ClientChatReceivedEvent event) {
        if (spam.getValBoolean()) {
            GuiNewChat chatinst = mc.ingameGUI.getChatGUI();
            String Incomingtext = event.getMessage().getUnformattedText();

            for (Priorchatlist Oldchat : priorchatlists) {
                if (levenshteinDistance(Oldchat.fulltext, Incomingtext) >= Similarity.getValDouble()) {
                    Oldchat.spammedtimes++;
                    String change = TextFormatting.GRAY + " (" + TextFormatting.GOLD + "x" + Oldchat.spammedtimes + TextFormatting.GRAY + ")";
                    event.getMessage().appendText(change);

                    chatinst.deleteChatLine(Oldchat.Removable + 1);
                    Oldchat.Removable = line;
                    break;
                }
            }

            priorchatlists.add(new Priorchatlist(line, Incomingtext, 1));
            line++;
            if (!event.isCanceled())
                chatinst.printChatMessageWithOptionalDeletion(event.getMessage(), line);
            if (priorchatlists.size() > 1000) {
                priorchatlists.remove(0);
            }
            event.setCanceled(true);
        }
    }


    public static class Priorchatlist {
        public int Removable;
        public String fulltext;
        public int spammedtimes;

        public Priorchatlist(int Removable, String fulltext, int spammedtimes) {
            this.Removable = Removable;
            this.fulltext = fulltext;
            this.spammedtimes = spammedtimes;
        }
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof SPacketChat) {
            final SPacketChat packet2 = (SPacketChat) packet;
            if (this.broadcasts.getValBoolean()) {
                if (packet2.getChatComponent().getFormattedText().startsWith("\2475[SERVER]")) {
                    return false;
                }
            }
            if (this.unicode.getValBoolean()) {
                if (packet2.getChatComponent() instanceof TextComponentString) {
                    final TextComponentString component = (TextComponentString) packet2.getChatComponent();

                    final StringBuilder sb = new StringBuilder();

                    boolean containsUnicode = false;

                    for (String s : component.getFormattedText().split(" ")) {

                        StringBuilder line = new StringBuilder();

                        for (char c : s.toCharArray()) {
                            if (c >= 0xFEE0) {
                                c -= 0xFEE0;
                                containsUnicode = true;
                            }

                            line.append(c);
                        }

                        sb.append(line).append(" ");
                    }

                    if (containsUnicode) {
                        packet2.chatComponent = new TextComponentString(sb.toString());
                    }
                }
            }
        }
        return true;

    }

    public static double levenshteinDistance(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

}
