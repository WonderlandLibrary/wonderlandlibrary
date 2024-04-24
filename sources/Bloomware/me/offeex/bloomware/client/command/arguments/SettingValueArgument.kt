package me.offeex.bloomware.client.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.nicolai.brigadier.dsl.DslCommandBuilder
import me.offeex.bloomware.api.manager.managers.CommandManager.commandException
import me.offeex.bloomware.api.helper.TypedArgument
import me.offeex.bloomware.client.command.Command.Companion.argumentTyped
import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.settings.SettingBool
import me.offeex.bloomware.client.setting.settings.SettingEnum
import me.offeex.bloomware.client.setting.settings.SettingNumber
import net.minecraft.command.CommandSource
import net.minecraft.util.Formatting.*
import java.util.concurrent.CompletableFuture

object SettingValueArgument : TypedArgument<String> {
	lateinit var setting: Setting

	override fun parse(reader: StringReader): String {
		val str = reader.readString()
		return when (setting) {
			is SettingBool -> if (str.equals("true", true) || str.equals("false", true)) str else throw commandException("Value must be true or false")
			is SettingEnum -> (setting as SettingEnum).modes.find { it.equals(str, true) } ?: throw commandException("Value must be one of the modes")
			is SettingNumber -> if (str.toDoubleOrNull() != null) str else throw commandException("Value must be a number")
			else -> throw commandException("${DARK_RED}Wrong setting value: $GREEN$BOLD$str")
		}
	}

	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val s = context.getArgument("setting", Setting::class.java)
		setting = s

		val resolved = when(s) {
			is SettingBool -> CommandSource.suggestMatching(listOf("true", "false"), builder)
			is SettingEnum -> CommandSource.suggestMatching(s.modes, builder)
			is SettingNumber -> CommandSource.suggestMatching(listOf(s.min.toString(), s.max.toString()), builder)
			else -> Suggestions.empty()
		}
		return resolved
	}

	override fun <S> get(b: DslCommandBuilder<S>) = b.argumentTyped("value", this)
}