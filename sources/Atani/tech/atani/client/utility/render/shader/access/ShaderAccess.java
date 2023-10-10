package tech.atani.client.utility.render.shader.access;


import tech.atani.client.utility.render.shader.shaders.BloomShader;
import tech.atani.client.utility.render.shader.shaders.BlurShader;

public interface ShaderAccess {

    BlurShader blurShader = new BlurShader();

    BloomShader bloomShader = new BloomShader();


}
