package com.boot.product.service;

import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${image.directory}")
    private String imageDirectory;
    private final ImageRepository imageRepository;

    public byte[] getImage(String directory, String imageName) {
        File productImages = new File(imageDirectory, directory);
        if (!productImages.isDirectory()) {
            throw new EntityNotFoundException("Image directory "+directory+" does not exist");
        }
        try {
            File image = new File(productImages, imageName);
            return FileUtils.readFileToByteArray(image);
        } catch (IOException e) {
            throw new EntityNotFoundException("Image " + imageName + " does not exist");
        }
    }

    public void saveImage(String directory, MultipartFile inputFile) {
        File productImages = new File(imageDirectory, directory);
        if (!productImages.isDirectory()) {
            productImages.mkdir();
        }
        try {
            File image = new File(productImages, inputFile.getOriginalFilename());
            FileUtils.writeByteArrayToFile(image, inputFile.getBytes());
        } catch (IOException e) {
            throw new EntityNotFoundException("Image " + inputFile.getOriginalFilename() + " could not be written");
        }
    }

    @Transactional
    public boolean deleteImage(String productSlug, String filename) {
        File productImages = new File(imageDirectory, productSlug);
        if (!productImages.isDirectory()) {
            throw new EntityNotFoundException("Directory " + productSlug + " could not be found");
        }
        File image = new File(productImages, filename);
        if (image.exists()) {
            boolean deleted = image.delete();
            imageRepository.removeImageBySlugAndName(productSlug, filename);
            return deleted;
        }

        throw new EntityNotFoundException("Image " + filename + " could not be found");
    }
}
