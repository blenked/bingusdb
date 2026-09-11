package ui;

import entities.Aluno;
import entities.Disciplina;
import entities.TipoProva;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;

public final class ComboBoxUtil {

    public interface Carregador<T> {
        List<T> carregar() throws SQLException;
    }

    private ComboBoxUtil() {
    }

    public static <T> void carregar(JComboBox<T> combo, Carregador<T> carregador) {
        try {
            List<T> lista = carregador.carregar();
            combo.removeAllItems();
            for (T item : lista) {
                combo.addItem(item);
            }
        } catch (SQLException ex) {
            combo.removeAllItems();
        }
    }

    public static void renderizarAluno(JComboBox<Aluno> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel rotulo = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Aluno aluno) {
                    rotulo.setText(aluno.getRa() + " - " + aluno.getNome());
                }
                return rotulo;
            }
        });
    }

    public static void renderizarDisciplina(JComboBox<Disciplina> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel rotulo = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Disciplina disciplina) {
                    rotulo.setText(disciplina.nome());
                }
                return rotulo;
            }
        });
    }

    public static void renderizarTipoProva(JComboBox<TipoProva> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel rotulo = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoProva tipoProva) {
                    rotulo.setText(tipoProva.nome());
                }
                return rotulo;
            }
        });
    }
}