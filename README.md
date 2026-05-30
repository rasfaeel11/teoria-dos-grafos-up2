# 🌐 Mapeamento da Rede Ipê (RNP) - Teoria dos Grafos

Este repositório contém o projeto prático da disciplina de **Teoria dos Grafos** do **Centro Universitário Tiradentes (UNIT)**. O objetivo central é modelar a **Rede Ipê** (rede nacional de ensino e pesquisa da RNP) utilizando grafos ponderados e não direcionados para analisar conectividade, minimizar custos de cabeamento e reduzir interferências entre pontos de acesso.

## 👥 Equipe
* Rafael
* Caua
* Victor Enrico
* Victor Batista

---

## 🛠️ Stack Tecnológica e Motivação

A arquitetura do projeto foi desenhada para priorizar eficiência algorítmica e boas práticas de Engenharia de Software, fugindo de implementações manuais propensas a erro e utilizando ferramentas consolidadas no mercado.

* **Java 17+:** Escolhido como linguagem base por sua robustez e forte tipagem, utilizando recursos modernos como `records` para garantir imutabilidade nos modelos de dados.
* **Maven:** Gerenciador de dependências e ciclo de vida. Configuramos um `pom.xml` enxuto que permite uma arquitetura MVC direta na pasta `src/`, sem a verbosidade corporativa padrão do Java.
* **JGraphT (v1.5.2):** O coração matemático do projeto. Em vez de reinventar a roda construindo matrizes de adjacência do zero, utilizamos esta biblioteca que é padrão ouro em Java para Teoria dos Grafos. Ela nos fornece implementações nativas e otimizadas ($O(E \log V)$) para Árvores Geradoras Mínimas e Coloração.
* **API OpenStreetMap (Nominatim & OSRM):** Para tornar a análise o mais próxima possível do mundo real, consumimos essas APIs via `java.net.http.HttpClient` nativo para buscar coordenadas e calcular a distância terrestre real em quilômetros entre os estados, definindo o peso exato das arestas.
* **Jackson Databind:** Utilizado para a desserialização eficiente das respostas JSON da API de roteamento.

---

## 📂 Arquitetura e Estrutura de Arquivos

O projeto adota um padrão **MVC (Model-View-Controller) Simplificado** para garantir separação de responsabilidades e facilitar o trabalho em equipe.

### `src/model/` (Domínio e Dados)
* **`Conexao.java`**: Um `record` imutável que atua como DTO (Data Transfer Object). Representa uma linha bruta da planilha fornecida, armazenando Ponto A, Ponto B e a largura de banda (Gbps).
* **`GrafoRede.java`**: A classe wrapper que encapsula o `SimpleWeightedGraph` do JGraphT. É responsável por instanciar a estrutura não direcionada e ponderada, além de garantir que não existam arestas duplicadas ao inserir novas rotas.

### `src/controller/` (Regras de Negócio e Algoritmos)
* **`OSMClient.java`**: O cliente HTTP. Comunica-se com a API do OpenStreetMap para transformar os nomes das cidades/estados em distâncias terrestres reais, que alimentam os pesos das arestas do grafo.
* **`AnalisadorRede.java`**: O motor algorítmico do projeto. Aplica as lógicas fundamentais:
  * **Kruskal e Prim**: Encontram a Árvore Geradora Mínima (MST), definindo a rota de cabeamento de menor custo para conectar todos os nós.
  * **Greedy Coloring (Coloração Gulosa)**: Atribui "cores" (IDs) aos vértices garantindo que pontos adjacentes tenham cores distintas, simulando a alocação de frequências para evitar interferências.

### `src/view/` (Entrada, Saída e Apresentação)
* **`Main.java`**: O ponto de entrada da aplicação. Orquestra a injeção da lista de conexões reais da Rede Ipê, instancia o grafo, aciona as APIs de distância e executa o analisador.
* **`ExportadorGrafo.java`**: Utiliza o `DOTExporter` nativo para compilar toda a estrutura gerada em memória e exportar um arquivo físico `.dot`. Esse arquivo é lido por softwares de renderização (como Gephi) para a visualização final do grafo.

---

## 🚀 Como Executar o Projeto

**Pré-requisitos:** Java 17+ e Apache Maven instalados.

1. Clone o repositório:
   ```bash
   git clone <URL_DO_SEU_REPOSITORIO>
   cd rede-ipe-grafos
