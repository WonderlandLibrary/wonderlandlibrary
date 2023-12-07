package Velo.impl.Modules.visuals;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import Velo.api.Module.Module;
import Velo.api.Util.Render.OutlineUtils;
import Velo.impl.Event.EventRender3D;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

public class ChestESP
extends Module {

	
	
public ChestESP() {
		super("ChestEsp","ChestEsp", 0, Category.VISUALS);
	
	}

@Override
	public void onRender3DUpdate(EventRender3D e) {

    for (Object object : this.mc.theWorld.loadedTileEntityList) {
        if (object instanceof TileEntityChest) {
    	TileEntity tileEntity = (TileEntity)object;
        GL11.glColor4f(0, 0, 0, 1);
        OutlineUtils.renderOne();
        TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, ((EventRender3D) e).getPartialTicks(), -1);
        OutlineUtils.renderTwo();
//        TileEntityRendererDispatcher.instance.func_180546_a(tileEntity, ((EventRender3D) e).getPartialTicks(), -1);
        OutlineUtils.renderThree();
        TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, ((EventRender3D) e).getPartialTicks(), -1);
        OutlineUtils.renderFour();
        TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, ((EventRender3D) e).getPartialTicks(), -1);
        OutlineUtils.renderFive();
        OutlineUtils.setColor(Color.WHITE);
//        RenderUtils.setColor(Rainbow.getRainbow(2, 1, 1, 3));
        }
        }
		super.onRender3DUpdate(e);
	}





}