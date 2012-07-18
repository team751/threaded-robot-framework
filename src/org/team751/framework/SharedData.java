package org.team751.framework;

import java.util.Hashtable;

import org.team751.framework.util.MutexedObject;

/**
 * Stores references to sensors, motor controllers, and other shared objects
 * used by multiple tasks.
 * 
 * @author Sam Crow
 */
public class SharedData {

	/**
	 * The data structure for storage of shared objects
	 */
	private static volatile Hashtable objects = new Hashtable(50);

	/**
	 * Add an object to the data structure of shared objects
	 * 
	 * @param key
	 *            The key to use to access the object
	 * @param value
	 *            The object to add
	 */
	public static void put(Object key, Object value) {
		MutexedObject mutexed = new MutexedObject(value);
		objects.put(key, mutexed);
	}

	/**
	 * Get a lock on an object and return the object. This will block if another
	 * thread is using the object and has not called
	 * {@link SharedData#release(Object)}.
	 * 
	 * @param key
	 *            The key for the object to get
	 * @return The requested object
	 * @throws InterruptedException
	 *             if the thread was interrupted while waiting for other threads
	 *             to release the object.
	 */
	public static Object get(Object key) throws InterruptedException {
		Object value = objects.get(key);
		if (value == null) {
			return null;
		}
		MutexedObject mutexed = (MutexedObject) value;

		return mutexed.get();
	}

	/**
	 * Release the lock on an object. This will allow other threads to access
	 * it. Do not access an object after calling this method on its key. It is
	 * very important that this method be called when a task is done using an
	 * object. If it is not called, all the other threads that use it will pause
	 * forever waiting for it.
	 * 
	 * @param key
	 *            The key of the object to release.
	 */
	public static void release(Object key) {
		Object value = objects.get(key);
		if (value != null) {
			MutexedObject mutexed = (MutexedObject) value;
			mutexed.release();
		}
	}

	// Don't allow construction
	/**
	 * A dummy constructor to prevent use of this class in a non-static way
	 */
	private SharedData() {
	};
}
