package com.example.bookshelf.data

data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
) {
    val items: List<Book>?
        get() {
            TODO()
        }
}

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String?
)