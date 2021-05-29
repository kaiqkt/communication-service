package communicationservice.domain.events

import communicationservice.domain.entities.Email

abstract class Event {
    abstract val eventId: String

    companion object {
        fun createEvent(email: Any): Event =
            EmailEvent.create(
                email = email as Email
            )
    }
}