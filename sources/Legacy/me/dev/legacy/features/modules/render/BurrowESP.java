package me.dev.legacy.features.modules.render;

import me.dev.legacy.event.events.Render3DEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.modules.client.ClickGui;
import me.dev.legacy.features.modules.combat.AutoCrystal;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.ColorUtil;
import me.dev.legacy.util.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;

public class BurrowESP extends Module {
    public BurrowESP() {
        super("BurrowESP", "BurrowESP", Module.Category.RENDER, false, false, false);
    }

    private final ArrayList<BlockPos> burrowedBlocks = new ArrayList<>();

    private final Setting<Integer> red = register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = register(new Setting<Integer>("Blue", 255, 0, 255));
    private final Setting<Integer> boxAlpha = register(new Setting<Integer>("BoxAlpha", 120, 0, 255));
    private final Setting<Integer> alpha = register(new Setting<Integer>("Alpha", 255, 0, 255));
    public Setting<Boolean> colorSync = this.register(new Setting<Object>("ColorSync", Boolean.valueOf(false)));
    public Setting<Boolean> box = this.register(new Setting<Object>("Box", Boolean.valueOf(true)));
    public Setting<Boolean> outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true)));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.5f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    public Setting<Boolean> customOutline = this.register(new Setting<Object>("CustomLine", Boolean.valueOf(false)));
    private final Setting<Integer> cRed = this.register(new Setting<Object>("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    private final Setting<Integer> cGreen = this.register(new Setting<Object>("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    private final Setting<Integer> cBlue = this.register(new Setting<Object>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    private final Setting<Integer> cAlpha = this.register(new Setting<Object>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));


    @Override
    public void onEnable() {
        burrowedBlocks.clear();
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) return;
        this.burrowedBlocks.clear();
        this.getFuckedPlayers();
    }

    @Override
    public void onRender3D(Render3DEvent event) {
            if (!this.burrowedBlocks.isEmpty()) {
                this.burrowedBlocks.forEach(this::renderBurrowedBlock);
            }
    }

    private void renderBurrowedBlock(BlockPos pos) {
        RenderUtil.drawBoxESP(pos, this.colorSync.getValue() != false ? ColorUtil.rainbow((int) ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), this.colorSync.getValue() != false ? getCurrentColor() : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
    }

    public Color getCurrentColor() {
        return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }

    private void getFuckedPlayers() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (this.isBurrowed(mc.player)) {
                this.burrowedBlocks.add(new BlockPos(player.posX, player.posY, player.posZ));
            }
        }
    }

    private boolean isBurrowed(EntityPlayer player) {
        BlockPos pos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY+0.2), Math.floor(player.posZ));
        return mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos).getBlock() == Blocks.CHEST;
    }
}
