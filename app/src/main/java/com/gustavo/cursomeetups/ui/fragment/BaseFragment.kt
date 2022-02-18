package com.gustavo.cursomeetups.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.gustavo.cursomeetups.NavGraphDirections
import com.gustavo.cursomeetups.R

open class BaseFragment(layout: Int) : Fragment(layout) {

    private val controller by lazy { findNavController() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        if (FirebaseAuth.getInstance().currentUser == null) {
            vaiParaInicio()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.lista_eventos_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.lista_eventos_menu_sair -> {
                FirebaseAuth.getInstance().signOut()
                vaiParaInicio()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun vaiParaInicio() {
        NavGraphDirections.acaoGlobalInicio().let(controller::navigate)
    }
}