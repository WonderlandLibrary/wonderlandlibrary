package net.ccbluex.liquidbounce.features.module.modules.mods

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.ScreenEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.client.gui.GuiChat
import net.minecraft.util.ResourceLocation

@ModuleInfo(name = "Blur", category = ModuleCategory.MODS, array = false)
class Blur : Module() {

        override fun onDisable() {
            if (mc.entityRenderer.shaderGroup != null && mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("fdpclient/blur.json")) {
                mc.entityRenderer.stopUseShader()
            }
        }

        @EventTarget
        fun onScreen(event: ScreenEvent) {
            if (mc.theWorld == null || mc.thePlayer == null) {
                return
            }
            if (this.state  && !mc.entityRenderer.isShaderActive && event.guiScreen != null && !(event.guiScreen is GuiChat || event.guiScreen is GuiHudDesigner)) {
                mc.entityRenderer.loadShader(ResourceLocation("fdpclient/blur.json"))
            } else if (mc.entityRenderer.shaderGroup != null && mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("fdpclient/blur.json")) {
                mc.entityRenderer.stopUseShader()
            }
        }

}
