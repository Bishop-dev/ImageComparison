package com.hubachov.utils.image.converter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class PixelArrayConverter {

    public static int[][] convertBufferedImageToPixelArray(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.drawImage(inputImage, 0, 0, null);
        graphics.dispose();
        int[] line = new int[width * height];
        image.getRGB(0, 0, width, height, line, 0, width);
        int[][] array = new int[height][width];
        int index = 0;
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                array[x][y] = line[index];
                index++;
            }
        }
        return array;
    }

}
