package com.gustavo.cursomeetups.di

import com.gustavo.cursomeetups.notifications.CanaisNotificacao
import com.gustavo.cursomeetups.preferences.FirebaseTokenPreferences
import com.gustavo.cursomeetups.repository.DispositivoRepository
import com.gustavo.cursomeetups.repository.EventoRepository
import com.gustavo.cursomeetups.ui.viewmodel.DetalhesEventoViewModel
import com.gustavo.cursomeetups.ui.viewmodel.EstadoAppViewModel
import com.gustavo.cursomeetups.ui.viewmodel.ListaEventoViewModel
import com.gustavo.cursomeetups.ui.viewmodel.ListaInscricoesViewModel
import com.gustavo.cursomeetups.webclient.DispositivoService
import com.gustavo.cursomeetups.webclient.EventoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val URL_BASE = "http://192.168.0.8:8080/api/"

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<EventoService> { get<Retrofit>().create(EventoService::class.java) }
    single<DispositivoService> { get<Retrofit>().create(DispositivoService::class.java) }
    single {
        val logging = HttpLoggingInterceptor().apply {
            this.level = Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}


val viewModelModule = module {
    viewModel { ListaEventoViewModel(get()) }
    viewModel { DetalhesEventoViewModel(get()) }
    viewModel { EstadoAppViewModel() }
    viewModel { ListaInscricoesViewModel(get()) }
}

val repositoryModule = module {
    single { EventoRepository(get()) }
    single { DispositivoRepository(get(), get()) }
}

val preferencesModule = module {
    single { FirebaseTokenPreferences(get()) }
}

val notificacaoModule = module {
    single { CanaisNotificacao(get()) }
    //single { get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
}

val appModules = listOf(
    retrofitModule,
    viewModelModule,
    repositoryModule,
    preferencesModule,
    notificacaoModule
)