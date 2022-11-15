package com.descord.graphqlserver.controller

import com.descord.graphqlserver.testDomain.Author
import com.descord.graphqlserver.testDomain.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.graphql.data.method.annotation.*
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class GreetingController(
    private val requesterMap: Map<String, RSocketRequester>
    ) {
    @QueryMapping
    suspend fun getFullAccount(@Argument userId: Long?): List<UserServerJoin>? {
        return withContext(Dispatchers.IO) {
            requesterMap["chatting_server"]!!
                .route("my.join.info")
                .data(userId!!)
                .retrieveFlux(UserServerJoin::class.java)
                .collectList()
                .block()
        }
    }

    @SubscriptionMapping
    fun serverJoinInfos(@Argument serverId: Long?): Flux<UserServerJoin> {
        return requesterMap["chatting_server"]!!
            .route("join.info")
            .data(serverId!!)
            .retrieveFlux(UserServerJoin::class.java)
    }

    @BatchMapping(typeName = "UserServerJoin", field = "account")
    fun joiningAccounts(serverJoinInfo: List<UserServerJoin>): Flux<Account> {
        return Flux.fromIterable(serverJoinInfo)
            .flatMap {
                    requesterMap["account_server"]!!
                        .route("find")
                        .data(it.userId!!)
                        .retrieveMono(Account::class.java)
                }
    }

    @QueryMapping
    suspend fun greeting(): Greeting? {
        return  withContext(Dispatchers.IO)  {
           Mono.just(Greeting("Hello World!!!"))
                .block()
        }
    }

    @QueryMapping
    fun bookById(@Argument id: String): Book? {
        return Book.getById(id)
    }

    @SchemaMapping
    fun author(book: Book): Author? {
        return Author.getById(book.id)
    }

    enum class UserLevel { OWNER, MANAGER, JOINER }
    data class UserServerJoin(val id: Long?, val userId: Long?, val userLevel: UserLevel?, val userServer: UserServer?, val Account: Account?)
    data class Account(val id: Long?, val name: String?)
    data class UserServer(val id:Long?, val name: String?)
    data class Greeting(val greeting: String)
}