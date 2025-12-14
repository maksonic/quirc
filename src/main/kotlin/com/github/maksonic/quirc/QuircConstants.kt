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

/** Library version */
const val QUIRC_VERSION = "2.0.0-kotlin"

/** Limits on the maximum size of QR-codes and their content */
const val QUIRC_MAX_VERSION = 40
const val QUIRC_MAX_GRID_SIZE = QUIRC_MAX_VERSION * 4 + 17
const val QUIRC_MAX_BITMAP = ((QUIRC_MAX_GRID_SIZE * QUIRC_MAX_GRID_SIZE) + 7) / 8
const val QUIRC_MAX_PAYLOAD = 8896

/** QR-code ECC types */
const val QUIRC_ECC_LEVEL_M = 0
const val QUIRC_ECC_LEVEL_L = 1
const val QUIRC_ECC_LEVEL_H = 2
const val QUIRC_ECC_LEVEL_Q = 3

/** QR-code data types */
const val QUIRC_DATA_TYPE_NUMERIC = 1
const val QUIRC_DATA_TYPE_ALPHA = 2
const val QUIRC_DATA_TYPE_BYTE = 4
const val QUIRC_DATA_TYPE_KANJI = 8

/** Common character encodings */
const val QUIRC_ECI_ISO_8859_1 = 1
const val QUIRC_ECI_IBM437 = 2
const val QUIRC_ECI_ISO_8859_2 = 4
const val QUIRC_ECI_ISO_8859_3 = 5
const val QUIRC_ECI_ISO_8859_4 = 6
const val QUIRC_ECI_ISO_8859_5 = 7
const val QUIRC_ECI_ISO_8859_6 = 8
const val QUIRC_ECI_ISO_8859_7 = 9
const val QUIRC_ECI_ISO_8859_8 = 10
const val QUIRC_ECI_ISO_8859_9 = 11
const val QUIRC_ECI_WINDOWS_874 = 13
const val QUIRC_ECI_ISO_8859_13 = 15
const val QUIRC_ECI_ISO_8859_15 = 17
const val QUIRC_ECI_SHIFT_JIS = 20
const val QUIRC_ECI_UTF_8 = 26

/** Internal constants */
internal const val QUIRC_PIXEL_WHITE = 0
internal const val QUIRC_PIXEL_BLACK = 1
internal const val QUIRC_PIXEL_REGION = 2

internal const val QUIRC_MAX_REGIONS = 254
internal const val QUIRC_MAX_CAPSTONES = 32
internal const val QUIRC_MAX_GRIDS = QUIRC_MAX_CAPSTONES * 2
internal const val QUIRC_PERSPECTIVE_PARAMS = 8
internal const val QUIRC_MAX_ALIGNMENT = 7
