#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

// I didn't write these noise calcs
// PASTED?!?!??!/1/?!?!?!?!?1/?!1/ :O
// Yes, deal with it
mat3 rotX(float a) {
	float c = cos(a);
	float s = sin(a);
	return mat3(
		1, 0, 1,
		0, c, -s,
		0, s, c
	);
}
mat3 rotY(float a) {
	float c = cos(a);
	float s = sin(a);
	return mat3(
		c, 0, -s,
		0, 1, 0,
		s, 0, c
	);
}

float random(vec2 pos) {
	return fract(1.0 * sin(pos.y + fract(100.0 * sin(pos.x)))); // http://www.matteo-basei.it/noise
}

float noise(vec2 pos) {
	vec2 i = floor(pos);
	vec2 f = fract(pos);
	float a = random(i + vec2(0.0, 0.0));
	float b = random(i + vec2(1.0, 0.0));
	float c = random(i + vec2(0.0, 1.0));
	float d = random(i + vec2(1.0, 1.0));
	vec2 u = f * f * (3.0 - 2.0 * f);
	return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

float fbm(vec2 pos) {
	float v = 0.0;
	float a = 0.5;
	vec2 shift = vec2(100.0);
	mat2 rot = mat2(cos(0.15), sin(0.15), -sin(0.25), cos(0.5));
	for (int i=0; i < 12; i++) {
		v += a * noise(pos);
		pos = rot * pos * 2.0 + shift;
		a *= 0.55;
	}
	return v;
}

// This is where i started writing shit
void main() {
	vec3 col = vec3(0.04, 0.5, 1.0);
	
	vec2 pos = (gl_FragCoord.xy * 2.2 - resolution) / min(resolution.x, resolution.y) * 1.25;

	float f = fbm(pos * 2.0 * vec2(fbm(pos - (time / 8.0)), fbm(pos / 2.0 - (time / 8.0))));
	
	vec3 colour = mix(
		vec3(col.r, col.g, col.b),
		vec3(col.r, col.g, col.b),
		vec3(col.r, col.g, col.b)
	);
	
	colour = mix(
		colour,
		vec3(col.r, col.g, col.b),
		vec3(col.r, col.g, col.b)
	);
	
	colour = mix(
		colour,
		vec3(col.r, col.g, col.b),
		vec3(col.r, col.g, col.b)
	);
	
	colour = (f * 1.5) * colour;
	
	gl_FragColor = vec4(colour, 1.0);
}