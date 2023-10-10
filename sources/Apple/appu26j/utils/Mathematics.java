package appu26j.utils;

import java.math.BigDecimal;

public class Mathematics
{
    public static double square(double number)
    {
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.multiply(bigDecimal).doubleValue();
    }

    public static double powerOf(double number, int times)
    {
        BigDecimal numberBigDecimal = new BigDecimal(number);
        BigDecimal result = new BigDecimal(1);

        for (int i = 0; i < times; i++)
        {
            result = result.multiply(numberBigDecimal);
        }

        return result.doubleValue();
    }

    public static double minimum(double number1, double number2)
    {
        return number1 < number2 ? number1 : number2;
    }

    public static double maximum(double number1, double number2)
    {
        return number1 > number2 ? number1 : number2;
    }

    public static double squareRoot(double number)
    {
        return Double.longBitsToDouble(((Double.doubleToRawLongBits(number) >> 32) + 1072632448) << 31);
    }

    public static int floor(double number)
    {
        return number < 0 ? ceilFunc(number) - 1 : floorFunc(number);
    }

    private static int floorFunc(double number)
    {
        return (int) number;
    }

    public static int ceil(double number)
    {
        return number < 0 ? floorFunc(number) : ceilFunc(number);
    }

    private static int ceilFunc(double number)
    {
        int numberInt = (int) number;
        return numberInt < number ? numberInt + 1 : numberInt;
    }

    public static double absoluteValue(double number)
    {
        return number < 0 ? -number : number;
    }

    public static int round(double number)
    {
        if (number < 0)
        {
            return -(number % 1) > 0.5 ? (int) number - 1 : (int) number;
        }

        else
        {
            return number % 1 > 0.4 ? (int) number + 1 : (int) number;
        }
    }

    public static double random()
    {
        StringBuilder digits = new StringBuilder("0.");

        for (int i = 0; i < 16; i++)
        {
            digits.append((randomDigit() + "").charAt(0));
        }

        return Double.parseDouble(digits.toString());
    }

    public static double random(int bound)
    {
        return random() * bound;
    }

    public static double random(int origin, int bound)
    {
        return (random() * (bound - origin)) + origin;
    }

    private static double randomDigit()
    {
        return System.nanoTime() % 9;
    }
}
