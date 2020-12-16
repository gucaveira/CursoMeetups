package com.cursomeetups.di

import com.cursomeetups.repository.EventoRepository
import com.cursomeetups.ui.viewmodel.DetalhesEventoViewModel
import com.cursomeetups.ui.viewmodel.EstadoAppViewModel
import com.cursomeetups.ui.viewmodel.ListaEventoViewModel
import com.cursomeetups.ui.viewmodel.ListaInscricoesViewModel
import com.cursomeetups.webclient.EventoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private val URL_BASE = "endereco_api"


val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<EventoService> { get<Retrofit>().create(EventoService::class.java) }

    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}

val viewModelModule = module {
    viewModel<ListaEventoViewModel> { ListaEventoViewModel(get()) }
    viewModel<DetalhesEventoViewModel> { DetalhesEventoViewModel(get()) }
    viewModel<EstadoAppViewModel> { EstadoAppViewModel() }
    viewModel<ListaInscricoesViewModel> { ListaInscricoesViewModel(get()) }
}

val repositoryModule = module {
    single { EventoRepository(get()) }
}

val appModules = listOf(
    retrofitModule,
    viewModelModule,
    repositoryModule,
)