package keyserver;

/**
 * Monitor time and print helpful debug informationf or performance testing
 */
public class TimeMonitor {
	private final ExponentialMovingAverage ema = new ExponentialMovingAverage(.05);
	private long lastTime = 0;
	
	/**
	 * Called every time a new keyvalue has been generated
	 * @param keyValue new monitonically increasing int generated from server
	 */
	public void update(long keyValue){
		if(keyValue % 100000 == 0){
			long newTime = System.currentTimeMillis();
			double diffSeconds = (newTime - lastTime) / 1000.0;
			lastTime = newTime;
			int emaValue = (int)ema.average(100000.0 / diffSeconds);
			System.out.println("Created key: " + keyValue + " keys/second: " + emaValue);
		}
	}
}
