#ifdef GL_ES
precision highp float;
#endif

#extension GL_OES_standard_derivatives : enable

#define NUM_OCTAVES 6

uniform float time;
uniform vec2 resolution;

mat3 rotX(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat3(
    1, 0, 0,
    0, c, -s,
    0, s, c
    );
}
mat3 rotY(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat3(
    c, 0, -s,
    0, 1, 0,
    s, 0, c
    );
}

float random(vec2 pos) {
    return fract(sin(dot(pos.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

float noise(vec2 pos) {
    vec2 i = floor(pos);
    vec2 f = fract(pos);
    float a = random(i + vec2(0.0, 0.0));
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

float fbm(vec2 pos) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100.0);
    mat2 rot = mat2(cos(0.5), sin(0.5), -sin(0.5), cos(0.5));
    for (int i=0; i<NUM_OCTAVES; i++) {
        float dir = mod(float(i), 2.0) > 0.5 ? 1.0 : -1.0;
        v += a * noise(pos - 0.05 * dir * time);

        pos = rot * pos * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}

void main(void) {
    vec2 p = (gl_FragCoord.xy * 2.0 - resolution.xy) / min(resolution.x, resolution.y);
    p -= vec2(12.0, 0.0);

    float t = 0.0, d;

    float time2 = 1.0;

    vec2 q = vec2(0.0);
    q.x = fbm(p + 0.00 * time2);
    q.y = fbm(p + vec2(1.0));
    vec2 r = vec2(0.0);
    r.x = fbm(p + 1.0 * q + vec2(1.7, 9.2) + 0.15 * time2);
    r.y = fbm(p + 1.0 * q + vec2(8.3, 2.8) + 0.126 * time2);
    float f = fbm(p + r);
    vec3 color = mix(
    vec3(1.0, 1.0, 2.0),
    vec3(1.0, 1.0, 1.0),
    //vec3(10.90, 0.2, 0.1),
    //vec3(10.50, 0.07, 0.07),
    clamp((f * f) * 4.0, 1.0, 10.0)
    );

    color = mix(
    color,
    vec3(1.0, 1.0, 1.0),
    //vec3(0.4, 0.1, 0.0),
    clamp(length(q), 10.0, 2.0)
    );


    color = mix(
    color,
    //vec3(0.0, 1.0, 0.0),
    vec3(0.3, 0.3, 0.3),
    clamp(length(r.x), 0.0, 1.0)
    );

    color = (f *f * f + 0.9 * f * f + 0.8 * f) * color;

    //vec2 uv = gl_FragCoord.xy / resolution.xy;
    //float alpha = 1.0 - max(pow(2.0 * distance(uv.x, 0.5), 100.0), pow(2.0 * distance(uv.y, 0.5), 5.0));
    gl_FragColor = vec4(color * 0.7, color.r);
    //gl_FragColor = vec4(color, alpha * color.r);
}