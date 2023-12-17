#version 120
uniform vec2 location, rectSize;
uniform vec4 color1, color2, color3, color4, outlineColor;
uniform float radius, outlineThickness;
#define NOISE .5/255.0

float roundSDF(vec2 p, vec2 b, float r) {
    return length(max(abs(p) - b, 0.0)) - r;
}

float roundedSDF(vec2 centerPos, vec2 size, float radius) {
    return length(max(abs(centerPos) - size + radius, 0.0)) - radius;
}

vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){
    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));
    return color;
}
void main() {
    vec2 st = gl_TexCoord[0].st;
    vec2 halfSize = rectSize * .5;
    float distance = roundedSDF(gl_FragCoord.xy - location - (rectSize * .5), (rectSize * .5) + (outlineThickness *.5) - 1.0, radius);

    float smoothedAlpha =  (1.0-smoothstep(0.0, 2., roundSDF(halfSize - (gl_TexCoord[0].st * rectSize), halfSize - radius - 1., radius))) * color1.a;

    vec3 col = createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb);

    vec4 insideColor = (distance < 0.) ? vec4(col.rgb, 0.0) : vec4(outlineColor.rgb,  0.0);
    gl_FragColor = vec4(insideColor.rgb, smoothedAlpha);
}