package com.example.adguardtv

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

class DnsManagerTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockPrefs: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs)
        `when`(mockPrefs.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putBoolean(anyString(), any())).thenReturn(mockEditor)
        `when`(mockEditor.putString(anyString(), any())).thenReturn(mockEditor)
    }

    @Test
    fun testSetConnected() {
        DnsManager.setConnected(mockContext, true)
        verify(mockEditor).putBoolean("is_connected", true)
        verify(mockEditor).apply()
    }

    @Test
    fun testIsConnected() {
        `when`(mockPrefs.getBoolean("is_connected", false)).thenReturn(true)
        val result = DnsManager.isConnected(mockContext)
        assertEquals(true, result)
    }

    @Test
    fun testCustomDns() {
        DnsManager.setCustomDns(mockContext, "1.1.1.1")
        verify(mockEditor).putString("custom_dns", "1.1.1.1")
        verify(mockEditor).apply()

        `when`(mockPrefs.getString("custom_dns", null)).thenReturn("1.1.1.1")
        val result = DnsManager.getCustomDns(mockContext)
        assertEquals("1.1.1.1", result)
    }
}