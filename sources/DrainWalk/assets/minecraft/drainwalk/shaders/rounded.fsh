uniform vec2 size;
uniform float round;
uniform vec2 smoothness;
uniform vec2 swap;
uniform vec4 firstColor, secondColor;

float dstfn(vec2 p, vec2 b, float r) {
    return length(max(abs(p) - b, .0f)) - r;
}

void main() {
    vec2 pixel = gl_TexCoord[0].st * size;
    vec2 centre = .5f * size;
    float sa = smoothstep(smoothness.x, smoothness.y, dstfn(centre - pixel, centre - round - 1.f, round));
    vec4 result = mix(firstColor, secondColor, clamp(min(pixel.x - swap.x, pixel.y - swap.y), 0, 1));
    vec4 c = mix(vec4(result.rgb, 1.f), vec4(result.rgb, .0f), sa);
    gl_FragColor = vec4(c.rgb, result.a * c.a);
}