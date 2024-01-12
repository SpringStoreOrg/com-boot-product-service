package com.boot.product.controller;

import com.boot.product.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

@Controller
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;

    @GetMapping(value = "/{slug}/image/{imageName}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable("slug") String productSlug, @PathVariable("imageName") String imageName) {
        MediaType mediaType = getMimeType(imageName);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageService.getImage(productSlug, imageName));
    }

    private MediaType getMimeType(String imageName) {
        File file = new File(imageName);
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        return MediaType.valueOf(fileTypeMap.getContentType(file.getName()));
    }
}
