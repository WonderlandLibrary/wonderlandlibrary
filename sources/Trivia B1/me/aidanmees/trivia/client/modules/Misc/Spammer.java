package me.aidanmees.trivia.client.modules.Misc;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.MarisaTimer;
import me.aidanmees.trivia.module.Module;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Spammer extends Module {

	 private MarisaTimer time = new MarisaTimer();
	  private String[] phraseList = { "I use the best soundtrack ever!" , "Yoyoyo! Whats remix client?" , "The ting goes SKRAAAA!", "Mees is my best froond!", "GUCCI GANG GUCCI GANG GUCCI GANG GUCCI GANG GUCCI GANG. SPEND MY MONEY ON NEW CLIENT(Remix).", "What is a hacked client?", "NO! Im not gonna turn it off >:D", "I'll be right back", "/root/StrikeCode is ma boi!", "u suck at this game just quit!", "Whats spam?","Stop with ur bad memes! they aren't funny!"};
	  private int lastUsed;
	  
	public Spammer() {
		super("Spammer", Keyboard.KEY_NONE, Category.MISC, "Spamms some dank memes!");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		 if (this.time.delay(randomDelay()))
		    {
		      mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(randomPhrase()));
		      this.time.reset();
		    }
		  }
		  
	 private String randomPhrase() {
	        Random rand;
	        int randInt;
	        for (rand = new Random(), randInt = rand.nextInt(this.phraseList.length); this.lastUsed == randInt; randInt = rand.nextInt(this.phraseList.length)) {}
	        this.lastUsed = randInt;
	        return this.phraseList[randInt];
	    }
	    
		  
		  private int randomDelay()
		  {
		    Random randy = new Random();
		    int randyInt = randy.nextInt(2000) + 2000;
		    return randyInt;
		  }
		}
