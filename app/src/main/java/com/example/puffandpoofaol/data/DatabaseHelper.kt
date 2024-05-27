package com.example.puffandpoofaol.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.puffandpoofaol.adapters.TransactionAdapter

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private  const val DATABASE_NAME = "puffandpoof.db"
        private  const val DATABASE_VERSION = 1
    }

    //Create Database WITH ITs table
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTableQuery = "CREATE TABLE user ( userid INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL, telephonenumber TEXT NOT NULL, gender TEXT NOT NULL)"
        val createTransactionTableQuery = "CREATE TABLE transactions ( transactionid INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER NOT NULL, dollid INTEGER NOT NULL, transactiondate DATE, transactionamount INTEGER, dollname STRING, FOREIGN KEY (userid) REFERENCES user(userid), FOREIGN KEY (dollid) REFERENCES doll(dollid))"
        val createDollTableQuery = "CREATE TABLE doll (dollid INTEGER PRIMARY KEY, dollname TEXT NOT NULL, dollsize TEXT NOT NULL, dollrating REAL NOT NULL, dollprice INTEGER NOT NULL, dollimage TEXT NOT NULL, dolldescription TEXT NOT NULL)"
        db?.execSQL(createUserTableQuery)
        db?.execSQL(createTransactionTableQuery)
        db?.execSQL(createDollTableQuery)
    }

    //DANGER! REMOVE DATABASE AND CREATE A NEW DATABASE
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropUserTableQuery = "DROP TABLE IF EXISTS user"
        val dropTransactionTableQuery = "DROP TABLE IF EXISTS transactions"
        val dropDollTableQuery = "DROP TABLE IF EXISTS doll"
        db?.execSQL(dropUserTableQuery)
        db?.execSQL(dropTransactionTableQuery)
        db?.execSQL(dropDollTableQuery)
        onCreate(db)
    }

    //Insert New User
    fun insertUser(user:User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", user.userName)
            put("email", user.userEmail)
            put("password", user.userPassword)
            put("telephonenumber", user.userTelephoneNumber)
            put("gender", user.userGender)
        }
        db.insert("user",null, values) //Insert New Values
        db.close(); //Close database everytime it opens
    }

    //Insert Doll. This will insert all the data from the JSON to the database.
    fun insertDoll(doll: Doll) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("dollid", doll.id)
            put("dollname", doll.dollName)
            put("dollsize", doll.dollSize)
            put("dollrating", doll.dollRating)
            put("dollprice", doll.dollPrice)
            put("dollimage", doll.dollImageURL)
            put("dolldescription", doll.dollDesc)
        }
        db.insert("doll",null, values)
        db.close();
    }

    //Insert a new Transaction
    fun insertTransaction(userID:Int, dollID: Int, amount:Int, dateTime: String, dollName: String) {
        println("date: $dateTime")
        println("userID: $userID")
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userid", userID)
            put("dollid", dollID)
            put("transactionamount", amount)
            put("transactiondate", dateTime)
            put("dollname", dollName)
        }
        db.insert("transactions", null, values)
        db.close()
    }

    //Validate User Registration, check if name is taken or not.
    fun validateRegisterUsername(username:String):Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query("user", arrayOf("userid", "username", "email", "password", "telephonenumber", "gender"),
            "username = ?", arrayOf(username),
            null, null, null)
        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return false;
        }
        cursor.close()
        db.close()
        return true
    }

    //Validate User Login, check if login data is correct or not. If it is correct, then return the user data, else return null
    fun validateUserLogin(username:String, password:String):User?{
        val db = readableDatabase
        val cursor: Cursor = db.query("user", arrayOf("userid", "username", "email", "password", "telephonenumber", "gender"),
            "username = ? AND password = ?", arrayOf(username, password),
            null, null, null)
        if (cursor.moveToFirst()) {
            val user = User(
            cursor.getInt(cursor.getColumnIndexOrThrow("userid")),
            cursor.getString(cursor.getColumnIndexOrThrow("username")),
            cursor.getString(cursor.getColumnIndexOrThrow("email")),
            cursor.getString(cursor.getColumnIndexOrThrow("password")),
            cursor.getString(cursor.getColumnIndexOrThrow("telephonenumber")),
            cursor.getString(cursor.getColumnIndexOrThrow("gender"))
            )
            cursor.close()
            db.close()
            return user
        }
        cursor.close()
        db.close()
        return null
    }

    //Show all transaction from current logged user id.
    fun selectTransaction(userID: Int):ArrayList<Transaction> {
        val db = readableDatabase
        val transactionList:ArrayList<Transaction> = ArrayList()
        val query = "SELECT * FROM transactions WHERE userid = ? ORDER BY transactiondate DESC"
        val cursor = db.rawQuery(query, arrayOf(userID.toString()))
        while(cursor.moveToNext()) {
            val transactionId = cursor.getInt(cursor.getColumnIndexOrThrow("transactionid"))
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow("userid"))
            val dollId = cursor.getInt(cursor.getColumnIndexOrThrow("dollid"))
            val transactionAmount = cursor.getInt(cursor.getColumnIndexOrThrow("transactionamount"))
            val transactionDate = cursor.getString(cursor.getColumnIndexOrThrow("transactiondate"))
            val dollName = cursor.getString(cursor.getColumnIndexOrThrow("dollname"))
            transactionList.add(Transaction(transactionId, userId, dollId, transactionAmount, transactionDate, dollName))
        }
        return transactionList
    }

    //Show all dolls from the database.
    fun selectAllDoll():ArrayList<Doll>{
        val db = readableDatabase
        val dollList:ArrayList<Doll> = ArrayList()
        val query = "SELECT * FROM doll"
        val cursor = db.rawQuery(query, null)
        while(cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("dollid"))
            val dollName = cursor.getString(cursor.getColumnIndexOrThrow("dollname"))
            val dollSize = cursor.getString(cursor.getColumnIndexOrThrow("dollsize"))
            val dollRating = cursor.getDouble(cursor.getColumnIndexOrThrow("dollrating"))
            val dollPrice = cursor.getInt(cursor.getColumnIndexOrThrow("dollprice"))
            val dollImage = cursor.getString(cursor.getColumnIndexOrThrow("dollimage"))
            val dollDescription = cursor.getString(cursor.getColumnIndexOrThrow("dolldescription"))
            val newDoll = Doll(id, dollName, dollDescription, dollSize, dollPrice, dollRating, dollImage);
            dollList.add(newDoll)
        }
        return dollList
    }

    //Update the transaction based on the changes.
    fun updateTransaction(transaction: Transaction) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userid", transaction.userID)
            put("dollid", transaction.dollID)
            put("transactionamount", transaction.transactionAmount)
            put("transactiondate", transaction.transactionDate)
            put("dollname", transaction.dollName)
        }
        val whereClause = "transactionid = ?"
        val whereArgs = arrayOf(transaction.transactionID.toString())
        db.update("transactions", values, whereClause, whereArgs)
        db.close()
    }

    //Delete Transaction.
    fun deleteTransaction(transactionID:Int) {
        val db = writableDatabase
        val whereClause = "transactionid = ?"
        val whereArgs = arrayOf(transactionID.toString())
        db.delete("transactions", whereClause, whereArgs)
        db.close()
    }
}