# Producer-Consumer Benchmark: Virtual Threads vs. Thread Pool

Dieses Projekt vergleicht die Leistung von Virtual Threads (Project Loom) mit klassischen Thread Pools in einem Producer-Consumer-Szenario.

## Anforderungen

- **JDK Version**: Java 21 oder neuer (Virtual Threads sind ab Java 21 verfügbar)
- **Build-Tool**: Maven 3.6+
- **Betriebssystem**: Windows, Linux, macOS

## Kompilieren

```bash
mvn clean compile
```

## Ausführen

### Standard-Benchmark (alle Testkombinationen)

```bash
mvn exec:java
```

oder

```bash
mvn clean package
java -cp target/classes vc.liebrecht.Main
```

### Mit CLI-Parametern

```bash
mvn exec:java -Dexec.args="--payload-sizes=1,1024 --producers=1,10 --messages=100,1000"
```

### Dry-Run Modus (schneller Test)

```bash
mvn exec:java -Dexec.args="--dry-run"
```

## CLI-Parameter

| Parameter | Standardwert | Beschreibung |
|-----------|--------------|--------------|
| `--payload-sizes` | `1,1024,1048576` | Payload-Größen in Bytes (kommagetrennt) |
| `--producers` | `1,10,100,1000,10000` | Anzahl der Producer (kommagetrennt) |
| `--messages` | `100,1000,10000` | Anzahl der Nachrichten pro Producer (kommagetrennt) |
| `--warmups` | `2` | Anzahl der Warm-up-Runs (nicht protokolliert) |
| `--runs` | `5` | Anzahl der Messläufe pro Kombination |
| `--queue-capacity` | `10000` | Kapazität der BlockingQueue |
| `--result-dir` | `results` | Verzeichnis für Ergebnisdateien |
| `--max-workers` | automatisch | Maximale Thread-Pool-Größe (überschreibt automatische Berechnung) |
| `--dry-run` | - | Führt nur minimale Testauswahl aus (1 Producer, 100 Messages, 1 Byte Payload) |

### Beispiel

```bash
mvn exec:java -Dexec.args="--payload-sizes=1024,1048576 --producers=10,100 --messages=1000 --runs=3 --result-dir=my-results"
```

## Testvariablen

Der Benchmark testet alle Kombinationen aus:

- **Payload-Größen**: 1 Byte, 1 KB (1.024 Bytes), 1 MB (1.048.576 Bytes)
- **Producer-Anzahlen**: 1, 10, 100, 1.000, 10.000
- **Messages pro Producer**: 100, 1.000, 10.000

Für jede Kombination werden beide Executor-Typen getestet:
- **Thread Pool**: `Executors.newFixedThreadPool()` mit Größe `min(numProducers, availableProcessors * 2)`
- **Virtual Threads**: `Executors.newVirtualThreadPerTaskExecutor()`

## Ergebnisse

### Ausgabedateien

Nach der Ausführung werden folgende Dateien im `results/` Verzeichnis erstellt:

1. **`producer_consumer_benchmark_<timestamp>.csv`**
   - Enthält alle einzelnen Messläufe mit Statistiken
   - Spalten: `timestamp,executorType,payloadSizeBytes,numProducers,messagesPerProducer,totalMessages,runIndex,durationMs,meanMs,medianMs,stddevMs,throughputMsgsPerSec`

2. **`summary_<timestamp>.json`**
   - JSON-Zusammenfassung mit Mittelwerten pro Kombination
   - Vergleich zwischen Thread Pool und Virtual Threads

3. **`errors.log`**
   - Enthält alle Fehler und Stacktraces (falls aufgetreten)

### Konsolen-Output

Der Benchmark gibt während der Ausführung Fortschrittsinformationen aus und zeigt am Ende:

- Tabellarische Übersicht aller Kombinationen mit:
  - Mittelwert (Mean) in Millisekunden
  - Standardabweichung (StdDev) in Millisekunden
  - Durchsatz (Throughput) in Nachrichten pro Sekunde
  - Vergleich zwischen Thread Pool und Virtual Threads (Schneller/Langsamer/Ähnlich)
  - Prozentuale Differenz

- Top-5 schnellste Kombinationen pro Executor-Typ

## Ergebnisinterpretation

### Metriken

- **durationMs**: Laufzeit eines einzelnen Messlaufs in Millisekunden
- **meanMs**: Durchschnittliche Laufzeit über alle Messläufe
- **medianMs**: Median der Laufzeiten
- **stddevMs**: Standardabweichung (zeigt Variabilität der Messungen)
- **throughputMsgsPerSec**: Gesamtdurchsatz in Nachrichten pro Sekunde

### Vergleich Virtual Threads vs. Thread Pool

- **Schneller**: Virtual Threads sind >5% schneller
- **Langsamer**: Virtual Threads sind >5% langsamer
- **Ähnlich**: Differenz <5%

### Typische Erwartungen

- **Kleine Payloads, viele Producer**: Virtual Threads sollten Vorteile zeigen, da sie weniger Overhead bei Thread-Erstellung haben
- **Große Payloads**: Thread Pool könnte bei sehr großen Payloads ähnlich oder besser performen
- **Viele Producer (10.000+)**: Virtual Threads sollten deutlich besser skalieren

## Reproduzierbarkeit

### System-Informationen dokumentieren

Für reproduzierbare Ergebnisse sollten folgende Informationen dokumentiert werden:

- **CPU**: Modell, Anzahl Kerne, Frequenz
- **RAM**: Gesamtgröße, verfügbar während des Tests
- **JDK**: Version und Vendor (z.B. `java -version`)
- **Betriebssystem**: Version und Build
- **Andere Last**: Andere laufende Prozesse minimieren

### Beispiel

```bash
# System-Informationen ausgeben
java -version
echo "CPU: $(sysctl -n machdep.cpu.brand_string 2>/dev/null || echo 'N/A')"
echo "RAM: $(sysctl -n hw.memsize 2>/dev/null || echo 'N/A')"
```

### Mögliche Messstörungen

- **JIT-Compiler**: Erste Läufe können langsamer sein (daher Warm-up-Runs)
- **Garbage Collection**: GC-Pausen können Messungen beeinflussen
- **Andere Prozesse**: CPU- und Speicherlast von anderen Anwendungen
- **Thermal Throttling**: CPU-Drosselung bei hoher Last
- **Betriebssystem-Scheduler**: Thread-Scheduling kann variieren

## Projektstruktur

```
src/main/java/vc/liebrecht/
  ├── Main.java                    # CLI-Entry-Point
  ├── config/
  │   └── BenchmarkConfig.java     # CLI-Parameter-Parsing
  ├── model/
  │   ├── Message.java             # Nachrichten-Record
  │   ├── ExecutorType.java        # Enum für Executor-Typen
  │   └── BenchmarkResult.java     # Ergebnis-Datenklasse
  ├── producer/
  │   └── Producer.java            # Producer-Implementierung
  ├── consumer/
  │   └── Consumer.java            # Consumer-Implementierung
  ├── runner/
  │   └── BenchmarkRunner.java     # Haupt-Benchmark-Logik
  ├── statistics/
  │   └── Statistics.java          # Statistik-Berechnungen
  └── writer/
      └── ResultWriter.java        # CSV/JSON-Export und Konsolen-Output
```

## Fehlerbehandlung

Der Benchmark behandelt folgende Fehler robust:

- **OutOfMemoryError**: Wird abgefangen und protokolliert, Benchmark läuft weiter
- **RejectedExecutionException**: Wird abgefangen, betroffene Kombination wird übersprungen
- **TimeoutException**: Nach 10 Minuten pro Testlauf wird abgebrochen
- **InterruptedException**: Korrekte Behandlung mit `Thread.currentThread().interrupt()`

## Thread-Pool-Größe

Die Thread-Pool-Größe wird automatisch berechnet als:
```
min(numProducers, availableProcessors * 2)
```

Dies kann mit `--max-workers=<n>` überschrieben werden.

## Hinweise

- Der Benchmark kann bei großen Konfigurationen (10.000 Producer, 10.000 Messages) sehr lange dauern
- Verwenden Sie `--dry-run` für schnelle Verifikation
- Für vollständige Tests kann die Ausführung mehrere Stunden dauern
- Virtual Threads benötigen Java 21+ (Project Loom)

## Lizenz

Dieses Projekt ist für akademische/Lehrzwecke erstellt.

