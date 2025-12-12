/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cadastroaluno.tela;

import br.com.cadastroaluno.dal.ModuloConexao;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// A linha abaixo importa recursos da biblioteca rs2xml.jar
import net.proteanit.sql.DbUtils;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;


/**
 *
 * @author Dev. ECSousa
 */

public class TelaCadastro extends javax.swing.JFrame {

    // Conexão com o banco
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    //private Object tbalunos;
    
public TelaCadastro() {
        initComponents();
        conexao = ModuloConexao.conector();
        iniciarRelogio();
        // Define o ícone da janela
        setIcon();
        
  }  

private void iniciarRelogio() {
    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            java.time.LocalDateTime agora = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter formato = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            jTDataHora.setText(agora.format(formato));
        }
    });
    timer.start();
}
       
    private void adicionar() {
    String sql = "INSERT INTO tbalunos (nome, endereco, telefone, email, curso, cpf, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, NOW())";
    
    try {
        pst = conexao.prepareStatement(sql);
        pst.setString(1, jTNome.getText());
        pst.setString(2, jTEndereco.getText());
        pst.setString(3, jTTelefone.getText());
        pst.setString(4, jTEmail.getText());
        pst.setString(5, jTCurso.getText());
        pst.setString(6, jTCpf.getText());

        //  Validação dos campos obrigatórios
        if (jTNome.getText().isEmpty() || jTEndereco.getText().isEmpty() ||
            jTTelefone.getText().isEmpty() || jTCpf.getText().isEmpty()) {
            
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            return;
        }

        //  Executa a inserção
        int adicionado = pst.executeUpdate();
        if (adicionado > 0) {
            JOptionPane.showMessageDialog(null, "Aluno adicionado com sucesso!");
            limpar();
        }

    } catch (java.sql.SQLIntegrityConstraintViolationException e) {
        //  Tratamento específico para CPF duplicado
        JOptionPane.showMessageDialog(null, "Já existe um aluno cadastrado com este CPF!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Erro ao adicionar aluno: " + e.getMessage());
    }
    
}

    // Método para pesquisar clientes pelo nome com friltros
    private void pesquisar() { 
	
	String textoPesquisa = jTRA.getText().trim();

    if (textoPesquisa.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Digite um RA para pesquisar.");
        return;
    }

       String sql = " select * from tbalunos tbalunos where ra like ?";
		
		
        try {
            pst = conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ? 
            // atencao ao "%" - contunuacao dqa String sql
            pst.setString(1, jTRA.getText() + "%");
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml para preencher a tabela
            tblAlunos.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
	
	// a linha abaixo desabilita o botao adicionar
        jTCpf.setEnabled(false);
	
	
}
    
   //Criando o metodo para alterar Cadastro do Aluno    
   // Método para alterar o cadastro do aluno
private void atualizar() {

    String sql = "UPDATE tbalunos SET nome=?, cpf=?, endereco=?, telefone=?, email=?, curso=?, status=?  WHERE ra=?";

    try {

        // Validação de campos obrigatórios
        if (jTNome.getText().isEmpty() || jTEndereco.getText().isEmpty() || jTEmail.getText().isEmpty() || jTCurso.getText().isEmpty()  ) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            return; // interrompe o método se faltar algo
        }

        pst = conexao.prepareStatement(sql);
        pst.setString(1, jTNome.getText());
        pst.setString(2, jTCpf.getText());
        pst.setString(3, jTEndereco.getText());
        pst.setString(4, jTTelefone.getText());
        pst.setString(5, jTEmail.getText());
        pst.setString(6, jTCurso.getText());
        pst.setString(7, cboStatus.getSelectedItem().toString());
        pst.setString(8, jTRA.getText()); // RA como identificador
        
        int atualizado = pst.executeUpdate();

        if (atualizado > 0) {
            JOptionPane.showMessageDialog(null, "Dados do aluno atualizados com sucesso!");

            limpar();
            
           /* // limpa o formulário
            jTRA.setText(null);
            jTNome.setText(null);
            jTCpf.setText(null);
            jTEndereco.setText(null);
            jTTelefone.setText(null);
            jTEmail.setText(null);
            jTCurso.setText(null);
            cboStatus.setSelectedIndex(-1);*/
        }

        pst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, " Erro ao atualizar: " + e.getMessage());
        e.printStackTrace();
    }
    
    // a linha abaixo desabilita o botao adicionar
        jTCpf.setEnabled(false);
}
  
private void carregarStatusAluno() {
    String sql = "SELECT usuario FROM tbusuarios";
    try {
        pst = conexao.prepareStatement(sql);
        rs = pst.executeQuery();
        cboStatus.removeAllItems(); // Limpa itens existentes
        while (rs.next()) {
            cboStatus.addItem(rs.getString("usuario"));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Erro ao carregar técnicos: " + e);
    }
}

// limpa o formulario apos o cadastro ou apos uma atualização e apos uma pesquisa de aluno cliando no botao limpar
 private void limpar() {
        
    jTData.setText(null);
    jTNome.setText(null);
    jTCpf.setText(null);
    jTEndereco.setText(null);
    jTTelefone.setText(null);
    jTEmail.setText(null);
    jTCurso.setText(null);
    cboStatus.setSelectedIndex(-1); // limpa seleção do ComboBox
    jTRA.setText(null);
    
  // a linha abaixo habilita o botao adicionar
      btnAdicionar.setEnabled(true);    
 // quando limpar deixa o campo cpf ativo outra vez para cadastro
      jTCpf.setEnabled(true);
      jTRA.setEnabled(true);
    }

 
  // Metodo para setar os campos do formulario com conteudo da tabela
     public void setar_campos() {
        int setar = tblAlunos.getSelectedRow();
        
        jTRA.setText(tblAlunos.getModel().getValueAt(setar, 0).toString() );
        jTNome.setText(tblAlunos.getModel().getValueAt(setar, 1).toString());
        jTEndereco.setText(tblAlunos.getModel().getValueAt(setar, 2).toString());
        jTTelefone.setText(tblAlunos.getModel().getValueAt(setar, 3).toString());
        jTEmail.setText(tblAlunos.getModel().getValueAt(setar, 4).toString() );
        jTCurso.setText(tblAlunos.getModel().getValueAt(setar, 5).toString() );
        
    // Conversão da data do cadastro
       String dataBanco = tblAlunos.getModel().getValueAt(setar, 6).toString();
    try {
        SimpleDateFormat formatoBanco = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoExibicao = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = formatoExibicao.format(formatoBanco.parse(dataBanco));
        jTData.setText(dataFormatada);
    } catch (ParseException e) {
        jTData.setText(dataBanco); // fallback se falhar
    }


        jTCpf.setText(tblAlunos.getModel().getValueAt(setar, 7).toString());
        cboStatus.setSelectedItem(tblAlunos.getModel().getValueAt(setar, 8).toString());
                             
      // a linha abaixo desabilita o botao adicionar
        btnAdicionar.setEnabled(false);
	
	 jTCpf.setEnabled(false);
         jTRA.setEnabled(false);
	
    }
 
     
     // método para imprimir  
    private void imprimir(){
        // 
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?", "Atençaõ", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            //imprimindo o relatório com o framework JasperReport
            try {
                // usando a classe HashMap para criar um filtro
                HashMap filtro = new HashMap();
                filtro.put("ra", Integer.parseInt(jTRA.getText()));
                // Usando a classe JasperPrint para preparar a impressao de um relatório
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/alunos.jasper"), filtro, conexao);
                // a linha abaixo exibe o relatortio atraves da clase JasperViewer
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
     
     
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTData = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTEndereco = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnAdicionar = new javax.swing.JButton();
        btnAtualizar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cboStatus = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        btnLimpar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jTRA = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTDataHora = new javax.swing.JTextField();
        jTCpf = new javax.swing.JTextField();
        jTTelefone = new javax.swing.JTextField();
        jTEmail = new javax.swing.JTextField();
        jTCurso = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlunos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Escola ECS");
        setResizable(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel7.setText("Cadastro de Aluno");

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel1.setText("RA:");

        jTData.setEditable(false);
        jTData.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/search.png"))); // NOI18N
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel2.setText("*Nome:");

        jTNome.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel3.setText("*Endereço:");

        jTEndereco.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel4.setText("*Telefone:");

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel5.setText("Email:");

        jLabel6.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel6.setText("Curso:");

        jLabel9.setText("Desevolvido: Evanilson Carvalho Sousa"); // NOI18N

        jLabel8.setText("ECS Tecnologia 2016"); // NOI18N

        btnAdicionar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnAdicionar.setText("Adicionar");
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnAtualizar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnAtualizar.setText("Atualizar");
        btnAtualizar.setToolTipText("Adicionar");
        btnAtualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAtualizar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel10.setText("*CPF:");

        jLabel11.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 0, 51));
        jLabel11.setText("* Campos obrigatório");

        cboStatus.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ativo", "Trancado", "Concluido", "Transferido" }));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setText("Situação:");

        btnLimpar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.setToolTipText("Adicionar");
        btnLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        btnImprimir.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnImprimir.setText("Imprimir");
        btnImprimir.setToolTipText("Adicionar");
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        jTRA.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTRA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTRAKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel13.setText("Data de Cadastro");

        jTDataHora.setEditable(false);
        jTDataHora.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jTCpf.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTCpfKeyTyped(evt);
            }
        });

        jTTelefone.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTTelefone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTTelefoneKeyTyped(evt);
            }
        });

        jTEmail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jTCurso.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        tblAlunos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblAlunos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblAlunos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "RA", "Nome", "CPF", "Endereco", "Telefone", "E-mail", "Curso", "Data Cadastro", "Situação"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAlunos.setFocusable(false);
        tblAlunos.getTableHeader().setReorderingAllowed(false);
        tblAlunos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAlunosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAlunos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel11)
                                .addGap(36, 36, 36))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jTRA, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(120, 120, 120)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(37, 37, 37)
                                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addComponent(jTNome, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnAdicionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnAtualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnLimpar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cboStatus, 0, 142, Short.MAX_VALUE)
                                            .addComponent(jLabel12)
                                            .addComponent(jTData)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(36, 36, 36))
                                    .addComponent(jTDataHora))))
                        .addGap(38, 38, 38))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(74, 74, 74))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel13))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTData, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTRA, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel12))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTNome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jLabel10)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTDataHora, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

   
    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // TODO add your handling code here:
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed
	
	 private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
       pesquisar();
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // Chamando o metodo Atualizar
        atualizar();
	
    }//GEN-LAST:event_btnAtualizarActionPerformed
 
    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        imprimir();
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void tblAlunosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAlunosMouseClicked
        // chamando o metodo para setar os campos
        setar_campos();
	
    }//GEN-LAST:event_tblAlunosMouseClicked

    private void jTRAKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTRAKeyReleased
        // TODO add your handling code here:
	pesquisar();
    }//GEN-LAST:event_jTRAKeyReleased

    private void jTCpfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCpfKeyTyped
        // TODO add your handling code here:
	char c = evt.getKeyChar();
    if (!Character.isDigit(c)) {
        evt.consume(); // Ignora o caractere se não for número
    }


    }//GEN-LAST:event_jTCpfKeyTyped

    private void jTTelefoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTTelefoneKeyTyped
        // TODO add your handling code here:
	char c = evt.getKeyChar();
    if (!Character.isDigit(c)) {
        evt.consume(); // Ignora o caractere se não for número
    }


    }//GEN-LAST:event_jTTelefoneKeyTyped
	  
    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icones/logo.jpg")));
    } 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JComboBox<String> cboStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTCpf;
    private javax.swing.JTextField jTCurso;
    private javax.swing.JTextField jTData;
    private javax.swing.JTextField jTDataHora;
    private javax.swing.JTextField jTEmail;
    private javax.swing.JTextField jTEndereco;
    private javax.swing.JTextField jTNome;
    private javax.swing.JTextField jTRA;
    private javax.swing.JTextField jTTelefone;
    private javax.swing.JTable tblAlunos;
    // End of variables declaration//GEN-END:variables

 
}