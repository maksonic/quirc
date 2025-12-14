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
 * This structure describes a location in the input image buffer.
 */
data class QuircPoint(
    val x: Int,
    val y: Int
)

/**
 * Decoder error types.
 */
enum class QuircDecodeError(val message: String) {
    SUCCESS("Success"),
    INVALID_GRID_SIZE("Invalid grid size"),
    INVALID_VERSION("Invalid version"),
    FORMAT_ECC("Format data ECC failure"),
    DATA_ECC("ECC failure"),
    UNKNOWN_DATA_TYPE("Unknown data type"),
    DATA_OVERFLOW("Data overflow"),
    DATA_UNDERFLOW("Data underflow")
}

/**
 * This structure is used to return information about detected QR codes
 * in the input image.
 */
data class QuircCode(
    /** The four corners of the QR-code, from top left, clockwise */
    val corners: Array<QuircPoint> = Array(4) { QuircPoint(0, 0) },
    
    /** The number of cells across in the QR-code */
    var size: Int = 0,
    
    /** The cell bitmap is a bitmask giving the actual values of cells */
    val cellBitmap: ByteArray = ByteArray(QUIRC_MAX_BITMAP)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuircCode

        if (!corners.contentEquals(other.corners)) return false
        if (size != other.size) return false
        if (!cellBitmap.contentEquals(other.cellBitmap)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = corners.contentHashCode()
        result = 31 * result + size
        result = 31 * result + cellBitmap.contentHashCode()
        return result
    }
}

/**
 * This structure holds the decoded QR-code data.
 */
data class QuircData(
    /** QR-code version */
    var version: Int = 0,
    
    /** ECC level */
    var eccLevel: Int = 0,
    
    /** Mask pattern */
    var mask: Int = 0,
    
    /** This field is the highest-valued data type found in the QR code */
    var dataType: Int = 0,
    
    /** Data payload. For the Kanji datatype, payload is encoded as Shift-JIS.
     * For all other datatypes, payload is ASCII text. */
    val payload: ByteArray = ByteArray(QUIRC_MAX_PAYLOAD),
    
    /** Payload length */
    var payloadLen: Int = 0,
    
    /** ECI assignment number (stored as Int for Java interoperability, always non-negative) */
    var eci: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuircData

        if (version != other.version) return false
        if (eccLevel != other.eccLevel) return false
        if (mask != other.mask) return false
        if (dataType != other.dataType) return false
        if (!payload.contentEquals(other.payload)) return false
        if (payloadLen != other.payloadLen) return false
        if (eci != other.eci) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version
        result = 31 * result + eccLevel
        result = 31 * result + mask
        result = 31 * result + dataType
        result = 31 * result + payload.contentHashCode()
        result = 31 * result + payloadLen
        result = 31 * result + eci.hashCode()
        return result
    }
}

/** Internal data structures */

internal data class QuircRegion(
    var seed: QuircPoint = QuircPoint(0, 0),
    var count: Int = 0,
    var capstone: Int = 0
)

internal data class QuircCapstone(
    var ring: Int = 0,
    var stone: Int = 0,
    val corners: Array<QuircPoint> = Array(4) { QuircPoint(0, 0) },
    var center: QuircPoint = QuircPoint(0, 0),
    val c: DoubleArray = DoubleArray(QUIRC_PERSPECTIVE_PARAMS),
    var qrGrid: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuircCapstone

        if (ring != other.ring) return false
        if (stone != other.stone) return false
        if (!corners.contentEquals(other.corners)) return false
        if (center != other.center) return false
        if (!c.contentEquals(other.c)) return false
        if (qrGrid != other.qrGrid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ring
        result = 31 * result + stone
        result = 31 * result + corners.contentHashCode()
        result = 31 * result + center.hashCode()
        result = 31 * result + c.contentHashCode()
        result = 31 * result + qrGrid
        return result
    }
}

internal data class QuircGrid(
    /** Capstone indices */
    val caps: IntArray = IntArray(3),
    
    /** Alignment pattern region and corner */
    var alignRegion: Int = 0,
    var align: QuircPoint = QuircPoint(0, 0),
    
    /** Timing pattern endpoints */
    val tpep: Array<QuircPoint> = Array(3) { QuircPoint(0, 0) },
    
    /** Grid size and perspective transform */
    var gridSize: Int = 0,
    val c: DoubleArray = DoubleArray(QUIRC_PERSPECTIVE_PARAMS)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuircGrid

        if (!caps.contentEquals(other.caps)) return false
        if (alignRegion != other.alignRegion) return false
        if (align != other.align) return false
        if (!tpep.contentEquals(other.tpep)) return false
        if (gridSize != other.gridSize) return false
        if (!c.contentEquals(other.c)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = caps.contentHashCode()
        result = 31 * result + alignRegion
        result = 31 * result + align.hashCode()
        result = 31 * result + tpep.contentHashCode()
        result = 31 * result + gridSize
        result = 31 * result + c.contentHashCode()
        return result
    }
}

internal data class QuircFloodFillVars(
    var y: Int = 0,
    var right: Int = 0,
    var leftUp: Int = 0,
    var leftDown: Int = 0
)

internal data class QuircRsParams(
    var bs: Int = 0,  // Small block size
    var dw: Int = 0,  // Small data words
    var ns: Int = 0   // Number of small blocks
)

internal data class QuircVersionInfo(
    val dataBytes: Int,
    val apat: IntArray,
    val ecc: Array<QuircRsParams>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuircVersionInfo

        if (dataBytes != other.dataBytes) return false
        if (!apat.contentEquals(other.apat)) return false
        if (!ecc.contentEquals(other.ecc)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dataBytes
        result = 31 * result + apat.contentHashCode()
        result = 31 * result + ecc.contentHashCode()
        return result
    }
}
