package com.example.firebase_auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.firebase_auth.databinding.FragmentLoginBinding
import com.example.firebase_auth.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.AccessController.getContext

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private var _binding: FragmentRegisterBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnRegister.setOnClickListener {
            // TODO: 1. Check if edit texts are empty and show error accordingly, 2. confirm pass check
            val name: String = binding.inputName.text.toString()
            val email: String = binding.inputEmailid.text.toString()
            val password: String = binding.inputPasswordReg.text.toString()
            val confirmPassword: String = binding.inputConfirmPass.text.toString()

            if (TextUtils.isEmpty(name)) {
                binding.inputName.error = "Name required"
            } else if (TextUtils.isEmpty(email)) {
                binding.inputEmailid.error = "Email required"
            } else if (TextUtils.isEmpty(password)) {
                binding.inputPasswordReg.error = "Password required"
            } else if (TextUtils.isEmpty(confirmPassword)) {
                binding.inputConfirmPass.error = "Type the password again"
            } else if (password != confirmPassword) {
                binding.inputPasswordReg.error
                binding.inputConfirmPass.error
                Toast.makeText(activity, "Password didn't match", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    activity,
                                    "Successfully Registered",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                findNavController().navigateUp()
                            } else {
                                Toast.makeText(
                                    activity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
            }
        }

        binding.login.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}