#ifdef GL_ES
precision highp float;
#endif

uniform vec2 resolution;
uniform vec2 mouse;
uniform float time;

float iTime = time;
vec2 iResolution = resolution;
vec2 iMouse = mouse;


#define A .1
#define V 3.
#define W 3.
#define T 1.3
#define S 6.

float sine(vec2 p, float o)
{
    return pow(T / abs((p.y + sin((p.x * W + o)) * A)), S);
}

float gaussian(float x, float sigma)
{
    return exp(-0.5 * (x * x) / (sigma * sigma));
}

void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
    vec2 p = fragCoord.xy / iResolution.xy * 5. - 1.;

    float sineValue = sine(p, iTime * V);

    vec3 lineColor = vec3(10.0 / 255.0, 60.0 / 255.0, 112.0 / 255.0);

    float glow = gaussian(sineValue, 0.3); // Adjust the second parameter for glow intensity

    vec3 finalColor = mix(lineColor, vec3(0.0), glow);

    fragColor = vec4(finalColor, sineValue);
}

void main(){
    mainImage(gl_FragColor, gl_FragCoord.xy);
}