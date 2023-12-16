#version 120

void main() {
    // Выборка данных из текстуры во фрагментном шейдере (координаты)
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}