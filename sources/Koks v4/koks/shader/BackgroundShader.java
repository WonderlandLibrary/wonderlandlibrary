package koks.shader;

import koks.Koks;
import koks.api.shader.Shader;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL20.*;

@Shader.Info(fragment = "background")
public class BackgroundShader extends Shader {
    public void init() {
        glUniform1f(getUniform("time"), (System.currentTimeMillis() - Koks.getKoks().initTime) / 1000F);
        glUniform2f(getUniform("mouse"), 0,0);
        glUniform2f(getUniform("resolution"), Display.getWidth(), Display.getHeight());
    }
}
