package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.api.manager.managers.FriendManager
import me.offeex.bloomware.api.manager.managers.FriendManager.PersonType.*
import me.offeex.bloomware.api.manager.managers.FriendManager.addPerson
import me.offeex.bloomware.api.manager.managers.FriendManager.removePerson
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.util.ChatUtil.errorMessage
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.client.command.arguments.PlayerArgument
import me.offeex.bloomware.client.command.commands.FriendCommand.personAction
import me.offeex.bloomware.client.command.commands.FriendCommand.PersonAction.*
import net.minecraft.util.Formatting
import net.minecraft.util.Formatting.*

object FriendCommand : Command("friend", {
	literal("add") {
		val entry by PlayerArgument.get(this)
		runs { personAction(entry.profile.name, ADD_FRIEND) }
	}
	literal("del") {
		val entry by PlayerArgument.get(this)
		runs { personAction(entry.profile.name, DEL_PERSON) }
	}
}) {
	override val description = "allows you to add/remove friends"

	fun personAction(name: String, action: PersonAction) {
		fun FriendManager.PersonType?.msg(type: String, color: Formatting) {
			if (this == FRIEND) addMessage("$BLUE$name$RESET is already your $color$type.")
			else addMessage("$BLUE$name$RESET now is your $color$type.")
		}

		val type = FriendManager.getType(name)
		if (name == cPlayer.displayName.string) {
			errorMessage("Lmao, you can't add yourself to the list.")
			return
		}
		when (action) {
			ADD_FRIEND -> {
				addPerson(name, FRIEND)
				type.msg("friend", GREEN)
			}
			ADD_ENEMY -> {
				addPerson(name, ENEMY)
				type.msg("enemy", RED)
			}
			DEL_PERSON -> {
				if (type == null) errorMessage("$AQUA$name$DARK_RED isn't in the list.")
				else {
					addMessage("$AQUA$name$RESET was removed from the list.")
					removePerson(name)
				}
			}
		}
	}

	enum class PersonAction { ADD_FRIEND, ADD_ENEMY, DEL_PERSON }
}