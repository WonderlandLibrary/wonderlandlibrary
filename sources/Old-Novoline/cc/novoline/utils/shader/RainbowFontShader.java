package cc.novoline.utils.shader;


import org.lwjgl.opengl.GL20;
import java.io.Closeable;

public class RainbowFontShader extends Shader implements Closeable {
	public RainbowFontShader() {
		super("rainbow_font_shader.frag");
	}

	private boolean inUse;
	private float strengthX;
	private float strengthY;
	private float offset;

	public static RainbowFontShader INSTANCE = new RainbowFontShader();

	public static RainbowFontShader begin(boolean enable, float x, float y, float offset) {
		RainbowFontShader instance = INSTANCE;

		if(enable) {
			instance.strengthX = x;
			instance.strengthY = y;
			instance.offset = offset;
			instance.startShader();
		}

		return instance;
	}

	@Override
	public void setupUniforms() {
		setupUniform("offset");
		setupUniform("strength");
	}

	@Override
	public void updateUniforms() {
		GL20.glUniform2f(getUniform("strength"), strengthX, strengthY);
		GL20.glUniform1f(getUniform("offset"), offset);
	}

	@Override
	public void startShader() {
		super.startShader();
		inUse = true;
	}

	@Override
	public void stopShader() {
		super.stopShader();
		inUse = false;
	}

	@Override
	public void close() {
		if(inUse) {
			stopShader();
		}
	}
}