package com.ojhdtapp.parabox.domain.use_case

import com.ojhdtapp.parabox.data.local.AppDatabase
import com.ojhdtapp.parabox.data.remote.dto.MessageDto
import com.ojhdtapp.parabox.domain.repository.MainRepository
import javax.inject.Inject

class HandleNewMessage @Inject constructor(
    val repository: MainRepository
) {
    suspend operator fun invoke(dto: MessageDto) {
        repository.handleNewMessage(dto)
    }
}