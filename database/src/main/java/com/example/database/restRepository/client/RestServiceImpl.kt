package com.example.database.restRepository.client

import com.example.database.restRepository.WebService

class RestServiceImpl(private val service : WebClient) : RestService {

    override fun provideWebService(): WebService = service.getClient().create(WebService::class.java)
}