package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.FirestoreEvent
import com.example.data.model.FirestoreProduct
import com.example.data.model.FirestoreUserProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class AnsorRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun saveUserProfile_authenticatedUser_succeeds(): Unit = runBlocking {
        val uid = signInTestUser("alice_${UUID.randomUUID()}@test.com")
        val repository = AnsorFirestoreRepository(firestore)

        val profile = FirestoreUserProfile(
            userId = uid,
            name = "Sahabat Alice",
            role = "kader",
            village = "Cukir",
            unit = "GP Ansor"
        )

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.saveUserProfile(profile)
        }
        assertTrue(result.isSuccess)
    }

    @Test
    fun getUserProfile_authenticatedOwner_returnsProfile(): Unit = runBlocking {
        val uid = signInTestUser("alice_${UUID.randomUUID()}@test.com")
        val repository = AnsorFirestoreRepository(firestore)

        val profile = FirestoreUserProfile(
            userId = uid,
            name = "Sahabat Alice",
            role = "kader",
            village = "Cukir",
            unit = "GP Ansor"
        )
        withTimeout(DEFAULT_TIMEOUT_MS) { repository.saveUserProfile(profile) }

        val fetchResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.getUserProfile(uid)
        }
        assertTrue(fetchResult.isSuccess)
        assertEquals("Sahabat Alice", fetchResult.getOrNull()?.name)
    }

    @Test
    fun addCadreProduct_authenticatedCadre_succeeds(): Unit = runBlocking {
        val uid = signInTestUser("alice_${UUID.randomUUID()}@test.com")
        val repository = AnsorFirestoreRepository(firestore)

        val prodId = "prod_" + UUID.randomUUID().toString().replace("-", "")
        val product = FirestoreProduct(
            id = prodId,
            userId = uid,
            name = "Kopi Robusta Ansor Diwek",
            businessName = "Kopi Mandiri",
            category = "Kuliner",
            price = "Rp 25.000",
            ownerName = "Sahabat Alice",
            phone = "08123456789",
            status = "approved"
        )

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.addCadreProduct(product)
        }
        assertTrue(result.isSuccess)
        assertEquals(prodId, result.getOrNull())
    }

    @Test
    fun observeApprovedProducts_authenticatedUser_emitsList(): Unit = runBlocking {
        val uid = signInTestUser("alice_${UUID.randomUUID()}@test.com")
        val repository = AnsorFirestoreRepository(firestore)

        val prodId = "prod_" + UUID.randomUUID().toString().replace("-", "")
        val product = FirestoreProduct(
            id = prodId,
            userId = uid,
            name = "Madu Murni Barokah",
            businessName = "Madu Kader",
            category = "Kuliner",
            price = "Rp 60.000",
            ownerName = "Sahabat Alice",
            phone = "08123456789",
            status = "approved"
        )
        withTimeout(DEFAULT_TIMEOUT_MS) { repository.addCadreProduct(product) }

        val emitted = withTimeout(FLOW_TIMEOUT_MS) {
            repository.observeApprovedProducts().first { list -> list.any { it.id == prodId } }
        }
        assertTrue(emitted.any { it.id == prodId })
    }

    @Test
    fun addCadreProduct_unauthenticatedUser_fails(): Unit = runBlocking {
        auth.signOut()
        val repository = AnsorFirestoreRepository(firestore)

        val prodId = "prod_unauth"
        val product = FirestoreProduct(
            id = prodId,
            userId = "unauth",
            name = "Produk Tanpa Login",
            businessName = "Usaha Fiktif",
            category = "Kuliner",
            price = "Rp 10.000",
            ownerName = "Anonim",
            phone = "08123456789",
            status = "approved"
        )

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.addCadreProduct(product)
        }
        assertTrue(result.isFailure)
    }

    @Test
    fun addAnsorEvent_authenticatedUser_createsEvent(): Unit = runBlocking {
        val uid = signInTestUser("alice_${UUID.randomUUID()}@test.com")
        val repository = AnsorFirestoreRepository(firestore)

        val eventId = "event_" + UUID.randomUUID().toString().replace("-", "")
        val event = FirestoreEvent(
            id = eventId,
            title = "Rutin MDS Rijalul Ansor PAC Diwek",
            category = "Majlis & Dzikir",
            dayOfMonth = 12L,
            monthNumber = 6L,
            year = 2026L,
            location = "Masjid Ranting Diwek",
            organizer = "PAC Ansor Diwek",
            creatorId = uid
        )

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.addAnsorEvent(event)
        }
        assertTrue(result.isSuccess)
        assertEquals(eventId, result.getOrNull())
    }

    @Test
    fun addAnsorEvent_unauthenticatedUser_fails(): Unit = runBlocking {
        auth.signOut()
        val repository = AnsorFirestoreRepository(firestore)

        val event = FirestoreEvent(
            id = "event_unauth",
            title = "Acara Tanpa Auth",
            category = "Sosial",
            dayOfMonth = 1L,
            monthNumber = 1L,
            year = 2026L,
            location = "Kantor",
            organizer = "Ansor",
            creatorId = "unauth"
        )

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.addAnsorEvent(event)
        }
        assertTrue(result.isFailure)
    }

    private companion object {
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 4000L
    }
}
