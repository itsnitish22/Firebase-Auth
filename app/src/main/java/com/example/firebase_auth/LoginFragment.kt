package com.example.firebase_auth

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebase_auth.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {
            val email: String = binding.inputEmail.text.toString()
            val password: String = binding.inputPassword.text.toString()

            //checking for valid inputs
            when {
                TextUtils.isEmpty(email) -> {
                    binding.inputEmail.error = "Email required"
                }

                TextUtils.isEmpty(password) -> {
                    binding.inputPassword.error = "Password required"
                }

                else -> {
                    //authorising the user and logging in
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    if(firebaseUser.isEmailVerified){
                                        Toast.makeText(activity, "Logged In", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(activity, "Email isn't verified. Please verify it.", Toast.LENGTH_SHORT).show()
                                    }

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
        }

        binding.register.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }
}
