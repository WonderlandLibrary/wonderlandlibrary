package wtf.expensive.util.render.shader.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {

    public String crypt(String text) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            output.append((char) (text.charAt(i) ^ 43217));
        }
        return output.toString();
    }

}
