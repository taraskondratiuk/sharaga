import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;

public class PictureReader {

    public static List<Integer> getFullBinaryCode(String path) throws IOException {
        BufferedImage image = getImageFromFile(path);

        int yCoord = image.getHeight() / 2;

        boolean firstDark = false;

        List<Pixel> pixelsFromFirstLeftDark = new ArrayList<>();
        for (int i = 0; i < image.getWidth(); i++) {
            int color = getLightOrDarkColor(image.getRGB(i, yCoord));

            if (color == AppConstants.DARK_COLOR) {
                firstDark = true;
            }

            if (firstDark) {
                pixelsFromFirstLeftDark.add(new Pixel(i, color));
            }
        }

        firstDark = false;
        List<Pixel> pixelsFromFirstRightDark = new ArrayList<>();
        for (int i = image.getWidth() - 1; i >= 0; i--) {
            int color = getLightOrDarkColor(image.getRGB(i, yCoord));

            if (color == AppConstants.DARK_COLOR) {
                firstDark = true;
            }

            if (firstDark) {
                pixelsFromFirstRightDark.add(new Pixel(i, color));
            }
        }

        pixelsFromFirstLeftDark.retainAll(pixelsFromFirstRightDark);

        return pixelsFromFirstLeftDark.stream().map(v -> v.color).collect(Collectors.toList());
    }


    private static int getLightOrDarkColor(int color) {
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = color & 0xff;

        return (r + g + b) < 383 ? AppConstants.DARK_COLOR : AppConstants.LIGHT_COLOR;
    }

    private static BufferedImage getImageFromFile(String path) throws IOException {
        File file = new File(path);
        return ImageIO.read(file);
    }
}
