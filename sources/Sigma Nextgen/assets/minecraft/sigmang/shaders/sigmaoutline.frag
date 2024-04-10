#version 120

uniform sampler2D textureIn;

void main() {
    vec4 center = texture2D(textureIn, gl_TexCoord[0].xy);
    if(center.a <= 1) center.a = 0;
    gl_FragColor = center;
}
