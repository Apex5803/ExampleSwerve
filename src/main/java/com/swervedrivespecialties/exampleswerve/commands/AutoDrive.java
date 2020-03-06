/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.swervedrivespecialties.exampleswerve.commands;

import com.swervedrivespecialties.exampleswerve.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDrive extends Command {
  Translation2d translation;
double turn;
double currentHeading;
double deltaAngle;
double deltaAnglePostMath;
boolean fieldOriented;
double timeout;
double CS_Count;
  public AutoDrive(Translation2d translation, double turnAngle, boolean fieldOriented, double timeoutSeconds) {
    // Use addRequirements() here to declare subsystem dependencies.
    CS_Count = 0;
    this.translation = translation;
    this.turn = turnAngle;    
    this.fieldOriented = fieldOriented;
    this.timeout = timeoutSeconds * 100;

  }


  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
       
    deltaAngle = turn - Robot.drivetrain.getRealAngle();
    if   (deltaAngle > 180){
      deltaAnglePostMath = ((deltaAngle - 360)/180);
    }
     else if (deltaAngle < -180){
      deltaAnglePostMath = ((360 + deltaAngle)/180);
    }
     else deltaAnglePostMath = (deltaAngle/180);
    CS_Count = CS_Count + 2;
    if(CS_Count <= timeout){
      Robot.drivetrain.drive(translation, deltaAnglePostMath, fieldOriented);
     }
     SmartDashboard.putNumber("TimeOut", timeout / 100);
     SmartDashboard.putNumber("ElapsedSeconds", CS_Count / 100);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if (this.CS_Count >= this.timeout){
      return true;
    }
    else return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
