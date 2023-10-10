package today.flux.utility.shader.shaders;

import org.lwjgl.opengl.GL20;

import net.minecraft.client.gui.ScaledResolution;
import today.flux.utility.shader.GLSLShader;
import today.flux.utility.AnimationUtils;

public class ShaderBlob extends GLSLShader {
	private float time;

	public ShaderBlob() {
		super("blob.frag");
	}

	@Override
	public void setupUniforms() {
		setupUniform("resolution");
		setupUniform("time");
	}

	@Override
	public void updateUniforms() {
		final ScaledResolution scaledResolution = new ScaledResolution(mc);

		final int resolutionID = getUniform("resolution");
		if (resolutionID > -1) {
			GL20.glUniform2f(resolutionID, (float) scaledResolution.getScaledWidth() * 2, (float) scaledResolution.getScaledHeight() * 2);
		}
		
		final int timeID = getUniform("time");
		if (timeID > -1) {
			GL20.glUniform1f(timeID, time);
		}
		this.time += 0.001f * AnimationUtils.delta;
	}

}
