package org.team751.framework.util;

import java.util.Hashtable;
import java.util.Random;

/**
 * A type of Object that creates a random identifying value when it is constructed
 * and implements hashCode() and equals() so that it can be used as a key in
 * a {@link Hashtable} or similar.
 * @author Sam Crow
 */
public class Key {
	
	private int id;
	
	{
		id = new Random().nextInt();
	}
	
	/**
	 * Create a set of keys
	 * @param count The number of keys to create
	 * @return An array of keys
	 */
	public static Key[] createSet(int count) {
		Key[] set = new Key[count];
		
		for(int i = 0; i < count; i++){
			set[i] = new Key();
		}
		
		return set;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Key)) {
			return false;
		}
		Key other = (Key) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
	
}
