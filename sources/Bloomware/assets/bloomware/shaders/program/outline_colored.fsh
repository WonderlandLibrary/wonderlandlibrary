#ifdef GL_ES
precision mediump float;
#endif

#define PI 3.1415926538
#define MAX_ITERATIONS 100

vec4 circle(vec2 coords, vec2 center, float radius) {
    float distance = distance(coords, center);
    return distance <= radius ? vec4(1.0) : vec4(0.0);
}


vec4 tex(vec2 coords) {
    return circle(coords, vec2(100.0, 100.0), 50.0);
}

vec2 getPointOnCircle(float angle, float radius, vec2 center) {
    float x = center.x + radius * cos(angle);
    float y = center.y + radius * sin(angle);
    return vec2(x, y);
}

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec4 center = tex(fragCoord);

    float radius = 5.0;
    int resolution = int(2.0 * PI * radius);
    
    float angleStep = 1.0 / radius;
    
    float totalDiff = 0.0;
    
    vec3 colorAccum = vec3(0.0);
    int colorProbes = 0;
    
    for (int i = 0; i < MAX_ITERATIONS; i++) {
        if (i >= resolution) break;
        vec2 probeCoords = getPointOnCircle(float(i) * angleStep, radius, fragCoord);
        vec4 probe = tex(probeCoords);
        totalDiff += center.a - probe.a;
        if (probe.a > 0.75) {
            colorProbes++;
            colorAccum += probe.rgb;
        }
    }
    
    vec3 colorRGB = colorProbes > 0 ? (colorAccum / float(colorProbes)) : vec3(0.0);
    
    fragColor = vec4(colorRGB, min(totalDiff, 1.0));
}

void main() {
    mainImage(gl_FragColor, gl_FragCoord.xy);
}