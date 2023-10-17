package de.tired.util.render.shaderloader.shader.list;

import de.tired.base.interfaces.IHook;
import de.tired.util.render.shaderloader.shader.Shader;
import de.tired.util.render.shaderloader.shader.ShaderProgram;
import org.lwjgl.opengl.GL20;

public class BackGroundShader extends Shader implements IHook {


    public BackGroundShader(int pass) {
        super(new ShaderProgram("fragment/background.glsl"));
        this.pass = pass;
    }


    @Override
    public void render(float x, float y, float width, float height) {
        super.render(x, y, width, height);
    }

    @Override
    public void setUniforms() {
        GL20.glUniform1f(shaderProgram.getUniform("time"), pass / 200f);
        GL20.glUniform2f(shaderProgram.getUniform("resolution"), MC.displayWidth, MC.displayHeight);
    }


}

