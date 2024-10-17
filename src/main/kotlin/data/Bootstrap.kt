package data

import model.AdminRole
import model.DraftNewUser
import model.DraftPost
import model.Item
import service.MediumService
import kotlin.random.Random

val random = Random(100)

private fun addUsers(mediumService: MediumService) {
    allUsers.forEach { mediumService.registerNewUser(
        DraftNewUser(
            "${it.firstName} ${it.lastName}",
            it.email,
            it.password,
            it.image
        )
    ) }
    val user = mediumService.registerNewUser(
        DraftNewUser(
            "admin",
            "admin@gmail.com",
            "admin",
            "https://cdn.iconscout.com/icon/free/png-256/avatar-370-456322.png"
        )
    )
    user.role = AdminRole()
}

private fun getRandomComment(): String {
    return allContent.shuffled(random).take(1).first().toString()
}

private fun createBody(): MutableList<Item> {
    return allContent
        .shuffled(random)
        .take(random.nextInt(4, 10))
        .toMutableList()
}

private fun addPosts(mediumService: MediumService) {
    mediumService.users.filter { it.role.isAdmin() }.forEach { user ->
        summaries.shuffled(random).forEach { summary ->
            mediumService.addPost(user.id, DraftPost(summary, createBody()))
        }
    }
}

private fun addComments(mediumService: MediumService) {
    mediumService.posts.forEach { post ->
        mediumService.users.shuffled(random).take(10).forEach { user ->
            mediumService.addComment(user.id, post.id, getRandomComment())
        }
    }
}

private fun addSavedPosts(mediumService: MediumService) {
    mediumService.users.take(50).forEach { user ->
        mediumService.posts.shuffled(random).take(10).forEach { post ->
            mediumService.toggleSaved(user.id, post.id)
        }
    }
}

fun initSystem(): MediumService {
    val mediumService = MediumService()
    addUsers(mediumService)
    addPosts(mediumService)
    addComments(mediumService)
    addSavedPosts(mediumService)
    return mediumService
}
