# Quirc - Kotlin Port

This is a Kotlin/JVM port of the quirc QR code recognition library.

## Status

This is an **initial port** of the quirc library from C to Kotlin. The current implementation includes:

### Completed
- ✅ Project structure with Gradle build system
- ✅ Core data structures (QuircPoint, QuircCode, QuircData)
- ✅ Public API design (Quirc class)
- ✅ Constants and enumerations
- ✅ Basic tests for data structures
- ✅ Version database structure (partial - versions 1-5)

### In Progress / TODO
- ⏳ Image identification logic (threshold, region detection, capstone finding)
- ⏳ QR code decoding logic (format reading, error correction, data extraction)
- ⏳ Complete version database (versions 6-40)
- ⏳ Full implementation of all algorithms from C version
- ⏳ Performance optimization
- ⏳ Comprehensive test suite with sample QR codes
- ⏳ Documentation and usage examples

## Project Structure

```
src/
├── main/kotlin/com/github/maksonic/quirc/
│   ├── Quirc.kt              # Main API class
│   ├── QuircConstants.kt     # Constants and limits
│   ├── QuircStructures.kt    # Data structures
│   ├── QuircDecode.kt        # Decoding logic (stub)
│   ├── QuircIdentify.kt      # Identification logic (stub)
│   └── QuircVersionDb.kt     # QR version database (partial)
└── test/kotlin/com/github/maksonic/quirc/
    └── QuircTest.kt          # Basic tests
```

## Building

```bash
./gradlew build
```

## Running Tests

```bash
./gradlew test
```

## Usage Example

```kotlin
import com.github.maksonic.quirc.*

// Create a decoder
val quirc = Quirc()

// Resize for your image dimensions
quirc.resize(640, 480)

// Get the image buffer
val (buffer, dims) = quirc.begin()

// Fill buffer with grayscale image data (one byte per pixel)
// ... (load your image here)

// Process the image
quirc.end()

// Get number of detected QR codes
val count = quirc.count()

// Extract and decode each code
for (i in 0 until count) {
    val code = quirc.extract(i)
    val (error, data) = Quirc.decode(code)
    
    if (error == QuircDecodeError.SUCCESS && data != null) {
        println("Decoded: ${String(data.payload, 0, data.payloadLen)}")
    } else {
        println("Decode failed: ${error.message}")
    }
}
```

## API Comparison with C Version

| C API | Kotlin API |
|-------|------------|
| `quirc_new()` | `Quirc()` constructor |
| `quirc_destroy()` | Automatic (GC) |
| `quirc_resize()` | `resize()` method |
| `quirc_begin()` | `begin()` method |
| `quirc_end()` | `end()` method |
| `quirc_count()` | `count()` method |
| `quirc_extract()` | `extract()` method |
| `quirc_decode()` | `Quirc.decode()` static |
| `quirc_flip()` | `Quirc.flip()` static |
| `quirc_version()` | `Quirc.version()` static |
| `quirc_strerror()` | `Quirc.strerror()` static |

## Differences from C Version

1. **Memory Management**: Kotlin uses automatic garbage collection instead of manual memory management
2. **Error Handling**: Uses enum class for error types with built-in messages
3. **API Style**: Uses Kotlin idiomatic patterns (data classes, companion objects)
4. **Type Safety**: Leverages Kotlin's type system for safer code
5. **Null Safety**: Uses Kotlin's null safety features

## Requirements

- JDK 11 or higher
- Gradle 8.5 or higher

## License

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

## Original C Library

This is a port of the original quirc library by Daniel Beer.
See the main README.md for information about the original C implementation.

## Contributing

This is an initial port with stub implementations for many core algorithms.
Contributions to complete the implementation are welcome!

Priority areas:
1. Complete the identification logic (image processing, region detection)
2. Implement the decoding logic (Reed-Solomon error correction, data extraction)
3. Port the full version database (versions 6-40)
4. Add comprehensive tests with real QR code images
5. Optimize performance to match or exceed the C version
