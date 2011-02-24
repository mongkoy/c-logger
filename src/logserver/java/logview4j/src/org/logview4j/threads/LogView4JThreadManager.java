/*
 * Copyright 1999-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * $Id: LogView4JThreadManager.java,v 1.5 2005/12/27 14:53:11 jpassenger Exp $
 */
package org.logview4j.threads;

import EDU.oswego.cs.dl.util.concurrent.BoundedBuffer;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/**
 * A buffer thread pool manager that uses Conccurent PooledExecutor
 * and BoundedBuffer to restrict the number of threads in use
 * by LogView4J and allow resuse of threads.
 * 
 * For futher tuning see:
 * http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/PooledExecutor.html
 * 
 * TODO these parameters will become configurable from the config GUI
 */
public class LogView4JThreadManager {

	/**
	 * The singleton instance
	 */
	private static LogView4JThreadManager instance = new LogView4JThreadManager();
	/**
	 * A thread pool
	 */
	private final PooledExecutor pool;
	/**
	 * Maximum number of queued requests
	 */
	private static final int BUFFER_SIZE = 1000;
	/**
	 * Maximum number of threads
	 */
	private static final int MAX_THREADS = 10;
	/**
	 * Minimum number of threads (lazily instantiated)
	 */
	private static final int MIN_THREADS = 4;
	/**
	 * How long to keep idle threads
	 */
	private static final long KEEP_ALIVE = 1000 * 60 * 5;

	/**
	 * Constructs the singleton instance
	 * and sets up the thread pool
	 */
	private LogView4JThreadManager() {
		pool = new PooledExecutor(new BoundedBuffer(BUFFER_SIZE), MAX_THREADS);
		pool.setMinimumPoolSize(MIN_THREADS);
		pool.setKeepAliveTime(KEEP_ALIVE);

		/**
		 * Tell the pool to get the calling thread to execute the runnable
		 * when the pool is empty and the buffer full to prevent deadlocks
		 */
		pool.runWhenBlocked();
	}

	/**
	 * Grabs the singleton instance
	 * @return the singleton instance
	 */
	public static LogView4JThreadManager getInstance() {
		return instance;
	}

	/**
	 * Executes the runnable using a thread from the pool
	 * @param runnable the runnable to execute
	 */
	public void execute(Runnable runnable) {
		try {
			pool.execute(runnable);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tells the pool to stop executing and not to hand out
	 * any more threads
	 */
	public void shutDown() {
		pool.shutdownNow();
	}
}
