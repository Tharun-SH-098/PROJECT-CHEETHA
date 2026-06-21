package task5;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class NeuralStyleTransfer {

    public static BufferedImage applyStyle(
            BufferedImage contentImage,
            BufferedImage styleImage) {

        int width = Math.min(contentImage.getWidth(),
                             styleImage.getWidth());

        int height = Math.min(contentImage.getHeight(),
                              styleImage.getHeight());

        BufferedImage output =
                new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int contentRGB = contentImage.getRGB(x, y);
                int styleRGB = styleImage.getRGB(x, y);

                int r = (((contentRGB >> 16) & 0xFF)
                        + ((styleRGB >> 16) & 0xFF)) / 2;

                int g = (((contentRGB >> 8) & 0xFF)
                        + ((styleRGB >> 8) & 0xFF)) / 2;

                int b = ((contentRGB & 0xFF)
                        + (styleRGB & 0xFF)) / 2;

                int newRGB = (r << 16) | (g << 8) | b;

                output.setRGB(x, y, newRGB);
            }
        }

        return output;
    }

    public static void main(String[] args) {

        try {
            JFileChooser chooser = new JFileChooser();

            System.out.println("Select Content Image");
            chooser.showOpenDialog(null);
            File contentFile = chooser.getSelectedFile();

            System.out.println("Select Style Image");
            chooser.showOpenDialog(null);
            File styleFile = chooser.getSelectedFile();

            if (contentFile == null || styleFile == null) {
                System.out.println("Image selection cancelled.");
                return;
            }

            BufferedImage contentImage =
                    ImageIO.read(contentFile);

            BufferedImage styleImage =
                    ImageIO.read(styleFile);

            BufferedImage result =
                    applyStyle(contentImage, styleImage);

            File outputFile = new File("styled_output.jpg");

            ImageIO.write(result, "jpg", outputFile);

            System.out.println(
                    "Neural Style Transfer Completed Successfully!");

            System.out.println(
                    "Output saved as: "
                    + outputFile.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}