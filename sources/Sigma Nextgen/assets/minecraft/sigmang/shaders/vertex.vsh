#version 120

varying vec2 texCoord;
varying vec2 oneTexel;

void main() {
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}