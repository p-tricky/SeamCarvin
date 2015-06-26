import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SeamCarver {
	private Picture pic;
	private int energyTable[][];
	private Color[][] colorTable;
	private int[] seam;
	private final static int MAX_ENERGY = 3061;
	private final static String PIC_SMALL = "./Ando-Hiroshige-11th-station-Mishima.JPG";
	private final static String PIC_TINY = "./12x10.png";

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		pic= new Picture(picture);
		colorTable = new Color[pic.height()][pic.width()];
		for(int i = 0; i < pic.height();i++)
			for(int j = 0; j < pic.width(); j++)
				colorTable[i][j]=pic.get(j, i);
		energyTable = getEnergyTable();
	}
	
	// current picture
	public Picture picture() {
		return this.pic;
	}
	
	// width of color array 
	public int width() {
		return colorTable[0].length;
	}

	// height of color array
	public int height() {
		return colorTable.length;
	}

	// energy of pixel at column x and row y
	public int energy(int x, int y) {
		int height = height(); 
		int width = width(); 
		Color self, above, below, left, right;
		self = colorTable[y][x];
		above = (y == 0) ? colorTable[y+1][x] : colorTable[y-1][x];
		below = (y == colorTable.length-1) ? colorTable[y-1][x] : colorTable[y+1][x];
		left = (x == 0) ? colorTable[y][x+1] : colorTable[y][x-1];
		right = (x == colorTable[0].length-1) ? colorTable[y][x-1] : colorTable[y][x+1];
		
		return gradientFunction(self, above) + gradientFunction(self, below) + 
				gradientFunction(self, left) + gradientFunction(self, right);
	}
	
	// creates and returns a new energy table
	int[][] getEnergyTable() {
		int[][] table = new int[height()][width()];
		for (int i=0; i<height(); i++) {
			for (int j=0; j<width(); j++) {
				table[i][j] = energy(j, i);
			}
		}
		return table;
	}
	
	ArrayList<Integer> getVertParentsEnergy(int x, int y, int[][] table) {
		int centralParent, leftParent=MAX_ENERGY*y, rightParent=MAX_ENERGY*y;
		centralParent = table[y-1][x];
		if (x!=0) leftParent = table[y-1][x-1];
		if (x!=table[0].length-1) rightParent = table[y-1][x+1];
		ArrayList<Integer> parents = new ArrayList<Integer>(3);
		parents.add(leftParent);
		parents.add(centralParent);
		parents.add(rightParent);
		return parents;
	}

	ArrayList<Integer> getHorizParentsEnergy(int x, int y, int[][] table) {
		int centralParent, lowerParent=MAX_ENERGY*x, upperParent=MAX_ENERGY*x;
		centralParent = table[y][x-1];
		if (y!=0) upperParent = table[y-1][x-1];
		if (y!=table.length-1) lowerParent = table[y+1][x-1];
		ArrayList<Integer> parents = new ArrayList<Integer>(3);
		parents.add(upperParent);
		parents.add(centralParent);
		parents.add(lowerParent);
		return parents;
	}

	public int gradientFunction(Color c1, Color c2) {
		int redDiff = Math.abs(c1.getRed() - c2.getRed());
		int blueDiff = Math.abs(c1.getBlue() - c2.getBlue());
		int greenDiff = Math.abs(c1.getGreen() - c2.getGreen());
		return redDiff + blueDiff + greenDiff;
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		int rows=height(), cols=width(), parentEnergy, minParentIndex;
		int seam[] = new int[cols];
		ArrayList<Integer> parents = new ArrayList<Integer>();
		int cumulativeEnergyTable[][] = new int[rows][cols];
		for (int i=0; i<rows; i++)
			cumulativeEnergyTable[i][0] = energyTable[i][0];
		for (int j=1; j<cols; j++) {
			for (int i=0; i<rows; i++) {
				parents = getHorizParentsEnergy(j, i, cumulativeEnergyTable);
				parentEnergy = Collections.min(parents);
				cumulativeEnergyTable[i][j] = energyTable[i][j] + parentEnergy;
			}
		}
		// now backtrack from bottom to top
		int seamPixel = 0;
		for (int i=1; i<rows; i++) {
			if (cumulativeEnergyTable[i][cols-1] < cumulativeEnergyTable[i][seamPixel]) {
				seamPixel = i;
			}
		}
		int seamIndex = cols-1;
		seam[seamIndex] = seamPixel;
		for (int i=cols-1; i>0; i--) {
			parents = getHorizParentsEnergy(i, seamPixel, cumulativeEnergyTable);
			parentEnergy = Collections.min(parents);
			minParentIndex = parents.indexOf(parentEnergy);
			seamPixel += minParentIndex-1;
			seam[--seamIndex] = seamPixel;
		}
		return seam;
	}
	
	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		int rows=height(), cols=width(), parentEnergy, minParentIndex;
		int seam[] = new int[rows];
		ArrayList<Integer> parents = new ArrayList<Integer>();
		int cumulativeEnergyTable[][] = new int[rows][cols];
		cumulativeEnergyTable[0] = energyTable[0];
		for (int i=1; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				parents = getVertParentsEnergy(j, i, cumulativeEnergyTable);
				parentEnergy = Collections.min(parents);
				cumulativeEnergyTable[i][j] = energyTable[i][j] + parentEnergy;
			}
		}
		// now backtrack from bottom to top
		int seamPixel = 0;
		for (int i=1; i<cols; i++) {
			if (cumulativeEnergyTable[rows-1][i] < cumulativeEnergyTable[rows-1][seamPixel]) {
				seamPixel = i;
			}
		}
		int seamIndex = rows-1;
		seam[seamIndex] = seamPixel;
		for (int i=rows-1; i>0; i--) {
			parents = getVertParentsEnergy(seamPixel, i, cumulativeEnergyTable);
			parentEnergy = Collections.min(parents);
			minParentIndex = parents.indexOf(parentEnergy);
			seamPixel += minParentIndex-1;
			seam[--seamIndex] = seamPixel;
		}
		return seam;
	}

	
	// reset arrays after finding vertical seam
	public void verticalUpdateArrays()
	{
		Color[][] newColor = new Color[height()][width()-1];
		int k = 0;
		for(int i = 0; i < height();i++) {
			for(int j = 0; j < width();j++)
			{
				if (j != seam[i])
				{
					newColor[i][k] = colorTable[i][j];
					k++;
				}
			}
			k = 0;
		}
		colorTable = newColor;
		energyTable = getEnergyTable();
	}
	
	// reset arrays after finding horizontal seam
	public void horizontalUpdateArrays()
	{
		Color[][] newColor = new Color[height()-1][width()];
		int k = 0;
		for(int i = 0; i < width();i++) {
			for(int j = 0; j < height();j++) {
				if (j != seam[i])
				{
					newColor[k][i] = colorTable[j][i];
					k++;
				}
			}
			k=0;
		}
		colorTable = newColor;
		energyTable = getEnergyTable();
	}

	// remove vertical seam from current picture
	public void updatePic() {
		int rows = colorTable.length;
		int cols = colorTable[0].length;
		pic = new Picture(cols, rows);
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				pic.set(j, i, colorTable[i][j]);
			}
		}
	}
	
	public void shrinkVertical(int rows) {
		for (int i=0; i<rows; i++) {
			seam = findVerticalSeam();
			verticalUpdateArrays();
		}
	}

	public void shrinkHorizontal(int cols) {
		for (int i=0; i<cols; i++) {
			seam = findHorizontalSeam();
			horizontalUpdateArrays();
		}
		
	}
	
	/////////////////// Debug Methods //////////////////
	public void printTable(int table[][]) {
		for (int y=0; y<table.length; y++) {
			for (int x=0; x<table[0].length; x++) {
				System.out.printf("%5d ", table[y][x]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void showVerticalSeam(int[] seam, Picture picCopy) {
		if (picCopy == null) picCopy = new Picture(pic);
		for (int i=0; i<seam.length; i++) {
			picCopy.set(seam[i], i, Color.red);
		}
		picCopy.show();
	}
	
	public void showHorizontalSeam(int[] seam, Picture picCopy) {
		if (picCopy == null) picCopy = new Picture(pic);
		for (int i=0; i<seam.length; i++) {
			picCopy.set(i, seam[i], Color.red);
		}
		picCopy.show();
	}

    public static void main(String[] args) {
        Picture picture = new Picture(PIC_SMALL);
        SeamCarver sc = new SeamCarver(picture);
        //int[] vSeam =  sc.findVerticalSeam();
        //int[] hSeam =  sc.findHorizontalSeam();
        //sc.showVerticalSeam(hSeam, null);
        //sc.showHorizontalSeam(hSeam, null);
        sc.shrinkHorizontal(164);
        sc.shrinkVertical(200);
        sc.updatePic();
        sc.pic.save("11th-Station-500x300.JPG");
    }
	
}
