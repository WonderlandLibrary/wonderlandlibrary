package me.offeex.bloomware.api.util

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.Bloomware.mc
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.system.Platform
import javax.sound.sampled.AudioSystem
import kotlin.random.Random

object ClientUtil {
	val system: Platform by lazy {
		val os = System.getProperty("os.name").lowercase()
		if (os.contains("windows")) Platform.WINDOWS
		else if (os.contains("mac") || os.contains("darwin")) Platform.MACOSX
		else Platform.LINUX
	}

	fun playSound() {
		if (Random.nextInt(100) <= 1) {
			val stream =
				AudioSystem.getAudioInputStream(Bloomware::class.java.getResourceAsStream("/assets/bloomware/sound/pidrila.wav"))
			val clip = AudioSystem.getClip()
			clip.open(stream)
			clip.start()
		}
	}

	fun KeyBinding.isActuallyPressed() = InputUtil.isKeyPressed(mc.window.handle, defaultKey.code)
}