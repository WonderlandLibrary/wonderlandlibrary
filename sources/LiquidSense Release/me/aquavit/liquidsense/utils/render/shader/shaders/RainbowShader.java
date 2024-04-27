package me.aquavit.liquidsense.utils.render.shader.shaders;

import me.aquavit.liquidsense.utils.render.shader.Shader;
import org.lwjgl.opengl.GL20;

import java.io.Closeable;

public final class RainbowShader extends Shader implements Closeable {
    private boolean isInUse;
    private float strengthX;
    private float strengthY;
    private float offset;

    public static RainbowShader INSTANCE = new RainbowShader();

    @Override
    public void setupUniforms() {
        this.setupUniform("offset");
        this.setupUniform("strength");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f(this.getUniform("strength"), this.strengthX, this.strengthY);
        GL20.glUniform1f(this.getUniform("offset"), this.offset);
    }

    @Override
    public void startShader() {
        super.startShader();
        this.isInUse = true;
    }

    @Override
    public void stopShader() {
        super.stopShader();
        this.isInUse = false;
    }

    @Override
    public void close() {
        if (this.isInUse) {
            this.stopShader();
        }
    }

    public RainbowShader() {
        super("rainbow_shader.frag");
    }

    public static RainbowShader begin(boolean enable, float x, float y, float offset) {
        RainbowShader instance = INSTANCE;
        if (enable) {
            instance.setStrengthX(x);
            instance.setStrengthY(y);
            instance.setOffset(offset);
            instance.startShader();
        }
        return instance;
    }

    public final boolean isInUse() {
        return this.isInUse;
    }

    public final float getStrengthX() {
        return this.strengthX;
    }

    public final void setStrengthX(float x) {
        this.strengthX = x;
    }

    public final float getStrengthY() {
        return this.strengthY;
    }

    public final void setStrengthY(float y) { this.strengthY = y; }

    public final float getOffset() {
        return this.offset;
    }

    public final void setOffset(float offset) {
        this.offset = offset;
    }
}
