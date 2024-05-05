package io.github.nevalackin.client.module.render.world;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.render.world.BlockRenderEvent;
import io.github.nevalackin.client.event.render.world.BlockSideRenderEvent;
import io.github.nevalackin.client.event.render.world.GetBlockLightLevelEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockOre;

import java.util.Arrays;
import java.util.List;

public final class XRay extends Module {

    private final List<Class<? extends Block>> blocksToFind = Arrays.asList(
        BlockOre.class,
        BlockMobSpawner.class
    );

    private final BooleanProperty xrayBypassProperty = new BooleanProperty("Bypass", true);

    private BlockSideRenderEvent.Callback opaqueCallback;

    private float prevGamma;

    public XRay() {
        super("XRay", Category.RENDER, Category.SubCategory.RENDER_WORLD);
    }

    @EventLink
    private final Listener<GetBlockLightLevelEvent> onGetLightLevel = event -> {
        event.setLightLevel(1);
    };

    @EventLink
    private final Listener<BlockRenderEvent> onBlockRender = event -> {
        if (!this.blocksToFind.contains(event.getBlock().getClass())) {
            event.setCancelled();
        }
    };

    @EventLink
    private final Listener<BlockSideRenderEvent> onSideRender = event -> {
        if (!this.xrayBypassProperty.getValue()) {
            event.setCallback((worldIn, pos, side) -> true);
            return;
        }

        if (this.opaqueCallback == null) {
            this.opaqueCallback = (worldIn, pos, side) ->
                worldIn.isAirBlock(pos.offset(side));
        }

        event.setCallback(this.opaqueCallback);
    };

    @Override
    public void onEnable() {
        this.prevGamma = this.mc.gameSettings.gammaSetting;

        this.mc.gameSettings.gammaSetting = 1000;

        if (this.mc.renderGlobal != null)
            this.mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        this.mc.gameSettings.gammaSetting = this.prevGamma;

        if (this.mc.renderGlobal != null)
            this.mc.renderGlobal.loadRenderers();
    }
}
