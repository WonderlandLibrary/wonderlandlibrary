#version 130

uniform sampler2D tex;
uniform vec2 resolution;

void main(void) {

    vec2 uv = texture2D(tex, gl_TexCoord[0].xy).xy;

    float lod = (2.0) * step(uv.x, 0.5);

    vec3 col = texture(tex, vec2(uv.x, 1.0-uv.y), lod).xyz;

    gl_FragColor = vec4(col, 1.0);

}