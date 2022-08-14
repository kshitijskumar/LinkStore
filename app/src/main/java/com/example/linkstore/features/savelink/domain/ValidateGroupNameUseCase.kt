package com.example.linkstore.features.savelink.domain

import javax.inject.Inject

class ValidateGroupNameUseCase @Inject constructor() {

    operator fun invoke(groupName: String): Boolean {
        return groupName.isNotEmpty()
    }

}