package today.flux.utility;

import com.soterdev.SoterObfuscator;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import today.flux.Flux;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by John on 2017/04/24.
 */
public class HttpUtil {

	public static HttpURLConnection createUrlConnection(URL url) throws IOException {
		Validate.notNull(url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setUseCaches(false);
		return connection;
	}

	public static String performGetRequest(URL url, boolean withKey) throws IOException {
		return new HttpUtil().performGetRequestWithoutStatic(url, withKey);
	}

	public static String performGetRequest(URL url) throws IOException {
		return new HttpUtil().performGetRequestWithoutStatic(url, false);
	}

	@SoterObfuscator.Obfuscation(flags = "+native")
	public String performGetRequestWithoutStatic(URL url, boolean withKey) throws IOException {
		Validate.notNull(url);

		if (Flux.DEBUG_MODE)
			System.out.println(url);

		HttpURLConnection connection = createUrlConnection(url);
		InputStream inputStream = null;
		connection.setRequestProperty("user-agent", "Mozilla/5.0 AppIeWebKit");

		if (withKey) {
			connection.setRequestProperty("xf-api-key", "LnM-qSeQqtJlJmJnVt76GhU-SoiolWs9");
		}

		String var6;
		try {
			String result;
			try {
				inputStream = connection.getInputStream();
				return IOUtils.toString(inputStream, Charsets.UTF_8);
			} catch (IOException var10) {
				IOUtils.closeQuietly(inputStream);
				inputStream = connection.getErrorStream();
				if (inputStream == null) {
					throw var10;
				}
			}

			result = IOUtils.toString(inputStream, Charsets.UTF_8);
			var6 = result;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

		return var6;
	}

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 AppIeWebKit");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
}
