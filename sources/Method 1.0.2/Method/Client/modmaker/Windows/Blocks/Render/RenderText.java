package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

public class RenderText extends Block {

    public RenderText() {
        super("RenderText", MainBlockType.Default, Tabs.Render, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);


        this.words[0] = "String:";
        this.words[1] = "Color:";
        this.words[2] = "Pos:";
        this.words[3] = "Scale:";


        this.typesAccepted.add(typesCollapse(MainBlockType.String));

        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        this.description = "Renders a text at a position";
    }
    //String s, Setting getcolor, Vec3d pos, double scale

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {

        Vec3d pos = (Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event);
        RenderUtils.rotateGlsetup(pos.x, pos.y, pos.z);
        double scale = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 3, event);
        GlStateManager.scale(-scale, -scale, scale);

        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        int width = Minecraft.getMinecraft().fontRenderer.getStringWidth((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
        bufferbuilder.pos(-(width + (width / 2)) / 3 - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, .25f).endVertex();
        bufferbuilder.pos(-(width + (width / 2)) / 3 - 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, .25f).endVertex();
        bufferbuilder.pos((width + (width / 2)) / 3 + 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, .25f).endVertex();
        bufferbuilder.pos((width + (width / 2)) / 3 + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, .25f).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), -width + (width / 2), (float) 0, (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event));

        RenderUtils.rotateglCleanup();
        super.runCode(dragableBlock, event);
    }


}
