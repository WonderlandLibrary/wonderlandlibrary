package tv.twitch;

public class AuthToken {
  public String data;
  
  public boolean getIsValid() {
    return (this.data != null && this.data.length() > 0);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\AuthToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */