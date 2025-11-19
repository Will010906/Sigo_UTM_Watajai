package com.example.sigo_utm_watajai.data.remote

import com.example.sigo_utm_watajai.model.LoginRequest
import com.example.sigo_utm_watajai.model.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de servicio de la API (ApiService).
 * Utiliza la librería Retrofit para definir las peticiones HTTP que la aplicación puede realizar.
 * Cada función en esta interfaz representa un endpoint específico del servidor.
 */
interface ApiService {

    /**
     * Define la llamada para iniciar sesión (autenticación) de un usuario.
     * * @POST Especifica que es una petición POST y define el endpoint relativo (ws/rest/auth).
     * @param request El cuerpo de la petición. El objeto LoginRequest se serializará a JSON y se enviará en el cuerpo del request.
     * @return Response<LoginResponse> Un objeto de Retrofit que contiene la respuesta HTTP del servidor.
     * * La palabra clave 'suspend' indica que esta es una función Coroutine, lo que permite que la llamada
     * de red se ejecute de forma asíncrona sin bloquear el hilo principal (UI).
     */
    @POST("ws/rest/auth")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}