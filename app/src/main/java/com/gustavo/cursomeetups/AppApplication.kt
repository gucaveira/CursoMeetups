package com.gustavo.cursomeetups

import android.app.Application
import com.gustavo.cursomeetups.di.appModules
import com.gustavo.cursomeetups.notifications.CanaisNotificacao
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(appModules)
        }
        val canais: CanaisNotificacao by inject()
        canais.criaCanal(CanaisNotificacao.Canal.PRINCIPAL)
        canais.criaCanal(CanaisNotificacao.Canal.SECUNDARIO)
    }

}