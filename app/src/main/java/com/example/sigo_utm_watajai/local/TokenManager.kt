package com.example.sigo_utm_watajai.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extensión de Kotlin que crea la instancia de DataStore a nivel de Context.
 * Esto define el archivo de almacenamiento de preferencias bajo el nombre "auth_prefs".
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

/**
 * Gestiona el almacenamiento y recuperación del token de autenticación utilizando DataStore Preferences.
 * Esta clase es crucial para mantener la sesión del usuario tras un login exitoso.
 *
 * @param context Contexto de la aplicación o actividad, necesario para acceder a DataStore.
 */
class TokenManager(private val context: Context) {

    // Define la clave PreferenceKey para el token de acceso.
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")

    /**
     * Guarda el token de acceso (Bearer Token) en DataStore.
     * * @param token La cadena del token a almacenar.
     * La función es 'suspend' porque la operación de escritura en DataStore es asíncrona.
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            // Escribe el valor del token usando la clave definida.
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    /**
     * Recupera el token de acceso como un flujo (Flow).
     * El uso de Flow permite que cualquier observador (ej. un Interceptor de Retrofit)
     * reaccione a los cambios del token de forma asíncrona.
     * * @return Flow<String?> Un flujo que emite el valor del token (o null si no existe).
     */
    fun getToken(): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                // Lee el valor del token usando la clave definida.
                preferences[ACCESS_TOKEN_KEY]
            }
    }

    /**
     * Elimina el token de acceso de DataStore.
     * Se usa típicamente para implementar la funcionalidad de "Cerrar Sesión" (Logout).
     * La función es 'suspend' porque la operación de escritura en DataStore es asíncrona.
     */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            // Remueve la clave y su valor asociado.
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }
}