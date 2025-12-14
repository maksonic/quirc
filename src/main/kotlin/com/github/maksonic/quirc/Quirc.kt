/* quirc -- QR-code recognition library
 * Copyright (C) 2010-2012 Daniel Beer <dlbeer@gmail.com>
 * Kotlin port Copyright (C) 2025
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.maksonic.quirc

/**
 * Main QR-code recognizer class.
 * 
 * This class is used to decode QR codes from grayscale images.
 * The typical usage pattern is:
 * 1. Create a new Quirc instance
 * 2. Resize it to match your image dimensions
 * 3. Use begin() to get the image buffer
 * 4. Fill the buffer with grayscale image data
 * 5. Call end() to process the image
 * 6. Use count() and extract() to get detected QR codes
 * 7. Use decode() to decode each QR code
 */
class Quirc {
    internal var image: ByteArray = ByteArray(0)
    internal var pixels: ShortArray = ShortArray(0)
    internal var w: Int = 0
    internal var h: Int = 0
    
    internal var numRegions: Int = 0
    internal val regions: Array<QuircRegion> = Array(QUIRC_MAX_REGIONS) { QuircRegion() }
    
    internal var numCapstones: Int = 0
    internal val capstones: Array<QuircCapstone> = Array(QUIRC_MAX_CAPSTONES) { QuircCapstone() }
    
    internal var numGrids: Int = 0
    internal val grids: Array<QuircGrid> = Array(QUIRC_MAX_GRIDS) { QuircGrid() }
    
    internal var floodFillVars: Array<QuircFloodFillVars> = emptyArray()
    
    /**
     * Resize the QR-code recognizer. The size of an image must be
     * specified before codes can be analyzed.
     *
     * @param w Width of the image
     * @param h Height of the image
     * @return true on success, false if memory allocation failed
     */
    fun resize(w: Int, h: Int): Boolean {
        if (w < 0 || h < 0) {
            return false
        }
        
        try {
            val newImage = ByteArray(w * h)
            val newPixels = ShortArray(w * h)
            
            // Copy old data if exists
            val oldDim = this.w * this.h
            val newDim = w * h
            val min = minOf(oldDim, newDim)
            
            if (min > 0 && image.isNotEmpty()) {
                System.arraycopy(image, 0, newImage, 0, min)
            }
            
            // Allocate flood fill work area
            // Size chosen based on maximum ring height (about 1/3 of image height)
            val numVars = maxOf(1, (h * 2) / 3)
            val newFloodFillVars = Array(numVars) { QuircFloodFillVars() }
            
            // Update instance variables
            this.w = w
            this.h = h
            this.image = newImage
            this.pixels = newPixels
            this.floodFillVars = newFloodFillVars
            
            return true
        } catch (e: OutOfMemoryError) {
            return false
        }
    }
    
    /**
     * Get access to the image buffer for filling with grayscale image data.
     * 
     * @return Pair of (buffer, Pair(width, height))
     */
    fun begin(): Pair<ByteArray, Pair<Int, Int>> {
        return Pair(image, Pair(w, h))
    }
    
    /**
     * Process the image for QR-code recognition after filling the buffer
     * obtained from begin().
     */
    fun end() {
        QuircIdentify.identify(this)
    }
    
    /**
     * Return the number of QR-codes identified in the last processed image.
     */
    fun count(): Int = numGrids
    
    /**
     * Extract the QR-code specified by the given index.
     * 
     * @param index Index of the QR code (0 to count()-1)
     * @return The extracted QR code structure
     */
    fun extract(index: Int): QuircCode {
        require(index >= 0 && index < numGrids) { "Index out of bounds" }
        return QuircIdentify.extractCode(this, index)
    }
    
    companion object {
        /**
         * Get the library version string.
         */
        fun version(): String = QUIRC_VERSION
        
        /**
         * Return a string error message for an error code.
         */
        fun strerror(err: QuircDecodeError): String = err.message
        
        /**
         * Decode a QR-code, returning the payload data.
         * 
         * @param code The QR code to decode
         * @return Pair of (error code, decoded data if successful)
         */
        fun decode(code: QuircCode): Pair<QuircDecodeError, QuircData?> {
            return QuircDecode.decode(code)
        }
        
        /**
         * Flip a QR-code according to optional mirror feature of ISO 18004:2015.
         * This can be used for a second decode attempt when the first fails with DATA_ECC error.
         * 
         * @param code The QR code to flip
         */
        fun flip(code: QuircCode) {
            QuircDecode.flip(code)
        }
    }
}
