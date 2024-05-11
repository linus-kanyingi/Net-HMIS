/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.hmis;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
//import javax.swing.text.Document;
import net.proteanit.sql.DbUtils;
//import com.lowagie.text.Document;


/**
 *
 * @author user
 */
public class Clerking extends javax.swing.JFrame {


    /**
     * Creates new form nursing
     */
    public void setPersonaText(String username) {
        persona_txt.setText(username);
    }
    public Clerking() {
        initComponents();
        showData();
        currentDate();
        
        this.setLocationRelativeTo(null);
        
        
    }
    
public void currentDate() {
    Calendar cal = new GregorianCalendar();
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    date_txt.setText(day+ "/" + month + "/" + year);

    int second = cal.get(Calendar.SECOND);
    int minute = cal.get(Calendar.MINUTE);
    int hour = cal.get(Calendar.HOUR_OF_DAY);

    time_txt.setText(hour + "." + minute + "." + second);
}

         
         

    /**
     */
    
     public void showData(){
        try{
            Class.forName("org.postgresql.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254");
            Statement st = conn.createStatement();
            String query = "select * from patient_details";
            ResultSet rs = st.executeQuery(query);
            register_report.setModel(DbUtils.resultSetToTableModel(rs));
            
            rs.close();
           st.close();
           conn.close();
            
            
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex);
        }
        try{
            Class.forName("org.postgresql.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254");
            Statement st = conn.createStatement();
            String query = "SELECT patient_no, patient_name, test, parameter, result, lower, upper, unit, verified, date, time FROM public.lab_results order by date ASC, time DESC;";
            ResultSet rs = st.executeQuery(query);
            lab_results.setModel(DbUtils.resultSetToTableModel(rs));
            
            rs.close();
            st.close();
            conn.close();
            
            
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex);
        }
  
    try {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254");
        Statement st = conn.createStatement();

        // Execute SQL query to retrieve data from patient_details table
        String query = "SELECT code, test, fee, time, bench FROM public.lab_test";
        ResultSet rs = st.executeQuery(query);

        // Create a new table model with the retrieved data and a boolean column
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                } else {
                    return super.getColumnClass(columnIndex);
                }
            }
            
        };

        model.setColumnIdentifiers(new String[] {"Select", "Code", "Test", "Fee", "Waiting Time", "Bench"});
        while (rs.next()) {
            model.addRow(new Object[] {new JCheckBox(), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)});
        }

        // Set the new table model to the existing JTable
        register_tb.setModel(model);

        // Add a checkbox in the first column
        TableColumnModel columnModel = register_tb.getColumnModel();
        columnModel.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        columnModel.getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected(Boolean.valueOf(value.toString()));
                return checkBox;
            }
        });
        
        

        // Set the column width of the boolean column to 50
        columnModel.getColumn(0).setPreferredWidth(50);

        // Close the ResultSet, Statement, and Connection
        rs.close();
        st.close();
        conn.close();
    } catch(Exception ex) {
        JOptionPane.showMessageDialog(this, ex);
    }
    try {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254");
        Statement st = conn.createStatement();

        // Execute SQL query to retrieve data from registered_pharms table
        String query = "SELECT code, drug_name, price, strength, units, quantity FROM public.registered_pharms_balance;";
        ResultSet rs = st.executeQuery(query);

        // Create a new table model with the retrieved data and a boolean column
              DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                } else {
                    return super.getColumnClass(columnIndex);
                }
            }
        };
        model.setColumnIdentifiers(new String[] {"Select", "Code", "Name", "Price", "Strength", "Units", "Quantity", "Quantity Requested"});
        while (rs.next()) {
            model.addRow(new Object[] {new JCheckBox(), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), ""});
        }
        
        // Set the new table model to the existing JTable
        registered_pharms_tb.setModel(model);

        // Add a checkbox in the first column
        TableColumnModel columnModel = registered_pharms_tb.getColumnModel();
        columnModel.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        columnModel.getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected(Boolean.valueOf(value.toString()));
                return checkBox;
            }
        });

        // Set the column width of the boolean column to 50
        columnModel.getColumn(0).setPreferredWidth(50);

        // Close the ResultSet, Statement, and Connection
        rs.close();
        st.close();
        conn.close();


    } catch(Exception ex) {
        JOptionPane.showMessageDialog(this, ex);
    }
    

}

     public void MyFrame() throws ClassNotFoundException {

        // Get the existing JTable
        register_tb = new JTable();

        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(register_tb);
        getContentPane().add(scrollPane);

        // Get data from PostgreSQL database
        try {
            Class.forName("org.postgresql.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254");
            PreparedStatement pstmt = conn.prepareStatement("SELECT code, test, fee, time, bench FROM public.lab_test;");
            ResultSet rs = pstmt.executeQuery();

            // Get the DefaultTableModel of the JTable
            DefaultTableModel model = (DefaultTableModel) register_tb.getModel();

            // Populate the JTable with the data retrieved from the database
            while (rs.next()) {
                Object[] row = new Object[]{Boolean.FALSE, rs.getString("column1"), rs.getString("column2"), rs.getString("column3"), rs.getString("column4"), rs.getString("column5")};
                model.addRow(row);
            }

            rs.close();
            pstmt.close();
            conn.close();
            
            JOptionPane.showMessageDialog(this, "Table has been populated successfully!");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setVisible(true);
    }
public void showRegisteredPharmsData() {
    try {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254");
        Statement st = conn.createStatement();

        // Execute SQL query to retrieve data from registered_pharms table
        String query = "SELECT code, drug_name, price, expiration_date, quantity, strength FROM public.registered_pharms;";
        ResultSet rs = st.executeQuery(query);

        // Create a new table model with the retrieved data and a boolean column
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                } else {
                    return super.getColumnClass(columnIndex);
                }
            }
        };
        model.setColumnIdentifiers(new String[] {"Select", "Code", "Name", "Price", "Expiry Date", "Quantity", "Strength", "Quantity Requested"});
        while (rs.next()) {
            model.addRow(new Object[] {false, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), ""});
        }
        
        // Create a new JTable with the new table model
        JTable table = new JTable(model);
        
        // Add a checkbox in the first column
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        columnModel.getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected(Boolean.valueOf(value.toString()));
                return checkBox;
            }
        });

        // Set the column width of the boolean column to 50
        columnModel.getColumn(0).setPreferredWidth(50);

        // Add the new JTable to a new JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


        // Close the ResultSet, Statement, and Connection
        rs.close();
        st.close();
        conn.close();

    } catch(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage());
    }
}


     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        register_report = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        diag_txt = new javax.swing.JTextPane();
        jScrollPane15 = new javax.swing.JScrollPane();
        treatment_txt = new javax.swing.JTextPane();
        jScrollPane16 = new javax.swing.JScrollPane();
        hist_txt = new javax.swing.JTextPane();
        jPanel11 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        search_lab = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        register_tb = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        registered_pharms_tb = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        search_pharm = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        notes_txt = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lab_results = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        pat_name_txt = new javax.swing.JTextField();
        pat_no_txt = new javax.swing.JTextField();
        date_txt = new javax.swing.JLabel();
        time_txt = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jPanel12 = new javax.swing.JPanel();
        persona_txt = new javax.swing.JLabel();
        db_connection_status = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        register_report.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Patient No", "Patient Name", "Urgency", "Age", "Clinic", "Arrival Time", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        register_report.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                register_reportMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(register_report);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jTabbedPane1.addTab("Occupancy", jPanel1);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        diag_txt.setBorder(javax.swing.BorderFactory.createTitledBorder("Diagnosis"));
        jScrollPane14.setViewportView(diag_txt);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel7.add(jScrollPane14, gridBagConstraints);

        treatment_txt.setBorder(javax.swing.BorderFactory.createTitledBorder("Treatment / Prescription"));
        jScrollPane15.setViewportView(treatment_txt);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel7.add(jScrollPane15, gridBagConstraints);

        hist_txt.setBorder(javax.swing.BorderFactory.createTitledBorder("History / Complaints"));
        jScrollPane16.setViewportView(hist_txt);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel7.add(jScrollPane16, gridBagConstraints);

        jPanel11.setLayout(new java.awt.GridBagLayout());

        jButton1.setText("Save Data");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel11.add(jButton1, gridBagConstraints);

        jButton2.setText("Clear Form");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel11.add(jButton2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel7.add(jPanel11, gridBagConstraints);

        jTabbedPane1.addTab("Clerking", jPanel7);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Search For Lab Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel9.add(jLabel3, gridBagConstraints);

        search_lab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search_labKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel9.add(search_lab, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel8.add(jPanel9, gridBagConstraints);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        register_tb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Select", "CODE", "TEST", "FEE", "TIME"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(register_tb);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel10.add(jScrollPane10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel8.add(jPanel10, gridBagConstraints);

        jPanel13.setLayout(new java.awt.GridBagLayout());

        jButton6.setText("Save Data");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel13.add(jButton6, gridBagConstraints);

        jButton7.setText("Clear Form");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel13.add(jButton7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel8.add(jPanel13, gridBagConstraints);

        jTabbedPane1.addTab("Lab Request", jPanel8);

        jPanel14.setBackground(new java.awt.Color(102, 204, 255));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        registered_pharms_tb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SELECT", "CODE", "NAME", "PRICE", "STRENGTH", "UNITS", "QUANTITY", "EXPIRY_DATE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane12.setViewportView(registered_pharms_tb);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel14.add(jScrollPane12, gridBagConstraints);

        jLabel4.setText("Search For Lab Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel14.add(jLabel4, gridBagConstraints);

        search_pharm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search_pharmKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel14.add(search_pharm, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jButton8.setText("Save Data");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel4.add(jButton8, gridBagConstraints);

        jButton9.setText("Clear Form");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel4.add(jButton9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel14.add(jPanel4, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        notes_txt.setBorder(javax.swing.BorderFactory.createTitledBorder("Notes"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel5.add(notes_txt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel14.add(jPanel5, gridBagConstraints);

        jTabbedPane1.addTab("Medical Request", jPanel14);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        lab_results.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Patient Number", "Patient Name", "Test", "Parameter", "Result", "Lower Limit", "Upper Limit", "Units", "Verified By", "Date", "Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(lab_results);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel2.add(jScrollPane2, gridBagConstraints);

        jButton4.setText("Show For Current Patient");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel2.add(jButton4, gridBagConstraints);

        jTabbedPane1.addTab("Laboratory Results", jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jButton3.setText("Patient Card");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel6.add(jButton3, gridBagConstraints);

        pat_name_txt.setEditable(false);
        pat_name_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pat_name_txtActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel6.add(pat_name_txt, gridBagConstraints);

        pat_no_txt.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel6.add(pat_no_txt, gridBagConstraints);

        date_txt.setBorder(javax.swing.BorderFactory.createTitledBorder("Date"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel6.add(date_txt, gridBagConstraints);

        time_txt.setBorder(javax.swing.BorderFactory.createTitledBorder("Time"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel6.add(time_txt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        jPanel3.add(jDateChooser2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel3, gridBagConstraints);

        jPanel12.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel12.add(persona_txt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel12.add(db_connection_status, gridBagConstraints);

        jLabel2.setText("Login User :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel12.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel12, gridBagConstraints);

        jMenu6.setText("Setup");

        jMenu7.setText("Staff Registry");

        jMenuItem8.setText("Add User");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem8);

        jMenuItem9.setText("Edit User");
        jMenu7.add(jMenuItem9);

        jMenu6.add(jMenu7);

        jMenu8.setText("Operating Parameters");

        jMenuItem10.setText("Pharmacy");
        jMenu8.add(jMenuItem10);

        jMenu9.setText("Laboratory");
        jMenu8.add(jMenu9);

        jMenu6.add(jMenu8);

        jMenu10.setText("Exit");
        jMenu6.add(jMenu10);

        jMenuBar1.add(jMenu6);

        jMenu1.setText("Registration");

        jMenuItem2.setText("Out Patient Registration");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Nursing");

        jMenuItem1.setText("Nursing Center");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Clinical");

        jMenuItem3.setText("Clerking");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setText("Eye");
        jMenu3.add(jMenuItem4);

        jMenuItem6.setText("Dental");
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Laboratory");

        jMenuItem7.setText("Laboratory LIMS");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuBar1.add(jMenu4);

        jMenu11.setText("Billing");

        jMenuItem11.setText("Cash Sale POS");
        jMenu11.add(jMenuItem11);

        jMenuItem12.setText("Dispense Drugs");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem12);

        jMenuBar1.add(jMenu11);

        jMenu5.setText("Reports");

        jMenuItem5.setText("Patient Card");
        jMenu5.add(jMenuItem5);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        Clerking rgf = new Clerking();
        rgf.setVisible(true);

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void pat_name_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pat_name_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pat_name_txtActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        dispose();
        new Clerking().setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void register_reportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_register_reportMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model =(DefaultTableModel) register_report.getModel();
        int i = register_report.getSelectedRow();
        try{
            pat_no_txt.setText(model.getValueAt(i, 0).toString());
            pat_name_txt.setText(model.getValueAt(i, 1).toString());
        }catch(Exception e){
        }
    }//GEN-LAST:event_register_reportMouseClicked

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        dispose();
        new lab().setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
  String path = "";
JFileChooser j = new JFileChooser();
j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
int x = j.showOpenDialog(this);

if (x == JFileChooser.APPROVE_OPTION) {
    path = j.getSelectedFile().getPath();
}

int rowIndex = register_report.getSelectedRow();

if (rowIndex != -1) {
    String Number = register_report.getValueAt(rowIndex, 0).toString();
    String Name = register_report.getValueAt(rowIndex, 1).toString();
    String Urgency = register_report.getValueAt(rowIndex, 2).toString();
    String Age = register_report.getValueAt(rowIndex, 3).toString();
    String Clinic = register_report.getValueAt(rowIndex, 4).toString();
    String Time = register_report.getValueAt(rowIndex, 5).toString();
    String Date = register_report.getValueAt(rowIndex, 6).toString();

    Document doc = new Document();

    try {
        // Save the PDF file with the patient number as the file name
        PdfWriter.getInstance(doc, new FileOutputStream(path + File.separator + Number + ".pdf"));

        // Add header with logo and text
        Paragraph header = new Paragraph();
        // Add logo
        Image logo = Image.getInstance("src\\main\\java\\com\\mycompany\\icons\\logo.png");
        logo.scaleAbsolute(100f, 100f);
        logo.setAlignment(Element.ALIGN_CENTER);
        header.add(logo);

        // Add header text
        Paragraph headerText = new Paragraph();
        headerText.add(new Phrase("BUNGOMA WEMA CENTER\n Fashion Place Plaza 2nd Floor, Hospital Road\n Opposite Shariffs Centre Next to County Comfort Hotel\n P.o Box 2270-50200 Bungoma \n Tel: 0726982221 \n Email: wemacentre@gmail.com \n"));
        headerText.setAlignment(Element.ALIGN_CENTER);
        header.add(headerText);

        // Center the header
        header.setAlignment(Element.ALIGN_CENTER);

        // Add header to the document
        doc.open();
        doc.add(header);
        doc.add(new Paragraph("\n")); // Add a blank paragraph



        // Add patient data table
        PdfPTable report = new PdfPTable(new float[] { 1, 2 }); // 2 columns: labels on left, data on right
        report.setWidthPercentage(100f);

        report.addCell("Patient Number");
        report.addCell(Number);
        report.addCell("Patient Name");
        report.addCell(Name);
        report.addCell("Gender");
        report.addCell(Urgency);
        report.addCell("Age");
        report.addCell(Age);
        report.addCell("ID");
        report.addCell(Clinic);
        report.addCell("Residence");
        report.addCell(Time);
        report.addCell("Clinic");
        report.addCell(Date);

        doc.add(report);
        
        // Add a row with the text "VITAL SIGNS"


PdfPTable vitalSignsRow = new PdfPTable(1);
Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
Phrase vitalSignsPhrase = new Phrase("VITAL SIGNS", boldFont);
vitalSignsRow.addCell(vitalSignsPhrase);
vitalSignsRow.setWidthPercentage(100f);
doc.add(vitalSignsRow);



      
        
        

// Add vital signs data table
PdfPTable vitalSignsTable = new PdfPTable(new float[] { 1, 1, 5 }); // 3 columns: date, time, vital signs
vitalSignsTable.setWidthPercentage(100f);

// Retrieve vital signs data for the selected patient number
String patNumber = pat_no_txt.getText();
String query = "SELECT  height, weight, bmi, systolic, diastolic, temperature, pressure, oxygen_saturation, date, time FROM vitals  WHERE pat_no = ? order by date ASC, time DESC " ;
try {
    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254");
    PreparedStatement pst = conn.prepareStatement(query);
    pst.setString(1, patNumber);
    ResultSet rs = pst.executeQuery();

    // Add the vital signs data to the table
    while (rs.next()) {
        String height = rs.getString("height");
        String weight = rs.getString("weight");
        String bmi = rs.getString("bmi");
        String systolic = rs.getString("systolic");
        String diastolic = rs.getString("diastolic");
        String temperature = rs.getString("temperature");
        String pressure = rs.getString("pressure");
        String oxygenSat = rs.getString("oxygen_saturation");
        String date = rs.getString("date");
        String time = rs.getString("time");

        String vitalSigns = "Height: " + height + ", Weight: " + weight + ", BMI: " + bmi + ", Systolic: " + systolic + ", Diastolic: " + diastolic + ", Temperature: " + temperature + ", Pressure: " + pressure + ", Oxygen Saturation: " + oxygenSat;

        vitalSignsTable.addCell(date);
        vitalSignsTable.addCell(time);
        vitalSignsTable.addCell(vitalSigns);
    }

    // Add the vital signs table to the document
    doc.add(vitalSignsTable);
    
    // Add a row with the text "VITAL SIGNS"
PdfPTable doctorsNotesLabel = new PdfPTable(1);
Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
Phrase phrase = new Phrase("DOCTORS NOTES", bold);
doctorsNotesLabel.addCell(phrase);
doctorsNotesLabel.setWidthPercentage(100f);
doc.add(doctorsNotesLabel);

    
// Add doctors notes data table
PdfPTable doctorsNotesTable = new PdfPTable(new float[] { 1, 1, 4 }); // 3 columns: date, time, note
doctorsNotesTable.setWidthPercentage(100f);

// Retrieve doctors notes data for the selected patient number
String doctorsNotesQuery = "SELECT history, diagnosis, treatment, date, time FROM public.doc_notes WHERE pat_no = ? ORDER BY date ASC, time DESC";
PreparedStatement doctorsNotesPst = conn.prepareStatement(doctorsNotesQuery);
doctorsNotesPst.setString(1, patNumber);
ResultSet doctorsNotesRs = doctorsNotesPst.executeQuery();

// Add the doctors notes data to the table
while (doctorsNotesRs.next()) {
    String history = doctorsNotesRs.getString("history");
    String diagnosis = doctorsNotesRs.getString("diagnosis");
    String treatment = doctorsNotesRs.getString("treatment");
    String date = doctorsNotesRs.getString("date");
    String time = doctorsNotesRs.getString("time");

    String note = "History: " + history + "\nDiagnosis: " + diagnosis + "\nTreatment: " + treatment;
    doctorsNotesTable.addCell(date);
    doctorsNotesTable.addCell(time);
    doctorsNotesTable.addCell(note);
}

// Add the doctors notes table to the document
doc.add(doctorsNotesTable);

    // Add a row with the text "VITAL SIGNS"
PdfPTable investigationslabel = new PdfPTable(1);
//Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
Phrase phraseinv = new Phrase("INVESTIGATION", boldFont);
investigationslabel.addCell(phraseinv);
investigationslabel.setWidthPercentage(100f);
doc.add(investigationslabel);

    
// Add doctors notes data table
PdfPTable investigations = new PdfPTable(new float[] { 1, 1, 4 }); // 3 columns: date, time, note
investigations.setWidthPercentage(100f);

// Retrieve doctors notes data for the selected patient number
String investigationsQuery = "SELECT  test_requested, date, time FROM public.lab_request WHERE patient_no = ? ORDER BY date ASC, time DESC";
PreparedStatement investigationsPst = conn.prepareStatement(investigationsQuery);
investigationsPst.setString(1, patNumber);
ResultSet investigationsRs = investigationsPst.executeQuery();

// Add the doctors notes data to the table
while (investigationsRs.next()) {
    String test = investigationsRs.getString("test_requested");
    String date = investigationsRs.getString("date");
    String time = investigationsRs.getString("time");

    String note = "Investigation: " + test;
    investigations.addCell(date);
    investigations.addCell(time);
    investigations.addCell(note);
}

// Add the doctors notes table to the document
doc.add(investigations);

// Add a row with the text "LABORATORY RESULTS"
PdfPTable labResultsLabel = new PdfPTable(1);
//Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
Phrase phraseLabResults = new Phrase("LABORATORY RESULTS", boldFont);
labResultsLabel.addCell(phraseLabResults);
labResultsLabel.setWidthPercentage(100f);
doc.add(labResultsLabel);

// Add laboratory results data table
PdfPTable labResultsTable = new PdfPTable(new float[] { 1, 1, 1, 1, 1 }); // 5 columns: parameter, result, lower limit, upper limit, status
labResultsTable.setWidthPercentage(100f);



// Retrieve laboratory results data for the selected patient number
String labResultsQuery = "SELECT test, parameter, result, lower, upper, unit, verified, date, time FROM public.lab_results WHERE patient_no = ? ORDER BY date ASC, time DESC";
PreparedStatement labResultsPst = conn.prepareStatement(labResultsQuery);
labResultsPst.setString(1, patNumber);
ResultSet labResultsRs = labResultsPst.executeQuery();

// Initialize variables to track the current test
String currentTest = "";
boolean isFirstRow = true;

// Declare the verified variable
String verified = "";

// Add the laboratory results data to the table
while (labResultsRs.next()) {
    String test = labResultsRs.getString("test");
    String parameter = labResultsRs.getString("parameter");
    String labResult = labResultsRs.getString("result");
    String lower = labResultsRs.getString("lower");
    String upper = labResultsRs.getString("upper");
    String unit = labResultsRs.getString("unit");
    String date = labResultsRs.getString("date");
    String time = labResultsRs.getString("time");

    // If the test changes, add a new row with the test name in bold
    if (!test.equals(currentTest)) {
        if (!isFirstRow) {
            labResultsTable.completeRow(); // Complete the previous row
        }

        // Add a row with the test name
        PdfPCell testCell = new PdfPCell();
        testCell.setColspan(5);
        Phrase testPhrase = new Phrase("PROCEDURE: " + test, boldFont);
        testCell.addElement(testPhrase);
        labResultsTable.addCell(testCell);
        
        // Add headers for the columns
        labResultsTable.addCell(new PdfPCell(new Phrase("Parameter", boldFont)));
        labResultsTable.addCell(new PdfPCell(new Phrase("Result", boldFont)));
        labResultsTable.addCell(new PdfPCell(new Phrase("Lower Limit", boldFont)));
        labResultsTable.addCell(new PdfPCell(new Phrase("Upper Limit", boldFont)));
        labResultsTable.addCell(new PdfPCell(new Phrase("Status", boldFont)));

        currentTest = test;
        isFirstRow = false;
    }

    // Merge the unit with the result and add it to the table
    String resultWithUnit = labResult + " " + unit;
    labResultsTable.addCell(parameter);
    labResultsTable.addCell(resultWithUnit);
    labResultsTable.addCell(lower);
    labResultsTable.addCell(upper);

    // Check if the result is within the limits
    boolean isWithinLimits = (Double.parseDouble(labResult) >= Double.parseDouble(lower) && Double.parseDouble(labResult) <= Double.parseDouble(upper));

    // Set the status cell color based on the result's position relative to the limits
    PdfPCell statusCell = new PdfPCell();
    statusCell.setBackgroundColor(isWithinLimits ? BaseColor.GREEN : BaseColor.RED);
    labResultsTable.addCell(statusCell);
    
    // Retrieve the "verified" value
    verified = labResultsRs.getString("verified");
}

// Complete the last row if there are any remaining cells
if (!isFirstRow) {
    labResultsTable.completeRow();
}

// Add the laboratory results table to the document
doc.add(labResultsTable);


// Add a row with the text "Results Verified By"
PdfPTable verifiedByRow = new PdfPTable(1);
Phrase verifiedByPhrase = new Phrase();
Chunk verifiedByChunk = new Chunk("Results Verified By: ", boldFont);
verifiedByChunk.append(verified);
verifiedByPhrase.add(verifiedByChunk);
verifiedByRow.addCell(verifiedByPhrase);
verifiedByRow.setWidthPercentage(100f);
doc.add(verifiedByRow);


    // Add a row with the text "VITAL SIGNS"
PdfPTable drugslabel = new PdfPTable(1);
//Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
Phrase drugsinv = new Phrase("DRUGS REQUESTED", boldFont);
drugslabel.addCell(drugsinv);
drugslabel.setWidthPercentage(100f);
doc.add(drugslabel);

    
// Add doctors notes data table
PdfPTable drugs = new PdfPTable(new float[] { 1, 1, 4 }); // 3 columns: date, time, note
drugs.setWidthPercentage(100f); // Set the table width to 100%

// Retrieve doctors notes data for the selected patient number
String drugsQuery = "SELECT drug,  quantity_requested, strength, date, time, notes FROM public.drugs_requested WHERE pat_no = ? ORDER BY date ASC, time DESC";
PreparedStatement drugsPst = conn.prepareStatement(drugsQuery);
drugsPst.setString(1, patNumber);
ResultSet drugsRs = drugsPst.executeQuery();

// Add the doctors notes data to the table
while (drugsRs.next()) {
    String test = drugsRs.getString("drug");
    String date = drugsRs.getString("date");
    String time = drugsRs.getString("time");

    String note = "Drug: " + test;
    drugs.addCell(date);
    drugs.addCell(time);
    drugs.addCell(note);
}

// Set the number of columns to span for the note column
int noteColumnSpan = 3;
drugs.getDefaultCell().setColspan(noteColumnSpan);

// Add the doctors notes table to the document
doc.add(drugs);







// Close the database connection
conn.close();



    // Close the document
    doc.close();
    
} catch (SQLException ex) {
    Logger.getLogger(Clerking.class.getName()).log(Level.SEVERE, null, ex);
}

 



        
        
        
        JOptionPane.showMessageDialog(null, "File Downloaded Successfully", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);

    } catch (FileNotFoundException ex) {
        Logger.getLogger(Clerking.class.getName()).log(Level.SEVERE, null, ex);
    } catch (DocumentException ex) {
        Logger.getLogger(Clerking.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(Clerking.class.getName()).log(Level.SEVERE, null, ex);
    }

    doc.close();
}




        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
 // Get the indices of all selected rows
    int[] selectedRows = register_tb.getSelectedRows();

    // Connect to the database
    try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254")) {
        // Create a prepared statement with an INSERT query to insert the data into a table
        String sql = "INSERT INTO public.lab_request ( patient_no, patient_name, test_requested, request_bench, date, time, test_code ) VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement stmt = conn.prepareStatement(sql);
        System.out.println("Connected to the database.");


        // Loop through all selected rows in the JTable
        for (int i = 0; i < selectedRows.length; i++) {
            // Retrieve the selected row
            int row = selectedRows[i];

            // Check if the checkbox in the first column is selected
            Object value = register_tb.getValueAt(row, 0);
            System.out.println("selected.");
            //System.out.println(value.getClass());

            if (value instanceof Boolean) {
                boolean isChecked = ((Boolean) value).booleanValue();
                
                if (isChecked) {

                 //   System.out.println("ischecked.");

                    // Retrieve the data from the selected row
                    String pat_no = pat_no_txt.getText();
                    String pat_name = pat_name_txt.getText();
                    String date = date_txt.getText();
                    String time = time_txt.getText();
                    String column1Value = register_tb.getValueAt(row, 2).toString();
                    String column4Value = register_tb.getValueAt(row, 5).toString();
                    String column5Value = register_tb.getValueAt(row, 1).toString();

                    // Set the parameter values for the prepared statement
                    stmt.setString(1, pat_no);
                    stmt.setString(2, pat_name);
                    stmt.setString(3, column1Value);
                    stmt.setString(4, column4Value);
                    stmt.setString(5, date);
                    stmt.setString(6, time);
                    stmt.setString(7, column5Value);

                    // Execute the prepared statement
                    stmt.executeUpdate();
                }
            }
        }

        // Display a success message to the user
        JOptionPane.showMessageDialog(this, "Data saved successfully!");
    } catch (SQLException ex) {
        // Display an error message to the user
        JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage());
    }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

// TODO add your handling code here:
        // Retrieve the selected rows from the JTable
// Get the indices of all selected rows
        // Connect to the database
// Connect to the database
try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254")) {
        String sql = "INSERT INTO public.drugs_requested ( pat_no, pat_name, code, drug, price, quantity, quantity_requested, strength, date, time, notes ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement stmt = conn.prepareStatement(sql);
        System.out.println("Connected to the database.");

        int rowCount = registered_pharms_tb.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Boolean checkbox = (Boolean) registered_pharms_tb.getValueAt(i, 0).equals(Boolean.TRUE);
            if (checkbox != null && checkbox.booleanValue()) {
                String code = registered_pharms_tb.getValueAt(i, 1).toString();
                String drug = registered_pharms_tb.getValueAt(i, 2).toString();
                String price = registered_pharms_tb.getValueAt(i, 3).toString();
                String strength = registered_pharms_tb.getValueAt(i, 4).toString();
                String units = registered_pharms_tb.getValueAt(i, 5).toString();
                String quantity = registered_pharms_tb.getValueAt(i, 6).toString();
                String quantity_requested = registered_pharms_tb.getValueAt(i, 7).toString();
                String notes = notes_txt.getText();

                stmt.setString(1, pat_no_txt.getText());
                stmt.setString(2, pat_name_txt.getText());
                stmt.setString(3, code);
                stmt.setString(4, drug);
                stmt.setString(5, price);
                stmt.setString(6, quantity);
                stmt.setString(7, quantity_requested);
                stmt.setString(8, strength);
                stmt.setString(9, date_txt.getText());
                stmt.setString(10, time_txt.getText());
                stmt.setString(11, notes);

                stmt.executeUpdate();
                System.out.println("Data inserted successfully!");
            }
        }

        JOptionPane.showMessageDialog(null, "Data saved successfully!");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error saving data: " + ex.getMessage());
        ex.printStackTrace();
    }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try{
            Class.forName("org.postgresql.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kanyingi254");
            String pat_no = pat_no_txt.getText();
            String name = pat_name_txt.getText();
            String history = hist_txt.getText();

            String diagnosis = diag_txt.getText();
            String treatment = treatment_txt.getText();
            String date = date_txt.getText();
            String time = time_txt.getText();

            String query = "INSERT INTO public.doc_notes (pat_no, pat_name, history, diagnosis, treatment,date,time) VALUES (?, ?, ?, ?, ?,?,?);";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, pat_no);
            pst.setString(2, name);
            pst.setString(3, history);

            pst.setString(4, diagnosis);
            pst.setString(5, treatment);
            pst.setString(6, date);
            pst.setString(7, time);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Inserted successfully");
            //Homepa receptionist = new Homepage();
            register_report.show();
        }catch(HeadlessException | ClassNotFoundException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void search_labKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_labKeyReleased
        // TODO add your handling code here:
        DefaultTableModel ob=(DefaultTableModel) register_tb.getModel();
        TableRowSorter<DefaultTableModel> obj= new TableRowSorter<>(ob);
        register_tb.setRowSorter(obj);
        obj.setRowFilter(RowFilter.regexFilter(search_lab.getText()));
    }//GEN-LAST:event_search_labKeyReleased

    private void search_pharmKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_pharmKeyReleased
        // TODO add your handling code here:
         DefaultTableModel ob=(DefaultTableModel) registered_pharms_tb.getModel();
        TableRowSorter<DefaultTableModel> obj= new TableRowSorter<>(ob);
        registered_pharms_tb.setRowSorter(obj);
        obj.setRowFilter(RowFilter.regexFilter(search_pharm.getText()));
    }//GEN-LAST:event_search_pharmKeyReleased

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
         dispose();
        new dispense().setVisible(true);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        dispose();
        new register_user().setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Clerking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Clerking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Clerking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Clerking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Clerking().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel date_txt;
    private javax.swing.JLabel db_connection_status;
    private javax.swing.JTextPane diag_txt;
    private javax.swing.JTextPane hist_txt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable lab_results;
    private javax.swing.JTextField notes_txt;
    private javax.swing.JTextField pat_name_txt;
    private javax.swing.JTextField pat_no_txt;
    private javax.swing.JLabel persona_txt;
    private javax.swing.JTable register_report;
    private javax.swing.JTable register_tb;
    private javax.swing.JTable registered_pharms_tb;
    private javax.swing.JTextField search_lab;
    private javax.swing.JTextField search_pharm;
    private javax.swing.JLabel time_txt;
    private javax.swing.JTextPane treatment_txt;
    // End of variables declaration//GEN-END:variables

   
}
