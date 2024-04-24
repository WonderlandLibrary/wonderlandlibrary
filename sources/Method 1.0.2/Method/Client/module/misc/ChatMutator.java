package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import net.minecraft.network.play.client.CPacketChatMessage;
import org.lwjgl.input.Keyboard;

import java.util.Random;

import static Method.Client.Main.setmgr;

public class ChatMutator extends Module {
    /////////////////////
    public ChatMutator() {
        super("ChatMutator", Keyboard.KEY_NONE, Category.MISC, "ChatMutator");
    }

    Setting mode = setmgr.add(new Setting("Mode", this, "FANCY", "LEET", "FANCY", "DUMB", "CONSOLE", "BYPASS"));
    /////////////////////

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof CPacketChatMessage) {
            final CPacketChatMessage packet2 = (CPacketChatMessage) packet;
            if (packet2.getMessage().startsWith("/") || packet2.getMessage().startsWith("&")) {
                return false;
            }
            if (mode.getValString().equalsIgnoreCase("BYPASS")) {
                StringBuilder msg = new StringBuilder();
                char[] charArray = packet2.getMessage().toCharArray();
                for (char c : charArray) {
                    msg.append(c).append("؜");
                }
                Wrapper.INSTANCE.sendPacket((new CPacketChatMessage(msg.toString().replaceFirst("%", ""))));
                return false;
            }
            if (mode.getValString().equalsIgnoreCase("leet"))
                packet2.message = leetSpeak(packet2.message);

            if (mode.getValString().equalsIgnoreCase("FANCY"))
                packet2.message = fancy(packet2.message);

            if (mode.getValString().equalsIgnoreCase("DUMB"))
                packet2.message = dumb(packet2.message);

            if (mode.getValString().equalsIgnoreCase("CONSOLE"))
                packet2.message = console(packet2.message);

        }

        return true;
    }

    public String leetSpeak(String input) {
        input = input.toLowerCase().replace("a", "4");
        input = input.toLowerCase().replace("e", "3");
        input = input.toLowerCase().replace("g", "9");
        input = input.toLowerCase().replace("h", "1");
        input = input.toLowerCase().replace("o", "0");
        input = input.toLowerCase().replace("s", "5");
        input = input.toLowerCase().replace("t", "7");
        input = input.toLowerCase().replace("i", "1");
        return input;
    }

    public String fancy(String input) {
        final StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c >= 0x21 && c <= 0x80) {
                sb.append(Character.toChars(c + 0xFEE0));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public String dumb(String input) {
        final StringBuilder sb = new StringBuilder(input);

        for (int i = 0; i < sb.length(); i += 2) {
            sb.replace(i, i + 1, sb.substring(i, i + 1).toUpperCase());
        }

        return sb.toString();
    }

    public String console(String input) {
        StringBuilder ret = new StringBuilder();

        final char[] unicodeChars = new char[]{'\u2E3B',
                '\u26D0',
                '\u26E8',
                '\u26BD',
                '\u26BE',
                '\u26F7',
                '\u23EA',
                '\u23E9',
                '\u23EB',
                '\u23EC',
                '\u2705',
                '\u274C',
                '\u26C4'};

        final int length = input.length();

        for (int i = 1, current = 0; i <= length || current < length; current = i, i += 1) {
            if (current != 0) {
                final Random random = new Random();

                for (int j = 0; j <= 2; j++) {
                    ret.append(unicodeChars[random.nextInt(unicodeChars.length)]);
                }
            }
            if (i <= length) {
                ret.append(input, current, i);
            } else {
                ret.append(input.substring(current));
            }
        }

        return ret.toString();
    }

}
