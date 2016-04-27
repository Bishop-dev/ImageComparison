package com.hubachov.service.impl;

import com.hubachov.service.ImageService;
import com.hubachov.utils.image.comparator.ImageComparator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String IMG_PATH_PREFIX = "src/main/webapp/imgs/";

    @Value("#{'${source.images}'.split(',')}")
    private List<String> imgFileNames;

    @Value("${result.image}")
    private String resultImageFile;

    @Override
    public void analyzeImages(List<MultipartFile> images) throws IOException {
        cleanUpDirectory();
        saveImagesToFiles(images);
        ImageComparator.instance().compareTwoImages(IMG_PATH_PREFIX + imgFileNames.get(0),
                IMG_PATH_PREFIX + imgFileNames.get(1),
                IMG_PATH_PREFIX + resultImageFile);
    }

    private void cleanUpDirectory() throws IOException {
        FileUtils.cleanDirectory(new File(IMG_PATH_PREFIX));
    }

    private void saveImagesToFiles(List<MultipartFile> images) {
        IntStream.range(0, images.size()).forEach(i -> {
            File imgFile = new File(IMG_PATH_PREFIX + imgFileNames.get(i));
            try {
                imgFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(imgFile);
                fos.write(images.get(i).getBytes());
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException("Can't save images on disk", e);
            }
        });
    }

}
