package me.offeex.bloomware.client.module.tweaks

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.CommandManager.prefix
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.module.client.Notifications
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.mixin.accessor.IChatMessageC2SPacket
import me.offeex.bloomware.mixin.accessor.IGameMessageS2CPacket
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import org.apache.commons.lang3.math.NumberUtils
import java.text.SimpleDateFormat
import java.util.*

object BetterChat : Module("BetterChat", "Improves your chat.", Category.TWEAKS) {
	private val addition = setting("Addition").group()
	private val greenText = addition.setting("GreenText").bool()
	private val autoGlobal = addition.setting("AutoGlobal").bool()
	private val suffix = addition.setting("Suffix").bool(true)
	private val timestamp = addition.setting("Timestamps").bool(true)
	private val antiCoordLeak = setting("AntiCoordLeak").bool(true)
	val infiniteTextField = setting("InfiniteTextField").bool()
	private val mentions = setting("Mentions").group(false)
	private val sound = mentions.setting("Sound").bool(true)
	private val notification = mentions.setting("Notification").bool()
	val infiniteChat = setting("InfiniteChat").bool(true)

	private val antiSpam = setting("AntiSpam").group(false)
	private val links = antiSpam.setting("Links").bool(true)
	private val nWords = antiSpam.setting("NWords").bool(true)

	private val forbiddenSpam =
		arrayOf("https:", "http:", ".com", ".ru", ".cc", ".gg", ".top", ".wtf", ".xyz", ".org", ".net, discord")
	private val forbiddenSymbols = listOf(".", ";", ">", prefix, "/")
	private var msg = ""

	@Subscribe
	private fun onPacketSend(event: EventPacket.Send) {
		if (event.packet is ChatMessageC2SPacket) {
			val packet = event.packet
			if (packet.chatMessage.startsWith("/")) return
			var message = ""
			if (!forbiddenSymbols.contains(packet.chatMessage.substring(0, 0)) && !packet.chatMessage.startsWith(
					"/"
				)
			) {
				if (autoGlobal.toggled) message += "!"
				if (greenText.toggled) message += "> "
				message += packet.chatMessage
			}
			if (suffix.toggled) message += " < ${Bloomware.NAME} >"
			if (antiCoordLeak.toggled && isCoords(message)) {
				event.canceled = true
				addMessage("This message may contain coordinates. Sending was canceled.")
			}
			(packet as IChatMessageC2SPacket).setChatMessage(message)
			msg = message
		}
	}

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		val packet = event.packet
		if (packet is GameMessageS2CPacket) {
			var message: Text = packet.content().copy()
			if (timestamp.toggled) {
				message =
					Text.literal("\u00A77[" + SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time) + "] ")
						.append(message)
				(packet as IGameMessageS2CPacket).setContent(message)
			}
			if (mentions.toggled && message.string.lowercase()
					.contains(mc.session.username.lowercase()) && !message.string.contains(msg)
			) {
				if (sound.toggled) cWorld.playSound(
					null,
					cPlayer.blockPos,
					SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
					SoundCategory.AMBIENT,
					1f,
					1f
				)
				if (notification.toggled) Notifications.showNotification("You just have been mentioned in chat!")
			}
			if (antiSpam.toggled) {
				if (links.toggled) {
					for (str in forbiddenSpam) {
						if (packet.content().string.contains(str)) {
							event.canceled = true
							break
						}
					}
				}
				if (nWords.toggled && packet.content().string.contains("nig")) event.canceled = true
			}
		}
	}

	private fun isCoords(message: String): Boolean {
		var coord: Short = 0
		message.split(" ").filter { NumberUtils.isParsable(it) }.forEach { _ -> coord++ }
		return coord >= 2
	}
}