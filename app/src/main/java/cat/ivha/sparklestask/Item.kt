package cat.ivha.sparklestask

enum class Categoria {
    COLLARS,
    GORROS,
    ULLERES
}
data class Item (
    val nom: String,
    val desc: String,
    val img: Int,
    val categoria: Categoria
)
