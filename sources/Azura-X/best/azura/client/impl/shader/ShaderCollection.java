package best.azura.client.impl.shader;

public class ShaderCollection {

	public static final String CHAMS_SHADER =
			"#version 120\n" +
					"uniform sampler2D texture;\n" +
					"uniform vec2 texelSize;\n" +
					"uniform float alpha = 0.2;\n" +
					"void main () {\n" +
					"vec2 texCoord = gl_TexCoord[0].st;\n" +
					"vec4 center = texture2D(texture, texCoord);\n" +
					"gl_FragColor = vec4(center.rgb, center.a * alpha)\n";

	public static final String BLUR_SHADER =
			"#version 120\n" +
					"uniform sampler2D originalTexture;\n" +
					"uniform vec2 texelSize, direction;\n" +
					"uniform float radius = 10, alpha;\n" +
					"\n" +
					"float gauss(float x, float sigma) {\n" +
					"    return .4 * exp(-.5 * x * x / (sigma * sigma)) / sigma;\n" +
					"}\n" +
					"\n" +
					"void main() {\n" +
					"vec3 color = vec3(0.0);\n" +
					"    for(float i = -radius; i <= radius; i++) {\n" +
					"        color += texture2D(originalTexture, gl_TexCoord[0].st + i * texelSize * direction).rgb * gauss(i, radius / 2);\n" +
					"    }\n" +
					"    gl_FragColor = vec4(color, alpha);\n" +
					"}";

	public static final String CHAMS_FILLED_SHADER =
			"#version 120\n" +
					"\n" +
					"uniform sampler2D texture;\n" +
					"uniform vec2 texelSize;\n" +
					"uniform float alpha = 0.2;\n" +
					"uniform vec3 color = vec3(1, 1, 1);\n" +
					"\n" +
					"void main() {\n" +
					"    vec2 texCoord = gl_TexCoord[0].st;\n" +
					"    vec4 center = texture2D(texture, texCoord);\n" +
					"    gl_FragColor = vec4(color, center.a * alpha);\n" +
					"}";

	public static final String OUTLINE_SHADER =
			"#version 120\n" +
					"\n" +
					"uniform sampler2D texture;\n" +
					"uniform vec2 texelSize;\n" +
					"uniform float lineWidth = 1.0, alpha = 0.2, radius = 20;\n" +
					"uniform vec3 color = vec3(1.);\n" +
					"\n" +
					"float gauss(float x, float sigma) {\n" +
					"    return .4 * exp(-.4 * x * x / (sigma * sigma)) / sigma;\n" +
					"}\n" +
					"\n" +
					"void main() {\n" +
					"    vec2 texCoord = gl_TexCoord[0].st;\n" +
					"    float lWidth = max(1.0, lineWidth);\n" +
					"    vec4 center = texture2D(texture, texCoord),\n" +
					"    left = texture2D(texture, texCoord - vec2(texelSize.x * lWidth, 0.)),\n" +
					"    right = texture2D(texture, texCoord + vec2(texelSize.x * lWidth, 0.)),\n" +
					"    up = texture2D(texture, texCoord - vec2(0., texelSize.y * lWidth)),\n" +
					"    down = texture2D(texture, texCoord + vec2(0., texelSize.y * lWidth));\n" +
					"    vec3 col = (left.rgb + right.rgb + up.rgb + down.rgb) * color;\n" +
					"    float alph = clamp((center.a - left.a) + (center.a - right.a) + (center.a - up.a) + (center.a - down.a), 0.0, 2.0) * center.a;\n" +
					"    float a = alph;\n" +
					"    for (float i = -radius; i <= radius; i++) {\n" +
					"        vec4 tex0 = texture2D(texture, gl_TexCoord[0].st + i * texelSize * vec2(1.0, 0.0)),\n" +
					"        tex1 = texture2D(texture, gl_TexCoord[0].st + i * texelSize * vec2(0.0, 1.0));\n" +
					"        a += tex0.a * gauss(i, radius / 2) / 2;\n" +
					"        a += tex1.a * gauss(i, radius / 2) / 2;\n" +
					"    }\n" +
					"    a -= center.a;\n" +
					"    gl_FragColor = vec4(color, a * alpha);\n" +
					"}";
}

