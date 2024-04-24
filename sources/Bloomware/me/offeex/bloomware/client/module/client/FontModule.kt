package me.offeex.bloomware.client.module.client

import me.offeex.bloomware.api.manager.managers.FontManagerr
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting

object FontModule : Module("Font", "Allows you to configure client's fonts", Category.CLIENT) {
    private val fontName = setting("Font").enum("Raleway")
    private val fontSize = setting("Size").number(20.0, 10.0, 24.0, 1.0)

    init {
        fontName.selectedUpdateBus.subscribe { _, newValue ->
            FontManagerr.updateFontTTF(newValue, fontSize.value)
        }
        fontSize.valueUpdateBus.subscribe { _, newValue ->
            FontManagerr.updateSize(newValue.toFloat())
        }
    }

    override fun onDisable() = enable()
}