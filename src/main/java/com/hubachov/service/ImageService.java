package com.hubachov.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    int analyzeImages(List<MultipartFile> images) throws IOException;

}
