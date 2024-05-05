#version 120

uniform vec2 location, rectSize;
uniform sampler2D tex;
uniform vec4 color1, color2, color3, color4;

#define NOISE .5/255.0

vec4 createGradient(vec2 coords, vec4 color1, vec4 color2, vec4 color3, vec4 color4){
    vec4 color = mix(mix(color1, color2, coords.y), mix(color3, color4, coords.y), coords.x);
    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));
    return color;
}

void main() {
    vec2 coords = (gl_FragCoord.xy - location) / rectSize;
    gl_FragColor = createGradient(coords, color1, color2, color3, color4);
}
