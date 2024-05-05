#version 120

uniform sampler2D texture;
uniform vec2 texelSize, direction;
uniform vec3 color;

#define offset direction * texelSize

void main() {
    float centerAlpha = texture2D(texture, gl_TexCoord[0].xy).a;
    gl_FragColor = vec4(color, 1) * step(0.0, -centerAlpha);

}