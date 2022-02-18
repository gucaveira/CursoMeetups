package com.gustavo.cursomeetups.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.gustavo.cursomeetups.R
import com.gustavo.cursomeetups.databinding.InicioBinding
import com.gustavo.cursomeetups.ui.extensions.snackBar
import com.gustavo.cursomeetups.ui.viewmodel.ComponentesVisuais
import com.gustavo.cursomeetups.ui.viewmodel.EstadoAppViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment(R.layout.inicio) {

    companion object {
        private const val RC_SIGN_IN = 1
    }

    private val controller by lazy { findNavController() }
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()
    private var _binding: InicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = InicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verificaAutenticacao()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoEntrar() {
        binding.inicioBotaoEntrar.setOnClickListener {
            abreAutenticacaoComFirebaseUI()
        }
    }

    private fun abreAutenticacaoComFirebaseUI() {
        val provedores = listOf(AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(provedores).build(), RC_SIGN_IN)
    }

    override fun onResume() {
        super.onResume()
        estadoAppViewModel.temComponentes =
            ComponentesVisuais(appBar = false, bottomNavigation = false)
    }

    private fun verificaAutenticacao() {
        FirebaseAuth.getInstance().currentUser?.let {
            vaiParaListaEventos()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    vaiParaListaEventos()
                }
            } else {
                view?.snackBar("Não foi possível autenticar")
            }
        }
    }

    private fun vaiParaListaEventos() {
        LoginFragmentDirections.actionLoginToListaEventosFragment().let(controller::navigate)
    }
}