/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.ui.cape

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import java.awt.image.BufferedImage

class SingleImageCape(override val name: String, val image: BufferedImage) : ICape {
    override val cape = ResourceLocation("fdpclient/cape/${name.lowercase().replace(" ","_")}")

    init {
        Minecraft.getMinecraft().textureManager.loadTexture(cape, DynamicTexture(image))
    }

    override fun finalize() {
        Minecraft.getMinecraft().textureManager.deleteTexture(cape)
    }
}