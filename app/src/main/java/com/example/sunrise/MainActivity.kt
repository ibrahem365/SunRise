package com.example.sunrise

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getSunRise(view: View) {

        val city = ed_city.text.toString()
        val url ="https://weather-ydn-yql.media.yahoo.com/forecastrss?location=$city"
        MYAsyncTask().execute(url)
    }

    inner class MYAsyncTask:AsyncTask<String,String,String>(){
        override fun onPreExecute() {

            //Before doInBackground
        }

        override fun doInBackground(vararg params: String?): String {
            //don't access to ui
            try {
                //get url from his position
                val url = URL(params[0])
                //connect to url by http
                val urlConnect = url.openConnection() as HttpURLConnection
                // if process not execute in 700 he will out
                urlConnect.connectTimeout=700

                //data return from process
                //data return in stream type an i will convert it to string
                val dataReturnFromProcess = convertStreamToString(urlConnect.inputStream)

                //use data to show it in ui
                publishProgress(dataReturnFromProcess)
                //This method send data to fun onProgressUpdate to show it in UI
            }catch (ex:Exception){}
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            //access to ui
            //data received in String and i will convert it to JSON object
            val json = JSONObject(values[0])
            val curntObs = json.getJSONObject("current_observation")
            val astronomy = curntObs.getJSONObject("astronomy")
            val sunrise = astronomy.getString("sunrise")

            txt_sun.text= "Time is "+sunrise
        }

        override fun onPostExecute(result: String?) {

            //When done process
        }

    }

    private fun convertStreamToString(inputStream : InputStream):String{
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String
        var allString:String=""

        do {
            line= bufferReader.readLine()
            if (line!=null)
            allString += line
        }while (line!=null)

        return allString
    }
}