/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.swervedrivespecialties.exampleswerve.commands.VisionDriveCommands;

import com.swervedrivespecialties.exampleswerve.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.geometry.Translation2d;

public class FollowBall extends Command {
  public FollowBall() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double forward = (50 / Robot.pixyCam.getTargetWidth());
    // forward = Utilities.deadband(forward);
    // Square the forward stick
    // forward = Math.copySign(Math.pow(forward, 2.0), forward);

    double strafe = 0.0;
    // strafe = Utilities.deadband(strafe);
    // Square the strafe stick
    // strafe = Math.copySign(Math.pow(strafe, 2.0), strafe);

    double rotation = Robot.pixyCam.getProportionalX();
    // rotation = Utilities.deadband(rotation);
    // // Square the rotation stick
    // rotation = Math.copySign(Math.pow(rotation, 2.0), rotation);

    Robot.drivetrain.drive(new Translation2d(forward, strafe), rotation, false);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
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
