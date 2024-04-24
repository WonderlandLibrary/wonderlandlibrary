package me.offeex.bloomware.api.util

import dev.virefire.yok.Yok
import net.fabricmc.loader.api.FabricLoader

object NetworkUtil {
    val CLIENT = Yok {
        baseUrl = url()
    }

    private fun url() = "https://api.bloomware.org"
}