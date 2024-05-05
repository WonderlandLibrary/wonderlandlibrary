uniform vec2 size;
uniform float softness;
uniform float radius;
uniform vec4 color;

float dstfn(vec2 p, vec2 b, float r) {
    return length(max(abs(p) - b, .0f)) - r;
}

void main() {
    vec2 pixel = gl_TexCoord[0].st * size;
    vec2 centre = .5f * size;
    float sa = (1.f - smoothstep(-softness, softness, dstfn(centre - pixel, centre - radius - softness , radius))) * color.a;
    gl_FragColor = vec4(color.rgb, sa);
}