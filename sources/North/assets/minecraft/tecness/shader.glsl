//From: https://codepen.io/shubniggurath/pen/VQwGyE
#version 330
#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;




const int octaves = 4;
const float seed = 2058.5453123;
const float seed2 = 2.8473192;

const float PI = 3.1415927;

// Choose your arrow head style
const float ARROW_TILE_SIZE = 80.0;

// How sharp should the arrow head be? Used
const float ARROW_HEAD_ANGLE = 10.0 * PI / 180.0;

// Used for ARROW_LINE_STYLE
const float ARROW_HEAD_LENGTH = ARROW_TILE_SIZE / 6.0;
const float ARROW_SHAFT_THICKNESS = 1.0;

float random(float val) {
    return fract(sin(val) * seed);
}

vec2 random2(vec2 st, float seed){
    st = vec2( dot(st,vec2(127.1,311.7)),
    dot(st,vec2(269.5,183.3)) );
    return -1.0 + 2.0*fract(sin(st)*seed);
}

float random2d(vec2 uv) {
    return fract(
    sin(
    dot( uv.xy, vec2(12.9898, 78.233) )
    ) * seed);
}

// Value Noise by Inigo Quilez - iq/2013
// https://www.shadertoy.com/view/lsf3WH
float noise(vec2 st, float seed) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    vec2 u = f*f*(3.0-2.0*f);

    return mix( mix( dot( random2(i + vec2(0.0,0.0), seed ), f - vec2(0.0,0.0) ),
    dot( random2(i + vec2(1.0,0.0), seed ), f - vec2(1.0,0.0) ), u.x),
    mix( dot( random2(i + vec2(0.0,1.0), seed ), f - vec2(0.0,1.0) ),
    dot( random2(i + vec2(1.0,1.0), seed ), f - vec2(1.0,1.0) ), u.x), u.y);
}
// Simplex 2D noise
//
vec3 permute(vec3 x) { return mod(((x*34.0)+1.0)*x, 289.0); }

float snoise(vec2 v){
    const vec4 C = vec4(0.211324865405187, 0.366025403784439,
    -0.577350269189626, 0.024390243902439);
    vec2 i  = floor(v + dot(v, C.yy) );
    vec2 x0 = v -   i + dot(i, C.xx);
    vec2 i1;
    i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
    vec4 x12 = x0.xyxy + C.xxzz;
    x12.xy -= i1;
    i = mod(i, 289.0);
    vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))
    + i.x + vec3(0.0, i1.x, 1.0 ));
    vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy),
    dot(x12.zw,x12.zw)), 0.0);
    m = m*m ;
    m = m*m ;
    vec3 x = 2.0 * fract(p * C.www) - 1.0;
    vec3 h = abs(x) - 0.5;
    vec3 ox = floor(x + 0.5);
    vec3 a0 = x - ox;
    m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );
    vec3 g;
    g.x  = a0.x  * x0.x  + h.x  * x0.y;
    g.yz = a0.yz * x12.xz + h.yz * x12.yw;
    return 130.0 * dot(m, g);
}

vec3 plotCircle(vec2 pos, vec2 uv, float size) {
    return vec3(smoothstep(size, size + 0.05, length(uv - pos)));
}

float fbm (in vec2 st, float seed) {
    // Initial values
    float value = 0.0;
    float amplitude = .5;
    float frequency = 0.;
    //
    // Loop of octaves
    for (int i = octaves; i > 0; i--) {
        value += amplitude * abs(noise(st, seed));
        st *= 2.;
        amplitude *= .5;
    }
    return clamp(value, 0., 1.);
}
float fbm1 (in vec2 st, float seed) {
    // Initial values
    float value = 0.0;
    float amplitude = .5;
    float frequency = 0.;
    //
    // Loop of octaves
    for (int i = octaves; i > 0; i--) {
        value += amplitude * fract(noise(st, seed));
        st *= 2.;
        amplitude *= .5;
    }
    return value;
}

// 2D vector field visualization by Morgan McGuire, @morgan3d, http://casual-effects.com

// Computes the center pixel of the tile containing pixel pos
vec2 arrowTileCenterCoord(vec2 pos) {
    return (floor(pos / ARROW_TILE_SIZE) + 0.5) * ARROW_TILE_SIZE;
}

// v = field sampled at tileCenterCoord(p), scaled by the length
// desired in pixels for arrows
// Returns 1.0 where there is an arrow pixel.
float arrow(vec2 p, vec2 v) {
    // Make everything relative to the center, which may be fractional
    p -= arrowTileCenterCoord(p);

    float mag_v = length(v);
    float mag_p = length(p);

    if (mag_v > 0.0) {
        // Non-zero velocity case
        vec2 dir_p = p / mag_p, dir_v = v / mag_v;

        // We can't draw arrows larger than the tile radius, so clamp magnitude.
        // Enforce a minimum length to help see direction
        mag_v = clamp(mag_v, 5.0, ARROW_TILE_SIZE / 2.0);

        // Arrow tip location
        v = dir_v * mag_v;

        // Define a 2D implicit surface so that the arrow is antialiased.
        // In each line, the left expression defines a shape and the right controls
        // how quickly it fades in or out.

        // Signed distance from a line segment based on https://www.shadertoy.com/view/ls2GWG by
        // Matthias Reitinger, @mreitinger

        // Line arrow style
        float dist =
        max(
        // Shaft
        ARROW_SHAFT_THICKNESS / 4.0 -
        max(abs(dot(p, vec2(dir_v.y, -dir_v.x))), // Width
        abs(dot(p, dir_v)) - mag_v + ARROW_HEAD_LENGTH / 2.0), // Length

        // Arrow head
        min(0.0, dot(v - p, dir_v) - cos(ARROW_HEAD_ANGLE / 2.0) * length(v - p)) * 2.0 + // Front sides
        min(0.0, dot(p, dir_v) + ARROW_HEAD_LENGTH - mag_v)); // Back

        return clamp(1.0 + dist, 0.0, 1.0);
    } else {
        // Center of the pixel is always on the arrow
        return max(0.0, 1.2 - mag_p);
    }
}

vec3 hsb2rgb( in vec3 c ){
    vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0,4.0,2.0),
    6.0)-3.0)-1.0,
    0.0,
    1.0 );
    rgb = rgb*rgb*(3.0-2.0*rgb);
    return c.z * mix( vec3(1.0), rgb, c.y);
}

/////////////////////////////////////////////////////////////////////

// The vector field; use your own function or texture
vec2 field(vec2 pos) {

    vec2 uv = (pos - 0.5 * resolution.xy) / resolution.y;

    uv *= .5;
    uv.x += cos(time / 2000.) * 200.;
    uv.y += sin(time / 2000.) * 200.;

    float val1 = fbm(uv, seed);
    float val2 = fbm(uv + vec2(100.), seed);

    return vec2(val1, val2) * 6. - 1.;
}


void main() {
    vec2 uv = gl_FragCoord.xy;

    uv *= 1.5;

    vec2 _field = field(uv);
    //float arrow = arrow(uv, field(arrowTileCenterCoord(uv)) * ARROW_TILE_SIZE * 0.5);

    gl_FragColor = vec4(_field * .7 + .5, 0.5, 1.);



}