package communicationservice.domain.events

import communicationservice.domain.entities.Email
import io.azam.ulidj.ULID

class EmailEvent(
    override val eventId: String,
    val email: Email
) : Event() {

    companion object {
        fun create(email: Email): EmailEvent {
            return EmailEvent(
                eventId = ULID.random(),
                email = email
            )
        }
    }
}
