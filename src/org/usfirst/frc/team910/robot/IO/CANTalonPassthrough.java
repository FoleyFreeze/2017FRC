package org.usfirst.frc.team910.robot.IO;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class CANTalonPassthrough {

	private TalonSRX controller;
	private ControlMode mode = ControlMode.PercentOutput;
	
	public CANTalonPassthrough(int channel) {
		controller = new TalonSRX(channel);
	}

	public void setFeedbackDevice(FeedbackDevice ctreMagencoderRelative) {
		controller.configSelectedFeedbackSensor(ctreMagencoderRelative, 0, 0);
	}

	public void setEncPosition(int i) {
		controller.setSelectedSensorPosition(0, i, 0);
	}

	public void reverseSensor(boolean b) {
		controller.setSensorPhase(b);
	}

	public void setInverted(boolean b) {
		controller.setInverted(b);
	}

	public void enableBrakeMode(boolean b) {
		if(b) {
			controller.setNeutralMode(NeutralMode.Brake);
		} else {
			controller.setNeutralMode(NeutralMode.Coast);
		}
	}

	public void configPeakOutputVoltage(double i, double j) {
		if(Math.abs(i) == Math.abs(j)) {
			controller.configClosedLoopPeakOutput(0, Math.abs(i), 0);
		}
	}

	public void reverseOutput(boolean b) {
		controller.setInverted(b);
	}

	public void setPID(double p, double i, double d, double f, int j, int k, int l) {
		controller.config_kP(0, p, 0);
		controller.config_kI(0, i, 0);
		controller.config_kD(0, d, 0);
		controller.config_kF(0, f, 0);
	}

	public void setPID(double p, double i, double d) {
		controller.config_kP(0, p, 0);
		controller.config_kI(0, i, 0);
		controller.config_kD(0, d, 0);
	}

	public void set(double d) {
		controller.set(mode, d);
	}

	public void changeControlMode(ControlMode percentoutput) {
		mode = percentoutput;
	}

	public int getSpeed() {
		return controller.getSelectedSensorVelocity(0);
	}

	public void setSetpoint(double speed) {
		if(mode == ControlMode.Position || mode == ControlMode.Velocity) {
			controller.set(mode, speed);
		}
	}

	public double getPosition() {
		return controller.getSelectedSensorPosition(0);
	}

	public double getOutputVoltage() {
		return controller.getMotorOutputVoltage();
	}
	
	
	
}
