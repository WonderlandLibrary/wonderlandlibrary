package appu26j.utils;

import java.time.ZonedDateTime;

public class ClockUtil
{
	public static String getTime12HourFormat()
	{
		ZonedDateTime zonedDateTime = ZonedDateTime.now();
		int hour = zonedDateTime.getHour() > 12 ? zonedDateTime.getHour() - 12 : (zonedDateTime.getHour() == 0 ? 12 : zonedDateTime.getHour());
		String minute = zonedDateTime.getMinute() < 10 ? "0" + zonedDateTime.getMinute() : ("" + zonedDateTime.getMinute());
		String aString = zonedDateTime.getHour() > 11 ? "PM" : "AM";
		return hour + ":" + minute + " " + aString;
	}
	
	public static String getTime24HourFormat()
	{
		ZonedDateTime zonedDateTime = ZonedDateTime.now();
		int hour = zonedDateTime.getHour();
		String minute = zonedDateTime.getMinute() < 10 ? "0" + zonedDateTime.getMinute() : ("" + zonedDateTime.getMinute());
		return hour + ":" + minute;
	}
}
