package com.github.gergerapex1.MCExternalConsoleTerminal.utils;

import java.awt.*;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class AsciiArt {
    public static String generateAsciiArt(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        image.getGraphics().setFont(Font.getFont("Courier New"));
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        graphics2D.drawString(text, 6, (int) (height * 0.70));
        StringBuilder asciiText = new StringBuilder();
        for (int y = 0; y < height; y++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int x = 0; x < width; x++) {
                stringBuilder.append(image.getRGB(x, y) == -16777216 ? " " : "*");
            }

            if (stringBuilder.toString()
                    .trim()
                    .isEmpty()) {
                continue;
            }

            asciiText.append(stringBuilder).append("\n");
        }
        return String.valueOf(asciiText);
    }
}
