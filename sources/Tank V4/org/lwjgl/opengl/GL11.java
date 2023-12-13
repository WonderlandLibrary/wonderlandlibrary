package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class GL11 {
   public static final int GL_ACCUM = 256;
   public static final int GL_LOAD = 257;
   public static final int GL_RETURN = 258;
   public static final int GL_MULT = 259;
   public static final int GL_ADD = 260;
   public static final int GL_NEVER = 512;
   public static final int GL_LESS = 513;
   public static final int GL_EQUAL = 514;
   public static final int GL_LEQUAL = 515;
   public static final int GL_GREATER = 516;
   public static final int GL_NOTEQUAL = 517;
   public static final int GL_GEQUAL = 518;
   public static final int GL_ALWAYS = 519;
   public static final int GL_CURRENT_BIT = 1;
   public static final int GL_POINT_BIT = 2;
   public static final int GL_LINE_BIT = 4;
   public static final int GL_POLYGON_BIT = 8;
   public static final int GL_POLYGON_STIPPLE_BIT = 16;
   public static final int GL_PIXEL_MODE_BIT = 32;
   public static final int GL_LIGHTING_BIT = 64;
   public static final int GL_FOG_BIT = 128;
   public static final int GL_DEPTH_BUFFER_BIT = 256;
   public static final int GL_ACCUM_BUFFER_BIT = 512;
   public static final int GL_STENCIL_BUFFER_BIT = 1024;
   public static final int GL_VIEWPORT_BIT = 2048;
   public static final int GL_TRANSFORM_BIT = 4096;
   public static final int GL_ENABLE_BIT = 8192;
   public static final int GL_COLOR_BUFFER_BIT = 16384;
   public static final int GL_HINT_BIT = 32768;
   public static final int GL_EVAL_BIT = 65536;
   public static final int GL_LIST_BIT = 131072;
   public static final int GL_TEXTURE_BIT = 262144;
   public static final int GL_SCISSOR_BIT = 524288;
   public static final int GL_ALL_ATTRIB_BITS = 1048575;
   public static final int GL_POINTS = 0;
   public static final int GL_LINES = 1;
   public static final int GL_LINE_LOOP = 2;
   public static final int GL_LINE_STRIP = 3;
   public static final int GL_TRIANGLES = 4;
   public static final int GL_TRIANGLE_STRIP = 5;
   public static final int GL_TRIANGLE_FAN = 6;
   public static final int GL_QUADS = 7;
   public static final int GL_QUAD_STRIP = 8;
   public static final int GL_POLYGON = 9;
   public static final int GL_ZERO = 0;
   public static final int GL_ONE = 1;
   public static final int GL_SRC_COLOR = 768;
   public static final int GL_ONE_MINUS_SRC_COLOR = 769;
   public static final int GL_SRC_ALPHA = 770;
   public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
   public static final int GL_DST_ALPHA = 772;
   public static final int GL_ONE_MINUS_DST_ALPHA = 773;
   public static final int GL_DST_COLOR = 774;
   public static final int GL_ONE_MINUS_DST_COLOR = 775;
   public static final int GL_SRC_ALPHA_SATURATE = 776;
   public static final int GL_CONSTANT_COLOR = 32769;
   public static final int GL_ONE_MINUS_CONSTANT_COLOR = 32770;
   public static final int GL_CONSTANT_ALPHA = 32771;
   public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 32772;
   public static final int GL_TRUE = 1;
   public static final int GL_FALSE = 0;
   public static final int GL_CLIP_PLANE0 = 12288;
   public static final int GL_CLIP_PLANE1 = 12289;
   public static final int GL_CLIP_PLANE2 = 12290;
   public static final int GL_CLIP_PLANE3 = 12291;
   public static final int GL_CLIP_PLANE4 = 12292;
   public static final int GL_CLIP_PLANE5 = 12293;
   public static final int GL_BYTE = 5120;
   public static final int GL_UNSIGNED_BYTE = 5121;
   public static final int GL_SHORT = 5122;
   public static final int GL_UNSIGNED_SHORT = 5123;
   public static final int GL_INT = 5124;
   public static final int GL_UNSIGNED_INT = 5125;
   public static final int GL_FLOAT = 5126;
   public static final int GL_2_BYTES = 5127;
   public static final int GL_3_BYTES = 5128;
   public static final int GL_4_BYTES = 5129;
   public static final int GL_DOUBLE = 5130;
   public static final int GL_NONE = 0;
   public static final int GL_FRONT_LEFT = 1024;
   public static final int GL_FRONT_RIGHT = 1025;
   public static final int GL_BACK_LEFT = 1026;
   public static final int GL_BACK_RIGHT = 1027;
   public static final int GL_FRONT = 1028;
   public static final int GL_BACK = 1029;
   public static final int GL_LEFT = 1030;
   public static final int GL_RIGHT = 1031;
   public static final int GL_FRONT_AND_BACK = 1032;
   public static final int GL_AUX0 = 1033;
   public static final int GL_AUX1 = 1034;
   public static final int GL_AUX2 = 1035;
   public static final int GL_AUX3 = 1036;
   public static final int GL_NO_ERROR = 0;
   public static final int GL_INVALID_ENUM = 1280;
   public static final int GL_INVALID_VALUE = 1281;
   public static final int GL_INVALID_OPERATION = 1282;
   public static final int GL_STACK_OVERFLOW = 1283;
   public static final int GL_STACK_UNDERFLOW = 1284;
   public static final int GL_OUT_OF_MEMORY = 1285;
   public static final int GL_2D = 1536;
   public static final int GL_3D = 1537;
   public static final int GL_3D_COLOR = 1538;
   public static final int GL_3D_COLOR_TEXTURE = 1539;
   public static final int GL_4D_COLOR_TEXTURE = 1540;
   public static final int GL_PASS_THROUGH_TOKEN = 1792;
   public static final int GL_POINT_TOKEN = 1793;
   public static final int GL_LINE_TOKEN = 1794;
   public static final int GL_POLYGON_TOKEN = 1795;
   public static final int GL_BITMAP_TOKEN = 1796;
   public static final int GL_DRAW_PIXEL_TOKEN = 1797;
   public static final int GL_COPY_PIXEL_TOKEN = 1798;
   public static final int GL_LINE_RESET_TOKEN = 1799;
   public static final int GL_EXP = 2048;
   public static final int GL_EXP2 = 2049;
   public static final int GL_CW = 2304;
   public static final int GL_CCW = 2305;
   public static final int GL_COEFF = 2560;
   public static final int GL_ORDER = 2561;
   public static final int GL_DOMAIN = 2562;
   public static final int GL_CURRENT_COLOR = 2816;
   public static final int GL_CURRENT_INDEX = 2817;
   public static final int GL_CURRENT_NORMAL = 2818;
   public static final int GL_CURRENT_TEXTURE_COORDS = 2819;
   public static final int GL_CURRENT_RASTER_COLOR = 2820;
   public static final int GL_CURRENT_RASTER_INDEX = 2821;
   public static final int GL_CURRENT_RASTER_TEXTURE_COORDS = 2822;
   public static final int GL_CURRENT_RASTER_POSITION = 2823;
   public static final int GL_CURRENT_RASTER_POSITION_VALID = 2824;
   public static final int GL_CURRENT_RASTER_DISTANCE = 2825;
   public static final int GL_POINT_SMOOTH = 2832;
   public static final int GL_POINT_SIZE = 2833;
   public static final int GL_POINT_SIZE_RANGE = 2834;
   public static final int GL_POINT_SIZE_GRANULARITY = 2835;
   public static final int GL_LINE_SMOOTH = 2848;
   public static final int GL_LINE_WIDTH = 2849;
   public static final int GL_LINE_WIDTH_RANGE = 2850;
   public static final int GL_LINE_WIDTH_GRANULARITY = 2851;
   public static final int GL_LINE_STIPPLE = 2852;
   public static final int GL_LINE_STIPPLE_PATTERN = 2853;
   public static final int GL_LINE_STIPPLE_REPEAT = 2854;
   public static final int GL_LIST_MODE = 2864;
   public static final int GL_MAX_LIST_NESTING = 2865;
   public static final int GL_LIST_BASE = 2866;
   public static final int GL_LIST_INDEX = 2867;
   public static final int GL_POLYGON_MODE = 2880;
   public static final int GL_POLYGON_SMOOTH = 2881;
   public static final int GL_POLYGON_STIPPLE = 2882;
   public static final int GL_EDGE_FLAG = 2883;
   public static final int GL_CULL_FACE = 2884;
   public static final int GL_CULL_FACE_MODE = 2885;
   public static final int GL_FRONT_FACE = 2886;
   public static final int GL_LIGHTING = 2896;
   public static final int GL_LIGHT_MODEL_LOCAL_VIEWER = 2897;
   public static final int GL_LIGHT_MODEL_TWO_SIDE = 2898;
   public static final int GL_LIGHT_MODEL_AMBIENT = 2899;
   public static final int GL_SHADE_MODEL = 2900;
   public static final int GL_COLOR_MATERIAL_FACE = 2901;
   public static final int GL_COLOR_MATERIAL_PARAMETER = 2902;
   public static final int GL_COLOR_MATERIAL = 2903;
   public static final int GL_FOG = 2912;
   public static final int GL_FOG_INDEX = 2913;
   public static final int GL_FOG_DENSITY = 2914;
   public static final int GL_FOG_START = 2915;
   public static final int GL_FOG_END = 2916;
   public static final int GL_FOG_MODE = 2917;
   public static final int GL_FOG_COLOR = 2918;
   public static final int GL_DEPTH_RANGE = 2928;
   public static final int GL_DEPTH_TEST = 2929;
   public static final int GL_DEPTH_WRITEMASK = 2930;
   public static final int GL_DEPTH_CLEAR_VALUE = 2931;
   public static final int GL_DEPTH_FUNC = 2932;
   public static final int GL_ACCUM_CLEAR_VALUE = 2944;
   public static final int GL_STENCIL_TEST = 2960;
   public static final int GL_STENCIL_CLEAR_VALUE = 2961;
   public static final int GL_STENCIL_FUNC = 2962;
   public static final int GL_STENCIL_VALUE_MASK = 2963;
   public static final int GL_STENCIL_FAIL = 2964;
   public static final int GL_STENCIL_PASS_DEPTH_FAIL = 2965;
   public static final int GL_STENCIL_PASS_DEPTH_PASS = 2966;
   public static final int GL_STENCIL_REF = 2967;
   public static final int GL_STENCIL_WRITEMASK = 2968;
   public static final int GL_MATRIX_MODE = 2976;
   public static final int GL_NORMALIZE = 2977;
   public static final int GL_VIEWPORT = 2978;
   public static final int GL_MODELVIEW_STACK_DEPTH = 2979;
   public static final int GL_PROJECTION_STACK_DEPTH = 2980;
   public static final int GL_TEXTURE_STACK_DEPTH = 2981;
   public static final int GL_MODELVIEW_MATRIX = 2982;
   public static final int GL_PROJECTION_MATRIX = 2983;
   public static final int GL_TEXTURE_MATRIX = 2984;
   public static final int GL_ATTRIB_STACK_DEPTH = 2992;
   public static final int GL_CLIENT_ATTRIB_STACK_DEPTH = 2993;
   public static final int GL_ALPHA_TEST = 3008;
   public static final int GL_ALPHA_TEST_FUNC = 3009;
   public static final int GL_ALPHA_TEST_REF = 3010;
   public static final int GL_DITHER = 3024;
   public static final int GL_BLEND_DST = 3040;
   public static final int GL_BLEND_SRC = 3041;
   public static final int GL_BLEND = 3042;
   public static final int GL_LOGIC_OP_MODE = 3056;
   public static final int GL_INDEX_LOGIC_OP = 3057;
   public static final int GL_COLOR_LOGIC_OP = 3058;
   public static final int GL_AUX_BUFFERS = 3072;
   public static final int GL_DRAW_BUFFER = 3073;
   public static final int GL_READ_BUFFER = 3074;
   public static final int GL_SCISSOR_BOX = 3088;
   public static final int GL_SCISSOR_TEST = 3089;
   public static final int GL_INDEX_CLEAR_VALUE = 3104;
   public static final int GL_INDEX_WRITEMASK = 3105;
   public static final int GL_COLOR_CLEAR_VALUE = 3106;
   public static final int GL_COLOR_WRITEMASK = 3107;
   public static final int GL_INDEX_MODE = 3120;
   public static final int GL_RGBA_MODE = 3121;
   public static final int GL_DOUBLEBUFFER = 3122;
   public static final int GL_STEREO = 3123;
   public static final int GL_RENDER_MODE = 3136;
   public static final int GL_PERSPECTIVE_CORRECTION_HINT = 3152;
   public static final int GL_POINT_SMOOTH_HINT = 3153;
   public static final int GL_LINE_SMOOTH_HINT = 3154;
   public static final int GL_POLYGON_SMOOTH_HINT = 3155;
   public static final int GL_FOG_HINT = 3156;
   public static final int GL_TEXTURE_GEN_S = 3168;
   public static final int GL_TEXTURE_GEN_T = 3169;
   public static final int GL_TEXTURE_GEN_R = 3170;
   public static final int GL_TEXTURE_GEN_Q = 3171;
   public static final int GL_PIXEL_MAP_I_TO_I = 3184;
   public static final int GL_PIXEL_MAP_S_TO_S = 3185;
   public static final int GL_PIXEL_MAP_I_TO_R = 3186;
   public static final int GL_PIXEL_MAP_I_TO_G = 3187;
   public static final int GL_PIXEL_MAP_I_TO_B = 3188;
   public static final int GL_PIXEL_MAP_I_TO_A = 3189;
   public static final int GL_PIXEL_MAP_R_TO_R = 3190;
   public static final int GL_PIXEL_MAP_G_TO_G = 3191;
   public static final int GL_PIXEL_MAP_B_TO_B = 3192;
   public static final int GL_PIXEL_MAP_A_TO_A = 3193;
   public static final int GL_PIXEL_MAP_I_TO_I_SIZE = 3248;
   public static final int GL_PIXEL_MAP_S_TO_S_SIZE = 3249;
   public static final int GL_PIXEL_MAP_I_TO_R_SIZE = 3250;
   public static final int GL_PIXEL_MAP_I_TO_G_SIZE = 3251;
   public static final int GL_PIXEL_MAP_I_TO_B_SIZE = 3252;
   public static final int GL_PIXEL_MAP_I_TO_A_SIZE = 3253;
   public static final int GL_PIXEL_MAP_R_TO_R_SIZE = 3254;
   public static final int GL_PIXEL_MAP_G_TO_G_SIZE = 3255;
   public static final int GL_PIXEL_MAP_B_TO_B_SIZE = 3256;
   public static final int GL_PIXEL_MAP_A_TO_A_SIZE = 3257;
   public static final int GL_UNPACK_SWAP_BYTES = 3312;
   public static final int GL_UNPACK_LSB_FIRST = 3313;
   public static final int GL_UNPACK_ROW_LENGTH = 3314;
   public static final int GL_UNPACK_SKIP_ROWS = 3315;
   public static final int GL_UNPACK_SKIP_PIXELS = 3316;
   public static final int GL_UNPACK_ALIGNMENT = 3317;
   public static final int GL_PACK_SWAP_BYTES = 3328;
   public static final int GL_PACK_LSB_FIRST = 3329;
   public static final int GL_PACK_ROW_LENGTH = 3330;
   public static final int GL_PACK_SKIP_ROWS = 3331;
   public static final int GL_PACK_SKIP_PIXELS = 3332;
   public static final int GL_PACK_ALIGNMENT = 3333;
   public static final int GL_MAP_COLOR = 3344;
   public static final int GL_MAP_STENCIL = 3345;
   public static final int GL_INDEX_SHIFT = 3346;
   public static final int GL_INDEX_OFFSET = 3347;
   public static final int GL_RED_SCALE = 3348;
   public static final int GL_RED_BIAS = 3349;
   public static final int GL_ZOOM_X = 3350;
   public static final int GL_ZOOM_Y = 3351;
   public static final int GL_GREEN_SCALE = 3352;
   public static final int GL_GREEN_BIAS = 3353;
   public static final int GL_BLUE_SCALE = 3354;
   public static final int GL_BLUE_BIAS = 3355;
   public static final int GL_ALPHA_SCALE = 3356;
   public static final int GL_ALPHA_BIAS = 3357;
   public static final int GL_DEPTH_SCALE = 3358;
   public static final int GL_DEPTH_BIAS = 3359;
   public static final int GL_MAX_EVAL_ORDER = 3376;
   public static final int GL_MAX_LIGHTS = 3377;
   public static final int GL_MAX_CLIP_PLANES = 3378;
   public static final int GL_MAX_TEXTURE_SIZE = 3379;
   public static final int GL_MAX_PIXEL_MAP_TABLE = 3380;
   public static final int GL_MAX_ATTRIB_STACK_DEPTH = 3381;
   public static final int GL_MAX_MODELVIEW_STACK_DEPTH = 3382;
   public static final int GL_MAX_NAME_STACK_DEPTH = 3383;
   public static final int GL_MAX_PROJECTION_STACK_DEPTH = 3384;
   public static final int GL_MAX_TEXTURE_STACK_DEPTH = 3385;
   public static final int GL_MAX_VIEWPORT_DIMS = 3386;
   public static final int GL_MAX_CLIENT_ATTRIB_STACK_DEPTH = 3387;
   public static final int GL_SUBPIXEL_BITS = 3408;
   public static final int GL_INDEX_BITS = 3409;
   public static final int GL_RED_BITS = 3410;
   public static final int GL_GREEN_BITS = 3411;
   public static final int GL_BLUE_BITS = 3412;
   public static final int GL_ALPHA_BITS = 3413;
   public static final int GL_DEPTH_BITS = 3414;
   public static final int GL_STENCIL_BITS = 3415;
   public static final int GL_ACCUM_RED_BITS = 3416;
   public static final int GL_ACCUM_GREEN_BITS = 3417;
   public static final int GL_ACCUM_BLUE_BITS = 3418;
   public static final int GL_ACCUM_ALPHA_BITS = 3419;
   public static final int GL_NAME_STACK_DEPTH = 3440;
   public static final int GL_AUTO_NORMAL = 3456;
   public static final int GL_MAP1_COLOR_4 = 3472;
   public static final int GL_MAP1_INDEX = 3473;
   public static final int GL_MAP1_NORMAL = 3474;
   public static final int GL_MAP1_TEXTURE_COORD_1 = 3475;
   public static final int GL_MAP1_TEXTURE_COORD_2 = 3476;
   public static final int GL_MAP1_TEXTURE_COORD_3 = 3477;
   public static final int GL_MAP1_TEXTURE_COORD_4 = 3478;
   public static final int GL_MAP1_VERTEX_3 = 3479;
   public static final int GL_MAP1_VERTEX_4 = 3480;
   public static final int GL_MAP2_COLOR_4 = 3504;
   public static final int GL_MAP2_INDEX = 3505;
   public static final int GL_MAP2_NORMAL = 3506;
   public static final int GL_MAP2_TEXTURE_COORD_1 = 3507;
   public static final int GL_MAP2_TEXTURE_COORD_2 = 3508;
   public static final int GL_MAP2_TEXTURE_COORD_3 = 3509;
   public static final int GL_MAP2_TEXTURE_COORD_4 = 3510;
   public static final int GL_MAP2_VERTEX_3 = 3511;
   public static final int GL_MAP2_VERTEX_4 = 3512;
   public static final int GL_MAP1_GRID_DOMAIN = 3536;
   public static final int GL_MAP1_GRID_SEGMENTS = 3537;
   public static final int GL_MAP2_GRID_DOMAIN = 3538;
   public static final int GL_MAP2_GRID_SEGMENTS = 3539;
   public static final int GL_TEXTURE_1D = 3552;
   public static final int GL_TEXTURE_2D = 3553;
   public static final int GL_FEEDBACK_BUFFER_POINTER = 3568;
   public static final int GL_FEEDBACK_BUFFER_SIZE = 3569;
   public static final int GL_FEEDBACK_BUFFER_TYPE = 3570;
   public static final int GL_SELECTION_BUFFER_POINTER = 3571;
   public static final int GL_SELECTION_BUFFER_SIZE = 3572;
   public static final int GL_TEXTURE_WIDTH = 4096;
   public static final int GL_TEXTURE_HEIGHT = 4097;
   public static final int GL_TEXTURE_INTERNAL_FORMAT = 4099;
   public static final int GL_TEXTURE_BORDER_COLOR = 4100;
   public static final int GL_TEXTURE_BORDER = 4101;
   public static final int GL_DONT_CARE = 4352;
   public static final int GL_FASTEST = 4353;
   public static final int GL_NICEST = 4354;
   public static final int GL_LIGHT0 = 16384;
   public static final int GL_LIGHT1 = 16385;
   public static final int GL_LIGHT2 = 16386;
   public static final int GL_LIGHT3 = 16387;
   public static final int GL_LIGHT4 = 16388;
   public static final int GL_LIGHT5 = 16389;
   public static final int GL_LIGHT6 = 16390;
   public static final int GL_LIGHT7 = 16391;
   public static final int GL_AMBIENT = 4608;
   public static final int GL_DIFFUSE = 4609;
   public static final int GL_SPECULAR = 4610;
   public static final int GL_POSITION = 4611;
   public static final int GL_SPOT_DIRECTION = 4612;
   public static final int GL_SPOT_EXPONENT = 4613;
   public static final int GL_SPOT_CUTOFF = 4614;
   public static final int GL_CONSTANT_ATTENUATION = 4615;
   public static final int GL_LINEAR_ATTENUATION = 4616;
   public static final int GL_QUADRATIC_ATTENUATION = 4617;
   public static final int GL_COMPILE = 4864;
   public static final int GL_COMPILE_AND_EXECUTE = 4865;
   public static final int GL_CLEAR = 5376;
   public static final int GL_AND = 5377;
   public static final int GL_AND_REVERSE = 5378;
   public static final int GL_COPY = 5379;
   public static final int GL_AND_INVERTED = 5380;
   public static final int GL_NOOP = 5381;
   public static final int GL_XOR = 5382;
   public static final int GL_OR = 5383;
   public static final int GL_NOR = 5384;
   public static final int GL_EQUIV = 5385;
   public static final int GL_INVERT = 5386;
   public static final int GL_OR_REVERSE = 5387;
   public static final int GL_COPY_INVERTED = 5388;
   public static final int GL_OR_INVERTED = 5389;
   public static final int GL_NAND = 5390;
   public static final int GL_SET = 5391;
   public static final int GL_EMISSION = 5632;
   public static final int GL_SHININESS = 5633;
   public static final int GL_AMBIENT_AND_DIFFUSE = 5634;
   public static final int GL_COLOR_INDEXES = 5635;
   public static final int GL_MODELVIEW = 5888;
   public static final int GL_PROJECTION = 5889;
   public static final int GL_TEXTURE = 5890;
   public static final int GL_COLOR = 6144;
   public static final int GL_DEPTH = 6145;
   public static final int GL_STENCIL = 6146;
   public static final int GL_COLOR_INDEX = 6400;
   public static final int GL_STENCIL_INDEX = 6401;
   public static final int GL_DEPTH_COMPONENT = 6402;
   public static final int GL_RED = 6403;
   public static final int GL_GREEN = 6404;
   public static final int GL_BLUE = 6405;
   public static final int GL_ALPHA = 6406;
   public static final int GL_RGB = 6407;
   public static final int GL_RGBA = 6408;
   public static final int GL_LUMINANCE = 6409;
   public static final int GL_LUMINANCE_ALPHA = 6410;
   public static final int GL_BITMAP = 6656;
   public static final int GL_POINT = 6912;
   public static final int GL_LINE = 6913;
   public static final int GL_FILL = 6914;
   public static final int GL_RENDER = 7168;
   public static final int GL_FEEDBACK = 7169;
   public static final int GL_SELECT = 7170;
   public static final int GL_FLAT = 7424;
   public static final int GL_SMOOTH = 7425;
   public static final int GL_KEEP = 7680;
   public static final int GL_REPLACE = 7681;
   public static final int GL_INCR = 7682;
   public static final int GL_DECR = 7683;
   public static final int GL_VENDOR = 7936;
   public static final int GL_RENDERER = 7937;
   public static final int GL_VERSION = 7938;
   public static final int GL_EXTENSIONS = 7939;
   public static final int GL_S = 8192;
   public static final int GL_T = 8193;
   public static final int GL_R = 8194;
   public static final int GL_Q = 8195;
   public static final int GL_MODULATE = 8448;
   public static final int GL_DECAL = 8449;
   public static final int GL_TEXTURE_ENV_MODE = 8704;
   public static final int GL_TEXTURE_ENV_COLOR = 8705;
   public static final int GL_TEXTURE_ENV = 8960;
   public static final int GL_EYE_LINEAR = 9216;
   public static final int GL_OBJECT_LINEAR = 9217;
   public static final int GL_SPHERE_MAP = 9218;
   public static final int GL_TEXTURE_GEN_MODE = 9472;
   public static final int GL_OBJECT_PLANE = 9473;
   public static final int GL_EYE_PLANE = 9474;
   public static final int GL_NEAREST = 9728;
   public static final int GL_LINEAR = 9729;
   public static final int GL_NEAREST_MIPMAP_NEAREST = 9984;
   public static final int GL_LINEAR_MIPMAP_NEAREST = 9985;
   public static final int GL_NEAREST_MIPMAP_LINEAR = 9986;
   public static final int GL_LINEAR_MIPMAP_LINEAR = 9987;
   public static final int GL_TEXTURE_MAG_FILTER = 10240;
   public static final int GL_TEXTURE_MIN_FILTER = 10241;
   public static final int GL_TEXTURE_WRAP_S = 10242;
   public static final int GL_TEXTURE_WRAP_T = 10243;
   public static final int GL_CLAMP = 10496;
   public static final int GL_REPEAT = 10497;
   public static final int GL_CLIENT_PIXEL_STORE_BIT = 1;
   public static final int GL_CLIENT_VERTEX_ARRAY_BIT = 2;
   public static final int GL_ALL_CLIENT_ATTRIB_BITS = -1;
   public static final int GL_POLYGON_OFFSET_FACTOR = 32824;
   public static final int GL_POLYGON_OFFSET_UNITS = 10752;
   public static final int GL_POLYGON_OFFSET_POINT = 10753;
   public static final int GL_POLYGON_OFFSET_LINE = 10754;
   public static final int GL_POLYGON_OFFSET_FILL = 32823;
   public static final int GL_ALPHA4 = 32827;
   public static final int GL_ALPHA8 = 32828;
   public static final int GL_ALPHA12 = 32829;
   public static final int GL_ALPHA16 = 32830;
   public static final int GL_LUMINANCE4 = 32831;
   public static final int GL_LUMINANCE8 = 32832;
   public static final int GL_LUMINANCE12 = 32833;
   public static final int GL_LUMINANCE16 = 32834;
   public static final int GL_LUMINANCE4_ALPHA4 = 32835;
   public static final int GL_LUMINANCE6_ALPHA2 = 32836;
   public static final int GL_LUMINANCE8_ALPHA8 = 32837;
   public static final int GL_LUMINANCE12_ALPHA4 = 32838;
   public static final int GL_LUMINANCE12_ALPHA12 = 32839;
   public static final int GL_LUMINANCE16_ALPHA16 = 32840;
   public static final int GL_INTENSITY = 32841;
   public static final int GL_INTENSITY4 = 32842;
   public static final int GL_INTENSITY8 = 32843;
   public static final int GL_INTENSITY12 = 32844;
   public static final int GL_INTENSITY16 = 32845;
   public static final int GL_R3_G3_B2 = 10768;
   public static final int GL_RGB4 = 32847;
   public static final int GL_RGB5 = 32848;
   public static final int GL_RGB8 = 32849;
   public static final int GL_RGB10 = 32850;
   public static final int GL_RGB12 = 32851;
   public static final int GL_RGB16 = 32852;
   public static final int GL_RGBA2 = 32853;
   public static final int GL_RGBA4 = 32854;
   public static final int GL_RGB5_A1 = 32855;
   public static final int GL_RGBA8 = 32856;
   public static final int GL_RGB10_A2 = 32857;
   public static final int GL_RGBA12 = 32858;
   public static final int GL_RGBA16 = 32859;
   public static final int GL_TEXTURE_RED_SIZE = 32860;
   public static final int GL_TEXTURE_GREEN_SIZE = 32861;
   public static final int GL_TEXTURE_BLUE_SIZE = 32862;
   public static final int GL_TEXTURE_ALPHA_SIZE = 32863;
   public static final int GL_TEXTURE_LUMINANCE_SIZE = 32864;
   public static final int GL_TEXTURE_INTENSITY_SIZE = 32865;
   public static final int GL_PROXY_TEXTURE_1D = 32867;
   public static final int GL_PROXY_TEXTURE_2D = 32868;
   public static final int GL_TEXTURE_PRIORITY = 32870;
   public static final int GL_TEXTURE_RESIDENT = 32871;
   public static final int GL_TEXTURE_BINDING_1D = 32872;
   public static final int GL_TEXTURE_BINDING_2D = 32873;
   public static final int GL_VERTEX_ARRAY = 32884;
   public static final int GL_NORMAL_ARRAY = 32885;
   public static final int GL_COLOR_ARRAY = 32886;
   public static final int GL_INDEX_ARRAY = 32887;
   public static final int GL_TEXTURE_COORD_ARRAY = 32888;
   public static final int GL_EDGE_FLAG_ARRAY = 32889;
   public static final int GL_VERTEX_ARRAY_SIZE = 32890;
   public static final int GL_VERTEX_ARRAY_TYPE = 32891;
   public static final int GL_VERTEX_ARRAY_STRIDE = 32892;
   public static final int GL_NORMAL_ARRAY_TYPE = 32894;
   public static final int GL_NORMAL_ARRAY_STRIDE = 32895;
   public static final int GL_COLOR_ARRAY_SIZE = 32897;
   public static final int GL_COLOR_ARRAY_TYPE = 32898;
   public static final int GL_COLOR_ARRAY_STRIDE = 32899;
   public static final int GL_INDEX_ARRAY_TYPE = 32901;
   public static final int GL_INDEX_ARRAY_STRIDE = 32902;
   public static final int GL_TEXTURE_COORD_ARRAY_SIZE = 32904;
   public static final int GL_TEXTURE_COORD_ARRAY_TYPE = 32905;
   public static final int GL_TEXTURE_COORD_ARRAY_STRIDE = 32906;
   public static final int GL_EDGE_FLAG_ARRAY_STRIDE = 32908;
   public static final int GL_VERTEX_ARRAY_POINTER = 32910;
   public static final int GL_NORMAL_ARRAY_POINTER = 32911;
   public static final int GL_COLOR_ARRAY_POINTER = 32912;
   public static final int GL_INDEX_ARRAY_POINTER = 32913;
   public static final int GL_TEXTURE_COORD_ARRAY_POINTER = 32914;
   public static final int GL_EDGE_FLAG_ARRAY_POINTER = 32915;
   public static final int GL_V2F = 10784;
   public static final int GL_V3F = 10785;
   public static final int GL_C4UB_V2F = 10786;
   public static final int GL_C4UB_V3F = 10787;
   public static final int GL_C3F_V3F = 10788;
   public static final int GL_N3F_V3F = 10789;
   public static final int GL_C4F_N3F_V3F = 10790;
   public static final int GL_T2F_V3F = 10791;
   public static final int GL_T4F_V4F = 10792;
   public static final int GL_T2F_C4UB_V3F = 10793;
   public static final int GL_T2F_C3F_V3F = 10794;
   public static final int GL_T2F_N3F_V3F = 10795;
   public static final int GL_T2F_C4F_N3F_V3F = 10796;
   public static final int GL_T4F_C4F_N3F_V4F = 10797;
   public static final int GL_LOGIC_OP = 3057;
   public static final int GL_TEXTURE_COMPONENTS = 4099;

   private GL11() {
   }

   public static void glAccum(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glAccum;
      BufferChecks.checkFunctionAddress(var3);
      nglAccum(var0, var1, var3);
   }

   static native void nglAccum(int var0, float var1, long var2);

   public static void glAlphaFunc(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glAlphaFunc;
      BufferChecks.checkFunctionAddress(var3);
      nglAlphaFunc(var0, var1, var3);
   }

   static native void nglAlphaFunc(int var0, float var1, long var2);

   public static void glClearColor(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glClearColor;
      BufferChecks.checkFunctionAddress(var5);
      nglClearColor(var0, var1, var2, var3, var5);
   }

   static native void nglClearColor(float var0, float var1, float var2, float var3, long var4);

   public static void glClearAccum(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glClearAccum;
      BufferChecks.checkFunctionAddress(var5);
      nglClearAccum(var0, var1, var2, var3, var5);
   }

   static native void nglClearAccum(float var0, float var1, float var2, float var3, long var4);

   public static void glClear(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glClear;
      BufferChecks.checkFunctionAddress(var2);
      nglClear(var0, var2);
   }

   static native void nglClear(int var0, long var1);

   public static void glCallLists(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCallLists;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglCallLists(var0.remaining(), 5121, MemoryUtil.getAddress(var0), var2);
   }

   public static void glCallLists(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCallLists;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglCallLists(var0.remaining(), 5125, MemoryUtil.getAddress(var0), var2);
   }

   public static void glCallLists(ShortBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCallLists;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglCallLists(var0.remaining(), 5123, MemoryUtil.getAddress(var0), var2);
   }

   static native void nglCallLists(int var0, int var1, long var2, long var4);

   public static void glCallList(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCallList;
      BufferChecks.checkFunctionAddress(var2);
      nglCallList(var0, var2);
   }

   static native void nglCallList(int var0, long var1);

   public static void glBlendFunc(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBlendFunc;
      BufferChecks.checkFunctionAddress(var3);
      nglBlendFunc(var0, var1, var3);
   }

   static native void nglBlendFunc(int var0, int var1, long var2);

   public static void glBitmap(int var0, int var1, float var2, float var3, float var4, float var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glBitmap;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      if (var6 != null) {
         BufferChecks.checkBuffer(var6, (var0 + 7) / 8 * var1);
      }

      nglBitmap(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddressSafe(var6), var8);
   }

   static native void nglBitmap(int var0, int var1, float var2, float var3, float var4, float var5, long var6, long var8);

   public static void glBitmap(int var0, int var1, float var2, float var3, float var4, float var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glBitmap;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOenabled(var8);
      nglBitmapBO(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglBitmapBO(int var0, int var1, float var2, float var3, float var4, float var5, long var6, long var8);

   public static void glBindTexture(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindTexture;
      BufferChecks.checkFunctionAddress(var3);
      nglBindTexture(var0, var1, var3);
   }

   static native void nglBindTexture(int var0, int var1, long var2);

   public static void glPrioritizeTextures(IntBuffer var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPrioritizeTextures;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkBuffer(var1, var0.remaining());
      nglPrioritizeTextures(var0.remaining(), MemoryUtil.getAddress(var0), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPrioritizeTextures(int var0, long var1, long var3, long var5);

   public static boolean glAreTexturesResident(IntBuffer var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glAreTexturesResident;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkBuffer(var1, var0.remaining());
      boolean var5 = nglAreTexturesResident(var0.remaining(), MemoryUtil.getAddress(var0), MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native boolean nglAreTexturesResident(int var0, long var1, long var3, long var5);

   public static void glBegin(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBegin;
      BufferChecks.checkFunctionAddress(var2);
      nglBegin(var0, var2);
   }

   static native void nglBegin(int var0, long var1);

   public static void glEnd() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glEnd;
      BufferChecks.checkFunctionAddress(var1);
      nglEnd(var1);
   }

   static native void nglEnd(long var0);

   public static void glArrayElement(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glArrayElement;
      BufferChecks.checkFunctionAddress(var2);
      nglArrayElement(var0, var2);
   }

   static native void nglArrayElement(int var0, long var1);

   public static void glClearDepth(double var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glClearDepth;
      BufferChecks.checkFunctionAddress(var3);
      nglClearDepth(var0, var3);
   }

   static native void nglClearDepth(double var0, long var2);

   public static void glDeleteLists(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDeleteLists;
      BufferChecks.checkFunctionAddress(var3);
      nglDeleteLists(var0, var1, var3);
   }

   static native void nglDeleteLists(int var0, int var1, long var2);

   public static void glDeleteTextures(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteTextures;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteTextures(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteTextures(int var0, long var1, long var3);

   public static void glDeleteTextures(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteTextures;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteTextures(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glCullFace(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCullFace;
      BufferChecks.checkFunctionAddress(var2);
      nglCullFace(var0, var2);
   }

   static native void nglCullFace(int var0, long var1);

   public static void glCopyTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCopyTexSubImage2D;
      BufferChecks.checkFunctionAddress(var9);
      nglCopyTexSubImage2D(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglCopyTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8);

   public static void glCopyTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glCopyTexSubImage1D;
      BufferChecks.checkFunctionAddress(var7);
      nglCopyTexSubImage1D(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglCopyTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glCopyTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glCopyTexImage2D;
      BufferChecks.checkFunctionAddress(var9);
      nglCopyTexImage2D(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglCopyTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8);

   public static void glCopyTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glCopyTexImage1D;
      BufferChecks.checkFunctionAddress(var8);
      nglCopyTexImage1D(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglCopyTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7);

   public static void glCopyPixels(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glCopyPixels;
      BufferChecks.checkFunctionAddress(var6);
      nglCopyPixels(var0, var1, var2, var3, var4, var6);
   }

   static native void nglCopyPixels(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glColorPointer(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glColorPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).GL11_glColorPointer_pointer = var2;
      }

      nglColorPointer(var0, 5130, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glColorPointer(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glColorPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).GL11_glColorPointer_pointer = var2;
      }

      nglColorPointer(var0, 5126, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glColorPointer(int var0, boolean var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glColorPointer;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).GL11_glColorPointer_pointer = var3;
      }

      nglColorPointer(var0, var1 ? 5121 : 5120, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglColorPointer(int var0, int var1, int var2, long var3, long var5);

   public static void glColorPointer(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glColorPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglColorPointerBO(var0, var1, var2, var3, var6);
   }

   static native void nglColorPointerBO(int var0, int var1, int var2, long var3, long var5);

   public static void glColorPointer(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glColorPointer;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).GL11_glColorPointer_pointer = var3;
      }

      nglColorPointer(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glColorMaterial(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glColorMaterial;
      BufferChecks.checkFunctionAddress(var3);
      nglColorMaterial(var0, var1, var3);
   }

   static native void nglColorMaterial(int var0, int var1, long var2);

   public static void glColorMask(boolean var0, boolean var1, boolean var2, boolean var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glColorMask;
      BufferChecks.checkFunctionAddress(var5);
      nglColorMask(var0, var1, var2, var3, var5);
   }

   static native void nglColorMask(boolean var0, boolean var1, boolean var2, boolean var3, long var4);

   public static void glColor3b(byte var0, byte var1, byte var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glColor3b;
      BufferChecks.checkFunctionAddress(var4);
      nglColor3b(var0, var1, var2, var4);
   }

   static native void nglColor3b(byte var0, byte var1, byte var2, long var3);

   public static void glColor3f(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glColor3f;
      BufferChecks.checkFunctionAddress(var4);
      nglColor3f(var0, var1, var2, var4);
   }

   static native void nglColor3f(float var0, float var1, float var2, long var3);

   public static void glColor3d(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glColor3d;
      BufferChecks.checkFunctionAddress(var7);
      nglColor3d(var0, var2, var4, var7);
   }

   static native void nglColor3d(double var0, double var2, double var4, long var6);

   public static void glColor3ub(byte var0, byte var1, byte var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glColor3ub;
      BufferChecks.checkFunctionAddress(var4);
      nglColor3ub(var0, var1, var2, var4);
   }

   static native void nglColor3ub(byte var0, byte var1, byte var2, long var3);

   public static void glColor4b(byte var0, byte var1, byte var2, byte var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glColor4b;
      BufferChecks.checkFunctionAddress(var5);
      nglColor4b(var0, var1, var2, var3, var5);
   }

   static native void nglColor4b(byte var0, byte var1, byte var2, byte var3, long var4);

   public static void glColor4f(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glColor4f;
      BufferChecks.checkFunctionAddress(var5);
      nglColor4f(var0, var1, var2, var3, var5);
   }

   static native void nglColor4f(float var0, float var1, float var2, float var3, long var4);

   public static void glColor4d(double var0, double var2, double var4, double var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glColor4d;
      BufferChecks.checkFunctionAddress(var9);
      nglColor4d(var0, var2, var4, var6, var9);
   }

   static native void nglColor4d(double var0, double var2, double var4, double var6, long var8);

   public static void glColor4ub(byte var0, byte var1, byte var2, byte var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glColor4ub;
      BufferChecks.checkFunctionAddress(var5);
      nglColor4ub(var0, var1, var2, var3, var5);
   }

   static native void nglColor4ub(byte var0, byte var1, byte var2, byte var3, long var4);

   public static void glClipPlane(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glClipPlane;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 4);
      nglClipPlane(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglClipPlane(int var0, long var1, long var3);

   public static void glClearStencil(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glClearStencil;
      BufferChecks.checkFunctionAddress(var2);
      nglClearStencil(var0, var2);
   }

   static native void nglClearStencil(int var0, long var1);

   public static void glEvalPoint1(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEvalPoint1;
      BufferChecks.checkFunctionAddress(var2);
      nglEvalPoint1(var0, var2);
   }

   static native void nglEvalPoint1(int var0, long var1);

   public static void glEvalPoint2(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEvalPoint2;
      BufferChecks.checkFunctionAddress(var3);
      nglEvalPoint2(var0, var1, var3);
   }

   static native void nglEvalPoint2(int var0, int var1, long var2);

   public static void glEvalMesh1(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glEvalMesh1;
      BufferChecks.checkFunctionAddress(var4);
      nglEvalMesh1(var0, var1, var2, var4);
   }

   static native void nglEvalMesh1(int var0, int var1, int var2, long var3);

   public static void glEvalMesh2(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glEvalMesh2;
      BufferChecks.checkFunctionAddress(var6);
      nglEvalMesh2(var0, var1, var2, var3, var4, var6);
   }

   static native void nglEvalMesh2(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glEvalCoord1f(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEvalCoord1f;
      BufferChecks.checkFunctionAddress(var2);
      nglEvalCoord1f(var0, var2);
   }

   static native void nglEvalCoord1f(float var0, long var1);

   public static void glEvalCoord1d(double var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEvalCoord1d;
      BufferChecks.checkFunctionAddress(var3);
      nglEvalCoord1d(var0, var3);
   }

   static native void nglEvalCoord1d(double var0, long var2);

   public static void glEvalCoord2f(float var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEvalCoord2f;
      BufferChecks.checkFunctionAddress(var3);
      nglEvalCoord2f(var0, var1, var3);
   }

   static native void nglEvalCoord2f(float var0, float var1, long var2);

   public static void glEvalCoord2d(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glEvalCoord2d;
      BufferChecks.checkFunctionAddress(var5);
      nglEvalCoord2d(var0, var2, var5);
   }

   static native void nglEvalCoord2d(double var0, double var2, long var4);

   public static void glEnableClientState(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEnableClientState;
      BufferChecks.checkFunctionAddress(var2);
      nglEnableClientState(var0, var2);
   }

   static native void nglEnableClientState(int var0, long var1);

   public static void glDisableClientState(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDisableClientState;
      BufferChecks.checkFunctionAddress(var2);
      nglDisableClientState(var0, var2);
   }

   static native void nglDisableClientState(int var0, long var1);

   public static void glEnable(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEnable;
      BufferChecks.checkFunctionAddress(var2);
      nglEnable(var0, var2);
   }

   static native void nglEnable(int var0, long var1);

   public static void glDisable(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDisable;
      BufferChecks.checkFunctionAddress(var2);
      nglDisable(var0, var2);
   }

   static native void nglDisable(int var0, long var1);

   public static void glEdgeFlagPointer(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glEdgeFlagPointer;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureArrayVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var2).GL11_glEdgeFlagPointer_pointer = var1;
      }

      nglEdgeFlagPointer(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglEdgeFlagPointer(int var0, long var1, long var3);

   public static void glEdgeFlagPointer(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glEdgeFlagPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOenabled(var3);
      nglEdgeFlagPointerBO(var0, var1, var4);
   }

   static native void nglEdgeFlagPointerBO(int var0, long var1, long var3);

   public static void glEdgeFlag(boolean var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEdgeFlag;
      BufferChecks.checkFunctionAddress(var2);
      nglEdgeFlag(var0, var2);
   }

   static native void nglEdgeFlag(boolean var0, long var1);

   public static void glDrawPixels(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawPixels;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureUnpackPBOdisabled(var5);
      BufferChecks.checkBuffer(var4, GLChecks.calculateImageStorage(var4, var2, var3, var0, var1, 1));
      nglDrawPixels(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glDrawPixels(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawPixels;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureUnpackPBOdisabled(var5);
      BufferChecks.checkBuffer(var4, GLChecks.calculateImageStorage(var4, var2, var3, var0, var1, 1));
      nglDrawPixels(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glDrawPixels(int var0, int var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawPixels;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureUnpackPBOdisabled(var5);
      BufferChecks.checkBuffer(var4, GLChecks.calculateImageStorage(var4, var2, var3, var0, var1, 1));
      nglDrawPixels(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglDrawPixels(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glDrawPixels(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glDrawPixels;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensureUnpackPBOenabled(var6);
      nglDrawPixelsBO(var0, var1, var2, var3, var4, var7);
   }

   static native void nglDrawPixelsBO(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glDrawElements(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDrawElements;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureElementVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      nglDrawElements(var0, var1.remaining(), 5121, MemoryUtil.getAddress(var1), var3);
   }

   public static void glDrawElements(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDrawElements;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureElementVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      nglDrawElements(var0, var1.remaining(), 5125, MemoryUtil.getAddress(var1), var3);
   }

   public static void glDrawElements(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glDrawElements;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureElementVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      nglDrawElements(var0, var1.remaining(), 5123, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglDrawElements(int var0, int var1, int var2, long var3, long var5);

   public static void glDrawElements(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawElements;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureElementVBOenabled(var5);
      nglDrawElementsBO(var0, var1, var2, var3, var6);
   }

   static native void nglDrawElementsBO(int var0, int var1, int var2, long var3, long var5);

   public static void glDrawElements(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawElements;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkBuffer(var3, var1);
      nglDrawElements(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glDrawBuffer(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDrawBuffer;
      BufferChecks.checkFunctionAddress(var2);
      nglDrawBuffer(var0, var2);
   }

   static native void nglDrawBuffer(int var0, long var1);

   public static void glDrawArrays(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawArrays;
      BufferChecks.checkFunctionAddress(var4);
      nglDrawArrays(var0, var1, var2, var4);
   }

   static native void nglDrawArrays(int var0, int var1, int var2, long var3);

   public static void glDepthRange(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDepthRange;
      BufferChecks.checkFunctionAddress(var5);
      nglDepthRange(var0, var2, var5);
   }

   static native void nglDepthRange(double var0, double var2, long var4);

   public static void glDepthMask(boolean var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDepthMask;
      BufferChecks.checkFunctionAddress(var2);
      nglDepthMask(var0, var2);
   }

   static native void nglDepthMask(boolean var0, long var1);

   public static void glDepthFunc(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDepthFunc;
      BufferChecks.checkFunctionAddress(var2);
      nglDepthFunc(var0, var2);
   }

   static native void nglDepthFunc(int var0, long var1);

   public static void glFeedbackBuffer(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFeedbackBuffer;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglFeedbackBuffer(var1.remaining(), var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglFeedbackBuffer(int var0, int var1, long var2, long var4);

   public static void glGetPixelMap(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPixelMapfv;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensurePackPBOdisabled(var2);
      BufferChecks.checkBuffer((FloatBuffer)var1, 256);
      nglGetPixelMapfv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetPixelMapfv(int var0, long var1, long var3);

   public static void glGetPixelMapfv(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPixelMapfv;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOenabled(var3);
      nglGetPixelMapfvBO(var0, var1, var4);
   }

   static native void nglGetPixelMapfvBO(int var0, long var1, long var3);

   public static void glGetPixelMapu(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPixelMapuiv;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensurePackPBOdisabled(var2);
      BufferChecks.checkBuffer((IntBuffer)var1, 256);
      nglGetPixelMapuiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetPixelMapuiv(int var0, long var1, long var3);

   public static void glGetPixelMapuiv(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPixelMapuiv;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOenabled(var3);
      nglGetPixelMapuivBO(var0, var1, var4);
   }

   static native void nglGetPixelMapuivBO(int var0, long var1, long var3);

   public static void glGetPixelMapu(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPixelMapusv;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensurePackPBOdisabled(var2);
      BufferChecks.checkBuffer((ShortBuffer)var1, 256);
      nglGetPixelMapusv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetPixelMapusv(int var0, long var1, long var3);

   public static void glGetPixelMapusv(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPixelMapusv;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensurePackPBOenabled(var3);
      nglGetPixelMapusvBO(var0, var1, var4);
   }

   static native void nglGetPixelMapusvBO(int var0, long var1, long var3);

   public static void glGetMaterial(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMaterialfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetMaterialfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMaterialfv(int var0, int var1, long var2, long var4);

   public static void glGetMaterial(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMaterialiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetMaterialiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMaterialiv(int var0, int var1, long var2, long var4);

   public static void glGetMap(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMapfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 256);
      nglGetMapfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMapfv(int var0, int var1, long var2, long var4);

   public static void glGetMap(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMapdv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 256);
      nglGetMapdv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMapdv(int var0, int var1, long var2, long var4);

   public static void glGetMap(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetMapiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 256);
      nglGetMapiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetMapiv(int var0, int var1, long var2, long var4);

   public static void glGetLight(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetLightfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetLightfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetLightfv(int var0, int var1, long var2, long var4);

   public static void glGetLight(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetLightiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetLightiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetLightiv(int var0, int var1, long var2, long var4);

   public static int glGetError() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGetError;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nglGetError(var1);
      return var3;
   }

   static native int nglGetError(long var0);

   public static void glGetClipPlane(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetClipPlane;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 4);
      nglGetClipPlane(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetClipPlane(int var0, long var1, long var3);

   public static void glGetBoolean(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetBooleanv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((ByteBuffer)var1, 16);
      nglGetBooleanv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetBooleanv(int var0, long var1, long var3);

   public static boolean glGetBoolean(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetBooleanv;
      BufferChecks.checkFunctionAddress(var2);
      ByteBuffer var4 = APIUtil.getBufferByte(var1, 1);
      nglGetBooleanv(var0, MemoryUtil.getAddress(var4), var2);
      return var4.get(0) == 1;
   }

   public static void glGetDouble(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetDoublev;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((DoubleBuffer)var1, 16);
      nglGetDoublev(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetDoublev(int var0, long var1, long var3);

   public static double glGetDouble(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetDoublev;
      BufferChecks.checkFunctionAddress(var2);
      DoubleBuffer var4 = APIUtil.getBufferDouble(var1);
      nglGetDoublev(var0, MemoryUtil.getAddress(var4), var2);
      return var4.get(0);
   }

   public static void glGetFloat(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetFloatv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 16);
      nglGetFloatv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetFloatv(int var0, long var1, long var3);

   public static float glGetFloat(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetFloatv;
      BufferChecks.checkFunctionAddress(var2);
      FloatBuffer var4 = APIUtil.getBufferFloat(var1);
      nglGetFloatv(var0, MemoryUtil.getAddress(var4), var2);
      return var4.get(0);
   }

   public static void glGetInteger(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetIntegerv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 16);
      nglGetIntegerv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetIntegerv(int var0, long var1, long var3);

   public static int glGetInteger(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetIntegerv;
      BufferChecks.checkFunctionAddress(var2);
      IntBuffer var4 = APIUtil.getBufferInt(var1);
      nglGetIntegerv(var0, MemoryUtil.getAddress(var4), var2);
      return var4.get(0);
   }

   public static void glGenTextures(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenTextures;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenTextures(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenTextures(int var0, long var1, long var3);

   public static int glGenTextures() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenTextures;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenTextures(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static int glGenLists(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenLists;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglGenLists(var0, var2);
      return var4;
   }

   static native int nglGenLists(int var0, long var1);

   public static void glFrustum(double var0, double var2, double var4, double var6, double var8, double var10) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glFrustum;
      BufferChecks.checkFunctionAddress(var13);
      nglFrustum(var0, var2, var4, var6, var8, var10, var13);
   }

   static native void nglFrustum(double var0, double var2, double var4, double var6, double var8, double var10, long var12);

   public static void glFrontFace(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFrontFace;
      BufferChecks.checkFunctionAddress(var2);
      nglFrontFace(var0, var2);
   }

   static native void nglFrontFace(int var0, long var1);

   public static void glFogf(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogf;
      BufferChecks.checkFunctionAddress(var3);
      nglFogf(var0, var1, var3);
   }

   static native void nglFogf(int var0, float var1, long var2);

   public static void glFogi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogi;
      BufferChecks.checkFunctionAddress(var3);
      nglFogi(var0, var1, var3);
   }

   static native void nglFogi(int var0, int var1, long var2);

   public static void glFog(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogfv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglFogfv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglFogfv(int var0, long var1, long var3);

   public static void glFog(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogiv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglFogiv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglFogiv(int var0, long var1, long var3);

   public static void glFlush() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glFlush;
      BufferChecks.checkFunctionAddress(var1);
      nglFlush(var1);
   }

   static native void nglFlush(long var0);

   public static void glFinish() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glFinish;
      BufferChecks.checkFunctionAddress(var1);
      nglFinish(var1);
   }

   static native void nglFinish(long var0);

   public static ByteBuffer glGetPointer(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetPointerv;
      BufferChecks.checkFunctionAddress(var4);
      ByteBuffer var6 = nglGetPointerv(var0, var1, var4);
      return LWJGLUtil.CHECKS && var6 == null ? null : var6.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetPointerv(int var0, long var1, long var3);

   public static boolean glIsEnabled(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsEnabled;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsEnabled(var0, var2);
      return var4;
   }

   static native boolean nglIsEnabled(int var0, long var1);

   public static void glInterleavedArrays(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glInterleavedArrays;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglInterleavedArrays(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glInterleavedArrays(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glInterleavedArrays;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglInterleavedArrays(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glInterleavedArrays(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glInterleavedArrays;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglInterleavedArrays(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glInterleavedArrays(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glInterleavedArrays;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglInterleavedArrays(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glInterleavedArrays(int var0, int var1, ShortBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glInterleavedArrays;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      nglInterleavedArrays(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglInterleavedArrays(int var0, int var1, long var2, long var4);

   public static void glInterleavedArrays(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glInterleavedArrays;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOenabled(var4);
      nglInterleavedArraysBO(var0, var1, var2, var5);
   }

   static native void nglInterleavedArraysBO(int var0, int var1, long var2, long var4);

   public static void glInitNames() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glInitNames;
      BufferChecks.checkFunctionAddress(var1);
      nglInitNames(var1);
   }

   static native void nglInitNames(long var0);

   public static void glHint(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glHint;
      BufferChecks.checkFunctionAddress(var3);
      nglHint(var0, var1, var3);
   }

   static native void nglHint(int var0, int var1, long var2);

   public static void glGetTexParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetTexParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexParameterfv(int var0, int var1, long var2, long var4);

   public static float glGetTexParameterf(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexParameterfv;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetTexParameterfv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetTexParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetTexParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexParameteriv(int var0, int var1, long var2, long var4);

   public static int glGetTexParameteri(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexParameteriv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetTexParameteriv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetTexLevelParameter(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTexLevelParameterfv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((FloatBuffer)var3, 4);
      nglGetTexLevelParameterfv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetTexLevelParameterfv(int var0, int var1, int var2, long var3, long var5);

   public static float glGetTexLevelParameterf(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexLevelParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      FloatBuffer var6 = APIUtil.getBufferFloat(var3);
      nglGetTexLevelParameterfv(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetTexLevelParameter(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetTexLevelParameteriv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetTexLevelParameteriv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetTexLevelParameteriv(int var0, int var1, int var2, long var3, long var5);

   public static int glGetTexLevelParameteri(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexLevelParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetTexLevelParameteriv(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGetTexImage(int var0, int var1, int var2, int var3, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetTexImage;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer(var4, GLChecks.calculateImageStorage(var4, var2, var3, 1, 1, 1));
      nglGetTexImage(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetTexImage(int var0, int var1, int var2, int var3, DoubleBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetTexImage;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer(var4, GLChecks.calculateImageStorage(var4, var2, var3, 1, 1, 1));
      nglGetTexImage(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetTexImage(int var0, int var1, int var2, int var3, FloatBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetTexImage;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer(var4, GLChecks.calculateImageStorage(var4, var2, var3, 1, 1, 1));
      nglGetTexImage(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetTexImage(int var0, int var1, int var2, int var3, IntBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetTexImage;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer(var4, GLChecks.calculateImageStorage(var4, var2, var3, 1, 1, 1));
      nglGetTexImage(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   public static void glGetTexImage(int var0, int var1, int var2, int var3, ShortBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glGetTexImage;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensurePackPBOdisabled(var5);
      BufferChecks.checkBuffer(var4, GLChecks.calculateImageStorage(var4, var2, var3, 1, 1, 1));
      nglGetTexImage(var0, var1, var2, var3, MemoryUtil.getAddress(var4), var6);
   }

   static native void nglGetTexImage(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetTexImage(int var0, int var1, int var2, int var3, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glGetTexImage;
      BufferChecks.checkFunctionAddress(var7);
      GLChecks.ensurePackPBOenabled(var6);
      nglGetTexImageBO(var0, var1, var2, var3, var4, var7);
   }

   static native void nglGetTexImageBO(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glGetTexGen(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexGeniv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetTexGeniv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexGeniv(int var0, int var1, long var2, long var4);

   public static int glGetTexGeni(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexGeniv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetTexGeniv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetTexGen(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexGenfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetTexGenfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexGenfv(int var0, int var1, long var2, long var4);

   public static float glGetTexGenf(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexGenfv;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetTexGenfv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetTexGen(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexGendv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglGetTexGendv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexGendv(int var0, int var1, long var2, long var4);

   public static double glGetTexGend(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexGendv;
      BufferChecks.checkFunctionAddress(var3);
      DoubleBuffer var5 = APIUtil.getBufferDouble(var2);
      nglGetTexGendv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetTexEnv(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexEnviv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetTexEnviv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexEnviv(int var0, int var1, long var2, long var4);

   public static int glGetTexEnvi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexEnviv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetTexEnviv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetTexEnv(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetTexEnvfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetTexEnvfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetTexEnvfv(int var0, int var1, long var2, long var4);

   public static float glGetTexEnvf(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetTexEnvfv;
      BufferChecks.checkFunctionAddress(var3);
      FloatBuffer var5 = APIUtil.getBufferFloat(var2);
      nglGetTexEnvfv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static String glGetString(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetString;
      BufferChecks.checkFunctionAddress(var2);
      String var4 = nglGetString(var0, var2);
      return var4;
   }

   static native String nglGetString(int var0, long var1);

   public static void glGetPolygonStipple(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetPolygonStipple;
      BufferChecks.checkFunctionAddress(var2);
      GLChecks.ensurePackPBOdisabled(var1);
      BufferChecks.checkBuffer((ByteBuffer)var0, 128);
      nglGetPolygonStipple(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGetPolygonStipple(long var0, long var2);

   public static void glGetPolygonStipple(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetPolygonStipple;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensurePackPBOenabled(var2);
      nglGetPolygonStippleBO(var0, var3);
   }

   static native void nglGetPolygonStippleBO(long var0, long var2);

   public static boolean glIsList(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsList;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsList(var0, var2);
      return var4;
   }

   static native boolean nglIsList(int var0, long var1);

   public static void glMaterialf(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMaterialf;
      BufferChecks.checkFunctionAddress(var4);
      nglMaterialf(var0, var1, var2, var4);
   }

   static native void nglMaterialf(int var0, int var1, float var2, long var3);

   public static void glMateriali(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMateriali;
      BufferChecks.checkFunctionAddress(var4);
      nglMateriali(var0, var1, var2, var4);
   }

   static native void nglMateriali(int var0, int var1, int var2, long var3);

   public static void glMaterial(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMaterialfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglMaterialfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglMaterialfv(int var0, int var1, long var2, long var4);

   public static void glMaterial(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMaterialiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglMaterialiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglMaterialiv(int var0, int var1, long var2, long var4);

   public static void glMapGrid1f(int var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMapGrid1f;
      BufferChecks.checkFunctionAddress(var4);
      nglMapGrid1f(var0, var1, var2, var4);
   }

   static native void nglMapGrid1f(int var0, float var1, float var2, long var3);

   public static void glMapGrid1d(int var0, double var1, double var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMapGrid1d;
      BufferChecks.checkFunctionAddress(var6);
      nglMapGrid1d(var0, var1, var3, var6);
   }

   static native void nglMapGrid1d(int var0, double var1, double var3, long var5);

   public static void glMapGrid2f(int var0, float var1, float var2, int var3, float var4, float var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glMapGrid2f;
      BufferChecks.checkFunctionAddress(var7);
      nglMapGrid2f(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglMapGrid2f(int var0, float var1, float var2, int var3, float var4, float var5, long var6);

   public static void glMapGrid2d(int var0, double var1, double var3, int var5, double var6, double var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMapGrid2d;
      BufferChecks.checkFunctionAddress(var11);
      nglMapGrid2d(var0, var1, var3, var5, var6, var8, var11);
   }

   static native void nglMapGrid2d(int var0, double var1, double var3, int var5, double var6, double var8, long var10);

   public static void glMap2f(int var0, float var1, float var2, int var3, int var4, float var5, float var6, int var7, int var8, FloatBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glMap2f;
      BufferChecks.checkFunctionAddress(var11);
      BufferChecks.checkDirect(var9);
      nglMap2f(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddress(var9), var11);
   }

   static native void nglMap2f(int var0, float var1, float var2, int var3, int var4, float var5, float var6, int var7, int var8, long var9, long var11);

   public static void glMap2d(int var0, double var1, double var3, int var5, int var6, double var7, double var9, int var11, int var12, DoubleBuffer var13) {
      ContextCapabilities var14 = GLContext.getCapabilities();
      long var15 = var14.glMap2d;
      BufferChecks.checkFunctionAddress(var15);
      BufferChecks.checkDirect(var13);
      nglMap2d(var0, var1, var3, var5, var6, var7, var9, var11, var12, MemoryUtil.getAddress(var13), var15);
   }

   static native void nglMap2d(int var0, double var1, double var3, int var5, int var6, double var7, double var9, int var11, int var12, long var13, long var15);

   public static void glMap1f(int var0, float var1, float var2, int var3, int var4, FloatBuffer var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glMap1f;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkDirect(var5);
      nglMap1f(var0, var1, var2, var3, var4, MemoryUtil.getAddress(var5), var7);
   }

   static native void nglMap1f(int var0, float var1, float var2, int var3, int var4, long var5, long var7);

   public static void glMap1d(int var0, double var1, double var3, int var5, int var6, DoubleBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glMap1d;
      BufferChecks.checkFunctionAddress(var9);
      BufferChecks.checkDirect(var7);
      nglMap1d(var0, var1, var3, var5, var6, MemoryUtil.getAddress(var7), var9);
   }

   static native void nglMap1d(int var0, double var1, double var3, int var5, int var6, long var7, long var9);

   public static void glLogicOp(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLogicOp;
      BufferChecks.checkFunctionAddress(var2);
      nglLogicOp(var0, var2);
   }

   static native void nglLogicOp(int var0, long var1);

   public static void glLoadName(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLoadName;
      BufferChecks.checkFunctionAddress(var2);
      nglLoadName(var0, var2);
   }

   static native void nglLoadName(int var0, long var1);

   public static void glLoadMatrix(FloatBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLoadMatrixf;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((FloatBuffer)var0, 16);
      nglLoadMatrixf(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglLoadMatrixf(long var0, long var2);

   public static void glLoadMatrix(DoubleBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLoadMatrixd;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((DoubleBuffer)var0, 16);
      nglLoadMatrixd(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglLoadMatrixd(long var0, long var2);

   public static void glLoadIdentity() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glLoadIdentity;
      BufferChecks.checkFunctionAddress(var1);
      nglLoadIdentity(var1);
   }

   static native void nglLoadIdentity(long var0);

   public static void glListBase(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glListBase;
      BufferChecks.checkFunctionAddress(var2);
      nglListBase(var0, var2);
   }

   static native void nglListBase(int var0, long var1);

   public static void glLineWidth(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glLineWidth;
      BufferChecks.checkFunctionAddress(var2);
      nglLineWidth(var0, var2);
   }

   static native void nglLineWidth(float var0, long var1);

   public static void glLineStipple(int var0, short var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glLineStipple;
      BufferChecks.checkFunctionAddress(var3);
      nglLineStipple(var0, var1, var3);
   }

   static native void nglLineStipple(int var0, short var1, long var2);

   public static void glLightModelf(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glLightModelf;
      BufferChecks.checkFunctionAddress(var3);
      nglLightModelf(var0, var1, var3);
   }

   static native void nglLightModelf(int var0, float var1, long var2);

   public static void glLightModeli(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glLightModeli;
      BufferChecks.checkFunctionAddress(var3);
      nglLightModeli(var0, var1, var3);
   }

   static native void nglLightModeli(int var0, int var1, long var2);

   public static void glLightModel(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glLightModelfv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((FloatBuffer)var1, 4);
      nglLightModelfv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglLightModelfv(int var0, long var1, long var3);

   public static void glLightModel(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glLightModeliv;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((IntBuffer)var1, 4);
      nglLightModeliv(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglLightModeliv(int var0, long var1, long var3);

   public static void glLightf(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glLightf;
      BufferChecks.checkFunctionAddress(var4);
      nglLightf(var0, var1, var2, var4);
   }

   static native void nglLightf(int var0, int var1, float var2, long var3);

   public static void glLighti(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glLighti;
      BufferChecks.checkFunctionAddress(var4);
      nglLighti(var0, var1, var2, var4);
   }

   static native void nglLighti(int var0, int var1, int var2, long var3);

   public static void glLight(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glLightfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglLightfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglLightfv(int var0, int var1, long var2, long var4);

   public static void glLight(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glLightiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglLightiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglLightiv(int var0, int var1, long var2, long var4);

   public static boolean glIsTexture(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsTexture;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsTexture(var0, var2);
      return var4;
   }

   static native boolean nglIsTexture(int var0, long var1);

   public static void glMatrixMode(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMatrixMode;
      BufferChecks.checkFunctionAddress(var2);
      nglMatrixMode(var0, var2);
   }

   static native void nglMatrixMode(int var0, long var1);

   public static void glPolygonStipple(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPolygonStipple;
      BufferChecks.checkFunctionAddress(var2);
      GLChecks.ensureUnpackPBOdisabled(var1);
      BufferChecks.checkBuffer((ByteBuffer)var0, 128);
      nglPolygonStipple(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglPolygonStipple(long var0, long var2);

   public static void glPolygonStipple(long var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPolygonStipple;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureUnpackPBOenabled(var2);
      nglPolygonStippleBO(var0, var3);
   }

   static native void nglPolygonStippleBO(long var0, long var2);

   public static void glPolygonOffset(float var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPolygonOffset;
      BufferChecks.checkFunctionAddress(var3);
      nglPolygonOffset(var0, var1, var3);
   }

   static native void nglPolygonOffset(float var0, float var1, long var2);

   public static void glPolygonMode(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPolygonMode;
      BufferChecks.checkFunctionAddress(var3);
      nglPolygonMode(var0, var1, var3);
   }

   static native void nglPolygonMode(int var0, int var1, long var2);

   public static void glPointSize(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPointSize;
      BufferChecks.checkFunctionAddress(var2);
      nglPointSize(var0, var2);
   }

   static native void nglPointSize(float var0, long var1);

   public static void glPixelZoom(float var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelZoom;
      BufferChecks.checkFunctionAddress(var3);
      nglPixelZoom(var0, var1, var3);
   }

   static native void nglPixelZoom(float var0, float var1, long var2);

   public static void glPixelTransferf(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelTransferf;
      BufferChecks.checkFunctionAddress(var3);
      nglPixelTransferf(var0, var1, var3);
   }

   static native void nglPixelTransferf(int var0, float var1, long var2);

   public static void glPixelTransferi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelTransferi;
      BufferChecks.checkFunctionAddress(var3);
      nglPixelTransferi(var0, var1, var3);
   }

   static native void nglPixelTransferi(int var0, int var1, long var2);

   public static void glPixelStoref(int var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelStoref;
      BufferChecks.checkFunctionAddress(var3);
      nglPixelStoref(var0, var1, var3);
   }

   static native void nglPixelStoref(int var0, float var1, long var2);

   public static void glPixelStorei(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelStorei;
      BufferChecks.checkFunctionAddress(var3);
      nglPixelStorei(var0, var1, var3);
   }

   static native void nglPixelStorei(int var0, int var1, long var2);

   public static void glPixelMap(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelMapfv;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureUnpackPBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      nglPixelMapfv(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPixelMapfv(int var0, int var1, long var2, long var4);

   public static void glPixelMapfv(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glPixelMapfv;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureUnpackPBOenabled(var4);
      nglPixelMapfvBO(var0, var1, var2, var5);
   }

   static native void nglPixelMapfvBO(int var0, int var1, long var2, long var4);

   public static void glPixelMapu(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelMapuiv;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureUnpackPBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      nglPixelMapuiv(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPixelMapuiv(int var0, int var1, long var2, long var4);

   public static void glPixelMapuiv(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glPixelMapuiv;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureUnpackPBOenabled(var4);
      nglPixelMapuivBO(var0, var1, var2, var5);
   }

   static native void nglPixelMapuivBO(int var0, int var1, long var2, long var4);

   public static void glPixelMapu(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelMapusv;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureUnpackPBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      nglPixelMapusv(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPixelMapusv(int var0, int var1, long var2, long var4);

   public static void glPixelMapusv(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glPixelMapusv;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureUnpackPBOenabled(var4);
      nglPixelMapusvBO(var0, var1, var2, var5);
   }

   static native void nglPixelMapusvBO(int var0, int var1, long var2, long var4);

   public static void glPassThrough(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPassThrough;
      BufferChecks.checkFunctionAddress(var2);
      nglPassThrough(var0, var2);
   }

   static native void nglPassThrough(float var0, long var1);

   public static void glOrtho(double var0, double var2, double var4, double var6, double var8, double var10) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glOrtho;
      BufferChecks.checkFunctionAddress(var13);
      nglOrtho(var0, var2, var4, var6, var8, var10, var13);
   }

   static native void nglOrtho(double var0, double var2, double var4, double var6, double var8, double var10, long var12);

   public static void glNormalPointer(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNormalPointer;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureArrayVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var2).GL11_glNormalPointer_pointer = var1;
      }

      nglNormalPointer(5120, var0, MemoryUtil.getAddress(var1), var3);
   }

   public static void glNormalPointer(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNormalPointer;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureArrayVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var2).GL11_glNormalPointer_pointer = var1;
      }

      nglNormalPointer(5130, var0, MemoryUtil.getAddress(var1), var3);
   }

   public static void glNormalPointer(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNormalPointer;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureArrayVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var2).GL11_glNormalPointer_pointer = var1;
      }

      nglNormalPointer(5126, var0, MemoryUtil.getAddress(var1), var3);
   }

   public static void glNormalPointer(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNormalPointer;
      BufferChecks.checkFunctionAddress(var3);
      GLChecks.ensureArrayVBOdisabled(var2);
      BufferChecks.checkDirect(var1);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var2).GL11_glNormalPointer_pointer = var1;
      }

      nglNormalPointer(5124, var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglNormalPointer(int var0, int var1, long var2, long var4);

   public static void glNormalPointer(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glNormalPointer;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOenabled(var4);
      nglNormalPointerBO(var0, var1, var2, var5);
   }

   static native void nglNormalPointerBO(int var0, int var1, long var2, long var4);

   public static void glNormalPointer(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNormalPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).GL11_glNormalPointer_pointer = var2;
      }

      nglNormalPointer(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glNormal3b(byte var0, byte var1, byte var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNormal3b;
      BufferChecks.checkFunctionAddress(var4);
      nglNormal3b(var0, var1, var2, var4);
   }

   static native void nglNormal3b(byte var0, byte var1, byte var2, long var3);

   public static void glNormal3f(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNormal3f;
      BufferChecks.checkFunctionAddress(var4);
      nglNormal3f(var0, var1, var2, var4);
   }

   static native void nglNormal3f(float var0, float var1, float var2, long var3);

   public static void glNormal3d(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glNormal3d;
      BufferChecks.checkFunctionAddress(var7);
      nglNormal3d(var0, var2, var4, var7);
   }

   static native void nglNormal3d(double var0, double var2, double var4, long var6);

   public static void glNormal3i(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNormal3i;
      BufferChecks.checkFunctionAddress(var4);
      nglNormal3i(var0, var1, var2, var4);
   }

   static native void nglNormal3i(int var0, int var1, int var2, long var3);

   public static void glNewList(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNewList;
      BufferChecks.checkFunctionAddress(var3);
      nglNewList(var0, var1, var3);
   }

   static native void nglNewList(int var0, int var1, long var2);

   public static void glEndList() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glEndList;
      BufferChecks.checkFunctionAddress(var1);
      nglEndList(var1);
   }

   static native void nglEndList(long var0);

   public static void glMultMatrix(FloatBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMultMatrixf;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((FloatBuffer)var0, 16);
      nglMultMatrixf(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglMultMatrixf(long var0, long var2);

   public static void glMultMatrix(DoubleBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMultMatrixd;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkBuffer((DoubleBuffer)var0, 16);
      nglMultMatrixd(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglMultMatrixd(long var0, long var2);

   public static void glShadeModel(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glShadeModel;
      BufferChecks.checkFunctionAddress(var2);
      nglShadeModel(var0, var2);
   }

   static native void nglShadeModel(int var0, long var1);

   public static void glSelectBuffer(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glSelectBuffer;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglSelectBuffer(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglSelectBuffer(int var0, long var1, long var3);

   public static void glScissor(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glScissor;
      BufferChecks.checkFunctionAddress(var5);
      nglScissor(var0, var1, var2, var3, var5);
   }

   static native void nglScissor(int var0, int var1, int var2, int var3, long var4);

   public static void glScalef(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glScalef;
      BufferChecks.checkFunctionAddress(var4);
      nglScalef(var0, var1, var2, var4);
   }

   static native void nglScalef(float var0, float var1, float var2, long var3);

   public static void glScaled(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glScaled;
      BufferChecks.checkFunctionAddress(var7);
      nglScaled(var0, var2, var4, var7);
   }

   static native void nglScaled(double var0, double var2, double var4, long var6);

   public static void glRotatef(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glRotatef;
      BufferChecks.checkFunctionAddress(var5);
      nglRotatef(var0, var1, var2, var3, var5);
   }

   static native void nglRotatef(float var0, float var1, float var2, float var3, long var4);

   public static void glRotated(double var0, double var2, double var4, double var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glRotated;
      BufferChecks.checkFunctionAddress(var9);
      nglRotated(var0, var2, var4, var6, var9);
   }

   static native void nglRotated(double var0, double var2, double var4, double var6, long var8);

   public static int glRenderMode(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glRenderMode;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglRenderMode(var0, var2);
      return var4;
   }

   static native int nglRenderMode(int var0, long var1);

   public static void glRectf(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glRectf;
      BufferChecks.checkFunctionAddress(var5);
      nglRectf(var0, var1, var2, var3, var5);
   }

   static native void nglRectf(float var0, float var1, float var2, float var3, long var4);

   public static void glRectd(double var0, double var2, double var4, double var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glRectd;
      BufferChecks.checkFunctionAddress(var9);
      nglRectd(var0, var2, var4, var6, var9);
   }

   static native void nglRectd(double var0, double var2, double var4, double var6, long var8);

   public static void glRecti(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glRecti;
      BufferChecks.checkFunctionAddress(var5);
      nglRecti(var0, var1, var2, var3, var5);
   }

   static native void nglRecti(int var0, int var1, int var2, int var3, long var4);

   public static void glReadPixels(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadPixels;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var2, var3, 1));
      nglReadPixels(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glReadPixels(int var0, int var1, int var2, int var3, int var4, int var5, DoubleBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadPixels;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var2, var3, 1));
      nglReadPixels(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glReadPixels(int var0, int var1, int var2, int var3, int var4, int var5, FloatBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadPixels;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var2, var3, 1));
      nglReadPixels(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glReadPixels(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadPixels;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var2, var3, 1));
      nglReadPixels(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glReadPixels(int var0, int var1, int var2, int var3, int var4, int var5, ShortBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glReadPixels;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensurePackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var2, var3, 1));
      nglReadPixels(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   static native void nglReadPixels(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glReadPixels(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glReadPixels;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensurePackPBOenabled(var8);
      nglReadPixelsBO(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglReadPixelsBO(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glReadBuffer(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glReadBuffer;
      BufferChecks.checkFunctionAddress(var2);
      nglReadBuffer(var0, var2);
   }

   static native void nglReadBuffer(int var0, long var1);

   public static void glRasterPos2f(float var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glRasterPos2f;
      BufferChecks.checkFunctionAddress(var3);
      nglRasterPos2f(var0, var1, var3);
   }

   static native void nglRasterPos2f(float var0, float var1, long var2);

   public static void glRasterPos2d(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glRasterPos2d;
      BufferChecks.checkFunctionAddress(var5);
      nglRasterPos2d(var0, var2, var5);
   }

   static native void nglRasterPos2d(double var0, double var2, long var4);

   public static void glRasterPos2i(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glRasterPos2i;
      BufferChecks.checkFunctionAddress(var3);
      nglRasterPos2i(var0, var1, var3);
   }

   static native void nglRasterPos2i(int var0, int var1, long var2);

   public static void glRasterPos3f(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glRasterPos3f;
      BufferChecks.checkFunctionAddress(var4);
      nglRasterPos3f(var0, var1, var2, var4);
   }

   static native void nglRasterPos3f(float var0, float var1, float var2, long var3);

   public static void glRasterPos3d(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glRasterPos3d;
      BufferChecks.checkFunctionAddress(var7);
      nglRasterPos3d(var0, var2, var4, var7);
   }

   static native void nglRasterPos3d(double var0, double var2, double var4, long var6);

   public static void glRasterPos3i(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glRasterPos3i;
      BufferChecks.checkFunctionAddress(var4);
      nglRasterPos3i(var0, var1, var2, var4);
   }

   static native void nglRasterPos3i(int var0, int var1, int var2, long var3);

   public static void glRasterPos4f(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glRasterPos4f;
      BufferChecks.checkFunctionAddress(var5);
      nglRasterPos4f(var0, var1, var2, var3, var5);
   }

   static native void nglRasterPos4f(float var0, float var1, float var2, float var3, long var4);

   public static void glRasterPos4d(double var0, double var2, double var4, double var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glRasterPos4d;
      BufferChecks.checkFunctionAddress(var9);
      nglRasterPos4d(var0, var2, var4, var6, var9);
   }

   static native void nglRasterPos4d(double var0, double var2, double var4, double var6, long var8);

   public static void glRasterPos4i(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glRasterPos4i;
      BufferChecks.checkFunctionAddress(var5);
      nglRasterPos4i(var0, var1, var2, var3, var5);
   }

   static native void nglRasterPos4i(int var0, int var1, int var2, int var3, long var4);

   public static void glPushName(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPushName;
      BufferChecks.checkFunctionAddress(var2);
      nglPushName(var0, var2);
   }

   static native void nglPushName(int var0, long var1);

   public static void glPopName() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glPopName;
      BufferChecks.checkFunctionAddress(var1);
      nglPopName(var1);
   }

   static native void nglPopName(long var0);

   public static void glPushMatrix() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glPushMatrix;
      BufferChecks.checkFunctionAddress(var1);
      nglPushMatrix(var1);
   }

   static native void nglPushMatrix(long var0);

   public static void glPopMatrix() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glPopMatrix;
      BufferChecks.checkFunctionAddress(var1);
      nglPopMatrix(var1);
   }

   static native void nglPopMatrix(long var0);

   public static void glPushClientAttrib(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPushClientAttrib;
      BufferChecks.checkFunctionAddress(var2);
      StateTracker.pushAttrib(var1, var0);
      nglPushClientAttrib(var0, var2);
   }

   static native void nglPushClientAttrib(int var0, long var1);

   public static void glPopClientAttrib() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glPopClientAttrib;
      BufferChecks.checkFunctionAddress(var1);
      StateTracker.popAttrib(var0);
      nglPopClientAttrib(var1);
   }

   static native void nglPopClientAttrib(long var0);

   public static void glPushAttrib(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPushAttrib;
      BufferChecks.checkFunctionAddress(var2);
      nglPushAttrib(var0, var2);
   }

   static native void nglPushAttrib(int var0, long var1);

   public static void glPopAttrib() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glPopAttrib;
      BufferChecks.checkFunctionAddress(var1);
      nglPopAttrib(var1);
   }

   static native void nglPopAttrib(long var0);

   public static void glStencilFunc(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glStencilFunc;
      BufferChecks.checkFunctionAddress(var4);
      nglStencilFunc(var0, var1, var2, var4);
   }

   static native void nglStencilFunc(int var0, int var1, int var2, long var3);

   public static void glVertexPointer(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).GL11_glVertexPointer_pointer = var2;
      }

      nglVertexPointer(var0, 5130, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glVertexPointer(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).GL11_glVertexPointer_pointer = var2;
      }

      nglVertexPointer(var0, 5126, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glVertexPointer(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).GL11_glVertexPointer_pointer = var2;
      }

      nglVertexPointer(var0, 5124, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glVertexPointer(int var0, int var1, ShortBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).GL11_glVertexPointer_pointer = var2;
      }

      nglVertexPointer(var0, 5122, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglVertexPointer(int var0, int var1, int var2, long var3, long var5);

   public static void glVertexPointer(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglVertexPointerBO(var0, var1, var2, var3, var6);
   }

   static native void nglVertexPointerBO(int var0, int var1, int var2, long var3, long var5);

   public static void glVertexPointer(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexPointer;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).GL11_glVertexPointer_pointer = var3;
      }

      nglVertexPointer(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glVertex2f(float var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertex2f;
      BufferChecks.checkFunctionAddress(var3);
      nglVertex2f(var0, var1, var3);
   }

   static native void nglVertex2f(float var0, float var1, long var2);

   public static void glVertex2d(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertex2d;
      BufferChecks.checkFunctionAddress(var5);
      nglVertex2d(var0, var2, var5);
   }

   static native void nglVertex2d(double var0, double var2, long var4);

   public static void glVertex2i(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertex2i;
      BufferChecks.checkFunctionAddress(var3);
      nglVertex2i(var0, var1, var3);
   }

   static native void nglVertex2i(int var0, int var1, long var2);

   public static void glVertex3f(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertex3f;
      BufferChecks.checkFunctionAddress(var4);
      nglVertex3f(var0, var1, var2, var4);
   }

   static native void nglVertex3f(float var0, float var1, float var2, long var3);

   public static void glVertex3d(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glVertex3d;
      BufferChecks.checkFunctionAddress(var7);
      nglVertex3d(var0, var2, var4, var7);
   }

   static native void nglVertex3d(double var0, double var2, double var4, long var6);

   public static void glVertex3i(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertex3i;
      BufferChecks.checkFunctionAddress(var4);
      nglVertex3i(var0, var1, var2, var4);
   }

   static native void nglVertex3i(int var0, int var1, int var2, long var3);

   public static void glVertex4f(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertex4f;
      BufferChecks.checkFunctionAddress(var5);
      nglVertex4f(var0, var1, var2, var3, var5);
   }

   static native void nglVertex4f(float var0, float var1, float var2, float var3, long var4);

   public static void glVertex4d(double var0, double var2, double var4, double var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glVertex4d;
      BufferChecks.checkFunctionAddress(var9);
      nglVertex4d(var0, var2, var4, var6, var9);
   }

   static native void nglVertex4d(double var0, double var2, double var4, double var6, long var8);

   public static void glVertex4i(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertex4i;
      BufferChecks.checkFunctionAddress(var5);
      nglVertex4i(var0, var1, var2, var3, var5);
   }

   static native void nglVertex4i(int var0, int var1, int var2, int var3, long var4);

   public static void glTranslatef(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTranslatef;
      BufferChecks.checkFunctionAddress(var4);
      nglTranslatef(var0, var1, var2, var4);
   }

   static native void nglTranslatef(float var0, float var1, float var2, long var3);

   public static void glTranslated(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glTranslated;
      BufferChecks.checkFunctionAddress(var7);
      nglTranslated(var0, var2, var4, var7);
   }

   static native void nglTranslated(double var0, double var2, double var4, long var6);

   public static void glTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ByteBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTexImage1D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      if (var7 != null) {
         BufferChecks.checkBuffer(var7, GLChecks.calculateTexImage1DStorage(var7, var5, var6, var3));
      }

      nglTexImage1D(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddressSafe(var7), var9);
   }

   public static void glTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, DoubleBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTexImage1D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      if (var7 != null) {
         BufferChecks.checkBuffer(var7, GLChecks.calculateTexImage1DStorage(var7, var5, var6, var3));
      }

      nglTexImage1D(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddressSafe(var7), var9);
   }

   public static void glTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, FloatBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTexImage1D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      if (var7 != null) {
         BufferChecks.checkBuffer(var7, GLChecks.calculateTexImage1DStorage(var7, var5, var6, var3));
      }

      nglTexImage1D(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddressSafe(var7), var9);
   }

   public static void glTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, IntBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTexImage1D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      if (var7 != null) {
         BufferChecks.checkBuffer(var7, GLChecks.calculateTexImage1DStorage(var7, var5, var6, var3));
      }

      nglTexImage1D(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddressSafe(var7), var9);
   }

   public static void glTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, ShortBuffer var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTexImage1D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOdisabled(var8);
      if (var7 != null) {
         BufferChecks.checkBuffer(var7, GLChecks.calculateTexImage1DStorage(var7, var5, var6, var3));
      }

      nglTexImage1D(var0, var1, var2, var3, var4, var5, var6, MemoryUtil.getAddressSafe(var7), var9);
   }

   static native void nglTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glTexImage1D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexImage1D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOenabled(var9);
      nglTexImage1DBO(var0, var1, var2, var3, var4, var5, var6, var7, var10);
   }

   static native void nglTexImage1DBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7, long var9);

   public static void glTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage2DStorage(var8, var6, var7, var3, var4));
      }

      nglTexImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, DoubleBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage2DStorage(var8, var6, var7, var3, var4));
      }

      nglTexImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, FloatBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage2DStorage(var8, var6, var7, var3, var4));
      }

      nglTexImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, IntBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage2DStorage(var8, var6, var7, var3, var4));
      }

      nglTexImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   public static void glTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ShortBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      if (var8 != null) {
         BufferChecks.checkBuffer(var8, GLChecks.calculateTexImage2DStorage(var8, var6, var7, var3, var4));
      }

      nglTexImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddressSafe(var8), var10);
   }

   static native void nglTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glTexImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTexImage2D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglTexImage2DBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglTexImage2DBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexSubImage1D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var3, 1, 1));
      nglTexSubImage1D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, DoubleBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexSubImage1D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var3, 1, 1));
      nglTexSubImage1D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, FloatBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexSubImage1D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var3, 1, 1));
      nglTexSubImage1D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, IntBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexSubImage1D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var3, 1, 1));
      nglTexSubImage1D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   public static void glTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, ShortBuffer var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexSubImage1D;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureUnpackPBOdisabled(var7);
      BufferChecks.checkBuffer(var6, GLChecks.calculateImageStorage(var6, var4, var5, var3, 1, 1));
      nglTexSubImage1D(var0, var1, var2, var3, var4, var5, MemoryUtil.getAddress(var6), var8);
   }

   static native void nglTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glTexSubImage1D(int var0, int var1, int var2, int var3, int var4, int var5, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTexSubImage1D;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureUnpackPBOenabled(var8);
      nglTexSubImage1DBO(var0, var1, var2, var3, var4, var5, var6, var9);
   }

   static native void nglTexSubImage1DBO(int var0, int var1, int var2, int var3, int var4, int var5, long var6, long var8);

   public static void glTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexSubImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      BufferChecks.checkBuffer(var8, GLChecks.calculateImageStorage(var8, var6, var7, var4, var5, 1));
      nglTexSubImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddress(var8), var10);
   }

   public static void glTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, DoubleBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexSubImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      BufferChecks.checkBuffer(var8, GLChecks.calculateImageStorage(var8, var6, var7, var4, var5, 1));
      nglTexSubImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddress(var8), var10);
   }

   public static void glTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, FloatBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexSubImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      BufferChecks.checkBuffer(var8, GLChecks.calculateImageStorage(var8, var6, var7, var4, var5, 1));
      nglTexSubImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddress(var8), var10);
   }

   public static void glTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, IntBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexSubImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      BufferChecks.checkBuffer(var8, GLChecks.calculateImageStorage(var8, var6, var7, var4, var5, 1));
      nglTexSubImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddress(var8), var10);
   }

   public static void glTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ShortBuffer var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexSubImage2D;
      BufferChecks.checkFunctionAddress(var10);
      GLChecks.ensureUnpackPBOdisabled(var9);
      BufferChecks.checkBuffer(var8, GLChecks.calculateImageStorage(var8, var6, var7, var4, var5, 1));
      nglTexSubImage2D(var0, var1, var2, var3, var4, var5, var6, var7, MemoryUtil.getAddress(var8), var10);
   }

   static native void nglTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTexSubImage2D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOenabled(var10);
      nglTexSubImage2DBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var11);
   }

   static native void nglTexSubImage2DBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10);

   public static void glTexParameterf(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterf;
      BufferChecks.checkFunctionAddress(var4);
      nglTexParameterf(var0, var1, var2, var4);
   }

   static native void nglTexParameterf(int var0, int var1, float var2, long var3);

   public static void glTexParameteri(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameteri;
      BufferChecks.checkFunctionAddress(var4);
      nglTexParameteri(var0, var1, var2, var4);
   }

   static native void nglTexParameteri(int var0, int var1, int var2, long var3);

   public static void glTexParameter(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameterfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglTexParameterfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexParameterfv(int var0, int var1, long var2, long var4);

   public static void glTexParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglTexParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexParameteriv(int var0, int var1, long var2, long var4);

   public static void glTexGenf(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexGenf;
      BufferChecks.checkFunctionAddress(var4);
      nglTexGenf(var0, var1, var2, var4);
   }

   static native void nglTexGenf(int var0, int var1, float var2, long var3);

   public static void glTexGend(int var0, int var1, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTexGend;
      BufferChecks.checkFunctionAddress(var5);
      nglTexGend(var0, var1, var2, var5);
   }

   static native void nglTexGend(int var0, int var1, double var2, long var4);

   public static void glTexGen(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexGenfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglTexGenfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexGenfv(int var0, int var1, long var2, long var4);

   public static void glTexGen(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexGendv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglTexGendv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexGendv(int var0, int var1, long var2, long var4);

   public static void glTexGeni(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexGeni;
      BufferChecks.checkFunctionAddress(var4);
      nglTexGeni(var0, var1, var2, var4);
   }

   static native void nglTexGeni(int var0, int var1, int var2, long var3);

   public static void glTexGen(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexGeniv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglTexGeniv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexGeniv(int var0, int var1, long var2, long var4);

   public static void glTexEnvf(int var0, int var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexEnvf;
      BufferChecks.checkFunctionAddress(var4);
      nglTexEnvf(var0, var1, var2, var4);
   }

   static native void nglTexEnvf(int var0, int var1, float var2, long var3);

   public static void glTexEnvi(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexEnvi;
      BufferChecks.checkFunctionAddress(var4);
      nglTexEnvi(var0, var1, var2, var4);
   }

   static native void nglTexEnvi(int var0, int var1, int var2, long var3);

   public static void glTexEnv(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexEnvfv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglTexEnvfv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexEnvfv(int var0, int var1, long var2, long var4);

   public static void glTexEnv(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexEnviv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglTexEnviv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexEnviv(int var0, int var1, long var2, long var4);

   public static void glTexCoordPointer(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexCoordPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).glTexCoordPointer_buffer[StateTracker.getReferences(var3).glClientActiveTexture] = var2;
      }

      nglTexCoordPointer(var0, 5130, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glTexCoordPointer(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexCoordPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).glTexCoordPointer_buffer[StateTracker.getReferences(var3).glClientActiveTexture] = var2;
      }

      nglTexCoordPointer(var0, 5126, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glTexCoordPointer(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexCoordPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).glTexCoordPointer_buffer[StateTracker.getReferences(var3).glClientActiveTexture] = var2;
      }

      nglTexCoordPointer(var0, 5124, var1, MemoryUtil.getAddress(var2), var4);
   }

   public static void glTexCoordPointer(int var0, int var1, ShortBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexCoordPointer;
      BufferChecks.checkFunctionAddress(var4);
      GLChecks.ensureArrayVBOdisabled(var3);
      BufferChecks.checkDirect(var2);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var3).glTexCoordPointer_buffer[StateTracker.getReferences(var3).glClientActiveTexture] = var2;
      }

      nglTexCoordPointer(var0, 5122, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglTexCoordPointer(int var0, int var1, int var2, long var3, long var5);

   public static void glTexCoordPointer(int var0, int var1, int var2, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glTexCoordPointer;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureArrayVBOenabled(var5);
      nglTexCoordPointerBO(var0, var1, var2, var3, var6);
   }

   static native void nglTexCoordPointerBO(int var0, int var1, int var2, long var3, long var5);

   public static void glTexCoordPointer(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTexCoordPointer;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureArrayVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      if (LWJGLUtil.CHECKS) {
         StateTracker.getReferences(var4).glTexCoordPointer_buffer[StateTracker.getReferences(var4).glClientActiveTexture] = var3;
      }

      nglTexCoordPointer(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   public static void glTexCoord1f(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glTexCoord1f;
      BufferChecks.checkFunctionAddress(var2);
      nglTexCoord1f(var0, var2);
   }

   static native void nglTexCoord1f(float var0, long var1);

   public static void glTexCoord1d(double var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoord1d;
      BufferChecks.checkFunctionAddress(var3);
      nglTexCoord1d(var0, var3);
   }

   static native void nglTexCoord1d(double var0, long var2);

   public static void glTexCoord2f(float var0, float var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glTexCoord2f;
      BufferChecks.checkFunctionAddress(var3);
      nglTexCoord2f(var0, var1, var3);
   }

   static native void nglTexCoord2f(float var0, float var1, long var2);

   public static void glTexCoord2d(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTexCoord2d;
      BufferChecks.checkFunctionAddress(var5);
      nglTexCoord2d(var0, var2, var5);
   }

   static native void nglTexCoord2d(double var0, double var2, long var4);

   public static void glTexCoord3f(float var0, float var1, float var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexCoord3f;
      BufferChecks.checkFunctionAddress(var4);
      nglTexCoord3f(var0, var1, var2, var4);
   }

   static native void nglTexCoord3f(float var0, float var1, float var2, long var3);

   public static void glTexCoord3d(double var0, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glTexCoord3d;
      BufferChecks.checkFunctionAddress(var7);
      nglTexCoord3d(var0, var2, var4, var7);
   }

   static native void nglTexCoord3d(double var0, double var2, double var4, long var6);

   public static void glTexCoord4f(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTexCoord4f;
      BufferChecks.checkFunctionAddress(var5);
      nglTexCoord4f(var0, var1, var2, var3, var5);
   }

   static native void nglTexCoord4f(float var0, float var1, float var2, float var3, long var4);

   public static void glTexCoord4d(double var0, double var2, double var4, double var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTexCoord4d;
      BufferChecks.checkFunctionAddress(var9);
      nglTexCoord4d(var0, var2, var4, var6, var9);
   }

   static native void nglTexCoord4d(double var0, double var2, double var4, double var6, long var8);

   public static void glStencilOp(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glStencilOp;
      BufferChecks.checkFunctionAddress(var4);
      nglStencilOp(var0, var1, var2, var4);
   }

   static native void nglStencilOp(int var0, int var1, int var2, long var3);

   public static void glStencilMask(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glStencilMask;
      BufferChecks.checkFunctionAddress(var2);
      nglStencilMask(var0, var2);
   }

   static native void nglStencilMask(int var0, long var1);

   public static void glViewport(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glViewport;
      BufferChecks.checkFunctionAddress(var5);
      nglViewport(var0, var1, var2, var3, var5);
   }

   static native void nglViewport(int var0, int var1, int var2, int var3, long var4);
}
