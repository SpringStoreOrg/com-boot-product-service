package com.boot.product.controller;

import com.boot.product.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @Value("${product.service.url}")
    private String productServiceUrl;

    @GetMapping(value = "/{slug}/image/{imageName}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable("slug") String productSlug, @PathVariable("imageName") String imageName) {
        MediaType mediaType = getMimeType(imageName);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageService.getImage(productSlug, imageName));
    }

    @DeleteMapping(value = "/{slug}/image/{imageName}")
    public ResponseEntity deleteImage(@PathVariable("slug") String productSlug, @PathVariable("imageName") String imageName) {
        imageService.deleteImage(productSlug, imageName);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{slug}/image")
    @ResponseBody
    public ResponseEntity<String> saveImage(@PathVariable("slug") String productSlug, @RequestParam("image") MultipartFile file) {
        imageService.saveImage(productSlug, file);
        return ResponseEntity.ok()
                .body(String.format("%s/%s/image/%s", productServiceUrl, productSlug, file.getOriginalFilename()));
    }

    private MediaType getMimeType(String imageName) {
        File file = new File(imageName);
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        return MediaType.valueOf(fileTypeMap.getContentType(file.getName()));
    }
}
