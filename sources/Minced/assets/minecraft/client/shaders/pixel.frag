#version 120

uniform sampler2D textureIn;

void main() {

    vec2 texCoord = gl_TexCoord[0].st;

    vec2 pixel = 1.0/vec2(100.0, 100.0);

    vec2 coord = floor(texCoord/pixel);

    gl_FragColor = texture2D(textureIn, coord * pixel);


}