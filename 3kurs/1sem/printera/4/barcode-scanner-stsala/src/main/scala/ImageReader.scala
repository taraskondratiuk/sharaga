import java.awt.image.BufferedImage
import java.io.File


import Constants._
import javax.imageio.ImageIO


object ImageReader {
    
    def getFullBinaryCode(path: String): String = {
        val image = getImageFromFile(path)
        val yCoord = image.getHeight / 2
        
        var firstDark = false
        var pixelsFromFirstLeftDark = Seq.empty[PixelWithCoord]
        for (i <- 0 until image.getWidth) {
            val color = getLightOrDarkColor(image.getRGB(i, yCoord))
            if (color equals DARK_COLOR) firstDark = true
            if (firstDark) pixelsFromFirstLeftDark = pixelsFromFirstLeftDark :+ PixelWithCoord(i, color)
        }
        
        firstDark = false
        var pixelsFromFirstRightDark = Seq.empty[PixelWithCoord]
        for (i <- image.getWidth - 1 to 0 by -1) {
            val color = getLightOrDarkColor(image.getRGB(i, yCoord))
            if (color equals DARK_COLOR) firstDark = true
            if (firstDark) pixelsFromFirstRightDark = pixelsFromFirstRightDark :+ PixelWithCoord(i, color)
        }
        
        val codePixels = pixelsFromFirstLeftDark.intersect(pixelsFromFirstRightDark)
        
        codePixels.map(_.color).reduce(_ + _)
    }
    
    private def getLightOrDarkColor(color: Int): String = {
        val r = (color >> 16) & 0xff
        val g = (color >> 8) & 0xff
        val b = color & 0xff
        if ((r + g + b) < 383) DARK_COLOR else LIGHT_COLOR
    }
    
    private def getImageFromFile(path: String): BufferedImage = {
        val file = new File(path)
        ImageIO.read(file)
    }
}
