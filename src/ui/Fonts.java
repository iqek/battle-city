package ui;

import java.awt.Font;

public class Fonts {
    private static Font pixelFont;
    private static Font pixelOperator;

    public static Font get(float size) {
        if(pixelFont == null) {
            try {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT,
                    Fonts.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf"));
            } catch(Exception e) {
                System.err.println("could not load pixel font: " + e.getMessage());
                pixelFont = new Font("Monospaced", Font.PLAIN, 12);
            }
        }
        return pixelFont.deriveFont(size);
    }

    public static Font getOperator(float size) {
        if(pixelOperator == null) {
            try {
                pixelOperator = Font.createFont(Font.TRUETYPE_FONT,
                    Fonts.class.getResourceAsStream("/fonts/PixelOperator.ttf"));
            } catch(Exception e) {
                System.err.println("could not load pixel operator font: " + e.getMessage());
                pixelOperator = new Font("Monospaced", Font.PLAIN, 12);
            }
        }
        return pixelOperator.deriveFont(size);
    }
}
