package com.boot.product.service;

import com.boot.product.exception.EntityNotFoundException;
import com.boot.product.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${image.directory}")
    private String imageDirectory;
    @Value("${full.image.size}")
    private int fullImageSize;
    private final ImageRepository imageRepository;

    public byte[] getImage(String directory, String imageName) {
        File productImages = new File(imageDirectory, directory);
        if (!productImages.isDirectory()) {
            throw new EntityNotFoundException("Image directory " + directory + " does not exist");
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
            resizeImage(productImages, inputFile);
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


    private void resizeImage(File productImages, MultipartFile inputFile) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputFile.getInputStream());
        BufferedImage finalImage = Scalr.resize(inputImage, fullImageSize);
        ImageIO.write(finalImage, FilenameUtils.getExtension(inputFile.getOriginalFilename()), new File(productImages, inputFile.getOriginalFilename()));
    }
}
