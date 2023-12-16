// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils.screenshot;

import com.google.gson.JsonParseException;
import java.io.Writer;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import org.apache.commons.codec.binary.Base64;
import java.io.OutputStream;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.awt.image.BufferedImage;
import com.google.gson.JsonObject;

public class ImgurHandler extends ImageHost
{
    public static final String CLIENT_ID = "a4d001f6bb8b38e";
    private JsonObject lastUploadData;
    
    public ImgurHandler() {
        super("imgur");
        System.out.println("added");
        this.lastUploadData = null;
    }
    
    @Override
    public boolean upload(final BufferedImage image, final UPLOAD_METHOD uploadMethod, final String... description) {
        OutputStreamWriter wr = null;
        BufferedReader in = null;
        HttpURLConnection conn = null;
        ByteArrayOutputStream baos = null;
        try {
            final URL url = new URL("https://api.imgur.com/3/upload.json");
            baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            final byte[] imageInByte = baos.toByteArray();
            final String encoded = Base64.encodeBase64String(imageInByte);
            final String data = String.valueOf(URLEncoder.encode("image", "UTF-8")) + "=" + URLEncoder.encode(encoded, "UTF-8");
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID 70df5d8909b8da8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            this.lastUploadData = new JsonParser().parse(in).getAsJsonObject();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.close((URLConnection)conn);
            IOUtils.closeQuietly((Writer)wr);
            IOUtils.closeQuietly((Reader)in);
            IOUtils.closeQuietly((OutputStream)baos);
        }
        return true;
    }
    
    @Override
    public boolean deleteLast() {
        HttpURLConnection conn = null;
        try {
            final URL url = new URL("https://api.imgur.com/3/image/" + this.getDeleteHash());
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Client-ID 70df5d8909b8da8");
            if (conn.getResponseCode() == 200) {
                this.lastUploadData = null;
                return true;
            }
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            IOUtils.close((URLConnection)conn);
        }
    }
    
    @Override
    public String getLink() {
        if (this.lastUploadData != null) {
            final JsonObject dataJson = this.lastUploadData.get("data").getAsJsonObject();
            return "https://imgur.com/" + dataJson.get("id").getAsString();
        }
        return null;
    }
    
    private String getDeleteHash() throws JsonParseException {
        if (this.lastUploadData != null) {
            final JsonObject dataJson = this.lastUploadData.get("data").getAsJsonObject();
            return dataJson.get("deletehash").getAsString();
        }
        return null;
    }
    
    @Override
    public boolean canUploadAnon() {
        return true;
    }
    
    @Override
    public boolean canUploadAccount() {
        return false;
    }
}
