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
 * Simple demo showing how to use the Quirc Kotlin library.
 */
fun main() {
    println("Quirc Kotlin Demo")
    println("=================")
    println()
    println("Library version: ${Quirc.version()}")
    println()
    
    // Create a decoder instance
    val quirc = Quirc()
    println("✓ Created Quirc decoder instance")
    
    // Resize for a typical image size
    val width = 640
    val height = 480
    val success = quirc.resize(width, height)
    
    if (success) {
        println("✓ Resized decoder to ${width}x${height}")
    } else {
        println("✗ Failed to resize decoder")
        return
    }
    
    // Get the image buffer
    val (buffer, dims) = quirc.begin()
    val (w, h) = dims
    println("✓ Got image buffer: ${w}x${h} (${buffer.size} bytes)")
    
    // In a real application, you would fill the buffer with grayscale image data here
    // For demo purposes, we'll just fill it with a gradient pattern
    for (i in buffer.indices) {
        buffer[i] = (i % 256).toByte()
    }
    println("✓ Filled buffer with demo data")
    
    // Process the image
    quirc.end()
    println("✓ Processed image")
    
    // Check for detected QR codes
    val count = quirc.count()
    println()
    println("Detected $count QR code(s)")
    
    if (count > 0) {
        for (i in 0 until count) {
            println()
            println("QR Code #${i + 1}:")
            
            val code = quirc.extract(i)
            println("  Grid size: ${code.size}x${code.size}")
            println("  Corners: ${code.corners.joinToString()}")
            
            val (error, data) = Quirc.decode(code)
            println("  Decode result: ${error.message}")
            
            if (error == QuircDecodeError.SUCCESS && data != null) {
                println("  Version: ${data.version}")
                println("  ECC Level: ${data.eccLevel}")
                println("  Data type: ${data.dataType}")
                println("  Payload length: ${data.payloadLen}")
                
                if (data.payloadLen > 0) {
                    val payload = String(data.payload, 0, data.payloadLen)
                    println("  Payload: $payload")
                }
            }
        }
    } else {
        println()
        println("Note: This is a demo with synthetic data.")
        println("To decode real QR codes, provide actual QR code images.")
        println()
        println("The full implementation including image processing")
        println("and decoding algorithms is still in progress.")
    }
    
    println()
    println("Demo complete!")
}
