package com.diu.mlab.foodie.zone.presentation.auth

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diu.mlab.foodie.zone.domain.model.FoodieUser
import com.diu.mlab.foodie.zone.domain.use_cases.auth.AuthUseCases
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val teacherInfo= savedStateHandle.getLiveData<FoodieUser>("teacherInfo")
    val loadingVisibility= savedStateHandle.getLiveData<Boolean>("loading")

    fun setLoadingVisibility(visibility: Boolean, time: Int = 1){
        viewModelScope.launch(Dispatchers.Main){
            if(!visibility)
                delay(time.seconds)
            savedStateHandle["loading"] = visibility
            Log.d("TAG", "setLoadingVisibility: $visibility")
        }
    }


    fun googleSignIn(
        activity: Activity,
        resultLauncher : ActivityResultLauncher<IntentSenderRequest>,
        user: FoodieUser? = null,
        failed : (msg : String) -> Unit,
    ){
        setLoadingVisibility(true)
        viewModelScope.launch(Dispatchers.IO)
        {
            authUseCases.googleSignIn(user, activity, resultLauncher){
                setLoadingVisibility(false)
                failed.invoke(it)
            }
        }
    }

    fun firebaseSignup(
        credential: SignInCredential, user: FoodieUser, success :() -> Unit, failed :(msg : String) -> Unit
    ){
        setLoadingVisibility(true)
        viewModelScope.launch(Dispatchers.IO){
            authUseCases.firebaseSignup(credential, user, success){
                setLoadingVisibility(false)
                failed.invoke(it)
            }
        }
    }

    fun firebaseLogin(
        credential: SignInCredential, success :(user: FoodieUser) -> Unit, failed :(msg : String) -> Unit
    ){
        setLoadingVisibility(true)
        viewModelScope.launch(Dispatchers.IO)
        {
            authUseCases.firebaseLogin(credential, success){
                setLoadingVisibility(false)
                failed.invoke(it)
            }
        }
    }

    fun getTeacherInfo(
        link: String,
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val teacherInfo = authUseCases.getTeacherInfo(link)
            withContext(Dispatchers.Main) {
                if (teacherInfo != null)
                    savedStateHandle["teacherInfo"] = teacherInfo
                else {
                    Log.e("TAG", "failed: Not a valid teacher profile link")
                }
            }
        }
    }
}
