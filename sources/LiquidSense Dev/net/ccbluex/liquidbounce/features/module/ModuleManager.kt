/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.KeyEvent
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.features.module.modules.`fun`.Derp
import net.ccbluex.liquidbounce.features.module.modules.`fun`.SkinDerp
import net.ccbluex.liquidbounce.features.module.modules.combat.*
import net.ccbluex.liquidbounce.features.module.modules.exploit.*
import net.ccbluex.liquidbounce.features.module.modules.misc.*
import net.ccbluex.liquidbounce.features.module.modules.movement.*
import net.ccbluex.liquidbounce.features.module.modules.player.*
import net.ccbluex.liquidbounce.features.module.modules.render.*
import net.ccbluex.liquidbounce.features.module.modules.world.*
import net.ccbluex.liquidbounce.features.module.modules.world.Timer
import net.ccbluex.liquidbounce.utils.ClientUtils
import java.util.*


class ModuleManager : Listenable {

    val modules = TreeSet<Module> { module1, module2 -> module1.name.compareTo(module2.name) }
    private val moduleClassMap = hashMapOf<Class<*>, Module>()

    init {
        LiquidBounce.eventManager.registerListener(this)
    }

    /**
     * Register all modules
     */
    fun registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...")

        registerModules(
                Aimbot::class.java,
                AutoArmor::class.java,
                AutoBow::class.java,
                AutoPot::class.java,
                AutoSoup::class.java,
                AutoWeapon::class.java,
                BowAimbot::class.java,
                Criticals::class.java,
                Aura::class.java, //
                Ghost::class.java,
                Velocity::class.java,
                Fly::class.java,
                ClickGUI::class.java,
                HighJump::class.java,
                InventoryMove::class.java,
                GhostHand::class.java,
                NoSlow::class.java,
                LiquidWalk::class.java,
                SafeWalk::class.java,
                Strafe::class.java,
                Sprint::class.java,
                Teams::class.java,
                NoRotateSet::class.java,
                AntiBot::class.java,
                ChestStealer::class.java,
                Scaffold::class.java,
                Tower::class.java,
                FastBreak::class.java,
                FastPlace::class.java,
                ESP::class.java,
                Speed::class.java,
                Tracers::class.java,
                NameTags::class.java,
                FastUse::class.java,
                Teleport::class.java,
                Fullbright::class.java,
                ItemESP::class.java,
                StorageESP::class.java,
                Projectiles::class.java,
                NoClip::class.java,
                PingSpoof::class.java,
                FastClimb::class.java,
                Step::class.java,
                AutoRespawn::class.java,
                AutoTool::class.java,
                NoWeb::class.java,
                Spammer::class.java,
                IceSpeed::class.java,
                Zoot::class.java,
                Regen::class.java,
                NoFall::class.java,
                Blink::class.java,
                NameProtect::class.java,
                NoHurtCam::class.java,
                MidClick::class.java,
                XRay::class.java,
                Timer::class.java,
                SkinDerp::class.java,
                AutoWalk::class.java,
                FreeCam::class.java,
                Eagle::class.java,
                HitBox::class.java,
                AntiCactus::class.java,
                Plugins::class.java,
                ConsoleSpammer::class.java,
                LongJump::class.java,
                FastBow::class.java,
                AutoClicker::class.java,
                NoBob::class.java,
                NoFriends::class.java,
                BlockESP::class.java,
                Chams::class.java,
                Clip::class.java,
                ServerCrasher::class.java,
                NoFOV::class.java,
                FastStairs::class.java,
                SwingAnimation::class.java,
                Derp::class.java,
                InventoryCleaner::class.java,
                TrueSight::class.java,
                LiquidChat::class.java,
                AntiBlind::class.java,
                NoSwing::class.java,
                PotionSaver::class.java,
                //CameraClip::class.java,
                Kick::class.java,
                Liquids::class.java,
                BufferSpeed::class.java,
                SuperKnockback::class.java,
                ProphuntESP::class.java,
                AutoFish::class.java,
                Freeze::class.java,
                KeepContainer::class.java,
                Reach::class.java,
                NoJumpDelay::class.java,
                BlockWalk::class.java,
                AntiAFK::class.java,
                PerfectHorseJump::class.java,
                HUD::class.java,
                TNTESP::class.java,
                ComponentOnHover::class.java,
                KeepAlive::class.java,
                AbortBreaking::class.java,
                BlockOverlay::class.java,
                Rotations::class.java,
                Damage::class.java,
                BugUp::class.java,
                Debug::class.java,
                AntiObsidian::class.java
        )

        registerModule(NoScoreboard)
        registerModule(Fucker)
        registerModule(ChestAura)

        ClientUtils.getLogger().info("[ModuleManager] Loaded ${modules.size} modules.")
    }

    /**
     * Register [module]
     */
    fun registerModule(module: Module) {
        modules += module
        moduleClassMap[module.javaClass] = module

        generateCommand(module)
        LiquidBounce.eventManager.registerListener(module)
    }

    /**
     * Register [moduleClass]
     */
    private fun registerModule(moduleClass: Class<out Module>) {
        try {
            registerModule(moduleClass.newInstance())
        } catch (e: Throwable) {
            ClientUtils.getLogger().error("Failed to load module: ${moduleClass.name} (${e.javaClass.name}: ${e.message})")
        }
    }

    /**
     * Register a list of modules
     */
    @SafeVarargs
    fun registerModules(vararg modules: Class<out Module>) {
        var liquidSenseModules = LiquidBounce.liquidSense.liquidSenseModules
        for (lbModule in modules){
            registerModule(lbModule)
        }
        for (cbModule in liquidSenseModules){
            registerModule(cbModule)
        }
    }

    private fun registerModule(cbModule: Any?) {
        registerModule((cbModule as Class<out Module>).newInstance())
    }


    /**
     * Unregister module
     */
    fun unregisterModule(module: Module) {
        modules.remove(module)
        moduleClassMap.remove(module::class.java)
        LiquidBounce.eventManager.unregisterListener(module)
    }

    /**
     * Generate command for [module]
     */
    private fun generateCommand(module: Module) {
        val values = module.values

        if (values.isEmpty())
            return

        LiquidBounce.commandManager.registerCommand(ModuleCommand(module, values))
    }

    /**
     * Legacy stuff
     *
     * TODO: Remove later when everything is translated to Kotlin
     */

    /**
     * Get module by [moduleClass]
     */
    fun getModule(moduleClass: Class<*>) = moduleClassMap[moduleClass]

    operator fun get(clazz: Class<*>) = getModule(clazz)

    /**
     * Get module by [moduleName]
     */
    fun getModule(moduleName: String?) = modules.find { it.name.equals(moduleName, ignoreCase = true) }

    /**
     * Module related events
     */

    /**
     * Handle incoming key presses
     */
    @EventTarget
    private fun onKey(event: KeyEvent) = modules.filter { it.keyBind == event.key }.forEach { it.toggle() }

    override fun handleEvents() = true
}
