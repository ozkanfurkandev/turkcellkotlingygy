fun main() {

    println("Banka uygulamasına hoş geldin\n")

    bakiyeGoster()

    paraYatir(100)
    bakiyeGoster()

    paraCek(30)
    bakiyeGoster()

    paraGonder(20)
    bakiyeGoster()

    bonusEkle()
    bakiyeGoster()
}

var bakiye = 0

fun paraYatir(miktar: Int) {
    bakiye = bakiye + miktar
    println("Para yatırıldı: $miktar TL")
}


fun paraCek(miktar: Int) {
    if (miktar > bakiye) {
        println("Yetersiz bakiye!")
    } else {
        bakiye = bakiye - miktar
        println("Para çekildi: $miktar TL")
    }
}

fun bakiyeGoster() {
    println("Bakiyeniz: $bakiye TL")
}

fun paraGonder(miktar: Int) {
    if (miktar > bakiye) {
        println("Yetersiz bakiye!")
    } else {
        bakiye = bakiye - miktar
        println("$miktar TL gönderildi")
    }
}

fun bonusEkle() {
    bakiye = bakiye + 10
    println("10 TL bonus eklendi")
}


