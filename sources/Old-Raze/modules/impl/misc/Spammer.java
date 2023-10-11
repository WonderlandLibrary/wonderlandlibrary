package markgg.modules.impl.misc;

import java.util.ArrayList;
import java.util.Random;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.util.others.RandomUtil;
import markgg.util.timer.Timer;

@ModuleInfo(name = "Spammer", category = Module.Category.MISC)
public class Spammer extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Default", "Default", "Bypass", "Communism");
	public NumberSetting delay = new NumberSetting("Delay", this, 10, 1, 300, 1);
	private Timer timer = new Timer();

	@EventHandler
	private final Listener<MotionEvent> veryShitEventPlsIHave100Errors = e -> {
		switch(mode.getMode()) {
			case "Default":
				if(timer.hasTimeElapsed((long) (delay.getValue() * 1000), true))
					mc.thePlayer.sendChatMessage(getDefaultMessages());
				break;
			case "Bypass":
				if(timer.hasTimeElapsed((long) (delay.getValue() * 1000), true))
					mc.thePlayer.sendChatMessage(getCommunismMessages());
				break;
			case "Communism":
				if(timer.hasTimeElapsed((long) (delay.getValue() * 1000), true))
					mc.thePlayer.sendChatMessage(getBypassMessage());
				break;
		}
	};

	public static String getDefaultMessages() {
		ArrayList<String> message = new ArrayList<>();

		message.add("I have the greatest gaming chair - discord.gg/qynVdyQaXe");
		message.add("raze helped me get a girlfriend");
		message.add("raze  raze  raze  raze ");
		message.add("The world's best PVP Client compared to Lunar!");
		message.add("Hawk? More like Raze");
		message.add("LiquidBounce? More like LiquidBAD! RAZE ON TOP");
		message.add("FDP CLIENT LMAO WTF - RAZE ON TOP");
		message.add("stop using nightx loser - RAZE ON TOP");
		message.add("without kants rat");
		message.add("Kellohylly rated 999/10 <3");
		message.add("Others say 1/10, but FUCK them! - discord.gg/qynVdyQaXe");
		message.add("I stuck Raze into my version folder discord.gg/qynVdyQaXe");
		message.add("0zg rates it 100/10! discord.gg/qynVdyQaXe");
		message.add("Kellohylly uses raze!");
		message.add("discord.gg/qynVdyQaXe");
		message.add("lolololololooloolollolololololololololololololollolololol");
		message.add("gamer client go brrr");
		message.add("completely clean code - discord.gg/qynVdyQaXe");
		message.add("atleast mine is not skidded!");
		message.add("full hypixel killaura bipass");
		message.add("Xoraii loves raze");

		Random random = new Random();
		int randomIndex = (int)(Math.random() * message.size());
		return message.get(randomIndex);
	}

	public static String getCommunismMessages() {
		ArrayList<String> message = new ArrayList<>();

		message.add("communism isnt that bad, bring it back!");
		message.add("help me bring back communism!");
		message.add("imagine not liking communism");
		message.add("Best verus bypasses since 2023, powered by Communism!");
		message.add("Verus? More like shittus");
		message.add("This client is sponsored by Da Baby");
		message.add("no skidded lines on this");
		message.add("Imagine losing to communists");
		message.add("Cuba is the best!!");
		message.add("discord.gg/qynVdyQaXe WAZ LOST?");
		message.add("Support our local furry zoos - discord.gg/qynVdyQaXe");
		message.add("Communism is getting rebranded!");

		Random random = new Random();
		int randomIndex = (int)(Math.random() * message.size());
		return message.get(randomIndex);
	}

	public static String getBypassMessage() {
		ArrayList<String> message = new ArrayList<>();

		message.add(RandomUtil.generateRandomLine() + "fdp sucks ass");
		message.add(RandomUtil.generateRandomLine() + "raze is actually free!");
		message.add(RandomUtil.generateRandomLine() + "imagine using fdp :clown:");
		message.add(RandomUtil.generateRandomLine() + "Best vulcan client Razeeeee");
		message.add(RandomUtil.generateRandomLine() + "wurst is better than fdp");
		message.add(RandomUtil.generateRandomLine() + "Anticheat got destroyed like your a*s");
		message.add(RandomUtil.generateRandomLine() + "Atleast mine is bigger!");
		message.add(RandomUtil.generateRandomLine() + "Imagine paying taxes");
		message.add(RandomUtil.generateRandomLine() + "Stop using Lunar and get Raze!");
		message.add(RandomUtil.generateRandomLine() + "discord.gg/qynVdyQaXe");
		message.add(RandomUtil.generateRandomLine() + "get raze - discord.gg/qynVdyQaXe");
		message.add(RandomUtil.generateRandomLine() + "raze on top m*f*s!!");

		Random random = new Random();
		int randomIndex = (int)(Math.random() * message.size());
		return message.get(randomIndex);
	}

}
