//Stars effect
#extension GL_OES_standard_derivatives : enable

#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;

float field(in vec3 p,float s) {
    float strength = 7. + .03 * log(1.e-6 + fract(sin(time) * 4373.11));
    float accum = s/4.;
    float prev = 0.;
    float tw = 0.;
    for (int i = 0; i < 20; ++i) {
        float mag = dot(p, p);
        float w = exp(-float(i) / 7.);
        p = abs(p) / mag + vec3(-.5, -.4, -1.5);
        accum += w * exp(-strength * pow(abs(mag - prev), 2.2));
        tw += w;
        prev = mag;
    }
    return max(0., 5. * accum / tw - .7) + 0.2;
}

float field2(in vec3 p, float s) {
    float strength = 7. + .03 * log(1.e-6 + fract(sin(time) * 4373.11));
    float accum = s/4.;
    float prev = 0.;
    float tw = 0.;
    for (int i = 0; i < 13; ++i) {
        float mag = dot(p, p);
        p += abs(p) / mag + vec3(-.5, -.4, -1.5);
        float w = exp(-float(i) / 7.);
        accum += w * exp(-strength * pow(abs(mag - prev), 2.2));
        tw += w;
        prev = mag;
    }
    return max(0., 5. * accum / tw - .7);
}

vec3 nrand3( vec2 co ) {
    vec3 a = fract(cos(co.x*0.3e-3 + co.y * vec3(12.,87.,1.0)) * vec3(1.3e5, 4.7e5, 2.9e5) * fract(sin(dot(co,vec2(12.8697,776.1243)))));
    vec3 b = fract(sin(co.x*8.3e-3 + co.y * vec3(12.,87.,1.0)) * vec3(8.1e5, 1.0e5, 0.1e5) * floor(sin(dot(co,vec2(26.7416,17.8943)))));
    vec3 c = mix(a, b, 0.2);
    return c;
}

// Simple 2D random function
float random(vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

vec2 rotateUV(vec2 uv, float rotation, vec2 mid)
{
    return vec2(
    cos(rotation) * (uv.x - mid.x) + sin(rotation) * (uv.y - mid.y) + mid.x,
    cos(rotation) * (uv.y - mid.y) - sin(rotation) * (uv.x - mid.x) + mid.y
    );
}

void mainImage( out vec4 fragColor, in vec2 fragCoord ) {
    vec2 uv = 2. * fragCoord.xy / resolution.xy - 1.;
    vec2 uvs = uv * resolution.xy / max(resolution.x, resolution.y);
    uvs = rotateUV(uvs, sin(time * 0.2), vec2(0.025, 0.025));
    vec3 p = vec3(uvs / 4., 0) + vec3(1., -1.3, 0.);
    p += .2 * vec3(sin(time / 16.), sin(time / 12.),  sin(time / 128.));
    //p.z += 0.2;

    float freqs[4];
    freqs[0] = 0.02;
    freqs[1] = 0.27;//GREEN
    freqs[2] = 0.57;//RED
    freqs[3] = 0.41;//BLUE

    float t = field(p,freqs[2]);
    float v = (1. - exp((abs(uv.x) - 1.) * 6.)) * (1. - exp((abs(uv.y) - 1.) * 6.));

    float zoom_pos = time;
    vec3 p2 = vec3(uvs / (4.+sin(zoom_pos*0.11)*0.2+0.2+sin(zoom_pos*0.15)*0.3+0.4), 1.5) + vec3(2., -1.3, -1.);
    p2 += 0.25 * vec3(sin(time / 16.), sin(time / 12.),  sin(time / 128.));
    //p2.z += 0.2;
    //float t2 = field2(p2,freqs[3]);
    //vec4 c2 = mix(.4, 0.5, v) * vec4(0.8 * t2 * t2 * t2 , 1.5 * t2 * t2 , 1.5 * t2, t2);
    vec4 c2 = vec4(0.0, 0.0, 0.0, 1.0);


    vec2 seed = p.xy * 2.0;
    seed = floor(seed * resolution.x);
    vec3 rnd = nrand3( seed );
    vec4 starcolor = vec4(pow(rnd.y,40.0));

    vec2 seed2 = p2.xy * 2.0;
    seed2 = floor(seed2 * resolution.x);
    vec3 rnd2 = nrand3( seed2 );
    starcolor += vec4(pow(rnd2.y,40.0));

    fragColor = mix(freqs[3]-.3, 1., v) * vec4(1.5*freqs[2] * t * t* t , 1.2*freqs[1] * t * t, freqs[3]*t, 1.0)+c2+starcolor;

    float radius = 0.35;
    vec2 lightDir = normalize(vec2(sin(time * 1.0), cos(time * 1.0)));
    vec4 ambient = vec4(0.0, 0.0, 0.0, 1.0);
    vec2 center = vec2(0.025, 0.025);
    float dist = distance(uvs,center);
    if (dist < radius) {
        vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
        vec2 dir = normalize(center-uvs);
        float intensity = ((dist -radius * 0.8)/ (radius * 0.2));
        //intensity *= max(0.5, dot(lightDir, dir));
        //intensity = floor(intensity * 8.0) / 8.0;
        vec4 planetColor = vec4(0.25 + random(uv) * 0.25, 0.25 + random(uv) * 0.25, 0.25 + random(uv)*0.25, 1.0);
        color = mix(ambient, planetColor, intensity);
        fragColor = color;
    }
    else
    {
        float intensity = min(1.0, max(0.0, sqrt(dist -radius) * 1.4 + 0.3));
        fragColor = mix(vec4(0.6, 0.6, 1.0, 1.0), fragColor, intensity);
    }

    //quantize frag color
    //fragColor.x = floor(fragColor.x * 8.0) / 8.0;
    //fragColor.y = floor(fragColor.y * 8.0) / 8.0;
    //fragColor.z = floor(fragColor.z * 8.0) / 8.0;

}

void main() {
    mainImage(gl_FragColor, gl_FragCoord.xy);
}