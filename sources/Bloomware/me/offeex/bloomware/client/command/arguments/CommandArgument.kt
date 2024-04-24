package me.offeex.bloomware.client.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.nicolai.brigadier.dsl.DslCommandBuilder
import me.offeex.bloomware.api.manager.managers.CommandManager.commands
import me.offeex.bloomware.api.manager.managers.CommandManager.findElement
import me.offeex.bloomware.api.helper.TypedArgument
import me.offeex.bloomware.client.command.Command
import me.offeex.bloomware.client.command.Command.Companion.argumentTyped
import net.minecraft.command.CommandSource
import java.util.concurrent.CompletableFuture

object CommandArgument : TypedArgument<Command> {
    override fun parse(reader: StringReader) = findElement(commands, "Command", reader) { it.name }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder?
    ): CompletableFuture<Suggestions> {
        return CommandSource.suggestMatching(commands.map { it.name }, builder)
    }

    override fun <S> get(b: DslCommandBuilder<S>) = b.argumentTyped("command", this)
}