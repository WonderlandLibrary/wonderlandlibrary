package me.crimp.claudius.mod.modules.render;

import me.crimp.claudius.event.events.Render3DEvent;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.utils.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BurrowEsp extends Module {
    public Setting<Integer> range = register(new Setting("Range", 500, 0, 500));
    public Setting<Boolean> self = register(new Setting("Self", true));
    public Setting<Integer> red = register(new Setting("Red", 116, 0, 255));
    public Setting<Integer> green = register(new Setting("Green",85, 0, 255));
    public Setting<Integer> blue = register(new Setting("Blue", 185, 0, 255));
    public Setting<Integer> alpha = register(new Setting("Alpha", 51, 0, 255));
    public Setting<Integer> outlineAlpha = register(new Setting("OL-Alpha", 255, 0, 255));

    private final List<BlockPos> posList = new ArrayList<>();

    public BurrowEsp() {
        super("BurrowEsp", "Shows When People Are Inside Of Blocks", Category.Render, false, false);
    }

    public void onTick(){
        posList.clear();
        for (EntityPlayer player : mc.world.playerEntities){
            BlockPos blockPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY+0.2), Math.floor(player.posZ));
            if((mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK) && blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= this.range.getValue()){
                if (!(blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= 1.5) || this.self.getValue()) {
                    posList.add(blockPos);
                }
            }
        }
    }


    @Override
    public void onRender3D(Render3DEvent event){
        for (BlockPos blockPos : posList) {
            RenderUtil.drawBoxESP(blockPos, new Color(red.getValue(), green.getValue(), blue.getValue(), outlineAlpha.getValue()), 1.5F, true, true, alpha.getValue());
        }
    }
}
