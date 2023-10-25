uniform vec2 size;
uniform float round;
uniform float thickness;
uniform vec2 smoothness;
uniform vec4 color;

float dstfn(vec2 p, vec2 b, float r) {
    return length(max(abs(p), b) - b) - r;
}

void main() {
    vec2 pixel = gl_TexCoord[0].st * size;
    vec2 centre = 0.5 * size;
    float d = dstfn(centre - pixel, centre - round - thickness, round);
    float sa = smoothstep(smoothness.x, smoothness.y, abs(d));
    vec4 c = mix(vec4(color.rgb, 1), vec4(color.rgb, 0), sa);
    gl_FragColor = vec4(c.rgb, color.a * c.a);
}