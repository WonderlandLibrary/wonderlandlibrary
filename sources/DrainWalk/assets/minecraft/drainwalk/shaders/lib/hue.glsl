#if defined TECHNO_XP || defined MAGICAL_GLINT || COLOR_BLOC_SELECTOR == 2 || COLOR_BLOC_SELECTOR == 3 || AURORA_COLOR == 5
    vec3 hue2(float h){
        float t = h * TAU;
        vec3 rgb = vec3(
        	sin(t		  ) * 0.5 + 0.5,
            sin(t + PI/2.0) * 0.5 + 0.5,
            sin(t + PI	  ) * 0.5 + 0.5
        );

        return rgb;
    }

#endif