/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import me.AquaVit.liquidSense.API.Particles.roundToPlace
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils.rainbow
import net.ccbluex.liquidbounce.utils.render.GLUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils.*
import net.ccbluex.liquidbounce.value.*
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemTool
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import java.util.*
import kotlin.math.roundToInt


@ModuleInfo(name = "NameTags", description = "Changes the scale of the nametags so you can always read them.", category = ModuleCategory.RENDER)
class NameTags : Module() {

    private val modeValue = ListValue("Mode", arrayOf("LiquidBounce", "Other","New"), "LiquidBounce")
    private val healthValue = BoolValue("Health", true)
    private val pingValue = BoolValue("Ping", true)
    private val distanceValue = BoolValue("Distance", false)
    private val armorValue = BoolValue("Armor", true)
    private val clearNamesValue = BoolValue("ClearNames", false)
    private val fontValue = FontValue("Font", Fonts.font40)
    private val borderValue = BoolValue("Border", true)
    private val scaleValue = FloatValue("Scale", 1F, 1F, 4F)
    private val colorRedValue = IntegerValue("R", 255, 0, 255)
    private val colorGreenValue = IntegerValue("G", 255, 0, 255)
    private val colorBlueValue = IntegerValue("B", 255, 0, 255)
    private val rb = BoolValue("Rainbow", true)
    private val tm = BoolValue("TeamColor", true)

    private val positions: MutableList<Vec3> = ArrayList()
    val teams = LiquidBounce.moduleManager[Teams::class.java] as Teams


    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (modeValue.get().equals("liquidbounce", ignoreCase = true)) {
            for(entity in mc.theWorld.loadedEntityList) {
                if(!EntityUtils.isSelected(entity, false))
                    continue

                renderNameTag(entity as EntityLivingBase,
                        if (clearNamesValue.get())
                            ColorUtils.stripColor(entity.getDisplayName().unformattedText) ?: continue
                        else
                            entity.getDisplayName().unformattedText
                )
            }
        }
        if (modeValue.get().equals("Other", ignoreCase = true)) {
            val iterator: Iterator<Entity> = mc.theWorld.loadedEntityList.iterator()
            while (iterator.hasNext()) {
                val entity = iterator.next()
                if (entity != null && entity !== mc.thePlayer && EntityUtils.isSelected(entity, false)) {
                    updateView()
                }
            }
        }
        if (modeValue.get().equals("New", ignoreCase = true)) {
            for(entity in mc.theWorld.loadedEntityList) {
                if(!EntityUtils.isSelected(entity, false))
                    continue
                val RenderManager = mc.renderManager
                if (entity !== mc.thePlayer) {
                    val pX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
                            - RenderManager.renderPosX)
                    val pY = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks
                            - RenderManager.renderPosY)
                    val pZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
                            - RenderManager.renderPosZ)
                    NameTag(entity as EntityLivingBase, entity.name, pX, pY, pZ)
                }
            }
        }
    }

    fun renderItemStack(stack: ItemStack?, x: Int, y: Int) {
        glPushMatrix()
        glDepthMask(true)
        clear(256)
        RenderHelper.enableStandardItemLighting()
        mc.renderItem.zLevel = -150.0f
        whatTheFuckOpenGLThisFixesItemGlint()
        mc.renderItem.renderItemAndEffectIntoGUI(stack, x, y)
        mc.renderItem.renderItemOverlays(Fonts.font35, stack, x, y)
        mc.renderItem.zLevel = 0.0f
        RenderHelper.disableStandardItemLighting()
        disableCull()
        enableAlpha()
        disableBlend()
        disableLighting()
        scale(0.5, 0.5, 0.5)
        disableDepth()
        enableDepth()
        scale(2.0f, 2.0f, 2.0f)
        glPopMatrix()
    }

    private fun whatTheFuckOpenGLThisFixesItemGlint() {
        disableLighting()
        disableDepth()
        disableBlend()
        enableLighting()
        enableDepth()
        disableLighting()
        disableDepth()
        disableTexture2D()
        disableAlpha()
        disableBlend()
        enableBlend()
        enableAlpha()
        enableTexture2D()
        enableLighting()
        enableDepth()
    }

    fun drawBorderedRectNameTag(x: Float, y: Float, x2: Float, y2: Float, l1: Float, col1: Int, col2: Int) {
        drawRect(x, y, x2, y2, col2)
        val f = (col1 shr 24 and 0xFF) / 255.0f
        val f2 = (col1 shr 16 and 0xFF) / 255.0f
        val f3 = (col1 shr 8 and 0xFF) / 255.0f
        val f4 = (col1 and 0xFF) / 255.0f
        glEnable(3042)
        glDisable(3553)
        glBlendFunc(770, 771)
        glEnable(2848)
        glPushMatrix()
        glColor4f(f2, f3, f4, f)
        glLineWidth(l1)
        glBegin(1)
        glVertex2d(x.toDouble(), y.toDouble())
        glVertex2d(x.toDouble(), y2.toDouble())
        glVertex2d(x2.toDouble(), y2.toDouble())
        glVertex2d(x2.toDouble(), y.toDouble())
        glVertex2d(x.toDouble(), y.toDouble())
        glVertex2d(x2.toDouble(), y.toDouble())
        glVertex2d(x.toDouble(), y2.toDouble())
        glVertex2d(x2.toDouble(), y2.toDouble())
        glEnd()
        glPopMatrix()
        glEnable(3553)
        glDisable(3042)
        glDisable(2848)
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent){
        if (modeValue.get().equals("Other", ignoreCase = true)) {
            glPushMatrix()

            glDisable(GL_DEPTH_TEST)
            val sr = ScaledResolution(mc)
            val twoDscale = sr.scaleFactor / Math.pow(sr.scaleFactor.toDouble(), 2.0)
            glScalef(twoDscale.toFloat(), twoDscale.toFloat(), twoDscale.toFloat())
            val iterator: Iterator<Entity> = mc.theWorld.loadedEntityList.iterator()
            while (iterator.hasNext()) {
                val entity = iterator.next()
                if (entity != null && entity !== mc.thePlayer && EntityUtils.isSelected(entity, false)) {
                    updatePositions(entity)
                    var maxLeft = Float.MAX_VALUE
                    var maxRight = Float.MIN_VALUE
                    var maxBottom = Float.MIN_VALUE
                    var maxTop = Float.MAX_VALUE
                    val iterator2: Iterator<Vec3> = positions.iterator()
                    var canEntityBeSeen = false
                    while (iterator2.hasNext()) {
                        val screenPosition: Vec3 = RenderUtils.WorldToScreen(iterator2.next())
                        if (screenPosition != null) {
                            if (screenPosition.zCoord >= 0.0 && screenPosition.zCoord < 1.0) {
                                maxLeft = Math.min(screenPosition.xCoord, maxLeft.toDouble()).toFloat()
                                maxRight = Math.max(screenPosition.xCoord, maxRight.toDouble()).toFloat()
                                maxBottom = Math.max(screenPosition.yCoord, maxBottom.toDouble()).toFloat()
                                maxTop = Math.min(screenPosition.yCoord, maxTop.toDouble()).toFloat()
                                canEntityBeSeen = true
                            }
                        }
                    }
                    if (!canEntityBeSeen) {
                        continue
                    }
                    if (entity !is EntityLivingBase) {
                        return
                    }
                    glPushMatrix()
                    val ent = entity
                    val health: Double = roundToPlace(ent.health, 1)
                    var prefix = ""
                    prefix = if (AntiBot.isBot(ent)) {
                        "§7"
                    } else if (ent is EntityPlayer && EntityUtils.isFriend(ent)) {
                        "§9"
                    } else if (teams.isInYourTeam(ent)) {
                        "§a"
                    } else if (ent.isSneaking) {
                        "§c"
                    } else {
                        "§r"
                    }
                    val nameStr = prefix + if (clearNamesValue.get()) entity.getName() else entity.getDisplayName().formattedText
                    val healthStr: String = ((health / ent.maxHealth * 100).toInt()).toString() +"%"
                    val distStr = "§7" + entity.getDistanceToEntity(mc.thePlayer).toInt() +"m"

                    val BGCOLOR = Color(0, 0, 0, 200).rgb
                    val BGCOLOR2 = Color(35, 35, 35, 180).rgb
                    val x = maxLeft + (maxRight - maxLeft)
                    val y = maxTop
                    val addons = 2f

                    drawnewrect(x - getStringWidth(nameStr) / 2 - addons.toInt(), y - this.getStringHeight() - addons.toInt(), x + getStringWidth(nameStr) / 2 + addons.toInt(), y + addons.toInt(), BGCOLOR)
                    drawString(nameStr, x - getStringWidth(nameStr) / 2, y - this.getStringHeight(), -1, true)
                    val gg = 4.0f

                    if (healthValue.get()) {
                        drawnewrect(x + getStringWidth(nameStr) / 2 + gg + addons - addons.toInt(), y - this.getStringHeight() - addons.toInt(), x + getStringWidth(nameStr) / 2 + getStringWidth(healthStr) + gg + addons + addons.toInt(), (y + addons.toInt()), BGCOLOR)
                        drawString(healthStr, x + getStringWidth(nameStr) / 2 + gg + addons, y - this.getStringHeight(), getHealthColor(ent.health, ent.maxHealth), true)
                    }
                    if (distanceValue.get()) {
                        drawnewrect(x - getStringWidth(nameStr) / 2 - getStringWidth(distStr) - gg - addons - addons.toInt(), y - this.getStringHeight() - addons.toInt(), x - getStringWidth(nameStr) / 2 - gg - addons + addons.toInt(), (y + addons.toInt()), BGCOLOR)
                        drawString(distStr, x - getStringWidth(nameStr) / 2 - getStringWidth(distStr) - gg - addons, y - this.getStringHeight(), -1, true)
                    }

                    if (armorValue.get() && entity is EntityPlayer) {
                        val itemsToRender: MutableList<ItemStack> = ArrayList()
                        for (index in 0..4) {
                            if (entity.getEquipmentInSlot(index) == null)
                                continue
                        }
                        val space = 4
                        var armorX = 0
                        val size = 2
                        for (stack in itemsToRender) {
                            val startX = x - (itemsToRender.size * 16 * size + Math.max(itemsToRender.size - 1, 0) * space) / 2
                            glPushMatrix()
                            glScalef(size.toFloat(), size.toFloat(), size.toFloat())
                            drawnewrect((startX + armorX) / size, (maxTop - this.getStringHeight() - 16 * size - 4) / size, (startX + armorX) / size + 16, ((maxTop - this.getStringHeight() - 16 * size - 4) / size) as Float + 16, BGCOLOR2)
                            renderItemStack(stack, ((startX + armorX) / size).toInt(), ((maxTop - this.getStringHeight() - 16 * size - 4) / size) as Int)
                            glPopMatrix()
                            val itemDamage = stack.maxDamage - stack.itemDamage
                            mc.fontRendererObj.drawStringWithShadow(itemDamage.toString(), (startX + armorX + (32 - mc.fontRendererObj.getStringWidth(itemDamage.toString()))), ((maxTop - this.getStringHeight() - 40) as Int).toFloat(), -1)
                            armorX += 16 * size + space
                        }
                    }
                    glPopMatrix()
                }
            }
            glEnable(GL_DEPTH_TEST)
            glPopMatrix()
        }

    }
    private fun updatePositions(entity: Entity) {
        positions.clear()
        val position = getEntityRenderPosition(entity)
        val x = position.xCoord - entity.posX
        val y = position.yCoord - entity.posY
        val z = position.zCoord - entity.posZ
        val height = entity.height + 0.3
        val aabb = AxisAlignedBB(entity.posX + x, entity.posY + y, entity.posZ + z, entity.posX + x, entity.posY + height + y, entity.posZ + z)
        positions.add(Vec3(aabb.minX, aabb.minY, aabb.minZ))
        positions.add(Vec3(aabb.minX, aabb.minY, aabb.maxZ))
        positions.add(Vec3(aabb.minX, aabb.maxY, aabb.minZ))
        positions.add(Vec3(aabb.minX, aabb.maxY, aabb.maxZ))
        positions.add(Vec3(aabb.maxX, aabb.minY, aabb.minZ))
        positions.add(Vec3(aabb.maxX, aabb.minY, aabb.maxZ))
        positions.add(Vec3(aabb.maxX, aabb.maxY, aabb.minZ))
        positions.add(Vec3(aabb.maxX, aabb.maxY, aabb.maxZ))
    }

    fun getColor(entity: Entity): Color {
        if (entity is EntityLivingBase) {
            val entityLivingBase = entity
            if (entityLivingBase.hurtTime > 0) return Color.RED
            if (EntityUtils.isFriend(entityLivingBase)) return Color.BLUE
            if (tm.get()) {
                val chars = entityLivingBase.displayName.formattedText.toCharArray()
                var color = Int.MAX_VALUE
                val colors = "0123456789abcdef"
                for (i in chars.indices) {
                    if (chars[i] != '§' || i + 1 >= chars.size) continue
                    val index = colors.indexOf(chars[i + 1])
                    if (index == -1) continue
                    color = ColorUtils.hexColors[index]
                    break
                }
                return Color(color)
            }
        }
        return if (rb.get()) rainbow() else Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get())
    }

    private fun NameTag(entity: EntityLivingBase, tag: String, pX: Double, pY: Double, pZ: Double) {
        var tag = tag
        var pY = pY
        val fr: FontRenderer = Fonts.font40
        var size = mc.thePlayer.getDistanceToEntity(entity) / 6.0f
        if (size < 0.8f) {
            size = 0.8f
        }
        pY += if (entity.isSneaking) 0.5 else 0.7
        var scale = size * 2.0f
        scale /= 100f
        tag = entity.displayName.unformattedText
        var bot = ""
        bot = if (AntiBot.isBot(entity) && LiquidBounce.moduleManager.getModule(AntiBot::class.java)!!.state) {
            "\u00a79[BOT]"
        } else {
            ""
        }
        var team: String
        val Teams = LiquidBounce.moduleManager.getModule("Teams") as Teams?
        team = if (Teams!!.isInYourTeam(entity) && Teams.state) {
            "\u00a7b[TEAM]"
        } else {
            ""
        }
        val RenderManager = mc.renderManager
        if (team + bot == "") team = "\u00a7a"
        val lol = team + bot + tag
        val hp = "\u00a77HP:" + entity.health.toInt()
        glPushMatrix()
        glTranslatef(pX.toFloat(), pY.toFloat() + 1.4f, pZ.toFloat())
        glNormal3f(0.0f, 1.0f, 0.0f)
        glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        glRotatef(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        glScalef(-scale, -scale, scale)
        GLUtils.setGLCap(2896, false)
        GLUtils.setGLCap(2929, false)
        val width = Fonts.font35.getStringWidth(lol) / 2
        GLUtils.setGLCap(3042, true)
        glBlendFunc(770, 771)
        drawBorderedRectNameTag(-width - 2.toFloat(), -(Fonts.font40.FONT_HEIGHT + 9).toFloat(), width + 6.toFloat(), 2.0f, 1.0f,
                RenderUtils.reAlpha(Color.BLACK.rgb, 0.3f), RenderUtils.reAlpha(Color.BLACK.rgb, 0.3f))
        glColor3f(1f, 1f, 1f)
        fr.drawString(lol, -width, -(Fonts.font40.FONT_HEIGHT + 8), -1)
        fr.drawString(hp, -Fonts.font40.getStringWidth(hp) / 2, -(Fonts.font40.FONT_HEIGHT - 2), -1)
        var COLOR = getColor(entity)
        val nowhealth = Math.ceil(entity.health + entity.absorptionAmount.toDouble()).toFloat()
        val health = nowhealth / (entity.maxHealth + entity.absorptionAmount)
        drawRect(width + health * width * 2 - width * 2 + 2, 2f, -width - 2.toFloat(), 0.9f, COLOR)
        glPushMatrix()
        var xOffset = 0
        if(entity is EntityPlayer){
            for (armourStack in entity.inventory.armorInventory) {
                if (armourStack != null) xOffset -= 11
            }
            val renderStack: ItemStack
            if (entity.heldItem != null) {
                xOffset -= 8
                renderStack = entity.heldItem.copy()
                if (renderStack.hasEffect()
                        && (renderStack.item is ItemTool
                                || renderStack.item is ItemArmor)) renderStack.stackSize = 1
                renderItemStack(renderStack, xOffset, -35)
                xOffset += 20
            }
            for (armourStack in entity.inventory.armorInventory) if (armourStack != null) {
                val renderStack1 = armourStack.copy()
                if (renderStack1.hasEffect() && (renderStack1.item is ItemTool
                                || renderStack1.item is ItemArmor)) renderStack1.stackSize = 1
                renderItemStack(renderStack1, xOffset, -35)
                xOffset += 20
            }
        }
        glPopMatrix()
        GLUtils.revertAllCaps()
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        glPopMatrix()
    }

    private fun renderNameTag(entity: EntityLivingBase, tag: String) {
        // Set fontrenderer local
        val fontRenderer = fontValue.get()

        // Modify tag
        val bot = AntiBot.isBot(entity)
        val nameColor = if (bot) "§3" else if (entity.isInvisible) "§6" else if (entity.isSneaking) "§4" else "§7"
        val ping = if (entity is EntityPlayer) EntityUtils.getPing(entity) else 0

        val distanceText = if (distanceValue.get()) "§7${mc.thePlayer.getDistanceToEntity(entity).roundToInt()}m " else ""
        val pingText = if (pingValue.get() && entity is EntityPlayer) (if (ping > 200) "§c" else if (ping > 100) "§e" else "§a") + ping + "ms §7" else ""
        val healthText = if (healthValue.get()) "§7§c " + entity.health.toInt() + " HP" else ""
        val botText = if (bot) " §c§lBot" else ""

        val text = "$distanceText$pingText$nameColor$tag$healthText$botText"

        // Push
        glPushMatrix()

        // Translate to player position
        val renderManager = mc.renderManager
        val timer = mc.timer

        glTranslated( // Translate to player position with render pos and interpolate it
                entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY + entity.eyeHeight.toDouble() + 0.55,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ
        )

        // Rotate view to player
        glRotatef(-mc.renderManager.playerViewY, 0F, 1F, 0F)
        glRotatef(mc.renderManager.playerViewX, 1F, 0F, 0F)

        // Scale
        var distance = mc.thePlayer.getDistanceToEntity(entity) / 4F

        if (distance < 1F)
            distance = 1F

        val scale = distance / 100F * scaleValue.get()

        glScalef(-scale, -scale, scale)

        // Disable lightning and depth test
        disableGlCap(GL_LIGHTING, GL_DEPTH_TEST)

        // Enable blend
        enableGlCap(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        // Draw nametag
        val width = fontRenderer.getStringWidth(text) / 2
        if(borderValue.get())
            drawBorderedRect(-width - 2F, -2F, width + 4F, fontRenderer.FONT_HEIGHT + 2F, 2F,
                    Color(255, 255, 255, 90).rgb, Integer.MIN_VALUE)
        else
            drawRect(-width - 2F, -2F, width + 4F, fontRenderer.FONT_HEIGHT + 2F,
                    Integer.MIN_VALUE)
        fontRenderer.drawString(text, 1F + -width, if (fontRenderer == Fonts.minecraftFont) 1F else 1.5F,
                0xFFFFFF, true)

        if (armorValue.get() && entity is EntityPlayer) {
            for (index in 0..4) {
                if (entity.getEquipmentInSlot(index) == null)
                    continue

                mc.renderItem.zLevel = -147F
                mc.renderItem.renderItemAndEffectIntoGUI(entity.getEquipmentInSlot(index), -50 + index * 20, -22)
            }

            enableAlpha()
            disableBlend()
            enableTexture2D()
        }

        // Reset caps
        resetCaps()

        // Reset color
        resetColor()
        glColor4f(1F, 1F, 1F, 1F)

        // Pop
        glPopMatrix()

    }
    fun drawString(str: String?, x: Float, y: Float, colorHex: Int, shadow: Boolean) {
        mc.fontRendererObj.drawString(str, x, y, colorHex, false)
    }

    fun getStringWidth(str: String?): Int {
        return mc.fontRendererObj.getStringWidth(str)
    }

    fun getStringHeight(): Int {
        return mc.fontRendererObj.FONT_HEIGHT
    }

    fun getHealthColor(health: Float, maxHealth: Float): Int {
        val percentage = health / maxHealth
        return if (percentage >= 0.75f) {
            Color(0, 255, 0).rgb
        } else if (percentage < 0.75 && percentage >= 0.25) {
            Color(255, 255, 0).rgb
        } else {
            Color(255, 0, 0).rgb
        }
    }
}
