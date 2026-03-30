import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;

/**
 * Copyright 2019-2026 Johnathan Waugh
 * This class is hereby released under the Creative Commons Attribution license version 4.0
 * https://creativecommons.org/licenses/by/4.0/
 */

public class ImageSign {
	public static int calculateAverage(int x,int y,int w,int h,BufferedImage b){
		long tr=0,tg=0,tb=0;
		int a=w*h;
		for(int i=x;i<x+w;i++){
			for(int j=y;j<y+h;j++){
				Color c=new Color(b.getRGB(i, j));
				tr+=c.getRed();
				tg+=c.getGreen();
				tb+=c.getBlue();
			}
		}
		return new Color((int) (tr/a),(int) (tg/a),(int) (tb/a)).getRGB()&0xFFFFFF;	
	}
	public static void main(String[] args) throws IOException {
		File inFolder=new File("input");
		int i=0;
		long ts=System.currentTimeMillis();
		PrintStream output=new PrintStream("sign.txt");
		for(File imageFile:inFolder.listFiles()){
			BufferedImage image=ImageIO.read(imageFile);
			output.printf("%s %06X %06X %06X %06X %06X\n",
					imageFile.getName(),
					calculateAverage(0,0,Photomosaic.tileSize,Photomosaic.tileSize,image),
					calculateAverage(0,0,Photomosaic.tileSize/2,Photomosaic.tileSize/2,image),
					calculateAverage(Photomosaic.tileSize/2,0,Photomosaic.tileSize/2,Photomosaic.tileSize/2,image),
					calculateAverage(0,Photomosaic.tileSize/2,Photomosaic.tileSize/2,Photomosaic.tileSize/2,image),
					calculateAverage(Photomosaic.tileSize/2,Photomosaic.tileSize/2,Photomosaic.tileSize/2,Photomosaic.tileSize/2,image)
			);
			i++;
		}
		System.out.println("Calculated signatures for "+i+" tile images ("+Photomosaic.tileSize+"x"+Photomosaic.tileSize+"), in "+(System.currentTimeMillis()-ts)+"ms");
		output.close();
	}

}