package org.usfirst.frc.team910.robot.Subsystems;

import org.usfirst.frc.team910.robot.IO.Inputs;
import org.usfirst.frc.team910.robot.IO.Outputs;
import org.usfirst.frc.team910.robot.IO.Sensors;

public class GearSystem {
		
	//if parameter is met which we are using to tell we're close enough to the peg to initiate the procedure
	//set gear motor to some constant power to eject gear
	//when the gear is fully ejected from the robot, back the robot up from the peg
	//set flapmoter to a negative power and close it
	
	private Inputs in;
	private Outputs out;
	private Sensors sense;
	
	public static final double START_POSITION = 0;
	public static final double HOPPER_POSITION= 0;
	public static final double NATURAL_GEAR_POSITION = 0;
	public static final double EXTENDED_GEAR_POSITION = 0;
	public static final double ROLLER_POWER = 0.75; 
	
	public GearSystem(Inputs in,Outputs out, Sensors sense) {
		
		this.in = in;
		this.out = out;
		this.sense = sense;
	}
	
	public void gearposition(int gearPanelPosition, double rightwindowmotor, double leftwindowmotor,int ultrasonicDistance){ //Shows where a gear is on the panel
		
		switch(gearPanelPosition){
		
		case 0: //startposition
			out.setGearPanelPower(START_POSITION);     	
			break;
		case 1: //hopperposition
			if(ultrasonicDistance >= 0){
				out.setGearPanelPower(HOPPER_POSITION);
			}
			break;
		
		case 2: //gearposition
			if(ultrasonicDistance <= 0){
				out.setGearPanelPower(NATURAL_GEAR_POSITION);
			}
			if(ultrasonicDistance >= 0){
				out.setGearPanelPower(EXTENDED_GEAR_POSITION);
			}
			break;
		}
 

	}

	public void run() { 
		if (in.autoClimb || in.autoGear || in.autoShoot || in.autoHopper)  {   //if we hit any of these dont do anything	
		}
		else {                                      
			if(in.gearIntake) {                          //if we hit the gear in-tank button
				gearRoller(ROLLER_POWER);				//power the roller to pull in a gear
			}
			else if(in.gearOuttake) {     				//if we hit the gear out-take button
				gearRoller(-ROLLER_POWER);				//power the roller to push out a gear
			}
			
			//TODO: Replace with position code
			if(in.fireOverride && in.gearPanelPosition == 1){
				out.setGearPanelPower(0.20);
			} else if (in.fireOverride && in.gearPanelPosition == 3){
				out.setGearPanelPower(-0.20);
			} else {
				out.setGearPanelPower(0);
			}
		}
	}	
	
	public void gearRoller(double power) { 			//when called set the GearRoller to some power
		out.setGearRoller(power);
		
	}
}
