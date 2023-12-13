package me.dev.legacy.features.modules.movement;

import me.dev.legacy.Legacy;
import me.dev.legacy.event.events.BlockCollisionBoundingBoxEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoWeb extends Module
{
    public Setting<Boolean> disableBB;
    public Setting<Float> bbOffset;
    public Setting<Boolean> onGround;
    public Setting<Float> motionY;
    public Setting<Float> motionX;

    public NoWeb() {
        super("NoWeb", "aw", Category.MOVEMENT, true, false, false);
        this.disableBB = (Setting<Boolean>)this.register(new Setting("AddBB", true));
        this.bbOffset = (Setting<Float>)this.register(new Setting("BBOffset", 0.4f, (-2.0f), 2.0f));
        this.onGround = (Setting<Boolean>)this.register(new Setting("On Ground", true));
        this.motionY = (Setting<Float>)this.register(new Setting("Set MotionY", 0.0f, 0.0f, 20.0f));
        this.motionX = (Setting<Float>)this.register(new Setting("Set MotionX", 0.8f, (-1.0f), 5.0f));
    }

    @SubscribeEvent
    public void bbEvent(final BlockCollisionBoundingBoxEvent event) {
        if (nullCheck()) {
            return;
        }
        if (NoWeb.mc.world.getBlockState(event.getPos()).getBlock() instanceof BlockWeb && this.disableBB.getValue()) {
            event.setCanceled(true);
            event.setBoundingBox(Block.FULL_BLOCK_AABB.contract(0.0, (double)this.bbOffset.getValue(), 0.0));
        }
    }

    @Override
    public void onUpdate() {
        if (Legacy.moduleManager.isModuleEnabled("WebTP")) {
            return;
        }
        if ((NoWeb.mc.player.isInWeb && !Legacy.moduleManager.isModuleEnabled("Step")) || (NoWeb.mc.player.isInWeb && !Legacy.moduleManager.isModuleEnabled("StepTwo"))) {
            if (Keyboard.isKeyDown(NoWeb.mc.gameSettings.keyBindSneak.keyCode)) {
                NoWeb.mc.player.isInWeb = true;
                final EntityPlayerSP player = NoWeb.mc.player;
                player.motionY *= this.motionY.getValue();
            }
            else if (this.onGround.getValue()) {
                NoWeb.mc.player.onGround = false;
            }
            if (Keyboard.isKeyDown(NoWeb.mc.gameSettings.keyBindForward.keyCode) || Keyboard.isKeyDown(NoWeb.mc.gameSettings.keyBindBack.keyCode) || Keyboard.isKeyDown(NoWeb.mc.gameSettings.keyBindLeft.keyCode) || Keyboard.isKeyDown(NoWeb.mc.gameSettings.keyBindRight.keyCode)) {
                NoWeb.mc.player.isInWeb = false;
                final EntityPlayerSP player2 = NoWeb.mc.player;
                player2.motionX *= this.motionX.getValue();
                final EntityPlayerSP player3 = NoWeb.mc.player;
                player3.motionZ *= this.motionX.getValue();
            }
        }
    }
}

