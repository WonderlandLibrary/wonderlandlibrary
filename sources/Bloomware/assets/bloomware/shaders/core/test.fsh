#version 150

//uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulator;
uniform float GameTime;

out vec4 fragColor;

void main() {
    fragColor = vec4(fract(GameTime * 1200) + 0.5, ColorModulator.g, fract(GameTime * 1200), 1.0f);
}