// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.misc;

import net.augustus.Augustus;
import net.augustus.utils.PlayerUtil;
import net.lenni0451.eventapi.reflection.EventTarget;
import org.apache.commons.lang3.RandomStringUtils;
import net.augustus.events.EventUpdate;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import java.util.HashMap;
import net.augustus.modules.Module;

public class Spammer extends Module
{
    private final char[] chars;
    private static final HashMap<Character, Character> lookalikes;
    public static String[] never_gonna;
    private int index;
    private final StringValue enumValue;
    private final DoubleValue delay;
    private final BooleanValue bipas;
    private final TimeHelper stopwatch;
    private final String[] cum;
    public static String spamString;
    
    public Spammer() {
        super("Spammer", new Color(73, 26, 245), Categorys.MISC);
        this.chars = new char[] { '\u26df', '\u26e0', '\u26e1', '\u26e2', '\u26e3', '\u26e4', '\u2753', '\u2e3b' };
        this.enumValue = new StringValue(1837699, "EnumValue", this, "RickRoll", new String[] { "Custom", "RickRoll", "Sus", "Sponsor" });
        this.delay = new DoubleValue(298637, "Delay", this, 3.0, 0.0, 10.0, 0);
        this.bipas = new BooleanValue(9382, "Bipas", this, false);
        this.stopwatch = new TimeHelper();
        this.cum = new String[] { "*intensive moaning*", "im gonna cum :D", "get xenza for insane waifus", "i love to cum uwu", "femboys are veri cute", "get xenza with minecraft sex mod support", "get an big orgasm now with xenza", "imma just get your moms pregnant using xenza", "B==)~ {()}", "uwu", "get xenza or no sex", "sex = gud", "my dick is so hard because of xenza's waifu support", "pornhub.com = free robux :pray:", "want to get infinite porn hub premium? buy xenza at jDev#7905", "want to suck of your homies dick? get xenza at jDev#7905", "want to get hard immediately? get xenza by jDev#7905", "femboys are on your way with xenza by jDev#7905", "wannt hallaal HINDI 2024 minecraft hackar clientert gut xenza at @ jDev#7905", "use spanky antihake please (jDev#7905)" };
    }
    
    public static String replaceWithUnicodeLookalike(final String str) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            final char ch = str.charAt(i);
            final char lookalike = Spammer.lookalikes.getOrDefault(ch, ch);
            result.append(lookalike);
        }
        return result.toString();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.stopwatch.reached((long)(this.delay.getValue() * 1000.0))) {
            final String selected = this.enumValue.getSelected();
            switch (selected) {
                case "Sponsor": {
                    if (this.RANDOM.nextBoolean()) {
                        Spammer.mc.thePlayer.sendChatMessage("StepWise on top <3 discord.gg/muvpzCj8MA");
                    }
                    else {
                        Spammer.mc.thePlayer.sendChatMessage("cheap minecraft server hosting by StepWise - discord.gg/muvpzCj8MA");
                    }
                    this.stopwatch.reset();
                    break;
                }
                case "Sus": {
                    try {
                        Spammer.mc.thePlayer.sendChatMessage(this.cum[this.RANDOM.nextInt(this.cum.length - 1)]);
                    }
                    catch (final ArrayIndexOutOfBoundsException exc) {
                        System.out.println("HARAM!!!!!!!!!!111");
                    }
                    this.stopwatch.reset();
                    break;
                }
                case "RickRoll": {
                    try {
                        Spammer.mc.thePlayer.sendChatMessage(Spammer.never_gonna[this.index++]);
                    }
                    catch (final ArrayIndexOutOfBoundsException exc) {
                        this.index = 0;
                    }
                    this.stopwatch.reset();
                    break;
                }
                case "Custom": {
                    if (this.bipas.getBoolean()) {
                        Spammer.mc.thePlayer.sendChatMessage(Spammer.spamString + " - " + RandomStringUtils.randomAlphabetic(32));
                    }
                    else {
                        Spammer.mc.thePlayer.sendChatMessage(Spammer.spamString);
                    }
                    this.stopwatch.reset();
                    break;
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.stopwatch.reset();
        this.index = 0;
        PlayerUtil.sendChat("Use .spammer [message] to edit the spam message.");
    }
    
    static {
        lookalikes = new HashMap<Character, Character>() {
            {
                this.put('a', '\u0430');
                this.put('b', '\u0184');
                this.put('c', '\u0441');
                this.put('d', '\u0501');
                this.put('e', '\u0435');
                this.put('f', '\u1e9d');
                this.put('g', '\u0261');
                this.put('h', '\u04bb');
                this.put('i', '\u0456');
                this.put('j', '\u03f3');
                this.put('k', '\u03ba');
                this.put('l', '\u04cf');
                this.put('m', '\u217f');
                this.put('n', '\u0578');
                this.put('o', '\u043e');
                this.put('p', '\u0440');
                this.put('q', '\u0566');
                this.put('r', '\u0433');
                this.put('s', '\ua731');
                this.put('t', 't');
                this.put('u', '\u03c5');
                this.put('v', '\u03bd');
                this.put('w', '\u051d');
                this.put('x', '\u0445');
                this.put('y', '\u0443');
                this.put('z', '\u0290');
            }
        };
        Spammer.never_gonna = new String[] { "We're no strangers to love", "You know the rules and so do I (do I)", "A full commitment's what I'm thinking of", "You wouldn't get this from any other guy", "I just wanna tell you how I'm feeling", "Gotta make you understand", "Never gonna give you up", "Never gonna let you down", "Never gonna run around and desert you", "Never gonna make you cry", "Never gonna say goodbye", "Never gonna tell a lie and hurt you", "We've known each other for so long", "Your heart's been aching, but you're too shy to say it (say it)", "Inside, we both know what's been going on (going on)", "We know the game and we're gonna play it", "And if you ask me how I'm feeling", "Don't tell me you're too blind to see", "Never gonna give you up", "Never gonna let you down", "Never gonna run around and desert you", "Never gonna make you cry", "Never gonna say goodbye", "Never gonna tell a lie and hurt you", "Never gonna give you up", "Never gonna let you down", "Never gonna run around and desert you", "Never gonna make you cry", "Never gonna say goodbye", "Never gonna tell a lie and hurt you", "We've known each other for so long", "Your heart's been aching, but you're too shy to say it (to say it)", "Inside, we both know what's been going on (going on)", "We know the game and we're gonna play it", "I just wanna tell you how I'm feeling", "Gotta make you understand", "Never gonna give you up", "Never gonna let you down", "Never gonna run around and desert you", "Never gonna make you cry", "Never gonna say goodbye", "Never gonna tell a lie and hurt you", "Never gonna give you up", "Never gonna let you down", "Never gonna run around and desert you", "Never gonna make you cry", "Never gonna say goodbye", "Never gonna tell a lie and hurt you", "Never gonna give you up", "Never gonna let you down", "Never gonna run around and desert you", "Never gonna make you cry", "Never gonna say goodbye", "Never gonna tell a lie and hurt you" };
        Spammer.spamString = Augustus.getInstance().getName() + " " + Augustus.getInstance().getVersion() + " by thebois industries";
    }
}
