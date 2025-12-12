🎓 Apresentação – Sistema de Cadastro de Alunos (TelaCadastro.java)  
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-UI-green?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JasperReports](https://img.shields.io/badge/JasperReports-Reporting-orange?style=for-the-badge&logo=apache&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-IDE-blue?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)  
🧩 Introdução  
O Sistema de Cadastro de Alunos, desenvolvido em Java utilizando o NetBeans e a biblioteca Swing para a interface gráfica.  
O projeto é uma atividade foi criado durante a faculdade de Análise e Desenvolvimento de Sistemas (ADS), e posteriormente aprimorado com novos recursos para torná-lo mais completo e profissional.  
O objetivo é automatizar o gerenciamento de alunos em uma instituição de ensino, permitindo cadastrar, pesquisar, atualizar e listar registros diretamente em um banco de dados.  
O sistema é simples, funcional e serve como base para projetos maiores, podendo futuramente ser adaptado para versão web.  
________________________________________
⚙️ Tecnologias Utilizadas  
•	Linguagem: Java (JDK 8+)  
•	IDE: NetBeans  
•	Banco de Dados: MySQL (pode ser facilmente migrado para PostgreSQL)  
•	Bibliotecas externas:  
o	rs2xml.jar – para exibir consultas SQL em tabelas  
o	JasperReports – para geração de relatórios e impressão  
•	Interface: Java Swing (JFrame, JTable, JButton, JTextField, JComboBox)  
•	Arquitetura: Padrão DAO (Data Access Object) simplificado  
________________________________________
📂 Funcionalidades do Sistema
✔ Cadastro de Alunos
•	Valida campos obrigatórios
•	Trata duplicidade de CPF
•	Registro automático da data no banco (NOW())
✔ Pesquisa por RA
•	Filtragem dinâmica
•	A tabela é atualizada conforme o usuário digita
✔ Atualização de Dados
Atualiza:
•	Nome
•	CPF
•	Endereço
•	Telefone
•	E-mail
•	Curso
•	Situação (Ativo, Trancado, Concluído, Transferido)
✔ Preenchimento Automático
Ao clicar na tabela, os dados são carregados nos campos para edição.
✔ Relatório com JasperReports
Gera relatório individual filtrando por RA.
✔ Data e Hora em Tempo Real
Atualização automática no formulário via Timer.
✔ Limpeza do formulário
Remove valores e libera campos para novo cadastro.
________________________________________



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
 └── imagens de botões e logotipo________________________________________
🧠 Descrição Geral do Funcionamento  
O sistema é composto por uma janela principal chamada TelaCadastro, que permite:  
1.	Cadastrar novos alunos com nome, CPF, endereço, telefone, e-mail e curso.  
2.	Pesquisar alunos pelo número do RA (Registro Acadêmico).  
3.	Atualizar dados de alunos existentes, incluindo status (Ativo, Trancado, Concluído, Transferido).  
4.	Limpar campos do formulário para novo cadastro.  
5.	Exibir automaticamente data e hora em tempo real.  
6.	Listar alunos em uma tabela dinâmica com atualização instantânea.  
🖼️ (Imagem: Tela de cadastro preenchida)   
 

________________________________________
🔧 Estrutura e Principais Funcionalidades  
🔹 Conexão com o Banco  
A classe ModuloConexao gerencia a comunicação com o banco de dados via JDBC:  
conexao = ModuloConexao.conector();  
Essa conexão é automática na inicialização e exibe o status visualmente no sistema.  
________________________________________
🔹 Relógio em Tempo Real  
O método iniciarRelogio() usa a classe Timer para atualizar a data e hora a cada segundo:  
Timer timer = new Timer(1000, new ActionListener() {  
    public void actionPerformed(ActionEvent e) {  
        LocalDateTime agora = LocalDateTime.now();  
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
        jTDataHora.setText(agora.format(formato));  
    }  
});  
timer.start();  
✅ Melhoria: o relógio atualiza continuamente sem interação do usuário.  
🖼️ (Imagem: Campo mostrando a data e hora em tempo real)  
 
________________________________________
🔹 Cadastro de Alunos  
O método adicionar() insere novos registros no banco:  
String sql = "INSERT INTO tbalunos (nome, endereco, telefone, email, curso, cpf, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, NOW())";  
Inclui:  
•	Validação de campos obrigatórios  
•	Tratamento de CPF duplicado  
•	Limpeza automática após cadastro  
try {  
    pst.executeUpdate();  
    JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");  
    limpar();  
} catch (SQLIntegrityConstraintViolationException e) {  
    JOptionPane.showMessageDialog(null, "CPF já cadastrado. Verifique!");  
}  
✅ Melhoria: o sistema impede cadastros repetidos e informa o usuário.  
🖼️ (Imagem: Mensagem de sucesso no cadastro)  
 
________________________________________


🔹 Pesquisa e Listagem  
A pesquisa é feita pelo RA, e os resultados são exibidos na JTable:  
tblAlunos.setModel(DbUtils.resultSetToTableModel(rs));  
✅ Melhoria: a listagem é dinâmica e permite seleção direta para edição.  
🖼️ (Imagem: Tabela com listagem de alunos)  
 
________________________________________
🔹 Preenchimento Automático  
Ao clicar em uma linha da tabela, o método setar_campos() preenche os campos automaticamente:  
jTNome.setText(tblAlunos.getModel().getValueAt(setar, 1).toString());  
A data é formatada para o padrão brasileiro:  
DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");  
              jTData.setText(formato.format(rs.getDate("data_cadastro")));  
✅ Melhoria: datas legíveis e consistentes no formato dd/MM/yyyy.  

🖼️ (Imagem: Campos preenchidos após selecionar um aluno)  
 


________________________________________
🔹 Atualização de Dados  
Permite alterar qualquer campo do aluno:
O método atualizar() usa PreparedStatement para segurança:  
UPDATE tbalunos   
 	SET nome=?, cpf=?, endereco=?, telefone=?, email=?, curso=?, status=?   
WHERE ra=?  
✅ Melhoria: evita SQL Injection e garante atualização precisa.  
Inclui:
•	Atualiza nome, CPF, endereço, telefone, email, curso, status
•	Valida campos antes de enviar ao banco


🖼️ (Imagem: Aluno sendo atualizado na tela)  
 

________________________________________
🔹 Impressão de Relatórios com JasperReports  
O sistema gera relatórios e permite impressão direta:  
JasperPrint print =   JasperFillManager.fillReport("C:\\relatorios\\relatorioAlunos.jasper", null, conexao);  
JasperViewer.viewReport(print, false);  
✅ Melhoria: relatórios prontos para exportação e impressão profissional.  

🖼️ (Imagem: Dados do aluno gerado pelo sistema para imprimir)  
 
________________________________________
🔹 Limpeza do Formulário  
O método limpar() redefine todos os campos:  
jTRA.setText(null);  
jTNome.setText(null);  
jTTelefone.setText(null);  
jTEmail.setText(null);  
jTEndereco.setText(null);  
jTCurso.setText(null);  
jTCPF.setText(null);  
✅ Melhoria: evita dados residuais entre cadastros.  
________________________________________
🧭 Fluxo de Uso  
1.	O sistema inicia e conecta-se ao banco automaticamente.  
2.	O relógio exibe data e hora em tempo real.  
3.	O usuário cadastra um aluno e salva.  
4.	Pode pesquisar pelo RA e editar os dados.  
5.	Pode imprimir os dados do aluno.  
6.	Pode limpar o formulário e reiniciar o processo.  
________________________________________
💡 Destaques Técnicos  
•	Validação e prevenção de duplicidade de CPF.  
•	Relógio automatizado e data de cadastro formatada.  
•	Interface amigável com Swing e JTable.  
•	Uso de DAO para modularidade.  
•	Integração completa com o banco via JDBC.  
•	Estrutura pronta para migração ao PostgreSQL.  
________________________________________
🚀 Possíveis Extensões Futuras  
•	Migrar completamente para PostgreSQL.  
•	Implementar login e controle de acesso por perfil.  
•	Adicionar upload e exibição de foto do aluno.  
•	Criar relatórios personalizados (por status, curso, etc.).  
•	Migrar para versão web (JSP, Spring Boot).  
________________________________________
▶ Como Executar o Projeto
1️⃣ Configure o Banco de Dados
•	Crie o banco no MySQL
•	Ajuste o arquivo ModuloConexao.java com seu usuário/senha
2️⃣ Adicione as bibliotecas necessárias
Coloque na pasta libs/:
•	mysql-connector-j.jar
•	rs2xml.jar
•	jasperreports.jar e dependências
3️⃣ Execute no NetBeans
•	Abra o projeto
•	Faça Clean & Build
•	Execute o arquivo TelaCadastro.java



🧾 Conclusão  
O Sistema de Cadastro de Alunos é um projeto funcional, robusto e educativo, que demonstra na prática:  
•	Programação Orientada a Objetos (POO);  
•	Integração com banco de dados via JDBC;  
•	Interface gráfica em Java Swing;  
•	E aplicação de boas práticas de codificação.  

📜 ## **Licença  
Este projeto é licenciado sob os termos da MIT License.  
Você pode usar, modificar e distribuir livremente, desde que mantenha os créditos ao autor.  
🌐### **Contato  
📧 E-mail: evanilsoncarvalho@gmail.com   
💼 LinkedIn: www.linkedin.com/in/evanilson-carvalho-79b20ab5 
