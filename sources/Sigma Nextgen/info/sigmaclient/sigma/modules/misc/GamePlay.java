
package info.sigmaclient.sigma.modules.misc;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.combat.Killaura;
import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.music.SoundPlayer;
import net.minecraft.entity.LivingEntity;

import java.util.Random;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class GamePlay extends Module {
    public static BooleanValue gg = new BooleanValue("GG", false);
    public static BooleanValue autoplay = new BooleanValue("Autoplay", false);

    public static ModeValue mode= new ModeValue("Mode","SoloInsane",new String[]{
            "SoloInsane", "SoloNormal", "TeamNormal", "TeamInsane"});

    public static BooleanValue L = new BooleanValue("AutoL", false);
    public static ModeValue Lmode= new ModeValue("AutoLMode","SigmaMemes",new String[]{
            "SigmaMemes"});
    public static BooleanValue unicode = new BooleanValue("NoUnicode", true);

    public static BooleanValue sound = new BooleanValue("PlaySound", true);
    public static ModeValue soundMode= new ModeValue("PlaySoundMode","Hurt",new String[]{
            "Hurt","Nya"});
    public GamePlay() {
        super("GamePlay", Category.Misc, "Auto play game");
     registerValue(gg);
     registerValue(autoplay);
     registerValue(L);
     registerValue(unicode);
     registerValue(sound);
        if(SigmaNG.getSigmaNG().gameMode2 == SigmaNG.GAME_MODE.DEV){
            Lmode = new ModeValue("AutoLMode","SigmaMemes",new String[]{
                    "SigmaMemes", "Dev"});
        }
    }

    String abc = "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ";
    public static void onChat(String c) {
        if (c.contains("Winner - ")) {
            if (gg.getValue())
                mc.player.sendChatMessage("gg");
            if (autoplay.getValue()) {
                new Thread(() -> {
                    try {
                        NotificationManager.notify(
                                "Auto Join",
                                "Joining a new game in 5 seconds.", 5000
                        );
                        for(int i = 4;i>=0;i--){
                            Thread.sleep(1000);
                            NotificationManager.notify("Auto Join", "Joining a new game in " + i +" seconds.", 5000);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if(mode.is("SoloNormal"))
                        mc.player.sendChatMessage("/play solo_normal");
                    if(mode.is("SoloInsane"))
                        mc.player.sendChatMessage("/play solo_insane");
                    if(mode.is("TeamInsane"))
                        mc.player.sendChatMessage("/play teams_insane");
                    if(mode.is("TeamNormal"))
                        mc.player.sendChatMessage("/play teams_normal");
                }).start();
            }
        }
        if(sound.getValue()){
            if(c.contains("Winner") && c.contains(mc.player.getName().getString())){
                String sound = "";
                switch (soundMode.getValue()){
                    case "Nya":
                        sound = "nya.wav";
                        break;
                    case "Hurt":
                        sound = "hurt.wav";
                        break;
                }
                SoundPlayer.MusicPlay(sound);
            }
        }
        if (!L.getValue()) return;
        LivingEntity entity = Killaura.cacheAttackTarget;
        for (int i = 0; i < 1; i++) {
            if (c.contains("胜") && c.contains(mc.player.getName().getString())) {
                break;
            }
            if (entity == null) return;
            if (!c.contains(entity.getName().getString()) || !c.contains(mc.player.getName().getString())) return;
            if(c.contains(" was slain by ")) return;
            if(c.contains("<") && c.contains(">")) return;
            if(c.contains("Inventories")) return;
            if(c.contains("by")) {
                if (c.indexOf(mc.player.getName().getString()) < c.indexOf(entity.getName().getString()))
                    return;
            }else{
                if (c.indexOf(mc.player.getName().getString()) > c.indexOf(entity.getName().getString()))
                    return;
            }
        }
        String str1 = null;
        String[] messages_dev = new String[]{
                "是不是喜欢我啊",
                "我就和人玩过sm，遇到专业的特别舒服",
                "表达对你的爱意，爱你",
                "我要接着看片子了",
                "来和姐姐bw，物理的",
                "要轻轻的哦~别打坏了",
                "你抱我了，现在我要指认是你把我搞成两条杠的，由于你接触了我",
                "黄瓜带刺能吃，不带刺可以用",
                "喜欢我要早点表白啊",
                "爱 需要大胆说出来",
                "我今早看片子累的我腰疼",
                "你这么喜欢vape,能不能和manthe结婚啊",
                "杂鱼❤, 杂鱼❤",
        };
        switch (Lmode.getValue()){
            case "SigmaMemes":
                str1 = messages[new Random().nextInt(messages.length)];
                if(unicode.isEnable()){
                    str1 = str1.replaceAll("ＳＩＧＭＡ", "Sigma");
                    str1 = str1.replaceAll("ｓｉｇｍａ", "sigma");
                }
                break;
            case "Dev":
                str1 = "L " + entity.getName() + ", " + messages_dev[new Random().nextInt(messages_dev.length)];
                break;
        }
        mc.player.sendChatMessage(str1);
    }

    public static String[] messages_dev = new String[]{
            "是不是喜欢我啊",
            "我就和人玩过sm，遇到专业的特别舒服",
            "表达对你的爱意，爱你",
            "我要接着看片子了",
            "来和姐姐bw，物理的",
            "要轻轻的哦~别打坏了",
            "你抱我了，现在我要指认是你把我搞成两条杠的，由于你接触了我",
            "黄瓜带刺能吃，不带刺可以用",
            "喜欢我要早点表白啊",
            "爱 需要大胆说出来",
            "我今早看片子累的我腰疼",
            "你这么喜欢vape,能不能和manthe结婚啊",
            "杂鱼❤, 杂鱼❤"
    };
    public static String[] messages = new String[] {
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
}
