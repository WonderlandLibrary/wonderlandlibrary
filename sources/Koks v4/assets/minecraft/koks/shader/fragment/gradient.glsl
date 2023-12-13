#version 120

uniform vec2 resolution;
uniform vec3 startColor;
uniform vec3 endColor;

void main() {
	vec2 position = (gl_FragCoord.xy / resolution.xy );
	vec3 difference = (startColor.xyz - endColor.xyz);
	vec3 fade = startColor.xyz - difference.xyz / resolution.x * gl_FragCoord.x;
	vec3 differenceDown = (fade.xyz - vec3(0,0,0).xyz);
	vec3 fadeDown = fade.xyz - differenceDown.xyz / resolution.y * gl_FragCoord.y;
	fade.xyz = fade.xyz - fadeDown.xyz;

	gl_FragColor = vec4( fade, 1.0 );
}