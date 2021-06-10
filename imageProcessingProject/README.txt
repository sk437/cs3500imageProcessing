OVERVIEW OF CLASSES/INTERFACES:

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

Package imageInput:
Package imageProgram:

Interface ImageProgram: Defines the required methods for a program that can be used to create an
image by following a particular algorithm.

Class CheckerBoard: Represents an image program which is used to generate a checkerboard image.
_________
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
_________
IMAGE CITATIONS:
Both images taken by us an authorized for use.
