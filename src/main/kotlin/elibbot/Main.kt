package elibbot

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


fun main(args: Array<String>) {
//    System.getProperties()["proxySet"] = "true";
//    System.getProperties()["socksProxyHost"] = "127.0.0.1";
//    System.getProperties()["socksProxyPort"] = "9150";


    ApiContextInitializer.init()
    try {
        val botsApi = TelegramBotsApi()
        botsApi.registerBot(Bot())
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
    println("LoggingTestBot successfully started!")
}
