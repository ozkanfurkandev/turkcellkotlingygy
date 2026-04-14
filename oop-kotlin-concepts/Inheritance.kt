/**
 * Inheritance (Kalıtım) örneği.
 *
 * Hazırlayan: Furkan Özkan
 *
 * Hedef:
 * - Ortak davranışı üst sınıfta toplamak
 * - Alt sınıfların bu davranışı genişletmesini/özelleştirmesini göstermek
 *
 * Kotlin notu:
 * - Sınıflar ve fonksiyonlar varsayılan olarak `final`dır.
 * - Kalıtım/override istiyorsanız `open` yazmanız gerekir.
 */
open class Animal(
    val name: String
) {
    /**
     * `protected` üyeler dışarıdan görünmez ama alt sınıflarca kullanılabilir.
     * Bu, “alt sınıfın bilmesi gereken ama dış dünyanın bilmemesi gereken” bilgiyi saklamaya yarar.
     */
    protected var energy: Int = 100

    open fun eat() {
        // Üst sınıf davranışı: enerji artır.
        energy = (energy + 10).coerceAtMost(100)
        println("$name yemek yiyor. enerji=$energy")
    }

    open fun move() {
        // Basit bir kural: hareket etmek enerji düşürür.
        energy = (energy - 15).coerceAtLeast(0)
        println("$name hareket ediyor. enerji=$energy")
    }
}

class Dog(
    name: String,
    private val breed: String
) : Animal(name) {

    fun bark() {
        println("$name havlıyor! (ırk=$breed)")
    }

    override fun move() {
        // `super.move()` ortak davranışı çalıştırır; sonra alt sınıf ekleme yaparız.
        super.move()
        println("$name koşmayı seviyor.")
    }
}

class Cat(
    name: String
) : Animal(name) {
    override fun eat() {
        // Alt sınıf, üst sınıfın kuralını tamamen değiştirebilir.
        energy = (energy + 5).coerceAtMost(100)
        println("$name seçici yiyor. enerji=$energy")
    }
}

fun main() {
    val dog = Dog(name = "Karabaş", breed = "Kangal")
    val cat = Cat(name = "Maviş")

    // Ortak davranışlar üst sınıfta.
    dog.eat()
    dog.move()
    dog.bark()

    println("----")

    cat.move()
    cat.eat()

    // Not: `energy` dışarıdan erişilemez (protected), ama sınıf içi mantıkta kullanılır.
}