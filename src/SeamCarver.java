import java.awt.Color;


public class SeamCarver {
	private Picture pic;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		pic= new Picture(picture);
		
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
	
	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		
	}
	
	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		
	}
	
}
