package de.violence.shaders;

import org.lwjgl.opengl.*;
import java.net.*;
import java.io.*;

public class ShaderLoader
{
    public static int loadShader(final String fileStream, final int s) {
        final int shader = ARBShaderObjects.glCreateShaderObjectARB(s);
        if (shader == 0) {
            throw new IllegalStateException("Shader creation failed!");
        }
        final String code = loadURL(fileStream);
        ARBShaderObjects.glShaderSourceARB(shader, (CharSequence)code);
        ARBShaderObjects.glCompileShaderARB(shader);
        final int compState = ARBShaderObjects.glGetObjectParameteriARB(shader, 35713);
        if (compState == 0) {
            System.err.println(GL20.glGetShaderInfoLog(shader, 4096));
            throw new IllegalStateException("Shader compilation failed!");
        }
        return shader;
    }
    
    public static String loadURL(final String url) {
        String result = "";
        String line = null;
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            while ((line = br.readLine()) != null) {
                result = String.valueOf(result) + line + "\n";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
