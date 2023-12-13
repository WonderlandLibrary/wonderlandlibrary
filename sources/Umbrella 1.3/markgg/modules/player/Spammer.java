/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import java.util.ArrayList;
import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.utilities.Random;
import markgg.utilities.TimerUtil;

public class Spammer
extends Module {
    public static int BlockAnimationInt = 0;
    public ModeSetting mode = new ModeSetting("Mode", "Communism", "Communism", "Da Baby", "Monsoon");
    public NumberSetting delay = new NumberSetting("Delay", 10.0, 1.0, 300.0, 1.0);
    TimerUtil timer = new TimerUtil();
    boolean PlayerEat = false;

    public Spammer() {
        super("Spammer", 0, Module.Category.PLAYER);
        this.addSettings(this.mode, this.delay);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            if (this.mode.is("Monsoon") && this.timer.hasTimeElapsed((long)this.delay.getValue() * 1000L, true)) {
                this.mc.thePlayer.sendChatMessage(Spammer.getChatMessageMonsoon());
            }
            if (this.mode.is("Communism") && this.timer.hasTimeElapsed((long)this.delay.getValue() * 1000L, true)) {
                this.mc.thePlayer.sendChatMessage(Spammer.getChatMessageCommunism());
            }
            if (this.mode.is("Da Baby") && this.timer.hasTimeElapsed((long)this.delay.getValue() * 1000L, true)) {
                this.mc.thePlayer.sendChatMessage("Let's gooooo");
            }
        }
    }

    public static String getChatMessageMonsoon() {
        ArrayList<String> message = new ArrayList<String>();
        message.add("I have the greatest gaming chair - buy now at monsoonclient.xyz");
        message.add("monsoonclient.xyz helped me get a girlfriend");
        message.add("monsoonclient.xyz  monsoonclient.xyz  monsoonclient.xyz  monsoonclient.xyz ");
        message.add("The world's best PVP Client for only $5 - monsoonclient.xyz");
        message.add("Hawk? More like monsoonclient.xyz");
        message.add("novoline.wtf? more like monsoonclient.xyz");
        message.add("Tybie says 10/10 - monsoonclient.xyz");
        message.add("ADP says 9.53290365/10 - monsoonclient.xyz");
        message.add("LiquidBounce? More like LiquidBAD! monsoonclient.xyz");
        message.add("FDP CLIENT LMAO WTF - monsoonclient.xyz");
        message.add("Astolfo? more like Astolshit! - monsoonclient.xyz");
        message.add("Asuna, the client made by a kid who wanted to rat 5000 people. Get monsoonclient.xyz instead.");
        message.add("less goooo monsoonclient.xyz");
        message.add("Skeeto rates 999/10 - monsoonclient.xyz");
        message.add("Alternative Accounts says 1/10, but FUCK his opinion! - monsoonclient.xyz");
        message.add("I am a registered Monsoon user, I stuck Monsoon into my version folder monsoonclient.xyz");
        message.add("MONSOON 2024 monsoonclient.xyz");
        message.add("Cyri rates it 100/10! monsoonclient.xyz");
        message.add("StanMC06 uses monsoonclient.xyz");
        message.add("Technoblade? more like technoBAD monsoonclient.xyz");
        message.add("monsoonclient.xyz");
        message.add("lolololololooloolollolololololololololololololollolololol monsoonclient.xyz");
        message.add("gamer client go brrr monsoonclient.xyz");
        message.add("lol what monsoonclient.xyz");
        message.add("FaZe_monsoonclient.xyz");
        Random random = new Random();
        int randomIndex = (int)(Math.random() * (double)message.size());
        return (String)message.get(randomIndex);
    }

    public static String getChatMessageCommunism() {
        ArrayList<String> message = new ArrayList<String>();
        message.add("Best client ever - Communism");
        message.add("help me bring back communism!");
        message.add("imagine not using communism client");
        message.add("Best verus bypasses since 2022, by Communism!");
        message.add("Verus? More like shittus");
        message.add("This client is sponsored by Da Baby");
        message.add("Made by MarkGG#8181");
        message.add("Imagine losing to communists");
        Random random = new Random();
        int randomIndex = (int)(Math.random() * (double)message.size());
        return (String)message.get(randomIndex);
    }
}

