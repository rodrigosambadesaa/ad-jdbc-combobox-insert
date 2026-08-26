package dev.rodrigosambade.jdbc;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
public final class App {
    public static void main(String[]a){
        SwingUtilities.invokeLater(App::show);
    }
    private static void show(){
        try{
            Connection c=DemoDatabase.open();
            ArticleRepository repo=new ArticleRepository();
            JFrame f=new JFrame("Alta de artículos");
            JComboBox<Article> combo=new JComboBox<>();
            JTextField model=new JTextField(),price=new JTextField();
            JTextArea desc=new JTextArea(4,30);
            JButton add=new JButton("Añadir");
            Runnable reload=()->{
                try{
                    combo.setModel(new DefaultComboBoxModel<>(repo.all(c).toArray(Article[]::new)));
                } catch(SQLException e){
                    throw new RuntimeException(e);
                }
            }
            ;
            add.addActionListener(e->{
                try{
                    repo.insert(c,model.getText(),desc.getText(),new BigDecimal(price.getText().trim()));
                    model.setText("");
                    desc.setText("");
                    price.setText("");
                    reload.run();
                } catch(Exception x){
                    JOptionPane.showMessageDialog(f,x.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            );
            JPanel form=new JPanel(new GridLayout(0,2,8,8));
            form.add(new JLabel("Modelo"));
            form.add(model);
            form.add(new JLabel("Precio"));
            form.add(price);
            form.add(new JLabel("Descripción"));
            form.add(new JScrollPane(desc));
            form.add(add);
            form.add(combo);
            form.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
            f.add(form);
            f.pack();
            f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            f.addWindowListener(new java.awt.event.WindowAdapter(){
                public void windowClosed(java.awt.event.WindowEvent e){
                    try{
                        c.close();
                    } catch(SQLException ignored){
                    }
                }
            }
            );
            reload.run();
            f.setVisible(true);
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
}
