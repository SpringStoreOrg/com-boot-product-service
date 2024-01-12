package com.boot.product.service;

import com.boot.product.exception.EntityNotFoundException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ImageService {
    @Value("${image.directory}")
    private String imageDirectory;

    public byte[] getImage(String directory, String imageName) {
        File productImages = new File(imageDirectory, directory);
        if (!productImages.isDirectory()) {
            throw new EntityNotFoundException("Image directory "+directory+" does not exist");
        }
        try {
            File image = new File(productImages, imageName);
            return FileUtils.readFileToByteArray(image);
        } catch (IOException e) {
            throw new EntityNotFoundException("Image" + imageName + " does not exist");
        }
    }
}
