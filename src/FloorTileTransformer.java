import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import mhframework.media.MHResourceManager;
import mhframework.tilemap.MHIsoMouseMap;


public class FloorTileTransformer
{
    public FloorTileTransformer(String filename, int divisionsX, int divisionsY) throws IOException
    {
        Image originalImage = MHResourceManager.loadImage(filename);
        int w = originalImage.getWidth(null)/divisionsX;
        int h = originalImage.getHeight(null)/divisionsY;

        for (int row = 0; row < divisionsY; row++)
            for (int col = 0; col < divisionsX; col++)
            {
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
                bi.getGraphics().drawImage(originalImage, 0, 0, w, h, row*h, col*w, row*h+h, col*w+w, null);
                
                BufferedImage im = transformFloorTile(bi);
                File output = new File(filename + "_transformed_" + col + "_" + row + ".png");
                ImageIO.write(im, "png", output);
            }
    }
    
    /****************************************************************
    *
    * @param image
    * @return
    */
   private BufferedImage transformFloorTile(final Image image)
   {
       final int w = MHIsoMouseMap.WIDTH;
       final int h = MHIsoMouseMap.HEIGHT;
       final int originalSize = image.getWidth(null);
       final int bufSize = (int) Math.sqrt(2 * Math.pow(originalSize, 2));  // Pythagorean theorem.
       final Image buffer = new BufferedImage(bufSize, bufSize, BufferedImage.TYPE_INT_ARGB_PRE);
       final Graphics2D g = (Graphics2D) buffer.getGraphics();

       // Rotate image.
       g.rotate(45 * (Math.PI / 180.0), bufSize/2, bufSize/2);
       final int offset = bufSize/2 - originalSize/2 ;
       g.drawImage(image, offset, offset, originalSize, originalSize, null);

       // Scale image.
       final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
       final Graphics2D rg = (Graphics2D) result.getGraphics();
       rg.drawImage(buffer, 0, 0, w, h, null);

       return result;
   }

    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.err.println("ERROR:  Please specify input filename and number of horizontal and vertical divisions.");
            System.exit(0);
        }
        
        String filename = args[0];
        int divisionsX = Integer.parseInt(args[1]);
        int divisionsY = Integer.parseInt(args[2]);
        
        try
        {
            FloorTileTransformer t = new FloorTileTransformer(filename, divisionsX, divisionsY);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
