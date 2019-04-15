package nicestring


fun String.isNice(): Boolean {
    val keywords = arrayOf(containsSubstrings(), containsVowels(), hasTwoDoubleLetters())
    return keywords.count { x -> x.equals(true) } >= 2
}

fun String.containsSubstrings() : Boolean {
    val keywords = arrayOf("bu", "ba", "be")
    return !keywords.map { k -> contains(k) }.reduce {a, b -> a || b}
}

fun String.containsVowels() : Boolean {
    val keywords = arrayOf('a', 'e', 'i', 'o', 'u')

    return keywords.map { k -> this.count { c -> c.equals(k) }}.reduce { a,b ->  a+b} >= 3
}

fun String.hasTwoDoubleLetters() : Boolean {
    for (i in 0..length-2) {
        if (get(i).equals(get(i + 1))) {
             return true
        }
    }
    return false
}