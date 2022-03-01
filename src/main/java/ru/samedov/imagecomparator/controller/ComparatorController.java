package ru.samedov.imagecomparator.controller;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.samedov.imagecomparator.dto.ImagesDto;
import ru.samedov.imagecomparator.service.ComparatorService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;


@Controller
public class ComparatorController {

    // Функция compare, принимает на вход два изображения в base64, выдаёт результат сравнения
    @PostMapping("/compare")
    public String compareImages(Model model, ImagesDto dto) {
        String result = "";
        //String regex = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
        if (Base64.isBase64(dto.getFirstImage()) && Base64.isBase64(dto.getSecondImage())) {
            int match = 0;
            BufferedImage firstImage;
            BufferedImage secondImage;
            try {
                firstImage = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(dto.getFirstImage())));
                secondImage = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(dto.getSecondImage())));

                // Картинки должны быть одного размера
                if (firstImage.getWidth() != secondImage.getWidth() || firstImage.getHeight() != secondImage.getHeight()) {
                    result = "The images must be the same size";
                } else {
                    int width = firstImage.getWidth();
                    int height = firstImage.getHeight();

                    int area = width * height;

                    int[][] imageMatrixFirst = ComparatorService.convertTo2DUsingGetRGB(firstImage);
                    int[][] imageMatrixSecond = ComparatorService.convertTo2DUsingGetRGB(secondImage);

                    for (int i = 0; i < imageMatrixFirst.length; i++) {
                        for (int j = 0; j < imageMatrixFirst[i].length; j++) {
                            // Попиксельное сравнение
                            if (imageMatrixFirst[i][j] == imageMatrixSecond[i][j]) {
                                match++;
                            }
                        }
                    }

                    // Расчет процента схожести
                    result = "The result is: " + (match * 100 / area) + "%";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            result = "Images are not in base64";
        }
        model.addAttribute("result", result);
        return "index";
    }

    @GetMapping("/")
    public String homePage(Model model) {
        ImagesDto imagesDto = new ImagesDto();
        model.addAttribute("imagesDto", imagesDto);
        //model.addAttribute("result", result);
        return "index";
    }

}
