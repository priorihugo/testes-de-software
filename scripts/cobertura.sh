#!/usr/bin/env bash
# Compila o projeto, executa um conjunto de testes JUnit e gera os relatórios de
# cobertura do JaCoCo (motor do EclEmma) e do BA-DUA (motor do Baduíno).
#
# Uso: scripts/cobertura.sh [func|todos]
#   func  -> só TestSetFunc (Parte II-A)
#   todos -> TestSetFunc + TestSetEstr (Parte II-B)
#
# TOOLS aponta para a pasta com jdk8/, jacoco/, badua/ e lib/ (JUnit, PIT).
set -euo pipefail
# impede o Git Bash de reescrever os classpaths separados por ";"
export MSYS_NO_PATHCONV=1

CONJUNTO="${1:-func}"
TOOLS="${TOOLS:-C:/Users/Hugo/tools}"
JAVA="$TOOLS/jdk8/bin/java"
JAVAC="$TOOLS/jdk8/bin/javac"
CP_JUNIT="$TOOLS/lib/junit-4.13.2.jar;$TOOLS/lib/hamcrest-core-1.3.jar"

case "$CONJUNTO" in
	func) TESTES="aquario.TestSetFunc" ;;
	todos) TESTES="aquario.TestSetFunc aquario.TestSetEstr" ;;
	*) echo "conjunto desconhecido: $CONJUNTO (use func ou todos)"; exit 1 ;;
esac

cd "$(dirname "$0")/.."
SAIDA="relatorios/$CONJUNTO"
rm -rf build "$SAIDA"
mkdir -p build/classes build/test-classes "$SAIDA"

"$JAVAC" -encoding UTF-8 -g -d build/classes src/aquario/*.java
"$JAVAC" -encoding UTF-8 -g -d build/test-classes -cp "build/classes;$CP_JUNIT" test/aquario/*.java
CP="build/classes;build/test-classes;$CP_JUNIT"

echo "== JUnit + JaCoCo ($TESTES)"
"$JAVA" -Dfile.encoding=UTF-8 -javaagent:"$TOOLS/jacoco/lib/jacocoagent.jar=destfile=build/jacoco.exec" \
	-cp "$CP" org.junit.runner.JUnitCore $TESTES | tail -3
"$JAVA" -jar "$TOOLS/jacoco/lib/jacococli.jar" report build/jacoco.exec --classfiles build/classes \
	--sourcefiles src --encoding UTF-8 --name "Aquário - $CONJUNTO" \
	--html "$SAIDA/jacoco" --csv "$SAIDA/jacoco.csv" --xml "$SAIDA/jacoco.xml" > /dev/null

echo "== BA-DUA"
(cd build && "$JAVA" -Dfile.encoding=UTF-8 -javaagent:"$TOOLS/badua/lib/ba-dua-agent-rt-0.8.0-all.jar" \
	-cp "classes;test-classes;$CP_JUNIT" org.junit.runner.JUnitCore $TESTES > /dev/null)
"$JAVA" -jar "$TOOLS/badua/lib/ba-dua-cli-0.8.0-all.jar" report -input build/coverage.ser \
	-classes build/classes -show-methods > "$SAIDA/badua.txt"
"$JAVA" -jar "$TOOLS/badua/lib/ba-dua-cli-0.8.0-all.jar" report -input build/coverage.ser \
	-classes build/classes -xml "$SAIDA/badua.xml" > /dev/null

echo "== Resumo ($SAIDA)"
awk -F, 'NR > 1 { im += $4; ic += $5; bm += $6; bc += $7; lm += $8; lc += $9; mm += $12; mc += $13 }
	END {
		printf "JaCoCo  instruções %d/%d (%.1f%%)  desvios %d/%d (%.1f%%)  linhas %d/%d (%.1f%%)  métodos %d/%d\n",
			ic, ic + im, 100 * ic / (ic + im), bc, bc + bm, 100 * bc / (bc + bm), lc, lc + lm, 100 * lc / (lc + lm), mc, mc + mm
	}' "$SAIDA/jacoco.csv"
# Soma o relatório de texto por método, que é a contagem oficial do BA-DUA. O XML
# agrupa pares que caem na mesma linha e por isso lista menos pares.
awk -F'\t' '{ split($2, a, /[()\/]/); c += a[2]; t += a[3] }
	END { printf "BA-DUA  pares definição-uso %d/%d (%.1f%%)\n", c, t, 100 * c / t }' "$SAIDA/badua.txt"
