package com.larapin.footballapps.api

import java.net.URL

class repoApi{

    fun doRequest(url: String): String{
        return URL(url).readText()
    }
}