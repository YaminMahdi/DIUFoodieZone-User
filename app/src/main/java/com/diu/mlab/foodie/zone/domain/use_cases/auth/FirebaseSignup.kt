package com.diu.mlab.foodie.zone.domain.use_cases.auth

import com.diu.mlab.foodie.zone.domain.model.FoodieUser
import com.diu.mlab.foodie.zone.domain.model.SuperUser
import com.diu.mlab.foodie.zone.domain.repo.AuthRepo
import com.google.android.gms.auth.api.identity.SignInCredential
import javax.inject.Inject

class FirebaseSignup @Inject constructor (
    val repo: AuthRepo
) {
    operator fun invoke(credential: SignInCredential, user: FoodieUser, success :() -> Unit, failed :(msg : String) -> Unit) {
        val partOfId = user.email.filter { it.isDigit() || it == '-'}
        if(user.userType!="Student")
            repo.firebaseSignup(credential, user, success, failed)
        else if (!credential.id.contains("@diu.edu.bd") || !credential.id.contains("@daffodilvarsity.edu.bd"))
            failed.invoke("Please use DIU email address.")
        else if(partOfId.isNotEmpty() && user.id.contains(partOfId))
            repo.firebaseSignup(credential, user, success, failed)
        else
            failed.invoke("You email add Student ID.")
    }
}