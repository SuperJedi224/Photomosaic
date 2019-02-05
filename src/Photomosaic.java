import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import com.pulispace.mc.ui.panorama.util.BigBufferedImage;


/**
 * Copyright 2019 Johnathan Waugh
 * This class is hereby released under the Creative Commons Attribution license version 4.0
 * https://creativecommons.org/licenses/by/4.0/
 */
public class Photomosaic {
	static final int step=7;
	static final int tileSize=160;
	public static Color calculateAverage(int x,int y,int w,int h,BufferedImage b){
		long tr=0,tg=0,tb=0;
		int a=w*h;
		for(int i=x;i<x+w;i++){
			for(int j=y;j<y+h;j++){
				try{Color c=new Color(b.getRGB(i, j));
				tr+=c.getRed();
				tg+=c.getGreen();
				tb+=c.getBlue();}catch(ArrayIndexOutOfBoundsException e){a--;}
			}
		}
		return new Color((int) (tr/a),(int) (tg/a),(int) (tb/a));	
	}
	public static int distanceSquared(Color c1,Color c2){
		int dr=c1.getRed()-c2.getRed(),dg=c1.getGreen()-c2.getGreen(),db=c1.getBlue()-c2.getBlue();
		return (dr*dr)+(dg*dg)+(db*db);
	}
	public static void main(String[]a) throws IOException{
		ImageSign.main(null);
		Scanner input=new Scanner(System.in);
		BufferedImage in=ImageIO.read(new File(input.nextLine()));
		input.close();
		BufferedImage out=BigBufferedImage.create((in.getWidth()/step)*tileSize,(in.getHeight()/step)*tileSize,BufferedImage.TYPE_INT_RGB);
		final int numCol=in.getWidth()/step;
		ArrayList<Integer>colorCodes=new ArrayList<>();
		ArrayList<BufferedImage>tiles=new ArrayList<>();
		Scanner read=new Scanner(new File("sign.txt"));
		while(read.hasNext()){
			String s=read.next();
			try{tiles.add(ImageIO.read(new File("Input/"+s)));}catch(IIOException e){
				System.err.println(s);
				throw e;
			}
			colorCodes.add(read.nextInt(16));
		}
		read.close();
		System.out.printf("Loaded %s tiles\n", colorCodes.size());
		for(int x=0;x<in.getWidth()/step;x++){
			for(int y=0;y<in.getHeight()/step;y++){
				Color c=calculateAverage(x*step,y*step,step,step,in);
				int minD=99999;
				BufferedImage b=null;
				for(int j=0;j<colorCodes.size();j++){
					int i=colorCodes.get(j);
					int d=distanceSquared(c,new Color(i));
					if(d<minD){
						minD=d;
						b=tiles.get(j);
					}
				}
				for(int x2=0;x2<tileSize;x2++){
					for(int y2=0;y2<tileSize;y2++){
						out.setRGB(x*tileSize+x2, y*tileSize+y2,b.getRGB(x2,y2));
					}
				}
			}
			System.out.printf("Finished column %s of %s\n",x+1,numCol);
		}
		System.out.print("Saving");
		ImageIO.write(out,"png",new File("output.png"));
	}
}