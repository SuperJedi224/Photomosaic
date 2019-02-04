# Note Before Compiling in Eclipse
The third-party library file BigBufferedImage.java (included) will not compile in Eclipse unless you change the Forbidden Reference compiler rules for your workspace to allow the use of sun.nio.ch.DirectBuffer.

# Input Files
Before running, please place the smaller images you wish to use as tiles in a directory named "Input" within the running directory. These tiles should ideally be square images of uniform size (the program as given here is configured for 160x160 tiles; if you wish to use tiles of a different size, simply change the tileSize value in Photomosaic.java and recompile.)

# License
The third-party library file BigBufferedImage.java is public domain under a CC0 public domain dedication (see the header of the appropriate file for more information.) All other source files included here are copyright 2019 Johnathan Waugh and are hereby released under the Creative Commons Attribution license version 4.0 (https://creativecommons.org/licenses/by/4.0/).

![](https://i.creativecommons.org/l/by/4.0/88x31.png)
