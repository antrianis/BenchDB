//package benchmark;
//
//import java.util.Random;
//
//public class TClientThread extends Thread
//{
//	static Random random=new Random();
//
//	int _opcount;
//	double _target;
//
//	int _opsdone;
//	int _threadid;
//	int _threadcount;
//
//	private String _db;
//
//	private TerminatorThread _t;
//
//	int _latency;
//
//	/**
//	 * Constructor.
//	 * 
//	 * @param db the DB implementation to use
//	 * @param dotransactions true to do transactions, false to insert data
//	 * @param workload the workload to use
//	 * @param threadid the id of this thread 
//	 * @param threadcount the total number of threads 
//	 * @param props the properties defining the experiment
//	 * @param opcount the number of operations (transactions or inserts) to do
//	 * @param targetperthreadperms target number of operations per thread per ms
//	 */
//	//public ClientThread(String db, int threadid, int threadcount, int opcount, double targetperthreadperms,TerminatorThread t)
//	public TClientThread(String db, int threadid, int threadcount, int opcount)
//	
//	{
//		_db=db;
//		_threadid=threadid;
//		_opcount=opcount;
//		_opsdone=0;
//		//_target=targetperthreadperms;
//		//_threadcount=threadcount;
//	}
//
//	public int getOpsDone()
//	{
//		return _opsdone;
//	}
//	
//	public int getTotalLatency()
//	{
//		return _latency;
//	}
//	
//
//	public void run()
//	{
//		//spread the thread operations out so they don't all hit the DB at the same time
//		try
//		{
//			//GH issue 4 - throws exception if _target>1 because random.nextInt argument must be >0
//			//and the sleep() doesn't make sense for granularities < 1 ms anyway
//			if ( (_target>0) && (_target<=1.0) ) 
//			{
//				sleep(random.nextInt((int)(1.0/_target)));
//			}
//		}
//		catch (InterruptedException e)
//		{
//			// do nothing.
//		}
//
//
//
//		while (((_opcount == 0) || (_opsdone < _opcount)) && !isStopRequested())
//		{
//			
//			long st=System.currentTimeMillis();
//			if (_db.equals("neo4j")) //$NON-NLS-1$
//				doOperationNeo4j();
//			else if (_db.equals("mysql")) //$NON-NLS-1$
//				doOperationMySQL();
//			
//			long end=System.currentTimeMillis();
//
//			_latency += end - st;
//			
//			_opsdone++;
//
//			//throttle the operations
////			if (_target>0)
////			{
////				//this is more accurate than other throttling approaches we have tried,
////				//like sleeping for (1/target throughput)-operation latency,
////				//because it smooths timing inaccuracies (from sleep() taking an int, 
////				//current time in millis) over many operations
////				while (System.currentTimeMillis()-st<((double)_opsdone)/_target)
////				{
////					try
////					{
////						sleep(1);
////					}
////					catch (InterruptedException e)
////					{
////						// do nothing.
////					}
////
////				}
////			}
//		}
//	}
//}