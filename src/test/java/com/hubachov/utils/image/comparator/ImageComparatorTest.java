package com.hubachov.utils.image.comparator;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ImageComparatorTest {

    private File img1;
    private File img2;
    private String image1Path;
    private String image2Path;
    private String resultImagePath;

    @Before
    public void before() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        img1 = new File(classLoader.getResource("imgs/1.jpg").getFile());
        img2 = new File(classLoader.getResource("imgs/2.jpg").getFile());
        image1Path = img1.getCanonicalPath();
        image2Path = img2.getCanonicalPath();
        resultImagePath = img1.getParent() + "/result.jpg";
    }

    @After
    public void cleanUp() {
        new File(resultImagePath).delete();
    }

    @Test
    public void testSameImages() throws IOException {
        int areas = ImageComparator.newInstance()
                .compareTwoImages(image1Path, image1Path, resultImagePath);
        // if we compare same with image with itself, we expect zero different areas
        Assert.assertEquals(0, areas);
    }

    @Test
    public void testImagesWithThreeDifferentRegion() {
        int areas = ImageComparator.newInstance()
                .compareTwoImages(image1Path, image2Path, resultImagePath);
        Assert.assertEquals(3, areas);
    }

    @Test(expected = RuntimeException.class)
    public void testNonExistingImages() {
        ImageComparator.newInstance()
                .compareTwoImages("/notexist.jpg", "/notexist.jpg", "/result.jpg");
    }

}
