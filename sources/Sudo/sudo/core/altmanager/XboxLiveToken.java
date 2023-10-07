package sudo.core.altmanager;

public class XboxLiveToken {
	private final String token;
	private final String hash;

	public XboxLiveToken(String token, String uhs)
	{
		this.token = token;
		this.hash = uhs;
	}

	public String getToken()
	{
		return token;
	}

	public String getHash()
	{
		return hash;
	}
}
