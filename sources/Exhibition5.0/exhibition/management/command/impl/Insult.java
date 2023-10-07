// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import exhibition.util.misc.ChatUtil;
import java.util.Random;
import exhibition.management.command.Command;

public class Insult extends Command
{
    public Insult(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        final String[] pre = { "You", "u", "You fucking", "I hate you, you", "Go die in a hole you", "Go fuck a duck you", "Go have a gay orgy with your dad you", "Kill your self you", "Go die in a fire you", "Eat my pants you", "Go have sex with your cousin you", "Go cry about your micropenis you", "Go fist a cat you", "Go take some more ritalin you", "dull", "bloody", "you bloody", "you fucking shit filled" };
        final String[] post = { "nodus user", "fat ass", "lard ass", "fatty", "pig", "homosexual", "terrorist", "muslim", "sand nigger", "piece of shit", "nigger", "cunt", "jew", "fag", "faggot", "fucker", "motherfucker", "nazi", "bitch", "woman", "gay ass mother fucker", "runt", "asshole", "ass", "douche", "douchebag", "asshat", "smart ass", "tampon", "dumbass", "retard", "douchefag", "monkey", "Windows 8 user", "pretentious piece of shit", "femo-nazi", "mutant", "wet piece of shit", "cocksucker", "moron", "anus", "anal bouncer", "ball licker", "ballsack", "cumslut", "cum eater", "cum sponge", "cum bucket", "crackwhore", "cum guzzler", "parasite infested piece of shit", "twat", "shitface", "shit sniffer", "redneck", "swag fag", "unclefucker", "cumrag", "dick cheese", "cum stain", "ADD piece of shit", "ADHD piece of shit", "piece of flying shit", "frozen condom", "autist", "re-fucking-tard" };
        final Random rand = new Random();
        final int i = rand.nextInt(pre.length - 1);
        final int i2 = rand.nextInt(post.length - 1);
        String insult = pre[i].concat(" ").concat(post[i2]);
        if (args != null) {
            insult = args[0] + " " + insult;
        }
        ChatUtil.sendChat_NoFilter(insult);
    }
    
    @Override
    public String getUsage() {
        return null;
    }
}
