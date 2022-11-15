package com.descord.graphqlserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GraphQlServerApplication

fun main(args: Array<String>) {
    runApplication<GraphQlServerApplication>(*args)
}
