package com.gustavo.cursomeetups.webclient

import com.gustavo.cursomeetups.model.Evento
import com.gustavo.cursomeetups.model.Usuario
import retrofit2.http.*

interface EventoService {

    @GET("events")
    suspend fun buscaTodos(): List<Evento>

    @GET("events/{id}/subscribed")
    suspend fun buscaPorId(
        @Path("id") id: String,
        @Query("email") email: String?,
    ): Evento

    @POST("events/{id}/subscribe")
    suspend fun inscreve(
        @Path("id") eventoId: String,
        @Body usuario: Usuario,
    )

    @PUT("events/{id}/unsubscribe")
    suspend fun cancela(
        @Path("id") eventId: String,
        @Body usuario: Usuario,
    )

    @GET("events/subscriptions")
    suspend fun buscaInscricoes(@Query("email") email: String?): List<Evento>
}
