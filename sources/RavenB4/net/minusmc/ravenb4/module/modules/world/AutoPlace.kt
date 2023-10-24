package net.minusmc.ravenb4.module.modules.world

import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.TickSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minecraft.block.Block
import net.minecraft.block.BlockLiquid
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse

class AutoPlace: Module("AutoPlace", ModuleCategory.world) {
    private val descriptionValue = DescriptionSetting("Description", "Best with safewalk.")
    private val frameDelay = SliderSetting("Frame delay", 8.0, 0.0, 30.0, 1.0)
    private val miniumDelay = SliderSetting("Minimum delay ms", 60.0, 25.0, 500.0, 5.0)
    private val motionValue = SliderSetting("Motion", 1.0, 0.5, 1.2, 0.01)
    private val disableLeft = TickValue("Disable left", false)
    private val holdRight = TickValue("Hold right", true)
    private val fastPlaceonJump = TickValue("Fast place on jump", true)
    private val pitchCheck = TickValue("Pitch check", false)

    private var movingObjectPosition: MovingObjectPosition? = null
    private var blockPos: BlockPos? = null

    private var b = 0.0
    private var c = 0


    init {

    }

    override fun onEnable() {
        b = 0.0
        c = 0
        movingObjectPosition = null
        blockPos = null
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun drawBlockHightLightEvent(drawBlockHighlightEvent: DrawBlockHighlightEvent) {
        if (!gd.aaa() || AutoPlace.b.field_71462_r != null || AutoPlace.b.field_71439_g.field_71075_bZ.field_75100_b) {
            return
        }
        val itemStack = mc.thePlayer.heldItem
        if (itemStack == null || itemStack.item !is ItemBlock) {
            return
        }
        if (disableLeft.getValue() && Mouse.isButtonDown(0)) {
            return
        }
        if (holdRight.getValue() && !Mouse.isButtonDown(1)) {
            return
        }
        if (pitchCheck.getValue() && mc.thePlayer.rotationPitch < 70.0f) {
            return
        }
        val movingObjectPosition = mc.objectMouseOver
        if (movingObjectPosition == null || movingObjectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || movingObjectPosition.sideHit == EnumFacing.UP || movingObjectPosition.sideHit == EnumFacing.DOWN) {
            return
        }

        if (this.movingObjectPosition != null && (double)this.c < frameDelay.getValue()) {
            ++this.c
            return
        }
        this.movingObjectPosition = movingObjectPosition
        BlockPos blockPos = movingObjectPosition.getBlockPos()
        if (this.g != null && bl.f(blockPos, this.g)) {
            return
        }
        Block block = mc.theWorld.getBlockState(blockPos).getBlock()
        if (block == null || block == Blocks.field_150350_a || block instanceof BlockLiquid || bl.l(block)) {
            return
        }
        long l2 = System.currentTimeMillis()
        if (l2 - 0L < miniumDelay.getValue()) {
            return
        }
        IIIIIIIIIlllllIIlIlIIllIlllIIIllIlIIIlllIIIlIIlllIIlIIlIlIIIIIllllIIIIIllIIllllIllIlIllIlIlIIllIlIIlllIIIlIIllIIIllIllllllIIIlIllllIlIllIIlllllIlIIlIIlIIIIIIlIIIlIIIIIIIIIlllllllIIIIIlIlllIIIlllllllII = l2
        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, blockPos,
                movingObjectPosition.sideHit, movingObjectPosition.hitVec)) {
            gd.ai(1, true)
            mc.thePlayer.swingItem()
            mc.getItemRenderer().renderOverlays()
            gd.ai(1, false)
            this.blockPos = blockPos
            this.c = 0
        }
    }

    public static boolean canPlace(C08PacketPlayerBlockPlacement c08PacketPlayerBlockPlacement) {
        return c08PacketPlayerBlockPlacement.getPlacedBlockDirection() != 255 || c08PacketPlayerBlockPlacement.getStack() == null
                || !(c08PacketPlayerBlockPlacement.getStack().getItem() instanceof ItemBlock) || System.currentTimeMillis() - 0L > 50L && !(Math.random() >= 0.5)
    }
}

