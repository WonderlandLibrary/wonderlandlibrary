package Method.Client.utils.proxy.Overrides;

import Method.Client.Main;
import Method.Client.utils.Patcher.Events.RenderBlockModelEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;
import net.minecraftforge.common.ForgeModContainer;

import java.util.List;

public class ForgeBlockModelRendererOverride extends ForgeBlockModelRenderer {

    public final ThreadLocal<VertexLighterFlat> lighterFlat;
    public final ThreadLocal<VertexLighterSmoothAo> lighterSmooth;
    public final ThreadLocal<VertexBufferConsumer> consumerFlat = ThreadLocal.withInitial(VertexBufferConsumer::new);
    public final ThreadLocal<VertexBufferConsumer> consumerSmooth = ThreadLocal.withInitial(VertexBufferConsumer::new);

    public static void init() {
        Minecraft.getMinecraft().blockRenderDispatcher.blockModelRenderer = new ForgeBlockModelRendererOverride(Minecraft.getMinecraft().getBlockColors());
    }


    public ForgeBlockModelRendererOverride(BlockColors colors) {
        super(colors);
        lighterFlat = ThreadLocal.withInitial(() -> new VertexLighterFlat(colors));
        lighterSmooth = ThreadLocal.withInitial(() -> new VertexLighterSmoothAo(colors));
    }

    public static boolean runevent = false;

    @Override
    public boolean renderModelFlat(IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder buffer, boolean checkSides, long rand) {
        if (runevent) {
            RenderBlockModelEvent post = new RenderBlockModelEvent(state);
            Main.Evtmgr.onRenderBlockModel(post);
            if (post.isCanceled())
                return false;
        }

        if (ForgeModContainer.forgeLightPipelineEnabled) {
            VertexBufferConsumer consumer = consumerFlat.get();
            consumer.setBuffer(buffer);
            consumer.setOffset(pos);

            VertexLighterFlat lighter = lighterFlat.get();
            lighter.setParent(consumer);

            return render(lighter, world, model, state, pos, buffer, checkSides, rand);
        } else {
            return super.renderModelFlat(world, model, state, pos, buffer, checkSides, rand);
        }
    }

    @Override
    public boolean renderModelSmooth(IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder buffer, boolean checkSides, long rand) {
        if (runevent) {
            RenderBlockModelEvent post = new RenderBlockModelEvent(state);
            Main.Evtmgr.onRenderBlockModel(post);
            if (post.isCanceled())
                return false;
        }

        if (ForgeModContainer.forgeLightPipelineEnabled) {
            VertexBufferConsumer consumer = consumerSmooth.get();
            consumer.setBuffer(buffer);
            consumer.setOffset(pos);

            VertexLighterSmoothAo lighter = lighterSmooth.get();
            lighter.setParent(consumer);

            return render(lighter, world, model, state, pos, buffer, checkSides, rand);
        } else {
            return super.renderModelSmooth(world, model, state, pos, buffer, checkSides, rand);
        }
    }

    public static boolean render(VertexLighterFlat lighter, IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, BufferBuilder wr, boolean checkSides, long rand) {
        lighter.setWorld(world);
        lighter.setState(state);
        lighter.setBlockPos(pos);
        boolean empty = true;

        List<BakedQuad> quads = model.getQuads(state, null, rand);
        if (!quads.isEmpty()) {
            lighter.updateBlockInfo();
            empty = false;
            for (BakedQuad quad : quads) {
                quad.pipe(lighter);
            }
        }

        EnumFacing[] enumValues = EnumFacing.values();

        for (EnumFacing side : enumValues) {
            quads = model.getQuads(state, side, rand);
            if (!quads.isEmpty()) {
                if (!checkSides || state.shouldSideBeRendered(world, pos, side)) {
                    if (empty) {
                        lighter.updateBlockInfo();
                        empty = false;
                    }
                    for (BakedQuad quad : quads) {
                        quad.pipe(lighter);
                    }
                }
            }
        }

        lighter.resetBlockInfo();
        return !empty;
    }

}
