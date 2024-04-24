package me.offeex.bloomware.client.module.tweaks

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.ClipboardImage
import me.offeex.bloomware.client.module.Module
import java.awt.Image
import java.awt.Toolkit
import java.io.File
import javax.swing.ImageIcon
import kotlin.concurrent.thread

object BetterScreenshot : Module("BetterScreenshot", "Adds some new features to screenshots", Category.TWEAKS) {
	@JvmStatic
	val latestScreenshot: Image
		get() {
			val path = File(mc.runDirectory.absolutePath + "/screenshots/")
			val lastFilePath =
				path.listFiles()!!.filter { !it.isDirectory }.maxBy { it.lastModified() }.absolutePath
			return ImageIcon(lastFilePath).image
		}

	@JvmStatic
	fun copyToClipboard(image: Image) {
		thread(true) {
			val image1 = ClipboardImage(image)
			val clipboard = Toolkit.getDefaultToolkit().systemClipboard
			clipboard.setContents(image1, null)
		}
	}
}