import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.*;

import javax.imageio.ImageIO;

import com.pulispace.mc.ui.panorama.util.BigBufferedImage;


/**
 * Copyright 2019-2026 Johnathan Waugh
 * This class is hereby released under the Creative Commons Attribution license version 4.0
 * https://creativecommons.org/licenses/by/4.0/
 */
public class Photomosaic {
	static final int step=5;
	static final int tileSize=240;
	static final String tilePath="input";
	
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
	static void cleanOldTempFiles(File dir) throws IOException {
		if(!dir.isDirectory())throw new IOException();
		l:for(File file:dir.listFiles()) {
			if(file.isDirectory()&&file.getName().startsWith("buffer-")) {
				for(File f:file.listFiles())if(!f.getName().endsWith(".dat"))continue l;
				System.err.println("Deleting temp folder "+file.getName());
				for(File f:file.listFiles())f.delete();
				file.delete();
			}
		}
	}
	public static void main(String[]a) throws IOException{
		cleanOldTempFiles(new File(System.getProperty("java.io.tmpdir")));
		ImageSign.main(null);
		Scanner input=new Scanner(System.in);
		System.out.print("Enter input file:");
		BufferedImage in=ImageIO.read(new File(input.nextLine()));
		input.close();
		BufferedImage out=BigBufferedImage.create((in.getWidth()/step)*tileSize,(in.getHeight()/step)*tileSize,BufferedImage.TYPE_INT_RGB);
		final int numCol=in.getWidth()/step;
		ArrayList<int[]>colorCodes=new ArrayList<>();
		ArrayList<String>tiles=new ArrayList<>();
		HashMap<String,BufferedImage>map=new HashMap<>();
		Scanner read=new Scanner(new File("sign.txt"));
		while(read.hasNext()){
			tiles.add(read.next());
			colorCodes.add(new int[] {
					read.nextInt(16),
					read.nextInt(16),
					read.nextInt(16),
					read.nextInt(16),
					read.nextInt(16)
			});
		}
		read.close();
		System.out.printf("Loaded %s tiles\n", colorCodes.size());
		long ts=System.currentTimeMillis();
		for(int x=0;x<in.getWidth()/step;x++){
			for(int y=0;y<in.getHeight()/step;y++){
				Color[] c=new Color[] {
						calculateAverage(x*step,y*step,step,step,in),
						calculateAverage(x*step,y*step,step/2,step/2,in),
						calculateAverage(x*step+step/2,y*step,step/2,step/2,in),
						calculateAverage(x*step,y*step+step/2,step/2,step/2,in),
						calculateAverage(x*step+step/2,y*step+step/2,step/2,step/2,in)
				};
				int minD=Integer.MAX_VALUE;
				BufferedImage b=null;
				for(int j=0;j<colorCodes.size();j++){
					int[] tileCodes=colorCodes.get(j);
					int d=0;
					for(int k=0;k<5;k++) {
						d+=distanceSquared(c[k],new Color(tileCodes[k]));
					}
					d*=(1+0.04*Math.random());//Add some random variation
					
					if(d<minD){
						minD=d;
						if(map.containsKey(tiles.get(j))){
							b=map.get(tiles.get(j));
						}else{
							b=ImageIO.read(new File(tilePath+"/"+tiles.get(j)));
							map.put(tiles.get(j),b);
						}
						
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
		System.out.printf("Finished %s columns in %ss\n",numCol,(System.currentTimeMillis()-ts)/1000);
		System.out.printf("Used %s distinct tiles of %s\n",map.size(),colorCodes.size());
		System.out.print("Saving...");
		ts=System.currentTimeMillis();
		map=null;
		ImageIO.write(out,"jpg",new File("output.jpg"));
		out.flush();
		System.out.println(" saved in "+(System.currentTimeMillis()-ts)+"ms.");

	}
}