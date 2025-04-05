package com.example.todolist.repo

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.todolist.model.QuoteResponse
import com.google.gson.Gson
import org.json.JSONArray

class QuoteRepository(private val context: Context) {

    fun getQuote(callback: (String) -> Unit, onError: (String) -> Unit) {
        val url = "https://zenquotes.io/api/today"
        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    val jsonObject = jsonArray.getJSONObject(0)

                    val res = Gson().fromJson(jsonObject.toString(), QuoteResponse::class.java)
                    val fullQuote = "\"${res.q}\" — ${res.a}"
                    callback(fullQuote)
                } catch (e: Exception) {
                    onError("Erro no parse: ${e.message}")
                }
            },
            { error ->
                onError("Erro na requisição: ${error.message}")
            }
        )

        queue.add(stringRequest)
    }
}
