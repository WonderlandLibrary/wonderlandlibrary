package appu26j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AppleClientVersionChecker extends Thread
{
	private boolean upToDate = false;
	
	@Override
	public void run()
	{
		super.run();
		HttpsURLConnection httpsURLConnection = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		
		try
		{
			URL url = new URL("https://pastebin.com/raw/eZYvUZHs");
			httpsURLConnection = (HttpsURLConnection) url.openConnection();
			httpsURLConnection.setDoInput(true);
			httpsURLConnection.setDoOutput(false);
			httpsURLConnection.connect();
			inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			float version = Float.parseFloat(bufferedReader.readLine());
			
			if (Float.parseFloat(Apple.VERSION) >= version)
			{
				this.upToDate = true;
			}
		}
		
		catch (Exception e)
		{
			;
		}
		
		finally
		{
			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
				}
				
				catch (Exception e)
				{
					;
				}
			}
			
			if (inputStreamReader != null)
			{
				try
				{
					inputStreamReader.close();
				}
				
				catch (Exception e)
				{
					;
				}
			}
			
			if (httpsURLConnection != null)
			{
				httpsURLConnection.disconnect();
			}
		}
	}
	
	/**
	 * Returns if Apple Client is updated or not.
	 * @return upToDate
	 */
	public boolean isUpToDate()
	{
		return this.upToDate;
	}
}
