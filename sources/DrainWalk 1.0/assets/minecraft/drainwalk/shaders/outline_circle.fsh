#version 120

#define PI 3.14159265359

uniform float radialSmoothness, radius, borderThickness, progress;
uniform int change;
uniform vec4 firstColor;
uniform vec4 secondColor;
uniform vec2 pos;

uniform vec2 gradient;

vec3 createGradient(float coords, vec3 color1, vec3 color2){
    vec3 color = mix(color1.rgb, color2.rgb, coords);
    return color;
}

void main() {
    vec2 st = gl_FragCoord.xy - (pos + radius + borderThickness);

    float circle = sqrt(dot(st, st));
    float smoothedAlpha = 1.0 - smoothstep(borderThickness, borderThickness + 3., abs(radius-circle));
    vec4 circleColor = vec4(firstColor.rgb, smoothedAlpha * firstColor.a);
    gl_FragColor = mix(vec4(circleColor.rgb, 0.0), vec4(createGradient(gradient.x * (atan(st.y, st.x) - (progress-.5) * PI * gradient.y), firstColor.rgb, secondColor.rgb), circleColor.a), smoothstep(0., radialSmoothness, change * (atan(st.y, st.x) - (progress-.5) * PI * 2.5)));
}