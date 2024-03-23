package xyz.northclient.util.shader;


import xyz.northclient.util.shader.impl.impl.GradientRoundedShader;
import xyz.northclient.util.shader.impl.impl.RoundedShader;

public class ShaderUtil {
    public static RoundedShader roundedShader;
    public static GradientRoundedShader gradientRoundedShader;

    public static void init() {
        roundedShader = new RoundedShader();
        gradientRoundedShader = new GradientRoundedShader();
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double output = 1.0 / Math.sqrt(2.0 * Math.PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }
}
