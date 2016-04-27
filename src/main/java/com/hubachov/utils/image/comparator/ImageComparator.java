package com.hubachov.utils.image.comparator;

import com.hubachov.utils.image.converter.PixelArrayConverter;
import com.hubachov.utils.image.entity.Point;
import com.hubachov.utils.image.loader.ImageLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ImageComparator {
    // common settings
    private static final int DIFFERENCE_RATE = 10;
    private static final int COLOR_MAX_VALUE = Color.BLACK.getRGB();
    private static final int MINIMAL_ALLOWED_DISTANCE = 100;
    private static final String JPG_FORMAT = "jpg";
    // setting for each comparison
    private int[][] firstArray;
    private int[][] secondArray;
    private int width, height;
    private List<Point> pointsWithDifferenсes = new ArrayList<>();
    private List<ArrayList<Point>> areasOfDifference = new ArrayList<>();
    private BufferedImage resultImage;

    private ImageComparator() {

    }

    public static ImageComparator newInstance() {
        return new ImageComparator();
    }

    public int compareTwoImages(String firstImageFilePath,
                                 String secondImageFilePath,
                                 String resultImageFilePath) {
        ImageLoader imageLoader = new ImageLoader();
        BufferedImage img1 = imageLoader.loadImage(firstImageFilePath);
        BufferedImage img2 = imageLoader.loadImage(secondImageFilePath);
        firstArray = PixelArrayConverter.convertBufferedImageToPixelArray(img1);
        secondArray = PixelArrayConverter.convertBufferedImageToPixelArray(img2);
        try {
            compare(resultImageFilePath);
            return areasOfDifference.size();
        } catch (IOException ioe) {
            throw new RuntimeException("Can't analyze pixel arrays", ioe);
        }
    }

    private void compare(String resultImageFilePath) throws IOException {
        height = firstArray.length;
        width = firstArray[0].length;
        resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        analyzePixels();
        findDifferentAreas();
        drawFrames();
        ImageIO.write(resultImage, JPG_FORMAT, new File(resultImageFilePath));
    }

    /**
     * This method compares two appropriate pixels on images.
     * If difference between them is more than 10%, add them to data structure
     */
    private void analyzePixels() {
        IntStream.range(0, height).forEach(i -> {
            IntStream.range(0, width).forEach(j -> {
                resultImage.setRGB(j, i, secondArray[i][j]);
                double firstPercentage = calculateDifferenceInPercentage(firstArray[i][j]);
                double secondPercentage = calculateDifferenceInPercentage(secondArray[i][j]);
                if (Math.abs(firstPercentage - secondPercentage) > DIFFERENCE_RATE) {
                    pointsWithDifferenсes.add(new Point(j, i));
                }
            });
        });
    }

    /**
     * This methods detects how many areas with differences in input images
     */
    private void findDifferentAreas() {
        pointsWithDifferenсes.forEach(point -> {
            if (areasOfDifference.isEmpty()) {
                areasOfDifference.add(new ArrayList<Point>() {{
                    add(pointsWithDifferenсes.get(0));
                }});
            } else {
                if (!checkMinimalAllowedDistance(point)) {
                    areasOfDifference.add(new ArrayList<Point>() {{
                        add(point);
                    }});
                }
            }
        });
    }

    /**
     * This method checks distance between target point
     * and other point that marked as "different"
     * @param point - target point
     * @return
     */
    private boolean checkMinimalAllowedDistance(Point point) {
        for (int i = 0; i < areasOfDifference.size(); i++) {
            for (int j = 0; j < areasOfDifference.get(i).size(); j++) {
                int x = areasOfDifference.get(i).get(j).getX();
                int y = areasOfDifference.get(i).get(j).getY();
                if ((point.getX() < x + MINIMAL_ALLOWED_DISTANCE)
                        && (point.getX() > x - MINIMAL_ALLOWED_DISTANCE)
                        && (point.getY() < y + MINIMAL_ALLOWED_DISTANCE)
                        && (point.getY() > y - MINIMAL_ALLOWED_DISTANCE)) {
                    areasOfDifference.get(i).add(point);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * For each area of difference this method detects corner pixels
     * to draw red rectangle
     */
    private void drawFrames() {
        IntStream.range(0, areasOfDifference.size()).forEach(i -> {
            int left = Integer.MAX_VALUE, right = 0, top = Integer.MAX_VALUE, bottom = 0;
            for (int j = 0; j < areasOfDifference.get(i).size(); j++) {
                Point point = areasOfDifference.get(i).get(j);
                if (point.getX() < left) {
                    left = point.getX();
                }
                if (point.getX() > right) {
                    right = point.getX();
                }
                if (point.getY() > bottom) {
                    bottom = point.getY();
                }
                if (point.getY() < top) {
                    top = point.getY();
                }
            }
            Graphics2D g2d = resultImage.createGraphics();
            g2d.setColor(Color.RED);
            g2d.drawRect(left, top, right - left, bottom - top);
            g2d.dispose();
        });
    }

    private double calculateDifferenceInPercentage(int value) {
        return Math.abs((double) value / COLOR_MAX_VALUE * 100);
    }

}
