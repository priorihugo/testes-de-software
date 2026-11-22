# Relatório da Parte II — Teste Estrutural do Jogo do Aquário

DCC168 Teste de Software — UFJF, 2026-3. Rascunho mantido junto com o código: cada ciclo de trabalho atualiza este arquivo no mesmo commit, para que o histórico do repositório mostre a ordem real (teste antes do código).

## 1. Implementação

- `Aquario`: validação da entrada e regras dos peixes, seguindo as decisões D01–D11 da Parte I (revisão 1.1).
- `Jogo`: iterações, opção C/E do jogador e pontuação. Recebe entrada e saída como parâmetros, o que permite automatizar os casos de controle.
- `Main`: interface de console.

A implementação foi escrita em 22/11/2026, depois dos casos de teste da Parte I (entregues em 13/10/2026).

## 2. Parte II-A — TestSet-Func

Os 68 casos da Tabela 2 da Parte I foram transcritos para `TestSetFunc.java` por um gerador que lê a tabela do documento, sem casos adicionais.

| Execução | Testes | Falhas | Defeitos |
|---|---|---|---|
| 22/11/2026 19:36 | 68 | 0 | nenhum |

Cobertura obtida só com o TestSet-Func (linha de comando; os números oficiais do EclEmma e do Baduíno entram após a rodada no Eclipse):

| Ferramenta | Critério | Coberto | Total | % |
|---|---|---|---|---|
| JaCoCo (EclEmma) | Instruções | 1027 | 1271 | 80,8 |
| JaCoCo (EclEmma) | Desvios | 109 | 124 | 87,9 |
| JaCoCo (EclEmma) | Linhas | 173 | 206 | 84,0 |
| BA-DUA (Baduíno) | Pares definição-uso | 461 | 523 | 88,1 |

O principal ponto não coberto é a classe `Main` (leitura do console), que nenhum caso funcional exercita.

## 3. Parte II-B — TestSet-Estr

### 3.1 Diário de execução

Cada ciclo registra o que foi escrito primeiro (teste), o resultado da execução, a mudança no código (se houve) e o resultado final.

| Ciclo | Data | Testes escritos | Resultado dos testes novos | Mudança no código | Resultado final |
|---|---|---|---|---|---|
| 0 | 22/11/2026 19:53 | CE01–CE14: parâmetro não numérico e grande demais, colchetes na matriz, linha vazia, primeira linha com tamanho errado, matriz sem A, símbolo inválido em (1,1), fim da entrada na escolha, quatro caminhos do `Main` | 14 passaram de primeira; nenhum defeito | nenhuma | 82/82 passam; JaCoCo 99,4% instruções, 97,6% desvios; BA-DUA 97,7% |

No ciclo 0, também o comentário do CT20 em `TestSetFunc.java` foi alinhado à revisão 1.1 da Parte I (V32 e V36 inalcançáveis); nenhuma entrada ou saída esperada mudou.

## 4. Pendências deste rascunho

- Números oficiais do EclEmma e do Baduíno (rodada no Eclipse).
- Requisitos estruturais inalcançáveis, com justificativa.
- Análise pessoal e resposta sobre TDD (a cargo do grupo).
