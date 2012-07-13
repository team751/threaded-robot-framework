package org.team751.framework;

import java.util.Vector;

import org.team751.framework.util.CompetitionState;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * <strong>Robot class for the threaded robot framework</strong>
 * <br />
 * Robot classes should extend this class to use the framework.
 * <br />
 * <strong>There's one important different to keep in mind when using this framework:</strong>
 * When overriding disabledInit(), autonomousInit(), or teleoperatedInit(), you must
 * explicitly call the function's superclass implementation (super.disabledInit(),
 * super.autonomousInit(), or super.teleoperatedInit() respectively). Not doing this
 * will prevent any tasks other than the main thread from responding to enable/disable
 * events and teleop/autonomous state changes.
 * 
 * @author Sam Crow
 */
public class ThreadedRobot extends IterativeRobot {
	
	private CompetitionState state = CompetitionState.kDisabled;
	
	private Vector tasks = new Vector();
	
	/**
	 * Add a new task. The task will immediately be given the current state and started.
	 * @param task The task to add
	 */
	protected void addTask(RobotTask task){
		tasks.add(task);
		task.setState(state);
		task.start();
	}
	
	public void disabledInit() {
		state = CompetitionState.kDisabled;
		updateStates();
	}

	public void autonomousInit() {
		state = CompetitionState.kAutonomous;
		updateStates();
	}

	public void teleopInit() {
		state = CompetitionState.kTeleop;
		updateStates();
	}
	
	/**
	 * Update every thread with the current competition state
	 */
	private void updateStates() {
		for(int i = 0, max = tasks.size(); i < max; i++){
			((RobotTask) tasks.elementAt(i)).setState(state);
		}
	}

}
