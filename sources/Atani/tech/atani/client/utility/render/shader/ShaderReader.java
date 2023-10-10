package tech.atani.client.utility.render.shader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.util.ResourceLocation;
import tech.atani.client.utility.interfaces.Methods;

@Native
public class ShaderReader implements Methods {

    public static String readShader(String fileName) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (fileName.startsWith("/"))
            fileName = fileName.substring(1);
        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(mc.getResourceManager().getResource(new ResourceLocation(String.format("atani/shaders/%s", fileName))).getInputStream());
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

}