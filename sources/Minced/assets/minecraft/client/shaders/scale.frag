#version 120

uniform sampler2D textureIn;
uniform float scale;

void main() {

//    scale the texture coordinates to value = 2

    vec2 texCoord = gl_TexCoord[0].st * scale;

    //    get the texture color

    vec4 color = texture2D(textureIn, texCoord);

    //    set the output color

    gl_FragColor = color;



}