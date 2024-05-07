package com.masterof13fps.utils.render;

import com.masterof13fps.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    // Uniforms
    private String[] shadertoy = new String[]{"iTime", "iMouse", "iResolution", "vec3 resolution", "vec4 mouse",
            "mainImage(out vec4 fragColor, in vec2 fragCoord)", "fragCoord", "fragColor"};
    private String[] glsl = new String[]{"time", "mouse", "resolution", "vec2 resolution", "vec2 mouse", "main",
            "gl_FragCoord.xy", "gl_FragColor"};

    private Minecraft mc = Minecraft.mc();

    private long startTime;
    private int program;

    public Shader(String vertex, String fragment) {
        try {
            program = glCreateProgram();
            startTime = System.currentTimeMillis();
            this.initShader(vertex, fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initShader(String vertexname, String fragmentname) {
        // setup shaders
        ResourceLocation url = new ResourceLocation(Client.main().getShaderLoc());
        String[] vfshader = new String[]{url + vertexname, url + fragmentname};

        // Vertex
        int vertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, loadURL(vfshader[0]));
        glCompileShader(vertex);

        // Fragment
        int fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment, loadURL(vfshader[1]));
        glCompileShader(fragment);

        glAttachShader(program, vertex);
        glAttachShader(program, fragment);

        glLinkProgram(program);

        glValidateProgram(program);
    }

    public void drawShader(int x, int y, int width, int height, int mouseX, int mouseY) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glPushMatrix();
        glUseProgram(program);

        float time = ((System.currentTimeMillis() - this.startTime) / 1000F);
        int[] mouse = calculateMousePosition();

        // time
        glUniform1f(glGetUniformLocation(program, glsl[0]), time);
        // mouse
        glUniform2f(glGetUniformLocation(program, glsl[1]), mouse[0], mouse[1]);
        // resolution
        glUniform2f(glGetUniformLocation(program, glsl[2]), width - x, height - y);

        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glColor3f(1, 1, 1);
        glBegin(GL_QUADS);
        glVertex3f(width, y, 0);
        glVertex3f(x, y, 0);
        glVertex3f(x, height, 0);
        glVertex3f(width, height, 0);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);

        glUseProgram(GL_ZERO);
        glPopMatrix();
    }

    private int[] calculateMousePosition() {
        ScaledResolution sr = new ScaledResolution(mc);
        int scaledHeight = sr.height();
        int scaledX = Mouse.getX() * sr.width() / mc.displayWidth;
        int scaledY = scaledHeight - Mouse.getY() * scaledHeight / mc.displayHeight - 1;

        return new int[]{scaledX, scaledY};
    }

    private String loadURL(String url) {
        String text = "";
        try {
            URLConnection conn = new URL(url).openConnection();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String content = reader.lines().collect(Collectors.joining("\n"));
                if (content.contains(shadertoy[0]) && !content.contains("Original ShaderToy ends here")) {
                    // https://www.shadertoy.com/
                    text = content.replace(shadertoy[0], glsl[0]).replace(shadertoy[1], glsl[1])
                            .replace(shadertoy[2], glsl[2]).replace(shadertoy[3], glsl[3])
                            .replace(shadertoy[4], glsl[4]).replace(shadertoy[5], glsl[5])
                            .replace(shadertoy[6], glsl[6]).replace(shadertoy[7], glsl[7]);
                } else {
                    // http://glslsandbox.com/
                    text = content;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

}
