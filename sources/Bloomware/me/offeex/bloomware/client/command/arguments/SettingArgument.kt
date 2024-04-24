package me.offeex.bloomware.client.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.nicolai.brigadier.dsl.DslCommandBuilder
import me.offeex.bloomware.api.manager.managers.CommandManager.findElement
import me.offeex.bloomware.api.helper.TypedArgument
import me.offeex.bloomware.client.command.Command.Companion.argumentTyped
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.settings.SettingGroup
import net.minecraft.command.CommandSource
import java.util.concurrent.CompletableFuture


object SettingArgument : TypedArgument<Setting> {
	private lateinit var module: Module

	override fun parse(reader: StringReader) = findElement(module.flattenSettings(), "Setting", reader) { replace(it.id) }

	override fun <S> listSuggestions(
		context: CommandContext<S>, builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		module = context.getArgument("module", Module::class.java)
		val settings = mutableListOf<String>()

		module.settings.forEach {
			if (it is SettingGroup) settings.addAll(it.flattenSettings().map {
				s -> replace(s.id)
			}) else settings.add(replace(it.id))
		}
		return CommandSource.suggestMatching(settings, builder)
	}

	override fun <S> get(b: DslCommandBuilder<S>) = b.argumentTyped("setting", this)

	private fun replace(s: String) = s.replaceBefore(".", "").drop(1)
}