package org.usfirst.frc.team910.robot.Vision;

public class Frame {
	Block[] blocks;
	int currentBlock;
	double time;
	int frameNumber;
	int numBlocks;

	public Frame() {
		currentBlock = numBlocks = 0;
		blocks = new Block[Camera.BLOCKS_PER_FRAME];
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new Block();
		}
	}
}
