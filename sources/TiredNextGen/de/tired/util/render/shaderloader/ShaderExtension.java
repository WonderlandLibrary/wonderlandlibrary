package de.tired.util.render.shaderloader;

import de.tired.base.interfaces.IHook;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;

@UtilityClass
public class ShaderExtension implements IHook {

    private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(ShaderExtension.class.getName());


    public String readShader(String fileName) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(String.format("assets/minecraft/shaders2/%s", fileName))));
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }

        return stringBuilder.toString();
    }


    public void bindZero() {
        GlStateManager.bindTexture(0);
    }

    public void useShader(int programID) {
        GL20.glUseProgram(programID);
    }

    public void deleteProgram() {
        GL20.glUseProgram(0);
    }

}
