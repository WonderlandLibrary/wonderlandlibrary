package me.AquaVit.liquidSense.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.event.TickEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils.rainbow
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.model.ModelBase
import org.lwjgl.opengl.GL11
import org.spongepowered.asm.mixin.Shadow
import java.awt.Color
import java.util.*

@ModuleInfo(name = "FakeBody", description = "FakeBody", category = ModuleCategory.RENDER)
class FakeBody : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Normal", "Color"), "Normal")
    private val delay = IntegerValue("Delay",2,0,8)
    private val range = IntegerValue("FakeRange",2,0,4)
    private val colorRedValue = IntegerValue("R", 0, 0, 255)
    private val colorGreenValue = IntegerValue("G", 160, 0, 255)
    private val colorBlueValue = IntegerValue("B", 255, 0, 255)
    private val alphaValue = IntegerValue("Alpha", 255, 0, 255)
    private val rainbow = BoolValue("RainBow", false)
    private val third = BoolValue("ThirdView",false)
    var sb = if (rainbow.get()) rainbow() else Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get(), alphaValue.get())
    private val old = mc.gameSettings.thirdPersonView
    var fakePlayer: EntityOtherPlayerMP? = null
    private var ticks = 0

    fun getModeValue(): ListValue? {
        return modeValue
    }

    override fun onEnable() {
        if (mc.thePlayer == null) return
    }

    override fun onDisable() {

        if (mc.thePlayer == null) return
        ticks = 0
        remove()
        if (third.get() && mc.gameSettings.thirdPersonView > 3) mc.gameSettings.thirdPersonView = old
    }

    fun remove(){
        if (fakePlayer != null) {
            mc.theWorld.removeEntityFromWorld(fakePlayer!!.entityId)
            fakePlayer = null
        }
    }

    fun drawbody(){
        fakePlayer = EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.gameProfile)
        fakePlayer!!.copyLocationAndAnglesFrom(mc.thePlayer)
        fakePlayer!!.rotationYaw = RandomUtils.nextFloat(-360f,360f)
        fakePlayer!!.rotationPitch = RandomUtils.nextFloat(-360f,360f)
        if(Random().nextBoolean()) fakePlayer!!.swingItem()
        fakePlayer!!.posX = mc.thePlayer.posX + RandomUtils.nextFloat(-range.get().toFloat(),range.get().toFloat())
        fakePlayer!!.posZ = mc.thePlayer.posZ + RandomUtils.nextFloat(-range.get().toFloat(),range.get().toFloat())
        mc.theWorld.addEntityToWorld(-1337, fakePlayer)
    }

    @EventTarget
    fun Tick(event: TickEvent){
        if(ticks == delay.get() * 10){
            drawbody()
            ticks = 0
        } else {
            ticks++
        }
    }

    @EventTarget
    fun Third(event: MoveEvent) {
        if(third.get()){
            mc.gameSettings.thirdPersonView = 4
        }
    }

}