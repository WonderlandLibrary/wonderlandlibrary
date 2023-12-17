#version 120

uniform sampler2D s_diffuse;
uniform vec2 u_oneTexel;
uniform int intensity;

uniform float offset[3] = float[](0.0, 1.3846153846, 3.2307692308);
uniform float weight[3] =float[](0.2270270270, 0.3162162162, 0.0702702703);

void main(){
	gl_FragColor = texture2D(s_diffuse, gl_TexCoord[0].st) * weight[0];
	 for (int i=1; i<3; i++) {
		gl_FragColor += texture2D(s_diffuse, (gl_TexCoord[0].st + vec2(offset[i] * u_oneTexel.x, 0.0))) * weight[i];
		gl_FragColor += texture2D(s_diffuse, (gl_TexCoord[0].st - vec2(offset[i] * u_oneTexel.x, 0.0))) * weight[i];
	}
}