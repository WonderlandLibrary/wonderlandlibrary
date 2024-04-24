package me.offeex.bloomware.client.module.tweaks

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.FriendManager
import me.offeex.bloomware.api.manager.managers.FriendManager.getType
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object BetterTab : Module("BetterTab", "Improves your tab.", Category.TWEAKS) {
	val modifySize = setting("ModifySize").group(true)
	val maxSize = setting("MaxPlayers").number(80.0, 80.0, 1000.0, 1.0)
	private val highlighting = setting("Highlighting").group()
	private val highlightFriends = highlighting.setting("Friends").bool(true)
	private val selfHighlight = highlighting.setting("Self").bool(true)
	private val highlightEnemies = highlighting.setting("Enemies").bool(true)
	private val hideMe = setting("NameProtect").bool()
	val ping = setting("Ping").bool(true)
	val customFont = setting("CustomFont").bool()

	fun getText(playerListEntry: PlayerListEntry): Text? {
		var color: String? = null
		var name = playerListEntry.displayName
		if (name == null) name = Text.literal(playerListEntry.profile.name)
		if (playerListEntry.profile.id.toString() == cPlayer.gameProfile.id.toString()) {
			if (hideMe.toggled) name = Text.of("Me")
			if (selfHighlight.toggled) color = "AQUA"
		} else {
			if (getType(playerListEntry.profile.name) != null) {
				if (getType(playerListEntry.profile.name) === FriendManager.PersonType.FRIEND && highlightFriends.toggled) color =
					"GREEN"
				if (getType(playerListEntry.profile.name) === FriendManager.PersonType.ENEMY && highlightEnemies.toggled) color =
					"RED"
			}
		}
		if (color != null) {
			var nameString = name!!.string
			Formatting.values().filter { it.isColor }.forEach {
				nameString = nameString.replace(it.toString(), "")
			}
			name = Text.literal(nameString).setStyle(name.style.withColor(Formatting.byName(color)))
		}
		return name
	}
}