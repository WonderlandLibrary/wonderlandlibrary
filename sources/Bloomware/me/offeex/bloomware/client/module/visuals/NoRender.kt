package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingMap
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object NoRender : Module("NoRender", "Allows you to cancel useless things rendering", Category.RENDER) {
	private val overlay = setting("Overlay").group()
	val pumpkin = overlay.setting("Pumpkin").map("textures/misc/pumpkinblur")
	val vignette = overlay.setting("Vignette").map("textures/gui/widgets")
	val widgets = overlay.setting("Widgets").map("textures/misc/powder_snow_outline")
	val spyGlassScope = overlay.setting("SpyGlassScope").bool()
	val powderSnow = overlay.setting("PowderSnow").bool()
	val fire = overlay.setting("Fire").bool()
	val water = overlay.setting("Water").bool()
	val walls = overlay.setting("Walls").bool()
	val nausea = overlay.setting("Nausea").number(100.0, 0.0, 100.0, 1.0)
	val portal = overlay.setting("Portal").bool()
	val totem = overlay.setting("Totem").bool()
	val hurtCamera = overlay.setting("HurtCamera").bool()

	private val game = setting("Game").group()
	val enchantedTable = game.setting("EnchantedTable").bool()
	val bob = game.setting("Bob").bool()
	val fireworks = game.setting("Fireworks").bool()
	val fog = game.setting("Fog").bool()
	private val fallingBlocks = game.setting("FallingBlocks").bool(true)
	private val experienceOrbs = game.setting("ExperienceOrb").bool()
	val scoreboard = game.setting("Scoreboard").bool()
	val weather = game.setting("Weather").bool()

	private val particles = setting("Particles").group()
	val fireworkDust = particles.setting("FireworkDust").bool()
	val explosions = particles.setting("Explosions").bool()

	private val screen = setting("Screen").group()
	val darkness = screen.setting("Darkness").bool()

	@Subscribe
	private fun onEntityRender(event: EventRender.Entity) {
		event.canceled = event.entity is ExperienceOrbEntity && experienceOrbs.toggled
		event.canceled = event.entity is FallingBlockEntity && fallingBlocks.toggled
	}

	override fun onTick() {
		mc.options.distortionEffectScale.value = nausea.value / 100
	}

	fun handleOverlays(id: Identifier, ci: CallbackInfo) {
		overlay.settings.filterIsInstance(SettingMap::class.java).forEach {
			if (id.path.contains((it.key) as String) && it.toggled) ci.cancel()
		}
	}
}