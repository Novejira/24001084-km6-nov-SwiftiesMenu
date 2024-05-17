package com.berkah.swiftiesmenu.repository

import app.cash.turbine.test
import com.berkah.swiftiesmenu.feature.data.datasource.cart.CartDataSource
import com.berkah.swiftiesmenu.feature.data.model.Cart
import com.berkah.swiftiesmenu.feature.data.model.Menu
import com.berkah.swiftiesmenu.feature.data.model.PriceItem
import com.berkah.swiftiesmenu.feature.data.repository.CartRepository
import com.berkah.swiftiesmenu.feature.data.repository.CartRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.source.entity.CartEntity
import com.berkah.swiftiesmenu.feature.data.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {
    @MockK
    lateinit var ds: CartDataSource

    private lateinit var repo: CartRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = CartRepositoryImpl(ds)
    }

    @Test
    fun getUserCartData_success() {
        val entity1 =
            CartEntity(
                id = 1,
                menuId = "afwwfawf",
                menuName = "awfawf",
                menuImgUrl = "awfawfafawf",
                menuPrice = 8000.0,
                itemQuantity = 3,
                itemNotes = "awfwafawfawfafwafa",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuId = "afwwfawf",
                menuName = "awfawf",
                menuImgUrl = "awfawfafawf",
                menuPrice = 10000.0,
                itemQuantity = 3,
                itemNotes = "awfwafawfawfafwafa",
            )
        val mockList = listOf(entity1, entity2)
        val mockFlow = flow { emit(mockList) }
        every { ds.getAllCarts() } returns mockFlow
        runTest {
            repo.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(mockList.size, actualData.payload?.first?.size)
                assertEquals(54000.0, actualData.payload?.second)
                verify { ds.getAllCarts() }
            }
        }
    }

    @Test
    fun getUserCartData_loading() {
        val entity1 =
            CartEntity(
                id = 1,
                menuId = "afwwfawf",
                menuName = "awfawf",
                menuImgUrl = "awfawfafawf",
                menuPrice = 8000.0,
                itemQuantity = 3,
                itemNotes = "awfwafawfawfafwafa",
            )
        val entity2 =
            CartEntity(
                id = 1,
                menuId = "afwwfawf",
                menuName = "awfawf",
                menuImgUrl = "awfawfafawf",
                menuPrice = 8000.0,
                itemQuantity = 3,
                itemNotes = "awfwafawfawfafwafa",
            )
        val mockList = listOf(entity1, entity2)
        val mockFlow = flow { emit(mockList) }
        every { ds.getAllCarts() } returns mockFlow
        runTest {
            repo.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(1120)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                verify { ds.getAllCarts() }
            }
        }
    }

    @Test
    fun getUserCartData_error() {
        every { ds.getAllCarts() } returns
            flow {
                throw IllegalStateException("errr")
            }
        runTest {
            repo.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2211)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                verify { ds.getAllCarts() }
            }
        }
    }

    @Test
    fun getUserCartData_empty() {
        val mockList = listOf<CartEntity>()
        val mockFlow = flow { emit(mockList) }
        every { ds.getAllCarts() } returns mockFlow
        runTest {
            repo.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Empty)
                assertEquals(true, actualData.payload?.first?.isEmpty())
                verify { ds.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_success() {
        val entity1 =
            CartEntity(
                id = 1,
                menuId = "121",
                menuName = "adoisn",
                menuImgUrl = "sdnais",
                menuPrice = 127.0,
                itemQuantity = 1,
                itemNotes = "jsdj",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuId = "21",
                menuName = "adisn",
                menuImgUrl = "sdais",
                menuPrice = 123.0,
                itemQuantity = 2,
                itemNotes = "jdj",
            )
        val mockkPriceList = listOf(PriceItem("adoisn", 127.0), PriceItem("adisn", 246.0))
        val mockList = listOf(entity1, entity2)
        val mockFlow = flow { emit(mockList) }
        every { ds.getAllCarts() } returns mockFlow
        runTest {
            repo.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(mockList.size, actualData.payload?.first?.size)
                assertEquals(mockkPriceList, actualData.payload?.second)
                assertEquals(373.0, actualData.payload?.third)
                verify { ds.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_error() {
        every { ds.getAllCarts() } returns
            flow {
                throw IllegalStateException("errr")
            }
        runTest {
            repo.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2211)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                verify { ds.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_loading() {
        val mockList = listOf<CartEntity>()
        val mockFlow = flow { emit(mockList) }
        every { ds.getAllCarts() } returns mockFlow
        runTest {
            repo.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(1111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                verify { ds.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_empty() {
        val mockList = listOf<CartEntity>()
        val mockFlow = flow { emit(mockList) }
        every { ds.getAllCarts() } returns mockFlow
        runTest {
            repo.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Empty)
                assertEquals(true, actualData.payload?.first?.isEmpty())
                verify { ds.getAllCarts() }
            }
        }
    }

    @Test
    fun createCart_success() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns "123"
        coEvery { ds.insertCart(any()) } returns 1
        runTest {
            repo.createCart(mockMenu, 3, "afawfawf")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2221)
                    val actualData = expectMostRecentItem()
                    assertTrue(actualData is ResultWrapper.Success)
                    assertEquals(true, actualData.payload)
                    coVerify { ds.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_success_note_null() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns "123"
        coEvery { ds.insertCart(any()) } returns 1
        runTest {
            repo.createCart(mockMenu, 3)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2221)
                    val actualData = expectMostRecentItem()
                    assertTrue(actualData is ResultWrapper.Success)
                    assertEquals(true, actualData.payload)
                    coVerify { ds.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_loading() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns "123"
        coEvery { ds.insertCart(any()) } returns 1
        runTest {
            repo.createCart(mockMenu, 3, "lol").map {
                delay(100)
                it
            }.test {
                delay(110)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { ds.insertCart(any()) }
            }
        }
    }

    @Test
    fun createCart_error_processing() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns "123"
        coEvery { ds.insertCart(any()) } throws IllegalStateException("error")
        runTest {
            repo.createCart(mockMenu, 3, "lol").map {
                delay(100)
                it
            }.test {
                delay(202)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { ds.insertCart(any()) }
            }
        }
    }

    @Test
    fun createCart_error_no_product_id() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns null
        coEvery { ds.insertCart(any()) } returns 1
        runTest {
            repo.createCart(mockMenu, 3, "lol").map {
                delay(100)
                it
            }.test {
                delay(2020)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify(atLeast = 0) { ds.insertCart(any()) }
            }
        }
    }

    @Test
    fun decreaseCart_when_quantity_more_than_0() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "afwwfawf",
                menuName = "awfawf",
                menuImgUrl = "awfawfafawf",
                menuPrice = 10000.0,
                itemQuantity = 3,
                itemNotes = "awfwafawfawfafwafa",
            )
        coEvery { ds.deleteCart(any()) } returns 1
        coEvery { ds.updateCart(any()) } returns 1
        runTest {
            repo.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 0) { ds.deleteCart(any()) }
                coVerify(atLeast = 1) { ds.updateCart(any()) }
            }
        }
    }

    @Test
    fun decreaseCart_when_quantity_less_than_1() {
        val mockCart =
            Cart(
                id = 1,
                menuId = "121",
                menuName = "adoisn",
                menuImgUrl = "awfawfafawf",
                menuPrice = 127.0,
                itemQuantity = 1,
                itemNotes = "jsjd",
            )
        coEvery { ds.deleteCart(any()) } returns 1
        coEvery { ds.updateCart(any()) } returns 1
        runTest {
            repo.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify { ds.deleteCart(any()) }
            }
        }
    }

    @Test
    fun increaseCart() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "afwwfawf",
                menuName = "awfawf",
                menuImgUrl = "awfawfafawf",
                menuPrice = 10000.0,
                itemQuantity = 3,
                itemNotes = "awfwafawfawfafwafa",
            )
        coEvery { ds.updateCart(any()) } returns 1
        runTest {
            repo.increaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify { ds.updateCart(any()) }
            }
        }
    }

    @Test
    fun setCartNotes() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "afwwfawf",
                menuName = "awfawf",
                menuImgUrl = "awfawfafawf",
                menuPrice = 10000.0,
                itemQuantity = 3,
                itemNotes = "awfwafawfawfafwafa",
            )
        coEvery { ds.updateCart(any()) } returns 1
        runTest {
            repo.setCartNotes(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 1) { ds.updateCart(any()) }
            }
        }
    }

    @Test
    fun deleteCart() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "afwwfawf",
                menuName = "awfawf",
                menuImgUrl = "awfawfafawf",
                menuPrice = 10000.0,
                itemQuantity = 3,
                itemNotes = "awfwafawfawfafwafa",
            )
        coEvery { ds.deleteCart(any()) } returns 1
        runTest {
            repo.deleteCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify { ds.deleteCart(any()) }
            }
        }
    }

    @Test
    fun deleteAllCart() {
        coEvery { ds.deleteAll() } returns Unit
        runTest {
            val result =
                repo.deleteAll().map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val actualData = expectMostRecentItem()
                    assertTrue(actualData is ResultWrapper.Success)
                }
            coVerify { ds.deleteAll() }
            assertEquals(Unit, result)
        }
    }
}
