package ru.samedov.imagecomparator.service;

import org.springframework.stereotype.Service;
import ru.samedov.imagecomparator.dto.ImagesDto;

import java.awt.image.BufferedImage;
import java.util.Base64;

@Service
public class ComparatorService {


    public static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = image.getRGB(col, row);
            }
        }

        return result;
    }

}
