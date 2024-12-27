package ro.pub.cs.systems.eim.practicaltest02v2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


data class DictionaryResponse(
    val word: String,
    val phonetics: List<Phonetic>,
    val meanings: List<Meaning>,
    val license: License?,
    val sourceUrls: List<String>
)

data class Phonetic(
    val text: String?,
    val audio: String?,
    val sourceUrl: String?,
    val license: License?
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>,
    val synonyms: List<String>,
    val antonyms: List<String>
)

data class Definition(
    val definition: String,
    val example: String?,
    val synonyms: List<String>,
    val antonyms: List<String>
)

data class License(
    val name: String,
    val url: String
)

object RetrofitInstance {
    private const val BASE_URL = "https://api.dictionaryapi.dev/"

    val api: DictionaryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }
}

interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    fun getDefinition(@Path("word") word: String): Call<List<DictionaryResponse>>
}

class MainActivity : AppCompatActivity() {
    private lateinit var wordInputText : EditText
    private lateinit var getDefinitionButton: Button
    private lateinit var definitionView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test02v2_main)
        wordInputText = findViewById(R.id.editTextText)
        getDefinitionButton = findViewById(R.id.button)
        definitionView = findViewById(R.id.textView2)

        getDefinitionButton.setOnClickListener {
            val word = wordInputText.text.toString()

            if (word.isNotEmpty()) {
                fetchWordDefinition(word)
            } else {
                Toast.makeText(this, "Introdu un cuvânt!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun fetchWordDefinition(word: String) {
        RetrofitInstance.api.getDefinition(word).enqueue(object : Callback<List<DictionaryResponse>> {
            override fun onResponse(
                call: Call<List<DictionaryResponse>>,
                response: Response<List<DictionaryResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val wordResponse = response.body()
                    val definition = wordResponse?.get(0)?.meanings?.get(0)?.definitions?.get(0)?.definition.toString()

                    Log.d("PracticalTest02v2", wordResponse.toString())
                    Log.d("PracticalTest02v2", definition)

                    definitionView.text = definition

                } else {
                    definitionView.text = "Nu s-a găsit definiția!"
                }
            }

            override fun onFailure(call: Call<List<DictionaryResponse>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Eroare: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}