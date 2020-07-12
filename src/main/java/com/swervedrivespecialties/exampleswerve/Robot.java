package com.swervedrivespecialties.exampleswerve;

// import com.swervedrivespecialties.exampleswerve.commands.AutoCommands.MoveAwayFromDriverStation;
// import com.swervedrivespecialties.exampleswerve.commands.AutoCommands.NullCommand;
// import com.swervedrivespecialties.exampleswerve.commands.ShooterCommands.RetractHood;
import com.swervedrivespecialties.exampleswerve.subsystems.*;
// import com.swervedrivespecialties.exampleswerve.utils.ColorChangeCounter;
// import com.swervedrivespecialties.exampleswerve.utils.GameData;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {
    public static OI oi;

    public static DrivetrainSubsystem drivetrain;
    // public static Intake intake;
    // public static Shooter shooter;
    // public static Tunnel tunnel;
    // public static Elevator elevator;
    // public static GameData gameData;
    // public static ColorWheel colorWheel;
    // public static ColorChangeCounter colorChangeCounter;
    public static Limelight limelight;
    public static PixyCam pixyCam;


    Command m_AutonomousCommand;
    SendableChooser<String> m_chooser = new SendableChooser<>();
    String SelectedCommand;

    public static OI getOi() {
        return oi;
    }

    @Override
    public void robotInit() {
        drivetrain = DrivetrainSubsystem.getInstance();
        // intake = Intake.getInstance();
        // shooter = Shooter.getInstance();
        // tunnel = Tunnel.getInstance();
        // elevator = Elevator.getInstance();
        // gameData = GameData.getInstance();
        // colorChangeCounter = ColorChangeCounter.getInstance();
        // colorWheel = ColorWheel.getInstance();
        limelight = Limelight.getInstance();
        pixyCam = PixyCam.getInstance();
        // pixyCam = new PixyCam();
        oi = new OI();

        // UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
        // camera1.setVideoMode(PixelFormat.kMJPEG, 160, 120, 20);// pixel format, x pixels, y pixels, FPS

        m_chooser.setDefaultOption("DriveFromLine", "DriveFromLine");
        // m_chooser.addOption("DriveFromLine", "DriveFromLine");
        m_chooser.addOption("Null Command", "Null Command");
        m_chooser.addOption("DriveFromLine", "DriveFromLine");
        SmartDashboard.putData("Auto Mode", m_chooser);
        
        
        // limelight.enableLED();
    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
        // SmartDashboard.putNumber("ShooterRPMS", shooter.getRPMS());
    //    SmartDashboard.putNumber("SteerCommand", limelight.getSteerCommand());
    SmartDashboard.putNumber("tv", NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0));
    SmartDashboard.putNumber("tx", NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0));
    // SmartDashboard.putNumber("PixyCommand", pixyCam.getProportionalX());
    // SmartDashboard.putNumber("Pixy x", pixyCam.getBiggestBlock().getX());
        // SmartDashboard.putBoolean("PixyIsConnected?", value)
}


    public void disabledInit(){
        // limelight.disableLED();
    }

    public void disabledPeriodic(){
    //    SmartDashboard.putNumber("PixyX", pixyCam.getAbsoluteX());
       SmartDashboard.putBoolean("HasTarget", pixyCam.PixyHasTarget());
       SmartDashboard.putNumber("TargetCount", pixyCam.PixyTargetCount());
    }

    public void autonomousInit(){
        drivetrain.resetGyroscope();
        // drivetrain.gyroscope.zeroYaw();
        // elevator.RetractElevator();
        

        String SelectedCommand = (String) m_chooser.getSelected();
        switch(SelectedCommand){
            case "Null Command":
                // m_AutonomousCommand = new NullCommand();
                break;
            case "DriveFromLine": 
                // m_AutonomousCommand = new MoveAwayFromDriverStation();
                break;
            default: 
                // m_AutonomousCommand = new MoveAwayFromDriverStation();
             }
        // new MoveAwayFromDriverStation();
        if (m_AutonomousCommand != null){
            m_AutonomousCommand.start();
        }

    }

    public void autonomousPeriodic(){

    }

    
    public void teleopInit(){
       
        // new RetractHood();
        
    }

    public void teleopPeriodic(){
        // SmartDashboard.putNumber("Pixy x", pixyCam.getProportionalX());
        // SmartDashboard.putNumber("PixyX", pixyCam.getAbsoluteX());
        SmartDashboard.putBoolean("HasTarget", pixyCam.PixyHasTarget());
        SmartDashboard.putNumber("TargetCount", pixyCam.PixyTargetCount());


    }
}
