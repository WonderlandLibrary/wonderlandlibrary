package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S02PacketChat;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.impl.combat.aura.Aura;
import rip.helium.event.minecraft.ProcessPacketEvent;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.StringsProperty;

public class KillSults extends Cheat {
    public static BooleanProperty filter_bypass = new BooleanProperty("Filter Bypass", "Bypasses the filter on hypixel", null, false);
    public static BooleanProperty antispam = new BooleanProperty("Anti-Spam Bypass", "Spam bypass", null, false);
    public StringsProperty mode;
    public BooleanProperty autogg;
    public BooleanProperty killsults = new BooleanProperty("Kill Sults", "Insult a player when you kill them!", null, false);
    int counter;
    Stopwatch timer;

    public KillSults() {
        super("ChatMods", "Chat Mods.", CheatCategory.VISUAL);
        this.timer = new Stopwatch();
        this.mode = new StringsProperty("KillSult Mode", "", null, false, true, new String[]{"AutoL", "Insult"}, new Boolean[]{true, false});
        this.autogg = new BooleanProperty("AutoGG", "", null, true);
        this.timer = new Stopwatch();
        this.registerProperties(killsults, mode, filter_bypass, autogg, antispam);
    }

    public void onEnable() {
        this.counter = 0;
    }

    @Collect
    public void onPacket(final ProcessPacketEvent e) {
        if (killsults.getValue()) {
            if (e.getPacket() instanceof S02PacketChat) {
                final S02PacketChat packet = (S02PacketChat) e.getPacket();
                final String msg = packet.getChatComponent().getUnformattedText();
                try {
                    if (msg.contains("Has Killed") || (msg.contains("killed by") || (msg.contains("thrown off a cliff") || (msg.contains("slain by")) || (msg.contains("thrown into the void"))))) {
                        if (msg.contains(String.valueOf(Cheat.getPlayer().getName()))) {
                            //for (final Object o : KillSults.mc.theWorld.getLoadedEntityList()) {
                            if (mc.thePlayer != null) {
                                final EntityLivingBase ent = Aura.getCurrentTarget();
                                if (!msg.contains(mc.thePlayer.getName())) {
                                    return;
                                }
                                if (this.mode.getValue().get("AutoL")) {
                                    KillSults.mc.thePlayer.sendChatMessage("L, " + ent.getName());
                                } else if (this.mode.getValue().get("Insult")) {
                                    final String[] messages = {"Did ur parents ask you to run away, " + ent.getName(), ent.getName() + " I'd tell you to uninstall, but your aim is so bad you wouldn't hit the button.", "I don't cheat, " + ent.getName() + " I just use Helium.", "You do be lookin' kinda bad at the game, " + ent.getName(), "Did someone leave your cage open " + ent.getName() + "?", "rage at me on discord Kansio#6759" + ent.getName(), ent.getName() + " you were the inspiration for birth control.", "Is being in the spectator mode fun, " + ent.getName() + "?", ent.getName() + " you're the type of guy to quickdrop irl", ent.getName() + " got an F on the iq test.", "I understand why your parents abused you, " + ent.getName(), "Do you practice being this bad, " + ent.getName(), "hi my name is " + ent.getName() + " and my iq is -420!", ent.getName() + "'s aim is sponsored by Parkinson's", ent.getName() + " go take a long walk on a short bridge", ent.getName() + " probably plays fortnite lmao.", "plz no repotr i no want ban " + ent.getName() + "!", ent.getName() + ", you probably have the coronavirus.", ent.getName() + ", you really like taking L's.", ent.getName() + " drown in your own salt", ent.getName() + ", I'm not saying you're worthless, but i'd unplug ur lifesupport to charge my phone.", ent.getName() + ", could you please commit not alive?", ent.getName() + " I don't cheat, you just need to click faster", ent.getName() + " I speak english not your gibberish", "Your mom do be lookin' kinda black doe, " + ent.getName(), "Hey look! It's a fortnite player " + ent.getName(), "Need some pvp advice? " + ent.getName() + ".", ent.getName() + ", do you really like dying this much?", ent.getName() + " probably reported me.", ent.getName() + " you're the type to get 3rd place in a 1v1.", ent.getName() + " how does it feel to get stomped on?", ent.getName() + ", the type of guy to use sigma.", ent.getName() + " that's a #VictoryRoyale! better luck next time!", "lol " + ent.getName() + " probably speaks dog eater", ent.getName() + " is a fricking monkey (black person)", ent.getName() + " be like: ''I'm black and this a robbery''", ent.getName() + ", even your mom is better than you in this game.", ent.getName() + " go back to fortnite you degenerate.", ent.getName() + ", were condoms named after you?", ent.getName() + " your iq is that of a steve.", ent.getName() + " go commit stop breathing plz", ent.getName() + ", your parents abandoned you, then the orphanage did the same", ent.getName() + " probably bought sigma premium", ent.getName() + " probably got an error on his hello world program lmao", ent.getName() + " how'd you hit the download button with that aim", "Someone in 1940 forgot to gas you, " + ent.getName() + " :)", ent.getName() + ", did your dad go get milk and never return?", ent.getName() + " you died in a block game.", ent.getName() + " thinks that his ping is equal to his iq.", ent.getName() + " stop eating dogs", "if the body is 70% water then how is " + ent.getName() + "'s body 100% salt?", "yo stop spreading corona, " + ent.getName() + ". I know you're asian, but stop spreading it.", ent.getName() + "'s got dropped him on his head by his parents.", "yo " + ent.getName() + " come rage at me on discord Kansio#6759", ent.getName() + " doesn't have parents L", "how are you so bad? im losing brain cells while watching you play", ent.getName() + " even lolitsalex has more wins than you", "some kids were dropped at birth, but " + ent.getName() + " got thrown at the wall.", ent.getName() + " black"};
                                    if (this.counter >= messages.length) {
                                        this.counter = 0;
                                    }
                                    KillSults.mc.thePlayer.sendChatMessage(messages[this.counter]);
                                }
                                ++this.counter;
                            }
                            //}
                        }
                    }
                } catch (Exception GAY) {
                    GAY.printStackTrace();
                }
            }
        }
    }

    @Collect
    public void onPacket3(final ProcessPacketEvent e) {
        if (autogg.getValue()) {
            if (e.getPacket() instanceof S02PacketChat) {
                final S02PacketChat packet = (S02PacketChat) e.getPacket();
                final String msg = packet.getChatComponent().getUnformattedText();
                if ((msg.contains("You won!") || msg.contains("Has Won The Game!") || msg.contains("1st Place")) && this.timer.hasPassed(4000.0)) {
                    KillSults.mc.thePlayer.sendChatMessage("GG! Are you mad at me for hacking?");
                }
            }
        }
    }

    @Collect
    public void chat(ProcessPacketEvent e) {
        if (autogg.getValue()) {
            if (e.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) e.getPacket();
                String msg = packet.getChatComponent().getUnformattedText();
                if (msg.contains("Chat> Chat is no longer silenced.")) {
                    mc.thePlayer.sendChatMessage("I'm using the Helium client, gl!");
                }
            }
        }
    }

    /*/@Collect
    public void nmasad(final ProcessPacketEvent e) {
        if (e.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packet = (S02PacketChat)e.getPacket();
            final String msg = packet.getChatComponent().getUnformattedText();
            final boolean cansay = true;
            if (msg.contains("why hack") || msg.contains("why cheat") || msg.contains("why do u cheat") || msg.contains("why do you hack") || msg.contains("hack") || msg.contains("cheat") || msg.contains("Hack") || (msg.contains("hax") && !msg.contains(KillSults.mc.thePlayer.getName() + ":"))) {
                KillSults.mc.thePlayer.sendChatMessage("no cheat i just use helium gamer mod");
            }
        }
    }/*/
}
