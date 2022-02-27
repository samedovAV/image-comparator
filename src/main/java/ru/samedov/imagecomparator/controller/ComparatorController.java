package ru.samedov.imagecomparator.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.samedov.imagecomparator.dto.ImagesDto;

import java.util.Arrays;


@Controller
public class ComparatorController {

    // Функция compare, принимает на вход два изображения в base64, выдаёт результат сравнения
    @PostMapping("/compare")
    public String compareImages(Model model, ImagesDto dto) {
        String result = "";
        //String regex = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
        if (Base64.isBase64(dto.getFirstImage()) && Base64.isBase64(dto.getSecondImage())) {
            String[] firstImage = Arrays.toString(Base64.decodeBase64(dto.getFirstImage())).split("");
            String[] secondImage = Arrays.toString(Base64.decodeBase64(dto.getSecondImage())).split("");

            // расчет процента схожести
            int match = 0;
            int min = Math.min(firstImage.length, secondImage.length);

            for (int i = 0; i < min; i++) {
                if (firstImage[i].equals(secondImage[i])) {
                    match++;
                }
            }

            result = "The result is: " + (match * 100 / min) + "%";
        } else {
            result = "The result is: false";
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
