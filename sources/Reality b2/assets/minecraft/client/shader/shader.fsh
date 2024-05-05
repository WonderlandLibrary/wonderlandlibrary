precision mediump float;

uniform vec2 resolution;
uniform vec2 mouse;
uniform float time;

float random (in vec2 _st) {
    return fract(sin(dot(_st.xy,
                         vec2(9.9898,78.233)))*
        43758.5453123);
}

// Basierend auf Morgan McGuire @morgan3d
// https://www.shadertoy.com/view/4dS3Wd
float noise (in vec2 _st) {
    vec2 i = floor(_st);
    vec2 f = fract(_st);

    // Vier Ecken in 2D einer Kachel
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x) +
            (c - a)* u.y * (1.0 - u.x) +
            (d - b) * u.x * u.y;
}

#define NUM_OCTAVES 4

float fbm ( in vec2 _st) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(20.0);
    // Drehung zur Verringerung von Achsenbias
    mat2 rot = mat2(cos(0.5), sin(0.5),
                    -sin(0.5), cos(0.50));
    for (int i = 0; i < NUM_OCTAVES; ++i) {
        v += a * noise(_st);
        _st = rot * _st * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}

void main() {
    vec2 st = gl_FragCoord.xy/resolution.xy*3.;
    // st += st * abs(sin(u_time*0.1)*3.0);
    vec3 color = vec3(0.0);

    vec2 q = vec2(0.);
    q.x = fbm( st + 0.00*time*0.2);
    q.y = fbm( st + vec2(1.0));

    vec2 r = vec2(0.);
    r.x = fbm( st + 1.0*q + vec2(.51,.82)+ 0.15*time*0.9 );
    r.y = fbm( st + 1.0*q + vec2(1.05,.68)+ 0.326*time*0.1);

    float f = fbm(st+r);

    color = mix(vec3(0.0),
                vec3(0.356863, 0.160784, 0.713725),
                clamp((f*f)*4.0, 0.0, 1.0));

    color = mix(color,
                vec3(0.0, 0.0, 0.094118),
                clamp(length(q), 0.0, 1.0));

    color = mix(color,
                vec3(0.721569, 0.0, 1.0),
                clamp(length(r.x), 0.0, 1.0));

    color = mix(vec3(0.0), color, 0.4);

    gl_FragColor = vec4((f*f*f + 0.6*f*f + 0.5*f)*color, 1.0);
}
