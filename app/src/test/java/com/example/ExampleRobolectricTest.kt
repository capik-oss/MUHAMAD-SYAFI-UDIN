package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.EventCategory
import com.example.data.repository.PdPrtRepository
import com.example.data.repository.SongRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Sahabat Ansor Diwek", appName)
  }

  @Test
  fun `verify pdprt repository contains articles`() {
    assertTrue(PdPrtRepository.items.isNotEmpty())
    val mukaddimah = PdPrtRepository.items.find { it.id == "pd_mukaddimah" }
    assertTrue(mukaddimah != null)
  }

  @Test
  fun `verify song repository contains mars ansor and mars banser`() {
    val songs = SongRepository.songs
    assertTrue(songs.any { it.id == "mars_ansor" })
    val banserSong = songs.find { it.id == "mars_banser" }
    assertTrue(banserSong != null && banserSong.verses.isNotEmpty())
  }

  @Test
  fun `verify event calendar categories exist`() {
    val categories = EventCategory.values()
    assertTrue(categories.contains(EventCategory.MAJLIS))
    assertTrue(categories.contains(EventCategory.RAPAT))
    assertTrue(categories.contains(EventCategory.BANSER))
    assertTrue(categories.contains(EventCategory.SOSIAL))
  }

  @Test
  fun `verify user roles exist for kader and admin`() {
    val roles = com.example.data.model.UserRole.values()
    assertTrue(roles.contains(com.example.data.model.UserRole.KADER))
    assertTrue(roles.contains(com.example.data.model.UserRole.ADMIN))
  }
}
