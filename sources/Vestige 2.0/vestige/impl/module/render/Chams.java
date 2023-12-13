package vestige.impl.module.render;

import org.lwjgl.opengl.GL11;

import vestige.api.event.Listener;
import vestige.api.event.impl.PostRenderPlayerEvent;
import vestige.api.event.impl.RenderPlayerEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;

@ModuleInfo(name = "Chams", category = Category.RENDER)
public class Chams extends Module {

	@Listener
	public void onRenderPlayer(RenderPlayerEvent event) {
		GL11.glEnable(32823);
		GL11.glPolygonOffset(1.0F, -1100000.0F);
	}
	
	@Listener
	public void onPostRenderPlayer(PostRenderPlayerEvent event) {
		GL11.glDisable(32823);
		GL11.glPolygonOffset(1.0F, 1100000.0F);
	}
	
}