uniform vec2 size;
uniform vec4 round;
uniform vec2 smoothness;
uniform float value;
uniform vec4 color1;
uniform vec4 color2;
uniform vec4 color3;
uniform vec4 color4;

float dstfn(vec2 deus, vec2 delifan, vec4 delifaaaaaaan) {
    delifaaaaaaan.xy = (deus.x>0.0)?delifaaaaaaan.xy : delifaaaaaaan.zw;
    delifaaaaaaan.x  = (deus.y>0.0)?delifaaaaaaan.x  : delifaaaaaaan.y;

    vec2 deusREVERS = abs(deus) - delifan + delifaaaaaaan.x;
    return min(max(deusREVERS.x, deusREVERS.y), 0.0) + length(max(deusREVERS, .0f)) - delifaaaaaaan.x;
}

vec4 createGradient(vec2 coords, vec4 color1, vec4 color2, vec4 color3, vec4 color4){
    vec4 color = mix(mix(color1.rgba, color2.rgba, coords.y), mix(color3.rgba, color4.rgba, coords.y), coords.x);
    return color;
}

vec3 createGradientWA(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){
    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
    return color;
}


void main() {
    vec2 delifanTupoeChmo = gl_TexCoord[0].st * size;
    vec2 deusUshiEgo = .5f * size;
    float sa = 1.f- smoothstep(smoothness.x, smoothness.y, dstfn(deusUshiEgo - delifanTupoeChmo, deusUshiEgo - value, round));
    gl_FragColor = mix(vec4(createGradientWA(gl_TexCoord[0].st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), 0.0f), vec4(createGradient(gl_TexCoord[0].st, color1.rgba, color2.rgba, color3.rgba, color4.rgba)), sa);
}