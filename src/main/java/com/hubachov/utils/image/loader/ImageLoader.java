package com.hubachov.utils.image.loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageLoader {

    public BufferedImage loadImage(String path) {
        File file = new File(path);
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("Reading error", e);
        }
    }

}
