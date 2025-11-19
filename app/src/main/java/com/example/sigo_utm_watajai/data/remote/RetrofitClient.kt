package com.example.sigo_utm_watajai.data.remote

import com.example.sigo_utm_watajai.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto singleton (RetrofitClient) para crear y configurar una instancia única de Retrofit.
 * El uso de 'object' garantiza que solo exista una instancia de este cliente de red en toda la aplicación.
 */
object RetrofitClient {
    // URL base de tu servidor. Es esencial que termine en una barra inclinada (/) para evitar errores.
    private const val BASE_URL = "http://189.206.96.198:8080/"

    /**
     * Instancia de Retrofit configurada con la URL base y un convertidor de Gson.
     * La creación es diferida (lazy) para que se inicialice solo cuando se accede por primera vez.
     */
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Establece la URL base del servidor.
            .addConverterFactory(GsonConverterFactory.create()) // Añade el convertidor JSON (Gson) para serializar/deserializar objetos.
            .build() // Construye la instancia de Retrofit.
    }

    /**
     * Implementación de la interfaz [ApiService] para realizar las llamadas a la API.
     * Es el punto de acceso que las Activities/ViewModels utilizan para hacer peticiones (ej. loginUser).
     * La creación es diferida (lazy) para que se inicialice solo cuando se accede por primera vez.
     */
    val apiService: ApiService by lazy {
        // Retrofit genera automáticamente la implementación de la interfaz ApiService.
        retrofit.create(ApiService::class.java)
    }
}