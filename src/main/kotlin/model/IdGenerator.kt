package model

class IdGenerator {
    private var currentUserId = 0
    private var currentPostId = 0
    private var currentCommentId = 0

    fun nextUserId(): String = "u_${++currentUserId}"
    fun nextPostId(): String = "p_${++currentPostId}"
    fun nextCommentId(): String = "c_${++currentCommentId}"
}