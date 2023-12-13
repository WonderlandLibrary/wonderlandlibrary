package io.github.nevalackin.client.util.render;

import static org.lwjgl.opengl.GL20.glUniform2f;

public final class Shaders {

    private Shaders() {
    }

    public static final String BACKGROUND_FRAG_SHADER =
        "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "//jms\n" +
            "#extension GL_OES_standard_derivatives : enable\n" +
            "\n" +
            "#define NUM_OCTAVES 4\n" +
            "\n" +
            "uniform float time;\n" +
            "uniform vec2 mouse;\n" +
            "uniform vec2 resolution;\n" +
            "\n" +
            "#define HALF_PI 1.5707963267948966\n" +
            "\n" +
            "float elasticOut(float t) {\n" +
            "  return sin(-13.0 * (t + 1.0) * HALF_PI) * pow(2.0, -10.0 * t) + 1.0;\n" +
            "}\n" +
            "\n" +
            "mat3 rotX(float a) {\n" +
            "\tfloat c = cos(a);\n" +
            "\tfloat s = sin(a);\n" +
            "\treturn mat3(\n" +
            "\t\t1, 0, 0,\n" +
            "\t\t0, c, -s,\n" +
            "\t\t0, s, c\n" +
            "\t);\n" +
            "}\n" +
            "mat3 rotY(float a) {\n" +
            "\tfloat c = cos(a);\n" +
            "\tfloat s = sin(a);\n" +
            "\treturn mat3(\n" +
            "\t\tc, 0, -s,\n" +
            "\t\t0, 1, 0,\n" +
            "\t\ts, 0, c\n" +
            "\t);\n" +
            "}\n" +
            "\n" +
            "float random(vec2 pos) {\n" +
            "\treturn fract(1.0 * sin(pos.y + fract(100.0 * sin(pos.x)))); // http://www.matteo-basei.it/noise\n" +
            "}\n" +
            "\n" +
            "float noise(vec2 pos) {\n" +
            "\tvec2 i = floor(pos);\n" +
            "\tvec2 f = fract(pos);\n" +
            "\tfloat a = random(i + vec2(0.0, 0.0));\n" +
            "\tfloat b = random(i + vec2(1.0, 0.0));\n" +
            "\tfloat c = random(i + vec2(0.0, 1.0));\n" +
            "\tfloat d = random(i + vec2(1.0, 1.0));\n" +
            "\tvec2 u = f * f * (3.0 - 2.0 * f);\n" +
            "\treturn mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;\n" +
            "}\n" +
            "\n" +
            "float fbm(vec2 pos) {\n" +
            "\tfloat v = 0.0;\n" +
            "\tfloat a = 0.5;\n" +
            "\tvec2 shift = vec2(mouse.x * 1.35, mouse.y * 1.25);\n" +
            "\tmat2 rot = mat2(cos(0.5), sin(0.2), -sin(0.25), cos(0.5));\n" +
            "\tfor (int i=0; i<NUM_OCTAVES; i++) {\n" +
            "\t\tv += a * noise(pos);\n" +
            "\t\tpos = rot * pos * 2.0 + shift;\n" +
            "\t\ta *= 0.55;\n" +
            "\t}\n" +
            "\treturn v;\n" +
            "}\n" +
            "\n" +
            "void main(void) {\n" +
            "\t\n" +
            "\tvec2 p = (gl_FragCoord.xy * 2.0 - resolution.xy) / min(resolution.x, resolution.y);\n" +
            "\n" +
            "\tfloat t = 0.0, d;\n" +
            "\n" +
            "\tfloat time2 = 1.0 * time / 3.5;\n" +
            "\n" +
            "\tvec2 q = vec2(0.0);\n" +
            "\tq.x = fbm(p + 0.00 * time2);\n" +
            "\tq.y = fbm(p + vec2(1.0));\n" +
            "\tvec2 r = vec2(0.0);\n" +
            "\tr.x = fbm(p + 2.0 * q + vec2(5, 52) + 0.5 * time2);\n" +
            "\tr.y = fbm(p + 1.0 * q + vec2(5.2, 5.2) + 0.1 * time2);\n" +
            "\tfloat f = fbm(p + r);\n" +
            "\tvec3 color = mix(\n" +
            "\t\tvec3(0.5, 0.0, 0.5),\n" +
            "\t\tvec3(.0, 0.0, 1.0),\n" +
            "\t\tclamp((f * f) *14.0, 0.0, 1.0)\n" +
            "\t);\n" +
            "\n" +
            "\tcolor = mix(\n" +
            "\t\tcolor,\n" +
            "\t\tvec3(0.7, 0.0, 0.5),\n" +
            "\t\tclamp(length(q), 0.0, 1.0)\n" +
            "\t);\n" +
            "\n" +
            "\n" +
            "\tcolor = mix(\n" +
            "\t\tcolor,\n" +
            "\t\tvec3(0.1, 0.00, 0.3),\n" +
            "\t\tclamp(length(r.x), 0.0, 1.0)\n" +
            "\t);\n" +
            "\n" +
            "\tcolor = (f *f * f + 0.3 * f * f + 0.3 * f) * color;\n" +
            "\n" +
            "\tgl_FragColor = vec4(color, 1);\n" +
            "}";

    public static final String BACKGROUND_CZ_FRAG_SHADER =
        "//#  Greetz from DK.... thank you ;\n" +
        "#ifdef GL_ES\n" +
        "precision mediump float;\n" +
        "#endif\n" +
        "\n" +
        "#extension GL_OES_standard_derivatives : enable\n" +
        "\n" +
        "uniform float time;\n" +
        "uniform vec2 mouse;\n" +
        "uniform vec2 resolution;\n" +
        "\n" +
        "const vec3 red = vec3(0.874, 0.049, 0.077);\n" +
        "const vec3 blue = vec3(0.040,0.332,0.6291);\n" +
        "const vec3 white = vec3(1, 1, 1);\n" +
        "vec3 col1;\n" +
        "const float PI = 3.1415926535;\n" +
        "void main( void ) {\n" +
        "\n" +
        "\tvec2 p = 2.0*( gl_FragCoord.xy / resolution.xy ) -1.0; \n" +
        "\tp.x *= resolution.x/resolution.y; \n" +
        "\t\n" +
        "\tp.x += sin(p.y+time*2.0)*.05;\n" +
        "\tp.y += sin(p.x*2.0-time*2.0)*.2;\n" +
        "\t\n" +
        "\t\n" +
        "\t\n" +
        "\tvec2 uv = (gl_FragCoord.xy*2.-resolution.xy)/resolution.y+1.1;\n" +
        "\t\n" +
        "\tfloat w = sin((uv.x + uv.y - time * .5 + sin(1.5 * uv.x + 4.5 * uv.y) * PI * .3) * PI * .6); // fake waviness factor\n" +
        " \n" +
        "\t\tcol1 = vec3(0.80,0.80,0.0);\n" +
        "\t\tcol1 = mix(col1, red, smoothstep(.01, .025, uv.y+w*.02));\n" +
        "\t\t \n" +
        "\t\tcol1 += w * .2;\n" +
        "\t\n" +
        "\tvec3 col = col1; \n" +
        "\t\n" +
        "\tif (p.y > 0.0) col = white+w*0.3;\n" +
        "\t\n" +
        "\tfloat tw = (1.0 - abs(p.y * resolution.x/resolution.y)) * 0.5;\n" +
        "\t\n" +
        "\tif (p.x < tw) col = blue+w*0.3;\n" +
        "\t\n" +
        "\t//if(abs(p.x) > 1.60) col = col1;\n" +
        "\t//if(abs(p.y) > 1.0) col = col1;\n" +
        "\t\n" +
        "\tgl_FragColor = vec4(col , 1.0); \n" +
        "}";

    public static final String VERTEX_SHADER =
        "#version 120 \n" +
            "\n" +
            "void main() {\n" +
            "    gl_Position = gl_Vertex;\n" +
            "}";
}
