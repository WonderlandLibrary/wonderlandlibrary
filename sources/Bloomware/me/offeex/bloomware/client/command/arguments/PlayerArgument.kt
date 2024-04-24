package me.offeex.bloomware.client.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.nicolai.brigadier.dsl.DslCommandBuilder
import me.offeex.bloomware.api.manager.managers.CommandManager.commandException
import me.offeex.bloomware.api.helper.TypedArgument
import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.client.command.Command.Companion.argumentTyped
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.command.CommandSource
import java.util.concurrent.CompletableFuture

object PlayerArgument : TypedArgument<PlayerListEntry> {
    override fun parse(reader: StringReader): PlayerListEntry {
        val str = reader.readString()
        return cNetHandler.playerList.find { it.profile.name == str }
            ?: throw commandException("Player $str not found")
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val names = cNetHandler.playerList.map { it.profile.name }
        return CommandSource.suggestMatching(names, builder)
    }

    override fun <S> get(b: DslCommandBuilder<S>) = b.argumentTyped("player", this)
}