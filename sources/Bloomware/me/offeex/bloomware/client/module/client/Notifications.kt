package me.offeex.bloomware.client.module.client

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.client.module.Module
import java.awt.Image
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon

object Notifications :
	Module("Notifications", "Shows system notifications about your game", Category.CLIENT) {
	private lateinit var tray: SystemTray
	private lateinit var image: Image
	private lateinit var icon: TrayIcon

	override fun onEnable() {
		if (!SystemTray.isSupported()) {
			addMessage("System notifications are not supported on your computer, disabling.")
			disable()
		} else {
			image = Toolkit.getDefaultToolkit()
				.createImage(Bloomware.javaClass.getResource("/assets/bloomware/elements/tray/icon32x32.png"))
			tray = SystemTray.getSystemTray()
			icon = TrayIcon(image, "Bloomware Client")
			icon.isImageAutoSize = true

			tray.add(icon)
		}
	}

	override fun onDisable() {
		tray.remove(icon)
	}

	fun showNotification(text: String) {
		if (enabled) icon.displayMessage("Bloomware", text, TrayIcon.MessageType.INFO)
	}
}