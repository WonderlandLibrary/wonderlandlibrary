package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.api.helper.ProtectionMark
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.module.tweaks.*
import me.offeex.bloomware.client.module.client.*
import me.offeex.bloomware.client.module.client.Notifications
import me.offeex.bloomware.client.module.client.StreamerMode
import me.offeex.bloomware.client.module.visuals.ToolTips
import me.offeex.bloomware.client.module.client.UnfocusedCPU
import me.offeex.bloomware.client.module.hud.*
import me.offeex.bloomware.client.module.hud.PlayerModel as PlayerModelHud
import me.offeex.bloomware.client.module.hud.Target
import me.offeex.bloomware.client.module.player.*
import me.offeex.bloomware.client.module.motion.*
import me.offeex.bloomware.client.module.network.*
import me.offeex.bloomware.client.module.pvp.*
import me.offeex.bloomware.client.module.visuals.*
import me.offeex.bloomware.client.module.world.*
import me.offeex.bloomware.event.events.EventInput
import me.offeex.bloomware.event.events.EventTick
import org.lwjgl.glfw.GLFW

object ModuleManager : Manager(), Runnable {
    var modules: MutableList<Module> = mutableListOf(AntiBots /*, new BowBomb()*/, NoDesync, Spoof, Preventer, Notifier, NoTrace, Durability, AutoRespawn, FakePlayer, MobOwner, NewChunks, NoRender, SoundTracker, Tracers, Timer, BarrierView, BoxHighlight, Environment, ESP, ESPTest, FullBright, LogoutSpot, Nametags, BetterScreenshot, ViewClip, CustomFOV, DamageTint, DeathCam, Freecam, RotationLock, ViewModel, Anchor, AutoAnvil, AutoCrystal, AutoLog, Offhand, Criticals, HoleFiller, KillAura, SelfAnvil, SelfWeb, Surround, Blink, PacketCancel, PacketLogger, AutoElytra, AutoJump, AutoWalk, BoatFly, ElytraFly, Flight, NoFall, NoSlow, Speed, Step, Velocity, AutoFish, AutoMount, Break, Interact, MiddleClick, Scaffold, XCarry, Armor, BreakingBlock, Position, Fps, HoleInfo, Hunger, InventoryViewer, LastPacket, MemoryUsage, ModuleList, ModuleNotifier, OnlineTime, Ping, PlayerCount, PlayerModelHud, PvPInfo,  CurrentServer, Speedometer, TabGUI, Target, TextRadar, Time, Tps, Watermark, Welcomer, YawPitch, Notifications/*, new RichPresence*/, StreamerMode, UnfocusedCPU, Colors, FontModule, Gui, Hud, BetterChat, BetterTab, Spammer, ToolTips, GodMode /*, new AutoReact()*/, Particles /*, new FGM148Javelin */, PortalGUI, PressRotate, GlobalControls, FastFall, TickShift, Sprint)

    override fun run() = modules.sortBy { it.name }

    @ProtectionMark
    fun protection() {}

    fun is_real(bool: Boolean?) = bool?.is_actually_real { this == true } ?: !true
    fun Boolean.is_actually_real(reall: Boolean.() -> Boolean) = ((((reall(this) == true)))) == true

    @Subscribe
    private fun onTick(event: EventTick) {
        checkNull { modules.forEach { if (it.enabled) it.onTick() } }
    }

    @Subscribe
    private fun onKeyPress(event: EventInput.Key.Press) {
        modules.forEach { if (event.key != GLFW.GLFW_KEY_F3 && it.key == event.key) it.toggle() }
    }

    fun addModule(mod: Module) {
        modules.add(mod)
        modules.sortBy { it.name }
    }

    fun getModuleByName(name: String): Module? = modules.find { it.name == name }

    fun getModulesByCategory(c: Module.Category) = modules.filter { it.category == c } // TODO: Remove

    fun checkNull(callback: (() -> Unit)? = null) {
        val isNull = mc.player == null || mc.world == null
        if (callback != null && !isNull) callback()
        else if (isNull && callback == null) throw RuntimeException("Bruh, you're not in game!")
    }
}
