package task4;

import java.util.Random;

public class Pix2PixCGAN {

    private static final int SIZE = 5;

    public static int[][] generateImage() {
        Random random = new Random();
        int[][] image = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                image[i][j] = random.nextInt(256);
            }
        }
        return image;
    }

    public static int[][] translateImage(int[][] input) {
        int[][] output = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                output[i][j] = 255 - input[i][j];
            }
        }
        return output;
    }

    public static void displayImage(int[][] image) {
        for (int[] row : image) {
            for (int pixel : row) {
                System.out.printf("%4d", pixel);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        System.out.println("Original Image:");

        int[][] inputImage = generateImage();
        displayImage(inputImage);

        System.out.println("\nTranslated Image (Pix2Pix Simulation):");

        int[][] outputImage = translateImage(inputImage);
        displayImage(outputImage);

        System.out.println("\nImage-to-Image Translation Completed.");
    }
}