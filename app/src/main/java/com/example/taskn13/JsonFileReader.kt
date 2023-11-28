package com.example.taskn13

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class JsonFileReader {
    fun loadJSONFromAsset(context: Context, filename: String): String? {
        val file = context.assets.open(filename)
        val bufferedReader = BufferedReader(InputStreamReader(file))

        try {
            val stringBuilder = StringBuilder()
            bufferedReader.useLines {
                it.forEach {line ->
                    stringBuilder.append(line)
                }
            }
            return stringBuilder.toString()
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            bufferedReader.close()
            file.close()
        }
        return null
    }
}