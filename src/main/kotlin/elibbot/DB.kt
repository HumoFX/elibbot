package elibbot

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import kotlin.system.exitProcess

//const val connect = "jdbc:postgresql://localhost:5432/postgres"
const val connect = "jdbc:postgresql://drona.db.elephantsql.com:5432/seallheo"
//const val user = "postgres"
const val user = "seallheo"
//const val pass = "123"
const val pass = "zc2-JGbg8Q9LClrrCkFcKxJ9rS8UDXGY"
open class DB {
    open fun CREATE() {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            println("Database opened successfully")
            stmt = c!!.createStatement()
            val sql = "CREATE TABLE LESSONS" +
                    "(ID               SERIAL PRIMARY KEY,"   +
                    " Univer_id            INT    NOT NULL,"  +
                    " NAME                 TEXT   NOT NULL,"  +
                    " TEXT              VARCHAR   NOT NULL)"
            stmt!!.executeUpdate(sql)
            stmt.close()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            exitProcess(0)
        }

        println("Table created successfully")
    }

    open fun write(
        user_id: Long,
        firstname: String,
        lastname:String,
        username:String,
        univer_id: Int,
        role:Int,
        books_id: String,
        books_num: Int,
        position: Int) {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false


            stmt = c.createStatement()
            var sql: String = "INSERT INTO USERS (ID,FIRSTNAME,LASTNAME,USERNAME,UNIVERSITY_ID,ROLE,BOOKS_ID,BOOKS_NUM,POSITION) VALUES ( '$user_id','$firstname','$lastname','$username',$univer_id,$role,'$books_id',$books_num,$position);"
            stmt.executeUpdate(sql)

            stmt.close()
            c.commit()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

    }
    open fun write_lesson(univer:Int, name: String,text: String) {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false


            stmt = c.createStatement()
            var sql: String = "INSERT INTO LESSONS (Univer_id,NAME,TEXT) VALUES ('$univer','$name','$text');"
            stmt.executeUpdate(sql)

            stmt.close()
            c.commit()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

    }


    open fun write_univer(name:String)
    {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false


            stmt = c.createStatement()
            var sql: String = "INSERT INTO UNIVERSITIES (NAME ) VALUES ('$name');"
            stmt.executeUpdate(sql)

            stmt.close()
            c.commit()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }
    }

    open fun write_book(id:Int,file_id:String,name:String,category_id:Int,photo_id:String)
    {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false


            stmt = c.createStatement()
            var sql: String = "INSERT INTO BOOKS (ID,FILE_ID,NAME,CATEGORY_ID,PHOTO_ID ) VALUES ('$id','$file_id','$name',$category_id,'$photo_id');"
            stmt.executeUpdate(sql)

            stmt.close()
            c.commit()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }
    }

    open fun read(): Array<Array<String>> {
        var c: Connection? = null
        var stmt: Statement? = null
        var i =0
        var test2 =0
        var test=read1(i)
        if(test ==0)
        {
            test2 = 1
        }
        else {
            test2 = test
        }
        var temp: Array<Array<String>> = Array(test2, { Array(3, {"0"}) })

        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false


            stmt = c.run { createStatement() }
            val rs = stmt!!.executeQuery("SELECT * FROM UNIVERSITIES ORDER BY id;")

            var t = 0

            while (rs.next()) {
                val id = rs.getInt("id")
                val name = rs.getString("name")
                temp[t][0] = id.toString()
                temp[t][1] = name
                t++
            }

            rs.close()
            stmt.close()
            c.close()

            temp[0][2]=test.toString()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            //System.exit(0)
        }

        println("Operation READ done successfully")
        return temp
    }
    fun read1(i:Int): Int {
        var c: Connection? = null
        var stmt: Statement? = null
        var test =i
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false

            stmt = c.createStatement()
            val rs = stmt!!.executeQuery("SELECT * FROM UNIVERSITIES ORDER BY id;")
            while (rs.next()) {
                test++

            }


            rs.close()
            stmt.close()
            c.close()

        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            //System.exit(0)
        }
        return test
    }

    open fun read_book(): Array<Array<String>> {
        var c: Connection? = null
        var stmt: Statement? = null
        var i =0
        var test2 =0
        var test=read1_book(i)
        if(test ==0)
        {
            test2 = 1
        }
        else {
            test2 = test
        }
        var temp: Array<Array<String>> = Array(test2, { Array(6, {"0"}) })

        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false


            stmt = c.run { createStatement() }
            val rs = stmt!!.executeQuery("SELECT * FROM BOOKS order by id;")

            var t = 0

            while (rs.next()) {
                val id = rs.getInt("id")
                val file_id = rs.getString("file_id")
                val name = rs.getString("name")
                val cat_id = rs.getInt("category_id")
                val photo_id = rs.getString("photo_id")
                temp[t][0] = id.toString()
                temp[t][1] = name
                temp[t][2] = cat_id.toString()
                temp[t][3] = photo_id
                temp[t][4] = file_id
                t++
            }

            rs.close()
            stmt.close()
            c.close()

            temp[0][5]=test.toString()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            //System.exit(0)
        }

        println("Operation READ done successfully")
        return temp
    }
    fun read1_book(i:Int): Int {
        var c: Connection? = null
        var stmt: Statement? = null
        var test =i
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false

            stmt = c.createStatement()
            val rs = stmt!!.executeQuery("SELECT * FROM BOOKS order by id;")
            while (rs.next()) {
                test++

            }


            rs.close()
            stmt.close()
            c.close()

        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            //System.exit(0)
        }
        return test
    }

    open fun read_user(): Array<Array<String>> {
        var c: Connection? = null
        var stmt: Statement? = null
        var i =0
        var test2 =0
        var test=read1_user(i)
        if(test ==0)
        {
            test2 = 1
        }
        else {
            test2 = test
        }
        var temp: Array<Array<String>> = Array(test2, { Array(11, {"0"}) })

        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false


            stmt = c.run { createStatement() }
            val rs = stmt!!.executeQuery("SELECT * FROM USERS;")

            var t = 0
            while (rs.next()) {
                val id = rs.getInt("id")
                val firstname = rs.getString("firstname")
                val lastname = rs.getString("lastname")
                val username = rs.getString("username")
                val univer_id = rs.getInt("university_id")
                val role = rs.getInt("role")
                val books_id = rs.getString("books_id")
                val books_num = rs.getInt("books_num")
                val position = rs.getInt("position")
                val archive = rs.getString("archived")
                temp[t][0] = id.toString()
                temp[t][1] = firstname
                temp[t][2] = lastname
                temp[t][3] = username
                temp[t][4] = univer_id.toString()
                temp[t][5] = role.toString()
                temp[t][6] = books_id
                temp[t][7] = books_num.toString()
                temp[t][8] = position.toString()
                temp[t][9] = archive
                t++
            }

            rs.close()
            stmt.close()
            c.close()

            temp[0][10]=test.toString()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            //System.exit(0)
        }

        println("Operation READ done successfully")
        return temp
    }
    fun read1_user(i:Int): Int {
        var c: Connection? = null
        var stmt: Statement? = null
        var test =i
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false

            stmt = c.createStatement()
            val rs = stmt!!.executeQuery("SELECT * FROM USERS;")
            while (rs.next()) {
                test++

            }


            rs.close()
            stmt.close()
            c.close()

        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            //System.exit(0)
        }
        return test
    }

    open fun read_user_info(message:Int): Array<Array<String>> {
        var c: Connection? = null
        var stmt: Statement? = null
        var i =0
        var test2 =0
        var test=read1_user_info(i,message)
        if(test ==0)
        {
            test2 = 1
        }
        else {
            test2 = test
        }
        var temp: Array<Array<String>> = Array(test2, { Array(7, {"0"}) })

        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false


            stmt = c.run { createStatement() }
            val rs = stmt!!.executeQuery("SELECT * FROM USERS where university_id = $message;")

            var t = 0

            while (rs.next()) {

                val firstname = rs.getString("firstname")
                val lastname = rs.getString("lastname")
                val username = rs.getString("username")
                val group = rs.getString("groups")
                val books_id = rs.getString("books_id")
                val books_num = rs.getInt("books_num")

                temp[t][0] = firstname
                temp[t][1] = lastname
                temp[t][2] = username
                temp[t][3] = group
                temp[t][4] = books_id
                temp[t][5] = books_num.toString()
                t++
            }

            rs.close()
            stmt.close()
            c.close()

            temp[0][6]=test.toString()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

        println("Operation READ done successfully")
        return temp
    }
    fun read1_user_info(i:Int, message: Int): Int {
        var c: Connection? = null
        var stmt: Statement? = null
        var test =i
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false

            stmt = c.createStatement()
            val rs = stmt!!.executeQuery("SELECT * FROM USERS where university_id = $message;")
            while (rs.next()) {
                test++

            }


            rs.close()
            stmt.close()
            c.close()

        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }
        return test
    }

    open fun read_lesson(message:String): Array<Array<String>> {
        var c: Connection? = null
        var stmt: Statement? = null
        var i =0
        var test2 =0
        var test=read1_lesson(i,message)
        if(test ==0)
        {
            test2 = 1
        }
        else {
            test2 = test
        }
        var temp: Array<Array<String>> = Array(test2, { Array(4, {"0"}) })

        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false


            stmt = c.run { createStatement() }
            val rs = stmt!!.executeQuery("SELECT * FROM lessons where univer_id=$message;")

            var t = 0

            while (rs.next()) {
                val univer = rs.getInt("univer_id")
                val name = rs.getString("name")
                val text = rs.getString("text")
                temp[t][0] = univer.toString()
                temp[t][1] = name
                temp[t][2] = text

                t++
            }

            rs.close()
            stmt.close()
            c.close()

            temp[0][3]=test.toString()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

        println("Operation READ done successfully")
        return temp
    }
    fun read1_lesson(i:Int, message: String): Int {
        var c: Connection? = null
        var stmt: Statement? = null
        var test =i
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false

            stmt = c.createStatement()
            val rs = stmt!!.executeQuery("SELECT * FROM lessons where univer_id=$message;")
            while (rs.next()) {
                test++

            }
            rs.close()
            stmt.close()
            c.close()

        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }
        return test
    }


    open fun update_info(user_id: Long, books_id: String, books_num: Int, position: Int) {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false
            println("Opened database successfully")

            stmt = c.createStatement()
            val sql = "UPDATE USERS set books_id = '$books_id', books_num = $books_num, position = $position where ID=$user_id;"
            stmt!!.executeUpdate(sql)
            c.commit()
            stmt.close()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

        println("Operation Update done successfully")
    }

    open fun update_role(user_id: Long, role:Int) {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false
            println("Opened database successfully")

            stmt = c.createStatement()
            val sql = "UPDATE USERS set role = $role where id = $user_id;"
            stmt!!.executeUpdate(sql)
            c.commit()
            stmt.close()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

        println("Operation Update done successfully")
    }

    open fun update_archive(user_id: Long, archive:String) {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false
            println("Opened database successfully")

            stmt = c.createStatement()
            val sql = "UPDATE USERS set archived = '$archive' where id = $user_id;"
            stmt!!.executeUpdate(sql)
            c.commit()
            stmt.close()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

        println("Operation Update done successfully")
    }


    open fun update_univer(user_id: Long, univer_id: Int) {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false
            println("Opened database successfully")

            stmt = c.createStatement()
            val sql = "UPDATE USERS set university_id = $univer_id where ID=$user_id;"
            stmt!!.executeUpdate(sql)
            c.commit()
            stmt.close()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

        println("Operation Update done successfully")
    }
    open fun update_group(user_id: Long, group: String) {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false
            println("Opened database successfully")

            stmt = c.createStatement()
            val sql = "update users set groups = '$group' where id = $user_id "
            stmt!!.executeUpdate(sql)
            c.commit()
            stmt.close()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

        println("Operation Update done successfully")
    }
    open fun update_position(user_id: Long, position: Int) {
        var c: Connection? = null
        var stmt: Statement? = null
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager
                .getConnection(connect, user, pass)
            c!!.autoCommit = false
            println("Opened database successfully")

            stmt = c.createStatement()
            val sql = "update users set position = $position where id = $user_id "
            stmt!!.executeUpdate(sql)
            c.commit()
            stmt.close()
            c.close()
        } catch (e: Exception) {
            System.err.println(e.javaClass.name + ": " + e.message)
            System.exit(0)
        }

        println("Operation Update done successfully")
    }




}