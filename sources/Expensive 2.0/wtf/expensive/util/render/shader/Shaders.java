package wtf.expensive.util.render.shader;

import wtf.expensive.util.render.shader.core.Shader;
import wtf.expensive.util.render.shader.impl.AlphaShader;

import java.util.ArrayList;
import java.util.List;

public class Shaders {

    public List<Shader> shaderList = new ArrayList<>();

    public Shaders() {

        shaderList.addAll(List.of(
           new AlphaShader("")
        ));

    }

}
