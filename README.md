# Jogo do Aquário — DCC168 Teste de Software (UFJF, 2026-3)

Implementação em Java 8 do Jogo do Aquário e dos testes JUnit 4 do trabalho final.

## Estrutura

- `src/aquario/` — implementação
  - `Aquario` — validação da entrada e regras dos peixes (decisões D01–D11 da Parte I)
  - `Jogo` — iterações, opção C/E do jogador e pontuação
  - `Main` — interface de console
- `test/aquario/TestSetFunc.java` — os 68 casos funcionais da Parte I (Parte II-A), um método por caso (`ct01` … `ct69`)
- `scripts/cobertura.sh` — compila, roda os testes e gera os relatórios do JaCoCo (EclEmma) e do BA-DUA (Baduíno)
- `relatorios/` — relatórios gerados pelo script

## Eclipse

File → Import → Existing Projects into Workspace, selecione esta pasta. O projeto usa JavaSE-1.8 e a biblioteca JUnit 4 do Eclipse.

## Linha de comando

```bash
scripts/cobertura.sh func    # só TestSet-Func
scripts/cobertura.sh todos   # TestSet-Func + TestSet-Estr
```

A variável `TOOLS` deve apontar para uma pasta com `jdk8/`, `jacoco/`, `badua/` e `lib/` (JUnit 4.13.2 e Hamcrest 1.3).
