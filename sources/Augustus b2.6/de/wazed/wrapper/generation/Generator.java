// 
// Decompiled by Procyon v0.5.36
// 

package de.wazed.wrapper.generation;

import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import de.wazed.wrapper.utils.exceptions.NotFoundException;
import de.wazed.wrapper.utils.exceptions.UnauthorizedException;
import java.util.HashMap;
import org.json.JSONObject;
import de.wazed.wrapper.utils.WebUtil;

public class Generator
{
    private static Generator instance;
    
    public Generator() {
        Generator.instance = this;
    }
    
    public Account generate(final String token) throws UnauthorizedException, NotFoundException {
        try {
            final List<String> lines = WebUtil.getInstance().performRequest("http://api.thealtening.com/v1/generate?token=" + token + "&info=true");
            final StringBuilder sb = new StringBuilder();
            for (final String line : lines) {
                sb.append(line).append("\n");
            }
            final JSONObject object = new JSONObject(sb.toString());
            final HashMap map = new HashMap();
            final HashMap<String, String> infoMap = new HashMap<String, String>();
            for (final String key : object.getJSONObject("info").keySet()) {
                infoMap.put(key, object.getJSONObject("info").getString(key));
            }
            final Account tempAccount = new Account(object.getString("token"), object.getString("password"), object.getString("username"), object.getBoolean("limit"), infoMap);
            return tempAccount;
        }
        catch (IOException e) {
            if (e.getMessage().contains("403")) {
                throw new UnauthorizedException();
            }
            if (e.getMessage().contains("404")) {
                throw new NotFoundException();
            }
            return null;
        }
    }
    
    public static Generator getInstance() {
        return Generator.instance;
    }
}
