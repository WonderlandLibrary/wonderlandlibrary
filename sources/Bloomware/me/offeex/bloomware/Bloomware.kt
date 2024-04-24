package me.offeex.bloomware

import me.offeex.bloomware.api.extension.plugin.PluginClassLoader
import me.offeex.bloomware.api.gui.screen.ClickGUI
import me.offeex.bloomware.api.gui.screen.HudEditor
import me.offeex.bloomware.api.helper.Saveable
import me.offeex.bloomware.api.manager.managers.*
import me.offeex.bloomware.api.util.ClientUtil
import me.offeex.bloomware.api.util.ClientUtil.system
import me.offeex.bloomware.client.module.network.Spammer
import me.offeex.bloomware.event.dispatcher.MicroEBus
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventEntrypoint
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.system.Platform

object Bloomware {
    const val NAME = "Bloomware"
    val VERSION: String = FabricLoader.getInstance()
        .getModContainer("bloomware").get().metadata.version.friendlyString
    val LOGGER: Logger by lazy { LogManager.getLogger(NAME) }
    val EVENTBUS = MicroEBus()
    val mc: MinecraftClient by lazy { MinecraftClient.getInstance() }
    val gui: ClickGUI by lazy { ClickGUI() }
    val hud: HudEditor by lazy { HudEditor() }

    var currentScreen: Screen? = null

    private lateinit var runnables: List<Runnable>
    private lateinit var saveables: List<Saveable>

    var expiresAt = 0L

    @Subscribe
    private fun onPreInit(event: EventEntrypoint.Main) {
        System.setProperty("java.awt.headless", if (system === Platform.MACOSX) "true" else "false")
    }

    fun init() {
        LOGGER.info("""
        ██████╗░██╗░░░░░░█████╗░░█████╗░███╗░░░███╗░██╗░░░░░░░██╗░█████╗░██████╗░███████╗
        ██╔══██╗██║░░░░░██╔══██╗██╔══██╗████╗░████║░██║░░██╗░░██║██╔══██╗██╔══██╗██╔════╝
        ██████╦╝██║░░░░░██║░░██║██║░░██║██╔████╔██║░╚██╗████╗██╔╝███████║██████╔╝█████╗░░
        ██╔══██╗██║░░░░░██║░░██║██║░░██║██║╚██╔╝██║░░████╔═████║░██╔══██║██╔══██╗██╔══╝░░
        ██████╦╝███████╗╚█████╔╝╚█████╔╝██║░╚═╝░██║░░╚██╔╝░╚██╔╝░██║░░██║██║░░██║███████╗
        ╚═════╝░╚══════╝░╚════╝░░╚════╝░╚═╝░░░░░╚═╝░░░╚═╝░░░╚═╝░░╚═╝░░╚═╝╚═╝░░╚═╝╚══════╝""")

        NativeLibManager
        runnables = listOf(GraphicsManager, FileManager, FriendManager, ModuleManager, CommandManager, PluginClassLoader, ConfigManager)
        saveables = listOf(ConfigManager, Spammer, FriendManager)
        FontManagerr
    }

    @Subscribe
    private fun onPostInit(event: EventEntrypoint.SetOverlay) {
        for (loadable in runnables) loadable.run()
        ClientUtil.playSound()
    }

    @Subscribe
    private fun onStop(event: EventEntrypoint.Stop) {
        for (saveable in saveables) saveable.save()
    }
}