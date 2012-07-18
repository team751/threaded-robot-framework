package org.team751.framework.util;

import java.util.concurrent.Semaphore;

/**
 * Stores an Object and a corresponding mutex (really a {@link Semaphore}) to restrict
 * access to a single thread.
 * @author Sam Crow
 *
 */
public class MutexedObject {
	
	/**
	 * The object that this object stores and restricts access to
	 */
	//No generics in ME :-(
	private Object object;
	
	/**
	 * The semaphore used to restrict access
	 */
	private Semaphore mutex = new Semaphore(1, true);
	
	/**
	 * Constructor
	 * @param object The object to wrap
	 */
	public MutexedObject(Object object) {
		this.object = object;
	}
	
	/**
	 * Get a lock on this object and then return the object.
	 * @return The object
	 * @throws InterruptedException If the thread was interrupted while waiting
	 * for another thread to release the object
	 */
	public Object get() throws InterruptedException {
		
		mutex.acquire();
		
		return object;
	}
	
	/**
	 * Release the lock on this object. After this method is called, other threads will
	 * be able to access it. To prevent unexpected behavior and concurrent modification,
	 * you should not access the object after calling this method.
	 */
	public void release() {
		mutex.release();
	}
}
