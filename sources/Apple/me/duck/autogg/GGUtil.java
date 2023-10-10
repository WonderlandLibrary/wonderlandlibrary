package me.duck.autogg;

public class GGUtil
{
	private static final String author = "https://github.com/ToxicDuckXx";
	private static final String originalClassName = "GetTriggers.java";
	
	private GGUtil()
	{
		;
	}
    
    public static boolean hasGameEnded(String message)
    {
        String[] triggers = getTriggers();
        
        for (String trigger : triggers)
        {
            if (message.contains(trigger.replaceAll("\n", "").trim()))
            {
                return true;
            }
        }
        
        return false;
    }
    
    private static String[] getTriggers()
    {
        String triggers = "1st Killer - \n1st Place - \nWinner: \n - Damage Dealt - \nWinning Team -\n1st - \nWinners: \nWinner: \nWinning Team: \n won the game!\nTop Seeker: \n1st Place: \nLast team standing!\nWinner #1 (\nTop Survivors\nWinners - \nSumo Duel - ";
        return triggers.split("\n");
    }
}
