package communicationservice.domain.entities

data class Template(
    val path: String,
    val data: Map<String, String>
)
