package model

class DraftNewUser(
    val name: String,
    val email: String,
    val password: String,
    val image: String,
)

class DraftPost(
    val summary: Summary,
    val body: MutableList<Item>
)