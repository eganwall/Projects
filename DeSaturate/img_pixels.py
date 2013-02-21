from __future__ import division

import Image
import colorsys

def ConvertToRG(RGBTuple):
	# print("Converting (%d, %d, %d)..." % RGBTuple)

	r, g, b = RGBTuple # extract the values

	if((r + g + b) == 0):
		# avoid our divide by zero by just returning a black pixel
		newRGBTuple = (0, 0, 0)

	else:
		# calculate the RG-chromaticity of the pixel
		newR = r / (r + g + b)
		newG = g / (r + g + b)
		newB = 1 - (newR + newG)

		newRGBTuple = (int(newR * 255), int(newG * 255), int(newB * 255))

	return newRGBTuple

def AverageRGB(RGBTuple):
	# print("\n=========================  AVERAGING  =========================\n")

	r, g, b = RGBTuple

	newColor = int((r + g + b) / 3)
	newRGBTuple = (newColor, newColor, newColor)

	return newRGBTuple


filename = "TESTING_IMAGE_2"
#filename = raw_input()

img = Image.open(filename + ".jpg")
img.convert("RGB") # make sure we're in RGB mode
width, height = img.size

# the image where our unchanged pixels will go
newImg = Image.new("RGB", (width, height))

# the mask for our grayscale pixels
newMask = Image.new("RGB", (width, height))

# our file containing RG-chromaticity information
newRG = Image.new("RGB", (width, height))

for y in xrange(height):
	for x in xrange(width):
		
		coords = (x, y)

		# get the RGB values from the pixel
		RGBTuple = img.getpixel(coords)

		# perform our conversion
		RGChrom = ConvertToRG(RGBTuple)

		# extract the individual values from the original tuple
		r, g, b = RGChrom

		HLSTuple = colorsys.rgb_to_hls(r / 255., g / 255., b / 255.)
		hue = int(HLSTuple[0] * 360) # clean up our hue value


		'''
			HUE INFORMATION:
				Hue is a circular value from 0-360 (degrees in a circle).
				Hue ranges determine base color of the pixel:
					20 = RED
					30 = ORANGE
					60 = YELLOW
					120 = GREEN
					240 = BLUE
					300 = PINK
		'''


		if(hue >= 330 and hue <= 360):
			# print("Placing pixel at (%d, %d) in image mask." % coords)
			newImg.putpixel(coords, RGBTuple)
			newMask.putpixel(coords, RGBTuple)

		else:
			# print("Placing pixel at (%d, %d) in output image." % coords)
			newRGBTuple = AverageRGB(RGBTuple)

			# print newRGBTuple
			newImg.putpixel(coords, newRGBTuple)
			newMask.putpixel(coords, (255, 255, 255))

		newRG.putpixel(coords, RGChrom)

newImg.save(filename + "_OUT.jpg", "JPEG")
newMask.save(filename + "_MASK.jpg", "JPEG")
newRG.save(filename + "_RG.jpg", "JPEG")