package ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class MainFrame extends JFrame {

    private final NotaPanel notaPanel = new NotaPanel();
    private final ConsultaPanel consultaPanel = new ConsultaPanel();

    public MainFrame() {
        super("Sistema Acadêmico");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Cadastro de Alunos", new AlunoPanel(this::atualizarListasDeAlunos));
        abas.addTab("Lançamento de Notas", notaPanel);
        abas.addTab("Consulta de Nota", consultaPanel);

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
        JButton botaoBoletim = new JButton("Abrir Boletim");
        botaoBoletim.addActionListener(e -> new BoletimFrame().setVisible(true));
        painelTopo.add(botaoBoletim);

        add(painelTopo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);

        setSize(760, 480);
        setLocationRelativeTo(null);
    }

    private void atualizarListasDeAlunos() {
        notaPanel.atualizarAlunos();
        consultaPanel.atualizarAlunos();
    }
}