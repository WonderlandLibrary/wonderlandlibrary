package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.event.dispatcher.Subscribe
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import dev.nicolai.brigadier.dsl.command
import dev.nicolai.brigadier.register
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.api.util.ChatUtil
import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.client.command.commands.*
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.mixin.accessor.IClientCommandSource
import net.minecraft.client.network.ClientCommandSource
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket
import net.minecraft.text.Text
import net.minecraft.util.Formatting.*

typealias Source = ClientCommandSource

object CommandManager : Manager(), Runnable {
	var prefix = "&"
	val source = Source(null, mc)
	val dispatcher = CommandDispatcher<Source>()
	var commands: MutableList<Command> = mutableListOf(
		BindCommand, ClearChatCommand, CopyNBTCommand, DismountCommand, EnemyCommand, FriendCommand, HelpCommand, LoginCommand, ModuleListCommand, PluginsCommand, PrefixCommand, RegisterCommand, ServerCommand, SessionCommand, SetCommand, SpammerCommand
	)

	@Subscribe
	private fun onPacketSend(event: EventPacket.Send) {
		val p = event.packet
		if (p !is ChatMessageC2SPacket) return
		if (p.chatMessage.startsWith(prefix)) event.canceled = true
		else return

		val msg = p.chatMessage
		try {
			val parsedMsg = dispatcher.parse(msg.drop(prefix.length), source)
			dispatcher.execute(parsedMsg)
		} catch (e: CommandSyntaxException) {
			if (e.type === NO_SUCH_ELEMENT) ChatUtil.addMessage(e.localizedMessage)
			else ChatUtil.errorMessage("Wrong command syntax! Type $GREEN${BOLD}${prefix}help$RESET$DARK_RED for help.")
		}
	}

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		val p = event.packet
		if (event.shift && p is GameJoinS2CPacket)
			(source as IClientCommandSource).setNetworkHandler(mc.networkHandler)
	}


	private val NO_SUCH_ELEMENT = Dynamic2CommandExceptionType { type, value ->
		val t = type.toString().replaceFirstChar { it.uppercase() }
		Text.literal("$t $RED$BOLD$value$RESET doesn't exist!")
	}

	fun commandException(msg: String): CommandSyntaxException = SimpleCommandExceptionType(Text.of(msg)).create()

	fun <T> findElement(list: List<T>, type: String, reader: StringReader, element: (T) -> String): T {
		val str = reader.readString()
		return list.find { element(it).equals(str, true) } ?: throw NO_SUCH_ELEMENT.create(type, str)
	}

	override fun run() {
		commands.forEach {
			dispatcher.register(command(it.name) { it.execute(this) })
		}
	}
}