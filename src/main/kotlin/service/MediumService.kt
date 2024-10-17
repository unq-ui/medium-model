package service

import model.*
import utilities.Page
import utilities.getPage

class MediumService {
    private val idGenerator = IdGenerator()
    val users: MutableList<User> = mutableListOf()
    val posts: MutableList<Post> = mutableListOf()

    /**
     * Registers a new user.
     * @param draftNewUser The draft user data.
     * @return The registered user.
     * @throws UserException if the email is already registered.
     */
    fun registerNewUser(draftNewUser: DraftNewUser): User {
        if (users.any { it.email == draftNewUser.email}) {
            throw UserException("The email is alredy registered.")
        }
        val user = User(
            idGenerator.nextUserId(),
            draftNewUser.email,
            draftNewUser.password,
            draftNewUser.name,
            draftNewUser.image,
            UserRole(),
            mutableListOf(),
        )
        users.add(user)
        return user
    }

    /**
     * Retrieves a user by email and password.
     * @param email The user's email.
     * @param password The user's password.
     * @return The user.
     * @throws UserException if the user is not found.
     */
    fun getUser(email: String, password: String): User {
        return users.find { it.email == email && it.password == password } ?: throw UserException("Not found")
    }

    /**
     * Retrieves a user by id.
     * @param id The user's id.
     * @return The user.
     * @throws UserException if the user is not found.
     */
    fun getUser(id: String): User {
        return users.find { it.id == id } ?: throw UserException("Not found")
    }

    /**
     * Retrieves a post by ID.
     * @param id The post's ID.
     * @return The post.
     * @throws PostException if the post is not found.
     */
    fun getPost(id: String): Post {
        return posts.find { it.id == id } ?: throw PostException("Not found")
    }

    /**
     * Add or remove a post from the user's saved posts.
     * @param idUser The user's ID.
     * @param idPost The post's ID.
     * @return The User updated.
     * @throws UserException if the user is not found.
     * @throws PostException if the post is not found.
     */
    fun  toggleSaved(idUser: String, idPost: String): User {
        val user = getUser(idUser)
        val post = getPost(idPost)
        if (user.postsSaved.contains(post)) {
            user.postsSaved.remove(post)
        } else {
            user.postsSaved.add(post)
        }
        return user
    }

    /**
     * Promote a user to admin.
     * @param idUser The user's ID.
     * @return The User updated.
     * @throws UserException if the user is not found.
     * @throws UserException if the user is not an admin.
     */
    fun promoteUserToAdmin(idUser: String): User {
        val user = getUser(idUser)
        user.role = AdminRole()
        return user
    }

    /**
     * Add a new post.
     * @param idUser The user's ID.
     * @param draftPost The draft post data.
     * @return The new post.
     * @throws UserException if the user is not found.
     * @throws UserException if the user is not an admin.
     */
    fun addPost(idUser: String, draftPost: DraftPost): Post {
        val user = getUser(idUser)
        checkIfUserIsAdmin(user)
        val post = Post(
            idGenerator.nextPostId(),
            draftPost.summary,
            draftPost.body,
            user,
            mutableListOf(),
        )
        posts.add(post)
        return post
    }

    /*
        * Delete a post.
        * @param idUser The user's ID.
        * @param idPost The post's ID.
        * @throws UserException if the user is not found.
        * @throws UserException if the user is not an admin.
        * @throws PostException if the post is not found.
     */
    fun deletePost(idUser: String, idPost: String) {
        val user = getUser(idUser)
        checkIfUserIsAdmin(user)
        val post = getPost(idPost)
        posts.remove(post)
    }

    /**
        * Get the posts.
        * @param pageNumber The page number.
        * @return The page of posts.
     */
    fun getPosts(pageNumber: Int): Page<Post> {
        return getPage(posts, pageNumber)
    }

    /**
     * Add a comment to a post.
     * @param idUser The user's ID.
     * @param idPost The post's ID.
     * @param text The comment text.
     * @return The post updated.
     * @throws UserException if the user is not found.
     * @throws PostException if the post is not found.
     */
    fun addComment(idUser: String, idPost: String, text: String): Post {
        val user = getUser(idUser)
        val post = getPost(idPost)
        val comment = Comment(
            idGenerator.nextCommentId(),
            user,
            text,
            false,
        )
        post.comments.add(comment)
        return post
    }

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
    fun blockComment(idUser: String, idPost: String, idComment: String): Post {
        val user = getUser(idUser)
        checkIfUserIsAdmin(user)
        val post = getPost(idPost)
        val comment = post.comments.find { it.id == idComment } ?: throw CommentException("Not found")
        comment.isBlock = true
        return post
    }

    /**
     * Get the users.
     * @param pageNumber The page number.
     * @return The page of users.
     */
    fun getUsers(pageNumber: Int): Page<User> {
        return getPage(users, pageNumber)
    }

    /**
     * Check if the user is admin
     * @param user The user to check.
     * @throws UserException if the user is not an admin.
     */
    private fun checkIfUserIsAdmin(user: User) {
        if (!user.role.isAdmin()) {
            throw UserException("User is not an admin")
        }
    }
}
