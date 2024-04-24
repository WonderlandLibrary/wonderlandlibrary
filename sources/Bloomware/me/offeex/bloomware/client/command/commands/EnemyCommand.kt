package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.client.command.arguments.PlayerArgument
import me.offeex.bloomware.client.command.commands.FriendCommand.PersonAction.ADD_ENEMY
import me.offeex.bloomware.client.command.commands.FriendCommand.PersonAction.DEL_PERSON
import me.offeex.bloomware.client.command.commands.FriendCommand.personAction

object EnemyCommand : Command("enemy", {
	literal("add") {
		val entry by PlayerArgument.get(this)
		runs { personAction(entry.profile.name, ADD_ENEMY) }
	}
	literal("del") {
		val entry by PlayerArgument.get(this)
		runs { personAction(entry.profile.name, DEL_PERSON) }
	}
}) {
	override val description = "allows you to add/remove enemies"
}