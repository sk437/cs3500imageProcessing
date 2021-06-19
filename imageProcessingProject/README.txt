README:
Project Creators: Marko Krstulovic and Kyle Sacks


ABOUT/OVERVIEW:
Sometimes, images have to edited or transformed in order to fit the needs of the people using them.
As such, it is necessary to have software that can facilitate this, allowing for image editing.

This program serves as a simple image processing software. It allows for image manipulation and enhancement.
This program also allows for the use of single or layered images for processing.

LIST OF FEATURES:
- Support for PPM, JPEG, PNG, and any image files supported by ImageIO library.
- Simple Image Creation (Programmatic, Copying, Empty, Transparent, and From Existing Files)
- Simple Image Color Transformation(Greyscale, Sepia) and Filtering(Blurring, Sharpening)
- Saving Simple Images as Files
- Creating Layered Images (New Empty Layered Images, From Specific File Type)
- Adding New Layers To A Layered Image Where Size Is Same As Layered Image
- Adding New Layers As Copy Of Existing Layer In Layered Image
- Adding New Layers From Existing Images Of Same Size As Destination Layered Image
- Layer Manipulation (Color Transformations and Filtering on Images)
- Removing Layers From Layered Images
- Moving Layers Within a Layered Image
- Updating Visibility of Layers in Layered Images
- Loading Images As Current Workspace Images
- Loading Layers As Current Workspace Layers
- Updating Pixel Colors for Simple Images and Layers of Layered Images
- Saving Simple Images and Layers as Individual Files
- Saving Layered Images as File Structures Housed in a Single Directory
- Saving Layered Images as Simple Images Through Blending Layers Together

PROJECT STRUCTURE:

Package imageasgraph:

Interface GraphOfPixels: Defines the required methods for a representation of an image as a graph of
pixels - represented this way because it can be iterated over in the same time as a double array,
and even though it takes slightly longer to get a particular pixel, this representation is much
more flexible for resizing if that is a later addition(which seems likely for image processing).

Abstract Class AbstractGraphOfPixels: Defines the required package-specific methods needed for a
representation of an image as a graph, used for initializing the first node of a graph.

Class SimpleGraphOfPixels: Represents an image as a graph of pixels, with functionality as defined
by the interface and abstract class above.

Class ImageToGraphConverter: Builder class for SimpleGraphOfPixels - contains static methods
which take images or file names as input, and converts those images into graphs.

Enum OutputType: Represents the possible output formats of a graph.


Interface Node: Defines the required methods for a representation of a pixel of an image as a Node,
which can access/change it's colors as well as point to neighboring Nodes - used to build a graph.

Abstract Class AbstractNode: Defines the required package-specific methods needed for a
representation of a pixel as a node - these methods involve updating the neighbors of the Node.

Class EmptyNode: Represents a sort of null node - used as the borders of an image.

Class PixelNode: Represents a node with neighbors and RGB values.

Class Utils: Miscellaneous utility methods.
___________________
Package imageinput:
Package imageProgram:

Interface ImageProgram: Defines the required methods for a program that can be used to create an
image by following a particular algorithm.

Class CheckerBoard: Represents an image program which is used to generate a checkerboard image.
_________
Package layeredImage:

Interface LayeredImage: Defines the required methods for a representation of an image as several layers of graphs of
pixels.

Class LayeredImageV0: Represents an instance of images layered on top of one another, allowing the individual manipulation of specific layers.

Class LayeredImageIterator: Represents the method of iterating throguh a Layered Image.

Class LayerData: Represent the package-specific information of a Layer in a Layered Image, describing position, image stored, and visibility.


Package blend:

Interface Blend: Defines the required methods for a program that can be used to combine the layers
of a layered image into one exported image.

Abstract Class AbstractBlend: Defines the required methods to combine the layers of an image.

Class BasicBlend: Represents a method of blending layers of an image together by taking the topmost
pixels and keeping them unless transparent.
____________________
Package mutators:
Package colorTransformations:

Interface ColorTransformation: Defines the necessary methods for a mutator that changes the color
of each pixel in an image by a transformation that does not depend on the pixel's neighbors.

Abstract Class AbstractColorTransformation: Contains code common to all ColorTransformations

Class GreyscaleTransform: Represents a transformation rendering every pixel in an image greyscale.

Class SepiaTransformation: Represents a transformation rendering every pixel in an image under a
sepia filter.

Package filters:

Interface Filter: Defines the necessary methods for a mutator that changes the color
of each pixel in an image by applying a kernel to each pixel, which makes changes that do depend
on that pixel's neighbors.

Abstract Class AbstractFilter: Contains code common to all Filters.

Class BlurFilter: Represents a filter that applies a blur to an image.

Class SharpenFilter: Represents a filter that applies a sharpening effect to an image.

Package matrices:

Interface Matrix: Defines the required methods for a matrix which can be used to store values and
can be multiplied.

Class MatrixImpl: An implementation of a Matrix, which stores it's values in an ArrayList.
_________
Package pixel:

Interface PixelAsColors: Defines the necessary methods for a representation of a pixel as RGB values
that can be retrieved and edited.

Class SimplePixel: Represents an implementation of PixelAsColors, which just stores the RGB values
as three integer fields.
____________________
Package scriptLanguage:

Interface LanguageSyntax: Defines the methods to parse commands and inputs for them from a line fed to it by the controller.

Class LanguageSyntaxImpl: Represents the interpreter for entire lines of user-inputted or designated commands.

Enum Command: Describes how to input information from the inputs and command directions given from LanguageSyntax.


Package parsedcommands:

Interface ParsedCommand: Defines the methods required to execute user-provided commands.

All Other Packages Here: Represent different instances of commands that can be executed to manipulate or process images.
___________________
Package controller:

Interface ImageProcessingController: Defines methods to run scripts and execute user-inputs to manipulate/process images.

Class ProcessingController: Represents an interactive controller that can take inputs and process images in real-time.
___________________
Package view:

Interface ErrorView: Defines a rudimentary sample view that can only write messages to output.

Class TextErrorView: Represents a way of knowing when provided scripts have not functioned correctly using a text-based visual representation.
_______________________


HOW TO RUN:
You may run example scripts through typing java -jar NameOfJARFile.jar after having navigated to the project directory where it is housed (/res).
It requires no arguments.
See the USEME to see what the example scripts do/how they proceed.

HOW TO USE PROGRAM:
To use the functionalities supported by this program, initialize an instance of the ImageProcessingController interface.
This allows one to specify a specific input source, be it an interactive Readable or a script written in a File.
This also allows specification of a target output destination, where error messages and notifications can be demonstrated or posted.
One may enter the desired scripts and the program will carry them out as long as they are supported (supported scripts can be seen in the USEME).

DESCRIPTIONS OF EXAMPLE RUNS:

Run 1 -- FileName: ExampleSimpleImage.txt
1. Creates several simple images
2. Updates the color of some of the images created in (1)
3. Applies all of supported mutators to different images
4. Saves images in the res directory.

Run 2 -- FileName: ExampleLayeredImage.txt
1. Creates a simple image and updates pixel colors for later use
2. Creates layered images through importing or creating new.
3. Adds layers through exporting, creating, or copying, using the image in (1)
4. Updates pixel colors of some of the layers in images from (2)
5. Applies mutators to different layers of layered images
6. Saves individual layers in the res directory.
7. Moves layers around within individual layered images
8. Changes visibilities of different layers in layered images
9. Removes some layers from some layered images
10. Saves layered images as file structures (directories) in the res/ directorys
11. Saves layered images as simple images of different image types


DESIGN/MODEL CHANGES:
Past Model Changes:
- Added FixedSizeGraph interface: All layers of Layered Images should have the same size. FixedSizeGraph is an interface that GraphOfPixels extends that ensures this.
- Nodes gained the added functionality to support transparency. This was done to support layered images and PNG files.
- Added new ways to create graphs, including copies, transparent graphs, and graphs from more filetypes. This was done to support new ways of adding Layered Images layers.
- Added support for pixel opacity variance, stored in Nodes, that allow for color variance in PNG files (and others that support transparancy/opacity).
- ImageToGraphConverter was refactored to have methods that take input types and file names, creating new graphs for the added file types.
Current Additions:
- Added Layered Image support
- Added support for more file types
- Added new read and write image file support

ASSUMPTIONS:
- Left space open for possibly resizing the images.
- A View will be defined in the future.

LIMITATIONS:
- Program works only with required image file types, but this can be expanded.
- Limited to Text-Based View, until expanded upon.

CITATIONS:
- For help with RGB conversion from components into a single integer and vice versa:
https://stackoverflow.com/questions/6001211/format-of-type-int-rgb-and-type-int-argb

- For images of a bird ("birb.jpg"):
Gordon Laing - https://www.flickr.com/photos/cameralabs/13997705508/

- For images of a galaxy("galaxy.png":
https://www.kindpng.com/imgv/booTwR_transparent-milkyway-png-galaxy-transparent-png-download/

- For images of a rainbow ("rainbow.png":
https://clipart.info/

- For images of a vulture ("vulture.png":
https://www.absolutebirdcontrol.com/products-by-bird/turkey-vulture

- For images of and Orange and a San Pellegrino Can:
We took these.
