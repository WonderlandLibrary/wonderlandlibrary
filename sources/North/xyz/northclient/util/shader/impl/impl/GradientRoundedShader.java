package xyz.northclient.util.shader.impl.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import xyz.northclient.util.shader.RectUtil;
import xyz.northclient.util.shader.impl.Shader;

import java.awt.*;

public class GradientRoundedShader extends Shader {
    @Override
    public String getSource() {
        return "#version 130\n" +
                "\n" +
                "uniform float width;\n" +
                "uniform float height;\n" +
                "uniform vec4 secondColor;\n" +
                "uniform vec2 resolution;\n" +
                "uniform vec4 firstColor;\n" +
                "uniform float dziewica;\n" +
                "\n" +
                "float calc(vec2 p, vec2 b, float r) {\n" +
                "   return length(max(abs(p) - b, 0.0)) - r;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "void main() {\n" +
                "vec2 st = gl_FragCoord.xy / resolution.xy;\n" +
                "vec4 color = vec4(mix(firstColor.xyz,secondColor.xyz,distance(st,vec2(0,1))),1);\n" +
                "vec2 size = vec2(width, height);\n" +
                "vec2 pixel = gl_TexCoord[0].st * size;\n" +
                "vec2 centre = 0.5 * size;\n" +
                "float sa = smoothstep(0.0, 1, calc(centre - pixel, centre - dziewica - 1, dziewica));\n" +
                "vec4 c = mix(vec4(color.rgb, 1), vec4(color.rgb, 0), sa);\n" +
                "gl_FragColor = vec4(c.rgb, color.a * c.a);\n" +
                "}";
    }

    public float radius = 0;
    public Color firstColor;
    public Color secondColor;

    @Override
    public void render(float x, float y, float width, float height) {
        RectUtil.begin();
        this.bind();
        this.uniform1f("width",width);
        this.uniform1f("height",height);
        this.uniform1f("dziewica",radius);
        this.uniform2f("resolution", new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(),new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight());
        this.uniform4f("firstColor", firstColor.getRed()/255.0f,firstColor.getGreen()/255.0f,firstColor.getBlue()/255.0f,firstColor.getAlpha()/255.0f);
        this.uniform4f("secondColor", secondColor.getRed()/255.0f,secondColor.getGreen()/255.0f,secondColor.getBlue()/255.0f,secondColor.getAlpha()/255.0f);
        this.renderQuad(x,y,width,height);
        this.unbind();
    }
}
