/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.swervedrivespecialties.exampleswerve.subsystems;

import java.util.ArrayList;

import com.swervedrivespecialties.exampleswerve.RobotMap;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.Pixy2.LinkType;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.*;
import io.github.pseudoresonance.pixy2api.links.I2CLink;
import io.github.pseudoresonance.pixy2api.links.SPILink;


public class PixyCam extends SubsystemBase {
  /**
   * Creates a new Pixy.
   */
  private static Pixy2 pixy;
  private static PixyCam pixyCam;
  private boolean hasTarget;
  private int blockCount;
  
  public PixyCam() {
    
    // System.out.println("pixy version is "+ pixy.getVersion());
    initPixy();
    hasTarget = false;
  }
private void initPixy(){
  pixy = Pixy2.createInstance(new SPILink());
    pixy.init();
    pixy.setLamp((byte) 1, (byte) 1); // Turns the LEDs on
    pixy.setLED(255, 255, 255); // Sets the RGB LED white

}
  public Block getBiggestBlock() {
		// Gets the number of "blocks", identified targets, that match signature 1 on the Pixy2,
		// does not wait for new data if none is available,
		// and limits the number of returned blocks to 25, for a slight increase in efficiency
		 blockCount = pixy.getCCC().getBlocks(false, Pixy2CCC.CCC_SIG1, 25);
    // System.out.println("Found " + blockCount + " blocks!"); // Reports number of blocks found
    // SmartDashboard.putNumber("BlockCount", blockCount);
		if (blockCount <= 0) {
      hasTarget = false;
      return null; // If blocks were not found, stop processing
    }
    if(blockCount > 0){
      hasTarget = true;
    }
		ArrayList<Block> blocks = pixy.getCCC().getBlocks(); // Gets a list of all blocks found by the Pixy2
		Block largestBlock = null;
		for (Block block : blocks) { // Loops through all blocks and finds the widest one
			if (largestBlock == null) {
				largestBlock = block;
			} else if (block.getWidth()  > largestBlock.getWidth() ){
				largestBlock = block;
			}
		}
		return largestBlock;
  }
  
  public double getProportionalX(){
    double proportionalX;
    getBiggestBlock();
    // converts pixy grid to a value between +/- 1 centered around the center of the frame (makes it behave like limelight)
    if(hasTarget){
       proportionalX = ((getBiggestBlock().getX() - 157) / 157 );
    }
    else {proportionalX = 0.0; 
      // System.out.println("No Block Found!");
    }
    return proportionalX;
  }
  public double getAbsoluteX(){
    double absoluteX;
    getBiggestBlock();
    if(hasTarget){
      absoluteX = (getBiggestBlock().getX());
    }
    else {
      absoluteX = 1.234567890;
    }
    return absoluteX;
  }
  public int PixyTargetCount(){
    getBiggestBlock();
    return blockCount;
  }
public boolean PixyHasTarget(){
  getBiggestBlock();
  if(hasTarget) {return true;}
  else {return false;}
}
  public double getProportionalY(){
    double proportionalY;
    if(hasTarget){
      proportionalY = ((getBiggestBlock().getY() - 103) / -103 );
    }
     else {proportionalY = 0.0; 
      // System.out.println("No Block Found!");
    }
    return proportionalY;
  }

  public double getTargetWidth(){
    getBiggestBlock();
    double width;
    if(hasTarget){
      width = getBiggestBlock().getWidth();
    }
    else width = 0.0;
    return width;
  }

  public double getTargetHeight(){
    double height;
    getBiggestBlock();
    if(hasTarget){
      height = ((getBiggestBlock().getY() - 103) / -103 );
    }
     else height = 0.0;
    return height;
  }
  public double getTargetArea(){
    double area;
    getBiggestBlock();
    if (hasTarget){
       area = getBiggestBlock().getHeight() * getBiggestBlock().getWidth();
    }
    else area = 0.0;
    return area;
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public static PixyCam getInstance(){
    if (pixyCam == null) {
      pixyCam = new PixyCam();
  }
  return pixyCam;
  }
}
