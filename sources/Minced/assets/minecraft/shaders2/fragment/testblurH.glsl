#version 120

const float kernel = 10.0;
const float weight = 1.0;
uniform sampler2D s_diffuse;
uniform vec2 u_oneTexel;
uniform int intensity;
uniform float blursigma;
uniform vec2 coords;

float CalcGauss(float x, float sigma)
{

    float sigmaMultiplication =  ((blursigma * blursigma));

    if (blursigma < 1) {
        return (exp(-.5 * x * x) * .4);
    } else {

        return (exp(-.5 * x * x / (sigmaMultiplication))/ blursigma) * .4;//bisschen umgeschrieben von der eigendlichen methode, da die eigendliche für einen full solid blur ist
        // (exp(-.5) -> Color correction
    }
}



void main(void) {
    vec3 finalColor = vec3(0.0);
    float weight = 0.;
    for (float radiusF = -intensity; radiusF <= intensity; radiusF++) {
        vec2 coord = (s_diffuse, gl_TexCoord[0].st + radiusF * u_oneTexel * coords);
        vec4 texel = texture2D(s_diffuse, gl_TexCoord[0].st + radiusF * u_oneTexel * coords);
        float w = length(texel.rgb)+0.1;
        weight+=w;
        finalColor.rgb += texel.rgb *w;
    }

    vec3 col = finalColor / weight;
    float gamma = 1.0;
    gl_FragColor = vec4(pow(col.rgb, vec3(1.0/gamma)), 1.0);
}
