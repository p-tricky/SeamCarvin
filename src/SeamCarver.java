import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

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
	
	// width of current picture
	public int width() {
		return pic.width();
	}

	// height of current picture
	public int height() {
		return pic.height();
	}

	// energy of pixel at column x and row y
	public int energy(int x, int y) {
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
		int height = pic.height();
		int width = pic.width();
		int[][] table = new int[pic.height()][pic.width()];
		for (int i=0; i<pic.height(); i++) {
			for (int j=0; j<pic.width(); j++) {
				table[i][j] = energy(j, i);
			}
		}
		return table;
	}
	
	ArrayList<Integer> getParentsEnergy(int x, int y) {
		ArrayList<Integer> parents = new ArrayList<Integer>();
		if (y == 0) return parents;
		parents.add(energyTable[y-1][x]);
		if (x!=0) parents.add(energyTable[y-1][x-1]);
		if (x!=pic.width()-1) parents.add(energyTable[y-1][x+1]);
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
		return null;
		
	}
	
	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		return null;
		
	}
	
	public void verticalUpdateArrays()
	{
		Color[][] newColor = new Color[energyTable.length][energyTable[0].length-1];
		int k = 0;
		for(int i = 0; i < newColor.length;i++)
			for(int j = 0; j < newColor[0].length;j++)
			{
				if (j != seam[i])
				{
					newColor[i][k] = colorTable[i][j];
					k++;
				}
			}
		colorTable = newColor;
		energyTable = getEnergyTable();
	}
	
	public void horizontalUpdateArrays()
	{
		Color[][] newColor = new Color[energyTable.length-1][energyTable[0].length];
		int k = 0;
		for(int i = 0; i < newColor[0].length;i++)
			for(int j = 0; j < newColor.length;j++)
			{
				if (j != seam[i])
				{
					newColor[k][i] = colorTable[j][i];
					k++;
				}
			}
		colorTable = newColor;
		energyTable = getEnergyTable();
	}
	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int rows) {
		for (int i = 0; i < rows; i++)
		{
			findHorizontalSeam();
			horizontalUpdateArrays();
		}
		shrinkHorizontal();
	}
	
	// remove vertical seam from current picture
	public void removeVerticalSeam(int cols) {
		
	}
	
	public Picture shrinkVertical() {
		return pic;
		
	}

	public Picture shrinkHorizontal() {
		return pic;
		
	}
	
	/////////////////// Debug Methods //////////////////
	public void printEnergyTable() {
		for (int y=0; y<pic.height(); y++) {
			for (int x=0; x<pic.height(); x++) {
				System.out.printf("%5d ", energyTable[y][x]);
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
        //sc.printEnergyTable();
        System.out.println(sc.getParentsEnergy(3, 0));
        System.out.println(sc.getParentsEnergy(3, 2));
        System.out.println(sc.getParentsEnergy(0, 2));
        int[] vSeam = new int[picture.height()];
        Arrays.fill(vSeam, 50);
        sc.showVerticalSeam(vSeam, null);
    }
	
}
