package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import today.flux.event.PreUpdateEvent;
import today.flux.event.UIRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.PlayerUtils;

import java.awt.*;

public class ObsidianRemover extends Module {
    public ObsidianRemover() {
        super("ObsidianRemover", Category.World, false);
    }

    public boolean notified = false;
    public ItemStack stack = new ItemStack(Blocks.obsidian);

    @EventTarget
    public void onUpdate(PreUpdateEvent e) {
        BlockPos playerHead = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(playerHead).getBlock() instanceof BlockObsidian) {

            if(!notified) {
                PlayerUtils.tellPlayer("Found obsidian block on your head! Removed in Client-Side!");
                PlayerUtils.tellPlayer("The icon will not disappear until you get out!");

                notified = true;
            }

            mc.theWorld.setBlockToAir(playerHead);
        } else {
            notified = false;
        }
    }

    @EventTarget
    public void onRender(UIRenderEvent evt) {
        if(this.notified) {
            ScaledResolution res = new ScaledResolution(mc);
            float width = res.getScaledWidth();
            float height = res.getScaledHeight();
            this.renderItemStack(stack, width / 2 - 8f, height / 2 + 15);
        }
    }

    private void renderItemStack(ItemStack stack, float x, float y) {

        int shit = Math.max(0, Math.min(255, (int) (Math.sin(System.currentTimeMillis() / 150.0) * 255.0 / 2 + 127.5)));
        Color col = new Color(shit, 0, 0, 255);

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        RenderHelper.enableGUIStandardItemLighting();
        this.mc.getRenderItem().zLevel = -150.0F;
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRendererObj, stack, x, y, "!", col.getRGB());
        this.mc.getRenderItem().zLevel = 0;
        GlStateManager.enableCull();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}

