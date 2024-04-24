package me.offeex.bloomware.client.command

import com.mojang.brigadier.arguments.ArgumentType
import dev.nicolai.brigadier.arguments.argumentImplied
import dev.nicolai.brigadier.arguments.impliedGetter
import dev.nicolai.brigadier.dsl.DslCommand
import dev.nicolai.brigadier.dsl.DslCommandBuilder
import me.offeex.bloomware.api.manager.managers.Source

abstract class Command(
	val name: String, val execute: DslCommandBuilder<Source>.() -> Unit
) : DslCommand<Source>(name, execute) {
	abstract val description: String

	companion object {
		internal inline fun <S, reified T> DslCommandBuilder<S>.argumentTyped(
			name: String, type: ArgumentType<T>
		) = argumentImplied(name, type, impliedGetter<S, T>())
	}
}