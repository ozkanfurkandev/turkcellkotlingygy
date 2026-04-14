## Kotlin OOP Kavramları (Örneklerle)

Bu mini paket, Kotlin üzerinde OOP (Nesne Yönelimli Programlama) temellerini **tek tek dosyalara ayrılmış, çalıştırılabilir** örneklerle anlatır.

**Hazırlayan:** Furkan Özkan

### Hedefler
- **Kavramı tanımak**: “Nedir / ne değildir?”
- **Neden kullanıldığını görmek**: bakım, güvenlik, okunabilirlik, genişletilebilirlik
- **Kotlin’e özgü detayları yakalamak**: `open`, `override`, property erişimi, `abstract` / `interface` farkları

### Dosya Haritası
- **[`Encapsulation.kt`](./Encapsulation.kt)**: Veri saklama, kontrollü erişim, doğrulama (validation), invariants
- **[`Inheritance.kt`](./Inheritance.kt)**: `open` / `override`, `protected`, `super`, hiyerarşi tasarımı
- **[`Polymorphism.kt`](./Polymorphism.kt)**: overriding + overloading, dinamik gönderim (dynamic dispatch), koleksiyonla kullanım
- **[`Abstraction.kt`](./Abstraction.kt)**: abstract class + interface, ortak akış (template method) fikri

### Nasıl Çalıştırılır?
Bu örnekler tek dosya olarak da çalışır. İki pratik seçenek:

- **IntelliJ IDEA / Android Studio**
  - Dosyayı açın, `main()` fonksiyonunun yanındaki “Run” ikonuna basın.

- **Terminal (Kotlin derleyicisi ile)**
  - Kotlin CLI kuruluysa, ilgili dosyayı çalıştırabilirsiniz:

```bash
# Örnek: tek bir dosyayı derleyip çalıştırma
FILE="Encapsulation"
kotlinc "$FILE.kt" -include-runtime -d "$FILE.jar"
java -jar "$FILE.jar"
```

> Not: Her dosyada ayrı bir `main()` olduğu için aynı anda tek dosya derlemek en kolayıdır.

Çalıştırabileceğin dosyalar:
- `FILE="Encapsulation"`
- `FILE="Inheritance"`
- `FILE="Polymorphism"`
- `FILE="Abstraction"`

---

### 1) Encapsulation (Kapsülleme)
Encapsulation, bir sınıfın iç durumunu (state) **dışarıya doğrudan açmayıp**, bu duruma erişimi **kontrollü** hale getirmesidir.

- **Ne kazandırır?**
  - **Güvenlik**: Yanlış atamaları engeller
  - **Tutarlılık**: Sınıfın invariants’ını korur (örn. bakiye negatif olamaz)
  - **Esneklik**: İç temsil değişse bile dış API aynı kalabilir

- **İpucu (Kotlin)**: `private set`, `val` + fonksiyonlarla kontrollü değişim sık kullanılan bir yaklaşımdır.

Örnek kod: [`Encapsulation.kt`](./Encapsulation.kt)

---

### 2) Inheritance (Kalıtım)
Inheritance, bir sınıfın başka bir sınıfın özelliklerini ve davranışını **miras almasıdır**.

- **Ne kazandırır?**
  - **Kod tekrarını azaltır**
  - **Ortak davranışı tek yerde toplar**
  - **Genişletilebilirlik** sağlar (ama aşırı kullanımı hiyerarşi karmaşası doğurabilir)

- **İpucu (Kotlin)**: Kotlin’de sınıflar varsayılan olarak `final`dır; kalıtım için `open` gerekir.

Örnek kod: [`Inheritance.kt`](./Inheritance.kt)

---

### 3) Polymorphism (Çok Biçimlilik)
Polymorphism, aynı arayüz/üst tür üzerinden farklı nesnelerin **kendi davranışlarını sergileyebilmesidir**.

- **Türler**
  - **Overriding (runtime)**: Üst sınıf fonksiyonunu alt sınıfta ezmek (`override`)
  - **Overloading (compile-time)**: Aynı isimli fonksiyonun farklı parametrelerle yazılması

- **Ne kazandırır?**
  - “`if/else` ile tür kontrolü” yerine, davranışı nesneye dağıtarak daha temiz tasarım

Örnek kod: [`Polymorphism.kt`](./Polymorphism.kt)

---

### 4) Abstraction (Soyutlama)
Abstraction, gereksiz detayları saklayıp **yalnızca gerekli yüzeyi** (API) göstermektir.

- **Araçlar**
  - `abstract class`: Ortak state + ortak davranış + zorunlu override noktaları
  - `interface`: Davranış sözleşmesi (çoğunlukla state yok), birden fazla implement edilebilir

Örnek kod: [`Abstraction.kt`](./Abstraction.kt)

---

### Küçük Çalışma Önerileri
- Dosyalardaki `main()` içindeki “senaryo” kısımlarını değiştirip çıktıları gözlemleyin.
- Bazı yanlış kullanım denemeleri yapın (negatif para yatırma vb.) ve doğrulamanın nasıl davrandığını inceleyin.
