#version 400

uniform sampler2D AtlasSampler;

in vec4 vertexColor;
in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = vec4(1.0, 1.0, 1.0, texelFetch(AtlasSampler, ivec2(texCoord), 0).r) * vertexColor;
    if (color.a < 0.1) {
        discard;
    }
    fragColor = color;
}
