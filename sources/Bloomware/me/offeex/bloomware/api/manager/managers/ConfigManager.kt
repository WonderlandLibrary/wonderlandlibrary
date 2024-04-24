package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.config.impl.GuiConfig
import me.offeex.bloomware.api.config.impl.ModuleConfig
import me.offeex.bloomware.api.config.impl.PrefixConfig
import me.offeex.bloomware.api.config.impl.TokenConfig
import me.offeex.bloomware.api.gui.screen.LoginScreen
import me.offeex.bloomware.api.helper.Saveable
import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender
import me.offeex.bloomware.event.events.EventScreen
import net.minecraft.client.gui.screen.TitleScreen

object ConfigManager : Manager(), Runnable, Saveable {
    private val configs = arrayOf(TokenConfig, GuiConfig, ModuleConfig, PrefixConfig)

    override fun run() {
        configs.forEach { it.loadExternal() }
    }

    override fun save() {
        configs.forEach { it.saveExternal() }
    }
}