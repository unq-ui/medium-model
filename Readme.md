# UNQ » UIs » Dominio » Medium
[![](https://jitpack.io/v/unq-ui/medium-model.svg)](https://jitpack.io/#unq-ui/medium-model)

Construcción de Interfaces de Usuario, Universidad Nacional de Quilmes.

### Dependencia

Agregar el repositorio:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Agregar la dependencia:

```xml
<dependency>
    <groupId>com.github.unq-ui</groupId>
    <artifactId>medium-model</artifactId>
    <version>v1.1.0</version>
</dependency>
```

### Interfaz de uso

```kotlin
class MediumService {
    val users: MutableList<User> = mutableListOf()
    val posts: MutableList<Post> = mutableListOf()

    /**
     * Registers a new user.
     * @param draftNewUser The draft user data.
     * @return The registered user.
     * @throws UserException if the email is already registered.
     */
    fun registerNewUser(draftNewUser: DraftNewUser): User

    /**
     * Retrieves a user by email and password.
     * @param email The user's email.
     * @param password The user's password.
     * @return The user.
     * @throws UserException if the user is not found.
     */
    fun getUser(email: String, password: String): User

    /**
     * Retrieves a user by id.
     * @param id The user's id.
     * @return The user.
     * @throws UserException if the user is not found.
     */
    fun getUser(id: String): User

    /**
     * Retrieves a post by ID.
     * @param id The post's ID.
     * @return The post.
     * @throws PostException if the post is not found.
     */
    fun getPost(id: String): Post

    /**
     * Add or remove a post from the user's saved posts.
     * @param idUser The user's ID.
     * @param idPost The post's ID.
     * @return The User updated.
     * @throws UserException if the user is not found.
     * @throws PostException if the post is not found.
     */
    fun  toggleSaved(idUser: String, idPost: String): User

    /**
     * Promote a user to admin.
     * @param idUser The user's ID.
     * @return The User updated.
     * @throws UserException if the user is not found.
     * @throws UserException if the user is not an admin.
     */
    fun promoteUserToAdmin(idUser: String): User

    /**
     * Add a new post.
     * @param idUser The user's ID.
     * @param draftPost The draft post data.
     * @return The new post.
     * @throws UserException if the user is not found.
     * @throws UserException if the user is not an admin.
     */
    fun addPost(idUser: String, draftPost: DraftPost): Post

    /**
        * Delete a post.
        * @param idUser The user's ID.
        * @param idPost The post's ID.
        * @throws UserException if the user is not found.
        * @throws UserException if the user is not an admin.
        * @throws PostException if the post is not found.
     */
    fun deletePost(idUser: String, idPost: String)

    /**
     * Get the posts.
     * @param pageNumber The page number.
     * @return The page of posts.
     */
    fun getPosts(pageNumber: Int): Page<Post>

    /**
     * Add a comment to a post.
     * @param idUser The user's ID.
     * @param idPost The post's ID.
     * @param text The comment text.
     * @return The post updated.
     * @throws UserException if the user is not found.
     * @throws PostException if the post is not found.
     */
    fun addComment(idUser: String, idPost: String, text: String): Post

    /**
     * Block a comment.
     * @param idUser The user's ID.
     * @param idPost The post's ID.
     * @param idComment The comment's ID.
     * @return The post updated.
     * @throws UserException if the user is not found.
     * @throws UserException if the user is not an admin.
     * @throws PostException if the post is not found.
     * @throws CommentException if the comment is not found.
     */
    fun blockComment(idUser: String, idPost: String, idComment: String): Post

    /**
     * Get the users.
     * @param pageNumber The page number.
     * @return The page of users.
     */
    fun getUsers(pageNumber: Int): Page<User>
}
```

### Iniciar el sistema con datos

```kotlin
val system = initSystem()
```

[Ver usuarios del sistema](https://github.com/unq-ui/medium-model/blob/main/resources/users.kt)

### Modelo

```kotlin
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

open class Item(val name: String, var data: String)
class Title(data: String): Item("Title", data)
class Image(data: String): Item("Image", data)
class Text(data: String): Item("Text", data)
class Quote(data: String): Item("Quote", data)

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
```

* El MediumSistem es el encargado de setear los ids de cada elemento que se agrega el sistema.
* Para simplificar se utilizan objetos draft

```kotlin
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
```

### Utils

Para crear paginas facilmente se puede usar:

```kotlin
fun <E> getPage(list: List<E>, page: Int): Page<E>
```