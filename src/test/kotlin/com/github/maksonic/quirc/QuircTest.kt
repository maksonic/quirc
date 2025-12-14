package com.github.maksonic.quirc

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class QuircTest {
    
    @Test
    fun `test version string`() {
        assertEquals("2.0.0-kotlin", Quirc.version())
    }
    
    @Test
    fun `test error messages`() {
        assertEquals("Success", Quirc.strerror(QuircDecodeError.SUCCESS))
        assertEquals("Invalid grid size", Quirc.strerror(QuircDecodeError.INVALID_GRID_SIZE))
        assertEquals("ECC failure", Quirc.strerror(QuircDecodeError.DATA_ECC))
    }
    
    @Test
    fun `test quirc creation and resize`() {
        val quirc = Quirc()
        assertTrue(quirc.resize(640, 480))
        
        val (buffer, dims) = quirc.begin()
        val (width, height) = dims
        
        assertEquals(640, width)
        assertEquals(480, height)
        assertEquals(640 * 480, buffer.size)
    }
    
    @Test
    fun `test quirc count initially zero`() {
        val quirc = Quirc()
        quirc.resize(320, 240)
        quirc.end()
        
        assertEquals(0, quirc.count())
    }
    
    @Test
    fun `test quirc point`() {
        val point = QuircPoint(10, 20)
        assertEquals(10, point.x)
        assertEquals(20, point.y)
    }
    
    @Test
    fun `test quirc code structure`() {
        val code = QuircCode()
        assertEquals(0, code.size)
        assertEquals(QUIRC_MAX_BITMAP, code.cellBitmap.size)
        assertEquals(4, code.corners.size)
    }
    
    @Test
    fun `test quirc data structure`() {
        val data = QuircData()
        assertEquals(0, data.version)
        assertEquals(0, data.eccLevel)
        assertEquals(QUIRC_MAX_PAYLOAD, data.payload.size)
    }
    
    @Test
    fun `test constants`() {
        assertEquals(40, QUIRC_MAX_VERSION)
        assertEquals(177, QUIRC_MAX_GRID_SIZE)
        assertEquals(8896, QUIRC_MAX_PAYLOAD)
    }
}
