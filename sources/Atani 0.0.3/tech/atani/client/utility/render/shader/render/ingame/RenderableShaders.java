package tech.atani.client.utility.render.shader.render.ingame;

import java.util.Arrays;

import tech.atani.client.feature.module.impl.hud.PostProcessing;
import tech.atani.client.utility.render.shader.access.ShaderAccess;
import tech.atani.client.utility.render.shader.data.ShaderRenderType;
import tech.atani.client.utility.render.shader.render.Type;

public interface RenderableShaders {

    static void render(boolean bloom, boolean blur, Runnable... runnables) {
    	if(bloom && PostProcessing.getInstance().isEnabled() && PostProcessing.getInstance().bloom.getValue()) {
			ShaderAccess.bloomShader.doRender(ShaderRenderType.OVERLAY, Type.QUADS, Arrays.asList(runnables));
    	}	
    	if(blur && PostProcessing.getInstance().isEnabled() && PostProcessing.getInstance().blur.getValue()) {
        	ShaderAccess.blurShader.doRender(ShaderRenderType.OVERLAY, Type.QUADS, Arrays.asList(runnables));
    	}
    }
    
    static void render(Runnable... runnables) {
    	RenderableShaders.render(true, true, runnables);
    }
    
    static void renderAndRun(boolean bloom, boolean blur, Runnable... runnables) {
    	RenderableShaders.render(bloom, blur, runnables);
        Arrays.asList(runnables).forEach(run -> run.run());
    }
    
    static void renderAndRun(Runnable... runnables) {
    	RenderableShaders.render(true, true, runnables);
        Arrays.asList(runnables).forEach(run -> run.run());
    }

}
