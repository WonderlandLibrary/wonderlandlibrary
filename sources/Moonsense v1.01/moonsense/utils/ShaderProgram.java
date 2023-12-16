// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.OpenGlHelper;
import moonsense.MoonsenseClient;
import org.lwjgl.opengl.GL20;
import net.minecraft.client.Minecraft;

public class ShaderProgram
{
    private static final Minecraft MINECRAFT;
    private int program;
    
    static {
        MINECRAFT = Minecraft.getMinecraft();
    }
    
    public ShaderProgram(final String domain, final String vertShaderFilename, final String fragShaderFilename) {
        try {
            this.init(domain, vertShaderFilename, fragShaderFilename);
            if (this.program > 0) {
                GL20.glUseProgram(this.program);
                GL20.glUniform1i(GL20.glGetUniformLocation(this.program, (CharSequence)"texture"), 0);
                GL20.glUseProgram(0);
            }
        }
        catch (Exception e) {
            MoonsenseClient.error("Could not initialize shader program!", e);
            this.program = 0;
        }
    }
    
    private void init(final String domain, final String vertShaderFilename, final String fragShaderFilename) {
        if (!OpenGlHelper.shadersSupported) {
            this.program = 0;
            return;
        }
        this.program = GL20.glCreateProgram();
        final int vertShader = this.loadAndCompileShader(domain, vertShaderFilename, 35633);
        final int fragShader = this.loadAndCompileShader(domain, fragShaderFilename, 35632);
        if (vertShader != 0) {
            GL20.glAttachShader(this.program, vertShader);
        }
        if (fragShader != 0) {
            GL20.glAttachShader(this.program, fragShader);
        }
        GL20.glLinkProgram(this.program);
        if (GL20.glGetProgrami(this.program, 35714) == 0) {
            MoonsenseClient.error("Could not link shader: {}", GL20.glGetProgramInfoLog(this.program, 1024));
            GL20.glDeleteProgram(this.program);
            this.program = 0;
            return;
        }
        GL20.glValidateProgram(this.program);
        if (GL20.glGetProgrami(this.program, 35715) == 0) {
            MoonsenseClient.error("Could not validate shader: {}", GL20.glGetProgramInfoLog(this.program, 1024));
            GL20.glDeleteProgram(this.program);
            this.program = 0;
        }
    }
    
    private int loadAndCompileShader(final String domain, final String filename, final int shaderType) {
        if (filename == null) {
            return 0;
        }
        final int handle = GL20.glCreateShader(shaderType);
        if (handle == 0) {
            MoonsenseClient.error("Could not create shader of type {} for {}: {}", shaderType, filename, GL20.glGetProgramInfoLog(this.program, 1024));
            return 0;
        }
        final String code = this.loadFile(new ResourceLocation(domain, filename));
        if (code == null) {
            GL20.glDeleteShader(handle);
            return 0;
        }
        GL20.glShaderSource(handle, (CharSequence)code);
        GL20.glCompileShader(handle);
        if (GL20.glGetShaderi(handle, 35713) == 0) {
            MoonsenseClient.error("Could not compile shader {}: {}", filename, GL20.glGetShaderInfoLog(this.program, 1024));
            GL20.glDeleteShader(handle);
            return 0;
        }
        return handle;
    }
    
    private String loadFile(final ResourceLocation resourceLocation) {
        try {
            final StringBuilder code = new StringBuilder();
            final InputStream inputStream = ShaderProgram.MINECRAFT.getResourceManager().getResource(resourceLocation).getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                code.append(line);
                code.append('\n');
            }
            reader.close();
            return code.toString();
        }
        catch (Exception e) {
            MoonsenseClient.error("Could not load shader file!", e);
            return null;
        }
    }
    
    public int getProgram() {
        return this.program;
    }
}
