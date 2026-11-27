# Relatório da Parte II — Teste Estrutural do Jogo do Aquário

**Universidade Federal de Juiz de Fora — Departamento de Ciência da Computação**
**DCC168 Teste de Software — 2026-3 — Prof. André Luiz de Oliveira**

**Integrantes:** Hugo Amoglia Priori (202076035), Igor Westermann Lima (201876021) e Laura Neves (201976032)

**Repositório:** https://github.com/priorihugo/testes-de-software

> Rascunho mantido junto com o código. Cada ciclo de trabalho atualiza este arquivo no mesmo commit, de modo que o histórico do repositório mostra a ordem real em que testes e código foram escritos. Trechos marcados como **[A PREENCHER PELO GRUPO]** ou **[PRINT DO ECLIPSE]** dependem do grupo.

## Sumário

1. Introdução
2. Arquitetura e implementação
3. Parte II-A: automatização do teste funcional (TestSet-Func)
4. Parte II-B: aplicação do teste estrutural (TestSet-Estr)
   - 4.1 Método de trabalho
   - 4.2 Critérios baseados em fluxo de controle
   - 4.3 Critérios baseados em fluxo de dados
   - 4.4 Casos de teste estruturais
   - 4.5 Diário de execução
   - 4.6 Cobertura final e requisitos inalcançáveis
   - 4.7 Defeitos encontrados
5. Análise pessoal: eficiência e eficácia das técnicas
6. Pergunta ao grupo: influência dos casos funcionais (TDD)
7. Conclusão
8. Referências

## 1. Introdução

Este relatório apresenta a Parte II do trabalho de DCC168 para o Jogo do Aquário: a implementação do programa em Java, a automatização em JUnit dos 68 casos de teste funcionais especificados na Parte I (TestSet-Func) e a aplicação dos critérios de teste estrutural baseados em fluxo de controle e em fluxo de dados, que levou a 19 casos adicionais (TestSet-Estr).

A especificação usada é o documento da Parte I. As decisões de projeto D01–D11 definidas lá (vizinhança ortogonal, ordem de atualização, desempates e contadores) determinam as saídas esperadas e foram seguidas na implementação. As decisões novas surgidas durante a Parte II (D12–D15: limite de inteiros, formato dos parâmetros, colchetes da matriz e fim da entrada) foram incorporadas na revisão 1.2 do documento, que também registra a execução dos 68 casos na coluna Saída Obtida.

A cobertura foi medida com o JaCoCo, motor do EclEmma, e com o BA-DUA, motor do Baduíno, pela linha de comando (`scripts/cobertura.sh`). Os números e as figuras oficiais do EclEmma e do Baduíno vêm da execução no Eclipse, registrada nas seções 3 e 4.6.

## 2. Arquitetura e implementação

![Diagrama de classes](figuras/diagrama-classes.png)

*Figura 1 — Diagrama de classes.*

| Classe | Responsabilidade |
|---|---|
| `Aquario` | Valida a entrada (`inicializar`), guarda a matriz e os parâmetros RA, MA, RB e MB, executa uma iteração (`iterar`) aplicando as regras de A (`agirA`) e de B (`agirB`) e informa o estado (`temPeixeB`, `getPontuacao`, `getMatriz`). |
| `Peixe` | Tipo, posição e contadores individuais: movimentos consecutivos, fome e consumos desde a última reprodução. |
| `Tipo` | Enumeração dos tipos A e B. |
| `Jogo` | Laço da partida: executa iterações, mostra a matriz, lê a opção C/E do jogador e informa a pontuação. |
| `Main` | Interface de console: lê M, N, X, Y, RA, MA, RB, MB e a matriz, e inicia o jogo. |
| `EntradaInvalidaException` | Sinaliza a recusa da inicialização (D03). |

Decisões de projeto relevantes para o teste:

- **Lógica separada da entrada e saída.** `Jogo.executar` recebe um `BufferedReader` e um `PrintStream`. Os testes simulam o teclado com um texto ("C\nE\n"), o que permite automatizar os casos de controle do jogo, como o CT25, que digita uma opção inválida.
- **Mesma notação da Parte I.** A matriz é lida e exibida no formato `[A . .; . . B]`, o que permitiu transcrever os 68 casos sem tradução manual.
- **Parâmetros recebidos como texto.** Assim, entradas não inteiras como "1.5" (classe I2) chegam ao programa e podem ser recusadas, em vez de falharem antes, na conversão.

## 3. Parte II-A — Automatização do teste funcional (TestSet-Func)

### 3.1 Implementação dos casos de teste

Os 68 casos da Tabela 2 da Parte I foram transcritos para `test/aquario/TestSetFunc.java`, um método por caso (`ct01` … `ct69`). A transcrição foi feita por um gerador que lê a tabela do documento, o que elimina erros de digitação e garante que nenhum caso foi acrescentado nesta subfase. Cada caso válido verifica a matriz exibida após cada iteração, o tipo de encerramento e a pontuação final; cada caso inválido verifica que a inicialização é recusada.

```java
/** CT25: V1–V6, V14–V23, V27, V33, I20. */
@Test
public void ct25() throws IOException {
    String saida = jogar(Aquario.inicializar("1", "4", "1", "1", "3", "3", "2", "3", "A . . B"), "Z\nC\nC\nE\n");
    assertIteracoes(saida, "[. A B .]", "[A B . .]", "[B . . .]");
    assertFim(saida, false, 3);
    assertEquals(1, ocorrencias(saida, "Opção inválida."));
}
```

*Listagem 1 — CT25: a opção inválida "Z" não avança a partida nem altera a pontuação.*

### 3.2 Resultado da execução

| Execução | Casos | Passaram | Falharam | Defeitos |
|---|---|---|---|---|
| 22/11/2026, commit `8409274` | 68 | 68 | 0 | nenhum |

A coluna Saída Obtida da Tabela 2 da Parte I foi preenchida com esse resultado na revisão 1.2 do documento.

**[PRINT DO ECLIPSE: JUnit com TestSetFunc — 68/68 passando]**

### 3.3 Cobertura do TestSet-Func

Cobertura do TestSet-Func sobre a versão final do código (a mesma usada nos prints do Eclipse):

| Ferramenta | Critério | Coberto | Total | % |
|---|---|---|---|---|
| JaCoCo (EclEmma) | Instruções | 1025 | 1264 | 81,1 |
| JaCoCo (EclEmma) | Desvios | 107 | 122 | 87,7 |
| JaCoCo (EclEmma) | Linhas | 171 | 201 | 85,1 |
| JaCoCo (EclEmma) | Métodos | 22 | 26 | 84,6 |
| BA-DUA (Baduíno) | Pares definição-uso | 464 | 547 | 84,8 |

Na primeira execução, sobre o código do commit `8409274`, os valores foram 80,8% das instruções, 87,9% dos desvios e 85,6% dos pares. A pequena diferença vem das mudanças dos ciclos 2 a 4, na validação de parâmetros e na remoção de verificações impossíveis.

**[PRINT DO ECLIPSE: view Coverage do EclEmma com TestSetFunc]**
**[PRINT DO ECLIPSE: view do Baduíno com TestSetFunc]**

**Cobertura por classe — TestSet-Func**

**[PRINT DO ECLIPSE: código de Aquario.java colorido pelo EclEmma, só TestSetFunc]**
**[PRINT DO ECLIPSE: código de Jogo.java colorido pelo EclEmma, só TestSetFunc]**
**[PRINT DO ECLIPSE: código de Main.java colorido pelo EclEmma, só TestSetFunc]**
**[PRINT DO ECLIPSE: código de Peixe.java colorido pelo EclEmma, só TestSetFunc]**
**[PRINT DO ECLIPSE: código de Tipo.java colorido pelo EclEmma, só TestSetFunc]**
**[PRINT DO ECLIPSE: código de EntradaInvalidaException.java colorido pelo EclEmma, só TestSetFunc]**
**[PRINT DO ECLIPSE: view do Baduíno com os métodos de Aquario expandidos, só TestSetFunc]**
**[PRINT DO ECLIPSE: view do Baduíno com os métodos de Jogo expandidos, só TestSetFunc]**
**[PRINT DO ECLIPSE: view do Baduíno com os métodos de Main expandidos, só TestSetFunc]**

Prévias geradas pela linha de comando, apenas para referência, em `docs/previas/`: `junit-func.png`, `jacoco-func.png`, `jacoco-func-Main.png` e `badua-func.png`.

O principal trecho não coberto é a classe `Main`, com 0%: nenhum caso funcional passa pela leitura do console, porque a Parte I especifica os casos a partir dos parâmetros e da matriz, não da digitação. Os demais trechos não cobertos são variações da leitura da matriz (colchetes, linha vazia) e alguns pares definição-uso de laços.

## 4. Parte II-B — Aplicação do teste estrutural (TestSet-Estr)

### 4.1 Método de trabalho

1. Para os seis métodos com mais decisões, os critérios de fluxo de controle e de fluxo de dados foram aplicados manualmente: grafo de fluxo de controle (GFC), complexidade ciclomática, caminhos independentes e associações definição-uso (seções 4.2 e 4.3).
2. Cada requisito foi associado a um caso existente que o exercita. Requisitos sem caso geraram casos novos, e requisitos impossíveis foram justificados.
3. Os relatórios das ferramentas foram usados para conferir a análise manual e para os demais métodos.
4. Os casos novos foram escritos antes de qualquer mudança no código, em ciclos registrados no diário (seção 4.5).

### 4.2 Critérios baseados em fluxo de controle

Para cada método são apresentados o código com os nós numerados, o GFC, a complexidade ciclomática calculada de duas formas, V(G) = E − N + 2 e V(G) = número de decisões + 1, e um conjunto de caminhos independentes com o caso de teste que executa cada um. O nó 0 representa a entrada do método e o último nó, a saída. Os critérios Todos-os-Nós e Todos-os-Arcos ficam satisfeitos pelos caminhos listados, que juntos percorrem todos os nós e arcos de cada grafo.

#### 4.2.1 `Aquario.inteiroPositivo`

```java
private static int inteiroPositivo(String nome, String valor) {       // nó 0
    String texto = valor.trim();                                      // nó 1
    if (texto.matches("-?\\d+")) {                                    // nó 1
        BigInteger numero = new BigInteger(texto);                    // nó 2
        if (numero.signum() <= 0) {                                   // nó 2
            throw new EntradaInvalidaException(... " não é positivo"); // nó 3
        }
        if (numero.bitLength() > 31) {                                // nó 4
            throw new EntradaInvalidaException(... " é grande demais"); // nó 5
        }
        return numero.intValue();                                     // nó 6
    }
    if (texto.matches("-?\\d*[.,]\\d+")) {                            // nó 7
        throw new EntradaInvalidaException(... " não é inteiro");     // nó 8
    }
    throw new EntradaInvalidaException(... " não é um inteiro ...");  // nó 9
}                                                                     // nó 10 (saída)
```

![GFC de inteiroPositivo](figuras/gfc-inteiroPositivo.png)

*Figura 2 — GFC de `Aquario.inteiroPositivo`.*

V(G) = 14 − 11 + 2 = **5** = 4 decisões + 1.

**Nós do grafo**

- Nó 0: entrada (parâmetros `nome` e `valor`)
- Nó 1: `texto = valor.trim()`; decisão: `texto` tem só dígitos?
- Nó 2: `numero = new BigInteger(texto)`; decisão: `numero <= 0`?
- Nó 3: lança "não é positivo"
- Nó 4: decisão: `numero` tem mais de 31 bits?
- Nó 5: lança "é grande demais"
- Nó 6: retorna o valor
- Nó 7: decisão: `texto` é número decimal?
- Nó 8: lança "não é inteiro"
- Nó 9: lança "não é um inteiro escrito só com dígitos"
- Nó 10: saída

**Critério Todos-os-Nós**

| Nó | Caminho | Caso |
|---|---|---|
| 0 | C1 | CT36 |
| 1 | C1 | CT36 |
| 2 | C1 | CT36 |
| 3 | C1 | CT36 |
| 4 | C2 | CE02 |
| 5 | C2 | CE02 |
| 6 | C3 | CT01 |
| 7 | C4 | CT37 |
| 8 | C4 | CT37 |
| 9 | C5 | CE01 |
| 10 | C1 | CT36 |

**Critério Todos-os-Arcos**

| Arco | Caminho | Caso |
|---|---|---|
| 0→1 | C1 | CT36 |
| 1→2 | C1 | CT36 |
| 1→7 | C4 | CT37 |
| 2→3 | C1 | CT36 |
| 2→4 | C2 | CE02 |
| 4→5 | C2 | CE02 |
| 4→6 | C3 | CT01 |
| 7→8 | C4 | CT37 |
| 7→9 | C5 | CE01 |
| 3→10 | C1 | CT36 |
| 5→10 | C2 | CE02 |
| 6→10 | C3 | CT01 |
| 8→10 | C4 | CT37 |
| 9→10 | C5 | CE01 |


**Critério Caminhos independentes**

| Caminho | Nós | Situação | Caso |
|---|---|---|---|
| C1 | 0-1-2-3-10 | inteiro não positivo | CT36 (X=0) |
| C2 | 0-1-2-4-5-10 | inteiro maior que o limite de `int` | CE02 |
| C3 | 0-1-2-4-6-10 | inteiro positivo válido | CT01 |
| C4 | 0-1-7-8-10 | número decimal | CT37 (X=1.5) |
| C5 | 0-1-7-9-10 | outro formato | CE01 ("abc"), CE18 ("1e1") |

#### 4.2.2 `Aquario.agirA`

```java
private void agirA(Peixe p) {                   // nó 0
    int[] livre = vizinhoVazio(p);              // nó 1
    if (livre != null) {                        // nó 1
        mover(p, livre);                        // nó 2
        p.movimentos++;                         // nó 2
        p.fome = 0;                             // nó 2
    } else {
        p.movimentos = 0;                       // nó 3
        p.fome++;                               // nó 3
    }
    if (p.fome >= ma) {                         // nó 4
        remover(p);                             // nó 5
        return;                                 // nó 5
    }
    if (p.movimentos >= ra) {                   // nó 6
        nascer(Tipo.A, vizinhoVazio(p));        // nó 7
        p.movimentos = 0;                       // nó 7
    }
}                                               // nó 8 (saída)
```

![GFC de agirA](figuras/gfc-agirA.png)

*Figura 3 — GFC de `Aquario.agirA`.*

V(G) = 11 − 9 + 2 = **4** = 3 decisões + 1.

**Nós do grafo**

- Nó 0: entrada (parâmetro `p`)
- Nó 1: `livre = vizinhoVazio(p)`; decisão: `livre != null`?
- Nó 2: move `p`, incrementa `movimentos`, zera `fome`
- Nó 3: zera `movimentos`, incrementa `fome`
- Nó 4: decisão: `fome >= ma`?
- Nó 5: remove `p` (morte) e retorna
- Nó 6: decisão: `movimentos >= ra`?
- Nó 7: nasce o filho e zera `movimentos`
- Nó 8: saída

**Critério Todos-os-Nós**

| Nó | Caminho | Caso |
|---|---|---|
| 0 | C1 | CT01 |
| 1 | C1 | CT01 |
| 2 | C1 | CT01 |
| 3 | C3 | CT20 |
| 4 | C1 | CT01 |
| 5 | C4 | CT07 |
| 6 | C1 | CT01 |
| 7 | C2 | CT02 |
| 8 | C1 | CT01 |

**Critério Todos-os-Arcos**

| Arco | Caminho | Caso |
|---|---|---|
| 0→1 | C1 | CT01 |
| 1→2 | C1 | CT01 |
| 1→3 | C3 | CT20 |
| 2→4 | C1 | CT01 |
| 3→4 | C3 | CT20 |
| 4→5 | C4 | CT07 |
| 4→6 | C1 | CT01 |
| 6→7 | C2 | CT02 |
| 6→8 | C1 | CT01 |
| 7→8 | C2 | CT02 |
| 5→8 | C4 | CT07 |


**Critério Caminhos independentes**

| Caminho | Nós | Situação | Caso |
|---|---|---|---|
| C1 | 0-1-2-4-6-8 | move, sobrevive, não reproduz | CT01 |
| C2 | 0-1-2-4-6-7-8 | move e reproduz | CT02 (RA=1) |
| C3 | 0-1-3-4-6-8 | bloqueado, sobrevive | CT20 |
| C4 | 0-1-3-4-5-8 | bloqueado, morre de fome | CT07 (MA=1) |

Dois caminhos do grafo são impossíveis e por isso não entram no conjunto: 0-1-2-4-5 (mover e morrer na mesma ação, pois mover zera a fome) e 0-1-3-4-6-7 (bloqueado e reproduzir, pois o bloqueio zera os movimentos). Correspondem às restrições E(C1, C2) e R(C3, C1) da revisão 1.1 da Parte I.

#### 4.2.3 `Aquario.agirB`

```java
private void agirB(Peixe p) {                                     // nó 0
    int[] presa = vizinhoDoTipo(p, Tipo.A);                       // nó 1
    if (presa != null) {                                          // nó 1
        grade[presa[0]][presa[1]].vivo = false;                   // nó 2
        mover(p, presa);                                          // nó 2
        p.consumos++;                                             // nó 2
        p.fome = 0;                                               // nó 2
    } else {
        int[] livre = vizinhoVazio(p);                            // nó 3
        if (livre != null) {                                      // nó 3
            mover(p, livre);                                      // nó 4
        }
        p.fome++;                                                 // nó 5
    }
    if (p.fome >= mb) {                                           // nó 6
        remover(p);                                               // nó 7
        return;                                                   // nó 7
    }
    if (p.consumos >= rb                                          // nó 8
            && vizinhoDoTipo(p, Tipo.B) == null) {                // nó 9
        nascer(Tipo.B, vizinhoVazio(p));                          // nó 10
        p.consumos = 0;                                           // nó 10
    }
}                                                                 // nó 11 (saída)
```

![GFC de agirB](figuras/gfc-agirB.png)

*Figura 4 — GFC de `Aquario.agirB`. A condição composta da linha 195 foi separada nos nós 8 e 9, pois o `&&` só avalia a segunda parte se a primeira for verdadeira.*

V(G) = 16 − 12 + 2 = **6** = 5 decisões + 1.

**Nós do grafo**

- Nó 0: entrada (parâmetro `p`)
- Nó 1: `presa = vizinhoDoTipo(p, A)`; decisão: `presa != null`?
- Nó 2: consome o A, move `p`, incrementa `consumos`, zera `fome`
- Nó 3: `livre = vizinhoVazio(p)`; decisão: `livre != null`?
- Nó 4: move `p` para a célula livre
- Nó 5: incrementa `fome`
- Nó 6: decisão: `fome >= mb`?
- Nó 7: remove `p` (morte) e retorna
- Nó 8: decisão: `consumos >= rb`?
- Nó 9: decisão: não há B vizinho?
- Nó 10: nasce o filho e zera `consumos`
- Nó 11: saída

**Critério Todos-os-Nós**

| Nó | Caminho | Caso |
|---|---|---|
| 0 | C1 | CT12 |
| 1 | C1 | CT12 |
| 2 | C1 | CT12 |
| 3 | C4 | CT01 |
| 4 | C4 | CT01 |
| 5 | C4 | CT01 |
| 6 | C1 | CT12 |
| 7 | C6 | CT16 |
| 8 | C1 | CT12 |
| 9 | C2 | CT14 |
| 10 | C2 | CT14 |
| 11 | C1 | CT12 |

**Critério Todos-os-Arcos**

| Arco | Caminho | Caso |
|---|---|---|
| 0→1 | C1 | CT12 |
| 1→2 | C1 | CT12 |
| 1→3 | C4 | CT01 |
| 3→4 | C4 | CT01 |
| 3→5 | C5 | CT20 |
| 4→5 | C4 | CT01 |
| 2→6 | C1 | CT12 |
| 5→6 | C4 | CT01 |
| 6→7 | C6 | CT16 |
| 6→8 | C1 | CT12 |
| 8→9 | C2 | CT14 |
| 8→11 | C1 | CT12 |
| 9→10 | C2 | CT14 |
| 9→11 | C3 | CT15 |
| 10→11 | C2 | CT14 |
| 7→11 | C6 | CT16 |


**Critério Caminhos independentes**

| Caminho | Nós | Situação | Caso |
|---|---|---|---|
| C1 | 0-1-2-6-8-11 | come, ainda abaixo de RB | CT12 (RB=2) |
| C2 | 0-1-2-6-8-9-10-11 | come, atinge RB, isolado: reproduz | CT14 (RB=1) |
| C3 | 0-1-2-6-8-9-11 | come, atinge RB, mas tem B vizinho | CT15 |
| C4 | 0-1-3-4-5-6-8-11 | move para célula livre e sobrevive | CT01 |
| C5 | 0-1-3-5-6-8-11 | sem presa nem espaço: permanece | CT20 |
| C6 | 0-1-3-4-5-6-7-11 | move sem comer e morre de fome | CT16 (MB=1) |

O caminho 0-1-3-4-5-6-8-9-10-11, reprodução adiada de um B que já tinha consumos suficientes e só agora ficou isolado, também é executado, pelo CT22 na 4ª iteração. O caminho 0-1-2-6-7 (comer e morrer de fome na mesma ação) é impossível, porque comer zera a fome.

#### 4.2.4 `Aquario.iterar`

```java
public void iterar() {                                    // nó 0
    List<Peixe> ordem = new ArrayList<Peixe>();           // nó 1
    for (int i = 0; i < linhas; i++) {                    // nó 1 (i = 0), nó 2 (i < linhas), nó 8 (i++)
        for (int j = 0; j < colunas; j++) {               // nó 3 (j = 0), nó 4 (j < colunas), nó 7 (j++)
            if (grade[i][j] != null) {                    // nó 5
                ordem.add(grade[i][j]);                   // nó 6
            }
        }
    }
    for (Peixe p : ordem) {                               // nó 9 (há próximo?)
        if (!p.vivo) {                                    // nó 10
            continue;                                     // volta ao nó 9
        }
        if (p.tipo == Tipo.A) {                           // nó 11
            agirA(p);                                     // nó 12
        } else {
            agirB(p);                                     // nó 13
        }
    }
    pontuacao++;                                          // nó 14
}                                                         // nó 15 (saída)
```

![GFC de iterar](figuras/gfc-iterar.png)

*Figura 5 — GFC de `Aquario.iterar`.*

V(G) = 21 − 16 + 2 = **7** = 6 decisões + 1.

**Nós do grafo**

- Nó 0: entrada
- Nó 1: cria a lista `ordem`; `i = 0`
- Nó 2: decisão: `i < linhas`?
- Nó 3: `j = 0`
- Nó 4: decisão: `j < colunas`?
- Nó 5: decisão: célula ocupada?
- Nó 6: adiciona o peixe à lista
- Nó 7: `j++`
- Nó 8: `i++`
- Nó 9: decisão: há próximo peixe na lista?
- Nó 10: decisão: peixe já morto ou consumido?
- Nó 11: decisão: peixe é do tipo A?
- Nó 12: `agirA(p)`
- Nó 13: `agirB(p)`
- Nó 14: `pontuacao++`
- Nó 15: saída

**Critério Todos-os-Nós**

| Nó | Caso |
|---|---|
| 0 | CT01 |
| 1 | CT01 |
| 2 | CT01 |
| 3 | CT01 |
| 4 | CT01 |
| 5 | CT01 |
| 6 | CT01 |
| 7 | CT01 |
| 8 | CT01 |
| 9 | CT01 |
| 10 | CT01 |
| 11 | CT01 |
| 12 | CT01 |
| 13 | CT01 |
| 14 | CT01 |
| 15 | CT01 |

**Critério Todos-os-Arcos**

| Arco | Caso |
|---|---|
| 0→1 | CT01 |
| 1→2 | CT01 |
| 2→3 | CT01 |
| 2→9 | CT01 |
| 3→4 | CT01 |
| 4→5 | CT01 |
| 4→8 | CT01 |
| 5→6 | CT01 |
| 5→7 | CT01 |
| 6→7 | CT01 |
| 7→4 | CT01 |
| 8→2 | CT01 |
| 9→10 | CT01 |
| 9→14 | CT01 |
| 10→9 | CT12 |
| 10→11 | CT01 |
| 11→12 | CT01 |
| 11→13 | CT01 |
| 12→9 | CT01 |
| 13→9 | CT01 |
| 14→15 | CT01 |


Em métodos com laços, os caminhos independentes se distinguem pelos arcos que percorrem dentro de cada volta. Todos os arcos são exercitados:

**Arcos agrupados por situação**

| Arcos | Situação | Caso |
|---|---|---|
| 2→3, 4→5, 5→6, 6→7, 7→4, 4→8, 8→2, 2→9 | percorre a matriz e coleta os peixes | todos os casos válidos |
| 5→7 | célula vazia | CT01 |
| 9→10, 10→11, 11→12, 12→9 | peixe A age | CT01 |
| 11→13, 13→9 | peixe B age | CT01 |
| 10→9 | peixe já consumido é ignorado | CT12 |
| 9→14, 14→15 | fim da lista e soma da pontuação | todos os casos válidos |

Não há caminho em que um dos laços termine sem dar nenhuma volta, porque M e N são sempre pelo menos 1. Esses são os pares inalcançáveis do grupo 1 da seção 4.6.

#### 4.2.5 `Jogo.executar`

```java
public static int executar(Aquario aquario, BufferedReader entrada, PrintStream saida) throws IOException { // nó 0
    while (true) {
        aquario.iterar();                                              // nó 1
        saida.println("Iteração " + ...);                              // nó 1
        if (!aquario.temPeixeB()) {                                    // nó 2
            saida.println("Não há mais peixes B. Fim de jogo.");       // nó 3
            break;                                                     // nó 3
        }
        if (lerOpcao(entrada, saida).equals("E")) {                    // nó 4
            saida.println("Jogo encerrado pelo jogador.");             // nó 5
            break;                                                     // nó 5
        }
    }
    saida.println("Pontuação final: " + aquario.getPontuacao());       // nó 6
    return aquario.getPontuacao();                                     // nó 6
}                                                                      // nó 7 (saída)
```

![GFC de executar](figuras/gfc-executar.png)

*Figura 6 — GFC de `Jogo.executar`.*

V(G) = 9 − 8 + 2 = **3** = 2 decisões + 1.

**Nós do grafo**

- Nó 0: entrada
- Nó 1: `aquario.iterar()` e mostra a matriz
- Nó 2: decisão: não há B?
- Nó 3: mostra "Fim de jogo" e sai do laço
- Nó 4: decisão: opção lida é E?
- Nó 5: mostra "Jogo encerrado pelo jogador" e sai do laço
- Nó 6: mostra a pontuação e retorna
- Nó 7: saída

**Critério Todos-os-Nós**

| Nó | Caminho | Caso |
|---|---|---|
| 0 | C1 | CT16 |
| 1 | C1 | CT16 |
| 2 | C1 | CT16 |
| 3 | C1 | CT16 |
| 4 | C2 | CT01 |
| 5 | C2 | CT01 |
| 6 | C1 | CT16 |
| 7 | C1 | CT16 |

**Critério Todos-os-Arcos**

| Arco | Caminho | Caso |
|---|---|---|
| 0→1 | C1 | CT16 |
| 1→2 | C1 | CT16 |
| 2→3 | C1 | CT16 |
| 2→4 | C2 | CT01 |
| 4→5 | C2 | CT01 |
| 4→1 | C3 | CT04 |
| 3→6 | C1 | CT16 |
| 5→6 | C2 | CT01 |
| 6→7 | C1 | CT16 |


**Critério Caminhos independentes**

| Caminho | Nós | Situação | Caso |
|---|---|---|---|
| C1 | 0-1-2-3-6-7 | sem B após a iteração: encerra sozinho | CT16 |
| C2 | 0-1-2-4-5-6-7 | jogador escolhe E | CT01 |
| C3 | 0-1-2-4-1-2-4-5-6-7 | jogador escolhe C e depois E | CT04 |

#### 4.2.6 `Jogo.lerOpcao`

```java
private static String lerOpcao(BufferedReader entrada, PrintStream saida) throws IOException { // nó 0
    while (true) {
        saida.print("Digite C para continuar ou E para encerrar: ");  // nó 1
        String linha = entrada.readLine();                            // nó 1
        if (linha == null) {                                          // nó 2
            return "E";                                               // nó 3
        }
        String opcao = linha.trim();                                  // nó 4
        if (opcao.equals("C")                                         // nó 4
                || opcao.equals("E")) {                               // nó 5
            return opcao;                                             // nó 6
        }
        saida.println("Opção inválida.");                             // nó 7
    }
}                                                                     // nó 8 (saída)
```

![GFC de lerOpcao](figuras/gfc-lerOpcao.png)

*Figura 7 — GFC de `Jogo.lerOpcao`.*

V(G) = 11 − 9 + 2 = **4** = 3 decisões + 1.

**Nós do grafo**

- Nó 0: entrada
- Nó 1: pede a opção e lê `linha`
- Nó 2: decisão: `linha == null`?
- Nó 3: retorna "E"
- Nó 4: `opcao = linha.trim()`; decisão: `opcao` é "C"?
- Nó 5: decisão: `opcao` é "E"?
- Nó 6: retorna `opcao`
- Nó 7: mostra "Opção inválida."
- Nó 8: saída

**Critério Todos-os-Nós**

| Nó | Caminho | Caso |
|---|---|---|
| 0 | C1 | CE09 |
| 1 | C1 | CE09 |
| 2 | C1 | CE09 |
| 3 | C1 | CE09 |
| 4 | C2 | CT04 |
| 5 | C3 | CT01 |
| 6 | C2 | CT04 |
| 7 | C4 | CT25 |
| 8 | C1 | CE09 |

**Critério Todos-os-Arcos**

| Arco | Caminho | Caso |
|---|---|---|
| 0→1 | C1 | CE09 |
| 1→2 | C1 | CE09 |
| 2→3 | C1 | CE09 |
| 2→4 | C2 | CT04 |
| 4→6 | C2 | CT04 |
| 4→5 | C3 | CT01 |
| 5→6 | C3 | CT01 |
| 5→7 | C4 | CT25 |
| 7→1 | C4 | CT25 |
| 3→8 | C1 | CE09 |
| 6→8 | C2 | CT04 |


**Critério Caminhos independentes**

| Caminho | Nós | Situação | Caso |
|---|---|---|---|
| C1 | 0-1-2-3-8 | a entrada terminou | CE09 |
| C2 | 0-1-2-4-6-8 | opção C | CT04 |
| C3 | 0-1-2-4-5-6-8 | opção E | CT01 |
| C4 | 0-1-2-4-5-7-1-2-4-6-8 | opção inválida e depois C | CT25 |

### 4.3 Critérios baseados em fluxo de dados

Os requisitos de fluxo de dados são as associações definição-uso de cada variável: um **c-uso** é o uso em um cálculo ou chamada; um **p-uso** é o uso em uma condição, associado a cada arco que sai da decisão (coluna "Arco para a linha"). Para cada método são apresentados os requisitos de três critérios:

- **todas-as-definições:** para cada definição, ao menos um uso deve ser exercitado;
- **todos-os-c-usos:** todas as associações com c-uso;
- **todos-os-p-usos:** todas as associações com p-uso.

Os requisitos foram obtidos do BA-DUA, que os calcula a partir do código compilado, e conferidos com a análise manual dos seis métodos da seção 4.2. Para indicar o caso que cobre cada associação, o BA-DUA foi executado separadamente para cada um dos 87 casos; a coluna Caso mostra o primeiro caso, na ordem CT01…CT69 e depois CE01…CE19, que exercita a associação. As linhas referem-se ao código do Apêndice B. Parâmetros aparecem definidos na primeira linha do corpo do método, e `this` representa o próprio objeto. Duas linhas iguais em uma tabela de p-usos correspondem a decisões diferentes na mesma linha do código, como as duas partes de uma condição `a || b`, ou à definição inicial e ao incremento de um laço `for`, que ficam na mesma linha.

O relatório XML do BA-DUA não lista 25 associações que envolvem as variáveis criadas pelo compilador para os laços `for (... : ...)` (11 em `vizinhoVazio`, 11 em `vizinhoDoTipo` e 3 em `iterar`), porque elas não têm nome no código-fonte. Essas associações entram nas contagens por método, que seguem o relatório de texto da ferramenta; duas delas são inalcançáveis (Apêndice A).

| Método | Associações | Cobertas | Inalcançáveis |
|---|---|---|---|
| `Aquario.inicializar` | 109 | 104 | 5 |
| `Aquario.inteiroPositivo` | 18 | 18 | 0 |
| `Aquario.lerMatriz` | 25 | 24 | 1 |
| `Aquario.iterar` | 59 | 57 | 2 |
| `Aquario.agirA` | 37 | 35 | 2 |
| `Aquario.agirB` | 52 | 51 | 1 |
| `Aquario.vizinhoVazio` | 31 | 30 | 1 |
| `Aquario.vizinhoDoTipo` | 41 | 40 | 1 |
| `Aquario.dentro` | 16 | 16 | 0 |
| `Aquario.temPeixeB` | 46 | 44 | 2 |
| `Aquario.getMatriz` | 50 | 44 | 6 |
| `Jogo.executar` | 11 | 11 | 0 |
| `Jogo.lerOpcao` | 9 | 9 | 0 |
| `Main.executar` | 40 | 37 | 3 |
| `Main.lerLinha` | 3 | 3 | 0 |
| **Total** | **547** | **523** | **24** |

#### 4.3.1 `Aquario.inicializar`

109 associações, 104 cobertas, 5 inalcançáveis.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `A` | 46 | 73 | CT01 |
| `B` | 46 | 76 | CT01 |
| `vx` | 46 | 84 (arco para 85) | CT55 |
| `vy` | 47 | 87 (arco para 88) | CT58 |
| `vra` | 48 | 90 | CT01 |
| `vma` | 49 | 90 | CT01 |
| `vrb` | 50 | 90 | CT01 |
| `vmb` | 51 | 90 | CT01 |
| `vm` | 52 | 56 (arco para 57) | CT52 |
| `vn` | 53 | 66 | CT01 |
| `celulas` | 55 | 56 (arco para 57) | CT52 |
| `i` | 59 | 59 (arco para 60) | CT01 |
| `grade` | 66 | 90 | CT01 |
| `contaA` | 67 | 84 (arco para 85) | CE07 |
| `contaB` | 68 | 87 (arco para 88) | CT59 |
| `i` | 69 | 69 (arco para 70) | CT01 |
| `j` | 70 | 70 (arco para 71) | CT01 |
| `s` | 71 | 72 (arco para 73) | CT01 |
| `contaA` | 74 | 84 (arco para 85) | CT55 |
| `contaB` | 77 | 87 (arco para 88) | CT58 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `A` | 46 | 73 | CT01 |
| `B` | 46 | 76 | CT01 |
| `vx` | 46 | 85 | CT55 |
| `vy` | 47 | 88 | CT58 |
| `vra` | 48 | 90 | CT01 |
| `vma` | 49 | 90 | CT01 |
| `vrb` | 50 | 90 | CT01 |
| `vmb` | 51 | 90 | CT01 |
| `vm` | 52 | 66 | CT01 |
| `vm` | 52 | 90 | CT01 |
| `vm` | 52 | 57 | CT52 |
| `vn` | 53 | 66 | CT01 |
| `vn` | 53 | 90 | CT01 |
| `vn` | 53 | 61 | CT53 |
| `celulas` | 55 | 71 | CT01 |
| `celulas` | 55 | 61 | CT53 |
| `celulas` | 55 | 57 | CT52 |
| `i` | 59 | 59 | CT01 |
| `i` | 59 | 61 | CE06 |
| `i` | 59 | 59 | CT11 |
| `i` | 59 | 61 | CT53 |
| `grade` | 66 | 90 | CT01 |
| `grade` | 66 | 76 | CT01 |
| `grade` | 66 | 73 | CT01 |
| `contaA` | 67 | 85 | CE07 |
| `contaA` | 67 | 74 | CT01 |
| `contaB` | 68 | 88 | CT59 |
| `contaB` | 68 | 77 | CT01 |
| `i` | 69 | 69 | CT01 |
| `i` | 69 | 71 | CT01 |
| `i` | 69 | 79 | CT54 |
| `i` | 69 | 76 | CT01 |
| `i` | 69 | 76 | CT01 |
| `i` | 69 | 73 | CT01 |
| `i` | 69 | 73 | CT01 |
| `j` | 70 | 71 | CT01 |
| `j` | 70 | 70 | CT01 |
| `j` | 70 | 79 | CE04 |
| `j` | 70 | 76 | CT07 |
| `j` | 70 | 76 | CT07 |
| `j` | 70 | 73 | CT01 |
| `j` | 70 | 73 | CT01 |
| `s` | 71 | 79 | CT54 |
| `contaA` | 74 | 85 | CT55 |
| `contaA` | 74 | 74 | CT07 |
| `contaB` | 77 | 88 | CT58 |
| `contaB` | 77 | 77 | CT15 |
| `j` | 70 | 71 | CT01 |
| `j` | 70 | 70 | CT01 |
| `j` | 70 | 79 | CT54 |
| `j` | 70 | 76 | CT01 |
| `j` | 70 | 76 | CT01 |
| `j` | 70 | 73 | CT07 |
| `j` | 70 | 73 | CT07 |
| `i` | 69 | 69 | CT11 |
| `i` | 69 | 71 | CT11 |
| `i` | 69 | 79 | CE16 |
| `i` | 69 | 76 | CT11 |
| `i` | 69 | 76 | CT11 |
| `i` | 69 | 73 | CT21 |
| `i` | 69 | 73 | CT21 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `vx` | 46 | 84 | 85 | CT55 |
| `vx` | 46 | 84 | 87 | CT01 |
| `vy` | 47 | 87 | 88 | CT58 |
| `vy` | 47 | 87 | 90 | CT01 |
| `vm` | 52 | 56 | 57 | CT52 |
| `vm` | 52 | 56 | 59 | CT01 |
| `vm` | 52 | 59 | 60 | CT01 |
| `vm` | 52 | 59 | 66 | CT01 |
| `vm` | 52 | 69 | 70 | CT01 |
| `vm` | 52 | 69 | 84 | CT01 |
| `vn` | 53 | 70 | 71 | CT01 |
| `vn` | 53 | 70 | 69 | CT01 |
| `vn` | 53 | 60 | 61 | CT53 |
| `vn` | 53 | 60 | 59 | CT01 |
| `celulas` | 55 | 56 | 57 | CT52 |
| `celulas` | 55 | 56 | 59 | CT01 |
| `celulas` | 55 | 60 | 61 | CT53 |
| `celulas` | 55 | 60 | 59 | CT01 |
| `i` | 59 | 59 | 60 | CT01 |
| `i` | 59 | 59 | 66 | **inalcançável** (Apêndice A) |
| `i` | 59 | 60 | 61 | CE06 |
| `i` | 59 | 60 | 59 | CT01 |
| `i` | 59 | 59 | 60 | CT11 |
| `i` | 59 | 59 | 66 | CT01 |
| `i` | 59 | 60 | 61 | CT53 |
| `i` | 59 | 60 | 59 | CT11 |
| `contaA` | 67 | 84 | 85 | CE07 |
| `contaA` | 67 | 84 | 87 | **inalcançável** (Apêndice A) |
| `contaB` | 68 | 87 | 88 | CT59 |
| `contaB` | 68 | 87 | 90 | **inalcançável** (Apêndice A) |
| `i` | 69 | 69 | 70 | CT01 |
| `i` | 69 | 69 | 84 | **inalcançável** (Apêndice A) |
| `j` | 70 | 70 | 71 | CT01 |
| `j` | 70 | 70 | 69 | **inalcançável** (Apêndice A) |
| `s` | 71 | 72 | 73 | CT01 |
| `s` | 71 | 72 | 75 | CT01 |
| `s` | 71 | 75 | 76 | CT01 |
| `s` | 71 | 75 | 78 | CT01 |
| `s` | 71 | 78 | 79 | CT54 |
| `s` | 71 | 78 | 70 | CT01 |
| `contaA` | 74 | 84 | 85 | CT55 |
| `contaA` | 74 | 84 | 87 | CT01 |
| `contaB` | 77 | 87 | 88 | CT58 |
| `contaB` | 77 | 87 | 90 | CT01 |
| `j` | 70 | 70 | 71 | CT01 |
| `j` | 70 | 70 | 69 | CT01 |
| `i` | 69 | 69 | 70 | CT11 |
| `i` | 69 | 69 | 84 | CT01 |

#### 4.3.2 `Aquario.inteiroPositivo`

18 associações; todas cobertas.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `nome` | 95 | 109 | CE01 |
| `valor` | 95 | 109 | CE01 |
| `texto` | 95 | 96 (arco para 97) | CT01 |
| `numero` | 97 | 98 (arco para 99) | CT36 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `nome` | 95 | 109 | CE01 |
| `nome` | 95 | 107 | CT37 |
| `nome` | 95 | 102 | CE02 |
| `nome` | 95 | 99 | CT36 |
| `valor` | 95 | 109 | CE01 |
| `valor` | 95 | 107 | CT37 |
| `valor` | 95 | 102 | CE02 |
| `valor` | 95 | 99 | CT36 |
| `texto` | 95 | 97 | CT01 |
| `numero` | 97 | 104 | CT01 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `texto` | 95 | 96 | 97 | CT01 |
| `texto` | 95 | 96 | 106 | CT37 |
| `texto` | 95 | 106 | 107 | CT37 |
| `texto` | 95 | 106 | 109 | CE01 |
| `numero` | 97 | 98 | 99 | CT36 |
| `numero` | 97 | 98 | 101 | CT01 |
| `numero` | 97 | 101 | 102 | CE02 |
| `numero` | 97 | 101 | 104 | CT01 |

#### 4.3.3 `Aquario.lerMatriz`

25 associações, 24 cobertas, 1 inalcançáveis.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `texto` | 113 | 114 (arco para 114) | CE03 |
| `texto` | 115 | 117 | CE03 |
| `partes` | 117 | 119 (arco para 120) | CT01 |
| `celulas` | 118 | 123 | CT01 |
| `i` | 119 | 119 (arco para 120) | CT01 |
| `linha` | 120 | 121 (arco para 121) | CE05 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `texto` | 113 | 117 | CT01 |
| `texto` | 113 | 115 | CE03 |
| `texto` | 115 | 117 | CE03 |
| `partes` | 117 | 120 | CT01 |
| `celulas` | 118 | 123 | CT01 |
| `celulas` | 118 | 121 | CT01 |
| `i` | 119 | 120 | CT01 |
| `i` | 119 | 121 | CT01 |
| `i` | 119 | 119 | CT01 |
| `linha` | 120 | 121 | CT01 |
| `i` | 119 | 120 | CT11 |
| `i` | 119 | 121 | CT11 |
| `i` | 119 | 119 | CT11 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `texto` | 113 | 114 | 114 | CE03 |
| `texto` | 113 | 114 | 117 | CT01 |
| `texto` | 113 | 114 | 115 | CE03 |
| `texto` | 113 | 114 | 117 | CE04 |
| `partes` | 117 | 119 | 120 | CT01 |
| `partes` | 117 | 119 | 123 | CT01 |
| `i` | 119 | 119 | 120 | CT01 |
| `i` | 119 | 119 | 123 | **inalcançável** (Apêndice A) |
| `linha` | 120 | 121 | 121 | CE05 |
| `linha` | 120 | 121 | 121 | CT01 |
| `i` | 119 | 119 | 120 | CT11 |
| `i` | 119 | 119 | 123 | CT01 |

#### 4.3.4 `Aquario.iterar`

59 associações, 57 cobertas, 2 inalcançáveis. O XML lista 56; as demais envolvem a variável interna do laço for-each.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `this` | 132 | 133 (arco para 134) | CT01 |
| `this.linhas` | 132 | 133 (arco para 134) | CT01 |
| `this.colunas` | 132 | 134 (arco para 135) | CT01 |
| `this.grade` | 132 | 135 (arco para 136) | CT01 |
| `A` | 132 | 144 (arco para 145) | CT01 |
| `this.pontuacao` | 132 | 150 | CT01 |
| `ordem` | 132 | 140 | CT01 |
| `i` | 133 | 133 (arco para 134) | CT01 |
| `j` | 134 | 134 (arco para 135) | CT01 |
| `p` | 140 | 141 (arco para 142) | CT12 |
| `p.vivo` | 140 | 141 (arco para 142) | CT12 |
| `p.tipo` | 140 | 144 (arco para 145) | CT01 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `this` | 132 | 150 | CT01 |
| `this` | 132 | 147 | CT01 |
| `this` | 132 | 145 | CT01 |
| `this` | 132 | 136 | CT01 |
| `this.grade` | 132 | 136 | CT01 |
| `this.pontuacao` | 132 | 150 | CT01 |
| `ordem` | 132 | 140 | CT01 |
| `ordem` | 132 | 136 | CT01 |
| `i` | 133 | 133 | CT01 |
| `i` | 133 | 136 | CT01 |
| `j` | 134 | 134 | CT01 |
| `j` | 134 | 136 | CT01 |
| `j` | 134 | 134 | CT01 |
| `j` | 134 | 136 | CT01 |
| `i` | 133 | 133 | CT11 |
| `i` | 133 | 136 | CT11 |
| `p` | 140 | 147 | CT01 |
| `p` | 140 | 145 | CT01 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `this` | 132 | 133 | 134 | CT01 |
| `this` | 132 | 133 | 140 | CT01 |
| `this` | 132 | 134 | 135 | CT01 |
| `this` | 132 | 134 | 133 | CT01 |
| `this` | 132 | 135 | 136 | CT01 |
| `this` | 132 | 135 | 134 | CT01 |
| `this.linhas` | 132 | 133 | 134 | CT01 |
| `this.linhas` | 132 | 133 | 140 | CT01 |
| `this.colunas` | 132 | 134 | 135 | CT01 |
| `this.colunas` | 132 | 134 | 133 | CT01 |
| `this.grade` | 132 | 135 | 136 | CT01 |
| `this.grade` | 132 | 135 | 134 | CT01 |
| `A` | 132 | 144 | 145 | CT01 |
| `A` | 132 | 144 | 147 | CT01 |
| `i` | 133 | 133 | 134 | CT01 |
| `i` | 133 | 133 | 140 | **inalcançável** (Apêndice A) |
| `i` | 133 | 135 | 136 | CT01 |
| `i` | 133 | 135 | 134 | CT01 |
| `j` | 134 | 134 | 135 | CT01 |
| `j` | 134 | 134 | 133 | **inalcançável** (Apêndice A) |
| `j` | 134 | 135 | 136 | CT01 |
| `j` | 134 | 135 | 134 | CT04 |
| `j` | 134 | 134 | 135 | CT01 |
| `j` | 134 | 134 | 133 | CT01 |
| `j` | 134 | 135 | 136 | CT01 |
| `j` | 134 | 135 | 134 | CT01 |
| `i` | 133 | 133 | 134 | CT11 |
| `i` | 133 | 133 | 140 | CT01 |
| `i` | 133 | 135 | 136 | CT11 |
| `i` | 133 | 135 | 134 | CT11 |
| `p` | 140 | 141 | 142 | CT12 |
| `p` | 140 | 141 | 144 | CT01 |
| `p` | 140 | 144 | 145 | CT01 |
| `p` | 140 | 144 | 147 | CT01 |
| `p.vivo` | 140 | 141 | 142 | CT12 |
| `p.vivo` | 140 | 141 | 144 | CT01 |
| `p.tipo` | 140 | 144 | 145 | CT01 |
| `p.tipo` | 140 | 144 | 147 | CT01 |

#### 4.3.5 `Aquario.agirA`

37 associações, 35 cobertas, 2 inalcançáveis.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `this` | 155 | 164 (arco para 165) | CT07 |
| `p` | 155 | 161 | CT07 |
| `p.movimentos` | 155 | 158 | CT01 |
| `p.fome` | 155 | 162 | CT07 |
| `this.ma` | 155 | 164 (arco para 165) | CT07 |
| `this.ra` | 155 | 168 (arco para 171) | CT02 |
| `A` | 155 | 171 | CT02 |
| `livre` | 155 | 156 (arco para 157) | CT01 |
| `p.movimentos` | 158 | 168 (arco para 171) | CT02 |
| `p.fome` | 159 | 164 (arco para 168) | CT01 |
| `p.movimentos` | 161 | 168 (arco para 174) | CT08 |
| `p.fome` | 162 | 164 (arco para 165) | CT07 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `this` | 155 | 171 | CT02 |
| `this` | 155 | 165 | CT07 |
| `this` | 155 | 157 | CT01 |
| `p` | 155 | 161 | CT07 |
| `p` | 155 | 162 | CT07 |
| `p` | 155 | 171 | CT02 |
| `p` | 155 | 172 | CT02 |
| `p` | 155 | 165 | CT07 |
| `p` | 155 | 157 | CT01 |
| `p` | 155 | 158 | CT01 |
| `p` | 155 | 159 | CT01 |
| `p.movimentos` | 155 | 158 | CT01 |
| `p.fome` | 155 | 162 | CT07 |
| `A` | 155 | 171 | CT02 |
| `livre` | 155 | 157 | CT01 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `this` | 155 | 164 | 165 | CT07 |
| `this` | 155 | 164 | 168 | CT01 |
| `this` | 155 | 168 | 171 | CT02 |
| `this` | 155 | 168 | 174 | CT01 |
| `p` | 155 | 164 | 165 | CT07 |
| `p` | 155 | 164 | 168 | CT01 |
| `p` | 155 | 168 | 171 | CT02 |
| `p` | 155 | 168 | 174 | CT01 |
| `this.ma` | 155 | 164 | 165 | CT07 |
| `this.ma` | 155 | 164 | 168 | CT01 |
| `this.ra` | 155 | 168 | 171 | CT02 |
| `this.ra` | 155 | 168 | 174 | CT01 |
| `livre` | 155 | 156 | 157 | CT01 |
| `livre` | 155 | 156 | 161 | CT07 |
| `p.movimentos` | 158 | 168 | 171 | CT02 |
| `p.movimentos` | 158 | 168 | 174 | CT01 |
| `p.fome` | 159 | 164 | 165 | **inalcançável** (Apêndice A) |
| `p.fome` | 159 | 164 | 168 | CT01 |
| `p.movimentos` | 161 | 168 | 171 | **inalcançável** (Apêndice A) |
| `p.movimentos` | 161 | 168 | 174 | CT08 |
| `p.fome` | 162 | 164 | 165 | CT07 |
| `p.fome` | 162 | 164 | 168 | CT08 |

#### 4.3.6 `Aquario.agirB`

52 associações, 51 cobertas, 1 inalcançáveis.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `this` | 178 | 185 | CT01 |
| `p` | 178 | 185 | CT01 |
| `this.grade` | 178 | 180 | CT04 |
| `p.consumos` | 178 | 195 (arco para 195) | CT22 |
| `p.fome` | 178 | 189 | CT01 |
| `this.mb` | 178 | 191 (arco para 192) | CT05 |
| `this.rb` | 178 | 195 (arco para 195) | CT08 |
| `B` | 178 | 195 (arco para 199) | CT08 |
| `presa` | 178 | 179 (arco para 180) | CT04 |
| `p.consumos` | 182 | 195 (arco para 195) | CT08 |
| `p.fome` | 183 | 191 (arco para 195) | CT04 |
| `livre` | 185 | 186 (arco para 187) | CT01 |
| `p.fome` | 189 | 191 (arco para 192) | CT05 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `this` | 178 | 185 | CT01 |
| `this` | 178 | 199 | CT08 |
| `this` | 178 | 192 | CT05 |
| `this` | 178 | 187 | CT01 |
| `this` | 178 | 180 | CT04 |
| `this` | 178 | 181 | CT04 |
| `p` | 178 | 185 | CT01 |
| `p` | 178 | 189 | CT01 |
| `p` | 178 | 199 | CT08 |
| `p` | 178 | 200 | CT08 |
| `p` | 178 | 192 | CT05 |
| `p` | 178 | 187 | CT01 |
| `p` | 178 | 181 | CT04 |
| `p` | 178 | 182 | CT04 |
| `p` | 178 | 183 | CT04 |
| `this.grade` | 178 | 180 | CT04 |
| `p.consumos` | 178 | 182 | CT04 |
| `p.fome` | 178 | 189 | CT01 |
| `B` | 178 | 199 | CT08 |
| `presa` | 178 | 180 | CT04 |
| `presa` | 178 | 181 | CT04 |
| `livre` | 185 | 187 | CT01 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `this` | 178 | 191 | 192 | CT05 |
| `this` | 178 | 191 | 195 | CT01 |
| `this` | 178 | 195 | 195 | CT08 |
| `this` | 178 | 195 | 202 | CT01 |
| `this` | 178 | 195 | 199 | CT08 |
| `this` | 178 | 195 | 202 | CT15 |
| `p` | 178 | 191 | 192 | CT05 |
| `p` | 178 | 191 | 195 | CT01 |
| `p` | 178 | 195 | 195 | CT08 |
| `p` | 178 | 195 | 202 | CT01 |
| `p` | 178 | 195 | 199 | CT08 |
| `p` | 178 | 195 | 202 | CT15 |
| `p.consumos` | 178 | 195 | 195 | CT22 |
| `p.consumos` | 178 | 195 | 202 | CT01 |
| `this.mb` | 178 | 191 | 192 | CT05 |
| `this.mb` | 178 | 191 | 195 | CT01 |
| `this.rb` | 178 | 195 | 195 | CT08 |
| `this.rb` | 178 | 195 | 202 | CT01 |
| `B` | 178 | 195 | 199 | CT08 |
| `B` | 178 | 195 | 202 | CT15 |
| `presa` | 178 | 179 | 180 | CT04 |
| `presa` | 178 | 179 | 185 | CT01 |
| `p.consumos` | 182 | 195 | 195 | CT08 |
| `p.consumos` | 182 | 195 | 202 | CT04 |
| `p.fome` | 183 | 191 | 192 | **inalcançável** (Apêndice A) |
| `p.fome` | 183 | 191 | 195 | CT04 |
| `livre` | 185 | 186 | 187 | CT01 |
| `livre` | 185 | 186 | 189 | CT20 |
| `p.fome` | 189 | 191 | 192 | CT05 |
| `p.fome` | 189 | 191 | 195 | CT01 |

#### 4.3.7 `Aquario.vizinhoVazio`

31 associações, 30 cobertas, 1 inalcançáveis. O XML lista 20; as demais envolvem a variável interna do laço for-each.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `this` | 205 | 208 (arco para 208) | CT01 |
| `p` | 205 | 206 | CT01 |
| `p.linha` | 205 | 206 | CT01 |
| `p.coluna` | 205 | 207 | CT01 |
| `this.grade` | 205 | 208 (arco para 209) | CT01 |
| `i` | 206 | 208 (arco para 208) | CT01 |
| `j` | 207 | 208 (arco para 208) | CT01 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `p` | 205 | 206 | CT01 |
| `p` | 205 | 207 | CT01 |
| `p.linha` | 205 | 206 | CT01 |
| `p.coluna` | 205 | 207 | CT01 |
| `i` | 206 | 209 | CT01 |
| `j` | 207 | 209 | CT01 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `this` | 205 | 208 | 208 | CT01 |
| `this` | 205 | 208 | 205 | CT01 |
| `this` | 205 | 208 | 209 | CT01 |
| `this` | 205 | 208 | 205 | CT07 |
| `this.grade` | 205 | 208 | 209 | CT01 |
| `this.grade` | 205 | 208 | 205 | CT07 |
| `i` | 206 | 208 | 208 | CT01 |
| `i` | 206 | 208 | 205 | CT01 |
| `i` | 206 | 208 | 209 | CT01 |
| `i` | 206 | 208 | 205 | CT07 |
| `j` | 207 | 208 | 208 | CT01 |
| `j` | 207 | 208 | 205 | CT01 |
| `j` | 207 | 208 | 209 | CT01 |
| `j` | 207 | 208 | 205 | CT07 |

#### 4.3.8 `Aquario.vizinhoDoTipo`

41 associações, 40 cobertas, 1 inalcançáveis. O XML lista 30; as demais envolvem a variável interna do laço for-each.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `this` | 216 | 219 (arco para 219) | CT01 |
| `p` | 216 | 217 | CT01 |
| `tipo` | 216 | 219 (arco para 220) | CT04 |
| `p.linha` | 216 | 217 | CT01 |
| `p.coluna` | 216 | 218 | CT01 |
| `this.grade` | 216 | 219 (arco para 219) | CT04 |
| `i` | 217 | 219 (arco para 219) | CT01 |
| `j` | 218 | 219 (arco para 219) | CT01 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `p` | 216 | 217 | CT01 |
| `p` | 216 | 218 | CT01 |
| `p.linha` | 216 | 217 | CT01 |
| `p.coluna` | 216 | 218 | CT01 |
| `i` | 217 | 220 | CT04 |
| `j` | 218 | 220 | CT04 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `this` | 216 | 219 | 219 | CT01 |
| `this` | 216 | 219 | 216 | CT01 |
| `this` | 216 | 219 | 219 | CT04 |
| `this` | 216 | 219 | 216 | CT01 |
| `this` | 216 | 219 | 220 | CT04 |
| `this` | 216 | 219 | 216 | CT08 |
| `tipo` | 216 | 219 | 220 | CT04 |
| `tipo` | 216 | 219 | 216 | CT08 |
| `this.grade` | 216 | 219 | 219 | CT04 |
| `this.grade` | 216 | 219 | 216 | CT01 |
| `this.grade` | 216 | 219 | 220 | CT04 |
| `this.grade` | 216 | 219 | 216 | CT08 |
| `i` | 217 | 219 | 219 | CT01 |
| `i` | 217 | 219 | 216 | CT01 |
| `i` | 217 | 219 | 219 | CT04 |
| `i` | 217 | 219 | 216 | CT01 |
| `i` | 217 | 219 | 220 | CT04 |
| `i` | 217 | 219 | 216 | CT08 |
| `j` | 218 | 219 | 219 | CT01 |
| `j` | 218 | 219 | 216 | CT01 |
| `j` | 218 | 219 | 219 | CT04 |
| `j` | 218 | 219 | 216 | CT01 |
| `j` | 218 | 219 | 220 | CT04 |
| `j` | 218 | 219 | 216 | CT08 |

#### 4.3.9 `Aquario.dentro`

16 associações; todas cobertas.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `this` | 227 | 227 (arco para 227) | CT01 |
| `i` | 227 | 227 (arco para 227) | CT01 |
| `j` | 227 | 227 (arco para 227) | CT01 |
| `this.linhas` | 227 | 227 (arco para 227) | CT01 |
| `this.colunas` | 227 | 227 (arco para 227) | CT01 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `this` | 227 | 227 | 227 | CT01 |
| `this` | 227 | 227 | 227 | CT01 |
| `this` | 227 | 227 | 227 | CT01 |
| `this` | 227 | 227 | 227 | CT01 |
| `i` | 227 | 227 | 227 | CT01 |
| `i` | 227 | 227 | 227 | CT01 |
| `i` | 227 | 227 | 227 | CT01 |
| `i` | 227 | 227 | 227 | CT01 |
| `j` | 227 | 227 | 227 | CT01 |
| `j` | 227 | 227 | 227 | CT01 |
| `j` | 227 | 227 | 227 | CT01 |
| `j` | 227 | 227 | 227 | CT01 |
| `this.linhas` | 227 | 227 | 227 | CT01 |
| `this.linhas` | 227 | 227 | 227 | CT01 |
| `this.colunas` | 227 | 227 | 227 | CT01 |
| `this.colunas` | 227 | 227 | 227 | CT01 |

#### 4.3.10 `Aquario.temPeixeB`

46 associações, 44 cobertas, 2 inalcançáveis.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `this` | 248 | 248 (arco para 249) | CT01 |
| `this.linhas` | 248 | 248 (arco para 249) | CT01 |
| `this.colunas` | 248 | 249 (arco para 250) | CT01 |
| `this.grade` | 248 | 250 (arco para 250) | CT01 |
| `B` | 248 | 250 (arco para 251) | CT01 |
| `i` | 248 | 248 (arco para 249) | CT01 |
| `j` | 249 | 249 (arco para 250) | CT01 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `i` | 248 | 248 | CT05 |
| `j` | 249 | 249 | CT01 |
| `j` | 249 | 249 | CT01 |
| `i` | 248 | 248 | CT24 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `this` | 248 | 248 | 249 | CT01 |
| `this` | 248 | 248 | 255 | CT05 |
| `this` | 248 | 249 | 250 | CT01 |
| `this` | 248 | 249 | 248 | CT05 |
| `this` | 248 | 250 | 250 | CT01 |
| `this` | 248 | 250 | 249 | CT01 |
| `this` | 248 | 250 | 251 | CT01 |
| `this` | 248 | 250 | 249 | CT01 |
| `this.linhas` | 248 | 248 | 249 | CT01 |
| `this.linhas` | 248 | 248 | 255 | CT05 |
| `this.colunas` | 248 | 249 | 250 | CT01 |
| `this.colunas` | 248 | 249 | 248 | CT05 |
| `this.grade` | 248 | 250 | 250 | CT01 |
| `this.grade` | 248 | 250 | 249 | CT01 |
| `this.grade` | 248 | 250 | 251 | CT01 |
| `this.grade` | 248 | 250 | 249 | CT01 |
| `B` | 248 | 250 | 251 | CT01 |
| `B` | 248 | 250 | 249 | CT01 |
| `i` | 248 | 248 | 249 | CT01 |
| `i` | 248 | 248 | 255 | **inalcançável** (Apêndice A) |
| `i` | 248 | 250 | 250 | CT01 |
| `i` | 248 | 250 | 249 | CT01 |
| `i` | 248 | 250 | 251 | CT01 |
| `i` | 248 | 250 | 249 | CT01 |
| `j` | 249 | 249 | 250 | CT01 |
| `j` | 249 | 249 | 248 | **inalcançável** (Apêndice A) |
| `j` | 249 | 250 | 250 | CT02 |
| `j` | 249 | 250 | 249 | CT01 |
| `j` | 249 | 250 | 251 | CT08 |
| `j` | 249 | 250 | 249 | CT02 |
| `j` | 249 | 249 | 250 | CT01 |
| `j` | 249 | 249 | 248 | CT05 |
| `j` | 249 | 250 | 250 | CT01 |
| `j` | 249 | 250 | 249 | CT05 |
| `j` | 249 | 250 | 251 | CT01 |
| `j` | 249 | 250 | 249 | CT01 |
| `i` | 248 | 248 | 249 | CT11 |
| `i` | 248 | 248 | 255 | CT05 |
| `i` | 248 | 250 | 250 | CT11 |
| `i` | 248 | 250 | 249 | CT11 |
| `i` | 248 | 250 | 251 | CT11 |
| `i` | 248 | 250 | 249 | CT11 |

#### 4.3.11 `Aquario.getMatriz`

50 associações, 44 cobertas, 6 inalcançáveis.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `this` | 265 | 266 (arco para 267) | CT01 |
| `this.linhas` | 265 | 266 (arco para 267) | CT01 |
| `this.colunas` | 265 | 270 (arco para 271) | CT01 |
| `this.grade` | 265 | 274 (arco para 274) | CT01 |
| `sb` | 265 | 277 | CT01 |
| `i` | 266 | 266 (arco para 267) | CT01 |
| `j` | 270 | 270 (arco para 271) | CT01 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `this` | 265 | 274 | CT01 |
| `this.grade` | 265 | 274 | CT01 |
| `sb` | 265 | 277 | CT01 |
| `sb` | 265 | 274 | CT01 |
| `sb` | 265 | 272 | CT01 |
| `sb` | 265 | 268 | CT11 |
| `i` | 266 | 266 | CT01 |
| `i` | 266 | 274 | CT01 |
| `j` | 270 | 274 | CT01 |
| `j` | 270 | 270 | CT01 |
| `j` | 270 | 274 | CT01 |
| `j` | 270 | 270 | CT01 |
| `i` | 266 | 266 | CT11 |
| `i` | 266 | 274 | CT11 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `this` | 265 | 266 | 267 | CT01 |
| `this` | 265 | 266 | 277 | CT01 |
| `this` | 265 | 270 | 271 | CT01 |
| `this` | 265 | 270 | 266 | CT01 |
| `this` | 265 | 274 | 274 | CT01 |
| `this` | 265 | 274 | 274 | CT01 |
| `this.linhas` | 265 | 266 | 267 | CT01 |
| `this.linhas` | 265 | 266 | 277 | CT01 |
| `this.colunas` | 265 | 270 | 271 | CT01 |
| `this.colunas` | 265 | 270 | 266 | CT01 |
| `this.grade` | 265 | 274 | 274 | CT01 |
| `this.grade` | 265 | 274 | 274 | CT01 |
| `i` | 266 | 266 | 267 | CT01 |
| `i` | 266 | 266 | 277 | **inalcançável** (Apêndice A) |
| `i` | 266 | 267 | 268 | **inalcançável** (Apêndice A) |
| `i` | 266 | 267 | 270 | CT01 |
| `i` | 266 | 274 | 274 | CT01 |
| `i` | 266 | 274 | 274 | CT01 |
| `j` | 270 | 270 | 271 | CT01 |
| `j` | 270 | 270 | 266 | **inalcançável** (Apêndice A) |
| `j` | 270 | 271 | 272 | **inalcançável** (Apêndice A) |
| `j` | 270 | 271 | 274 | CT01 |
| `j` | 270 | 274 | 274 | CT01 |
| `j` | 270 | 274 | 274 | CT02 |
| `j` | 270 | 270 | 271 | CT01 |
| `j` | 270 | 270 | 266 | CT01 |
| `j` | 270 | 271 | 272 | CT01 |
| `j` | 270 | 271 | 274 | **inalcançável** (Apêndice A) |
| `j` | 270 | 274 | 274 | CT01 |
| `j` | 270 | 274 | 274 | CT01 |
| `i` | 266 | 266 | 267 | CT11 |
| `i` | 266 | 266 | 277 | CT01 |
| `i` | 266 | 267 | 268 | CT11 |
| `i` | 266 | 267 | 270 | **inalcançável** (Apêndice A) |
| `i` | 266 | 274 | 274 | CT11 |
| `i` | 266 | 274 | 274 | CT11 |

#### 4.3.12 `Jogo.executar`

11 associações; todas cobertas.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `aquario` | 23 | 25 (arco para 26) | CT05 |
| `entrada` | 23 | 29 (arco para 30) | CT01 |
| `saida` | 23 | 29 (arco para 30) | CT01 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `aquario` | 23 | 34 | CT01 |
| `aquario` | 23 | 35 | CT01 |
| `saida` | 23 | 30 | CT01 |
| `saida` | 23 | 34 | CT01 |
| `saida` | 23 | 26 | CT05 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `aquario` | 23 | 25 | 26 | CT05 |
| `aquario` | 23 | 25 | 29 | CT01 |
| `entrada` | 23 | 29 | 30 | CT01 |
| `entrada` | 23 | 29 | 23 | CT04 |
| `saida` | 23 | 29 | 30 | CT01 |
| `saida` | 23 | 29 | 23 | CT04 |

#### 4.3.13 `Jogo.lerOpcao`

9 associações; todas cobertas.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `saida` | 40 | 49 | CT25 |
| `linha` | 41 | 42 (arco para 43) | CE09 |
| `opcao` | 45 | 46 (arco para 46) | CT01 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `saida` | 40 | 49 | CT25 |
| `linha` | 41 | 45 | CT01 |
| `opcao` | 45 | 47 | CT01 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `linha` | 41 | 42 | 43 | CE09 |
| `linha` | 41 | 42 | 45 | CT01 |
| `opcao` | 45 | 46 | 46 | CT01 |
| `opcao` | 45 | 46 | 47 | CT04 |
| `opcao` | 45 | 46 | 47 | CT01 |
| `opcao` | 45 | 46 | 49 | CT25 |

#### 4.3.14 `Main.executar`

40 associações, 37 cobertas, 3 inalcançáveis.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `entrada` | 27 | 50 | CE10 |
| `saida` | 27 | 49 | CE10 |
| `PARAMETROS` | 27 | 28 (arco para 29) | CE10 |
| `valores` | 27 | 34 | CE10 |
| `k` | 28 | 28 (arco para 29) | CE10 |
| `linhas` | 34 | 39 (arco para 40) | CE10 |
| `matriz` | 38 | 47 | CE10 |
| `i` | 39 | 39 (arco para 40) | CE10 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `entrada` | 27 | 50 | CE10 |
| `entrada` | 27 | 44 | CE10 |
| `entrada` | 27 | 30 | CE10 |
| `saida` | 27 | 49 | CE10 |
| `saida` | 27 | 50 | CE10 |
| `saida` | 27 | 40 | CE10 |
| `saida` | 27 | 29 | CE10 |
| `PARAMETROS` | 27 | 29 | CE10 |
| `valores` | 27 | 34 | CE10 |
| `valores` | 27 | 47 | CE10 |
| `valores` | 27 | 30 | CE10 |
| `k` | 28 | 29 | CE10 |
| `k` | 28 | 30 | CE10 |
| `k` | 28 | 28 | CE10 |
| `k` | 28 | 29 | CE10 |
| `k` | 28 | 30 | CE10 |
| `k` | 28 | 28 | CE10 |
| `matriz` | 38 | 47 | CE10 |
| `matriz` | 38 | 44 | CE10 |
| `matriz` | 38 | 42 | CE15 |
| `i` | 39 | 40 | CE10 |
| `i` | 39 | 39 | CE10 |
| `i` | 39 | 40 | CE15 |
| `i` | 39 | 39 | CE15 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `PARAMETROS` | 27 | 28 | 29 | CE10 |
| `PARAMETROS` | 27 | 28 | 34 | CE10 |
| `k` | 28 | 28 | 29 | CE10 |
| `k` | 28 | 28 | 34 | **inalcançável** (Apêndice A) |
| `k` | 28 | 28 | 29 | CE10 |
| `k` | 28 | 28 | 34 | CE10 |
| `linhas` | 34 | 39 | 40 | CE10 |
| `linhas` | 34 | 39 | 47 | CE10 |
| `i` | 39 | 39 | 40 | CE10 |
| `i` | 39 | 39 | 47 | CE12 |
| `i` | 39 | 41 | 42 | **inalcançável** (Apêndice A) |
| `i` | 39 | 41 | 44 | CE10 |
| `i` | 39 | 39 | 40 | CE15 |
| `i` | 39 | 39 | 47 | CE10 |
| `i` | 39 | 41 | 42 | CE15 |
| `i` | 39 | 41 | 44 | **inalcançável** (Apêndice A) |

#### 4.3.15 `Main.lerLinha`

3 associações; todas cobertas.

**todas-as-definições**

| Variável | Definição (linha) | Uso exercitado (linha) | Caso |
|---|---|---|---|
| `linha` | 58 | 59 (arco para 59) | CE13 |

**todos-os-c-usos**

| Variável | Definição (linha) | Uso (linha) | Caso |
|---|---|---|---|
| `linha` | 58 | 59 | CE10 |

**todos-os-p-usos**

| Variável | Definição (linha) | Uso (linha) | Arco para a linha | Caso |
|---|---|---|---|---|
| `linha` | 58 | 59 | 59 | CE13 |
| `linha` | 58 | 59 | 59 | CE10 |

### 4.4 Casos de teste estruturais (TestSet-Estr)

Implementados em `test/aquario/TestSetEstr.java`. "Recusar" significa lançar `EntradaInvalidaException` sem iniciar a partida.

| ID | Condições de entrada | Saída esperada | Requisito estrutural | Saída obtida |
|---|---|---|---|---|
| CE01 | X="abc" | Recusar | `inteiroPositivo`, caminho C5 | Conforme esperado |
| CE02 | RA="99999999999" | Recusar | `inteiroPositivo`, caminho C2 | Conforme esperado |
| CE03 | Matriz "[A . . B]" (com colchetes) | Aceitar; matriz "[A . . B]" | `lerMatriz`, desvio dos colchetes | Conforme esperado |
| CE04 | Matriz "[A . . B" (sem fechar) | Recusar | `lerMatriz`, abre sem fechar | Conforme esperado |
| CE05 | M=2, N=2, matriz "A B;" | Recusar (linha vazia) | `lerMatriz`, linha vazia | Conforme esperado |
| CE06 | M=2, N=2, matriz "A; B ." | Recusar (1ª linha curta) | `inicializar`, par de `i` inicial | Conforme esperado |
| CE07 | M=1, N=3, X=1, Y=1, matriz "B . ." | Recusar (sem A) | `inicializar`, par de `contaA` = 0 | Conforme esperado |
| CE08 | M=2, N=2, matriz "? A; . B" | Recusar (símbolo em (1,1)) | `inicializar`, pares de `i` e `j` iniciais | Conforme esperado |
| CE09 | CT01 sem digitar nada | 1 iteração; encerra; pontuação 1 | `lerOpcao`, caminho C1 | Conforme esperado |
| CE10 | Console: CT01 seguido de C e E | Matriz inicial e 2 iterações; pontuação 2 | `Main.executar`, inicialização válida | Conforme esperado |
| CE11 | Console: X=0 | "Inicialização recusada" | `Main.executar`, captura da exceção | Conforme esperado |
| CE12 | Console: M="abc" | "Inicialização recusada: M=abc" | `Main.executar`, M não numérico | Conforme esperado |
| CE13 | Console: entrada termina após "1" | "Inicialização recusada" | `Main.lerLinha`, fim da entrada | Conforme esperado |
| CE14 | `Main.main` lendo "A B" com E | Iteração 1 "[B .]"; pontuação 1 | `Main.main` | Conforme esperado |
| CE15 | Console: M=2, N=2, matriz "B ." / ". A", E | Matriz inicial "[B .; . A]"; iteração "[. A; B .]" | `Main.executar`, matriz com várias linhas | Conforme esperado |
| CE16 | M=2, N=2, matriz "A B; ? ." | Recusar (símbolo em (2,1)) | `inicializar`, par de `i` incrementado | Conforme esperado |
| CE17 | RA="2.0" | Recusar | D13 (ciclo 2) | Conforme esperado após o ciclo 2b |
| CE18 | MA="1e1" | Recusar | D13; `inteiroPositivo`, caminho C5 | Conforme esperado após o ciclo 2b |
| CE19 | RB="+3" | Recusar | D13 | Conforme esperado após o ciclo 2b |

```java
/** CE17: inteiro escrito com casa decimal é recusado (decisão D13, revisão 1.2 da Parte I). */
@Test(expected = EntradaInvalidaException.class)
public void ce17ParametroComCasaDecimalZero() {
    Aquario.inicializar("1", "4", "1", "1", "2.0", "3", "3", "3", "A . . B");
}
```

*Listagem 2 — CE17, escrito e executado (falhando) antes da correção do código.*

### 4.5 Diário de execução

Cada ciclo registra o que foi escrito primeiro (teste), o resultado da execução, a mudança no código e o resultado final. Os commits do repositório seguem a mesma ordem.

| Ciclo | Testes escritos | Resultado dos testes novos | Mudança no código | Resultado final |
|---|---|---|---|---|
| 0.1 | nenhum (análise dos relatórios do JaCoCo e do BA-DUA com o TestSet-Func) | — | nenhuma | Requisitos não cobertos levantados: classe `Main` inteira (0%), parâmetro não numérico e grande demais, colchetes e linha vazia na matriz, primeira linha com tamanho errado, matriz sem A, símbolo inválido em (1,1) e fim da entrada na escolha |
| 0.2 | CE01–CE08 (validação da entrada e leitura da matriz), CE09 (fim da entrada na escolha) e CE10–CE14 (caminhos do `Main`) | — (escritos antes de qualquer execução) | nenhuma | — |
| 0.3 | nenhum (execução dos 14 casos novos com os 68 funcionais) | 14 passaram de primeira; nenhum defeito | nenhuma | 82/82 passam; JaCoCo 99,4% instruções, 97,6% desvios; BA-DUA 516/548 (94,2%) |
| 1 | CE15: matriz de duas linhas lida pelo `Main` (único desvio alcançável ainda não coberto); CE16: símbolo inválido na célula (2,1) (par definição-uso de `i` incrementado) | 2 passaram de primeira; nenhum defeito | nenhuma | 84/84 passam; JaCoCo 99,8% instruções, 98,4% desvios; BA-DUA 522/548 (95,3%) |
| 2a | CE17–CE19: "2.0", "1e1" e "+3" devem ser recusados (nova decisão D13: parâmetro aceito só se escrito apenas com dígitos) | **3 falharam** (vermelho): o código aceitava os três, convertendo para 2, 10 e 3 | — (commit só dos testes) | 84/87 passam |
| 2b | — | — | `Aquario.inteiroPositivo` passa a classificar pelo formato: só dígitos (depois positivo e tamanho), número decimal ("não é inteiro", classe I2) ou outro formato (recusado) | CE17–CE19 passam. **CE12 falhou**: conferia a mensagem literal "M=abc não é um número", que mudou. Como D03 não fixa mensagens literais, o defeito era do teste; o CE12 passou a verificar só a recusa de M. Depois disso, 87/87 passam; JaCoCo 99,8% instruções, 98,4% desvios; BA-DUA 529/555 (95,3%) |
| 3 | nenhum (refatoração: os testes existentes precisam continuar verdes) | — | Removidas as verificações "não há célula para o filho" em `agirA` e `agirB`, que nunca podem ser verdadeiras pelas restrições R(C3, C4) e I(C9, C10) da revisão 1.1 da Parte I; o motivo ficou em comentário no código | 87/87 passam; JaCoCo 100% desvios, 99,8% instruções; BA-DUA 523/547 (95,6%) |
| 4 | nenhum (refatoração) | — | Construtor privado em `Main`, que o JaCoCo não conta como método a cobrir (antes, o construtor implícito aparecia como método não coberto) | 87/87 passam; JaCoCo 100% em instruções, desvios, linhas e métodos; BA-DUA 523/547 (95,6%) |

**Correção de medição.** Os percentuais do BA-DUA registrados inicialmente estavam inflados: o script de resumo contava o texto `covered="1"` no XML, que também aparece nos totalizadores por método. A contagem passou a somar o relatório de texto do BA-DUA (`badua.txt`), que é a contagem oficial da ferramenta, e todos os valores acima foram recalculados executando o script corrigido em cada commit. O XML não serve para contar porque omite as 25 associações das variáveis que o compilador cria para os laços for-each, que não têm nome no código-fonte.

### 4.6 Cobertura final e requisitos inalcançáveis

| Ferramenta | Critério | TestSet-Func | TestSet-Func + TestSet-Estr |
|---|---|---|---|
| JaCoCo (EclEmma) | Instruções | 81,1% | **100%** (1264/1264) |
| JaCoCo (EclEmma) | Desvios | 87,7% | **100%** (122/122) |
| JaCoCo (EclEmma) | Linhas | 85,1% | **100%** (201/201) |
| JaCoCo (EclEmma) | Métodos | 22/26 | **100%** (26/26) |
| BA-DUA (Baduíno) | Pares definição-uso | 84,8% | **95,6%** (523/547) |

![Relatório do JaCoCo, TestSet-Func + TestSet-Estr](figuras/jacoco-todos.png)

*Figura 8 — Relatório HTML do JaCoCo com os dois conjuntos (linha de comando).*

**[PRINT DO ECLIPSE: JUnit com TestSetFunc e TestSetEstr — 87/87 passando]**
**[PRINT DO ECLIPSE: view Coverage do EclEmma com os dois conjuntos]**
**[PRINT DO ECLIPSE: view do Baduíno com os dois conjuntos]**

**Cobertura por classe — TestSet-Func + TestSet-Estr**

**[PRINT DO ECLIPSE: código de Aquario.java colorido pelo EclEmma, TestSetFunc + TestSetEstr]**
**[PRINT DO ECLIPSE: código de Jogo.java colorido pelo EclEmma, TestSetFunc + TestSetEstr]**
**[PRINT DO ECLIPSE: código de Main.java colorido pelo EclEmma, TestSetFunc + TestSetEstr]**
**[PRINT DO ECLIPSE: código de Peixe.java colorido pelo EclEmma, TestSetFunc + TestSetEstr]**
**[PRINT DO ECLIPSE: código de Tipo.java colorido pelo EclEmma, TestSetFunc + TestSetEstr]**
**[PRINT DO ECLIPSE: código de EntradaInvalidaException.java colorido pelo EclEmma, TestSetFunc + TestSetEstr]**
**[PRINT DO ECLIPSE: view do Baduíno com os métodos de Aquario expandidos, TestSetFunc + TestSetEstr]**
**[PRINT DO ECLIPSE: view do Baduíno com os métodos de Jogo expandidos, TestSetFunc + TestSetEstr]**
**[PRINT DO ECLIPSE: view do Baduíno com os métodos de Main expandidos, TestSetFunc + TestSetEstr]**

Os 24 pares definição-uso não cobertos são inalcançáveis e estão justificados um a um em `docs/pares-inalcancaveis.md`:

| Grupo | Pares | Motivo |
|---|---|---|
| Laços que sempre dão pelo menos uma volta | 13 | M, N ≥ 1; `DIRECOES` tem 4 elementos; `PARAMETROS` tem 8 |
| Contagem zero igual à declaração | 2 | X, Y ≥ 1: uma matriz sem A ou sem B é sempre recusada |
| Combinações proibidas pelas regras | 3 | mover zera a fome de A; o bloqueio zera os movimentos; comer zera a fome de B |
| Primeira volta contra as demais | 6 | `i > 0` é sempre falso na primeira volta e sempre verdadeiro nas outras |

Descontados os inalcançáveis, a cobertura dos pares alcançáveis é de 100% (523/523). Não se reescreveram os laços para que a ferramenta deixasse de enxergar esses pares, pois isso mudaria o código sem benefício de comportamento.

### 4.7 Defeitos encontrados

| Fase | Onde | Defeito | Como apareceu | Correção |
|---|---|---|---|---|
| II-A | — | nenhum | 68/68 casos funcionais passaram | — |
| II-B | `Aquario.inteiroPositivo` | Parâmetros "2.0", "1e1" e "+3" eram aceitos como inteiros | CE17–CE19, escritos antes da mudança (ciclo 2a) | Validação pelo formato (ciclo 2b) |
| II-B | Teste CE12 | Conferia uma mensagem literal, contrariando D03 | Falhou quando a mensagem mudou (ciclo 2b) | Passou a verificar só a recusa |

## 5. Análise pessoal: eficiência e eficácia das técnicas

**[A PREENCHER PELO GRUPO: análise pessoal sobre a eficiência e a eficácia das técnicas de Teste Funcional e Estrutural para encontrar defeitos no programa]**

## 6. Pergunta ao grupo: influência dos casos funcionais (TDD)

> "Ao desenvolver o programa, vocês sentiram que foram influenciados por terem criado os casos de teste funcionais primeiro (ou seja, aplicando o desenvolvimento dirigido por teste – do inglês – Test-Driven Development - TDD)? Se sim, como isso refletiu no código?"

**[A PREENCHER PELO GRUPO: resposta à pergunta]**

## 7. Conclusão

A implementação passou nos 68 casos funcionais da Parte I sem defeitos. A aplicação dos critérios estruturais, feita manualmente nos métodos principais e conferida pelas ferramentas, levou a 19 casos novos, a 100% em todos os critérios de fluxo de controle do JaCoCo e a 100% dos pares definição-uso alcançáveis do BA-DUA. Os 24 pares restantes são inalcançáveis e justificados. O trabalho estrutural encontrou um defeito de validação de entrada e um teste mal especificado, ambos corrigidos em ciclos registrados, com os testes escritos antes das mudanças.

## 8. Referências

- OLIVEIRA, A. L. DCC168 Teste de Software: especificação do trabalho final. UFJF, 2026-3.
- Documento da Parte I — Teste Funcional do Jogo do Aquário, revisões 1.0, 1.1 e 1.2.
- JaCoCo — Java Code Coverage Library. https://www.jacoco.org/jacoco/
- EclEmma — Java Code Coverage for Eclipse. https://www.eclemma.org/
- BA-DUA — Data-flow testing coverage for Java. https://github.com/saeg/ba-dua
- Baduíno — BA-DUA plug-in for Eclipse. https://github.com/saeg/baduino
- JUnit 4. https://junit.org/junit4/
- McCABE, T. J. A complexity measure. IEEE Transactions on Software Engineering, SE-2(4), p. 308–320, 1976.
- RAPPS, S.; WEYUKER, E. J. Selecting software test data using data flow information. IEEE Transactions on Software Engineering, SE-11(4), p. 367–375, 1985.
