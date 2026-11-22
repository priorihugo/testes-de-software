# Pares definição-uso inalcançáveis (BA-DUA)

Execução de referência: `scripts/cobertura.sh todos` após o ciclo 4 — 523 de 547 pares cobertos. Os 24 pares restantes não podem ser exercitados por nenhuma entrada válida ou inválida do programa. As linhas referem-se a `src/aquario/Aquario.java` e `src/aquario/Main.java` nessa versão; "alvo" é o destino do desvio, quando o uso é uma condição.

O relatório XML do BA-DUA lista 22 desses pares. Os outros 2 estão em `vizinhoVazio` e `vizinhoDoTipo` e não aparecem no XML porque envolvem a variável interna que o compilador cria para o `for (int[] d : DIRECOES)`, que não tem nome no código-fonte.

## 1. Laços que sempre executam pelo menos uma volta (13 pares)

M e N são validados como inteiros positivos, `DIRECOES` tem sempre 4 elementos, `PARAMETROS` tem sempre 8 e `split(";", -1)` devolve pelo menos uma parte. Logo, nenhum desses laços pode terminar sem executar o corpo, e o par "definição inicial do contador → saída imediata do laço" é inalcançável.

| Método | Variável | Definição | Uso (alvo) |
|---|---|---|---|
| `Aquario.inicializar` | i | 59 | 59 (66) |
| `Aquario.inicializar` | i | 69 | 69 (84) |
| `Aquario.inicializar` | j | 70 | 70 (69) |
| `Aquario.lerMatriz` | i | 119 | 119 (123) |
| `Aquario.iterar` | i | 133 | 133 (140) |
| `Aquario.iterar` | j | 134 | 134 (133) |
| `Aquario.vizinhoVazio` | contador do for-each | — | saída sem volta |
| `Aquario.vizinhoDoTipo` | contador do for-each | — | saída sem volta |
| `Aquario.temPeixeB` | i | 248 | 248 (255) |
| `Aquario.temPeixeB` | j | 249 | 249 (248) |
| `Aquario.getMatriz` | i | 266 | 266 (277) |
| `Aquario.getMatriz` | j | 270 | 270 (266) |
| `Main.executar` | k | 28 | 28 (34) |

## 2. Contagem zero igual à declaração (2 pares)

`contaA` e `contaB` começam em zero. Para o par chegar ao `return`, a contagem inicial teria de ser igual a X (ou Y), mas X e Y já foram validados como positivos. Uma matriz sem A ou sem B sempre é recusada (CE07, CT59).

| Método | Variável | Definição | Uso (alvo) |
|---|---|---|---|
| `Aquario.inicializar` | contaA | 67 | 84 (87) |
| `Aquario.inicializar` | contaB | 68 | 87 (90) |

## 3. Combinações proibidas pelas regras do jogo (3 pares)

São as restrições da seção 6.5 da Parte I (revisão 1.1).

| Método | Variável | Definição | Uso (alvo) | Motivo |
|---|---|---|---|---|
| `Aquario.agirA` | p.fome | 159 (movimento zera a fome) | 164 (165, morte) | Com fome 0, `fome >= MA` é falso, pois MA ≥ 1 — restrição E(C1, C2) |
| `Aquario.agirA` | p.movimentos | 161 (bloqueio zera os movimentos) | 168 (171, reprodução) | Com 0 movimentos, `movimentos >= RA` é falso, pois RA ≥ 1 — restrição R(C3, C1) |
| `Aquario.agirB` | p.fome | 183 (alimentação zera a fome) | 191 (192, morte) | Com fome 0, `fome >= MB` é falso, pois MB ≥ 1 — alimentação e morte por fome não ocorrem na mesma ação |

## 4. Primeira volta contra as demais (6 pares)

Nos laços que escrevem separadores, a condição `i > 0` (ou `j > 0`) usa o contador. Com a definição inicial (valor 0) a condição é sempre falsa, e com a definição do incremento (valor ≥ 1) é sempre verdadeira. Como as duas definições ficam na mesma linha, o BA-DUA mostra os pares impossíveis com o mesmo número de linha dos possíveis.

| Método | Variável | Definição | Uso (alvo) | Combinação impossível |
|---|---|---|---|---|
| `Aquario.getMatriz` | i | 266 | 267 (268) | i inicial (0) com `i > 0` verdadeiro |
| `Aquario.getMatriz` | i | 266 | 267 (270) | i incrementado (≥ 1) com `i > 0` falso |
| `Aquario.getMatriz` | j | 270 | 271 (272) | j inicial (0) com `j > 0` verdadeiro |
| `Aquario.getMatriz` | j | 270 | 271 (274) | j incrementado (≥ 1) com `j > 0` falso |
| `Main.executar` | i | 39 | 41 (42) | i inicial (0) com `i > 0` verdadeiro |
| `Main.executar` | i | 39 | 41 (44) | i incrementado (≥ 1) com `i > 0` falso |

## Por que não reescrever o código para eliminar esses pares

Todos decorrem da estrutura normal de laços `for` e das invariantes do jogo. Reescrever os laços apenas para que a ferramenta deixe de enxergar esses pares mudaria o código sem benefício de comportamento. A prática em teste de fluxo de dados é documentar os pares inalcançáveis e calcular a cobertura sobre os alcançáveis.
