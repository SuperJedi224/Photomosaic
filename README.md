# Note Before Compiling in Eclipse
The third-party library file BigBufferedImage.java (included) will not compile in Eclipse unless you change the Forbidden Reference compiler rules for your workspace to allow the use of sun.nio.ch.DirectBuffer.

# Input Files
Before running, please place the smaller images you wish to use as tiles in a directory named "Input" within the running directory. These tiles should ideally be square images of uniform size (the program as given here is configured for 160x160 tiles; if you wish to use tiles of a different size, simply change the tileSize value in Photomosaic.java and recompile.)

# Use
Once you have placed your image tiles in the Input folder and compiled the java source with the appropriate values for `tileSize` and `step`, simply run the program and input the filename of your big image on STDIN. Note that processing your image and outputing the final mosaic may take some time.

# Limitations
While this program is unlikely to have any serious memory issues due to the use of BigBufferedImage rather than the standard BufferedImage class, it still fails if the final image would be larger than about 2 gigapixels due to other limitations involved in the image processing library. If this occurs, try using a larger value for `step` or using smaller image tiles. I don't currently know of any way around this limitation.

# Credits
This project uses the library file BigBufferedImage.java by Zsolt Pocze and Dimitry Polivaev of Team Puli Space, which is available under a CC0 public domain dedication. You can find a copy at http://worksheetsoftware.com/resource/blog/java/BigBufferedImage.java.

# License
The third-party library file BigBufferedImage.java is public domain under a CC0 public domain dedication (see the header of the appropriate file for more information.) All other source files included here are copyright 2019 Johnathan Waugh and are hereby released under the Creative Commons Attribution license version 4.0 (https://creativecommons.org/licenses/by/4.0/).

![](https://i.creativecommons.org/l/by/4.0/88x31.png)
