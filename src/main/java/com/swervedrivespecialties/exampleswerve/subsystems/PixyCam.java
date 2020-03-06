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
  public PixyCam() {
    pixy = Pixy2.createInstance(Pixy2.LinkType.SPI);
    pixy.init();
    pixy.setLamp((byte) 1, (byte) 1); // Turns the LEDs on
		pixy.setLED(255, 255, 255); // Sets the RGB LED white
  }

  public static Block getBiggestBlock() {
		// Gets the number of "blocks", identified targets, that match signature 1 on the Pixy2,
		// does not wait for new data if none is available,
		// and limits the number of returned blocks to 25, for a slight increase in efficiency
		int blockCount = pixy.getCCC().getBlocks(false, Pixy2CCC.CCC_SIG1, 25);
		// System.out.println("Found " + blockCount + " blocks!"); // Reports number of blocks found
		if (blockCount <= 0) {
			return null; // If blocks were not found, stop processing
		}
		ArrayList<Block> blocks = pixy.getCCC().getBlocks(); // Gets a list of all blocks found by the Pixy2
		Block largestBlock = null;
		for (Block block : blocks) { // Loops through all blocks and finds the widest one
			if (largestBlock == null) {
				largestBlock = block;
			} else if (block.getWidth() > largestBlock.getWidth()) {
				largestBlock = block;
			}
		}
		return largestBlock;
  }
  
  public double getProportionalX(){
    double proportionalX = ((getBiggestBlock().getX() - 157) / 157 );
    return proportionalX;
  }

  public double getProportionalY(){
    double proportionalY = ((getBiggestBlock().getY() - 103) / -103 );
    return proportionalY;
  }

  public double getTargetWidth(){
    double width = getBiggestBlock().getWidth();
    return width;
  }

  public double getTargetHeight(){
    double height = getBiggestBlock().getHeight();
    return height;
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
