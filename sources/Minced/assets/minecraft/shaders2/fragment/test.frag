#version 120

uniform vec3 color;
uniform sampler2D texture;
uniform float alpha;
void main() {
    vec4 texture = texture2D(texture, gl_TexCoord[0].st);

    gl_FragColor = vec4(texture.rgb * color.rgb, 1.0) * alpha;
}