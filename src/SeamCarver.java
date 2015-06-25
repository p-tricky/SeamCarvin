import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class SeamCarver {
	private Picture pic;
	private int energyTable[][];
	private boolean[][] keepArray;
	private final static int MAX_ENERGY = 3061;
	private final static String PIC_SMALL = "./Ando-Hiroshige-11th-station-Mishima.JPG";
	private final static String PIC_TINY = "./12x10.png";

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		pic= new Picture(picture);
		energyTable = getEnergyTable();
		keepArray = new boolean[pic.height()][pic.width()];
		Arrays.fill(keepArray,true);
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
		self = pic.get(x, y);
		above = (y == 0) ? pic.get(x, y+1) : pic.get(x, y-1);
		below = (y == pic.height()-1) ? pic.get(x, y-1) : pic.get(x, y+1);
		left = (x == 0) ? pic.get(x+1, y) : pic.get(x-1, y);
		right = (x == pic.width()-1) ? pic.get(x-1, y) : pic.get(x+1, y);
		
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

	// return array of n horizontal seams
	public int[][] findHorizontalSeams(int n) {
		return null;
		
	}
	
	// return array of n vertical seams
	public int[][] findVerticalSeams(int n) {
		return null;
		
	}
	
	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		
	}
	
	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		
	}
	
	public Picture shrinkVertical(Picture pic, int numRows) {
		return pic;
		
	}

	public Picture shrinkHorizontal(Picture pic, int numCols) {
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
