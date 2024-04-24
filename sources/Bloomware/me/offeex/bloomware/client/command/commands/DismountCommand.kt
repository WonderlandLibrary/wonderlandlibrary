package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.util.ChatUtil.errorMessage
import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.command.Command
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket
import net.minecraft.util.Formatting.*

object DismountCommand : Command("dismount", {
	runs {
		if (cPlayer.vehicle != null) {
			val donki = cPlayer.vehicle!!
			cNetHandler.sendPacket(PlayerInputC2SPacket(0f, 0f, false, true))
			val name = donki.name.string
			val uuid = donki.entityName
			addMessage("Successfully dismounted from $GREEN$name$RESET: $AQUA$uuid")
		} else errorMessage("You are not riding anything!")
	}
}) {
	override val description = "dismounts you from an entity you are riding"
}