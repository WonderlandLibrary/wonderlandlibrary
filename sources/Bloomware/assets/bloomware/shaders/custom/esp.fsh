#version 400

in vec4 vertexColor;
in vec4 vertexPosition;

out vec4 fragColor;

void main() {
    vec4 color = vertexColor;
    if (color.a < 0.1) {
        discard;
    }
    fragColor = vertexPosition;
}
