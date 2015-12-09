package settings;

public class BenchmarkSettings 
{
	public static final int SIMULATION_LENGTH = 	12000;
	public static final int REPETITIONS = 			5;
		
	public static final boolean LOGARITHMIC = 		false;
	
	public static final 
		BenchmarkType BENCHMARK_TYPE = 				BenchmarkType.PLANNING_TIME;
	
	public static final int BENCHMARK_VALUE_MIN = 	0,
							BENCHMARK_VALUE_MAX = 	10,
							BENCHMARK_POINTS = 		10;
	
	public static final int BENCHMARK_RANGE = 		5; // only for hole gestation time and life time
	
	// for learning
	public static final int TRAINING_LENGTH = 100000;
	public static final int TEST_LENGTH     = 5000;
	
	public enum BenchmarkType {
		DYNAMISM, PLANNING_TIME, MIN_GESTATION_TIME, MIN_LIFETIME, WORLD_SIZE;
	}

}
