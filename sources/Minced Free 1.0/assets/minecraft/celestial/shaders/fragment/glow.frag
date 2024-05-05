#version 120

uniform sampler2D texture;
uniform vec2 texelSize;
uniform vec3 color;
uniform float radius;
uniform vec2 direction;

void main() {
    vec4 center = texture2D(texture, gl_TexCoord[0].xy);
    if(center.a != 0) {
        gl_FragColor = vec4(color, 0);
    } else {
        vec4 innerColor;
        for(float r = -radius; r <= radius; r++) {
            vec4 colorCurrent = texture2D(texture, gl_TexCoord[0].xy + texelSize * r * direction);
            if(colorCurrent.a != 0) {
                innerColor += colorCurrent;
            }
        }
        gl_FragColor = vec4(innerColor.rgb / innerColor.a, innerColor.a);
    }
}