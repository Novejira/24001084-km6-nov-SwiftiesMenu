package com.berkah.swiftiesmenu.repository

import com.berkah.swiftiesmenu.feature.data.datasource.user.UserDataSource
import com.berkah.swiftiesmenu.feature.data.repository.PreferenceRepository
import com.berkah.swiftiesmenu.feature.data.repository.PreferenceRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PreferenceRepositoryImplTest {
    @MockK
    lateinit var ds: UserDataSource

    private lateinit var repo: PreferenceRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = PreferenceRepositoryImpl(ds)
    }

    @Test
    fun isUsingGridMode() {
        every { ds.isUsingGridMode() } returns true
        val actualResult = repo.isUsingGridMode()
        verify { ds.isUsingGridMode() }
        Assert.assertEquals(true, actualResult)
    }

    @Test
    fun setUsingGridMode() {
        every { ds.setUsingGridMode(any()) } returns Unit
        repo.setUsingGridMode(true)
        verify { ds.setUsingGridMode(any()) }
    }
}
