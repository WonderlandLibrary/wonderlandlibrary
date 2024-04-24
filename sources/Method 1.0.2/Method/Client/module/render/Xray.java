package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Patcher.Events.GetAmbientOcclusionLightValueEvent;
import Method.Client.utils.Patcher.Events.RenderBlockModelEvent;
import Method.Client.utils.Patcher.Events.RenderTileEntityEvent;
import Method.Client.utils.Patcher.Events.ShouldSideBeRenderedEvent;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.proxy.Overrides.ForgeBlockModelRendererOverride;
import Method.Client.utils.proxy.Overrides.VisGraphOverride;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;


public class Xray extends Module {
    Setting Gui = setmgr.add(new Setting("Gui", this, Main.Xraygui));

    public Xray() {
        super("Xray", Keyboard.KEY_NONE, Category.RENDER, "Xray");
    }

    @Override
    public void onEnable() {
        mc.renderGlobal.loadRenderers();
        VisGraphOverride.stop = true;
        ForgeBlockModelRendererOverride.runevent = true;
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        mc.gameSettings.gammaSetting = 16;
    }


    @Subscribe
    public void onGetAmbientOcclusionLightValue(GetAmbientOcclusionLightValueEvent event) {
        event.setLightValue(1);
    }


    public void onShouldSideBeRendered(ShouldSideBeRenderedEvent event) {
        if (this.isToggled())
            event.setRendered(isVisible(event.getState().getBlock()));
    }

    @Subscribe
    public void onRenderBlockModel(RenderBlockModelEvent event) {
        if (!isVisible(event.getState().getBlock()))
            event.setCanceled(true);
    }


    // Repeat chest objects//
    @Subscribe
    public void onRenderTileEntity(RenderTileEntityEvent event) {
        if (!isVisible(event.getTileEntity().getBlockType()))
            event.setCanceled(true);
    }

    @Override
    public void onDisable() {
        VisGraphOverride.stop = false;
        ForgeBlockModelRendererOverride.runevent = false;
        mc.renderGlobal.loadRenderers();
    }

    private boolean isVisible(Block block) {
        for (SubGui.SelectedThing selectedThing : Main.Xraygui.listGui.list) {
            if (selectedThing.isSelected)
                if (selectedThing.name.equalsIgnoreCase(getName(block))) return true;
        }

        return false;
    }

    public static String getName(Block block) {
        return "" + Block.REGISTRY.getNameForObject(block);
    }

}
