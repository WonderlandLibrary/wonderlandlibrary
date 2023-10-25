#version 120

uniform float width;
uniform float height;
uniform float radius;
uniform float time;

uniform float shadeSpeed;
uniform float shadeSize;
uniform bool shadeColor;
uniform bool rainbow;
uniform bool verticalShade;

uniform vec3 firstColor;
uniform vec3 secondColor;
uniform float rainbowSaturation;
uniform float rainbowBrightness;
uniform float alphaColor;
uniform float thickness;

float getColorDirection() {
    return gl_TexCoord[0].st.x + gl_TexCoord[0].st.y;
}

float roundedRect(vec2 pixel, vec2 center, float radius) {
    vec2 rectangle = abs(pixel) - center + radius;
    return min(max(rectangle.x, rectangle.y), 0.0) + length(max(rectangle, 0.0)) - radius;
}

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec3 mixColors(float speed, float size, vec3 firstColor, vec3 secondColor) {
    float mixed = 0.5 + 0.5 * cos((time * -speed) + (getColorDirection() / size));

    return mix(firstColor, secondColor, mixed);
}

vec3 colorCycle(vec2 pixel, vec2 center, float speed, float size, vec3 firstColor, vec3 secondColor) {
    float angle = length(abs(pixel) - center) / max(1.0, float(int(size)));

    float mixed = 0.5 + 0.5 * cos((time * -speed) + angle);

    return mix(firstColor, secondColor, mixed);
}

vec3 rainbowColor(float speed, float size) {
    float mixed = 0.5 + 0.5 * cos((time * -speed) + (getColorDirection() / size));

    return hsv2rgb(vec3(mixed, rainbowSaturation, rainbowBrightness));
}

void main() {
    vec2 size = vec2(width, height);
    vec2 pixel = gl_TexCoord[0].st * size;
    vec2 center = 0.5 * size;

    float outline = roundedRect(pixel - center, center - thickness, radius);

    float finalOutline = smoothstep(1.0, 0, abs(outline) / thickness);

    vec3 shade = firstColor;
    if (shadeColor) {
        shade = mixColors(shadeSpeed, shadeSize, firstColor, secondColor);
    } else if (rainbow) {
        shade = rainbowColor(shadeSpeed, shadeSize);
    }

    gl_FragColor = vec4(shade, alphaColor * finalOutline);
}