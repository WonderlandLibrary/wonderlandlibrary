package me.offeex.bloomware.client.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.nicolai.brigadier.dsl.DslCommandBuilder
import me.offeex.bloomware.api.manager.managers.CommandManager.findElement
import me.offeex.bloomware.api.helper.TypedArgument
import me.offeex.bloomware.client.command.Command.Companion.argumentTyped
import me.offeex.bloomware.mixin.accessor.IInputUtil
import net.minecraft.client.util.InputUtil
import net.minecraft.command.CommandSource
import java.util.concurrent.CompletableFuture

object KeyArgument : TypedArgument<InputUtil.Key> {
	private val keys = (InputUtil.Type.KEYSYM as IInputUtil).map.values.toList()
	// TODO: Move to manager/util

	override fun parse(reader: StringReader): InputUtil.Key {
		return findElement(keys, "Key", reader) { it.str() }
	}

	override fun <S> listSuggestions(
		context: CommandContext<S>, builder: SuggestionsBuilder?
	): CompletableFuture<Suggestions> {
		return CommandSource.suggestMatching(keys.map { it.str().uppercase() }, builder)
	}

	override fun <S> get(b: DslCommandBuilder<S>) = b.argumentTyped("key", this)

	internal fun InputUtil.Key.str() = // TODO: To util pls
		translationKey.replace("key.keyboard.", "").replace('.', '_')
}