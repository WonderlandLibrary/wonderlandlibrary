package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;

public class Bleach extends Module {

	WaitTimer timer = new WaitTimer();
	WaitTimer soundTimer = new WaitTimer();
	WaitTimer liftTimer = new WaitTimer();

	public Bleach() {
		super("Bleach", Keyboard.KEY_NONE, Category.HIDDEN, "Kills you in an instant. Deep.");
	}

	@Override
	public void onEnable() {
		Jigsaw.sendChatMessage("Chug chug chug...");
		if (mc.player.capabilities.isCreativeMode) {
			Jigsaw.chatMessage("The bleach has no effect!");
			setToggled(false, true);
			return;
		}
		mc.player.rotationPitch = 0;
		// Jigsaw.chatMessage("Whatever life throws at me, ill"
		// + " take with smile upon my face and Nirvana's \"bleach\" on
		// stereo.");
		mc.player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 1337));
		timer.reset();
		soundTimer.reset();
		liftTimer.reset();
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (liftTimer.hasTimeElapsed(2000, false)) {
			mc.player.rotationPitch += 3;
		} else {
			if (!liftTimer.hasTimeElapsed(1000, false)) {
				mc.player.rotationPitch -= 2;
			}
			if (soundTimer.hasTimeElapsed(150, true)) {
				mc.player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1, 0.8f);
			}
		}
		if (!timer.hasTimeElapsed(4000, true)) {
			return;
		}
		Jigsaw.sendChatMessage(".damage 10");
		setToggled(false, true);
		super.onUpdate();
	}

}
