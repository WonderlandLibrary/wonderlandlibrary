package com.masterof13fps.features.modules.impl.render;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.render.RenderUtils;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventRender;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.features.modules.Category;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.Objects;

@ModuleInfo(name = "BlockInfo", category = Category.RENDER, description = "Shows a small label with block name and id")
public class BlockInfo extends Module {
    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender) {
            if (((EventRender) event).getType() == EventRender.Type.twoD) {
                if (!(Objects.isNull(mc.objectMouseOver.getBlockPos()))) {
                    BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                    Block block = getBlockAtPos(blockPos);

                    if(block instanceof BlockAir){
                        return;
                    }

                    ScaledResolution s = new ScaledResolution(mc);

                    UnicodeFontRenderer font = Client.main().fontMgr().font("BigNoodleTitling", 20, Font.PLAIN);

                    String text = block.getLocalizedName() + " ID: " + Block.getIdFromBlock(block);

                    RenderUtils.drawRect(s.width() / 2 + 10, s.height() / 2 - 8, s.width() / 2 + font.getStringWidth(text) + 15, s.height() / 2 + 8, new Color(0, 0, 0).getRGB());

                    font.drawStringWithShadow(text, s.width() / 2 + 12, s.height() / 2 - 4, -1);
                }
            }
        }
    }

    public static Block getBlockAtPos(BlockPos inBlockPos) {
        BlockPos currentPos = inBlockPos;
        IBlockState s = mc.theWorld.getBlockState(currentPos);
        return s.getBlock();
    }
}
