#version 330

in vec2 pos;

uniform sampler2D u_texture;
uniform vec2      u_textureSize;
uniform float     u_sigma;
uniform vec2      u_dir;

float CalcGauss( float x, float sigma )
{
    if ( sigma <= 0.0 )
    return 0.0;
    return exp( -(x*x) / (2.0 * sigma) ) / (2.0 * 3.14157 * sigma);
}

void main()
{
    vec2 texC     = pos.st * 0.5 + 0.5;
    vec4 texCol   = texture2D( u_texture, texC );
    vec4 gaussCol = vec4( texCol.rgb, 1.0 );
    vec2 step     = u_dir / u_textureSize;
    for ( int i = 1; i <= 32; ++ i )
    {
        float weight = CalcGauss( float(i) / 32.0, u_sigma * 0.5 );
        if ( weight < 1.0/255.0 )
        break;
        texCol    = texture2D( u_texture, texC + step * float(i) );
        gaussCol += vec4( texCol.rgb * weight, weight );
        texCol    = texture2D( u_texture, texC - step * float(i) );
        gaussCol += vec4( texCol.rgb * weight, weight );
    }
    gaussCol.rgb = clamp( gaussCol.rgb / gaussCol.w, 0.0, 1.0 );
    gl_FragColor = vec4( gaussCol.rgb, 1.0 );
}