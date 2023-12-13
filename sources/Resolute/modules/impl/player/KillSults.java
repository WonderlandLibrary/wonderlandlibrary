// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.network.Packet;
import org.apache.commons.lang3.RandomStringUtils;
import net.minecraft.network.play.server.S02PacketChat;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import org.apache.commons.lang3.RandomUtils;
import vip.Resolute.modules.Module;

public class KillSults extends Module
{
    public KillSults() {
        super("KillSults", 0, "Insults whoever you kill", Category.PLAYER);
    }
    
    public static String killSult() {
        final String[] killSults = { "Did ur parents ask you to run away", "You make me want to use summer v5", "Download sigma in https://sigmaclient.info/", "F 4 g", "Wait everyone isn't flying too?", "Get dortware at https://pornhub.com/gay", "Do alt + f4 for free sugma premium", "Why aren't you using vip.Resolute Client lmao", "Im not racist, but I only like vip.Resolute Client users", "Imagine using FDP Client", "Imagine not using vip.Resolute Client", "Sugma client sucks omg sugma hatar", "wow you being this ass makes me wanna downgrade vip.Resolute to your level", "Buy vip.Resolute Client", "Hack Lixo", "YO JOIN MY STEAM", "me when, when the", "Imagine not using sigma", "Wait what? You died? Switch to Sigma 5", "Damn vip.Resolute client is so clean", "LMAO RESOLUTE IS SO GOOD", "git gud", "nig moment", "SIGMA HATAR REMOVED FROM GAME", "nig cheese", "NoHaxJustResolute", "replace it with vip.Resolute, ill do it", "Your client sucks, just get vip.Resolute", "vip.Resolute made this world a better place, killing you with it even more", "Stop being a disapointment to your parents and download vip.Resolute", "After I started using  vip.Resolute my dad finally came home from the gas station", "it's a bird! It's a plane! It's The redesky assripper vip.Resolute!!!", "Another vip.Resolute user? Awww man", "Fly faster than light, only available in vip.Resolute Client On Yt" };
        return killSults[RandomUtils.nextInt(0, killSults.length - 1)];
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket) {
            final EventPacket eventPacket = (EventPacket)e;
            final Packet<?> packet = eventPacket.getPacket();
            if (KillSults.mc.thePlayer != null && KillSults.mc.thePlayer.ticksExisted >= 0 && packet instanceof S02PacketChat) {
                final String look = "killed by " + KillSults.mc.thePlayer.getName();
                final String look2 = "slain by " + KillSults.mc.thePlayer.getName();
                final String look3 = "void while escaping " + KillSults.mc.thePlayer.getName();
                final String look4 = "was killed with magic while fighting " + KillSults.mc.thePlayer.getName();
                final String look5 = "couldn't fly while escaping " + KillSults.mc.thePlayer.getName();
                final String look6 = "fell to their death while escaping " + KillSults.mc.thePlayer.getName();
                final String look7 = "foi morto por " + KillSults.mc.thePlayer.getName();
                final String look8 = "fue asesinado por " + KillSults.mc.thePlayer.getName();
                final S02PacketChat s02PacketChat = (S02PacketChat)packet;
                final String cp21 = s02PacketChat.getChatComponent().getUnformattedText();
                if ((cp21.startsWith(KillSults.mc.thePlayer.getName() + "(") && cp21.contains("asesino ha")) || cp21.contains(look) || cp21.contains(look2) || cp21.contains(look3) || cp21.contains("You have been rewarded $50 and 2 point(s)!") || cp21.contains(look4) || cp21.contains(look5) || cp21.contains(look6) || cp21.contains(look7) || cp21.contains(look8)) {
                    KillSults.mc.thePlayer.sendChatMessage("" + killSult() + " [" + RandomStringUtils.random(2, "abcdef") + "]");
                }
            }
        }
    }
}
