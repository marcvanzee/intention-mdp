package constants;

import java.util.Random;

/**
 * Class containing static helper functions.
 * We ensure it cannot be instantiated by declaring a prive constructor.
 * 
 * @author marc.vanzee
 *
 */
public class MathOperations 
{
	private static Random r = new Random();
	
	//Suppress default constructor for noninstantiability
	private MathOperations() {
		// This constructor will never be invoked
	}
	
	public static double round(double d, int decimals) {
		return Math.round(d * 100.0)/100.0;
	}
	
	public static boolean throw_dice(double d) {
		return r.nextDouble() < d;
	}
	
	/**
	 * Generate a random integer that is on average of value <i>average</i>.
	 * The variance from <i>average> is <i>variance</i>, but the number will
	 * never be lower than <i>min</i> and never larger than <i>max</i>.
	 * 
	 * @param average
	 * @param variance
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomInt(int average, int variance, int min, int max)
	{
		int minValue = Math.max(1, average-variance);
		int maxValue = Math.min(max, average+variance);
		
		return r.nextInt((maxValue - minValue) + 1) + minValue;
	}
}