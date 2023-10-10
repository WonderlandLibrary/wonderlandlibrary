#version 120

uniform vec2 u_size;
uniform vec4 u_radius;
uniform vec4 u_color;

void main(void)
{
//    rounded rectange with antialiasing, and vec4 radius by corner
//    by inigo quilez - iq/2013

    vec2  p = gl_FragCoord.xy/u_size.xy;
    vec2  d = fwidth(p);
    vec2  r = u_radius.xy;
    vec2  q = abs(p-0.5);
    vec2  e = r+d;
    vec2  a = smoothstep( r, e, q );
    float f = a.x*a.y;
    vec4  c = vec4( u_color.xyz, u_color.w*f );

    gl_FragColor = c;
}