#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif
uniform vec2 resolution;
uniform float time;
float rand(vec2 co){
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453)*100.0;
}
vec2 getblock(float res, vec2 loc) {
    vec2 block = floor((loc) / res)*res;
    return block;
}
vec2 getVector(float res, vec2 loc) {
    float block = rand(getblock(res, loc))*100.0;
    return vec2(sin(block),cos(block));
}
float interpolate(float t) {
    return 6.0*pow(t,5.0)-15.0*pow(t,4.0)+10.0*pow(t,3.0);
}
float perlin(float res, vec2 loc) {
    vec2 localCoord = (getblock(res, loc+res)-loc)/res;
    vec2 vecTL = getVector(res, loc);
    vec2 vecTR = getVector(res, loc+vec2(res,0));
    vec2 vecBL = getVector(res, loc+vec2(0,-res));
    vec2 vecBR = getVector(res, loc+vec2(res,-res));
    vec2 interp = vec2(interpolate(localCoord.x),interpolate(localCoord.y));
    localCoord = localCoord*2.0-1.0;
    vec2 offsettl = vec2(1.0,-1.0);
    vec2 offsettr = vec2(-1.0,-1.0);
    vec2 offsetbl = vec2(1.0,1.0);
    vec2 offsetbr = vec2(-1.0,1.0);
    float dottl = (dot(vecTL, localCoord-offsettl)+1.4)/2.3;
    float dottr = (dot(vecTR, localCoord-offsettr)+1.4)/2.3;
    float dotbl = (dot(vecBL, localCoord-offsetbl)+1.4)/2.3;
    float dotbr = (dot(vecBR, localCoord-offsetbr)+1.4)/2.3;
    float horizTop = interp.x*dottl+(1.0-interp.x)*dottr;
    float horizBottom = interp.x*dotbl+(1.0-interp.x)*dotbr;
    float vertical = interp.y*horizBottom+(1.0-interp.y)*horizTop;
    return vertical/1.1;
}
void main(void) {
    float resolution =  100.0;
    float moveAmount = 1000.0; // Higher = more movement
    float moveSpeed = 35.0; // Lower = faster
    float sex1 = perlin(moveSpeed, vec2(time))*moveAmount;
    float sex2 = perlin(moveSpeed, vec2(time+500.0))*moveAmount;
    float perlin1 = perlin(resolution, 1.0*(gl_FragCoord.xy+vec2(sex1, sex2)));
    float perlin2 = perlin(resolution, 6.0*(gl_FragCoord.xy+vec2(sex1, sex2/2)))/8.0;
    float perlin3 = perlin(resolution, 4.0*(gl_FragCoord.xy+vec2(sex1, sex2)))/16.0;
    float value = (perlin1 + perlin3);
    // change the value to change the color
    gl_FragColor = vec4(value, value, value, 1.0);
    gl_FragColor = vec4(vec3(mod(value*10.0, 1.0)/12.0), 1.0);
}