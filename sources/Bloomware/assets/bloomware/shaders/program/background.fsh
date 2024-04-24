#version 150

#define PI 3.14159265359

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform float Time;

in vec2 texCoord;
out vec4 fragColor;

// --------[ Original ShaderToy begins here ]---------- //
#define SHADOW_ANGLE -50.0
#define SHADOW_COLOR vec3(1, 0, 0)
#define LIGHT_DIR vec2(1, 1)
#define LIGHT_INTENSITY 0.3

#define EPS 0.01
#define ITER 30

float time() {
    return sin(Time * PI);
}

vec2 rot(vec2 uv, float t) {
    float c = cos(t), s = sin(t);
    return mat2(c, -s, s, c) * uv;
}

float sdCircle(vec2 p, float radius) {
    return length(p) - radius;
}

// Copied from iq's article
float sdStar5(in vec2 p, in float r, in float rf)
{
    const vec2 k1 = vec2(0.809016994375, -0.587785252292);
    const vec2 k2 = vec2(-k1.x, k1.y);
    p.x = abs(p.x);
    p -= 2.0 * max(dot(k1, p), 0.0) * k1;
    p -= 2.0 * max(dot(k2, p), 0.0) * k2;
    p.x = abs(p.x);
    p.y -= r;
    vec2 ba = rf * vec2(-k1.y, k1.x) - vec2(0, 1);
    float h = clamp(dot(p, ba) / dot(ba, ba), 0.0, r);
    return length(p - ba * h) * sign(p.y * ba.x - p.x * ba.y);
}

float map(vec2 p) {
    p = rot(p, time());
    return sdStar5(p, 0.4, 0.5);
    //return sdCircle(p, 0.3);
}

vec2 getNormal(vec2 p) {
    vec2 d = vec2(1, 0) * EPS;
    return normalize(vec2(
                         map(p + d.xy) - map(p - d.xy),
                         map(p + d.yx) - map(p - d.yx)
                     ));
}
// --------[ Original ShaderToy ends here ]---------- //

void main() {
    vec2 uv = gl_FragCoord.xy / OutSize.xy;
    vec2 p = uv * 2. - 1.;
    p.x *= OutSize.x / OutSize.y; // fix aspect ratio

    vec3 col;

    // Draw BG
    float a = floor((uv.x - uv.y * 0.5 - time() * .08) * 8.) - time() * 2.;
    col = vec3(sin(a), sin(a + 2.), sin(a + 3.)) * 0.2 + 0.7;

    // Draw infinite shadow by raymarching
    float angle = radians(SHADOW_ANGLE);
    vec2 dir = -vec2(cos(angle), sin(angle));
    float d;
    float t = 0.;
    vec2 p2;

    for (int i = 0; i < ITER; i++) {
        p2 = p + dir * t;
        d = map(p2);
        if (d < EPS) {
            vec2 n = getNormal(p2);
            float shade = dot(n, normalize(LIGHT_DIR));
            col = SHADOW_COLOR;
            col *= 1. + shade * LIGHT_INTENSITY;
        }
        t += d;
    }

    // Draw foreground
    d = map(p);
    float inside = step(d, EPS);
    if (d < EPS) {
        col = vec3(step(d, -EPS));
    }

    // vignette
    col -= smoothstep(0.5, 3.0, length(p));

    fragColor = vec4(col, 1.0);
}