package com.descord.graphqlserver.testDomain

import java.util.*
import java.util.function.Predicate

class Author(
    val id: String,
    val firstName: String,
    val lastName: String
) {
    companion object {
        private val authors = listOf(
            Author("author-1", "Joanne", "Rowling"),
            Author("author-2", "Herman", "Melville"),
            Author("author-3", "Anne", "Rice")
        )

        fun getById(id: String): Author? {
            return authors.firstOrNull {
                it.id == id
            }
        }
    }
}