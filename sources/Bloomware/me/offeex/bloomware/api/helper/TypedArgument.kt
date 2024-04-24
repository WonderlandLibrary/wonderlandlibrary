package me.offeex.bloomware.api.helper

import com.mojang.brigadier.arguments.ArgumentType
import dev.nicolai.brigadier.RequiredArgument
import dev.nicolai.brigadier.dsl.DslCommandBuilder

interface TypedArgument<T> : ArgumentType<T> {
	fun <S> get(b: DslCommandBuilder<S>): RequiredArgument<S, T, T>
}