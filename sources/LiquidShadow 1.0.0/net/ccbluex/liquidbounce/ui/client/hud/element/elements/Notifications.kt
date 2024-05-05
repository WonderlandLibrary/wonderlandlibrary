/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.AnimationUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.IntegerValue
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications")
class Notifications(x: Double = 0.0, y: Double = 30.0, scale: Float = 1F,
                    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    /**
     * Example notification for CustomHUD designer
     */
    private val exampleNotification = Notification("Example Notification",NotificationType.NORMAL)

    private val backgroundAlphaValue = IntegerValue("backgroundAlpha",60,0,255)

    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        if (LiquidBounce.hud.notifications.size > 0) {
            var i = 0
            var y = 0F
            try {
                for (notification in LiquidBounce.hud.notifications) {
                    LiquidBounce.hud.notifications[i].drawNotification(backgroundAlphaValue.get(),y)
                    i++
                    y += 30F
                }
            } catch (_:ConcurrentModificationException) {
                
            }


        }


        if (mc.currentScreen is GuiHudDesigner) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification))
                LiquidBounce.hud.addNotification(exampleNotification)

            exampleNotification.fadeState = Notification.FadeState.STAY
            exampleNotification.x = exampleNotification.textLength + 8F

            return Border(-95F, -20F, 0F, 0F)
        }

        return null
    }

}

class Notification(private val message: String,private val notificationType: NotificationType) {
    var x = 0F
    var textLength = 0

    private var stay = 0F
    private var fadeStep = 0F
    var fadeState = FadeState.IN

    /**
     * Fade state for animation
     */
    enum class FadeState { IN, STAY, OUT, END }

    init {
        textLength = Fonts.font35.getStringWidth(message)
    }

    /**
     * Draw notification
     */
    fun drawNotification(backgroundAlpha:Int,y: Float) {
        // Draw notification
        RenderUtils.drawRect(-x + 8 + textLength, -y, -x, -y + -20F, Color(0,0,0,backgroundAlpha).rgb)
        if (notificationType == NotificationType.NORMAL) {
            RenderUtils.drawRect(-x + 8 + textLength, -y, -x, -y + -20F, Color(65, 215, 255,backgroundAlpha).rgb)
            RenderUtils.drawRect(-x + stay, -y, -x, -y + -20F, Color(150, 255, 255,backgroundAlpha).rgb)
        }
        else if (true) {
            if (notificationType == NotificationType.GOOD) {
                RenderUtils.drawRect(-x + 8 + textLength, -y, -x, -y + -20F, Color(80, 255, 80,backgroundAlpha).rgb)
                RenderUtils.drawRect(-x + stay, -y, -x, -y + -20F, Color(150, 255, 150,backgroundAlpha).rgb)
            } else if (notificationType == NotificationType.BAD) {
                RenderUtils.drawRect(-x + 8 + textLength, -y, -x, -y + -20F, Color(255,80,80,backgroundAlpha).rgb)
                RenderUtils.drawRect(-x + stay, -y, -x, -y + -20F, Color(255,150,150,backgroundAlpha).rgb)
            }
        }
        else if (notificationType == NotificationType.WARNING) {
            RenderUtils.drawRect(-x + 8 + textLength, -y, -x, -y + -20F, Color(255,195,0,backgroundAlpha).rgb)
            RenderUtils.drawRect(-x + stay, -y, -x, -y + -20F, Color(255,235,45,backgroundAlpha).rgb)
        }

        Fonts.font35.drawString(message, -x + 4, -y + -14F, Color(50,50,50).rgb)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        // Animation
        val delta = RenderUtils.deltaTime
        val width = textLength + 8F

        when (fadeState) {
            FadeState.IN -> {
                if (x < width) {
                    x = AnimationUtils.easeOut(fadeStep, width) * width
                    fadeStep += delta / 4F
                }
                if (x >= width) {
                    fadeState = FadeState.STAY
                    x = width
                    fadeStep = width
                }

                stay = textLength.toFloat()
            }

            FadeState.STAY -> if (stay > 0)
                stay--;
            else
                fadeState = FadeState.OUT

            FadeState.OUT -> if (x > 0) {
                x = AnimationUtils.easeOut(fadeStep, width) * width
                fadeStep -= delta / 4F
            } else
                fadeState = FadeState.END

            FadeState.END -> LiquidBounce.hud.removeNotification(this)
        }

    }
}

enum class NotificationType {
    NORMAL,GOOD,BAD,WARNING
}
