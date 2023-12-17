#version 120

uniform sampler2D textureIn;

void main() {

//    mirror effect
    vec2 texCoord = gl_TexCoord[0].st;
    texCoord.x = 1.0 - texCoord.x;
    gl_FragColor = texture2D(textureIn, texCoord);


}