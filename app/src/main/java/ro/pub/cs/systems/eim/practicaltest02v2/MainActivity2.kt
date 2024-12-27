package ro.pub.cs.systems.eim.practicaltest02v2

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class MainActivity2 : AppCompatActivity() {

    private lateinit var timeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeTextView = findViewById(R.id.dateAndTime)

        // Pornire thread pentru conectare la server
        Thread {
            connectToServer()
        }.start()
    }

    private fun connectToServer() {
        val host = "10.0.2.2" // Adresa IP a serverului (înlocuiește cu cea corectă)
        val port = 12345           // Portul serverului

        try {
            val socket = Socket(host, port)
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            Log.d("Error", "aici intru")

            while (true) {
                val serverMessage = reader.readLine() // Citește mesajul trimis de server
                Log.d("Error", serverMessage)
                if (serverMessage != null) {
                    runOnUiThread {
                        timeTextView.text = serverMessage // Actualizează TextView-ul
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Eroare: ${e.message}", Toast.LENGTH_LONG).show()
                Log.d("Error", "Eroare: ${e.message}")
            }
        }
    }
}