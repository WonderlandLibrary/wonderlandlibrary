package me.offeex.bloomware.client.module

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.manager.managers.ModuleManager
import me.offeex.bloomware.client.module.hud.ModuleNotifier
import me.offeex.bloomware.client.setting.Setting
import me.offeex.bloomware.client.setting.SettingsContainer
import net.minecraft.network.listener.PacketListener
import net.minecraft.network.packet.Packet

abstract class Module(val name: String, val description: String, val category: Category) : SettingsContainer {
    var key: Int = 0
    override val settings: MutableList<Setting> = mutableListOf()
    var enabled: Boolean = false
        private set

    // So fucking awful
    override val nameContainer: String
        get() = name

    enum class Category(val title: String) {
        PVP("PvP"), MOTION("Motion"), PLAYER("Player"), TWEAKS("Tweaks"), NETWORK("Network"), RENDER("Render"), WORLD("World"), CLIENT("Client"), HUD("HUD")
    }

    open fun toggle() = if (enabled) disable() else enable()

    fun enable() {
        if (enabled) return
        ModuleNotifier.setMessage("$name enabled!")
        Bloomware.EVENTBUS.register(this)
        enabled = true
        ModuleManager.checkNull { onEnable() } // Temporary fix
    }

    fun disable() {
        if (!enabled) return
        ModuleNotifier.setMessage("$name disabled!")
        Bloomware.EVENTBUS.unregister(this)
        enabled = false
        ModuleManager.checkNull { onDisable() }
    }

    protected open fun onEnable(): Unit? = null
    protected open fun onDisable(): Unit? = null
    open fun onTick() {}

    protected fun <T : PacketListener> sendPacket(p: Packet<T>) {
        cNetHandler.sendPacket(p)
    }
}