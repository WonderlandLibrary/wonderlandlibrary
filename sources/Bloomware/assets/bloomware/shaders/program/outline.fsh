#version 330

#define PI 3.1415926538

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulate;
uniform float Width;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

vec2 getPointOnCircle(float angle, float radius, vec2 center) {
    float x = center.x + radius * cos(angle) * oneTexel.x;
    float y = center.y + radius * sin(angle) * oneTexel.y;
    return vec2(x, y);
}

void main() {
    vec4 center = texture(DiffuseSampler, texCoord);

    int resolution = int(2.0 * PI * Width);
    float angleStep = 1.0 / Width;

    float totalDiff = 0.0;

    for (int i = 0; i < resolution; i++) {
        vec2 probeCoords = getPointOnCircle(float(i) * angleStep, Width, texCoord);
        vec4 probe = texture(DiffuseSampler, probeCoords);
        totalDiff += center.a - probe.a;
    }

    fragColor = vec4(ColorModulate.rgb, min(totalDiff, 1.0) * ColorModulate.a);
}