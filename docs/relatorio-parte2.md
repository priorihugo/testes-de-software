# Relatório da Parte II — Teste Estrutural do Jogo do Aquário

DCC168 Teste de Software — UFJF, 2026-3. Rascunho mantido junto com o código: cada ciclo de trabalho atualiza este arquivo no mesmo commit, para que o histórico do repositório mostre a ordem real (teste antes do código).

## 1. Implementação

- `Aquario`: validação da entrada e regras dos peixes, seguindo as decisões D01–D11 da Parte I (revisão 1.1) e a nova decisão D13 (ciclo 2).
- `Jogo`: iterações, opção C/E do jogador e pontuação. Recebe entrada e saída como parâmetros, o que permite automatizar os casos de controle.
- `Main`: interface de console.

A implementação foi escrita em 22/11/2026, depois dos casos de teste da Parte I (entregues em 13/10/2026).

## 2. Parte II-A — TestSet-Func

Os 68 casos da Tabela 2 da Parte I foram transcritos para `TestSetFunc.java` por um gerador que lê a tabela do documento, sem casos adicionais.

| Execução | Testes | Falhas | Defeitos |
|---|---|---|---|
| 22/11/2026 19:36 (commit `8409274`) | 68 | 0 | nenhum |

Cobertura obtida só com o TestSet-Func (linha de comando; os números oficiais do EclEmma e do Baduíno entram após a rodada no Eclipse):

| Ferramenta | Critério | Coberto | Total | % |
|---|---|---|---|---|
| JaCoCo (EclEmma) | Instruções | 1027 | 1271 | 80,8 |
| JaCoCo (EclEmma) | Desvios | 109 | 124 | 87,9 |
| JaCoCo (EclEmma) | Linhas | 173 | 206 | 84,0 |
| BA-DUA (Baduíno) | Pares definição-uso | 469 | 548 | 85,6 |

O principal ponto não coberto é a classe `Main` (leitura do console), que nenhum caso funcional exercita.

## 3. Parte II-B — TestSet-Estr

### 3.1 Diário de execução

Cada ciclo registra o que foi escrito primeiro (teste), o resultado da execução, a mudança no código (se houve) e o resultado final.

| Ciclo | Data | Testes escritos | Resultado dos testes novos | Mudança no código | Resultado final |
|---|---|---|---|---|---|
| 0 | 22/11/2026 19:53 | CE01–CE14: parâmetro não numérico e grande demais, colchetes na matriz, linha vazia, primeira linha com tamanho errado, matriz sem A, símbolo inválido em (1,1), fim da entrada na escolha, quatro caminhos do `Main` | 14 passaram de primeira; nenhum defeito | nenhuma | 82/82 passam; JaCoCo 99,4% instruções, 97,6% desvios; BA-DUA 516/548 (94,2%) |
| 1 | 22/11/2026 19:56 | CE15: matriz de duas linhas lida pelo `Main` (único desvio alcançável ainda não coberto); CE16: símbolo inválido na célula (2,1) (par definição-uso de `i` incrementado) | 2 passaram de primeira; nenhum defeito | nenhuma | 84/84 passam; JaCoCo 99,8% instruções, 98,4% desvios; BA-DUA 522/548 (95,3%) |
| 2a | 22/11/2026 19:58 | CE17–CE19: "2.0", "1e1" e "+3" devem ser recusados (nova decisão D13: parâmetro aceito só se escrito apenas com dígitos) | **3 falharam** (vermelho): o código aceitava os três, convertendo para 2, 10 e 3 | — (commit só dos testes) | 84/87 passam |
| 2b | 22/11/2026 20:01 | — | — | `Aquario.inteiroPositivo` passa a classificar pelo formato: só dígitos (depois positivo e tamanho), número decimal ("não é inteiro", classe I2) ou outro formato (recusado) | CE17–CE19 passam. **CE12 falhou**: conferia a mensagem literal "M=abc não é um número", que mudou. Como D03 não fixa mensagens literais, o defeito era do teste; o CE12 passou a verificar só a recusa de M. Depois disso, 87/87 passam; JaCoCo 99,8% instruções, 98,4% desvios; BA-DUA 529/555 (95,3%) |
| 3 | 22/11/2026 20:03 | nenhum (refatoração: os testes existentes precisam continuar verdes) | — | Removidas as verificações "não há célula para o filho" em `agirA` e `agirB`, que nunca podem ser verdadeiras pelas restrições R(C3, C4) e I(C9, C10) da revisão 1.1 da Parte I; o motivo ficou em comentário no código | 87/87 passam; JaCoCo 100% desvios, 99,8% instruções; BA-DUA 523/547 (95,6%) |
| 4 | 22/11/2026 20:05 | nenhum (refatoração) | — | Construtor privado em `Main`, que o JaCoCo não conta como método a cobrir (antes, o construtor implícito aparecia como método não coberto) | 87/87 passam; JaCoCo 100% em instruções, desvios, linhas e métodos; BA-DUA 523/547 (95,6%) |

**Correção de medição (22/11/2026 20:08).** Os percentuais do BA-DUA registrados antes desta data estavam inflados: o script de resumo contava o texto `covered="1"` no XML, que também aparece nos totalizadores por método. A contagem passou a somar o relatório de texto do BA-DUA (`badua.txt`), que é a contagem oficial da ferramenta, e todos os valores acima foram recalculados executando o script corrigido em cada commit. O XML não serve para contar porque agrupa pares que caem na mesma linha e omite variáveis sem nome no código.

### 3.2 Cobertura final

| Ferramenta | Critério | Coberto | Total | % |
|---|---|---|---|---|
| JaCoCo (EclEmma) | Instruções | 1264 | 1264 | 100 |
| JaCoCo (EclEmma) | Desvios | 122 | 122 | 100 |
| JaCoCo (EclEmma) | Linhas | 201 | 201 | 100 |
| JaCoCo (EclEmma) | Métodos | 26 | 26 | 100 |
| BA-DUA (Baduíno) | Pares definição-uso | 523 | 547 | 95,6 |

Os 24 pares não cobertos são inalcançáveis; a justificativa de cada um está em `docs/pares-inalcancaveis.md`. Descontados eles, a cobertura dos pares alcançáveis é de 100% (523/523).

### 3.3 Defeitos encontrados

| Onde | Defeito | Como apareceu | Correção |
|---|---|---|---|
| Implementação | Parâmetros como "2.0", "1e1" e "+3" eram aceitos como inteiros | Testes CE17–CE19 escritos antes da mudança (ciclo 2a) | Validação pelo formato (ciclo 2b) |
| Teste CE12 | Conferia uma mensagem literal, contrariando D03 | Falhou ao mudar a mensagem no ciclo 2b | Passou a verificar só a recusa |

Os 68 casos funcionais não revelaram defeitos na II-A.

## 4. Pendências deste rascunho

- Números oficiais do EclEmma e do Baduíno (rodada no Eclipse).
- Revisão 1.2 da Parte I com as decisões novas (D12–D15) e a coluna Saída Obtida.
- Análise pessoal e resposta sobre TDD (a cargo do grupo).
