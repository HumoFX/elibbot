package elibbot

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


fun main(args: Array<String>) {

    ApiContextInitializer.init()
    try {
        val botsApi = TelegramBotsApi()
        botsApi.registerBot(Bot())
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
    println("LoggingTestBot successfully started!")
}
