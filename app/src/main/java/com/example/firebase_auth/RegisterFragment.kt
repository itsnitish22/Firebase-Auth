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
import com.example.firebase_auth.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private var _binding: FragmentRegisterBinding? = null
    private var auth: FirebaseAuth? = null
    private lateinit var databaseReference: DatabaseReference
    private val binding get() = _binding!!
    private var uid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnRegister.setOnClickListener {
            val name: String = binding.inputName.text.toString()
            val email: String = binding.inputEmailid.text.toString()
            val password: String = binding.inputPasswordReg.text.toString()
            val confirmPassword: String = binding.inputConfirmPass.text.toString()

            //checking for valid inputs
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
                //adding user for authentication
                auth = FirebaseAuth.getInstance()
                auth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                            if (task.isSuccessful) {
                                val currentUser = Firebase.auth.currentUser
                                if (currentUser != null) {
                                    Log.i("Current User", currentUser.uid)
                                }
                                sendEmailVerification(currentUser, name, email, confirmPassword)
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

    private fun sendEmailVerification(
        currentUser: FirebaseUser?,
        userName: String,
        userEmail: String,
        userPass: String
    ) {
        Toast.makeText(
            activity,
            "Successfully Registered. Check e-mail for verification",
            Toast.LENGTH_SHORT
        ).show()
        currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Notification", "Email sent.")
                saveUserData(currentUser.uid, userName, userEmail, userPass)
            }
        }
    }

    //saving the user data in realtime database (firebase)
    private fun saveUserData(
        userId: String,
        userName: String,
        userEmail: String,
        userPass: String
    ) {
        Log.i("Inside saveUser", "Inside saveUser")
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val user = User(userName, userEmail, userPass)
        databaseReference.child(userId).setValue(user)
    }
}