package tech.atani.client.feature.module.impl.chat;

import net.minecraft.network.play.client.C01PacketChatMessage;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.MultiStringBoxValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.java.StringUtil;
import tech.atani.client.utility.math.random.RandomUtil;

import java.security.SecureRandom;

@ModuleData(name = "AntiAntiSwear", description = "Prevents detection of naughty words", category = Category.CHAT)
public class AntiAntiSwear extends Module {

    private MultiStringBoxValue langs = new MultiStringBoxValue("Languages", "In what languages should the module try and detect swears?", this, new String[]{"English"}, new String[]{"English", "Czech", "Finnish"});

    private final String[] ENGLISH = new String[] {
        "autist", "autism", "arse", "ass", "bastard", "bitch", "fucker", "shit", "cock", "crap", "cunt", "damn", "dick", "fuck", "kike", "nigga", "nigger", "piss", "pussy", "shit", "slut", "whore", "turd", "twat", "wanker", "fag", "tranny", "tard", "degen", "kill", "suicide"
    };

    private final String[] CZECH = new String[] {
        "prdel", "hovn", "kurv", "píč", "pic", "srač", "sráč", "srac", "negře", "negr", "buzn", "kund", "děvk", "devk", "chcíp", "chcip", "zabí", "zabi", "ojeb", "ojed", "opích", "opich"
    };

    private final String[] FINNISH = new String [] {
            "autisti", "autismi", "perse", "kusipää", "mulkku", "vittuilia", "paska", "kulli", "saatana", "muna", "vittu", "nekru", "neekeri", "kusi", "huora", "tapa", "itsemurha"
    };

    private final StringBoxValue mode = new StringBoxValue("Mode", "Which method to use?", this, new String[]{"Parentheses"});

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if(mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        if(packetEvent.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage c01 = (C01PacketChatMessage) packetEvent.getPacket();
            String newMessage = c01.getMessage();
            if(this.langs.getValue().contains("English")) {
                for(String s : ENGLISH) {
                    newMessage = replace(newMessage, s);
                }
            }
            if(this.langs.getValue().contains("Czech")) {
                for(String s : CZECH) {
                    newMessage = replace(newMessage, s);
                }
            }
            if(this.langs.getValue().contains("Finnish")) {
                for(String s : FINNISH) {
                    newMessage = replace(newMessage, s);
                }
            }
            c01.setMessage(newMessage);
        }
    }

    private SecureRandom secureRandom = new SecureRandom();

    public String replace(String originalMessage, String swear) {
        StringBuilder result = new StringBuilder(swear);
        switch (mode.getValue()) {
            case "Parentheses":
                int randomIndex = secureRandom.nextInt(swear.length());
                String before = StringUtil.multiplyString("(", (int) RandomUtil.randomBetween(1, 3));
                String after = StringUtil.multiplyString(")", (int) RandomUtil.randomBetween(1, 3));
                result.insert(randomIndex, before);
                result.insert(randomIndex + before.length() + 1, after);
                break;
        }
        return originalMessage.replace(swear, result.toString());
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}
