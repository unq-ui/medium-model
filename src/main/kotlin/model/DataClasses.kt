package model

open class MediumRole(val name: String) {
    open fun isAdmin() = false
}

class UserRole: MediumRole("User")

class AdminRole: MediumRole("Admin") {
    override fun isAdmin() = true
}

class User(
    val id: String,
    val email: String,
    var password: String,
    var name: String,
    var image: String,
    var role: MediumRole,
    val postsSaved: MutableList<Post>,
)

open class Item(val name: String)
class Title(var data: String): Item("Title")
class Image(var data: String): Item("Image")
class Text(var data: String): Item("Text")
class Quote(var data: String): Item("Quote")
class Section(
    val title: Title,
    val image: Image?,
    val text: Text,
): Item("Section")

class Summary(
    var headline: String,
    var subHeadline: String,
    var image: String?,
)

class Comment(
    val id: String,
    val owner: User,
    val message: String,
    var isBlock: Boolean,
)

class Post(
    val id: String,
    val summary: Summary,
    val body: MutableList<Item>,
    val owner: User,
    val comments: MutableList<Comment>,
)