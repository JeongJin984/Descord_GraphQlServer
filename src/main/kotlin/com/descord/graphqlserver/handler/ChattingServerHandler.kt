package com.descord.graphqlserver.handler

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class ChattingServerHandler {
    @MessageMapping("")
    fun nothing() {}
}