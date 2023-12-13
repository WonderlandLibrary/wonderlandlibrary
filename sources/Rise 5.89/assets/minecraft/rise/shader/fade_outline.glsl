#version 120

uniform sampler2D u_diffuse_sampler;
uniform vec2 u_texel_size;
uniform int u_fading;
uniform int u_radius;

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec4 color = texture2D(u_diffuse_sampler, uv);
    if (color.a != 0.0) return;

    float alpha = 0.0;
    for (int x = -u_radius; x <= u_radius; ++x) {
        for (int y = -u_radius; y <= u_radius; ++y) {
            vec4 next_color = texture2D(u_diffuse_sampler, uv + vec2(x, y) * u_texel_size);
            if (next_color.a == 0.0) continue;

			color = next_color;

            alpha += max(0.0, u_radius - sqrt(x * x + y * y));
        }
    }

    gl_FragColor = vec4(color.rgb, alpha * color.a / u_fading);
}