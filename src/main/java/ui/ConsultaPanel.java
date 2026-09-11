package ui;

import entities.Aluno;
import entities.Disciplina;
import entities.TipoProva;
import repository.AlunoRepository;
import repository.ConsultaNotaResultado;
import repository.DisciplinaRepository;
import repository.NotasRepository;
import repository.TipoProvaRepository;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Locale;

public class ConsultaPanel extends JPanel {

    private final AlunoRepository alunoRepository = new AlunoRepository();
    private final DisciplinaRepository disciplinaRepository = new DisciplinaRepository();
    private final TipoProvaRepository tipoProvaRepository = new TipoProvaRepository();
    private final NotasRepository notasRepository = new NotasRepository();

    private final JComboBox<Aluno> comboAluno = new JComboBox<>();
    private final JComboBox<Disciplina> comboDisciplina = new JComboBox<>();
    private final JComboBox<TipoProva> comboTipoProva = new JComboBox<>();
    private final JButton botaoConsultar = new JButton("Consultar Nota");

    public ConsultaPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        ComboBoxUtil.renderizarAluno(comboAluno);
        ComboBoxUtil.renderizarDisciplina(comboDisciplina);
        ComboBoxUtil.renderizarTipoProva(comboTipoProva);

        int linha = 0;
        adicionarCampo(linha++, new JLabel("Aluno:"), comboAluno);
        adicionarCampo(linha++, new JLabel("Disciplina:"), comboDisciplina);
        adicionarCampo(linha++, new JLabel("Tipo de Prova:"), comboTipoProva);

        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = linha;
        cons.gridwidth = 2;
        cons.insets = new Insets(12, 4, 4, 4);
        add(botaoConsultar, cons);

        botaoConsultar.addActionListener(e -> consultarNota());

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

    private void consultarNota() {
        Aluno aluno = (Aluno) comboAluno.getSelectedItem();
        Disciplina disciplina = (Disciplina) comboDisciplina.getSelectedItem();
        TipoProva tipoProva = (TipoProva) comboTipoProva.getSelectedItem();

        if (aluno == null || disciplina == null || tipoProva == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione o aluno, a disciplina e o tipo de prova.",
                    "Consulta de Nota", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ConsultaNotaResultado resultado = notasRepository.consultarNota(
                    aluno.getRa(), disciplina.id(), tipoProva.id());

            if (resultado == null) {
                JOptionPane.showMessageDialog(this,
                        "Nenhuma nota lançada para essa combinação de aluno, disciplina e tipo de prova.",
                        "Consulta de Nota", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            exibirResultado(resultado);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível acessar o banco de dados. Verifique se o MariaDB está em execução e tente novamente.",
                    "Erro no banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirResultado(ConsultaNotaResultado resultado) {
        Frame dono = (Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(dono, "Resultado da Consulta", true);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel painel = new JPanel(new GridLayout(0, 2, 12, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        painel.add(new JLabel("Nome do Aluno:"));
        painel.add(campoValor(resultado.nomeAluno()));
        painel.add(new JLabel("RA:"));
        painel.add(campoValor(resultado.ra()));
        painel.add(new JLabel("Disciplina:"));
        painel.add(campoValor(resultado.nomeDisciplina()));
        painel.add(new JLabel("Tipo de Prova:"));
        painel.add(campoValor(resultado.tipoProva()));
        painel.add(new JLabel("Nota:"));
        painel.add(campoValor(String.format(Locale.forLanguageTag("pt-BR"), "%.2f", resultado.nota())));

        JButton botaoFechar = new JButton("Fechar");
        botaoFechar.addActionListener(e -> dialog.dispose());
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(botaoFechar);

        dialog.add(painel, BorderLayout.CENTER);
        dialog.add(painelBotoes, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private static JLabel campoValor(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(rotulo.getFont().deriveFont(java.awt.Font.BOLD));
        return rotulo;
    }

    public void atualizarAlunos() {
        ComboBoxUtil.carregar(comboAluno, alunoRepository::listarTodos);
    }
}