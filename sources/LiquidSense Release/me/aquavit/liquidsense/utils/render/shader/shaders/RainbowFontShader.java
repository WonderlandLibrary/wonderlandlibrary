package me.aquavit.liquidsense.utils.render.shader.shaders;

import me.aquavit.liquidsense.utils.render.shader.Shader;
import org.lwjgl.opengl.GL20;

import java.io.Closeable;

public final class RainbowFontShader extends Shader implements Closeable {

    private boolean isInUse;
    private float strengthX;
    private float strengthY;
    private float offset;

    public static final RainbowFontShader INSTANCE = new RainbowFontShader();

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

    public RainbowFontShader() {
        super("rainbow_font_shader.frag");
    }

    public static RainbowFontShader begin(boolean enable, float x, float y, float offset) {
        RainbowFontShader instance = INSTANCE;
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

    public final void setStrengthY(float y) {
        this.strengthY = y;
    }

    public final float getOffset() {
        return this.offset;
    }

    public final void setOffset(float offset) {
        this.offset = offset;
    }

}

