package utilities

import model.PageException

class Page<T>(
    val amountOfPages: Int,
    val currentPage: Int,
    val items: List<T>,
    val amountOfElements: Int
)

fun <E> getPage(list: List<E>, page: Int): Page<E> {
    if (page < 1) throw PageException("Page most be 1 or more")
    val chunkedList = list.chunked(15)
    return Page(
        currentPage = page,
        items = chunkedList.getOrElse(page - 1) { listOf() },
        amountOfElements = list.size,
        amountOfPages = chunkedList.size,
    )
}