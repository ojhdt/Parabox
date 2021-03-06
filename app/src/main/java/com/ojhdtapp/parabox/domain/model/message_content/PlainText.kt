package com.ojhdtapp.parabox.domain.model.message_content

import kotlinx.parcelize.Parcelize

@Parcelize
data class PlainText(val text: String) : MessageContent {
    val type = MessageContent.PLAIN_TEXT
    override fun getContentString(): String {
        return text
    }
}
