package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Map;

public class RenderItem extends Block {

    public RenderItem() {
        super("RenderItem", MainBlockType.Default, Tabs.Render, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);

        this.words[0] = "pos:";
        this.words[1] = "scale:";
        this.words[2] = "item:";
        this.words[3] = "Offset:";

        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.description = " Renders an item at a position";
    }
    //Vec3d pos, double scale, ItemStack item, Vec3d offset

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Vec3d pos = ((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
        double scale = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
        Vec3d offset = ((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 3, event));
        
        RenderUtils.rotateGlsetup(pos.x, pos.y, pos.z);

        GlStateManager.scale(0.4 * scale, 0.4 * scale, 0);

        GlStateManager.translate(offset.x, offset.y, offset.z);

        Minecraft.getMinecraft().itemRenderer.renderItemSide(Minecraft.getMinecraft().player, (ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event), ItemCameraTransforms.TransformType.NONE, false);
        GlStateManager.enableTexture2D();

        GlStateManager.disableLighting();

        GlStateManager.scale(-0.05F, -0.05F, 0);


        if (((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)).getCount() > 0) {
            int w = Minecraft.getMinecraft().fontRenderer.getStringWidth("x" + ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)).getCount()) / 2;
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("x" + ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)).getCount(), 7 - w, 5, 0xffffff);
        }

        GlStateManager.scale(0.85F, 0.85F, 0.85F);

        int c = 0;
        for (Map.Entry<Enchantment, Integer> m : EnchantmentHelper.getEnchantments(((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event))).entrySet()) {
            int w1 = Minecraft.getMinecraft().fontRenderer.getStringWidth(I18n.format(m.getKey().getName().substring(0, 2)) + m.getValue() / 2);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(
                    I18n.format(m.getKey().getName()).substring(0, 2) + m.getValue(), -4 - w1 + 3, c * 10 - 1,
                    m.getKey() == Enchantments.VANISHING_CURSE || m.getKey() == Enchantments.BINDING_CURSE
                            ? 0xff5050 : 0xffb0e0);
            c--;

        }

        GlStateManager.scale(0.6F, 0.6F, 0.6F);
        String dur = ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)).getMaxDamage() - ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)).getItemDamage() + "";
        int color = MathHelper.hsvToRGB(((float) (((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)).getMaxDamage() - ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)).getItemDamage()) / ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)).getMaxDamage()) / 3.0F, 1.0F, 1.0F);
        if (((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)).isItemStackDamageable())
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(dur, -8 - dur.length() * 3, 15, new Color(color >> 16 & 255, color >> 8 & 255, color & 255).getRGB());


        RenderUtils.rotateglCleanup();
        super.runCode(dragableBlock, event);
    }


}
