package ui;

import entities.Aluno;
import repository.AlunoRepository;
import repository.NotasRepository;
import repository.RegistroBoletim;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class BoletimFrame extends JFrame {

    private static final String[] COLUNAS = {"Disciplina", "Tipo de Prova", "Nota"};

    private final AlunoRepository alunoRepository = new AlunoRepository();
    private final NotasRepository notasRepository = new NotasRepository();

    private final JComboBox<Aluno> comboAluno = new JComboBox<>();
    private final DefaultTableModel modelo =
            new DefaultTableModel(COLUNAS, 0) {
                @Override
                public boolean isCellEditable(int linha, int coluna) {
                    return false;
                }
            };
    private final JTable tabela = new JTable(modelo);
    private final JButton botaoGerar = new JButton("Gerar Boletim");

    public BoletimFrame() {
        super("Boletim do Aluno");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ComboBoxUtil.renderizarAluno(comboAluno);

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        painelTopo.add(new JLabel("Aluno:"));
        painelTopo.add(comboAluno);
        painelTopo.add(botaoGerar);

        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        botaoGerar.addActionListener(e -> gerarBoletim());

        ComboBoxUtil.carregar(comboAluno, alunoRepository::listarTodos);

        add(painelTopo, BorderLayout.NORTH);
        add(rolagem, BorderLayout.CENTER);
        setSize(700, 450);
        setLocationRelativeTo(null);
    }

    private void gerarBoletim() {
        Aluno aluno = (Aluno) comboAluno.getSelectedItem();
        if (aluno == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um aluno para gerar o boletim.",
                    "Boletim", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<RegistroBoletim> registros = notasRepository.listarBoletim(aluno.getRa());
            modelo.setRowCount(0);
            if (registros.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nenhuma nota lançada para o aluno " + aluno.getNome() + ".",
                        "Boletim", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            for (RegistroBoletim registro : registros) {
                modelo.addRow(new Object[]{
                        registro.nomeDisciplina(),
                        registro.tipoProva(),
                        String.format(Locale.forLanguageTag("pt-BR"), "%.2f", registro.nota())
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível acessar o banco de dados. Verifique se o MariaDB está em execução e tente novamente.",
                    "Erro no banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }
}