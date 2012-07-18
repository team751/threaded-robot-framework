package org.team751.framework;

import org.team751.framework.util.CompetitionState;

import edu.wpi.first.wpilibj.Timer;

/**
 * A thread that contains a robot task. Operations in this task can safely block
 * without freezing the robot. <br />
 * <br />
 * Note that the methods in this class are called as frequently as possible. You
 * must add a call to {@link Thread#sleep(long)} or {@link Timer#delay(double)}
 * to prevent this task from running continuously and causing 100% CPU usage.
 * 
 * @author Sam Crow
 */
public abstract class RobotTask extends Thread {

	/** The current phase of competition */
	private volatile CompetitionState state;

	/**
	 * The minimum time, in seconds, that a task ({@link RobotTask#disabled()},
	 * {@link RobotTask#autonomous()}, or {@link RobotTask#teleop()}) is
	 * expected to take. If one of those method calls takes less than this time,
	 * it is likely that the programmer didn't use {@link Thread#sleep(long)} or
	 * {@link Timer#delay(double)} to prevent continous task operation and 100%
	 * CPU usage. In this event, a warning will be printed to {@link System#err}
	 * .
	 */
	public static final float kMinTaskTime = 0.01f;

	public final void run() {
		while (true) {

			// Clear the thread's interrupt state.
			// An interrupt can be applied to tell this thread that
			// the status has changed. However, at this point
			// (right before the state check), we need to
			// clear the interrupt to signify that the check is
			// being done.
			Thread.interrupted();

			if (state == CompetitionState.kDisabled) {
				try {
					Timer timer = new Timer();
					timer.start();
					
					disabled();
					
					if(timer.get() < kMinTaskTime) {
						timingWarning("disabled");
					}
				} catch (InterruptedException e) {
					// Thread interrupted, meaning the state has changed.
					// Go on to the next iteration of the loop and check
					// the state again.
					continue;
				}
			}

			else if (state == CompetitionState.kAutonomous) {
				try {
					Timer timer = new Timer();
					timer.start();
					
					autonomous();
					
					if(timer.get() < kMinTaskTime) {
						timingWarning("autonomous");
					}
				} catch (InterruptedException e) {
					// Thread interrupted, meaning the state has changed.
					// Go on to the next iteration of the loop and check
					// the state again.
					continue;
				}
			}

			else if (state == CompetitionState.kTeleop) {
				try {
					Timer timer = new Timer();
					timer.start();
					
					teleop();
					
					if(timer.get() < kMinTaskTime) {
						timingWarning("teleop");
					}
				} catch (InterruptedException e) {
					// Thread interrupted, meaning the state has changed.
					// Go on to the next iteration of the loop and check
					// the state again.
					continue;
				}
			}

		}
	}

	/**
	 * Change this task's competition state If the new state is different from
	 * the current state, this method will call interrupt() to ensure that it
	 * knows about the new state.
	 * 
	 * @param state
	 *            The new state to set
	 */
	// Default visibility: Should make it inaccessible to anything outside
	// org.team751.framework
	final synchronized void setState(CompetitionState state) {
		if (this.state != state) {
			this.state = state;
			interrupt();
		}
	}

	/**
	 * The robot task handler calls this method as frequently as possible while
	 * the robot is disabled.
	 * 
	 * @throws InterruptedException
	 *             If the task has been interrupted. You don't need to worry
	 *             about this. The robot task handler will deal with it.
	 */
	protected void disabled() throws InterruptedException {
		sleep((long) (kMinTaskTime * 1000));
	};

	/**
	 * The robot task handler calls this method as frequently as possible while
	 * the robot is in autonomous mode.
	 * 
	 * @throws InterruptedException
	 *             If the task has been interrupted. You don't need to worry
	 *             about this. The robot task handler will deal with it.
	 */
	protected void autonomous() throws InterruptedException {
		sleep((long) (kMinTaskTime * 1000));
	};

	/**
	 * The robot task handler calls this method as frequently as possible while
	 * the robot is in teleoperated mode.
	 * 
	 * @throws InterruptedException
	 *             If the task has been interrupted. You don't need to worry
	 *             about this. The robot task handler will deal with it.
	 */
	protected void teleop() throws InterruptedException {
		sleep((long) (kMinTaskTime * 1000));
	};

	/**
	 * Check if the thread has been interrupted. An interrupt happens when the
	 * competition state changes. Robot code should call this method in between
	 * iterations whenever doing anything that could take some time (except
	 * <code>Thread.sleep()</code> or anything else that throws an
	 * <code>InterruptedException</code>) to ensure that it responds quickly to
	 * state changes. <br />
	 * <br />
	 * If this method is called and that task has been interrupted, the task
	 * will immediately return up to <code>RobotTask.run()</code> and the state
	 * will be checked again. <br />
	 * <br />
	 * If you need to clean up after a process in the event that it's
	 * interrupted, do this:
	 * 
	 * <pre>
	 * // Do some operation
	 * try {
	 * 	checkInterrupt();
	 * } catch (InterruptedException e) {
	 * 	// Clean up
	 * 	throw e; // Always re-throw so that the state will be rechecked
	 * }
	 * </pre>
	 * 
	 * @throws InterruptedException
	 *             If the task has been interrupted. You don't need to worry
	 *             about this. The robot task handler will deal with it.
	 */
	protected final void checkInterrupt() throws InterruptedException {
		if (isInterrupted()) {
			throw new InterruptedException(
					"Interrupted while running checkInterrupt()");
		}
	}
	
	/**
	 * Print a warning to {@link System#err} telling the user that a task
	 * didn't take long enough to complete and probably lacks a call to
	 * {@link Thread#sleep(long)} or {@link Timer#delay(double)}.
	 * @param methodName The name of the method that was called, not including
	 * parenthesis. For example, "disabled", "autonomous", or "teleop".
	 */
	private void timingWarning(String methodName) {
		System.err.println("Your "+methodName+"() method took less than "+kMinTaskTime+" seconds to complete. Did you forget to sleep() or Timer.delay()?");
	}
}
