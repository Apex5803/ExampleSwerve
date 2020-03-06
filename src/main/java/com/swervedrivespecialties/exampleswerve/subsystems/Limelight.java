/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.swervedrivespecialties.exampleswerve.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.swervedrivespecialties.exampleswerve.RobotMap;

import edu.wpi.first.cameraserver.*;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.networktables.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class Limelight extends SubsystemBase {
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
//   private Solenoid ledRing; 
    public boolean HasValidTarget = false;
    // double m_LimeLightDriveCommand = 0.0;
    public double SteerCommand = 0.0;
    public double ShooterAngle = 0.0;
    private static Limelight instance;
  /**
   * Creates a new Limelight.
   */
  public Limelight() {
    // ledRing = new Solenoid(RobotMap.PDP2ID, RobotMap.LEDRingID);
  }


  public void UpdateLimelightTracking(){

    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

    if(tv == 1.0){
      HasValidTarget = true;
      ShooterAngle = ty;
      SteerCommand = tx / 29.8; //converts tx to steer comand between -1 and 1 so that its easier
    }
    else{
      
      HasValidTarget = false;
      SteerCommand = 0.0;
      ShooterAngle = 0.0;
    }
    
  }
  public double getSteerCommand(){
    UpdateLimelightTracking();
    return SteerCommand;
    
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
  }

//   public void enableLED(){
//     ledRing.set(true);
//   }

//   public void disableLED(){
//     ledRing.set(false);
//   }
  public static Limelight getInstance() {
    if (instance == null) {
        instance = new Limelight();
    }

    return instance;
  
  }
}
