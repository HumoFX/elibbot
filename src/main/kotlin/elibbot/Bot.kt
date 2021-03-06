package elibbot



import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Document
import org.telegram.telegrambots.meta.api.objects.PhotoSize
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Bot : TelegramLongPollingBot()
    {
        private var file = Txt()
        private val db = DB()
        private var users = mutableMapOf<Long,ArrayList<String>>()
        private var univer_lesson = mutableMapOf<Int,ArrayList<Int>>()
        private var admin_univer_id = mutableMapOf<Long,Int>()
        private var universities = arrayListOf<String>()
        private var univerlist = mutableMapOf<Int,String>()
        private var file_ids = arrayListOf<String>()
        private var users_pos  = mutableMapOf<Long,Int>()
        private var books = mutableMapOf<String,ArrayList<String>>()
        private var teacher = mutableMapOf<Int,ArrayList<ArrayList<String>>>()
        private var teacher_size = ArrayList<ArrayList<String>>()
        private var files_size = mutableMapOf<String, ArrayList<String>>()
        private var addFile = mutableMapOf<Int,ArrayList<ArrayList<String>>>()
        private var books_id = arrayListOf<String>()
        private var books_name = arrayListOf<String>()
        private var lessons = mutableMapOf<Long,ArrayList<String>>()
        private var book_id = mutableMapOf<Long,String>()
        private var book_file = mutableMapOf<String,String>()
        private var admin_add_book = mutableMapOf<Long,String>()
        private var admin_add_photo = mutableMapOf<Long,String>()
        private var admin_add_file = mutableMapOf<Long,Int>()
        private var inited = false
        private var registr = mutableMapOf<Long,ArrayList<String>>()
        private var admins = mutableMapOf<Long, Int>()
        private var archived = mutableMapOf<Long,ArrayList<String>>()
        private var posting_photo = mutableMapOf<Long,String>()
        private var UniverRole = mutableMapOf<String,Int>()
        override fun getBotToken(): String {
            return "995371026:AAEKQNV2Xin97x0m-JLnJnutDN9VzJyNUbU"
        }

        override fun getBotUsername(): String {
            return "Qrbookbot"
        }
        private fun init()
        {
            if(!(this.inited))
            {
                UniverRole.put("Студент",1)
                UniverRole.put("Преподователь",2)
                UniverRole.put("Администрация",3)
                UniverRole.put("Работник",4)

                var db_read = db.read()
                var db_size = db_read[0][2].toInt()
                var a = 0
                while (a< db_size)
                {

                   // println("univer ${universities.size}")
                    universities.add(db_read[a][1])
                    univerlist.put(db_read[a][0].toInt(),db_read[a][1])
                    a++
                }

                var db_read_book = db.read_book()
                var db_book_size = db_read_book[0][5].toInt()
                var i = 0
                while(i < db_book_size)
                {
                    var book = arrayListOf<String>()
                    a = 1
                    while(a < 4)
                    {
                        book.add(db_read_book[i][a])
                        a++
                    }

                    if(!books.contains(db_read_book[i][4])) {
                        books.put(db_read_book[i][4], book)
                        books_name.add(db_read_book[i][1])
                        books_id.add(db_read_book[i][4])
                        book_file.put(db_read_book[i][0],db_read_book[i][4])
                    }
                    i++

                }
                var teachers = db.read_teacher()

                if(teachers[0][3].toInt() > 0) {

                    for ((univ, univN) in univerlist) {
//                        println("SS")
                        var arrays = ArrayList<ArrayList<String>>()


                        var c = 0
                        while (c < teachers[0][3].toInt()) {
//                            println("univ = $univ  techuniv = ${teachers[c][1]}")
                            if (teachers[c][1] == "0"){
                                var array = ArrayList<String>()
//                                println("SS2")

                                array.add(teachers[c][0])
                                array.add(teachers[c][2])
                                arrays.add(array)
                                array.add(univ.toString())
                                teacher_size.add(array)

                            }
                            c++
                        }
                        teacher.put(0, arrays)
//                        println("teach $teacher")
                    }
                    println(teacher_size)

                }

                var file = db.read_file()
                println("${file[0][0]} ${file[0][1]} ${file[0][2]} ${file[0][3]} ${file[0][4]} ${file[0][5]} ")
                var j =0
                while (j < teacher_size.size)
                {
                    var array = ArrayList<ArrayList<String>>()

                    var c = 0
                    while (c < file[0][5].toInt())
                    {
                        if( teacher_size[j][0].toInt() == file[c][2].toInt())
                        {
                          var arr = ArrayList<String>()
                          arr.add(file[c][4])
                          arr.add(file[c][3])
                          arr.add(file[c][1])
                          arr.add(file[c][0])
                           array.add(arr)
                            files_size.put(file[c][0],arr)
                        }
                        c++
                    }
                    addFile.put(teacher_size[j][0].toInt(),array)
                    j++
                }



                var db_user = db.read_user()
                var db_user_size = db_user[0][10].toInt()


                i = 0
                while( i < db_user_size)
                {
                    var user_info = arrayListOf<String>()
                    a = 1
                    while (a < 10)
                    {
                        user_info.add(db_user[i][a])
                        a++
                    }
                    users.put(db_user[i][0].toLong(),user_info)
                    users_pos.put(db_user[i][0].toLong(),db_user[i][8].toInt())
                    if(user_info[4].toInt() != 0)
                    {
                        admins.put(db_user[i][0].toLong(),user_info[4].toInt())
                    }
                    //println("Admins $admins \n")
                    var archive = db_user[i][9].split(",")
                    var arr = arrayListOf<String>()
                    archive.forEach { i -> arr.add(i) }
                    if(arr[0]=="nll")
                        arr.removeAt(0)
//                    println("arr-- $arr")
//
                    if(arr.isNotEmpty())
                    {
                        archived.put(db_user[i][0].toLong(),arr)
                    }

//                    if(archive.get(0).equals("nll") )
//                    {
//
//                    }
                    if(archive.isNotEmpty())
                    {
//                       archived.put(db_user[i][0].toLong(),archive)
//                        println("arch $archived")
                    }
                   // user_info.clear()
                    i++
                }

                inited = true
            }

        }

        override fun onUpdateReceived(update: Update?) {

           if (update!=null)
           {
               init()
               println("elem =${book_file.keys.last()} ")
               if(update.hasMessage())
               {
                   //println("EVRBDY $users")
                   val chat_id = update.message.chatId
                   val message = update.message.text
                   val message_id = update.message.messageId

                   var user_username:String = try{
                       update.message.from.userName
                   }catch(e :IllegalStateException ){
                       "null"
                   }
                   var last_name = " "
                   var first_name = update.message.from.firstName
                   val pos = users_pos[chat_id]

                   if(update.message.hasDocument() )
                   {
                       val file = update.message.document

                       if(pos == 11) {
                           getFiles(chat_id, file)
                           add_book(chat_id)
                           buttongroup2(chat_id,message)
                       }
                       if(pos == 34){
                           getNewFile(chat_id,file)

                       }
                   }
                   //join(chat_id)
                   if(update.message.hasPhoto())
                   {

                       var photo = update.message.photo
                       var id = getPhoto(chat_id,photo)
                       if(pos == 5)
                       {
                           if(admins.containsKey(chat_id))
                           {
                               posting_photo.put(chat_id,id)
                           }
                       }

                       if(pos == 10)
                       {
                           admin_add_photo.put(chat_id,id)
                           users_pos.replace(chat_id,11)
                           db.update_position(chat_id,11)
                       }

                        //add_book(chat_id,photo)
                   }

                   if (update.message.hasText() )
                   {

                       if(update.message.text.startsWith("/start"))
                       {

                           if(users.containsKey(chat_id)){
//                               if(message.length>30)
//                               {
//                                   sendDocument(chat_id,recover_olds(message))
//                               }

                                   update(chat_id,message, first_name, last_name, user_username)


                               maintenance(chat_id)
                           }
                           else {
                               update(chat_id,message, first_name, last_name, user_username)
                           }
                       }
                       else{
                           if(!users_pos.containsKey(chat_id)){
                               update(chat_id,message, first_name, last_name, user_username)
                           }
                       }
                       if(pos == 0)
                       {
                           var array = arrayListOf<String>()
                           array.add(message)
                           array.add("0")
                           registr.put(chat_id,array)
                           registration(chat_id,message,first_name,array[0],user_username,0,0," ",0,1)
                           //get_groupname(chat_id)
 //
                          // buttongroup1(chat_id,message)
                           deleteMessage(chat_id,message_id)
                           deleteMessage(chat_id,message_id-1)
                           deleteMessage(chat_id,message_id-2)
                       }
                       else if(pos == 2)
                       {
                           db.update_group(chat_id,message)
                           var array = users[chat_id]
                           array!!.set(8,message)
                           users.replace(chat_id,array)
                           deleteMessage(chat_id,message_id)
                           deleteMessage(chat_id,message_id-1)
                           maintenance(chat_id)

                       }
                       else if(pos == 3)
                       {
                           buttongroup2(chat_id,message)
                       }

                       else if(pos == 5)
                       {
                           if(message!="Готово") {
                               if (posting_photo.containsKey(chat_id)) {
                                   posting_photo.get(chat_id)?.let { posting(it, message) }
                                   posting_photo.remove(chat_id)
                                   maintenance(chat_id)
                               }
                           }
                           else
                           {
                               buttongroup2(chat_id,message)
                           }

                       }
                       //println("ALLBOOKS $books")
                       //println("book_info1 $book_info")
                       //sendDocument(user_id,get_book_id)
                       when (pos) {
                           6 -> {
                               add_admin(chat_id,message)
                               buttongroup2(chat_id,message)
                           }
                           7 -> {
                               add_super_admin(chat_id,message)
                               buttongroup2(chat_id,message)

                           }
                           8 -> {
                               add_univer(chat_id,message)
                               buttongroup2(chat_id,message)
                           }
                           9 -> {
                               buttongroup1(chat_id,message)
                           }
                           10 -> {
                               buttongroup2(chat_id,message)
                           }
                           12 -> {
                               if(message != "Готово") {
                                   if (univerlist.containsKey(message.toInt())) {
                                       admin_univer_id.put(chat_id, message.toInt())
                                       users_pos.replace(chat_id, 10)
                                       db.update_position(chat_id, 10)
                                       sendMessage(chat_id, "``` Send/Forward Photo then Book```")
                                   } else {
                                       sendMessage(chat_id, "``` Incorrect ID!Enter Again```")
                                   }
                               }
                               buttongroup2(chat_id, message)
                           }
                           13 -> {
                               if(message != "Готово" && !message.startsWith("/start")) {
                                   search(chat_id, message)
                               }
                               buttongroup2(chat_id, message)
                           }
                           22 -> {
                               var array = lessons.get(chat_id)!!.toMutableList()
                               array.add(message)
                               val univer = users.get(chat_id)
                               admin_univer_id.get(chat_id)?.let { db.write_lesson(it,array.get(0),message) ; println("OK") }
                               maintenance(chat_id)
                           }
                           21 -> {
                               var array = arrayListOf<String>()
                               array.add(message)
                               lessons.put(chat_id,array)
                               sendMessage(chat_id,"Enter Text")
                               users_pos.replace(chat_id,22)
                               db.update_position(chat_id, 22)

                           }
                           20 -> {
                               if(message != "Готово") {
                                   if (univerlist.containsKey(message.toInt())) {
                                       admin_univer_id.put(chat_id, message.toInt())
                                       users_pos.replace(chat_id, 21)
                                       db.update_position(chat_id, 21)
                                       sendMessage(chat_id, "``` SEND GROUPNAME```")
                                   } else {
                                       sendMessage(chat_id, "``` Incorrect ID!Enter Again```")
                                   }
                               }
                               buttongroup2(chat_id, message)

                           }
//                           30 -> {
//                               if(message != "Готово") {
//                                   if (univerlist.containsKey(message.toInt())) {
//                                       admin_univer_id.put(chat_id, message.toInt())
//                                       users_pos.replace(chat_id, 31)
//                                       db.update_position(chat_id, 31)
//                                       sendMessage(chat_id, "``` SEND TEACHER's NAME```")
//                                   } else {
//                                       sendMessage(chat_id, "``` Incorrect ID!Enter Again```")
//                                   }
//                               }
//                               buttongroup2(chat_id, message)
//
//                           }
                           31 -> {
                               if(message != "Готово") {
                                   val array = ArrayList<ArrayList<String>>()
                                   val arr = ArrayList<String>()
                                   arr.add(teacher_size.size.toString())
                                   arr.add(message)
                                   array.add(arr)
                                   teacher_size.add(arr)
                                   teacher[0]!!.add(arr)
                                   db.write_teacher(message)
                                   sendMessage(chat_id,"Added")

                               }
                               buttongroup2(chat_id, message)

                           }
                           23 -> {
                               if(message != "Готово") {
                                   val univer = users[chat_id]!![3]
                                   val lesson = univer_lesson[univer.toInt()]
                                   if (lesson != null) {
                                       if (lesson.contains(message.toInt()-1)) {
                                           var text = "*** Расписание группы - ${db.read_lesson(univer)[message.toInt()-1][1]} \n ***"
                                           text += db.read_lesson(univer)[message.toInt()-1][2]
                                           sendMessage(chat_id,text)
                                           lessons.clear()
                                           maintenance(chat_id)
                                       } else {
                                           sendMessage(chat_id, "``` Неверный номер.Введите заново!```")
                                       }
                                   }
                               }
                               lessons.clear()
                               buttongroup2(chat_id, message)

                           }
                           32->{
                               if(message != "Готово") {
//                                   if (univerlist.containsKey(message.toInt())) {
//                                       admin_univer_id.put(chat_id, message.toInt())
//
////                                       sendMessage(chat_id,"```TEACHERS  ${teacher.get}```")
                                       if(teacher.containsKey(message.toInt()))
                                       {
                                           users_pos.replace(chat_id, 33)
                                           db.update_position(chat_id, 33)
                                       }
                                       else{
                                           sendMessage(chat_id,"NO TEACHERS")
                                           maintenance(chat_id)
                                       }
                                   } else {
                                       sendMessage(chat_id, "``` Incorrect ID!Enter Again```")
                                   }
                               buttongroup2(chat_id, message)
                           }
                           33->{
                               if (message != "Готово")
                               {
//                                   val univer_id = admin_univer_id.get(chat_id)
                                   var array = teacher[0]!!
                                   println(" size  =  ${array.size}")
                                   if (message.toInt() <= array.size )
                                   {
                                       println("te  ${array[message.toInt() - 1]}")
                                       admin_add_file.put(chat_id,array[message.toInt() - 1][0].toInt())
                                       sendMessage(chat_id, "``` SEND FILE ```")
                                       users_pos.replace(chat_id,34)
                                       db.update_position(chat_id,34)
                                   }
                                   else {
                                       sendMessage(chat_id, "INCORRECT ID! Try again")
                                   }
                               }
                               else{
                                   buttongroup2(chat_id,message)
                               }
                           }
                           34->{
                               buttongroup2(chat_id,message)
                           }
                       }


                       if(book_id.containsKey(chat_id)) {
                           if (users.containsKey(chat_id) && (users[chat_id]!![3].toInt() != 0)) {
                               val text = "Пожалуйста подождите . . . "
                               val fileId = book_id[chat_id]
                               if(!books.contains(fileId))
                               {
                                   book_id[chat_id]?.let { sendMessage(chat_id,text);archivate(chat_id, getid(message)) ; deleteMessage(chat_id,message_id+2);deleteMessage(chat_id,message_id) }
                                   book_id.remove(chat_id)
                               }
                               else {
                                   var book_info = books[book_id[chat_id]]
                                   //println("ALLBOOKS $books")
                                   //println("book_info1 $book_info")
                                   //sendDocument(user_id,get_book_id)
                                   book_id[chat_id]?.let {
                                       sendMessage(chat_id, text);sendPhoto(
                                       chat_id,
                                       book_info!![2],
                                       book_info!![0]
                                   ); archivate(chat_id, getid(message)); deleteMessage(chat_id, message_id + 2)
                                   }
                                   book_id.remove(chat_id)
                               }
                           }
                       }
                       log(first_name,user_username,chat_id.toString(),message)
                   }


               }
               if(update.hasCallbackQuery())
               {
                   var call_data = update.callbackQuery.data
                   var user_id = update.callbackQuery.message.chatId
                   val message_id = update.callbackQuery.message.messageId

                   if(call_data.startsWith("archive"))
                   {
                       val fileid = call_data.substring(8)
                           deleteMessage(user_id, message_id)
                           deleteMessage(user_id, message_id - 1)
                           if (!archived.containsKey(user_id)) {
                               var array = ArrayList<String>()
                               array.add(fileid)
                               archived.put(user_id, array)
                               db.update_archive(user_id, fileid)

                           } else {
                               var array = archived[user_id]
                               if (!array!!.contains(fileid)) {
                                   array.add(fileid)
                                   archived.replace(user_id, array)
                                   var info = users[user_id]!![8]
                                   info = "$info,$fileid"
                                   db.update_archive(user_id, info)
                                   println("infoo $info")
                               }
                           }

                       val answer = AnswerCallbackQuery()
                       answer.callbackQueryId = update.callbackQuery.id
                       answer.text = "Принято!"
                       answer.showAlert = true
                       try {
                           execute(answer)
                       } catch (e: TelegramApiException) {
                           e.printStackTrace()
                       }
                   }
                   else if (call_data.startsWith("ol")){
                       val num = call_data.substring(2)
                       val univer = users[user_id]!![3]
                       val lesson = univer_lesson[univer.toInt()]
                       if (lesson != null) {
                           var text = "*** Расписание - ${db.read_lesson(univer)[num.toInt()-1][1]} \n ***"
                           text += db.read_lesson(univer)[num.toInt()-1][2]
                           sendMessage(user_id,text)
                           lessons.clear()
                           maintenance(user_id)

                       }

                   }
                   else if (call_data == "teach"){
                       val univer = users[user_id]!![3]
                       val size = teacher[univer.toInt()]!!.size
                       var arrays = teacher[univer.toInt()]!!
                       var markupInline = InlineKeyboardMarkup()
                       val rowsInline = ArrayList<List<InlineKeyboardButton>>()
                       val rowInline = ArrayList<InlineKeyboardButton>()
//            var last = array.last()
                       var a = 0
                       var text = "*** Учителя\n ***"
                       var c = 1
                       var array = ArrayList<Int>()
                       for ( i in teacher[univer.toInt()]!!)
                       {
                           array.add(c)
                           text += "$c - ${i[1]}\n"
                           c++
                       }

                       for (id in array) {
//                           println("rowsize ${array.size}")
//                           sendMessage(user_id,text)
//                           users_pos.replace(user_id, 33)
//                           db.update_position(user_id, 33)

                           if (array.size - a >=5)
                           {
                               rowInline.add(InlineKeyboardButton().setText(id.toString()).setCallbackData("t$id"))
                               println("rowsize ${rowInline.size}")
                               println(rowInline.size.rem(5))
                               if((rowInline.size - a).rem(5) == 0)
                               {

                                   rowsInline.add(arrayListOf(rowInline[id - 5],rowInline[id - 4],rowInline[id - 3],rowInline[id - 2],rowInline[id-1]))
                                   a += 5
                                   println("rowsize ${rowInline.size}")
                               }
                           }
                           else if (array.size - a <5)
                           {
                               println("rowsize ${rowInline.size}")
                               rowInline.add(InlineKeyboardButton().setText(id.toString()).setCallbackData("t$id"))
                               if(array.size - a == 4)
                               {
                                   if((rowInline.size-a).rem(4) == 0)
                                   {
                                       rowsInline.add(arrayListOf(rowInline[id - 4],rowInline[id - 3],rowInline[id - 2],rowInline[id-1]))
                                       a += 4
                                   }
                               }
                               else if(array.size - a == 3)
                               {
                                   if((rowInline.size-a).rem(3) == 0)
                                   {
                                       rowsInline.add(arrayListOf(rowInline[id - 3],rowInline[id - 2],rowInline[id-1]))
                                       a += 3
                                   }
                               }
                               else if(array.size - a == 2)
                               {
                                   if((rowInline.size-a).rem(2) == 0)
                                   {
                                       rowsInline.add(arrayListOf(rowInline[id - 2],rowInline[id-1]))
                                       a +=2
                                   }
                               }
                               else if(array.size - a == 1)
                               {
                                   rowsInline.add(listOf(rowInline[id-1]))
                               }
                           }
                       }
                       val message = SendMessage() // Create a message object object
                           .setChatId(user_id)
                           .setParseMode("Markdown")
                           .setText(text)

                       markupInline.keyboard = rowsInline
                       message.replyMarkup = markupInline
                       try {
                           execute(message) // Sending our message object to user
                       } catch (e: TelegramApiException) {
                           e.printStackTrace()
                       }
                   }
                   else if (call_data.startsWith("t"))
                   {
                       val num = call_data.substring(1)
                       val univer_id = 0
                       val array = teacher[univer_id]!!
                       println(" size  =  ${array.size}")
                       if (num.toInt() <= array.size ) {
                           println("te  ${array[num.toInt() - 1]}")
                           val arrays = addFile[array[num.toInt() - 1][0].toInt()]
                           println(arrays)
                           var i = 0
                           if (arrays!!.size >= 1) {
                               var count = 0
                               val array1 = ArrayList<String>()
                               while (i < arrays.size) {
                                   println("c = $arrays")
                                   count++
//                                  text += " ${i+1}.[${arrays[i][1]}](t.me/Qrbookbot?start=FILE${arrays[i][3]})\n"
                                   array1.add("$count.[${arrays[i][1]}](t.me/Qrbookbot?start=FILE${arrays[i][3]})\n")
                                   i++
                               }
                               var j = 0
                               var b = 0
                               while (b < array1.size) {
                                   var text1 = "*** ${array[num.toInt() - 1][1]}\n ***"
                                   j = b
                                   while ((b - j < 50) and (b < array1.size)) {
                                       text1 += array1[b]
                                       b++
                                   }

                                   sendMessage(user_id, text1)
                               }
                           }
                           else
                           {
                               sendMessage(user_id, "Пока нет файлов")
                           }

                       }
                   }
                   else {
                       if(call_data.startsWith("R"))
                            {
                                val num = call_data.substring(1)
                                if(num!="1")
                                {
                                    db.update_group(user_id,call_data)
                                    var array = users[user_id]
                                    array!!.set(8,call_data)
                                    users.replace(user_id,array)
                                    val new_message = DeleteMessage()
                                        .setChatId(user_id)
                                        .setMessageId(message_id.toInt())
                                    try {
                                        execute(new_message)
                                    } catch (e: TelegramApiException) {
                                        e.printStackTrace()
                                    }
                                    maintenance(user_id)
                                }
                                else{
                                    val new_message = DeleteMessage()
                                        .setChatId(user_id)
                                        .setMessageId(message_id.toInt())
                                    try {
                                        execute(new_message)
                                    } catch (e: TelegramApiException) {
                                        e.printStackTrace()
                                    }
                                    get_groupname(user_id)
                                }
                            }
                       else {
                                for ((a, _) in univerlist) {

                                    if (call_data == "$a") {
                                        db.update_univer(user_id, a)
                                        var users_info = users[user_id]
                                        // println("users ${users.get(user_id)}")
                                        if (users.containsKey(user_id)) {
                                            users_info!![3] = (a).toString()
                                            users.replace(user_id, users_info)

                                        }
                                        println("users ${users.get(user_id)}")
                                        val new_message = DeleteMessage()
                                            .setChatId(user_id)
                                            .setMessageId(message_id.toInt())
                                        try {
                                            execute(new_message)
                                        } catch (e: TelegramApiException) {
                                            e.printStackTrace()
                                        }
                                        val answer = AnswerCallbackQuery()
                                        answer.callbackQueryId = update.callbackQuery.id
                                        answer.text = "Принято!"
                                        answer.showAlert = true
                                        users_pos.replace(user_id, 1)
                                        try {
                                            execute(answer)
                                            role(user_id)
                                        } catch (e: TelegramApiException) {
                                            e.printStackTrace()
                                        }
                                        if (users_info != null) {
                                            users_info.forEachIndexed { index, s ->
                                                if (s.contains("'")) {
                                                    val b = s.replace("'", "''");users_info[index] =
                                                        b; println("s- $b i- $index")
                                                }
                                            }
                                            db.write(
                                                user_id,
                                                users_info[0],
                                                users_info[1],
                                                users_info[2],
                                                users_info[3].toInt(),
                                                users_info[4].toInt(),
                                                users_info[5],
                                                users_info[6].toInt(),
                                                users_info[7].toInt()
                                            )
                                        }

                                        //maintenance(user_id)

                                        break
                                    }
                                }
                            }
                   }
               }
           }
        }
        private fun recover_olds(message: String): String {
                return message.substring(28)
        }
        private fun reload(user_id: Long)
        {
            if(admins[user_id] == 2)
            {
                books.clear()
                book_file.clear()
                books_name.clear()
                books_id.clear()
                this.inited = false
                init()
            }
        }
        private fun get_name(user_id:Long)
        {

            sendMessage(user_id,"``` Для полноценной работы бота заполните анкету.\nПожалуйста введите вашу Фамилию и Имя (Иванов Иван) :```")
                users_pos.put(user_id, 0)


        }
        private fun get_groupname(user_id:Long)
        {
            val message = SendMessage()
                .setChatId(user_id)
                .setParseMode("Markdown")
                .setText("Пожалуйста введите Номер Группы :")
            users_pos.put(user_id,2 )
            try{
                execute(message)
            }
            catch(e:TelegramApiException)
            {
                e.printStackTrace()
            }

        }

        private fun registration(user_id: Long,message: String,first_name: String,last_name:String,user_username:String,university:Int,role:Int,books_id:String,books_num:Int,position:Int)
        {
           // if(message.)
            var user_info = arrayListOf<String>()
            user_info.add(first_name)
            user_info.add(last_name)
            user_info.add(user_username)
            user_info.add(university.toString())
            user_info.add(role.toString())
            user_info.add(books_id)
            user_info.add(books_num.toString())
            user_info.add(position.toString())
            user_info.add(" ")

            users.put(user_id,user_info)

            //db.write(user_id,user_info[0],user_info[1],user_info[2],user_info[3].toInt(),user_info[4].toInt(),user_info[5],user_info[6].toInt(),user_info[7].toInt())
            println(" user ${users.get(user_id)}")
            //users_pos.put(user_id,0)
            //user_info.clear()
            //users_pos.replace(user_id,2)
            if(university == 0)
            {
                choose_university(user_id,message)
            }

        }

        private fun choose_university(user_id: Long,message: String)
        {
            var text = "Выберите ваш университет"
            var db_read = db.read()
            var db_size = db_read[0][2].toInt()
            var a = 0
            var markupInline = InlineKeyboardMarkup()
            val rowsInline = ArrayList<List<InlineKeyboardButton>>()

            for ((b,v) in univerlist)
            {
                val rowInline = ArrayList<InlineKeyboardButton>()
                rowInline.add(InlineKeyboardButton().setText(v).setCallbackData(b.toString()))
                rowsInline.add(rowInline)
            }
            val message = SendMessage() // Create a message object object
                .setChatId(user_id)
                .setParseMode("Markdown")
                .setText("*$text*")

            markupInline.keyboard = rowsInline
            message.replyMarkup = markupInline
            try {
                execute(message) // Sending our message object to user
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }


        }

        private fun lessonButton(user_id: Long){
            val univer = users[user_id]
            var text = "*** Номер гр. -\tНазвание гр. \n***"
            var i=0
            var array = ArrayList<Int>()
            var array_group = ArrayList<String>()
            while (i < db.read_lesson(univer!![3])[0][3].toInt() )
            {
                array.add(i+1)
                text += "`\t\t\t\t\t\t\t${i+1} - ${db.read_lesson(univer[3])[i][1]}` \n"
                array_group.add(db.read_lesson(univer[3])[i][1])
                i++
            }
            univer_lesson.put(univer[3].toInt(),array)
            val text2 = "__Введите номер группы!__"
            sendMessage(user_id,text)
//
//            users_pos.replace(user_id,23)
//            db.update_position(user_id,23)
            var markupInline = InlineKeyboardMarkup()
            val rowsInline = ArrayList<List<InlineKeyboardButton>>()
            val rowInline = ArrayList<InlineKeyboardButton>()
//            var last = array.last()
            var a = 0
            for (id in array) {
                println("rowsize ${array.size}")

                if (array.size - a >=5)
                {
                    rowInline.add(InlineKeyboardButton().setText(id.toString()).setCallbackData("ol$id"))
                    println("rowsize ${rowInline.size}")
                    println(rowInline.size.rem(5))
//                    println("rw ${rowInline.get(id-1)}")
                    if((rowInline.size - a).rem(5) == 0)
                    {
//                        rowsInline.add(rowInline)

                        rowsInline.add(arrayListOf(rowInline[id - 5],rowInline[id - 4],rowInline[id - 3],rowInline[id - 2],rowInline[id-1]))
                        a += 5
                        println("rowsize ${rowInline.size}")

                    }
                }
                else if (array.size - a <5)
                {
                    println("rowsize ${rowInline.size}")

                    rowInline.add(InlineKeyboardButton().setText(id.toString()).setCallbackData("ol$id"))
                    if(array.size - a == 4)
                    {
                        if((rowInline.size-a).rem(4) == 0)
                        {
                            rowsInline.add(arrayListOf(rowInline[id - 4],rowInline[id - 3],rowInline[id - 2],rowInline[id-1]))

                            a += 4
                        }
                    }
                    else if(array.size - a == 3)
                    {
                        if((rowInline.size-a).rem(3) == 0)
                        {
                            rowsInline.add(arrayListOf(rowInline[id - 3],rowInline[id - 2],rowInline[id-1]))
                            a += 3

                        }
                    }
                    else if(array.size - a == 2)
                    {
                        if((rowInline.size-a).rem(2) == 0)
                        {
                            rowsInline.add(arrayListOf(rowInline[id - 2],rowInline[id-1]))
                            a +=2

                        }
                    }
                    else if(array.size - a == 1)
                    {

                        rowsInline.add(listOf(rowInline[id-1]))

                    }



                }




            }
//            val rowInline1 = ArrayList<InlineKeyboardButton>()
//            rowInline1.add(InlineKeyboardButton().setText("Учителя").setCallbackData("teach"))
//            rowsInline.add(listOf(rowInline1[0]))





            val message = SendMessage() // Create a message object object
                .setChatId(user_id)
                .setParseMode("Markdown")
                .setText(text2)

            markupInline.keyboard = rowsInline
            message.replyMarkup = markupInline
            try {
                execute(message) // Sending our message object to user
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }

        }

        private fun role(user_id: Long)
        {
            var text = "Выберите вашу роль в ВУЗе"
            var role = db.read()
            var a = 0
            var markupInline = InlineKeyboardMarkup()
            val rowsInline = ArrayList<List<InlineKeyboardButton>>()

            for ((b,v) in UniverRole)
            {
                val rowInline = ArrayList<InlineKeyboardButton>()
                rowInline.add(InlineKeyboardButton().setText(b).setCallbackData("R$v"))
                rowsInline.add(rowInline)
            }
            val message = SendMessage() // Create a message object object
                .setChatId(user_id)
                .setParseMode("Markdown")
                .setText("*$text*")

            markupInline.keyboard = rowsInline
            message.replyMarkup = markupInline
            try {
                execute(message) // Sending our message object to user
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }

        }

        private fun maintenance (user_id: Long)
        {
            var text = "Меню"
            var button_text1 = "Архив"
            var button_text2 = "Статистика"
            var button_text15 = "Список книг"
            var button_text3 = "Add Univer"
            var button_text4 = "Add book"
            var button_text5 = "Add admin"
            var button_text6 = "Add SuperAdmin"
            var button_text7 = "Posting"
            var button_text8 = "Univer Stats"
            var button_text9 = "Поиск"
            var button_text10 = "Reload"
            var button_text11 = "Add Lesson"
            var button_text13 = "Add Genre"
            var button_text14 = "Add File"
            val button_text12 = "Онлайн Уроки"
            val button_text16 = "Жанры"
            val replyKeyboardMarkup = ReplyKeyboardMarkup()
            val message = SendMessage()
                .setChatId(user_id)
                .setParseMode("Markdown")
                .setText("_${text}_")

            message.setReplyMarkup(replyKeyboardMarkup)
            replyKeyboardMarkup.selective = true
            replyKeyboardMarkup.resizeKeyboard = true
            replyKeyboardMarkup.oneTimeKeyboard = false

            val keyboard = ArrayList<KeyboardRow>()
            val keyboardFirstRow = KeyboardRow()
            val keyboardSecondRow = KeyboardRow()
            val keyboardThirdRow = KeyboardRow()
            val keyboardFourthRow = KeyboardRow()
            val keyboardFifthRow = KeyboardRow()
            val keyboardSixRow = KeyboardRow()
            val keyboardSeventhRow = KeyboardRow()
            val keyboardEighthRow = KeyboardRow()
            keyboardFirstRow.add(KeyboardButton("$button_text1"))
            keyboardFirstRow.add(KeyboardButton("$button_text2"))
//            keyboardFirstRow.add(KeyboardButton("$button_text15"))
            keyboardSecondRow.add(KeyboardButton("$button_text3"))
            keyboardSecondRow.add(KeyboardButton("$button_text4"))
            keyboardSeventhRow.add(KeyboardButton("$button_text11"))
            keyboardEighthRow.add(KeyboardButton("$button_text13"))
            keyboardEighthRow.add(KeyboardButton("$button_text14"))
            keyboardThirdRow.add(KeyboardButton("$button_text5"))
            keyboardThirdRow.add(KeyboardButton("$button_text6"))
            keyboardFourthRow.add(KeyboardButton("$button_text7"))
            keyboardFourthRow.add(KeyboardButton("$button_text10"))
            keyboardFifthRow.add(KeyboardButton("$button_text8"))
            keyboardSixRow.add(KeyboardButton("$button_text9"))
            keyboardSixRow.add(KeyboardButton("$button_text12"))
            keyboardSixRow.add(KeyboardButton("$button_text16"))
            keyboard.add(keyboardFirstRow)
            keyboard.add(keyboardSixRow)

            if(admins.containsKey(user_id))
            {
                var role = admins[user_id]
                if(role == 2)
                {
                    keyboard.add(keyboardSecondRow)
                    keyboard.add(keyboardThirdRow)
                    keyboard.add(keyboardFourthRow)
                    keyboard.add(keyboardFifthRow)
                    keyboard.add(keyboardSeventhRow)
                    keyboard.add(keyboardEighthRow)
                }
                if(role == 1)
                {
                    keyboard.add(keyboardFifthRow)
                }
            }

            replyKeyboardMarkup.keyboard = keyboard

            try {
                execute(message)
            }
            catch (e: TelegramApiException){
                e.printStackTrace()
            }
            users_pos.replace(user_id,9)
            db.update_position(user_id,9)
        }

        private fun posting(file_id: String,message: String)
        {
            val size = users.size
            var i = 0
            var sendPhoto =  SendPhoto()
            while(i < size)
            {
                sendPhoto.chatId = db.read_user()[i][0]
                sendPhoto.setCaption(message)
                sendPhoto.setPhoto(file_id)
                try {
                    execute(sendPhoto)
                }
                catch (e:TelegramApiException)
                {
                    e.printStackTrace()
                }
                i++
            }
        }


        private fun search(user_id: Long,message: String)
        {
//            var a = 0
            var count = 0
            var text = "``` По вашему запросу найдено : ```"
            var text2 = " "
            var array = ArrayList<String>()
//            var temp: Array<Array<String>> = Array(books.size, { Array(1, {"${books.values}"}) })
//            while(a < books.size)
//            {
//
//                if(books_name[a].contains(message))
//                {
//
//                    count ++
//                    println("books--- ${books_name[a]} ${books.size} = $a")
//                    text2 += " $count. [${books_name[a]}](t.me/Qrbookbot?start=${books_id[a]})\n"
//
//                }
//                a++
//            }
            var c =0
            for((v,k) in books){
                if (k[0].contains(message))
                {
                    for((i,j) in book_file)
                    {
                        if(j.contains(v))
                        {
                            count++
                            array.add(" $count.[${k[0]}](t.me/Qrbookbot?start=${i})\n")
                        }
                    }
                }
            }

            println("assa0")

            for ((v,k) in addFile)
            {
                for(size in 0 until k.size step 1 )
                {
                    println(k[size][1])
                    println(size)
                    if(k[size][1].contains(message)){
                        count++
                        println("xx")
                        array.add(" $count.[${k[size][1]}](t.me/Qrbookbot?start=FILE${k[size][3]})\n")
                    }
                    else{
                        continue
                    }
                }
            }
            text += " $count  книг\n"
            sendMessage(user_id,text)
            var j =0
            var b =0
            while (b<array.size){
                var text1 = " "
                j = b
                while( (b - j< 50) and (b<array.size))
                {
                    text1 += array[b]
                    b++
                }

                sendMessage(user_id, text1)
            }
//            text += text2
//            sendMessage(user_id, text)
        }

        private fun univer_stats(user_id: Long,message: String)
        {
            if(admins.containsKey(user_id))
            {
                var univer = users[user_id]!![3]
                var read = db.read_user_info(univer.toInt())
                val size = db.read_user_info(univer.toInt())[0][6]
                val array = arrayListOf<Int>(1,2,3,4,5)
               // println(array)
               // println("READ $size $univer")
               // println(read[0][0])
                var text = "      Whole List of Users With Statistics : \n"
                text += ""
                var i = 0
                while(i < size.toInt())
                {
                    text += "__________________________________________\n"
                    text += "1-Телеграм ник      : ${read[i][0]} \n"
                    text += "2-Фамилие и Имя     : ${read[i][1]} \n"
                    text += "3-Телеграм Юзернейм :  @${read[i][2]} \n"
                    text += "4-Номер группы      : ${read[i][3]} \n"
                    text += "5-Кол-во скач. книг : ${read[i][5]} \n"
                    text += "6-Скачавшие книги   : ${read[i][4]} \n"
                    text += "___________________________________________\n"

                    i++
                }
                //sendMessage(user_id,text)

                file.write(user_id.toString(),text,user_id)
                sendDocument(user_id,user_id.toString())
                file.delete_file(user_id.toString())
            }
        }
        private fun add_univer(user_id: Long,message: String)
        {
            //var text = "Added"
            if(message != "Готово") {
                var index = univerlist.keys.last()
                index += 1
                print("UNIVER index = $index")
                db.write_univer(index,message)
                universities.add(message)
                univerlist.put(index,message)
                //println("Univer $universities")
                sendMessage(user_id, "Added")
            }
            //sendMessage(user_id,text)
            //btn_done(user_id)
           // users_pos.replace(user_id,0)
        }
        private fun add_book(user_id:Long)
        {

                if(admin_add_photo.containsKey(user_id) && admin_add_book.containsKey(user_id))
                {
                    var photo = admin_add_photo.get(user_id)
                    var id = admin_add_book.get(user_id)
                    var book_info = books.get(id)
                    if (photo != null) {
                        book_info!![2] = photo
                    }
                    var elem = book_file.keys.last().toInt()
                    elem += 1
                    if (id != null) {
                        if (book_info != null) {
                           // println("information ${book_info}")
                            book_info.forEachIndexed { index, s -> if( s.contains("'")){val a = s.replace("'","`");book_info[index] = a; println("s- $a i- $index")} }
                            books.replace(id, book_info)
                            books_name.add(book_info[0])
                            books_id.add(id)
                        }
                        db.write_book(elem,id, book_info!![0], book_info[1].toInt(), book_info[2])
                    }

                    var message = SendMessage()
                        .setChatId(user_id)
                        .setText("telegram.me/Qrbookbot?start=${elem}")
                    if (id != null) {
                        book_file.put(elem.toString(),id)
                    }
                    try {
                        execute(message)
                    }
                    catch (e:TelegramApiException)
                    {
                        e.printStackTrace()
                    }
                    admin_add_photo.remove(user_id)
                    admin_add_book.remove(user_id)
                    users_pos.replace(user_id,10)
                    db.update_position(user_id,10)

                }


        }
        private fun btn_done(user_id: Long)
        {
            var text = "Нажмите ```Готово``` если хотите остановить процесс."
            var button = "Готово"
            val replyKeyboardMarkup = ReplyKeyboardMarkup()
            val sendMessage = SendMessage()
                .setChatId(user_id)
                // .enableMarkdown(true)
                .setParseMode("Markdown")
                .setText(text)
            sendMessage.replyMarkup = replyKeyboardMarkup
            replyKeyboardMarkup.selective = true
            replyKeyboardMarkup.resizeKeyboard = true
            replyKeyboardMarkup.oneTimeKeyboard = false
            val keyboard = ArrayList<KeyboardRow>()
            val keyboardFirstRow = KeyboardRow()
            keyboardFirstRow.add("$button")
            keyboard.add(keyboardFirstRow)
            replyKeyboardMarkup.keyboard = keyboard
            try {
                execute(sendMessage)

            }
            catch (e: TelegramApiException){
                e.printStackTrace()
            }
            //users_pos.replace(user_id,1)
        }

        private fun sendMessage(user_id: Long, message: String)
        {
            var text = SendMessage()
                .setChatId(user_id)
                .setParseMode("Markdown")
                .setText(message)
            try {
                execute(text)
            }
            catch (err:TelegramApiException)
            {
                err.printStackTrace()
            }
        }
        private fun sendPhoto(user_id: Long,photo_id: String ,book_name: String)
        {
            var answer = SendPhoto()
                .setChatId(user_id)
                .setCaption("$book_name")
                .setPhoto(photo_id)
            try {
                execute(answer)
            }
            catch (e:TelegramApiException)
            {
                e.printStackTrace()
            }
        }
        private fun archivate(user_id: Long,file_id: String)
        {
            println("file $file_id")
            println("filelenght ${file_id.length}")
            var markupInline = InlineKeyboardMarkup()
            val rowsInline = ArrayList<List<InlineKeyboardButton>>()
            var button = "Архивировать"
            val rowInline = ArrayList<InlineKeyboardButton>()
            rowInline.add(InlineKeyboardButton().setText(button).setCallbackData("archive=$file_id"))
            rowsInline.add(rowInline)
            if (!book_file.contains(file_id))
            {
                if(file_id.startsWith("FILE")){
                    val id = file_id.substring(4)
                    val files_id = files_size[id]!![0]
                    println("files  == ${files_size[id]!![1]}")
                    val answer = SendDocument()
                        .setChatId(user_id)
                        .setDocument(files_id)
                    markupInline.keyboard = rowsInline
                    answer.replyMarkup = markupInline
                    // println("a = $a")
                    try {
                        execute(answer) // Sending our message object to user
                    } catch (e: TelegramApiException) {
                        e.printStackTrace()
                    }
                }
                else {
                    val answer = SendDocument()
                        .setChatId(user_id)
                        .setDocument(file_id)
                    markupInline.keyboard = rowsInline
                    answer.replyMarkup = markupInline
                    // println("a = $a")
                    try {
                        execute(answer) // Sending our message object to user
                    } catch (e: TelegramApiException) {
                        e.printStackTrace()
                    }
                }
            }
            else{
                val file = book_file[file_id]
                val answer = SendDocument()
                    .setChatId(user_id)
                    .setDocument(file)
                markupInline.keyboard = rowsInline
                answer.replyMarkup = markupInline
                // println("a = $a")
                try {
                    execute(answer) // Sending our message object to user
                } catch (e: TelegramApiException) {
                    e.printStackTrace()
                }
            }




        }
        private fun sendDocument(user_id: Long, message: String)
        {
            val initialFile = File("C:\\Users\\User\\Documents\\Humo\\$message.txt")
            var document = SendDocument()
            var targetStream = FileInputStream(initialFile)
            document.setDocument("$message.txt", targetStream as InputStream?)
            document.caption = message
            document.setChatId(user_id)
            try {
                execute(document)
            }
            catch (err:TelegramApiException)
            {
                err.printStackTrace()
            }
        }
        private fun getid(message: String): String {
            val get_book_id = message.substring(7)
            return get_book_id
        }
        private fun parser(user_id: Long,message: String): String {

            val get_book_id = message.substring(7)
            //  println("Book : $get_book_id")
            if (isInteger(get_book_id)) {
                val file_id = book_file[get_book_id]
                if (file_id != null) {
                    book_id.put(user_id, file_id)
                }
                return file_id.toString()
            } else {
                if (get_book_id.startsWith("FILE")) {
                    val id = get_book_id.substring(4)
                    if (files_size.containsKey(id)) {
                        book_id[user_id] = files_size[id]!![0]
                        println("books_id = $book_id")
                    }

                }
                else{
                    book_id[user_id] = get_book_id

                }
                return get_book_id

            }
        }
        private fun update(user_id: Long,message: String,first_name: String,last_name:String,user_username:String)
        {
            if (message.startsWith("/start") && message.length >= 7) {
                var book_id = parser(user_id,message)
                println("BOOOK $book_id")
                if(message.length>=31)
                {
                    println("HERE")
                    var book_name = "xx"
                    if(users.containsKey(user_id))
                    {
                        var user_info = users.get(user_id)
                        var books_id = user_info?.get(5)
                        books_id = if(books_id == " ") {
                            " $book_name"
                        } else {
                            "$books_id, $book_name"
                        }
                        var books_num = user_info?.get(6)!!.toInt()
                        books_num += 1
                        var position = user_info.get(7)
                        user_info[5] = books_id
                        user_info[6] = books_num.toString()
                        users.replace(user_id,user_info)
                        db.update_info(user_id,books_id, books_num, position.toInt())
                    }
                    else
                    {
                        //   println("NO {${users.get(user_id)}")
                        get_name(user_id)
                        //registration(user_id,message,first_name,last_name,user_username,univer_id.toInt(),0,book_name.toString(),1,0)
                    }
                }
                else {
                    println("THERE")
                    if (books.containsKey(book_id)) {
                        var books_info = books.get(book_id)
                        println("books_info $books_info")
                        var univer_id = books_info?.get(1).toString()
                        var book_name = books_info?.get(0)
                        if (users.containsKey(user_id)) {
                            var user_info = users.get(user_id)
                            var books_id = user_info?.get(5)
                            books_id = if (books_id == " ") {
                                " $book_name"
                            } else {
                                "$books_id, $book_name"
                            }
                            var books_num = user_info?.get(6)!!.toInt()
                            books_num += 1
                            var position = user_info?.get(7)
                            user_info!!.set(5, books_id)
                            user_info.set(6, books_num.toString())
                            users.replace(user_id, user_info)
                            db.update_info(user_id, books_id, books_num!!.toInt(), position!!.toInt())
                        } else {
                            //   println("NO {${users.get(user_id)}")
                            get_name(user_id)
                            //registration(user_id,message,first_name,last_name,user_username,univer_id.toInt(),0,book_name.toString(),1,0)
                        }

                    }
                    else if(book_id.startsWith("FILE"))
                    {
                        val id = book_id.substring(4)
                        if (files_size.containsKey(id)) {
                            println("id = $id")
                            println("id = ${files_size[id]!![0][0]}")



                            val book_name = files_size[id]!![0][1]
                            println("THURE")
                            if (users.containsKey(user_id)) {
                                var user_info = users.get(user_id)
                                var books_id = user_info?.get(5)
                                books_id = if (books_id == " ") {
                                    " $book_name"
                                } else {
                                    "$books_id, $book_name"
                                }
                                var books_num = user_info?.get(6)!!.toInt()
                                books_num += 1
                                var position = user_info.get(7)
                                user_info[5] = books_id
                                user_info[6] = books_num.toString()
                                users.replace(user_id, user_info)
                                db.update_info(user_id, books_id, books_num, position.toInt())
                            } else {
                                //   println("NO {${users.get(user_id)}")
                                get_name(user_id)
                                //registration(user_id,message,first_name,last_name,user_username,univer_id.toInt(),0,book_name.toString(),1,0)
                            }

                        }
                    }
                    else {
                        if (users.containsKey(user_id)) {
                            sendMessage(user_id, "Книга не найдена")
                        } else {
                            //   println("NO {${users.get(user_id)}")
                            sendMessage(user_id, "Книга не найдена")
                            get_name(user_id)
                            //registration(user_id,message,first_name,last_name,user_username,univer_id.toInt(),0,book_name.toString(),1,0)
                        }
                    }
                }
            }
            else{
                if(!users.containsKey(user_id))
                {
                    get_name(user_id)
                }
            }
        }
        private fun recoverFiles(user_id: Long,fileId: String)
        {
            sendFile(user_id,fileId)

        }
        private fun getFiles(user_id: Long, file: Document)
        {
            var book_info = arrayListOf<String>()
            val id = file.fileId
            val size = file.fileSize
            val name = file.fileName
            book_info.add(name)
            book_info.add("${admin_univer_id[user_id]}")
            book_info.add("0")
            books.put(id,book_info)
           // db.write_book(id,book_info[0],book_info[1].toInt(),book_info[2])
            admin_add_book.put(user_id, id)
            file_ids.add(id)
            println("ID = $id   size = $size name  = $name")

        }

        private fun getNewFile(user_id: Long, file: Document)
        {
            val id = file.fileId
            val name =  file.fileName
            val teach_id = admin_add_file[user_id]
//            var array = addFile[user_id]
            var c = 0
            var univer = "0"
            while (c < teacher_size.size)
            {
                if(teacher_size[c][0].contains("$teach_id"))
                {
                    univer = teacher_size[c][2]
                }
                c++
            }
            if(addFile.containsKey(teach_id))
            {
                val array = addFile[teach_id]
                val arr = ArrayList<String>()
                arr.add(id)
                arr.add(name)
                arr.add(univer)
                val size = files_size.maxBy { it.key.toInt() }!!.key.toInt() +1
                arr.add(size.toString())
                array!!.add(arr)
                addFile.replace(teach_id!!,array)

                println("size11 = $size")
                files_size.put((size).toString(),arr)
                db.write_file(size, teach_id,name,id)
            }
            else{
                val array = ArrayList<ArrayList<String>>()
                val arr = ArrayList<String>()
                arr.add(id)
                arr.add(name)
                arr.add(univer)
                val size = files_size.maxBy { it.key.toInt() }!!.key.toInt() +1
                arr.add(size.toString())
                array.add(arr)
                addFile.put(teach_id!!,array)

                println("size11 = $size")
                files_size.put((size).toString(),arr)
                db.write_file(size, teach_id,name,id)

            }
        }
        fun add_admin(user_id: Long,message: String)
        {
            if(!message.equals("Готово")) {
                try {
                    admins.put(message.toLong(), 1)
                    db.update_role(message.toLong(), 1)
                    sendMessage(user_id, "```Added```")
                    sendMessage(message.toLong(), "``` Вы были повышены до ```" + "*Admin*.Нажмите /start ")
                } catch (e: TelegramApiException) {
                    e.printStackTrace()
                }
            }
        }
        fun add_super_admin(user_id: Long,message: String)
        {
            try{
                if(message!="Готово") {
                    admins.put(message.toLong(), 2)
                    db.update_role(message.toLong(), 2)
                    sendMessage(user_id, "```Added```")
                    sendMessage(message.toLong(), "``` Вы были повышены до ```" + "*Super Admin*.Нажмите /start ")
                }
            }
            catch (e:TelegramApiException)
            {
                e.printStackTrace()
            }
        }
        private fun getPhoto(
            user_id:Long,
            photos: MutableList<PhotoSize>
        ): String
        {
            var index = photos.lastIndex

            return photos[index].fileId
        }

        private fun sendFile(user_id: Long,file_id: String)
        {
            val answer = SendDocument()
                .setChatId(user_id)
                .setDocument(file_id)

            try{
                execute(answer)
            }
            catch (err:TelegramApiException)
            {
                err.printStackTrace()
            }
        }

        private fun buttongroup1(user_id: Long,message: String)
        {
            when(message)
            {
                "Add Univer"->
                {
                    //add_univer(user_id, message)
                    users_pos.replace(user_id,8)
                    db.update_position(user_id,8)
                    btn_done(user_id)
                    sendMessage(user_id,"``` Add univer name```")
                    sendMessage(user_id,"$universities ")

                }
                "Add book"->
                {
                    btn_done(user_id)
                    users_pos.replace(user_id,12)
                    db.update_position(user_id,12)
                    var text = "    *Univer LIST*\n"

                    for(a in univerlist.keys) {

                        text += "``` ${univerlist.get(a)} - ${a}```\n"
                    }
                    sendMessage(user_id,text)
                    sendMessage(user_id,"` Enter Univer_id`")


                }
                "Add Lesson"->
                {
                    btn_done(user_id)
                    users_pos.replace(user_id,20)
                    db.update_position(user_id,20)
                    var text = "    *Univer LIST*\n"

                    for(a in univerlist.keys) {

                        text += "``` ${univerlist.get(a)} - ${a}```\n"
                    }
                    sendMessage(user_id,text)
                    sendMessage(user_id,"` Enter Univer_id`")

                }
                "Add Genre"->
                {
                    btn_done(user_id)
                    users_pos.replace(user_id,31)
                    db.update_position(user_id,31)
                    sendMessage(user_id,"` SEND GENRE`s NAME`")

                }
                "Add File"->
                {
                    btn_done(user_id)
                        var c = 1
                        var text = "GENREs\n"
                        for ( v in teacher[0]!!)
                        {
                            text += "$c - ${v[1]}\n"
                            c++
                        }
                        sendMessage(user_id,text)
                        users_pos.replace(user_id, 33)
                        db.update_position(user_id, 33)

                        sendMessage(user_id, "``` SEND GENRE's ID```")

                }
                "Список книг"->{
//                    println("aloha")
                    val text = " `Все книги: ${books.size}.` "
                    var count =  0
                    val array = ArrayList<String>()
                    for((v,k) in books){
                        for((i,j) in book_file)
                        {
                            if(j.contains(v))
                            {
                            count++
//                                text += " $count.[${k[0]}](t.me/Qrbookbot?start=${i})\n"
                            array.add(" $count.[${k[0]}](t.me/Qrbookbot?start=${i})\n")
                            }
                        }
//                        println("aloha  == $count")
                    }
                    sendMessage(user_id,text)
                    var j =0
                    var b =0
                    while (b<array.size){
                        var text1 = " "
                        j = b
                        while( (b - j< 50) and (b<array.size))
                        {
                            text1 += array[b]
                            b++
                        }

                        sendMessage(user_id, text1)
                    }
                }
                "Статистика"->
                {
                    var text = "`Книг: ${books.size}. Пользователей : ${users.size}. \n   Из них` \n"
                    for ((num,univer) in univerlist)
                    {
                        var per = 0
                        var count = 0
                        for((user,properties) in users)
                        {
                            if(properties[3].contains(num.toString()))
                                count++
                        }
                        //println("UC: $univer $count")
                        per = (count*100)/users.size
                        text += "``` $univer - ${per}% ($count)``` \n"
                    }
                    text +="*Моя статистика*"
                    text += "` \nСкачано вами : ${users[user_id]!![6]}  \nМой ВУЗ : ${univerlist[users[user_id]!![3].toInt()]} `"
                    println("TEXT $text")
                    sendMessage(user_id,text)
                    //println("univer ${users[user_id]!![3]}  ${universities[1]}")
                }
                "Архив"->
                {
                    var text = "\tВаш Архив:\n"

                    if(archived.containsKey(user_id)) {
                        val j = archived[user_id]
                        for (i in j!!) {

                            if(book_file.contains(i))
                            {
                                val file = book_file[i]
                                println("i = $i")
                                text = " $text [${books[file]!![0]}](t.me/Qrbookbot?start=$i)\n"
                            }
                            else if (i.startsWith("FILE"))
                            {
                                val id = i.substring(4)
                                text = " $text [${files_size[id]!![1]}](t.me/Qrbookbot?start=$i)\n"
                            }
                            else
                            {
                                text = " $text ['неизвестная книга'](t.me/Qrbookbot?start=$i)\n"
                            }



                        }
                       sendMessage(user_id, text)
                    }
                    else {
                        sendMessage(user_id,"```Пусто!```")
                    }
//                    users_pos.replace(user_id,4)
//                    db.update_position(user_id,4)
                }
                "Posting"->
                {
                    sendMessage(user_id,"``` Do a new post(Add Photo then text)```")
                    users_pos.replace(user_id,5)
                    db.update_position(user_id,5)
                    btn_done(user_id)
                }
                "Add admin"->
                {
                    sendMessage(user_id,"``` Enter User_id```")
                    users_pos.replace(user_id,6)
                    db.update_position(user_id,6)
                    btn_done(user_id)
                }
                "Add SuperAdmin"->
                {
                    sendMessage(user_id,"``` Enter User_id```")
                    users_pos.replace(user_id,7)
                    db.update_position(user_id,7)
                    btn_done(user_id)
                }
                "Univer Stats"->
                {
                    if(admins.containsKey(user_id))
                    {
                        univer_stats(user_id,message)
                    }
                }
                "Онлайн Уроки"->
                {
                    lessonButton(user_id)

                }
                "Жанры"->{
                    val univer = 0
                    val size = teacher[univer.toInt()]!!.size
                    var arrays = teacher[univer.toInt()]!!
                    var markupInline = InlineKeyboardMarkup()
                    val rowsInline = ArrayList<List<InlineKeyboardButton>>()
                    val rowInline = ArrayList<InlineKeyboardButton>()
//            var last = array.last()
                    var a = 0
                    var text = "*** Жанры\n ***"
                    var c = 1
                    var array = ArrayList<Int>()
                    for ( i in teacher[univer.toInt()]!!)
                    {
                        array.add(c)
                        text += "$c - ${i[1]}\n"
                        c++
                    }

                    for (id in array) {
//                           println("rowsize ${array.size}")
//                           sendMessage(user_id,text)
//                           users_pos.replace(user_id, 33)
//                           db.update_position(user_id, 33)

                        if (array.size - a >=5)
                        {
                            rowInline.add(InlineKeyboardButton().setText(id.toString()).setCallbackData("t$id"))
                            println("rowsize ${rowInline.size}")
                            println(rowInline.size.rem(5))
                            if((rowInline.size - a).rem(5) == 0)
                            {

                                rowsInline.add(arrayListOf(rowInline[id - 5],rowInline[id - 4],rowInline[id - 3],rowInline[id - 2],rowInline[id-1]))
                                a += 5
                                println("rowsize ${rowInline.size}")
                            }
                        }
                        else if (array.size - a <5)
                        {
                            println("rowsize ${rowInline.size}")
                            rowInline.add(InlineKeyboardButton().setText(id.toString()).setCallbackData("t$id"))
                            if(array.size - a == 4)
                            {
                                if((rowInline.size-a).rem(4) == 0)
                                {
                                    rowsInline.add(arrayListOf(rowInline[id - 4],rowInline[id - 3],rowInline[id - 2],rowInline[id-1]))
                                    a += 4
                                }
                            }
                            else if(array.size - a == 3)
                            {
                                if((rowInline.size-a).rem(3) == 0)
                                {
                                    rowsInline.add(arrayListOf(rowInline[id - 3],rowInline[id - 2],rowInline[id-1]))
                                    a += 3
                                }
                            }
                            else if(array.size - a == 2)
                            {
                                if((rowInline.size-a).rem(2) == 0)
                                {
                                    rowsInline.add(arrayListOf(rowInline[id - 2],rowInline[id-1]))
                                    a +=2
                                }
                            }
                            else if(array.size - a == 1)
                            {
                                rowsInline.add(listOf(rowInline[id-1]))
                            }
                        }
                    }
                    val message = SendMessage() // Create a message object object
                        .setChatId(user_id)
                        .setParseMode("Markdown")
                        .setText(text)

                    markupInline.keyboard = rowsInline
                    message.replyMarkup = markupInline
                    try {
                        execute(message) // Sending our message object to user
                    } catch (e: TelegramApiException) {
                        e.printStackTrace()
                    }
                }
                "Поиск"->
                {
                    sendMessage(user_id," `Введите название книги.`")
                    users_pos.replace(user_id,13)
                    db.update_position(user_id,13)
                    btn_done(user_id)
                }
                "Reload"->
                {
                    sendMessage(user_id,"Wait")
                    reload(user_id)
                    sendMessage(user_id,"Ready")
                }
            }
        }
        private fun buttongroup2(user_id: Long,message: String)
        {
            when(message)
            {
                "Готово"->
                {
                   // sendMessage(user_id,"Adding ended!")
                    if(admin_univer_id.containsKey(user_id))
                    {
                        admin_univer_id.remove(user_id)
                    }
                    if(admin_add_book.containsKey(user_id))
                    {
                        admin_add_book.remove(user_id)
                    }
                    if(admin_add_photo.containsKey(user_id))
                    {
                        admin_add_photo.remove(user_id)
                    }
                    maintenance(user_id)
                }
            }
        }
        private fun deleteMessage(user_id: Long,message_id: Int)
        {
            val message = DeleteMessage()
                .setChatId(user_id)
                .setMessageId(message_id)
            try {
                execute(message)
            }
            catch (e:TelegramApiException)
            {
                e.printStackTrace()
            }
        }
        private  fun isInteger(string: String): Boolean {
            return try {
                Integer.parseInt(string)
                true
            } catch (e: NumberFormatException) {
                false
            }
        }

        private fun log(
            first_name: String,
            user_username: String,
            user_id: String,
            txt: String
        ) {
            println("\n ----------------------------")
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val date = Date()
            println(dateFormat.format(date))
            println("Message from $first_name User_name - $user_username. (id = $user_id) \n Text - $txt ")
            //  println("Bot answer: \n Text - $answer")
        }
    }


