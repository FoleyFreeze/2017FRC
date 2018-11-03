package org.usfirst.frc.team910.robot.Subsystems;

import org.usfirst.frc.team910.robot.IO.Inputs;
import org.usfirst.frc.team910.robot.IO.Outputs;
import org.usfirst.frc.team910.robot.IO.Sensors;
import org.usfirst.frc.team910.robot.IO.Util;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
	private static final double SHOOTER_SPEED = 1865;// 1740; //1820; //1830; //1850;
	private static final double JOG_AMOUNT = 10*5;
	private static final double ALLOWABLE_SHOOTER_ERROR = 70;
	
	private static final double AGI_FILT = 0.2;
	
	private static final double SHOOTER_ADJ = 100;//B4 CMP 70;	//Added at MSC because all shooting was SHORT!
	
	//private static final double[] SHOOTER_PWR_AXIS = {12, 18, 36, 48, 60};
	private static final double[] SHOOTER_PWR_AXIS = {24, 30, 36, 42, 48, 54};
	//private static final double[] SHOOTER_PWR_TABLE = {1630, 1660, 1880, 1900, 1930}; // took 10 off all 4/7/17 11:21
	//private static final double[] SHOOTER_PWR_TABLE = {1615, 1645, 1865, 1885, 1915}; // took 15 off all 4/7/17 4:31
	//private static final double[] SHOOTER_PWR_TABLE = {1695, 1725, 1775, 1825, 1865, 1895}; //B4 MSC 
	private static final double[] SHOOTER_PWR_TABLE = {1695+ SHOOTER_ADJ, 1725+ SHOOTER_ADJ, 1775+ SHOOTER_ADJ, 1825+ SHOOTER_ADJ, 1865+ SHOOTER_ADJ, 1895+ SHOOTER_ADJ}; 
	private Outputs out;
	private Inputs in;

	private double jogoffset = 0;

	public Shooter(Outputs out, Inputs in) {
		this.out = out;
		this.in = in;
	}

	public void run() {
		jog();
		if (in.autoClimb || in.autoGear || in.autoShoot || in.autoHopper) { // if we hit any of these do nothing

		} else { // if we do anything else

			shooterPrime(in.primeButton, in.fireButton, 37, in.reverseButton, false); // call prime with the prime button

			//shooterFire(in.fireButton); // and shooter with the fire button

		}
	}

	private double shooterSetSpeed = 0;
	
	private double prevTime = 0;
	private double agiTime = 0;
	private boolean pauseAgi = false;
	
	public void shooterPrime(boolean primeButton, boolean fire, double dist, boolean reverse, boolean slowAgi) { // Moves the big roller
		if (primeButton) { // if we hit the prime button
			
			shooterSetSpeed = Util.interpolate(SHOOTER_PWR_AXIS, SHOOTER_PWR_TABLE, dist);
			out.setShooterSpeed(shooterSetSpeed + jogoffset); // ready the shooter to fire (with our constant shooter speed + how much we have jogged)
			
			SmartDashboard.putNumber("JogValue", shooterSetSpeed + jogoffset); // put out the jog-adjusted value to the dashboard
			//out.setAgitatorPower(1); // start spinning the agitator to get the fuel moving
			// out.setShooterPower (0.5);
			
			if(!fire){
				out.setTransportPower(-0.2);
				out.setAgitatorPower(0);
				
			} else{	//firing
				out.setTransportSpeed(6000); //was 3500
				
				//track the agitator time
				double time = Timer.getFPGATimestamp();
				double deltaTime = time - prevTime;
				
				//if we have been shooting, increment the agitator time
				if(deltaTime < 0.5){
					agiTime += deltaTime;
				} else { //otherwise reset the clock
					agiTime = 0;
					pauseAgi = false;
				}
				
				//if we have been agitating for 3 seconds, pause
				if(agiTime > 1.5 && !pauseAgi) {
					pauseAgi = true;
					agiTime = 0;
				
				//if we have been paused for 0.5 seconds, agitate again
				} else if(agiTime > 1.0 && pauseAgi){
					pauseAgi = false;
					agiTime = 0;
				}
				
				//either pause the agi or dont
				if(pauseAgi){
					if(slowAgi) out.setAgitatorSpeed(400);
					else out.setAgitatorPower(-1);
				} else {
					if(slowAgi) out.setAgitatorSpeed(400);
					else out.setAgitatorPower(-1);
				}
				
				//record the last time for the next loop
				prevTime = time;
			}
		} else { // if anything else happens make sure the shooter motor doesn't move
			
			if(fire) {//if fire pressed and not prime, just spin the agitator
				
				if(reverse){ //if reverse is pressed, spin it backwards
					//out.setAgitatorPower(1);
					out.setAgitatorSpeed(-800);
				} else {
					//out.setAgitatorPower(-1);					
					out.setAgitatorSpeed(800);
				}

			} else {
				out.setAgitatorPower(0);
			}
			
			out.setShooterPower(0);
			out.setTransportPower(0);
		}
	}
	
	public void justPrime(boolean prime){
		if(prime){
			out.setTransportPower(-0.2);
			out.setShooterSpeed(SHOOTER_SPEED + jogoffset);
		} else {
			out.setTransportPower(0);
			out.setShooterPower(0);
		}
	}

	public boolean upToSpeed() { // lets us know when we get the shooter motor up to the speed we set it to
		return out.shooterSpeedEncoder > shooterSetSpeed + jogoffset - ALLOWABLE_SHOOTER_ERROR;
	}
	
	public void agitate(double pwr){
		out.setAgitatorPower(pwr);
	}

	/*public void shooterFire(boolean fireButton) { // Moves the smaller roller, transports balls from hopper
		if (fireButton) { // when we hit that fire button
			//out.setTransportPower(0.75); // start up the transporter motor and start moving balls into the shooter motor
			out.setTransportSpeed(2000);
		} else { // if anything else happens make sure the transport motor doesn't move
			out.setTransportPower(0);
		}
	}*/

	boolean prevjogup = false;
	boolean prevjogdown = false;

	public void jog() { // allows slight adjustment of positions

		if (in.jogShooterUp && !prevjogup) { // if we hit the jog up button and didn't just hit jog up a moment ago
			jogoffset += JOG_AMOUNT; // add the jog constant to the jog offset of the motor
		} else if (in.jogShooterDown && !prevjogdown) { // if we hit jog down and didn't just hit jog down a moment ago

			jogoffset -= JOG_AMOUNT; // subtract the jog constant to the jog offset of the motor
		}

		prevjogup = in.jogShooterUp; // make sure we don't add more than once for button push
		prevjogdown = in.jogShooterDown; // or subtract

		//SmartDashboard.putNumber("JogValue", SHOOTER_SPEED + jogoffset); // put out the jog-adjusted value to the dashboard
	}
}