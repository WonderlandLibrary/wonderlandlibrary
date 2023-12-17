#version 120

uniform sampler2D textureIn;

uniform float time;

void main() {

    //    distort the texture coordinates with time and sine wave
    vec2 uv = gl_TexCoord[0].st;
    uv.x += sin(uv.y * 10.0 + time) * 0.01;
    uv.y += sin(uv.x * 10.0 + time) * 0.01;

    //    get the color from the texture
    vec4 color = texture2D(textureIn, uv);

    //    set the color
    gl_FragColor = color;







//    vec2 uv = gl_TexCoord[0].st;
//
//    uv.x += sin(uv.y * 10.0) * 0.01;
//
//    uv.y += sin(uv.x * 10.0) * 0.01;
//
//    gl_FragColor = texture2D(textureIn, uv);



}