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

1. **Uso de Records:** Utilize `record` nativo do Java para modelos de dados imutáveis (ex: DTOs, representações da planilha).
2. **Nomenclatura:** Classes de domínio, métodos e variáveis devem ser nomeados em Português (ex: `Conexao`, `obterDistanciaReal`), mantendo os termos técnicos universais em Inglês onde fizer sentido (ex: `HttpClient`, `API`, `JSON`).
3. **Modularidade:** O código deve ser altamente modular (separação clara entre ingestão de dados/parser, chamadas de API, estruturação do grafo e execução de algoritmos) para que diferentes membros da equipe possam trabalhar em paralelo sem gerar conflitos de merge.
4. **Tratamento de Exceções:** Ao lidar com o `HttpClient` e APIs externas, trate timeouts e falhas de conexão de forma resiliente, evite engolir exceções genéricas. Inclua logs básicos (ex: `System.out.println` ou `Logger`).

## 4. Profundidade Teórica e Comentários

Como se trata de um trabalho de Ciência da Computação:

- Sempre que implementar ou chamar um algoritmo do `JGraphT` (como Kruskal, Prim ou Coloração Gulosa), adicione um comentário Javadoc explicando brevemente a complexidade de tempo/espaço (ex: $O(E \log V)$) daquela operação no contexto do código.
- Identifique claramente as propriedades do grafo no código (simples, conexo, planar, etc.).

## 5. Instruções de Saída (Output)

- Entregue o código pronto para compilação.
- Não remova imports ou lógicas existentes ao sugerir modificações em arquivos que já foram criados. Forneça o contexto exato de onde o novo código deve ser inserido.
- Evite blocos de texto excessivos fora do código. Seja direto ao ponto, priorizando a qualidade da solução técnica.
