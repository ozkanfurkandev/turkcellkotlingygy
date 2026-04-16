/**
 * Abstraction (Soyutlama) örneği.
 *
 * Hazırlayan: Furkan Özkan
 *
 * Soyutlama, “kullanıcının bilmesi gereken yüzeyi” gösterip,
 * detayları sınıfın içine saklamaktır.
 *
 * Bu dosyada iki yaklaşım var:
 * - `abstract class`: ortak durum + ortak akış + zorunlu override noktaları
 * - `interface`: davranış sözleşmesi (birden fazla implement edilebilir)
 */

/**
 * `Vehicle` bir soyut sınıf:
 * - Ortak akışı (template method) `start()` içinde tanımlar
 * - Detayları alt sınıflara bırakır: `ignite()` ve `name`
 */
abstract class Vehicle {
    abstract val name: String

    /** Alt sınıflar “çalıştırma” detayını burada sağlar. */
    protected abstract fun ignite()

    /**
     * Template method:
     * Kullanıcı sadece `start()` çağırır; içeride “standart süreç” işler.
     */
    fun start() {
        println("[$name] Başlatma ön kontrolleri yapılıyor...")
        ignite()
        println("[$name] Hazır.")
    }
}

class Car : Vehicle() {
    override val name: String = "Araba"

    override fun ignite() {
        // Detay alt sınıfta: burada araba özelindeki çalıştırma adımını simüle ediyoruz.
        println("[$name] Kontak çevrildi, motor çalıştı.")
    }
}

class Motorcycle : Vehicle() {
    override val name: String = "Motosiklet"

    override fun ignite() {
        println("[$name] Marşa basıldı, motor çalıştı.")
    }
}

/**
 * Interface örneği: “şarj edilebilir olma” davranışı.
 * Bir sınıf aynı anda birden fazla interface’i implement edebilir.
 */
interface Chargeable {
    val batteryPercentage: Int
    fun charge(minutes: Int): Int
}

class ElectricScooter(
    override var batteryPercentage: Int = 30
) : Vehicle(), Chargeable {
    override val name: String = "Elektrikli Scooter"

    override fun ignite() {
        require(batteryPercentage > 0) { "Pil bitmişken başlatılamaz." }
        println("[$name] Sistem açıldı (pil=$batteryPercentage%).")
    }

    override fun charge(minutes: Int): Int {
        require(minutes > 0) { "Şarj süresi pozitif olmalı." }
        // Basit simülasyon: her 1 dk = +1%
        batteryPercentage = (batteryPercentage + minutes).coerceAtMost(100)
        return batteryPercentage
    }
}

fun main() {
    // Soyutlama sayesinde kullanıcı “nasıl çalışıyor” detayını bilmeden `start()` çağırır.
    val vehicles: List<Vehicle> = listOf(Car(), Motorcycle(), ElectricScooter())

    for (v in vehicles) {
        v.start()
        println("----")
    }

    // Interface tarafı: elektrikli araçlara özgü bir davranış
    val scooter = ElectricScooter(batteryPercentage = 10)
    println("Şarj öncesi pil=${scooter.batteryPercentage}%")
    scooter.charge(minutes = 25)
    println("Şarj sonrası pil=${scooter.batteryPercentage}%")
}