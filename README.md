# clojure-capital-gains

Projeto criado para aprender clojure, o principal objetivo deste projeto é calcular ganhos baseado em ações fakes e impostos relacionados a operações de compra e venda de ações e retornar para cada operação o valor de imposto cobrado.

## Arquitetura

O projeto segue uma arquitetura modular e organizada em camadas:

- **Controller**: Responsável por interagir com o mundo externo, como leitura de arquivos e saída de dados em formato JSON.
- **Service**: Contém a lógica de negócios principal, como o cálculo de impostos e manipulação de operações.
- **Domain**: Define as regras de domínio, como cálculos específicos para operações de compra e venda, além de estados e tipos de operações.

## Estrutura de Pastas

- `src/clojure_capital_gains/controller`: Contém os controladores para leitura de arquivos e saída de dados.
- `src/clojure_capital_gains/service`: Implementa a lógica de negócios, como o cálculo de impostos.
- `src/clojure_capital_gains/domain`: Define as regras de domínio, como cálculos de lucro, prejuízo e impostos.
- `resources`: Contém arquivos de entrada, como `test-operations.txt`.

## Como Rodar

1. Certifique-se de ter o [Leiningen](https://leiningen.org/) instalado.
2. Clone este repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd clojure-capital-gains
   ```
3. Execute o projeto:
   ```bash
   lein run
   ```
4. O resultado será exibido no console em formato JSON.

## Exemplo de Saída

O programa exibirá algo como:
```json
[{"tax": 0.0}, {"tax": 50.0}]
```
