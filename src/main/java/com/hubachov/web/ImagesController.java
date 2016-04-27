package com.hubachov.web;

import com.hubachov.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;


@RestController
public class ImagesController {

    @Autowired
    private ImageService imageService;


    @RequestMapping(value = {"/", "/index", "/uploadImages"}, method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/uploadImages", method = RequestMethod.POST)
    public ModelAndView uploadImages(
            @RequestParam("firstImg") MultipartFile firstMultipartImage,
            @RequestParam("secondImg") MultipartFile secondMultipartImage) {
        ModelAndView mav = new ModelAndView("index");
        try {
            imageService.analyzeImages(Arrays.asList(firstMultipartImage, secondMultipartImage));
        } catch (Exception e) {
            return mav.addObject("errorMessage", e.getMessage());
        }
        mav.addObject("successMessage", "Images compared successfully");
        return mav;
    }

}
