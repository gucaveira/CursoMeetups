package com.gustavo.cursomeetups.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.gustavo.cursomeetups.R
import com.gustavo.cursomeetups.databinding.ActivityMainBinding
import com.gustavo.cursomeetups.model.Dispositivo
import com.gustavo.cursomeetups.preferences.FirebaseTokenPreferences
import com.gustavo.cursomeetups.repository.DispositivoRepository
import com.gustavo.cursomeetups.ui.viewmodel.ComponentesVisuais
import com.gustavo.cursomeetups.ui.viewmodel.EstadoAppViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: EstadoAppViewModel by viewModel()
    private val controlller by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        configuraEstadoInicialDosComponentes()
        configuraNavigation()
    }

    override fun onResume() {
        super.onResume()
        tentaReenviarToken()
    }

    private fun tentaReenviarToken() {
        val preferences: FirebaseTokenPreferences by inject()
        if (preferences.enviado.not()) {
            val repository: DispositivoRepository by inject()
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                repository.salva(Dispositivo(token = token))
            }
        }
    }

    private fun configuraNavigation() {
        controlller.addOnDestinationChangedListener { _, destination, _ ->
            title = destination.label
            viewModel.componentes.observe(this) {
                it?.let { temComponentes ->
                    configuraEstadoAppBar(temComponentes)
                    configuraEstadoBottomNavigation(temComponentes)
                }
            }
        }
        binding.mainActivityBottomNavigation.setupWithNavController(controlller)
    }

    private fun configuraEstadoBottomNavigation(temComponentes: ComponentesVisuais) {
        when (temComponentes.bottomNavigation) {
            true -> binding.mainActivityBottomNavigation.visibility = View.VISIBLE
            false -> binding.mainActivityBottomNavigation.visibility = View.GONE
        }
    }


    private fun configuraEstadoAppBar(temComponentes: ComponentesVisuais) {
        when (temComponentes.appBar) {
            true -> supportActionBar?.show()
            false -> supportActionBar?.hide()
        }
    }

    private fun configuraEstadoInicialDosComponentes() {
        supportActionBar?.hide()
    }
}