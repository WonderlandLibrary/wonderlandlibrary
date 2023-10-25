package intent.AquaDev.aqua.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderBackground {
    public static final String VERTEX_SHADER = "#version 130\n\nvoid main() {\n    gl_TexCoord[0] = gl_MultiTexCoord0;\n    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n}";
    private final Minecraft mc = Minecraft.getMinecraft();
    private final int program = GL20.glCreateProgram();
    private final long startTime = System.currentTimeMillis();
    private final String fragment;

    public ShaderBackground() {
        this("#ifdef GL_ES\nprecision mediump float;\n#endif\n#extension GL_OES_standard_derivatives : enable\n#define NUM_OCTAVES 10\nuniform float time;\nuniform vec2 resolution;\nmat3 rotX(float a) {\nfloat c = cos(a);\nfloat s = sin(a);\nreturn mat3(\n    1, 0, 0,\n    0, c, -s,\n    0, s, c\n);\n}\nmat3 rotY(float a) {\nfloat c = cos(a);\nfloat s = sin(a);\nreturn mat3(\n    c, 0, -s,\n    0, 1, 0,\n    s, 0, c\n);\n}\nfloat random(vec2 pos) {\nreturn fract(sin(dot(pos.xy, vec2(.8, 7.233))) * 438.5453123);\n}\nfloat noise(vec2 pos) {\nvec2 i = floor(pos);\nvec2 f = fract(pos);\nfloat a = random(i + vec2(0.0, 0.0));\nfloat b = random(i + vec2(1.0, 0.0));\nfloat c = random(i + vec2(0.0, 1.0));\nfloat d = random(i + vec2(1.0, 1.0));\nvec2 u = f * f * (3.0 - 2.0 * f);\nreturn mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;\n}\nfloat fbm(vec2 pos) {\nfloat v = 0.0;\nfloat a = 0.5;\nvec2 shift = vec2(1000.0);\nmat2 rot = mat2(cos(0.5), sin(0.5), -sin(0.5), cos(0.5));\nfor (int i=0; i<NUM_OCTAVES; i++) {\n    v += a * noise(pos);\n    pos = rot * pos * 1.0 + shift;\n    a *= 0.5;\n}\nreturn v;\n}\nvoid main(void) {\nvec2 p = (gl_FragCoord.xy * 2.0 - resolution.xy) / min(resolution.x, resolution.y);\nfloat t = 0.0, d;\nfloat time2 = 3.0 * time / 2.0;\nvec2 q = vec2(0.0);\nq.x = fbm(p + 0.00 * time2);\nq.y = fbm(p + vec2(1.0));\nvec2 r = vec2(0.0);\nr.x = fbm(p + 1.0 * q + vec2(0.0, 9.2) + 0.15 * time2);\nr.y = fbm(p + 1.0 * q + vec2(0.0, 2.8) + 0.126 * time2);\nfloat f = fbm(p + r);\nvec3 color = mix(\n    vec3(0.0, 0.0, 0),\n    vec3(3, 0.0, 0),\n    clamp((f * f) * 4.0, 0.0, 1.0)\n);\ncolor = mix(\n    color,\n    vec3(0.0, 0.0, 0.5),\n    clamp(length(q), 0.0, 1.0)\n);\ncolor = mix(\n    color,\n    vec3(0.9, .0, .9),\n    clamp(length(r.x), 0.0, 1.0)\n);\ncolor = (f *f * f + 0.0 * f * f + 0.9 * f) * color;\ngl_FragColor = vec4(color, 1);\n}");
    }

    public ShaderBackground(String fragment) {
        this.initShader(fragment);
        this.fragment = fragment;
    }

    public void initShader(String frag) {
        int vertex = GL20.glCreateShader((int)35633);
        int fragment = GL20.glCreateShader((int)35632);
        GL20.glShaderSource((int)vertex, (CharSequence)VERTEX_SHADER);
        GL20.glShaderSource((int)fragment, (CharSequence)frag);
        GL20.glValidateProgram((int)this.program);
        GL20.glCompileShader((int)vertex);
        GL20.glCompileShader((int)fragment);
        GL20.glAttachShader((int)this.program, (int)vertex);
        GL20.glAttachShader((int)this.program, (int)fragment);
        GL20.glLinkProgram((int)this.program);
    }

    public void renderFirst() {
        GL11.glClear((int)16640);
        this.bindShader();
    }

    public void renderSecond(int scaledWidth, int scaledHeight) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)7);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glTexCoord2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)scaledHeight);
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)scaledWidth, (double)scaledHeight);
        GL11.glTexCoord2d((double)1.0, (double)1.0);
        GL11.glVertex2d((double)scaledWidth, (double)0.0);
        GL11.glEnd();
        GL20.glUseProgram((int)0);
    }

    public void renderShader() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.renderFirst();
        this.addDefaultUniforms();
        this.renderSecond(sr.getScaledWidth(), sr.getScaledHeight());
    }

    public void renderShader(int width, int height) {
        this.renderFirst();
        this.addDefaultUniforms();
        this.renderSecond(width, height);
    }

    public void bindShader() {
        GL20.glUseProgram((int)this.program);
    }

    public void addDefaultUniforms() {
        GL20.glUniform2f((int)GL20.glGetUniformLocation((int)this.program, (CharSequence)"resolution"), (float)this.mc.displayWidth, (float)this.mc.displayHeight);
        float time = (float)(System.currentTimeMillis() - this.startTime) / 1000.0f;
        GL20.glUniform1f((int)GL20.glGetUniformLocation((int)this.program, (CharSequence)"time"), (float)time);
    }
}
