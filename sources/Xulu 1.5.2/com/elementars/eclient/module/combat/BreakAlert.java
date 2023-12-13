// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.util.ColorUtils;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.command.Command;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.init.Items;
import com.elementars.eclient.util.ColorTextUtils;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.util.math.BlockPos;
import io.netty.util.internal.ConcurrentSet;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class BreakAlert extends Module
{
    private final Value<String> mode;
    private final Value<Boolean> ignoreSelf;
    private final Value<Boolean> watermark;
    private final Value<String> color;
    private ConcurrentSet<BlockPos> breaking;
    private ConcurrentSet<BlockPos> test;
    public BlockPos[] xd;
    boolean testy;
    int delay;
    
    public BreakAlert() {
        super("BreakAlert", "Alerts you when your feet blocks are being broken", 0, Category.COMBAT, true);
        this.mode = this.register(new Value<String>("Mode", this, "Chat", new ArrayList<String>(Arrays.asList("Chat", "Text", "Dot"))));
        this.ignoreSelf = this.register(new Value<Boolean>("Ignore Self", this, true));
        this.watermark = this.register(new Value<Boolean>("Watermark", this, true));
        this.color = this.register(new Value<String>("Color", this, "White", ColorTextUtils.colors));
        this.breaking = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        this.test = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        this.xd = new BlockPos[] { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
    }
    
    @Override
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
        this.test.clear();
        if (BreakAlert.mc.world == null) {
            return;
        }
        RayTraceResult ray;
        RayTraceResult ray2;
        BreakAlert.mc.world.playerEntities.forEach(entityPlayer -> {
            if (this.ignoreSelf.getValue()) {
                if (!entityPlayer.getName().equalsIgnoreCase(BreakAlert.mc.player.getName()) && entityPlayer.isSwingInProgress && entityPlayer.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE) {
                    ray = entityPlayer.rayTrace(5.0, BreakAlert.mc.getRenderPartialTicks());
                    if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && BreakAlert.mc.world.getBlockState(ray.getBlockPos()).getBlock() != Blocks.BEDROCK) {
                        this.test.add((Object)ray.getBlockPos());
                    }
                }
            }
            else if (entityPlayer.isSwingInProgress && entityPlayer.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE) {
                ray2 = entityPlayer.rayTrace(5.0, BreakAlert.mc.getRenderPartialTicks());
                if (ray2 != null && ray2.typeOfHit == RayTraceResult.Type.BLOCK && BreakAlert.mc.world.getBlockState(ray2.getBlockPos()).getBlock() != Blocks.BEDROCK) {
                    this.test.add((Object)ray2.getBlockPos());
                }
            }
            return;
        });
        this.breaking.removeIf(blockPos -> !this.test.contains((Object)blockPos));
        this.breaking.addAll((Collection)this.test);
        this.testy = false;
        for (final BlockPos pos : this.xd) {
            final BlockPos pos2 = new BlockPos(BreakAlert.mc.player.posX, BreakAlert.mc.player.posY, BreakAlert.mc.player.posZ).add(pos.x, pos.y, pos.z);
            if (!this.breaking.isEmpty() && this.breaking.contains((Object)pos2)) {
                this.testy = true;
            }
        }
        if (this.testy && this.mode.getValue().equalsIgnoreCase("Chat")) {
            if (this.delay == 0) {
                if (this.watermark.getValue()) {
                    Command.sendChatMessage(ColorTextUtils.getColor(this.color.getValue()) + "Your feet are being mined!");
                }
                else {
                    Command.sendRawChatMessage(ColorTextUtils.getColor(this.color.getValue()) + "Your feet are being mined!");
                }
            }
            this.delay = 100;
        }
    }
    
    @Override
    public void onRender() {
        if (this.testy && this.mode.getValue().equalsIgnoreCase("dot")) {
            GlStateManager.pushMatrix();
            Gui.drawRect(BreakAlert.mc.displayWidth / 4 - 3, BreakAlert.mc.displayHeight / 4 - 3, BreakAlert.mc.displayWidth / 4 + 4, BreakAlert.mc.displayHeight / 4 + 4, new Color(255, 0, 0, 255).getRGB());
            GlStateManager.popMatrix();
        }
        else if (this.testy && this.mode.getValue().equalsIgnoreCase("Text")) {
            GlStateManager.pushMatrix();
            BreakAlert.mc.fontRenderer.drawStringWithShadow(Command.SECTIONSIGN() + ColorTextUtils.getColor(this.color.getValue()).substring(1) + "Your feet are being mined!", (float)(BreakAlert.mc.displayWidth / 4 - BreakAlert.mc.fontRenderer.getStringWidth("Your feet are being mined!") / 2), (float)(BreakAlert.mc.displayHeight / 4 - BreakAlert.mc.fontRenderer.FONT_HEIGHT / 2), ColorUtils.Colors.RED);
            GlStateManager.popMatrix();
        }
    }
}
