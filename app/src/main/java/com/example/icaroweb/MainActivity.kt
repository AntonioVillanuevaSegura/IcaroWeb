package com.example.icaroweb

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.icaroweb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val URL_BASE = "https://google.com" //url de base para la busqueda
    private val SEARCH_PATH="/search?q=" //https://google.com/search?q= BUSCA Busquedas en Google

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setContentView(R.layout.activity_main)//sistema antiguo binding

        //Binding widgets
        var buscar :SearchView =binding.searchView1
        var resultados : WebView =binding.webView1

        //Busqueda
        buscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                //Sugerencia busquedas anteriores
                return false //No quiero sugerencia
            }

            //Momento que pulso la lupa de busqueda en mi searchView buscar
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //Accedo al texto que ha introducido el usuario
                //Si no es nullo accedo al bloque
                p0?.let {
                    //Es una direccion URL valida?
                    if (URLUtil.isValidUrl(it )) { //it corresponde al texto p0 desenpaquetado
                        //Es una URL valida
                        resultados.loadUrl(it) //it corresponde al texto p0 desenpaquetado
                    }else{
                        //No es una URL es algo a buscar
                        resultados.loadUrl("$URL_BASE$SEARCH_PATH$it")
                    }
                }
                return false //Control comportamiento
            }
        })

        //Refresco , reload android swipeRefresh
        var swipeRefresh :SwipeRefreshLayout=binding.swipeRefresh

        //Refresca pagina web actual
        swipeRefresh.setOnRefreshListener {
            resultados.reload()
        }

        resultados.webChromeClient=object : WebChromeClient(){

        }

        resultados.webViewClient=object :WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                //return super.shouldOverrideUrlLoading(view, request)
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                //buscar.setQuery(url,false) //Evita desencadenar una busqueda solo la muestra en la barra de google
                swipeRefresh.isRefreshing=true //Aparece circulo flecha refresco
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing=false //Desaparece circulo flecha refresco pagina
            }

        }

        //Settings
        val settings:WebSettings = resultados.settings
        resultados.settings.javaScriptEnabled=true

        resultados.loadUrl(URL_BASE)


    }

    override fun onBackPressed() { //Vuelta atras <-
        if (binding.webView1.canGoBack()) {
            binding.webView1.goBack()
        } else {
            super.onBackPressed()
        }
    }

}