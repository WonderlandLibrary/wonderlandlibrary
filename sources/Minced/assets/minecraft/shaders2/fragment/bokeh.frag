/*
Bokeh blur shader example written by felix1337 @12.04.2022
*/

#version 120

uniform sampler2D tex;
uniform float max_radius;
uniform vec2 texelSize;
uniform vec2 coords;

void main(void) {
    vec3 finalColor = vec3(0.0);
    float weight = 0.;
    for (float radiusF = -max_radius; radiusF <= max_radius; radiusF++) {
        vec2 coord = (tex, gl_TexCoord[0].st + radiusF * texelSize * coords);
        vec4 texel = texture2D(tex, gl_TexCoord[0].st + radiusF * texelSize * coords);
        float w = length(texel.rgb);
        weight+=w;
        finalColor.rgb += texel.rgb *w;
    }

    vec3 col = finalColor / weight;
    float gamma = 1.0;
    gl_FragColor = vec4(pow(col.rgb, vec3(1.0/gamma)), 1.0);
}  
  