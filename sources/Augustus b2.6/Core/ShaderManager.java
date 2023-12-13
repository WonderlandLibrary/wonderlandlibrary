// 
// Decompiled by Procyon v0.5.36
// 

package Core;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import org.lwjgl.BufferUtils;
import java.util.Iterator;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.ARBShaderObjects;
import java.nio.FloatBuffer;
import java.util.HashMap;

public class ShaderManager
{
    String name;
    private int shader_id;
    private int Wloc;
    private int Hloc;
    private HashMap<String, Integer> varLocs;
    private HashMap<String, Float> floatVars;
    private HashMap<String, Integer> intVars;
    private HashMap<String, FloatBuffer> floatArrayVars;
    
    public ShaderManager(final String name) {
        this.shader_id = 0;
        this.varLocs = new HashMap<String, Integer>();
        this.floatVars = new HashMap<String, Float>();
        this.intVars = new HashMap<String, Integer>();
        this.floatArrayVars = new HashMap<String, FloatBuffer>();
        this.name = name;
    }
    
    public int getShaderID() {
        return this.shader_id;
    }
    
    public void load() {
        int vertShader = 0;
        int fragShader = 0;
        try {
            vertShader = this.createShader("shaders/default.vert", 35633);
            fragShader = this.createShader("shaders/" + this.name + ".frag", 35632);
        }
        catch (Exception exc) {
            exc.printStackTrace();
            return;
        }
        finally {
            if (vertShader == 0 || fragShader == 0) {
                return;
            }
        }
        if (vertShader == 0 || fragShader == 0) {
            return;
        }
        this.shader_id = ARBShaderObjects.glCreateProgramObjectARB();
        if (this.shader_id == 0) {
            return;
        }
        ARBShaderObjects.glAttachObjectARB(this.shader_id, vertShader);
        ARBShaderObjects.glAttachObjectARB(this.shader_id, fragShader);
        ARBShaderObjects.glLinkProgramARB(this.shader_id);
        if (ARBShaderObjects.glGetObjectParameteriARB(this.shader_id, 35714) == 0) {
            return;
        }
        ARBShaderObjects.glValidateProgramARB(this.shader_id);
        if (ARBShaderObjects.glGetObjectParameteriARB(this.shader_id, 35715) == 0) {
            return;
        }
        this.Wloc = GL20.glGetUniformLocation(this.getShaderID(), "width");
        this.Hloc = GL20.glGetUniformLocation(this.getShaderID(), "height");
    }
    
    public int getWidthLoc() {
        return this.Wloc;
    }
    
    public int getHeightLoc() {
        return this.Hloc;
    }
    
    public void setUniforms() {
        for (final String key : this.floatVars.keySet()) {
            final float val = this.floatVars.get(key);
            final int loc = this.varLocs.get(key);
            GL20.glUniform1f(loc, val);
        }
        for (final String key : this.intVars.keySet()) {
            final int val2 = this.intVars.get(key);
            final int loc = this.varLocs.get(key);
            GL20.glUniform1i(loc, val2);
        }
        for (final String key : this.floatArrayVars.keySet()) {
            final FloatBuffer arr = this.floatArrayVars.get(key);
            final int loc = this.varLocs.get(key);
            GL20.glUniform1(loc, arr);
        }
    }
    
    public void setFloat(final String name, final float value) {
        if (this.varLocs.containsKey(name)) {
            this.floatVars.put(name, value);
        }
        else {
            final int loc = GL20.glGetUniformLocation(this.getShaderID(), name);
            if (loc == -1) {
                this.VDNEerror(name);
                return;
            }
            this.varLocs.put(name, loc);
            this.floatVars.put(name, value);
        }
    }
    
    public void setInt(final String name, final int value) {
        if (this.varLocs.containsKey(name)) {
            this.intVars.put(name, value);
        }
        else {
            final int loc = GL20.glGetUniformLocation(this.getShaderID(), name);
            if (loc == -1) {
                this.VDNEerror(name);
                return;
            }
            this.varLocs.put(name, loc);
            this.intVars.put(name, value);
        }
    }
    
    public void setFloatArray(final String name, final float[] array) {
        if (this.varLocs.containsKey(name)) {
            final FloatBuffer fb = BufferUtils.createFloatBuffer(array.length);
            fb.put(array);
            fb.flip();
            this.floatArrayVars.put(name, fb);
        }
        else {
            final int loc = GL20.glGetUniformLocation(this.getShaderID(), name);
            if (loc == -1) {
                this.VDNEerror(name);
                return;
            }
            this.varLocs.put(name, loc);
            final FloatBuffer fb2 = BufferUtils.createFloatBuffer(array.length);
            fb2.put(array);
            fb2.flip();
            this.floatArrayVars.put(name, fb2);
        }
    }
    
    private void VDNEerror(final String var) {
        System.err.println("In Shader [" + this.name + "] variable \"" + var + "\" does not exist! (Or is not used in therefore the compiler has optimized it away)");
    }
    
    private int createShader(final String filename, final int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
            if (shader == 0) {
                return 0;
            }
            ARBShaderObjects.glShaderSourceARB(shader, this.readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);
            if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
                throw new RuntimeException("Error creating shader!");
            }
            return shader;
        }
        catch (Exception exc) {
            System.out.println("Shader Compile Log [" + this.name + "]:\n" + ARBShaderObjects.glGetInfoLogARB(shader, 1024));
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw exc;
        }
    }
    
    private String readFileAsString(final String filename) throws Exception {
        final StringBuilder source = new StringBuilder();
        final FileInputStream in = new FileInputStream(filename);
        Exception exception = null;
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Exception innerExc = null;
            Label_0167: {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        source.append(line).append('\n');
                    }
                }
                catch (Exception exc) {
                    exception = exc;
                    try {
                        reader.close();
                    }
                    catch (Exception exc2) {
                        if (innerExc == null) {
                            innerExc = exc2;
                        }
                        else {
                            exc2.printStackTrace();
                        }
                    }
                    break Label_0167;
                }
                finally {
                    try {
                        reader.close();
                    }
                    catch (Exception exc2) {
                        if (innerExc == null) {
                            innerExc = exc2;
                        }
                        else {
                            exc2.printStackTrace();
                        }
                    }
                }
                try {
                    reader.close();
                }
                catch (Exception exc2) {
                    if (innerExc == null) {
                        innerExc = exc2;
                    }
                    else {
                        exc2.printStackTrace();
                    }
                }
            }
            if (innerExc != null) {
                throw innerExc;
            }
        }
        catch (Exception exc3) {
            exception = exc3;
        }
        finally {
            try {
                in.close();
            }
            catch (Exception exc4) {
                if (exception == null) {
                    exception = exc4;
                }
                else {
                    exc4.printStackTrace();
                }
            }
            if (exception != null) {
                throw exception;
            }
        }
        try {
            in.close();
        }
        catch (Exception exc4) {
            if (exception == null) {
                exception = exc4;
            }
            else {
                exc4.printStackTrace();
            }
        }
        if (exception != null) {
            throw exception;
        }
        return source.toString();
    }
}
