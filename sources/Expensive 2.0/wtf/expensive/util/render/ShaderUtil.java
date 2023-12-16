package wtf.expensive.util.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import wtf.expensive.util.IMinecraft;
import wtf.expensive.util.misc.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author Jefferson
 * @since 30/11/2022
 */

public class ShaderUtil implements IMinecraft {
    private final int programID;
    // Шейдеры, используемые в приложении
    public static ShaderUtil
            CORNER_ROUND_SHADER,
            CORNER_ROUND_SHADER_TEXTURE,
            GLOW_ROUND_SHADER,
            GRADIENT_ROUND_SHADER,
            TEXTURE_ROUND_SHADER,
            ROUND_SHADER_OUTLINE,
            GRADIENT_MASK_SHADER,

            ROUND_SHADER;

    public ShaderUtil(String fragmentShaderLoc) {
        programID = ARBShaderObjects.glCreateProgramObjectARB();

        try {
            // Загрузка фрагментного шейдера
            int fragmentShaderID = switch (fragmentShaderLoc) {
                case "corner" ->
                        createShader(new ByteArrayInputStream(roundedCornerRect.getBytes()), GL_FRAGMENT_SHADER);
                case "cornerGradient" ->
                        createShader(new ByteArrayInputStream(roundedCornerRectGradient.getBytes()), GL_FRAGMENT_SHADER);
                case "noise" -> createShader(new ByteArrayInputStream("""
                        uniform sampler2D u_texture;
                        uniform float u_value;
                        #define NOISE .5/255.0
                                                
                        float random(vec2 st) {
                            return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453);
                        }
                                                
                        void main() {
                            vec2 st = gl_TexCoord[0].st;
                         
                            // Получение цвета из входной текстуры
                            vec4 color = texture2D(u_texture, st);
                           
                            float noise = (sin(st.x) * cos(st.y)) * random(st);
                           
                            // Применение шума
                            color.rgb -= vec3(noise / u_value);
                            // Отрисовка на выход
                            gl_FragColor = color;
                        }
                        """.getBytes()), GL_FRAGMENT_SHADER);
                case "blur2" ->
                        createShader(new ByteArrayInputStream("""
                                #version 120
                                 
                                 uniform sampler2D textureIn, textureToCheck;
                                 uniform vec2 texelSize, direction;
                                 uniform float exposure, radius;
                                 uniform float weights[256];
                                 uniform bool avoidTexture;
                                 
                                 #define offset direction * texelSize
                                 
                                 void main() {
                                     if (direction.y >= 1 && avoidTexture) {
                                         if (texture2D(textureToCheck, gl_TexCoord[0].st).a != 0.0) discard;
                                     }
                                     vec4 innerAlpha = texture2D(textureIn, gl_TexCoord[0].st);
                                     innerAlpha *= innerAlpha.a;
                                     innerAlpha *= weights[0];
                                 
                                 
                                 
                                     for (float r = 1.0; r <= radius; r ++) {
                                         vec4 colorCurrent1 = texture2D(textureIn, gl_TexCoord[0].st + offset * r);
                                         vec4 colorCurrent2 = texture2D(textureIn, gl_TexCoord[0].st - offset * r);
                                 
                                         colorCurrent1.rgb *= colorCurrent1.a;
                                         colorCurrent2.rgb *= colorCurrent2.a;
                                 
                                         innerAlpha += (colorCurrent1 + colorCurrent2) * weights[int(r)];
                                 
                                     }
                                 
                                     gl_FragColor = vec4(innerAlpha.rgb / innerAlpha.a, mix(innerAlpha.a, 1.0 - exp(-innerAlpha.a * exposure), step(0.0, direction.y)));
                                 }
                                """.getBytes()), GL_FRAGMENT_SHADER);

                case "blur2c" ->
                        createShader(new ByteArrayInputStream("""
                                #version 120
                                 
                                 uniform vec4 color;
                                 uniform sampler2D textureIn, textureToCheck;
                                 uniform vec2 texelSize, direction;
                                 uniform float exposure, radius;
                                 uniform float weights[256];
                                 uniform bool avoidTexture;
                                 
                                 #define offset direction * texelSize
                                 
                                 void main() {
                                     if (direction.y >= 1 && avoidTexture) {
                                         if (texture2D(textureToCheck, gl_TexCoord[0].st).a != 0.0) discard;
                                     }
                                     vec4 innerAlpha = texture2D(textureIn, gl_TexCoord[0].st);
                                     innerAlpha *= innerAlpha.a;
                                     innerAlpha *= weights[0];
                                 
                                 
                                 
                                     for (float r = 1.0; r <= radius; r ++) {
                                         vec4 colorCurrent1 = texture2D(textureIn, gl_TexCoord[0].st + offset * r);
                                         vec4 colorCurrent2 = texture2D(textureIn, gl_TexCoord[0].st - offset * r);
                                 
                                         colorCurrent1.rgb *= colorCurrent1.a;
                                         colorCurrent2.rgb *= colorCurrent2.a;
                                 
                                         innerAlpha += (colorCurrent1 + colorCurrent2) * weights[int(r)];
                                 
                                     }
                                 
                                     gl_FragColor = vec4(color.rgb, mix(innerAlpha.a, 1.0 - exp(-innerAlpha.a * exposure), step(0.0, direction.y)));
                                 }
                                """.getBytes()), GL_FRAGMENT_SHADER);
                case "cornerTex" ->
                        createShader(new ByteArrayInputStream(roundedCornerRectTexture.getBytes()), GL_FRAGMENT_SHADER);
                case "round" -> createShader(new ByteArrayInputStream(roundedRect.getBytes()), GL_FRAGMENT_SHADER);
                case "fog" -> createShader(new ByteArrayInputStream(fog.getBytes()), GL_FRAGMENT_SHADER);
                case "glow" -> createShader(new ByteArrayInputStream(glowRect.getBytes()), GL_FRAGMENT_SHADER);
                case "out" -> createShader(new ByteArrayInputStream(roundedOut.getBytes()), GL_FRAGMENT_SHADER);
                case "up" -> createShader(new ByteArrayInputStream(kawaseUpBloom.getBytes()), GL_FRAGMENT_SHADER);
                case "down" -> createShader(new ByteArrayInputStream(kawaseDownBloom.getBytes()), GL_FRAGMENT_SHADER);
                case "bloom" -> createShader(new ByteArrayInputStream(bloom.getBytes()), GL_FRAGMENT_SHADER);
                case "shadow" -> createShader(new ByteArrayInputStream("""
                        #version 120
                                                
                        uniform sampler2D sampler1;
                        uniform sampler2D sampler2;
                        uniform vec2 texelSize;
                        uniform vec2 direction;
                        uniform float radius;
                        uniform float kernel[256];
                                                
                        void main(void)
                        {
                            vec2 uv = gl_TexCoord[0].st;                      
                            vec4 pixel_color = texture2D(sampler1, uv);
                            pixel_color.rgb *= pixel_color.a;
                            pixel_color *= kernel[0];
                                                
                            for (float f = 1; f <= radius; f++) {
                                vec2 offset = f * texelSize * direction;
                                vec4 left = texture2D(sampler1, uv - offset);
                                vec4 right = texture2D(sampler1, uv + offset);
                                left.rgb *= left.a;
                                right.rgb *= right.a;
                                pixel_color += (left + right) * kernel[int(f)];
                            }
                                                
                            gl_FragColor = vec4(pixel_color.rgb / pixel_color.a, pixel_color.a);
                        }
                        """.getBytes()), GL_FRAGMENT_SHADER);
                //case "shadow" -> createShader(new ByteArrayInputStream(glow.getBytes()), GL_FRAGMENT_SHADER);
                case "texture" -> createShader(new ByteArrayInputStream(texture.getBytes()), GL_FRAGMENT_SHADER);
                case "outline" -> createShader(new ByteArrayInputStream(outline.getBytes()), GL_FRAGMENT_SHADER);
                case "outlineC" -> createShader(new ByteArrayInputStream(colorOut.getBytes()), GL_FRAGMENT_SHADER);
                case "gradientMask" ->
                        createShader(new ByteArrayInputStream(gradientMask.getBytes()), GL_FRAGMENT_SHADER);

                case "blur" ->
                        createShader(new ByteArrayInputStream("""
                                #version 120
                                                                
                                uniform sampler2D textureIn;
                                uniform sampler2D textureOut;
                                uniform vec2 texelSize, direction;
                                uniform float radius, weights[256];
                                                                
                                #define offset texelSize * direction
                                                                
                                void main() {
                                    vec2 uv = gl_TexCoord[0].st;
                                    uv.y = 1.0f - uv.y;
                                    
                                    float alpha = texture2D(textureOut, uv).a;
                                    if (direction.x == 0.0 && alpha == 0.0) {
                                        discard;
                                    }
                                
                                    vec3 color = texture2D(textureIn, gl_TexCoord[0].st).rgb * weights[0];
                                    float totalWeight = weights[0];
                                                                
                                    for (float f = 1.0; f <= radius; f++) {
                                        color += texture2D(textureIn, gl_TexCoord[0].st + f * offset).rgb * (weights[int(abs(f))]);
                                        color += texture2D(textureIn, gl_TexCoord[0].st - f * offset).rgb * (weights[int(abs(f))]);
                                                                
                                        totalWeight += (weights[int(abs(f))]) * 2.0;
                                    }
                                                                
                                    gl_FragColor = vec4(color / totalWeight, 1.0);
                                }
                                """.getBytes()), GL_FRAGMENT_SHADER);

                case "alphaMask" -> createShader(new ByteArrayInputStream(alphaMask.getBytes()), GL_FRAGMENT_SHADER);
                case "roundRectOutline" ->
                        createShader(new ByteArrayInputStream(roundRectOutline.getBytes()), GL_FRAGMENT_SHADER);
                case "gradientRoundRect" ->
                        createShader(new ByteArrayInputStream(gradientRoundRect.getBytes()), GL_FRAGMENT_SHADER);
                default ->
                        createShader(mc.getResourceManager().getResource(new ResourceLocation("expensive/shader/" + fragmentShaderLoc)).getInputStream(), GL_FRAGMENT_SHADER);
            };
            ARBShaderObjects.glAttachObjectARB(programID, fragmentShaderID);

            // Загрузка вершинного шейдера
            ARBShaderObjects.glAttachObjectARB(programID, createShader(new ByteArrayInputStream(vertex.getBytes()), GL_VERTEX_SHADER));

            // Связывание шейдеров
            ARBShaderObjects.glLinkProgramARB(programID);
        } catch (IOException exception) {
            exception.fillInStackTrace();
            System.out.println("Ошибка при загрузке: " + fragmentShaderLoc);
        }
    }


    /**
     * Инициализация шейдеров при запуске клиента
     */
    public static void init() {
        CORNER_ROUND_SHADER = new ShaderUtil("corner");
        CORNER_ROUND_SHADER_TEXTURE= new ShaderUtil("cornerTex");
        GLOW_ROUND_SHADER = new ShaderUtil("glow");
        TEXTURE_ROUND_SHADER = new ShaderUtil("texture");
        ROUND_SHADER = new ShaderUtil("round");
        GRADIENT_ROUND_SHADER = new ShaderUtil("gradientRoundRect");
        ROUND_SHADER_OUTLINE = new ShaderUtil("roundRectOutline");
        GRADIENT_MASK_SHADER = new ShaderUtil("gradientMask");
    }

    public int getUniform(String name) {
        return ARBShaderObjects.glGetUniformLocationARB(programID, name);
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

    private static final HashMap<Integer, FloatBuffer> kernelCache = new HashMap<>();


    public void setUniformfb(String name, FloatBuffer buffer) {
        ARBShaderObjects.glUniform1fvARB(GL30.glGetUniformLocation(programID, name), buffer);
    }

    /**
     * Подключение шейдера к контексту OpenGL
     */
    public void attach() {
        ARBShaderObjects.glUseProgramObjectARB(programID);
    }

    /**
     * Отключение шейдера от контекста OpenGL
     */
    public void detach() {
        glUseProgram(0);
    }

    /**
     * Установка значения uniform переменной
     *
     * @param name
     * @param args
     */
    public void setUniform(String name, float... args) {
        int loc = ARBShaderObjects.glGetUniformLocationARB(programID, name);
        switch (args.length) {
            case 1 -> ARBShaderObjects.glUniform1fARB(loc, args[0]);
            case 2 -> ARBShaderObjects.glUniform2fARB(loc, args[0], args[1]);
            case 3 -> ARBShaderObjects.glUniform3fARB(loc, args[0], args[1], args[2]);
            case 4 -> ARBShaderObjects.glUniform4fARB(loc, args[0], args[1], args[2], args[3]);
            default ->
                    throw new IllegalArgumentException("Недопустимое количество аргументов для uniform '" + name + "'");
        }
    }

    public void setUniform(String name, int... args) {
        int loc = ARBShaderObjects.glGetUniformLocationARB(programID, name);
        switch (args.length) {
            case 1 -> glUniform1iARB(loc, args[0]);
            case 2 -> glUniform2iARB(loc, args[0], args[1]);
            case 3 -> glUniform3iARB(loc, args[0], args[1], args[2]);
            case 4 -> glUniform4iARB(loc, args[0], args[1], args[2], args[3]);
            default ->
                    throw new IllegalArgumentException("Недопустимое количество аргументов для uniform '" + name + "'");
        }
    }

    public void setUniformf(String var1, float... var2) {
        int var3 = ARBShaderObjects.glGetUniformLocationARB(this.programID, var1);
        switch (var2.length) {
            case 1 -> ARBShaderObjects.glUniform1fARB(var3, var2[0]);
            case 2 -> ARBShaderObjects.glUniform2fARB(var3, var2[0], var2[1]);
            case 3 -> ARBShaderObjects.glUniform3fARB(var3, var2[0], var2[1], var2[2]);
            case 4 -> ARBShaderObjects.glUniform4fARB(var3, var2[0], var2[1], var2[2], var2[3]);
        }
    }

    public static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, ShaderUtil roundedTexturedShader) {

        roundedTexturedShader.setUniform("location", (float) (x * 2),
                (float) ((sr.getHeight() - (height * 2)) - (y * 2)));
        roundedTexturedShader.setUniform("rectSize", (float) (width * 2), (float) (height * 2));
        roundedTexturedShader.setUniform("radius", (float) (radius * 2));
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != mc.getMainWindow().getWidth() || framebuffer.framebufferHeight != mc.getMainWindow().getHeight()) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(Math.max(mc.getMainWindow().getWidth(),1),Math.max(mc.getMainWindow().getHeight(),1), true, false);
        }
        return framebuffer;
    }

    public static void update(Framebuffer framebuffer) {
        if (framebuffer.framebufferWidth != mc.getMainWindow().getWidth() || framebuffer.framebufferHeight != mc.getMainWindow().getHeight()) {
            framebuffer.createBuffers(mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight(),false);
        }
    }

    /**
     * Отрисовка квадрата
     */
    public void drawQuads(final float x,
                          final float y,
                          final float width,
                          final float height) {

        RenderUtil.Render2D.quadsBegin(x, y, width, height, GL_QUADS);
    }

    public static void drawQuads() {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, Math.max(sr.getScaledHeight(),1));
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(Math.max(sr.getScaledWidth(), 1), Math.max(sr.getScaledHeight(),1));
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(Math.max(sr.getScaledWidth(),1), 0);
        GL11.glEnd();
    }

    private int createShader(InputStream inputStream, int shaderType) {
        int shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
        ARBShaderObjects.glShaderSourceARB(shader, FileUtil.readInputStream(inputStream));
        ARBShaderObjects.glCompileShaderARB(shader);
        if (GL20.glGetShaderi(shader, 35713) == 0) {
            System.out.println(GL20.glGetShaderInfoLog(shader, 4096));
            throw new IllegalStateException(String.format("Shader (%s) failed to compile!", shaderType));
        }
        return shader;
    }


    private final String roundedOut = """
            #version 120
                     
            // объявление переменных
            uniform vec2 size; // размер прямоугольника
            uniform vec4 round; // коэффициенты скругления углов
            uniform vec2 smoothness; // плавность перехода от цвета к прозрачности
            uniform float value; // значение, используемое для расчета расстояния до границы
            uniform vec4 color; // цвет прямоугольника
            uniform float outlineSize; // размер обводки
            uniform vec4 outlineColor; // цвет обводки
                     
            // функция для расчета расстояния до границы
            float test(vec2 vec_1, vec2 vec_2, vec4 vec_4) {
                vec_4.xy = (vec_1.x > 0.0) ? vec_4.xy : vec_4.zw;
                vec_4.x = (vec_1.y > 0.0) ? vec_4.x : vec_4.y;
                vec2 coords = abs(vec_1) - vec_2 + vec_4.x;
                return min(max(coords.x, coords.y), 0.0) + length(max(coords, vec2(0.0f))) - vec_4.x;
            }
                     
            void main() {
                vec2 st = gl_TexCoord[0].st * size; // координаты текущего пикселя
                vec2 halfSize = 0.5 * size; // половина размера прямоугольника
                float sa = 1.0 - smoothstep(smoothness.x, smoothness.y, test(halfSize - st, halfSize - value, round));
                // рассчитываем прозрачность в зависимости от расстояния до границы
                gl_FragColor = mix(vec4(color.rgb, 0.0), vec4(color.rgb, color.a), sa); // устанавливаем цвет прямоугольника с прозрачностью sa
              \s
                // добавляем обводку
                vec2 outlineSizeVec = size + vec2(outlineSize);
                float outlineDist = test(halfSize - st, halfSize - value, round);
                float outline = smoothstep(smoothness.x, smoothness.y, outlineDist) - smoothstep(smoothness.x, smoothness.y, outlineDist - outlineSize);
                if (outlineDist < outlineSize)
                    gl_FragColor = mix(gl_FragColor, outlineColor, outline);
            }
            """;


    private final String fog = """
            #version 120
                        
            uniform float BLUR_AMOUNT = 1.6;
            uniform sampler2D depthtex0;
            uniform float near;
            uniform float far;
            uniform sampler2D textureIn;
            uniform vec2 texelSize, direction;
            uniform float startRadius;
            uniform float endRadius;
            uniform float depthStart;
            uniform float weights[256];
                        
                        
            #define offset texelSize * direction
            #define clipping far
            #define NOISE .5/255.0
                        
                        
            float getDepth(vec2 coord) {
                return 2.0 * near * far / (far + near - (2.0 * texture2D(depthtex0, coord).x - 1.0) * (far - near)) / clipping;
            }
                        
            void main() {
                float depth = getDepth(gl_TexCoord[0].st);
                vec4 finalColor;
                if (depth >= depthStart) {
                    float totalWeight = weights[0];
                    vec3 blr = texture2D(textureIn, gl_TexCoord[0].st).rgb * weights[0];
                        
                    for (float f = 0f; f <= mix(startRadius, endRadius, depth - depthStart); f++) {
                        blr += texture2D(textureIn, gl_TexCoord[0].st + f * offset).rgb * (weights[int(abs(f))]);
                        blr += texture2D(textureIn, gl_TexCoord[0].st - f * offset).rgb * (weights[int(abs(f))]);
                        totalWeight += (weights[int(abs(f))]) * 2.0;
                    }
                        
                    finalColor = vec4(blr / totalWeight, 1);
                        
                }
                else {
                    finalColor = vec4(texture2D(textureIn, gl_TexCoord[0].st).rgb, 1.0);
                }
                        
                gl_FragColor = finalColor;
            }
            """;

    private final String bloom = "#version 120\n" +
            "\n" +
            "uniform sampler2D textureIn, textureToCheck;\n" +
            "uniform vec2 texelSize, direction;\n" +
            "uniform float exposure, radius;\n" +
            "uniform float weights[64];\n" +
            "uniform bool avoidTexture;\n" +
            "\n" +
            "#define offset direction * texelSize\n" +
            "\n" +
            "void main() {\n" +
            "    if (direction.y == 1 && avoidTexture) {\n" +
            "        if (texture2D(textureToCheck, gl_TexCoord[0].st).a != 0.0) discard;\n" +
            "    }\n" +
            "    vec4 innerAlpha = texture2D(textureIn, gl_TexCoord[0].st);\n" +
            "    innerAlpha *= innerAlpha.a;\n" +
            "    innerAlpha *= weights[0];\n" +
            "\n" +
            "\n" +
            "\n" +
            "    for (float r = 1.0; r <= radius; r ++) {\n" +
            "        vec4 colorCurrent1 = texture2D(textureIn, gl_TexCoord[0].st + offset * r);\n" +
            "        vec4 colorCurrent2 = texture2D(textureIn, gl_TexCoord[0].st - offset * r);\n" +
            "\n" +
            "        colorCurrent1.rgb *= colorCurrent1.a;\n" +
            "        colorCurrent2.rgb *= colorCurrent2.a;\n" +
            "\n" +
            "        innerAlpha += (colorCurrent1 + colorCurrent2) * weights[int(r)];\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    gl_FragColor = vec4(innerAlpha.rgb / innerAlpha.a, mix(innerAlpha.a, 1.0 - exp(-innerAlpha.a * exposure), step(0.0, direction.y)));\n" +
            "}";

    String glowRect = """
            uniform vec2 rectSize; // размеры прямоугольника
            uniform vec4 color1, color2, color3, color4; // четыре цвета для градиента
            uniform float radius, soft; // радиус и сглаживание круга

            /* Функция, вычисляющая расстояние от точки до круга с заданным радиусом и центром */
            float roundSDF(vec2 p, vec2 b, float r) {
                return length(max(abs(p) - b , 0.0)) - r;
            }

            /* Функция, создающая градиент по заданным цветам */
            vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){
                vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
                return color;
            }

            void main() {
                vec2 st = gl_TexCoord[0].st; // координаты текущего пикселя на текстуре
                vec2 halfSize = rectSize * .5; // половина размеров прямоугольника
                float dist = roundSDF(halfSize - (gl_TexCoord[0].st * rectSize), halfSize - (soft + (radius * 0.75)), radius); // расстояние от пикселя до круга
                float smoothedAlpha = (1. - smoothstep(-soft, soft, dist)) * color1.a; // сглаженная альфа-составляющая цвета
                gl_FragColor = mix(vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), 0.0), vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), smoothedAlpha), smoothedAlpha); // смешивание цвета градиента с прозрачностью и установка конечного цвета пикселя
            }""";

    private final String roundRectOutline = """
            #version 120
                       \s
            uniform vec2 location, rectSize;
            uniform vec4 color, outlineColor1,outlineColor2,outlineColor3,outlineColor4;
            uniform float radius, outlineThickness;
            #define NOISE .5/255.0
                     
            float roundedSDF(vec2 centerPos, vec2 size, float radius) {
                return length(max(abs(centerPos) - size + radius, 0.0)) - radius;
            }
                     
            vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4)
            {
                vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
                //Dithering the color
                // from https://shader-tutorial.dev/advanced/color-banding-dithering/
                color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));
                return color;
            }
                     
            void main() {
                float distance = roundedSDF(gl_FragCoord.xy - location - (rectSize * .5), (rectSize * .5) + (outlineThickness * 0.5) - 1.0, radius);
                     
                float blendAmount = smoothstep(0., 2., abs(distance) - (outlineThickness * 0.5));
                vec4 outlineColor = vec4(createGradient(gl_TexCoord[0].st, outlineColor1.rgb, outlineColor2.rgb, outlineColor3.rgb, outlineColor4.rgb), outlineColor1.a);
                vec4 insideColor = (distance < 0.) ? color : vec4(outlineColor.rgb,  0.0);
                gl_FragColor = mix(outlineColor, insideColor, blendAmount);
            }
            """;
    String gradientRoundRect = "#version 120\n" +
            "\n" +
            "uniform vec2 location, rectSize;\n" +
            "uniform vec4 color1, color2, color3, color4;\n" +
            "uniform float radius;\n" +
            "\n" +
            "#define NOISE .5/255.0\n" +
            "\n" +
            "float roundSDF(vec2 p, vec2 b, float r) {\n" +
            "    return length(max(abs(p) - b , 0.0)) - r;\n" +
            "}\n" +
            "\n" +
            "vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){\n" +
            "    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);\n" +
            "    //Dithering the color\n" +
            "    // from https://shader-tutorial.dev/advanced/color-banding-dithering/\n" +
            "    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));\n" +
            "    return color;\n" +
            "}\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 st = gl_TexCoord[0].st;\n" +
            "    vec2 halfSize = rectSize * .5;\n" +
            "    \n" +
            "    float smoothedAlpha =  (1.0-smoothstep(0.0, 2., roundSDF(halfSize - (gl_TexCoord[0].st * rectSize), halfSize - radius - 1., radius))) * color1.a;\n" +
            "    gl_FragColor = vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), smoothedAlpha);\n" +
            "}";
    String roundedRect = "#version 120\n" +
            "\n" +
            "uniform vec2 location, rectSize;\n" +
            "uniform vec4 color;\n" +
            "uniform float radius;\n" +
            "uniform bool blur;\n" +
            "\n" +
            "float roundSDF(vec2 p, vec2 b, float r) {\n" +
            "    return length(max(abs(p) - b, 0.0)) - r;\n" +
            "}\n" +
            "\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 rectHalf = rectSize * 0.5;\n" +
            "    // Smooth the result (free antialiasing).\n" +
            "    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * rectSize), rectHalf - radius - 1., radius))) * color.a;\n" +
            "    gl_FragColor = vec4(color.rgb, smoothedAlpha);// mix(quadColor, shadowColor, 0.0);\n" +
            "\n" +
            "}";
    String vertex = """
            #version 120      \s
            void main() {
                // Выборка данных из текстуры во фрагментном шейдере (координаты)
                gl_TexCoord[0] = gl_MultiTexCoord0;
                gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
            }
            """;


    String texture = """
            uniform vec2 rectSize; // Координаты и размер прямоугольника
            uniform sampler2D textureIn; // Входная текстура
            uniform float radius, alpha; // Радиус закругления углов прямоугольника и прозрачность

            // Создаем функцию для определения расстояния от текущей позиции до края прямоугольника
            float roundedSDF(vec2 centerPos, vec2 size, float radius) {
                return length(max(abs(centerPos) - size, 0.)) - radius;
            }

            void main() {
                // Определяем расстояние от текущей позиции до края прямоугольника
                float distance = roundedSDF((rectSize * .5) - (gl_TexCoord[0].st * rectSize), (rectSize * .5) - radius - 1., radius);
               \s
                // Создаем плавный переход от границы прямоугольника к прозрачной области
                float smoothedAlpha = (1.0 - smoothstep(0.0, 2.0, distance)) * alpha;

                // Создаем окончательный цвет пикселя, используя цвет из входной текстуры и плавный переход между границей прямоугольника и прозрачной областью
                gl_FragColor = vec4(texture2D(textureIn, gl_TexCoord[0].st).rgb, smoothedAlpha);
            }
            """;
    String roundedCornerRect = """
            #version 120
                // объявление переменных
                uniform vec2 size; // размер прямоугольника
                uniform vec4 round; // коэффициенты скругления углов
                uniform vec2 smoothness; // плавность перехода от цвета к прозрачности
                uniform float value; // значение, используемое для расчета расстояния до границы
                uniform vec4 color; // цвет прямоугольника

                // функция для расчета расстояния до границы
                float test(vec2 vec_1, vec2 vec_2, vec4 vec_4) {
                    vec_4.xy = (vec_1.x > 0.0) ? vec_4.xy : vec_4.zw;
                    vec_4.x = (vec_1.y > 0.0) ? vec_4.x : vec_4.y;
                    vec2 coords = abs(vec_1) - vec_2 + vec_4.x;
                    return min(max(coords.x, coords.y), 0.0) + length(max(coords, vec2(0.0f))) - vec_4.x;
                }
                

                void main() {
                    vec2 st = gl_TexCoord[0].st * size; // координаты текущего пикселя
                    vec2 halfSize = 0.5 * size; // половина размера прямоугольника
                    float sa = 1.0 - smoothstep(smoothness.x, smoothness.y, test(halfSize - st, halfSize - value, round));
                    // рассчитываем прозрачность в зависимости от расстояния до границы
                    gl_FragColor = mix(vec4(color.rgb, 0.0), vec4(color.rgb, color.a), sa); // устанавливаем цвет прямоугольника с прозрачностью sa
                }""";

    String roundedCornerRectGradient = """
            #version 120
                // объявление переменных
                uniform vec2 size; // размер прямоугольника
                uniform vec4 round; // коэффициенты скругления углов
                uniform vec2 smoothness; // плавность перехода от цвета к прозрачности
                uniform float value; // значение, используемое для расчета расстояния до границы
                uniform vec4 color1; // цвет прямоугольника
                uniform vec4 color2; // цвет прямоугольника
                uniform vec4 color3; // цвет прямоугольника
                uniform vec4 color4; // цвет прямоугольника
                #define NOISE .5/255.0
                // функция для расчета расстояния до границы
                float test(vec2 vec_1, vec2 vec_2, vec4 vec_4) {
                    vec_4.xy = (vec_1.x > 0.0) ? vec_4.xy : vec_4.zw;
                    vec_4.x = (vec_1.y > 0.0) ? vec_4.x : vec_4.y;
                    vec2 coords = abs(vec_1) - vec_2 + vec_4.x;
                    return min(max(coords.x, coords.y), 0.0) + length(max(coords, vec2(0.0f))) - vec_4.x;
                }
                
                vec4 createGradient(vec2 coords, vec4 color1, vec4 color2, vec4 color3, vec4 color4)
            {
                vec4 color = mix(mix(color1, color2, coords.y), mix(color3, color4, coords.y), coords.x);
                //Dithering the color
                // from https://shader-tutorial.dev/advanced/color-banding-dithering/
                color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));
                return color;
            }

                void main() {
                    vec2 st = gl_TexCoord[0].st * size; // координаты текущего пикселя
                    vec2 halfSize = 0.5 * size; // половина размера прямоугольника
                    float sa = 1.0 - smoothstep(smoothness.x, smoothness.y, test(halfSize - st, halfSize - value, round));
                    // рассчитываем прозрачность в зависимости от расстояния до границы
                    vec4 color = createGradient(gl_TexCoord[0].st, color1, color2,color3,color4);
                    gl_FragColor = mix(vec4(color.rgb, 0.0), vec4(color.rgb, color.a), sa); // устанавливаем цвет прямоугольника с прозрачностью sa
                }""";

    String roundedCornerRectTexture = """
           #version 120
                // объявление переменных
                uniform vec2 size; // размер прямоугольника
                uniform vec4 round; // коэффициенты скругления углов
                uniform vec2 smoothness; // плавность перехода от цвета к прозрачности
                uniform float value; // значение, используемое для расчета расстояния до границы
                uniform sampler2D textureIn;
                uniform float alpha;

                // функция для расчета расстояния до границы
                float test(vec2 vec_1, vec2 vec_2, vec4 vec_4) {
                    vec_4.xy = (vec_1.x > 0.0) ? vec_4.xy : vec_4.zw;
                    vec_4.x = (vec_1.y > 0.0) ? vec_4.x : vec_4.y;
                    vec2 coords = abs(vec_1) - vec_2 + vec_4.x;
                    return min(max(coords.x, coords.y), 0.0) + length(max(coords, vec2(0.0f))) - vec_4.x;
                }

                void main() {
                    vec4 color = texture2D(textureIn, gl_TexCoord[0].st);
                    vec2 st = gl_TexCoord[0].st * size; // координаты текущего пикселя
                    vec2 halfSize = 0.5 * size; // половина размера прямоугольника
                    float sa = 1.0 - smoothstep(smoothness.x, smoothness.y, test(halfSize - st, halfSize - value, round));
                    // рассчитываем прозрачность в зависимости от расстояния до границы
                    gl_FragColor = mix(vec4(color.rgb, 0.0), vec4(color.rgb, alpha), sa); // устанавливаем цвет прямоугольника с прозрачностью sa
                }""";

    private String gradientMask = "#version 120\n" +
            "\n" +
            "uniform vec2 location, rectSize;\n" +
            "uniform sampler2D tex;\n" +
            "uniform vec3 color1, color2, color3, color4;\n" +
            "uniform float alpha;\n" +
            "\n" +
            "#define NOISE .5/255.0\n" +
            "\n" +
            "vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){\n" +
            "    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);\n" +
            "    //Dithering the color from https://shader-tutorial.dev/advanced/color-banding-dithering/\n" +
            "    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898,78.233))) * 43758.5453));\n" +
            "    return color;\n" +
            "}\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 coords = (gl_FragCoord.xy - location) / rectSize;\n" +
            "    float texColorAlpha = texture2D(tex, gl_TexCoord[0].st).a;\n" +
            "    gl_FragColor = vec4(createGradient(coords, color1, color2, color3, color4), texColorAlpha * alpha);\n" +
            "}";

    private String alphaMask = "#version 120\n" +
            "uniform sampler2D tex;\n" +
            "uniform float alpha;\n" +
            "\n" +
            "\n" +
            "void main() {\n" +
            "    float texColorAlpha = texture2D(tex, gl_TexCoord[0].st).a;\n" +
            "    gl_FragColor = vec4(texture2D(tex, gl_TexCoord[0].st).rgb, texColorAlpha * alpha);\n" +
            "}";

    private String kawaseUpBloom = "#version 120\n" +
            "\n" +
            "uniform sampler2D inTexture, textureToCheck;\n" +
            "uniform vec2 halfpixel, offset, iResolution;\n" +
            "uniform int check;\n" +
            "\n" +
            "void main() {\n" +
            "  //  if(check && texture2D(textureToCheck, gl_TexCoord[0].st).a > 0.0) discard;\n" +
            "    vec2 uv = vec2(gl_FragCoord.xy / iResolution);\n" +
            "\n" +
            "    vec4 sum = texture2D(inTexture, uv + vec2(-halfpixel.x * 2.0, 0.0) * offset);\n" +
            "    sum.rgb *= sum.a;\n" +
            "    vec4 smpl1 =  texture2D(inTexture, uv + vec2(-halfpixel.x, halfpixel.y) * offset);\n" +
            "    smpl1.rgb *= smpl1.a;\n" +
            "    sum += smpl1 * 2.0;\n" +
            "    vec4 smp2 = texture2D(inTexture, uv + vec2(0.0, halfpixel.y * 2.0) * offset);\n" +
            "    smp2.rgb *= smp2.a;\n" +
            "    sum += smp2;\n" +
            "    vec4 smp3 = texture2D(inTexture, uv + vec2(halfpixel.x, halfpixel.y) * offset);\n" +
            "    smp3.rgb *= smp3.a;\n" +
            "    sum += smp3 * 2.0;\n" +
            "    vec4 smp4 = texture2D(inTexture, uv + vec2(halfpixel.x * 2.0, 0.0) * offset);\n" +
            "    smp4.rgb *= smp4.a;\n" +
            "    sum += smp4;\n" +
            "    vec4 smp5 = texture2D(inTexture, uv + vec2(halfpixel.x, -halfpixel.y) * offset);\n" +
            "    smp5.rgb *= smp5.a;\n" +
            "    sum += smp5 * 2.0;\n" +
            "    vec4 smp6 = texture2D(inTexture, uv + vec2(0.0, -halfpixel.y * 2.0) * offset);\n" +
            "    smp6.rgb *= smp6.a;\n" +
            "    sum += smp6;\n" +
            "    vec4 smp7 = texture2D(inTexture, uv + vec2(-halfpixel.x, -halfpixel.y) * offset);\n" +
            "    smp7.rgb *= smp7.a;\n" +
            "    sum += smp7 * 2.0;\n" +
            "    vec4 result = sum / 12.0;\n" +
            "    gl_FragColor = vec4(result.rgb / result.a, mix(result.a, result.a * (1.0 - texture2D(textureToCheck, gl_TexCoord[0].st).a),check));\n" +
            "}";

    private String outline = """
            #version 120
            
            uniform vec4 color;
            uniform sampler2D textureIn, textureToCheck;
            uniform vec2 texelSize, direction;
            uniform float size;
            
            #define offset direction * texelSize
            
            void main() {
                if (direction.y == 1) {
                    if (texture2D(textureToCheck, gl_TexCoord[0].st).a != 0.0) discard;
                }
            
                vec4 innerAlpha = texture2D(textureIn, gl_TexCoord[0].st);
                innerAlpha *= innerAlpha.a;
                for (float r = 1.0; r <= size; r ++) {
                    vec4 colorCurrent1 = texture2D(textureIn, gl_TexCoord[0].st + offset * r);
                    vec4 colorCurrent2 = texture2D(textureIn, gl_TexCoord[0].st - offset * r);
                    colorCurrent1.rgb *= colorCurrent1.a;
                    colorCurrent2.rgb *= colorCurrent2.a;
                    innerAlpha += (colorCurrent1 + colorCurrent2) * r;
                }
                gl_FragColor = vec4(color.rgb, mix(innerAlpha.a, 1.0 - exp(-innerAlpha.a), step(0.0, direction.y)));
            }""";

    private String colorOut = """
            #version 120
            
            uniform vec4 color;
            uniform sampler2D textureIn, textureToCheck;
            uniform vec2 texelSize, direction;
            uniform float size;
            
            #define offset direction * texelSize
            
            void main() {
                if (direction.y == 1) {
                    if (texture2D(textureToCheck, gl_TexCoord[0].st).a != 0.0) discard;
                }
            
                vec4 innerAlpha = texture2D(textureIn, gl_TexCoord[0].st);
                innerAlpha *= innerAlpha.a;
                for (float r = 1.0; r <= size; r ++) {
                    vec4 colorCurrent1 = texture2D(textureIn, gl_TexCoord[0].st + offset * r);
                    vec4 colorCurrent2 = texture2D(textureIn, gl_TexCoord[0].st - offset * r);
                    colorCurrent1.rgb *= colorCurrent1.a;
                    colorCurrent2.rgb *= colorCurrent2.a;
                    innerAlpha += (colorCurrent1 + colorCurrent2) * r;
                }
                gl_FragColor = vec4(innerAlpha.rgb / innerAlpha.a, mix(innerAlpha.a, 1.0 - exp(-innerAlpha.a), step(0.0, direction.y)));
            }""";

    private String kawaseDownBloom = "#version 120\n" +
            "\n" +
            "uniform sampler2D inTexture;\n" +
            "uniform vec2 offset, halfpixel, iResolution;\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 uv = vec2(gl_FragCoord.xy / iResolution);\n" +
            "    vec4 sum = texture2D(inTexture, gl_TexCoord[0].st);\n" +
            "    sum.rgb *= sum.a;\n" +
            "    sum *= 4.0;\n" +
            "    vec4 smp1 = texture2D(inTexture, uv - halfpixel.xy * offset);\n" +
            "    smp1.rgb *= smp1.a;\n" +
            "    sum += smp1;\n" +
            "    vec4 smp2 = texture2D(inTexture, uv + halfpixel.xy * offset);\n" +
            "    smp2.rgb *= smp2.a;\n" +
            "    sum += smp2;\n" +
            "    vec4 smp3 = texture2D(inTexture, uv + vec2(halfpixel.x, -halfpixel.y) * offset);\n" +
            "    smp3.rgb *= smp3.a;\n" +
            "    sum += smp3;\n" +
            "    vec4 smp4 = texture2D(inTexture, uv - vec2(halfpixel.x, -halfpixel.y) * offset);\n" +
            "    smp4.rgb *= smp4.a;\n" +
            "    sum += smp4;\n" +
            "    vec4 result = sum / 8.0;\n" +
            "    gl_FragColor = vec4(result.rgb / result.a, result.a);\n" +
            "}";
}