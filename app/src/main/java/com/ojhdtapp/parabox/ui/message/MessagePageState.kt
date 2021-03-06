package com.ojhdtapp.parabox.ui.message

import com.ojhdtapp.parabox.domain.model.Contact
import com.ojhdtapp.parabox.domain.model.PluginConnection
import com.ojhdtapp.parabox.domain.model.Profile
import com.ojhdtapp.parabox.domain.model.chat.ChatBlock

class MessagePageState {
}

data class ContactState(val isLoading: Boolean = true, val data: List<Contact> = emptyList())
data class MessageState(
    val state: Int = MessageState.NULL,
    val profile: Profile? = null,
    val data: Map<Long, List<ChatBlock>>? = null,
    val message: String? = null
) {
    companion object {
        const val NULL = 0
        const val LOADING = 1
        const val SUCCESS = 2
        const val ERROR = 3
    }
}

data class GroupInfoState(
    val state: Int = GroupInfoState.NULL,
    val resource: GroupEditResource? = null,
    val message: String? = null
) {
    companion object {
        const val NULL = 0
        const val LOADING = 1
        const val SUCCESS = 2
        const val ERROR = 3
    }
}

sealed class ContactTypeFilterState(
    val label: String,
    val contactCheck: (contact: Contact) -> Boolean
) {
    class All : ContactTypeFilterState("类型", { _: Contact -> true })
    class Grouped :
        ContactTypeFilterState("已编组", { contact: Contact -> contact.contactId != contact.senderId })

    class Ungrouped :
        ContactTypeFilterState("未编组", { contact: Contact -> contact.contactId == contact.senderId })
}

sealed class ContactReadFilterState(val contactCheck: (contact: Contact) -> Boolean) {
    class All : ContactReadFilterState({ contact: Contact -> true })
    class Unread : ContactReadFilterState({ contact: Contact ->
        contact.latestMessage?.let { it.unreadMessagesNum > 0 } ?: false
    })
}

data class GroupEditResource(
    val name: List<String>,
    val avatar: List<ByteArray>,
    val pluginConnections: List<PluginConnection>
)