package org.usfirst.frc.team910.robot.Vision;

public class TargetArray {
	private Target[] targets;
	private int currentTarget;

	public TargetArray() {
		currentTarget = 0;
		targets = new Target[Camera.NUMBER_OF_TARGETS];
		for (int i = 0; i < targets.length; i++) {
			targets[i] = new Target();

		}
	}

	public Target getCurrentTarget() {
		return targets[currentTarget];
	}

	public Target getNextTarget() {
		int index = currentTarget + 1;
		if (index > targets.length) {
			index = 0;
		}
		return targets[index];
	}

	public void setNextTargetAsCurrent() {
		currentTarget++;
		if (currentTarget > targets.length) {
			currentTarget = 0;
		}
	}

}