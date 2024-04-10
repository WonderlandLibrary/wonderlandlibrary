package info.sigmaclient.sigma.modules.misc;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;

import java.util.Random;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Jargon extends Module {
    public Jargon() {
        super("Jargon", Category.Misc, "Whats this, sigma has it...");
    }
    String[] jargonList = new String[]{
            "Of course Johnny's gonna fall for Baby because he's this big, dreamy country bumpkin and she is the shiny, new thing.",
            "You farmer.",
            "I would rather be a penny pincher then tossing money away to take the \"easy route\".",
            "I was searching for a fool when I found you",
            "Thou dost infect mine eyes.",
            "All the world’s a stage, Please make your exit",
            "knowing that you are not happy, I feel at ease.",
            " to know what things, are good.",
            "do you like this one in the series, most can only live 2 sets!",
            "I am not the grass, you mean don't always go to my here hair!",
            "I drew a coffin, lie inside you and she. I kind-hearted, let you die together.",
            "I want to ask you to experience the KTV! Know what is KTV? Is K you up, then T your feet, finally I'll do a V gestures!",
            "Twinkle twinkle little whore, close your legs, they're not a door.",
            "Do you want people to accept you as you are or do you want them to like you?",
            "When God was throwing intelligence down to the Earth, you were holding an umbrella.",
            "Your birth certificate is an apology letter from the condom factory.",
            "Maybe if you ate some of that makeup you could be pretty on the inside."
    };
    @Override
    public void onEnable() {
        String[] messages = new String[] {
                "ＳＩＧＭＡ users belike: Hit or miss I guess I never miss!",
                "ＳＩＧＭＡclient . Info is your new home",
                "ＳＩＧＭＡ utility client no hax 100%",
                "Want some skills? Check out ＳＩＧＭＡclient . Info!",
                "No hax just beta testing the anti-cheat with ＳＩＧＭＡ.",
                "Search ＳＩＧＭＡclient . Info to get the best mineman skills!",
                "Mama once told me, use ＳＩＧＭＡ it's free",
                "ＳＩＧＭＡ made this world a better place, killing you with it even more",
                "Why ＳＩＧＭＡ? Cause it is the additon of pure skill and incredible intellectual abilities",
                "Wow! My combo is ＳＩＧＭＡ'n!",
                "ＳＩＧＭＡ gang, ＳＩＧＭＡ gang, ＳＩＧＭＡ gang, you spent ten racks on this server, i killed you get better.",
                "ＳＩＧＭＡ. The only client run by speakers of Breton",
                "Behind every ＳＩＧＭＡ user, is an incredibly cool human being. Trust me, cooler than you.",
                "To cure you Ligma get ＳＩＧＭＡ",
                "Quick Quiz: I am zeus's son, who am I? ＳＩＧＭＡ",
                "Don't piss me off or you will discover the true power of ＳＩＧＭＡ's inf reach",
                "how do I cure your dumbassery?",
                "I have a good ＳＩＧＭＡ config, don't blame me",
                "What should I choose? ＳＩＧＭＡ or ＳＩＧＭＡ?",
                "Do like Tenebrous, subscribe to LeakedPvP!",
                "don't use ｓｉｇｍａ? ok boomer",
                "What? You've never downloaded Jello for ＳＩＧＭＡ? You know it's the best right?",
                "I don't hack I just ＳＩＧＭＡ",
                "You have been offed by ＳＩＧＭＡ oof oof",
                "I dont hack i just have ＳＩＧＭＡ Gaming Chair",
                "ＳＩＧＭＡ will help you! Oops, i killed you instead.",
                "Learn your alphabet with the ＳＩＧＭＡ client: Omikron, ＳＩＧＭＡ, Epsilon, Alpha!",
                "ＳＩＧＭＡ never dies",
                "My whole life changed since I discovered ＳＩＧＭＡ",
                "I am a sig-magician, thats how I am able to do all thos block game tricks",
                "Stop Hackusation me cuz im just ＳＩＧＭＡ",
                "ＳＩＧＭＡ helps reducing arm fatigue. Available for free at your local pharmacy.",
                "Stop it, get some help! Get ＳＩＧＭＡ",
                "Look a divinity! He definitely uses ＳＩＧＭＡ!",
                "In need of a cute present for Christmas? ＳＩＧＭＡ is all you need!"
        };
        this.enabled = false;
        mc.player.sendChatMessage(messages[new Random().nextInt(messages.length)]);
        super.onEnable();
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }
}
