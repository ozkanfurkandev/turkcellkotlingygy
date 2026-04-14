/**
 * Polymorphism (Çok biçimlilik) örneği.
 *
 * Hazırlayan: Furkan Özkan
 *
 * Burada iki şeyi birlikte gösteriyoruz:
 * - Overriding (runtime): `sound()` davranışı alt sınıfa göre değişir.
 * - Overloading (compile-time): Aynı isimli fonksiyonun farklı parametrelerle çağrılması.
 *
 * Önemli fikir:
 * Üst tür (`Animal`) üzerinden dolaşırken, hangi alt tür olursa olsun doğru davranış çalışır.
 * Buna dinamik gönderim (dynamic dispatch) denir.
 */
open class Animal(
    val name: String
) {
    open fun sound() {
        println("$name: (belirsiz hayvan sesi)")
    }

    // Overloading: aynı isim, farklı parametre listesi
    fun greet() = println("$name selamladı.")

    fun greet(toWhom: String) = println("$name, $toWhom kişisini selamladı.")
}

class Cat(name: String) : Animal(name) {
    override fun sound() {
        println("$name: Miyav")
    }
}

class Dog(name: String) : Animal(name) {
    override fun sound() {
        println("$name: Hav hav")
    }
}

/**
 * Polimorfizm genelde “iş yapan” fonksiyonlarda en net görülür:
 * Bu fonksiyon, Animal listesini alır ve tür kontrolü yapmadan davranışı çağırır.
 */
fun makeAllAnimalsSpeak(animals: List<Animal>) {
    for (animal in animals) {
        animal.sound() // hangi sınıfsa onun override ettiği sürüm çalışır
    }
}

fun main() {
    val animals: List<Animal> = listOf(
        Cat(name = "Tekir"),
        Dog(name = "Karabaş"),
        Animal(name = "Bilinmeyen")
    )

    // Overloading örneği
    animals[0].greet()
    animals[1].greet("Furkan")

    println("----")

    // Overriding + dynamic dispatch örneği
    makeAllAnimalsSpeak(animals)
}