package koks.shader;

import koks.api.shader.Shader;
import org.lwjgl.opengl.GL20;

@Shader.Info(vertex = "vertex", fragment = "lsd")
public class LSD extends Shader {
    public void setup() {
        /*{ "name": "ProjMat",      "type": "matrix4x4", "count": 16, "values": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },
        { "name": "InSize",       "type": "float",     "count": 2,  "values": [ 1.0, 1.0 ] },
        { "name": "OutSize",      "type": "float",     "count": 2,  "values": [ 1.0, 1.0 ] },
        { "name": "Time",         "type": "float",     "count": 1,  "values": [ 0.0 ] },
        { "name": "Frequency",    "type": "float",     "count": 2,  "values": [ 512.0, 288.0 ] },
        { "name": "WobbleAmount", "type": "float",     "count": 2,  "values": [ 0.002, 0.002 ] }*/
        GL20.glUniform2f(getUniform("InSize"), 1F,1F);
        GL20.glUniform2f(getUniform("OutSize"), 1F,1F);
        GL20.glUniform1f(getUniform("Time"), 1F);
        GL20.glUniform2f(getUniform("Frequency"), 512F, 288F);
        GL20.glUniform2f(getUniform("WobbleAmount"), 0.002F, 0.002F);
    }
}
