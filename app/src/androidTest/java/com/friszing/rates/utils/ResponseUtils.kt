package com.friszing.rates.utils

import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ResponseUtils {

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun <T> loadJson(path: String, clazz: Class<T>): T? {
        return try {
            val json = getFileString(path)
            moshi.adapter(clazz).fromJson(json)
        } catch (e: IOException) {
            throw IllegalArgumentException("Could not deserialize: $path into class: $clazz")
        }
    }

    private fun getFileString(path: String): String {
        return try {
            val reader = BufferedReader(
                InputStreamReader(
                    this.javaClass.classLoader?.getResourceAsStream(path)
                )
            )
            reader.useLines { lines -> lines.joinToString(separator = "") }
        } catch (e: IOException) {
            throw IllegalArgumentException("Could not read from resource at: $path")
        }
    }
}
