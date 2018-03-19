package worldUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import com.flowpowered.noise.*;
import com.flowpowered.noise.module.Module;
import com.flowpowered.noise.module.source.Perlin;


public class WorldUtils {
	/*//inshortform
	public static double makecontintentmappoint(int x, int y,
			OpenSimplexNoise rangeNoise, OpenSimplexNoise shapeNoise, OpenSimplexNoise baseNoise)
	{
		double scale = 2<<19;
		double imountrangesize = 2;
		double ifisrangesize = 3;
		double icontinentsize = 1;
		float mheight = (float) (rangeNoise.eval(((float)x)/(scale/imountrangesize),
				((float)y)/(scale/imountrangesize)));
		mheight = 1f-Math.abs(mheight);
		float fheight = (float) (rangeNoise.eval(((float)(x))/(scale/ifisrangesize),//same scale, different area
				((float)y)/(scale/ifisrangesize)));
		fheight =  -(float)Math.pow(1 - Math.abs(fheight),3);
		float cheight = (float) (shapeNoise.eval(((float)x)/(scale/icontinentsize),
				((float)y)/(scale/icontinentsize)));
		cheight = (cheight+1)/2;
		double[][] LayerWeightsAndiSizes = new double[][]	{
			{2<<2  , 2<<3  , 2<<4   , 2<<5   , 2<<6   , 2<<7   , 2<<8   ,},	
			{17    , 15    , 13     , 10     , 7      , 5      , 3      ,}
		};
		float layerweighttotal = 0;
		float rheight = 0;
		for(int i = 0; i < LayerWeightsAndiSizes[0].length; i++)
		{
			double layersize = scale/LayerWeightsAndiSizes[0][i];
			double layerweight =LayerWeightsAndiSizes[1][i];			
			double lheight = (baseNoise.eval(x/layersize,  (y/layersize)));
			lheight *= layerweight;
			rheight += lheight;
			layerweighttotal += LayerWeightsAndiSizes[1][i];
		}
		rheight = rheight/layerweighttotal;
		rheight = (rheight+1)/2;
		float height = mheight*0.2f  + fheight*0.1f + rheight*0.45f + cheight*0.45f;
		//height = fheight;
		return Math.pow(height, 4);
	}*/

	//65536 is the numbers in a short
	public static short DoubleToShort(double doub)
	{
		return (short)(doub*Short.MAX_VALUE);
		//return (short)(doub*65536 + Short.MIN_VALUE);
	}

	public static double ShortToDouble(short shor)
	{
		return ((double)shor)/Short.MAX_VALUE;
		//return (((double)shor)-Short.MIN_VALUE)/(65536);
	}

	/**
	 * 
	 * @param noise
	 * @param scale the scale of the noise.
	 * @param xSize the x size of the shortMap
	 * @param ySize the y size of the shortMap
	 * @return A ShortMap with size xSize by ySize representing the noise
	 */
	public static ShortMap noise(Module noise, int xSize, int ySize)
	{
		short[][] map = new short[xSize][ySize]; 
		for(int x = 0; x < xSize; x++)
		{
			for(int y = 0; y < ySize; y++)
			{
				map[x][y] = DoubleToShort(noise.getValue(x, y, 0));
			}
		}
		return new ShortMap(map);		
	}

	
	
	/**
	 * 
	 * @param map the map to be processed
	 * @return returns a map that has been negated
	 */
	public static ShortMap negate(ShortMap map)
	{
		int xSize = map.getXSize();
		int ySize = map.getYSize();
		short[][] oldmap = map.getMap();
		short[][] newmap = new short[xSize][ySize];
		for(int x = 0; x < xSize; x++)
		{
			for(int y = 0; y < ySize; y++)
			{
				newmap[x][y] = (short) -oldmap[x][y];
			}
		}
		return new ShortMap(oldmap);
	}

	/**
	 * 
	 * @param value the level of the surface
	 * @param xSize the x size of the ShortMap
	 * @param ySize the y size of the ShortMap
	 * @return a ShortMap which is flat
	 */
	public static ShortMap constantValue(double value, int xSize, int ySize)
	{
		short[][] map = new short[xSize][ySize]; 
		for(int x = 0; x < xSize; x++)
		{
			for(int y = 0; y < ySize; y++)
			{
				map[x][y] = DoubleToShort(value);
			}
		}
		return new ShortMap(map);
	}

	/**
	 * Raise all values on the map by a certain amount;
	 * @param map The map to perform the operation on
	 * @param pow
	 * @return
	 */
	public static ShortMap pow(ShortMap map, double pow)
	{ 
		int xSize = map.getXSize();
		int ySize = map.getYSize();
		short[][] oldmap = map.getMap();
		short[][] newmap = new short[xSize][ySize]; 
		double value;
		for(int x = 0; x < xSize; x++)
		{
			for(int y = 0; y < ySize; y++)
			{
				value = ShortToDouble(oldmap[x][y]);
				newmap[x][y] = DoubleToShort(Math.pow(value, pow));
			}
		}
		return new ShortMap(oldmap);
	}


	public static ShortMap scale(ShortMap map, double multiplier)
	{
		int xSize = map.getXSize();
		int ySize = map.getYSize();
		short[][] oldmap = map.getMap();
		short[][] newMap = new short[xSize][ySize];

		for(int x = 0; x < xSize; x++)
		{
			for(int y = 0; y < ySize; y++)
			{
				newMap[x][y] =  (short)ShortMap.clamp(oldmap[x][y]*multiplier, Short.MIN_VALUE, Short.MAX_VALUE);
			}
		}
		return new ShortMap(newMap);
	}
	
	public static ShortMap addOffset(ShortMap map1, ShortMap map2, int xoff, int yoff)
	{
		int xSize1 = map1.getXSize();
		int ySize1 = map1.getYSize();

		int xSize2 = map2.getXSize();
		int ySize2 = map2.getYSize();
		
		short[][] mapSum = new short[xSize1][ySize1];
		
		int sum = 0;
		int x2,y2; //the coordinates on the second map
		for(int x = 0; x < xSize1; x++)
		{
			for(int y = 0; y < ySize1; y++)
			{
				sum = map1.get(x, y);
				x2 = x-xoff;
				y2 = y-yoff;
				if(x2 >= 0 && y2 >= 0 && x2 < xSize2 && y2 < ySize2)
				{
					sum += map2.get(x,y);
				}
				mapSum[x][y] = (short)ShortMap.clamp(sum, Short.MIN_VALUE, Short.MAX_VALUE);
			}
		}
		return new ShortMap(mapSum);
	}
	
	


	/**
	 * 
	 * @param maps the maps to add (must be same size) 
	 * @return The sum of the maps (will be clamped between 1 and 0)
	 */
	public static ShortMap add(ShortMap[] maps)
	{
		int xSize = maps[0].getXSize();
		int ySize = maps[0].getYSize();

		short[][] mapSum = new short[xSize][ySize];
		long sum = 0;
		for(int x = 0; x < xSize; x++)
		{
			for(int y = 0; y < ySize; y++)
			{
				sum = 0;
				for(int i = 0; i < maps.length; i++)
				{
					sum += maps[i].get(x, y);
				}
				mapSum[x][y] = (short)ShortMap.clamp(sum, Short.MIN_VALUE, Short.MAX_VALUE);
			}
		}
		return new ShortMap(mapSum);
	}

	/**
	 * 
	 * @param maps the maps to average (must be same size)
	 * @param weights the weights of each map (Should all add up to 1, with each individual weight being in between 1 and 0)
	 * @return The average of the maps
	 */
	public static ShortMap weightedAverage(ShortMap[] maps, double[] weights)
	{
		for(int i = 0; i < weights.length; i++)
		{
			maps[i] = scale(maps[i],weights[i]);
		}
		return add(maps);
	}
	
	
	
	
	public static void FastGaussianBlur(int[] source, int[] output, int width, int height, int radius) {
		ArrayList<Integer> gaussianBoxes = CreateGausianBoxes(radius, 3);
		BoxBlur(source, output, width, height, (gaussianBoxes.get(0) - 1) / 2);
		BoxBlur(output, source, width, height, (gaussianBoxes.get(1) - 1) / 2);
		BoxBlur(source, output, width, height, (gaussianBoxes.get(2) - 1) / 2);
	}

	public static ArrayList<Integer> CreateGausianBoxes(double sigma, int n) {
		double idealFilterWidth = Math.sqrt((12 * sigma * sigma / n) + 1);

		int filterWidth = (int) Math.floor(idealFilterWidth);

		if (filterWidth % 2 == 0) {
			filterWidth--;
		}

		int filterWidthU = filterWidth + 2;

		double mIdeal = (12 * sigma * sigma - n * filterWidth * filterWidth - 4 * n * filterWidth - 3 * n) / (-4 * filterWidth - 4);
		double m = Math.round(mIdeal);

		ArrayList<Integer> result = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			result.add(i < m ? filterWidth : filterWidthU);
		}

		return result;
	}

	
	public static ShortMap blur(ShortMap map, int radius)
	{
		return new ShortMap(blurImage(map.getImage(), radius));
	}
	
	private static BufferedImage blurImage(BufferedImage image, int radius) {
		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		int[] changedPixels = new int[pixels.length];

		FastGaussianBlur(pixels, changedPixels, width, height, radius);

		BufferedImage newImage = new BufferedImage(width, height, image.getType());
		newImage.setRGB(0, 0, width, height, changedPixels, 0, width);

		return newImage;
	}
	
	private static void BoxBlur(int[] source, int[] output, int width, int height, int radius) {
		System.arraycopy(source, 0, output, 0, source.length);
		BoxBlurHorizontal(output, source, width, height, radius);
		BoxBlurVertical(source, output, width, height, radius);
	}

	private static void BoxBlurHorizontal(int[] sourcePixels, int[] outputPixels, int width, int height, int radius) {
		int resultingColorPixel;
		float iarr = 1f / (radius + radius);
		for (int i = 0; i < height; i++) {
			int outputIndex = i * width;
			int li = outputIndex;
			int sourceIndex = outputIndex + radius;

			int fv = Byte.toUnsignedInt((byte) sourcePixels[outputIndex]);
			int lv = Byte.toUnsignedInt((byte) sourcePixels[outputIndex + width - 1]);
			float val = (radius) * fv;

			for (int j = 0; j < radius; j++) {
				val += Byte.toUnsignedInt((byte) (sourcePixels[outputIndex + j]));
			}

			for (int j = 0; j < radius; j++) {
				val += Byte.toUnsignedInt((byte) sourcePixels[sourceIndex++]) - fv;
				resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
				outputPixels[outputIndex++] = (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel);
			}

			for (int j = (radius + 1); j < (width - radius); j++) {
				val += Byte.toUnsignedInt((byte) sourcePixels[sourceIndex++]) - Byte.toUnsignedInt((byte) sourcePixels[li++]);
				resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
				outputPixels[outputIndex++] = (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel);
			}

			for (int j = (width - radius); j < width; j++) {
				val += lv - Byte.toUnsignedInt((byte) sourcePixels[li++]);
				resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
				outputPixels[outputIndex++] = (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel);
			}
		}
	}

	private static void BoxBlurVertical(int[] sourcePixels, int[] outputPixels, int width, int height, int radius) {
		int resultingColorPixel;
		float iarr = 1f / (radius + radius + 1);
		for (int i = 0; i < width; i++) {
			int outputIndex = i;
			int li = outputIndex;
			int sourceIndex = outputIndex + radius * width;

			int fv = Byte.toUnsignedInt((byte) sourcePixels[outputIndex]);
			int lv = Byte.toUnsignedInt((byte) sourcePixels[outputIndex + width * (height - 1)]);
			float val = (radius + 1) * fv;

			for (int j = 0; j < radius; j++) {
				val += Byte.toUnsignedInt((byte) sourcePixels[outputIndex + j * width]);
			}
			for (int j = 0; j <= radius; j++) {
				val += Byte.toUnsignedInt((byte) sourcePixels[sourceIndex]) - fv;
				resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
				outputPixels[outputIndex] = (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel);
				sourceIndex += width;
				outputIndex += width;
			}
			for (int j = radius + 1; j < (height - radius); j++) {
				val += Byte.toUnsignedInt((byte) sourcePixels[sourceIndex]) - Byte.toUnsignedInt((byte) sourcePixels[li]);
				resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
				outputPixels[outputIndex] = (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel);
				li += width;
				sourceIndex += width;
				outputIndex += width;
			}
			for (int j = (height - radius); j < height; j++) {
				val += lv - Byte.toUnsignedInt((byte) sourcePixels[li]);
				resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
				outputPixels[outputIndex] = (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel);
				li += width;
				outputIndex += width;
			}
		}
	}
}