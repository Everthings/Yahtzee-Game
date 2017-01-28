package Utilities;

import java.awt.Color;

public class GraphicsUtils {
	public static Color getGreyScale(Color c){
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		
		int greyScale = (int)(0.2989 * red + 0.5870 * green + 0.1140 * blue);
		
		return new Color(greyScale, greyScale, greyScale);
	}
}
