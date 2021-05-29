package communicationservice.domain.entities

data class Push(
    var title: String,
    val message: String,
    val payload: Map<String, String>,
    val topic: String,
    val token: String
)