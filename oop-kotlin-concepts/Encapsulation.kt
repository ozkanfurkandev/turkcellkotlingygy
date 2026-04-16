/**
 * Encapsulation (Kapsülleme) örneği.
 *
 * Hazırlayan: Furkan Özkan
 *
 * Amaç: `balance` gibi kritik bir veriyi dış dünyaya doğrudan açmak yerine
 * kontrollü bir API üzerinden yönetmek.
 *
 * - Dışarıdan `balance = -999` gibi anlamsız atamaları engelleriz
 * - Kuralları tek bir yerde toplarız (validation / business rules)
 * - İleride iç temsil değişse bile (Double -> BigDecimal gibi) dış API’yı koruyabiliriz
 */
class BankAccount(
    ownerName: String,
    openingBalance: Double
) {
    /** Hesap sahibinin adı: dışarıdan okunabilir, içeriden değiştirilebilir. */
    var ownerName: String = ownerName
        private set

    /**
     * Bakiye: dışarıdan sadece okunabilir (`private set`).
     *
     * Kotlin ipucu: `var x private set` kapsülleme için çok pratik bir kalıptır.
     */
    var balance: Double = 0.0
        private set

    init {
        require(ownerName.isNotBlank()) { "Hesap sahibi adı boş olamaz." }
        require(openingBalance >= 0.0) { "Açılış bakiyesi negatif olamaz." }
        balance = openingBalance
    }

    /**
     * Hesaba para yatırır.
     * @return Yeni bakiye
     */
    fun deposit(amount: Double): Double {
        require(amount > 0.0) { "Yatırılacak tutar pozitif olmalı." }
        balance += amount
        return balance
    }

    /**
     * Hesaptan para çeker.
     *
     * Burada “başarısız durumda exception mı atmalı, yoksa false mu dönmeli?”
     * gibi tasarım kararları vardır. Eğitim amacıyla `Boolean` döndürüyoruz.
     */
    fun withdraw(amount: Double): Boolean {
        require(amount > 0.0) { "Çekilecek tutar pozitif olmalı." }

        // Kural: bakiye eksiye düşemez (invariant).
        if (amount > balance) return false

        balance -= amount
        return true
    }

    /**
     * Hesap sahibinin adını günceller.
     * Dışarıda `ownerName = ...` yapmak yerine, bu fonksiyon üzerinden kuralları uygularız.
     */
    fun renameOwner(newName: String) {
        require(newName.isNotBlank()) { "Yeni isim boş olamaz." }
        ownerName = newName.trim()
    }
}

fun main() {
    // Senaryo: Basit bir banka hesabı akışı.
    val account = BankAccount(ownerName = "Furkan", openingBalance = 1000.0)

    println("Başlangıç -> ${account.ownerName}, bakiye=${account.balance}")

    account.deposit(500.0)
    println("Para yatırma sonrası -> bakiye=${account.balance}")

    val ok = account.withdraw(200.0)
    println("200 çekme -> başarılı mı? $ok, bakiye=${account.balance}")

    val ok2 = account.withdraw(5000.0)
    println("5000 çekme -> başarılı mı? $ok2, bakiye=${account.balance}")

    account.renameOwner("Furkan Özkan")
    println("İsim güncelleme -> ${account.ownerName}, bakiye=${account.balance}")

    // Not: `account.balance = 0.0` derlenmez çünkü setter `private`.
}