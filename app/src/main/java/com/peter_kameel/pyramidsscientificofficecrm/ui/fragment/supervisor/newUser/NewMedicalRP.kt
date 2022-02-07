package com.peter_kameel.pyramidsscientificofficecrm.ui.fragment.supervisor.newUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.peter_kameel.pyramidsscientificofficecrm.databinding.CreateNewUserBinding
import com.peter_kameel.pyramidsscientificofficecrm.helper.objects.CheckNetwork
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import com.peter_kameel.pyramidsscientificofficecrm.util.Shared
import com.peter_kameel.pyramidsscientificofficecrm.util.SharedTag
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewMedicalRP : Fragment() {
    private val viewModel by viewModels<NewMedicalRPViewModel>()
    private var userID: String? = null
    private var _binding: CreateNewUserBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //ADD Account
    private val auth = FirebaseAuth.getInstance()
    @Inject
    lateinit var shared: Shared
    @Inject
    lateinit var checkNetwork: CheckNetwork
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreateNewUserBinding.inflate(inflater, container, false)
        val view = binding.root
        val uid = shared.readSharedString(SharedTag.UID, "null").toString()
        userID = uid
        //create new user
        binding.CreateNewUserButton.setOnClickListener {
            when {
                binding.NewUserNameTextFiled.text.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.medicalName, Toast.LENGTH_LONG).show()
                }
                binding.NewUserPasswordTextFiled.text.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.medicalPassword, Toast.LENGTH_LONG).show()
                }
                binding.NewUserEmailTextFiled.text.isNullOrEmpty() -> {
                    Toast.makeText(context, Massages.medicalEmail, Toast.LENGTH_LONG).show()
                }
                !checkNetwork.isNetworkAvailable() -> {
                    Toast.makeText(context, Massages.connection, Toast.LENGTH_LONG).show()
                }
                else -> {
                    createNewMedicalRP(
                        binding.NewUserEmailTextFiled.text.toString(),
                        binding.NewUserPasswordTextFiled.text.toString(),
                        binding.NewUserNameTextFiled.text.toString()
                    )
                }
            }
        }
        //on user add successfully
        viewModel.massageLiveData.observeForever {
            if (it == Massages.successful){
                clearView()
            }
        }
        return view
    }

    private fun createNewMedicalRP(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.saveMedicalRPData(name,userID.toString())
                } else {
                    Toast.makeText(context, Massages.error, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun clearView(){
        binding.NewUserNameTextFiled.text?.clear()
        binding.NewUserPasswordTextFiled.text?.clear()
        binding.NewUserEmailTextFiled.text?.clear()
        Toast.makeText(context,Massages.successful,Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}