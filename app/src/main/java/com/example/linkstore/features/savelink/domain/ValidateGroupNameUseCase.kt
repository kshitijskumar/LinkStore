package com.example.linkstore.features.savelink.domain

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ValidateGroupNameUseCase @Inject constructor() {

    operator fun invoke(groupName: String): Boolean {
        return groupName.isNotEmpty()
    }

}