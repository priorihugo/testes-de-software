# Prévias das imagens das ferramentas

Imagens geradas pela linha de comando (`scripts/cobertura.sh` e o relatório de testes do JUnit), só para ter uma ideia do resultado antes da rodada no Eclipse. **Não substituem os prints do Eclipse** pedidos na especificação: cada uma traz a faixa "Prévia gerada pela linha de comando".

| Arquivo | Conteúdo | Equivale a |
|---|---|---|
| `junit-func.png` | 68/68 testes do TestSetFunc | view JUnit do Eclipse |
| `junit-todos.png` | 87/87 testes (TestSetFunc + TestSetEstr) | view JUnit do Eclipse |
| `jacoco-func.png` | cobertura do JaCoCo só com o TestSetFunc | view Coverage do EclEmma |
| `jacoco-func-Main.png` | código do `Main` em vermelho: nenhum caso funcional passa pelo console | editor com cores do EclEmma |
| `jacoco-todos.png` | 100% com os dois conjuntos | view Coverage do EclEmma |
| `badua-func.png` | pares definição-uso por método, só TestSetFunc (464/547) | view do Baduíno |
| `badua-todos.png` | pares definição-uso por método, os dois conjuntos (523/547) | view do Baduíno |

O EclEmma usa o mesmo motor (JaCoCo), então os números devem coincidir. O Baduíno embute outra versão do BA-DUA e pode contar os pares de outro jeito; se os números diferirem, valem os do Eclipse.
