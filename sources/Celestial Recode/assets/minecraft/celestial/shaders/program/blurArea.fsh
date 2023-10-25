#version 130

uniform float radius;
uniform vec2 resolution;
uniform vec4 in_color;
uniform vec2 p1;
uniform vec2 p2;
uniform vec2 shadow_pos;
uniform float shadow_blur;
uniform vec4 shadow_color;

float roundedBoxSDF(vec2 CenterPosition, vec2 Size, float Radius) {
    return length(max(abs(CenterPosition)-Size+Radius, 0.0))-Radius;
}

void main() {
    vec2 point1 = p1;
    vec2 point2 = p2;
    point1 /= vec2(2.);
    point1 += vec2(.5);
    point2 /= vec2(2.);
    point2 += vec2(.5);

    vec2 diff = abs(point2 - point1);
    float len = length(diff);
    float width = (resolution.x / resolution.y) * (diff.x / diff.y);

    vec2 uv = (gl_FragCoord.xy / resolution - point1) / (point2 - point1) - .5;
    uv = vec2(uv.x * width, uv.y);

    float shadowAlpha = 1.;
    float distance = 0.0;
    if(shadow_color.a != 0.0) {
        float distance = roundedBoxSDF((uv - shadow_pos) * resolution.x * len, vec2(.5 * width, .5) * resolution.x * len, radius * resolution.x * len);

        if (shadow_blur != 0.0) {
            float blur = shadow_blur * resolution.x * len;
            shadowAlpha = 1.-smoothstep(-blur, blur, 1.-distance);
        } else {
            shadowAlpha = distance;
        }
    }

    distance = roundedBoxSDF(uv * resolution.x * len, vec2(.5 * width, .5) * resolution.x * len, radius * resolution.x * len);
    gl_FragColor = mix(vec4(1., 1., 1., clamp(1.-shadowAlpha, 0., 1.)) * shadow_color, vec4(1., 1., 1., 1.)*in_color, clamp(1.-distance, 0., 1.));
}