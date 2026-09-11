package ui;

import entities.Aluno;
import repository.AlunoRepository;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class AlunoPanel extends JPanel {

    private static final DateTimeFormatter FORMATO_DATA = new DateTimeFormatterBuilder()
            .appendPattern("dd/MM/uuuu")
            .toFormatter(Locale.of("pt", "BR"))
            .withResolverStyle(ResolverStyle.STRICT);

    private final AlunoRepository alunoRepository = new AlunoRepository();
    private final Runnable aposCadastro;

    private final JTextField campoRa = new JTextField(10);
    private final JTextField campoNome = new JTextField(30);
    private final JTextField campoDataNascimento = new JTextField(10);
    private final JTextField campoRg = new JTextField(15);
    private final JButton botaoCadastrar = new JButton("Cadastrar Aluno");

    public AlunoPanel(Runnable aposCadastro) {
        this.aposCadastro = aposCadastro;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        int linha = 0;
        adicionarCampo(linha++, new JLabel("RA:"), campoRa);
        adicionarCampo(linha++, new JLabel("Nome:"), campoNome);
        adicionarCampo(linha++, new JLabel("Data de Nascimento:"), campoDataNascimento);
        adicionarCampo(linha++, new JLabel("RG:"), campoRg);

        JLabel dica = new JLabel("RA (ex.: R00001-1) | Data no formato dd/mm/aaaa | RG no formato 12.345.678-9");
        dica.setForeground(java.awt.Color.GRAY);
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = linha++;
        cons.gridwidth = 2;
        cons.insets = new Insets(4, 4, 12, 4);
        add(dica, cons);

        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = linha;
        cons.gridwidth = 2;
        cons.insets = new Insets(8, 4, 4, 4);
        add(botaoCadastrar, cons);

        botaoCadastrar.addActionListener(e -> cadastrarAluno());
    }

    private void adicionarCampo(int linha, JLabel rotulo, JTextField campo) {
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = linha;
        cons.anchor = GridBagConstraints.WEST;
        cons.insets = new Insets(4, 4, 4, 8);
        add(rotulo, cons);

        cons = new GridBagConstraints();
        cons.gridx = 1;
        cons.gridy = linha;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.insets = new Insets(4, 4, 4, 4);
        add(campo, cons);
    }

    private void cadastrarAluno() {
        String ra = campoRa.getText().trim();
        String nome = campoNome.getText().trim();
        String textoData = campoDataNascimento.getText().trim();
        String rg = campoRg.getText().trim();

        if (ra.isEmpty() || nome.isEmpty() || textoData.isEmpty() || rg.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos obrigatórios.",
                    "Cadastro de Aluno", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate dataNascimento;
        try {
            dataNascimento = LocalDate.parse(textoData, FORMATO_DATA);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Data de nascimento inválida. Use o formato dd/mm/aaaa (ex.: 15/02/2005).",
                    "Data inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Aluno aluno;
        try {
            aluno = new Aluno(ra, nome, dataNascimento, rg);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dados inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (alunoRepository.raJaExiste(ra)) {
                JOptionPane.showMessageDialog(this,
                        "Já existe um aluno cadastrado com o RA " + ra + ".",
                        "RA duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (alunoRepository.rgJaExiste(rg)) {
                JOptionPane.showMessageDialog(this,
                        "Já existe um aluno cadastrado com o RG informado.",
                        "RG duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            alunoRepository.cadastrar(aluno);
            JOptionPane.showMessageDialog(this,
                    "Aluno " + aluno.getNome() + " cadastrado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            aposCadastro.run();
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this,
                    "RA ou RG já cadastrado para outro aluno.",
                    "Dados duplicados", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível acessar o banco de dados. Verifique se o MariaDB está em execução e tente novamente.",
                    "Erro no banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        campoRa.setText("");
        campoNome.setText("");
        campoDataNascimento.setText("");
        campoRg.setText("");
    }
}