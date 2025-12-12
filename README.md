# 🎓 Apresentação – Sistema de Cadastro de Alunos (TelaCadastro.java)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-UI-green?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JasperReports](https://img.shields.io/badge/JasperReports-Reporting-orange?style=for-the-badge&logo=apache&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-IDE-blue?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)

---

## 🧩 Introdução
O Sistema de Cadastro de Alunos foi desenvolvido em Java utilizando o NetBeans e a biblioteca Swing para a interface gráfica.  
Criado inicialmente como atividade acadêmica na faculdade de Análise e Desenvolvimento de Sistemas (ADS), foi posteriormente aprimorado com novos recursos para torná-lo mais completo e profissional.  

O objetivo é automatizar o gerenciamento de alunos em uma instituição de ensino, permitindo cadastrar, pesquisar, atualizar e listar registros diretamente em um banco de dados.  
O sistema é simples, funcional e serve como base para projetos maiores, podendo futuramente ser adaptado para versão web.

---

## ⚙️ Tecnologias Utilizadas
- **Linguagem:** Java (JDK 8+)
- **IDE:** NetBeans
- **Banco de Dados:** MySQL (pode ser migrado para PostgreSQL)
- **Bibliotecas externas:**
  - rs2xml.jar – para exibir consultas SQL em tabelas
  - JasperReports – para geração de relatórios e impressão
- **Interface:** Java Swing (JFrame, JTable, JButton, JTextField, JComboBox)
- **Arquitetura:** Padrão DAO (Data Access Object) simplificado

---

## 📂 Funcionalidades do Sistema
- ✔ **Cadastro de Alunos**
  - Valida campos obrigatórios
  - Tratar duplicidade de CPF
  - Registro automático da data no banco (NOW())
- ✔ **Pesquisa por RA**
  - Filtragem dinâmica
  - Atualização da tabela conforme o usuário digita
- ✔ **Atualização de Dados**
  - Nome, CPF, Endereço, Telefone, E-mail, Curso, Situação
- ✔ **Preenchimento Automático**
  - Ao clicar na tabela, os dados são carregados nos campos
- ✔ **Relatório com JasperReports**
  - Gera relatório individual filtrando por RA
- ✔ **Data e Hora em Tempo Real**
  - Atualização automática via Timer
- ✔ **Limpeza do Formulário**
  - Remove valores e libera campos para novo cadastro

---

## 📂 Estrutura do Projeto  

src/  
 └── br/com/cadastroaluno/  
      ├── tela/  
      │    └── TelaCadastro.java  
      └── dal/  
           └── ModuloConexao.java  
reports/  
 └── alunos.jasper  
icones/  
 └── imagens de botões e logotipo  


---

## 🧠 Descrição Geral do Funcionamento
O sistema é composto por uma janela principal chamada **TelaCadastro**, que permite:
1. Cadastrar novos alunos
2. Pesquisar alunos pelo RA
3. Atualizar dados existentes
4. Limpar campos do formulário
5. Exibir data e hora em tempo real
6. Listar alunos em tabela dinâmica

## 🖼️ (Imagem: Tela de cadastro preenchida  
*(Adicione aqui screenshots reais da interface para ilustrar cada funcionalidade)*

---

## 🔧 Estrutura e Principais Funcionalidades
- **Conexão com o Banco:**  
  `conexao = ModuloConexao.conector();`

- **Relógio em Tempo Real:**  
  Atualização automática com `Timer` e `LocalDateTime`.

- **Cadastro de Alunos:**  
  Inserção com validação e tratamento de CPF duplicado.


- **Pesquisa e Listagem:**  
  Exibição dinâmica com `DbUtils.resultSetToTableModel(rs)`.

- **Preenchimento Automático:**  
  Campos preenchidos ao selecionar aluno na tabela.

- **Atualização de Dados:**  
  Uso de `PreparedStatement` para segurança contra SQL Injection.

- **Relatórios com JasperReports:**  
  Impressão e exportação profissional.

- **Limpeza do Formulário:**  
  Reset automático dos campos.

---

## 🧭 Fluxo de Uso
1. O sistema inicia e conecta-se ao banco automaticamente  
2. O relógio exibe data e hora em tempo real  
3. O usuário cadastra um aluno  
4. Pesquisa pelo RA e edita dados  
5. Imprime relatório do aluno  
6. Limpa formulário e reinicia processo  

---

## 💡 Destaques Técnicos
- Validação e prevenção de duplicidade de CPF  
- Relógio automatizado e data formatada  
- Interface amigável com Swing e JTable  
- Uso de DAO para modularidade  
- Integração completa com JDBC  
- Estrutura pronta para migração ao PostgreSQL  

---

## 🚀 Possíveis Extensões Futuras
- Migrar para PostgreSQL  
- Implementar login e controle de acesso  
- Adicionar upload de foto do aluno  
- Criar relatórios personalizados  
- Migrar para versão web (JSP, Spring Boot)  

---

## ▶ Como Executar o Projeto
1. Configure o Banco de Dados  
   - Crie o banco no MySQL  
   - Ajuste o arquivo `ModuloConexao.java` com usuário/senha  
2. Adicione as bibliotecas necessárias na pasta `libs/`  
   - mysql-connector-j.jar  
   - rs2xml.jar  
   - jasperreports.jar e dependências  
3. Execute no NetBeans  
   - Abra o projeto  
   - Faça **Clean & Build**  
   - Execute `TelaCadastro.java`  

---

## 🧾 Conclusão
O Sistema de Cadastro de Alunos é um projeto funcional, robusto e educativo, que demonstra na prática:
- Programação Orientada a Objetos (POO)  
- Integração com banco de dados via JDBC  
- Interface gráfica em Java Swing  
- Aplicação de boas práticas de codificação  

---

## 📜 Licença
Este projeto é licenciado sob os termos da **MIT License**.  
Você pode usar, modificar e distribuir livremente, desde que mantenha os créditos ao autor.

---

## **👨‍💻 Desenvolvedor  

**Evanilson Carvalho Sousa**  

	- **Pós-graduado em Engenharia de Software (UNINOVE)  
	- **Tecnólogo em Análise e Desenvolvimento de Sistemas  
	- **Técnico em Informática  
	- **Criador de Web Sites (Microcamp)  
	- **Analista de Suporte Técnico em TI

## 🌐 Contato  
- 📧 E-mail: **evanilsoncarvalho@gmail.com**  
- 💼 LinkedIn: [Evanilson Carvalho](https://www.linkedin.com/in/evanilson-carvalho-79b20ab5)

