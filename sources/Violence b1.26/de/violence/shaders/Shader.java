package de.violence.shaders;

import org.lwjgl.opengl.*;

public class Shader
{
    private int program;
    private long startTime;
    
    public Shader(final String vertexname, final String fragmentname) {
        this.program = 0;
        this.initShader(vertexname, fragmentname);
    }
    
    public void initShader(final String vertexname, final String fragmentname) {
        final int vertexShader = ShaderLoader.loadShader("http://skidclient.de/Violence/Shaders/" + fragmentname, 35633);
        final int fragmentShader = ShaderLoader.loadShader("http://skidclient.de/Violence/Shaders/" + vertexname, 35632);
        if (vertexShader == 0 || fragmentShader == 0) {
            throw new IllegalStateException("Shaderprogram shader setup failed!");
        }
        this.program = ARBShaderObjects.glCreateProgramObjectARB();
        if (this.program == 0) {
            throw new IllegalStateException("Shaderprogram creation failed!");
        }
        ARBShaderObjects.glAttachObjectARB(this.program, vertexShader);
        ARBShaderObjects.glAttachObjectARB(this.program, fragmentShader);
        ARBShaderObjects.glLinkProgramARB(this.program);
        final int linkState = ARBShaderObjects.glGetObjectParameteriARB(this.program, 35714);
        if (linkState == 0) {
            System.err.println(GL20.glGetProgramInfoLog(this.program, 4096));
            throw new IllegalStateException("Shaderprogram linking failed!");
        }
        ARBShaderObjects.glValidateProgramARB(this.program);
        final int validState = ARBShaderObjects.glGetObjectParameteriARB(this.program, 35715);
        if (linkState == 0) {
            System.err.println(GL20.glGetProgramInfoLog(this.program, 4096));
            throw new IllegalStateException("Shaderprogram validation failed!");
        }
        this.startTime = System.currentTimeMillis();
    }
    
    public void drawShader(final int x, final int y, final int x2, final int y2, final int mouseX, final int mouseY) {
        GL11.glClear(16384);
        GL11.glClear(256);
        ARBShaderObjects.glUseProgramObjectARB(this.program);
        final float time = (float)((System.currentTimeMillis() - this.startTime) / 1000.0);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(this.program, (CharSequence)"u214544"), time);
        ARBShaderObjects.glUniform2fARB(ARBShaderObjects.glGetUniformLocationARB(this.program, (CharSequence)"u155664"), (float)(x2 - x), (float)(y2 - y));
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glVertex3f((float)x2, (float)y, 0.0f);
        GL11.glVertex3f((float)x, (float)y, 0.0f);
        GL11.glVertex3f((float)x, (float)y2, 0.0f);
        GL11.glVertex3f((float)x2, (float)y2, 0.0f);
        GL11.glEnd();
        ARBShaderObjects.glUseProgramObjectARB(0);
    }
}
