package elibbot

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

open class Txt {
    
    open fun write(name:String,message:String,user_id:Long)
    {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date = Date()
        var log = File("C:\\Users\\User\\Documents\\Humo\\$name.txt")
        try {

            if (log.exists() === false) {
                println("We had to make a new file.")
                log.createNewFile()
            }
            val out = FileWriter(log, true)
            out.append(" $message  :: ${dateFormat.format(date)}\n ")
            out.close()
        } catch (e: IOException) {
            println("COULD NOT LOG!!")
        }


    }
    fun delete_file(name: String)
    {
        //удаление с использованием полного пути в файлу
        var file = File("C:\\Users\\User\\Documents\\Humo\\$name.txt")
        if (file.delete()) {
            println("$name.txt файл удален")
        } else
            println("Файла $name.txt не обнаружено")
    }
}