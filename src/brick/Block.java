package brick;

import java.awt.Color;

public class Block {
	private Color color;
	
	
	public Block(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public String toString() {
		return color.toString();
	}
		
	
}
