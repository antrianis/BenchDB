//package benchmark;
//import java.util.Vector;
//
///**
// * A thread that waits for the maximum specified time and then interrupts all the client
// * threads passed as the Vector at initialization of this thread.
// * 
// * The maximum execution time passed is assumed to be in seconds.
// */
//
//public class TTerminatorThread extends Thread {
//
//	private Vector<Thread> threads;
//	private long maxExecutionTime;
//	private long waitTimeOutInMS;
//
//	public TTerminatorThread(long maxExecutionTime, Vector<Thread> threads) {
//
//		this.maxExecutionTime = maxExecutionTime;
//		this.threads = threads;
//		waitTimeOutInMS = 2000;
//		System.err.println("Maximum execution time specified as: " + maxExecutionTime + " secs");
//	}
//
//	public void run() {
//		try {
//			Thread.sleep(maxExecutionTime * 1000);
//		} catch (InterruptedException e) {
//			System.err.println("Could not wait until max specified time, TerminatorThread interrupted.");
//			return;
//		}
//
//		System.err.println("Maximum time elapsed. Requesting stop for the workload.");
//		
//		stopRequested();
//		
//		
//		
//		System.err.println("Stop requested for workload. Now Joining!");
//		for (Thread t : threads) {
//			while (t.isAlive()) {
//				try {
//					t.join(waitTimeOutInMS);
//					if (t.isAlive()) {
//						System.err.println("Still waiting for thread " + t.getName() + " to complete. ");
//					}
//				} catch (InterruptedException e) {
//					// Do nothing. Don't know why I was interrupted.
//				}
//			}
//		}
//	}
//}
