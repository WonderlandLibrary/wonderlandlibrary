/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.Gui.Notifications.Notificationsn
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker.tag
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications
import net.ccbluex.liquidbounce.utils.ChatUtil
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.render.ColorUtils.stripColor
import net.ccbluex.liquidbounce.value.Value
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard

@SideOnly(Side.CLIENT)
open class Module() : MinecraftInstance(), Listenable {

    // Module information
    // TODO: Remove ModuleInfo and change to constructor (#Kotlin)
    var name: String
    var description: String
    var category: ModuleCategory
    var arrayListName: String
    var keyBind = Keyboard.CHAR_NONE
        set(keyBind) {
            field = keyBind
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }
    var slideStep = 0F
    private val canEnable: Boolean

    init {
        val moduleInfo = javaClass.getAnnotation(ModuleInfo::class.java)!!
        name = moduleInfo.name
        arrayListName = name
        description = moduleInfo.description
        category = moduleInfo.category
        keyBind = moduleInfo.keyBind
        canEnable = moduleInfo.canEnable

    }
    // Current state of module
    var state = false
        set(value) {
            if (field == value) return

            // Call toggle
            onToggle(value)

            // Play sound and add notification
            if (!LiquidBounce.isStarting) {
                val hd = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD?
                if(hd!!.no.get()) ChatUtil.sendClientMessage(if (value) "$name Enabled" else "$name Disabled", if (value) Notificationsn.Type.SUCCESS else Notificationsn.Type.ERROR)
                mc.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("random.click"),
                        1F))
                LiquidBounce.hud.addNotification(Notification("${if (value) "Enabled " else "Disabled "}$name"," §o("+description+")"))
            }

            // Call on enabled or disabled
            if (value) {
                onEnable()

                if (canEnable)
                    field = true
            } else {
                onDisable()
                field = false
            }

            // Save module state
            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.modulesConfig)
        }


    // HUD
    val hue = Math.random().toFloat()
    var slide = 0F
    var array = true
    var higt = 0F

    // Tag
    open val tag: String?
        get() = null

    val tagName: String
        get() = "$arrayListName${if (tag == null) "" else " §7$tag"}"

    val colorlessTagName: String
        get() = "$arrayListName${if (tag == null) "" else " " + stripColor(tag)}"

    /**
     * Toggle module
     */
    fun toggle() {
        state = !state
    }

    fun setNameCommad( namecommand : String) {
        arrayListName = namecommand
    }

    /**
     * Called when module toggled
     */
    open fun onToggle(state: Boolean) {}

    /**
     * Called when module enabled
     */
    open fun onEnable() {}

    /**
     * Called when module disabled
     */
    open fun onDisable() {}

    /**
     * Get module by [valueName]
     */
    open fun getValue(valueName: String) = javaClass.declaredFields.map { valueField ->
        valueField.isAccessible = true
        valueField[this]
    }.filterIsInstance<Value<*>>().find { it.name.equals(valueName, ignoreCase = true) }

    /**
     * Get all values of module
     */
    open val values: List<Value<*>>
        get() = javaClass.declaredFields.map { valueField ->
            valueField.isAccessible = true
            valueField[this]
        }.filterIsInstance<Value<*>>()

    /**
     * Events should be handled when module is enabled
     */
    override fun handleEvents() = state
}