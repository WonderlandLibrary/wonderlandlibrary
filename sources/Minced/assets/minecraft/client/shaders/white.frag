#version 120
uniform sampler2D textureIn;
void main() {
    vec4 original = texture2D(textureIn, gl_TexCoord[0].st); // получаем оригинальный цвет текстуры
    float d = (original.r + original.b + original.g) / 3; // вычисл€ем €ркость
    gl_FragColor = vec4(d, d, d, 1);
}
