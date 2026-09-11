package ui;

import entities.Aluno;
import entities.Disciplina;
import entities.Notas;
import entities.TipoProva;
import repository.AlunoRepository;
import repository.DisciplinaRepository;
import repository.NotasRepository;
import repository.TipoProvaRepository;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class NotaPanel extends JPanel {

    private final AlunoRepository alunoRepository = new AlunoRepository();
    private final DisciplinaRepository disciplinaRepository = new DisciplinaRepository();
    private final TipoProvaRepository tipoProvaRepository = new TipoProvaRepository();
    private final NotasRepository notasRepository = new NotasRepository();

    private final JComboBox<Aluno> comboAluno = new JComboBox<>();
    private final JComboBox<Disciplina> comboDisciplina = new JComboBox<>();
    private final JComboBox<TipoProva> comboTipoProva = new JComboBox<>();
    private final JTextField campoNota = new JTextField(6);
    private final JButton botaoLancar = new JButton("Lançar Nota");

    public NotaPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        ComboBoxUtil.renderizarAluno(comboAluno);
        ComboBoxUtil.renderizarDisciplina(comboDisciplina);
        ComboBoxUtil.renderizarTipoProva(comboTipoProva);

        int linha = 0;
        adicionarCampo(linha++, new JLabel("Aluno:"), comboAluno);
        adicionarCampo(linha++, new JLabel("Disciplina:"), comboDisciplina);
        adicionarCampo(linha++, new JLabel("Tipo de Prova:"), comboTipoProva);
        adicionarCampo(linha++, new JLabel("Nota (0,00 a 10,00):"), campoNota);

        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = linha;
        cons.gridwidth = 2;
        cons.insets = new Insets(12, 4, 4, 4);
        add(botaoLancar, cons);

        botaoLancar.addActionListener(e -> lancarNota());

        atualizarAlunos();
        ComboBoxUtil.carregar(comboDisciplina, disciplinaRepository::listarTodas);
        ComboBoxUtil.carregar(comboTipoProva, tipoProvaRepository::listarTodos);
    }

    private void adicionarCampo(int linha, JLabel rotulo, Component campo) {
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

    private void lancarNota() {
        Aluno aluno = (Aluno) comboAluno.getSelectedItem();
        Disciplina disciplina = (Disciplina) comboDisciplina.getSelectedItem();
        TipoProva tipoProva = (TipoProva) comboTipoProva.getSelectedItem();
        String textoNota = campoNota.getText().trim();

        if (aluno == null || disciplina == null || tipoProva == null || textoNota.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Selecione o aluno, a disciplina, o tipo de prova e informe a nota.",
                    "Lançamento de Nota", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double nota;
        try {
            nota = Double.parseDouble(textoNota.replace(',', '.'));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Nota inválida. Informe um número entre 0,00 e 10,00.",
                    "Nota inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Notas notas;
        try {
            notas = new Notas(aluno.getRa(), disciplina.id(), tipoProva.id(), nota);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Nota inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (notasRepository.notaJaExiste(notas.getRa(), notas.getIdDisciplina(), notas.getIdTipoProva())) {
                JOptionPane.showMessageDialog(this,
                        "Já existe uma nota para esse aluno, disciplina e tipo de prova.",
                        "Nota duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            notasRepository.lancarNota(notas);
            JOptionPane.showMessageDialog(this,
                    "Nota lançada com sucesso para o aluno " + aluno.getNome() + "!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            campoNota.setText("");
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this,
                    "Já existe uma nota para esse aluno, disciplina e tipo de prova.",
                    "Nota duplicada", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível acessar o banco de dados. Verifique se o MariaDB está em execução e tente novamente.",
                    "Erro no banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void atualizarAlunos() {
        ComboBoxUtil.carregar(comboAluno, alunoRepository::listarTodos);
    }
}