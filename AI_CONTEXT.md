# Diretrizes de Desenvolvimento - Projeto Rede Ipê (Teoria dos Grafos)

## 1. Papel e Contexto

O objetivo do projeto é modelar a "Rede Ipê" da RNP como um grafo ponderado e não direcionado para otimizar conexões, aplicar árvores geradoras mínimas (Kruskal e Prim) e coloração de grafos.

O foco aqui não é apenas fazer funcionar, mas garantir profundidade teórica. O código deve ser acompanhado de documentação clara sobre a complexidade algorítmica e a justificativa das estruturas de dados utilizadas, facilitando o entendimento de toda a equipe de desenvolvimento (Rafael, João, Ian, Kauã e Gabriel).

## 2. Stack Tecnológica Obrigatória

- **Linguagem:** Java 17
- **Gerenciador de Dependências:** Maven (`pom.xml`).
- **Core de Grafos:** Biblioteca `JGraphT` (uso de `SimpleWeightedGraph`, algoritmos nativos de MST e Coloração).
- **APIs HTTP:** Uso estrito do `java.net.http.HttpClient` nativo do Java para requisições.
- **Integração de Roteamento:** OpenStreetMap (Nominatim para Geocoding e OSRM para distâncias reais).
- **Serialização/JSON:** `Jackson` (ou `Gson`).

## 3. Padrões de Código e Arquitetura

Ao gerar ou modificar código, você DEVE obedecer às seguintes regras:

## 3. Padrões de Código e Arquitetura (Padrão MVC Simplificado)

O projeto rejeita a estrutura tradicional e burocrática de pacotes do Java Enterprise (como `src/main/java/br/com/...`). Você DEVE utilizar uma estrutura de pastas MVC simples, direta na raiz do `src`.

A estrutura exata deve ser:

- `src/model/`: Classes de domínio (ex: `Conexao` usando `record`) e a classe wrapper do grafo (`GrafoRede.java`).
- `src/controller/`: Lógica algorítmica (Kruskal, Prim, Coloração) e integrações externas (ex: `OSMClient.java` para buscar distâncias na API).
- `src/view/`: Ponto de entrada da aplicação (`Main.java`) e classes de exportação visual.

**Regra do Maven:** Para que o Maven aceite essa estrutura simplificada, você deve obrigatoriamente incluir a tag `<sourceDirectory>src</sourceDirectory>` dentro da tag `<build>` no `pom.xml` gerado.

**Outras Regras:**

1. **Nomenclatura:** Classes, métodos e variáveis em Português, mantendo termos técnicos universais em Inglês (ex: `HttpClient`, `JSON`).
2. **Modularidade:** Mantenha as responsabilidades estritas ao seu pacote MVC. O Controller não deve guardar estado do grafo, e o Model não deve fazer requisições HTTP.
3. **Tratamento de Exceções:** Ao lidar com a API do OpenStreetMap, trate timeouts e falhas de conexão de forma resiliente.

## 4. Profundidade Teórica e Comentários

Como se trata de um trabalho de Ciência da Computação:

- Sempre que implementar ou chamar um algoritmo do `JGraphT` (como Kruskal, Prim ou Coloração Gulosa), adicione um comentário Javadoc explicando brevemente a complexidade de tempo/espaço (ex: $O(E \log V)$) daquela operação no contexto do código.
- Identifique claramente as propriedades do grafo no código (simples, conexo, planar, etc.).

## 5. Instruções de Saída (Output)

- Entregue o código pronto para compilação.
- Não remova imports ou lógicas existentes ao sugerir modificações em arquivos que já foram criados. Forneça o contexto exato de onde o novo código deve ser inserido.
- Evite blocos de texto excessivos fora do código. Seja direto ao ponto, priorizando a qualidade da solução técnica.
