package org.team751.framework.util;

/**
 * Enumeration of possible competition states (disabled, teleoperated, autonomous)
 * @author Sam Crow
 */
public class CompetitionState {
	private CompetitionState(){}
	
	/** Disabled state */
	public static CompetitionState kDisabled = new CompetitionState();
	/** Autonomous state */
	public static CompetitionState kAutonomous = new CompetitionState();
	/** Teleoperated state */
	public static CompetitionState kTeleop = new CompetitionState();
}
