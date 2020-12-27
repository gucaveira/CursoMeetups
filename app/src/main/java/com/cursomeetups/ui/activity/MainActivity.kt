package com.cursomeetups.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cursomeetups.R
import com.cursomeetups.model.Dispositivo
import com.cursomeetups.preferences.FirebaseTokenPreferences
import com.cursomeetups.repository.DispositivoRepository
import com.cursomeetups.ui.viewmodel.ComponentesVisuais
import com.cursomeetups.ui.viewmodel.EstadoAppViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: EstadoAppViewModel by viewModel()
    private val controller by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(activity_main_toolbar)
        configuraEstadoInicialDosComponentes()
        configuraNavigation()
    }

    override fun onResume() {
        super.onResume()
        tentaReenviarToken()
    }

    private fun tentaReenviarToken() {
        val preferences: FirebaseTokenPreferences by inject()
        if (!preferences.enviado) {
            val repository: DispositivoRepository by inject()
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                repository.salva(Dispositivo(token = token))
            }
        }
    }

    private fun configuraNavigation() {
        controller.addOnDestinationChangedListener { _, destination, _ ->
            title = destination.label
            viewModel.componentes.observe(this) {
                it?.let { temComponentes ->
                    configuraEstadoAppBar(temComponentes)
                    configuraEstadoBottomNavigation(temComponentes)
                }
            }
        }
        main_activity_bottom_navigation.setupWithNavController(controller)
    }

    private fun configuraEstadoBottomNavigation(temComponentes: ComponentesVisuais) {
        if (temComponentes.bottomNavigation) {
            main_activity_bottom_navigation.visibility = View.VISIBLE
        } else {
            main_activity_bottom_navigation.visibility = View.GONE
        }
    }

    private fun configuraEstadoAppBar(temComponentes: ComponentesVisuais) {
        if (temComponentes.appBar) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    private fun configuraEstadoInicialDosComponentes() {
        supportActionBar?.hide()
    }
}