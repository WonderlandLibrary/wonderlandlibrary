package me.offeex.bloomware.client.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.nicolai.brigadier.dsl.DslCommandBuilder
import me.offeex.bloomware.api.manager.managers.CommandManager.findElement
import me.offeex.bloomware.api.manager.managers.ModuleManager.modules
import me.offeex.bloomware.api.helper.TypedArgument
import me.offeex.bloomware.client.command.Command.Companion.argumentTyped
import me.offeex.bloomware.client.module.Module
import net.minecraft.command.CommandSource
import java.util.concurrent.CompletableFuture

object ModuleArgument : TypedArgument<Module> {
	override fun parse(reader: StringReader) = findElement(modules, "Module", reader) { it.name }

	override fun <S> listSuggestions(
		context: CommandContext<S>, builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		return CommandSource.suggestMatching(modules.map { it.name }, builder)
	}

	override fun getExamples(): MutableCollection<String> {
		return modules.take(3).map { it.name }.toMutableList()
	}

	override fun <S> get(b: DslCommandBuilder<S>) = b.argumentTyped("module", this)
}
