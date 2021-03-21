/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.uamp.media.library

import android.content.ContentResolver
import android.content.res.Resources
import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.android.uamp.media.R
import com.example.android.uamp.media.extensions.albumArtUri
import com.example.android.uamp.media.extensions.artUri
import com.example.android.uamp.media.extensions.displayDescription
import com.example.android.uamp.media.extensions.displayIconUri
import com.example.android.uamp.media.extensions.displaySubtitle
import com.example.android.uamp.media.extensions.displayTitle
import com.example.android.uamp.media.extensions.downloadStatus
import com.example.android.uamp.media.extensions.flag
import com.example.android.uamp.media.extensions.id
import com.example.android.uamp.media.extensions.mediaUri
import com.example.android.uamp.media.extensions.title
import java.util.concurrent.atomic.AtomicReference

/**
 * Source of [MediaMetadataCompat] objects created from a basic JSON stream.
 *
 * The definition of the JSON is specified in the docs of [JsonMusic] in this file,
 * which is the object representation of it.
 */
class StaticSource(resources: Resources) : AbstractMusicSource() {
    private var _resources = AtomicReference<Resources?>(resources)

    private var catalog: List<MediaMetadataCompat> = emptyList()

    override fun iterator(): Iterator<MediaMetadataCompat> = catalog.iterator()

    override suspend fun load() {
        if (state == STATE_INITIALIZING) return
        if (state == STATE_INITIALIZED) return
        val resources = _resources.getAndSet(null) ?: return

        state = STATE_INITIALIZING
        catalog = listOf(
            static(
                id = "KPPV",
                title = "KPPV 106.7 FM",
                mediaUri = "http://stream.affordablestreaming.com:8000/KPPV.mp3",
                artUri = resources.uri(R.drawable.kppv)
            ),
            static(
                id = "KQNA",
                title = "KQNA 1130AM",
                mediaUri = "http://stream.affordablestreaming.com:8000/KQNA.mp3",
                artUri = resources.uri(R.drawable.kqna)
            ),
            static(
                id = "KDDL",
                title = "KDDL",
                mediaUri = "http://stream.affordablestreaming.com:8000/KDDL.mp3",
                artUri = resources.uri(R.drawable.kddl)
            ),
            static(
                id = "KPKR",
                title = "Jack fm 95.7 95.5",
                mediaUri = "http://stream.affordablestreaming.com:8000/KPKR.mp3",
                artUri = resources.uri(R.drawable.kpkr)
            ),
            static(
                id = "KDMM",
                title = "KDMM",
                mediaUri = "http://stream.affordablestreaming.com:8000/KDMM.mp3",
                artUri = resources.uri(R.drawable.kdmm)
            ),
            static(
                id = "JACKFM",
                title = "JACKFM 94.7",
                mediaUri = "http://stream.affordablestreaming.com:8000/JACKFM.mp3",
                artUri = resources.uri(R.drawable.jackfm)
            ),
            static(
                id = "JUAN",
                title = "Juan 107.1 FM",
                mediaUri = "http://stream.affordablestreaming.com:8000/JUANFM.mp3",
                artUri = resources.uri(R.drawable.juan)
            ),
            static(
                id = "KUGO",
                title = "Grand Canyon Info",
                mediaUri = "http://stream.affordablestreaming.com:8000/KUGO-1.mp3",
                artUri = resources.uri(R.drawable.kugo)
            ),
            static(
                id = "KXBB",
                title = "KXBB Badass blues",
                mediaUri = "http://stream.affordablestreaming.com:8000/KXBB.mp3",
                artUri = resources.uri(R.drawable.kxbb)
            )
        )
        state = STATE_INITIALIZED
    }

    private fun static(
        id: String,
        title: String,
        mediaUri: String,
        artUri: String
    ) = MediaMetadataCompat.Builder()
        .apply {
            this.id = id
            this.title = title
            this.mediaUri = mediaUri
            this.albumArtUri = artUri

            this.displayIconUri = artUri
            this.displayTitle = title
            this.displaySubtitle = ""
            this.displayDescription = ""

            this.flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

            this.downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED
        }
        .build()

    private fun Resources.uri(resourceId: Int) = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(getResourcePackageName(resourceId))
        .appendPath(getResourceTypeName(resourceId))
        .appendPath(getResourceEntryName(resourceId))
        .build()
        .toString()
}
