/*
 * FileChooserFrame.java
 *
 * --- Last Update: 8/7/2013 ---
 *
 * Update Notes 8/7/2013 by Bryan Pauquette:
 * Changed tns references to database references
 *
 * Update Notes 6/19/2010 6:30 PM by Adrian Wijasa:
 * Changed the access config of SAVE_SQL_INSERT and SAVE_SQL_MERGE static values from default to public,
 * because CSVPanel class from panels package needs to access it.
 *
 * Update Notes 6/18/2010 9:38 PM by Adrian Wijasa:
 * If the text of Approve Button is 'Open', don't do any Action listed under actionPerformed method.
 *
 * Update Notes 5/20/2010 1:06 PM by Adrian Wijasa:
 * Added an ability to Save Data Comparison into a CSV file.
 *
 * Update Notes 5/20/2010 8:39 AM by Adrian Wijasa:
 * Fixed to follow the new CSVWriter constructor format.  It was CSVWriter( main, writeTitles ).
 *
 * Update Notes 5/18/2010 1:38 AM by Adrian Wijasa:
 * Now handles ColumnNotConfiguredException.
 *
 * Update Notes 4/23/2010 9:01 PM by Adrian Wijasa:
 * If task is SAVE_PLAN, return focus to PlanFrame instead of Main when the job completes.
 *
 * Update Notes 4/18/2010 11:30 AM by Adrian Wijasa:
 * This class is made to work with Java 5.
 *
 * Update Notes 3/30/2010 10:53 PM by Adrian Wijasa:
 * Now save and read "Do Not Load Column" configurations.
 *
 * Update Notes 1/31/2010 12:04 AM by Adrian Wijasa:
 * Added a new task: SAVE_SQL_MERGE.
 * Added to actionPerformed: What to do if SAVE_SQL_MERGE task is chosen.
 *
 * Update Notes 1/30/2010 11:56 PM by Adrian Wijasa:
 * Renamed InsertPlanWriter to PlanWriter.
 * Renamed SAVE_SQL to SAVE_SQL_INSERT.
 *
 * Created on March 1, 2007, 5:28 PM
 *
 * CSV Loader
 * Copyright 2007, 2009, 2010 Adrian Wijasa
 *
 * This file is part of CSV Loader.
 *
 * CSV Loader is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CSV Loader is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CSV Loader.  If not, see <http://www.gnu.org/licenses/>.
 */

package forms;

import config.ConfigException;
import config.DatabaseConfiguration;
import csv.ColumnNotConfiguredException;
import csv.CSVMetaDataWriter;
import csv.CSVPanelImageReader;
import csv.CSVPanelImageWriter;
import csv.CSVWriter;
import csv.PlanWriter;
import csv.SQLWriter;
import file.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sql.TableNotFoundException;

/**
 * File Chooser Window.  Allows user to choose a file or name a save file, and then invoke a function
 *
 * @author awijasa
 */
public class FileChooserFrame extends javax.swing.JFrame implements ActionListener {

    /** Creates new form FileChooserFrame */
    public FileChooserFrame(
        Main main
        , int task // Possible inputs: UPLOAD_DATABASE_CONFIGURATION, APPEND_DATABASE_CONFIGURATION, UPLOAD_CSV, SAVE_CSV, SAVE_DATA_COMPARISON, SAVE_SQL_INSERT, SAVE_SQL_MERGE, SAVE_IMAGE, UPLOAD_IMAGE, SAVE_PLAN, SAVE_METADATA
    ) {
        this.main = main;
        this.task = task;
        initFormTitle();            // Initialize Form Title based on the task value
        initComponents();
        initApproveButtonText();    // Initialize Approve Button text based on the task value
        initFilter();               // Initialize File Filter based on the task value
        initButtons();              // Add Action Listeners to Approve and Cancel Buttons

        if( task == SAVE_CSV || task == SAVE_DATA_COMPARISON || task == SAVE_SQL_INSERT ||
                task == SAVE_SQL_MERGE || task == SAVE_IMAGE || task == SAVE_PLAN || task == SAVE_METADATA )
            initFileTextField();

        setLocation( main.getX() + 109, main.getY() + 101 ); // Locate this form at the center of Main Window
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogType(javax.swing.JFileChooser.CUSTOM_DIALOG);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 506, Short.MAX_VALUE)
            .add(fileChooser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 326, Short.MAX_VALUE)
            .add(fileChooser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if( task == SAVE_DATA_COMPARISON || task == SAVE_PLAN ) {
            main.planFrame.setEnabled( true );
            main.planFrame.requestFocus();
        }
        else {
            main.setEnabled( true );
            main.requestFocus();
        }
    }//GEN-LAST:event_formWindowClosed

    void disposeFileChooser() {
        if( task == SAVE_DATA_COMPARISON || task == SAVE_PLAN ) {
            main.planFrame.setEnabled( true );
            main.planFrame.requestFocus();
        }
        else {
            main.setEnabled( true );
            main.requestFocus();
        }

        dispose();
    }

    public File getFile() {
        String fileName = fileTextField.getText();

        if( task == SAVE_CSV || task == SAVE_DATA_COMPARISON || task == SAVE_METADATA ) {
            if( fileName.endsWith( ".csv" ) || fileName.endsWith( ".CSV" ) )
                return new File( fileChooser.getCurrentDirectory(), fileName );
            else
                return new File( fileChooser.getCurrentDirectory(), fileName + ".csv" );
        }
        else if( task == SAVE_SQL_INSERT || task == SAVE_SQL_MERGE ) {
            if( fileName.endsWith( ".sql" ) || fileName.endsWith( ".SQL" ) )
                return new File( fileChooser.getCurrentDirectory(), fileName );
            else
                return new File( fileChooser.getCurrentDirectory(), fileName + ".sql" );
        }
        else if( task == SAVE_IMAGE ) {
            if( fileName.endsWith( ".cfg" ) || fileName.endsWith( ".CFG" ) )
                return new File( fileChooser.getCurrentDirectory(), fileName );
            else
                return new File( fileChooser.getCurrentDirectory(), fileName + ".cfg" );
        }
        else {
            if( fileName.endsWith( ".pln" ) || fileName.endsWith( ".PLN" ) )
                return new File( fileChooser.getCurrentDirectory(), fileName );
            else
                return new File( fileChooser.getCurrentDirectory(), fileName + ".pln" );
        }
    }

    /* Initialize Approve Button text based on the task value */
    private void initApproveButtonText() {
        if( task == APPEND_DATABASE_CONFIGURATION )
            fileChooser.setApproveButtonText( "Append" );
        else if( task == UPLOAD_DATABASE_CONFIGURATION || task == UPLOAD_CSV || task == UPLOAD_IMAGE )
            fileChooser.setApproveButtonText( "Upload" );
        else
            fileChooser.setApproveButtonText( "Save" );
    }

    /* Add Action Listeners to Approve and Cancel Buttons */
    private void initButtons() {
        JPanel buttonPanel = (JPanel)( (JPanel)fileChooser.getComponent( 3 ) ).getComponent( 3 );
        approveButton = (JButton)buttonPanel.getComponent( 0 );
        cancelButton = (JButton)buttonPanel.getComponent( 1 );
        approveButton.addActionListener( this );
        cancelButton.addActionListener( this );
    }

    private void initFileTextField() {
        fileTextField = (JTextField)( (JPanel)( (JPanel)fileChooser.getComponent( 3 ) ).getComponent( 0 ) ).getComponent( 1 );
    }

    /* Initialize File Filter based on the task value */
    private void initFilter() {
        FileNameExtensionFilter filter;

        if( task == UPLOAD_DATABASE_CONFIGURATION || task == APPEND_DATABASE_CONFIGURATION )
            filter = new FileNameExtensionFilter( "XML Files", "xml" );
        else if( task == UPLOAD_CSV || task == SAVE_CSV || task == SAVE_DATA_COMPARISON ||
            task == SAVE_METADATA )
            filter = new FileNameExtensionFilter( "CSV Files", "csv" );
        else if( task == SAVE_SQL_INSERT || task == SAVE_SQL_MERGE )
            filter = new FileNameExtensionFilter( "SQL Files", "sql" );
        else if( task == UPLOAD_IMAGE || task == SAVE_IMAGE )
            filter = new FileNameExtensionFilter( "CSV Panel Image Config File", "cfg" );
        else
            filter = new FileNameExtensionFilter( "Insert Plan Report File", "pln" );

        fileChooser.setFileFilter( filter );
    }

    /* Initialize Form Title based on the task value */
    private void initFormTitle() {
        if( task == UPLOAD_DATABASE_CONFIGURATION )
            setTitle( "Select the DATABASES.XML to be Uploaded" );
        else if( task == APPEND_DATABASE_CONFIGURATION )
            setTitle( "Select the DATABASES.XML to be Appended" );
        else if( task == UPLOAD_CSV )
            setTitle( "Select the CSV File to be Converted to a SQL Insert Script" );
        else if( task == SAVE_CSV )
            setTitle( "Choose the location to save your modified CSV File" );
        else if( task == SAVE_DATA_COMPARISON )
            setTitle( "Choose the location to save your Data Comparison" );
        else if( task == SAVE_SQL_INSERT )
            setTitle( "Choose the location to save your SQL Insert Script" );
        else if( task == SAVE_SQL_MERGE )
            setTitle( "Choose the location to save your SQL Insert Script" );
        else if( task == SAVE_IMAGE )
            setTitle( "Choose the location to save your CSV Panel Image Configuration File" );
        else if( task == UPLOAD_IMAGE )
            setTitle( "Select the CFG File to be Uploaded" );
        else if( task == SAVE_PLAN )
            setTitle( "Choose the location to save your Insert Plan Report File" );
        else
            setTitle( "Choose the location to save your Data Type & Length Report" );
    }

    public void actionPerformed( ActionEvent e ) {
        if( e.getSource() == approveButton ) {
            if( !approveButton.getText().equals( "Open" ) ) {
                if( task == UPLOAD_DATABASE_CONFIGURATION ) {
                    try {
                        databaseConfigurationReader = new DatabaseConfiguration();
                        databaseConfigurationReader.setConfigurationFile(fileChooser.getSelectedFile());
                        databaseConfigurationReader.read();
                        main.databasePanel.setDatabaseTableContent( databaseConfigurationReader.getDatabaseVector() );
                        
                        disposeFileChooser();
                    }
                    catch( ConfigException ce ) {
                        main.fileChooser.setEnabled( false );
                        new TNSExceptionFrame( main, ce ).setVisible( true );
                    }
                    catch( NullPointerException ne ) {} // Prevents Error Message when Approve Button is clicked without any file being selected
                }
                else if( task == APPEND_DATABASE_CONFIGURATION ) {
                    try {
                        databaseConfigurationReader = new DatabaseConfiguration(); // Digest selected XML file
                        databaseConfigurationReader.setConfigurationFile(fileChooser.getSelectedFile());
                        main.databasePanel.appendTNSTableContent(databaseConfigurationReader.getDatabaseVector());       // Displays the Database Configurations obtained from the XML file
                        
                        disposeFileChooser();
                    }
                    catch( ConfigException ce ) {
                        main.fileChooser.setEnabled( false );
                        new TNSExceptionFrame( main, ce ).setVisible( true );
                    }
                    catch( NullPointerException ne ) {} // Prevents Error Message when Approve Button is clicked without any file being selected
                }
                else if( task == UPLOAD_CSV ) { // UPLOAD_CSV
                    setEnabled( false );
                    new ColTitleFrame( main ).setVisible( true );
                }
                else if( task == SAVE_CSV || task == SAVE_DATA_COMPARISON ) {
                    setVisible( false );
                    int choice = JOptionPane.showConfirmDialog( null, "Do you want to include column titles?", "Column Titles", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE );

                    if( task == SAVE_CSV )
                        main.setTitle( "CSV Loader 2.2 - Loading..." );
                    else
                        main.planFrame.setTitle( "Plan - Loading..." );

                    AbstractList dataAbstractList;
                    AbstractList colTitleAbstractList;

                    /* Source Data: CSV Data Snapshot */
                    if( task == SAVE_CSV ) {
                        dataAbstractList = main.csvPanel.currentImage.csvDataArrayList;
                        colTitleAbstractList = main.csvPanel.currentImage.columnArrayList;
                    }

                    /* Source Data: Data Comparison */
                    else {
                        dataAbstractList = main.csvPanel.currentImage.plan.dataComparisonList;
                        colTitleAbstractList = main.csvPanel.currentImage.plan.columnTitleList;
                    }

                    if( choice == JOptionPane.YES_OPTION ) {
                        try {
                            new CSVWriter( main, true, dataAbstractList, colTitleAbstractList );
                        }
                        catch( IOException ie ) {
                            JOptionPane.showMessageDialog( null, ie.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE );
                        }
                    }
                    else if( choice == JOptionPane.NO_OPTION ) {
                        try {
                            new CSVWriter( main, false, dataAbstractList, colTitleAbstractList );
                        }
                        catch( IOException ie ) {
                            JOptionPane.showMessageDialog( null, ie.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE );
                        }
                    }

                    if( task == SAVE_CSV )
                        main.setTitle( "CSV Loader 2.2" );
                    else
                        main.planFrame.setTitle( "Plan" );

                    disposeFileChooser();
                }
                else if( task == SAVE_SQL_INSERT ) {
                    setVisible( false );

                    main.setTitle( "CSV Loader 2.2 - Loading..." );

                    try {
                        new SQLWriter( main, SQLWriter.INSERT );
                    }
                    catch( ClassNotFoundException ce ) {
                        JOptionPane.showMessageDialog( null, ce.getMessage(), "JDBC Driver Error", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( ColumnNotConfiguredException colE ) {
                        JOptionPane.showMessageDialog( null, colE.getMessage(), "Column Config Error", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( IOException ie ) {
                        JOptionPane.showMessageDialog( null, ie.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( SQLException se ) {
                        JOptionPane.showMessageDialog( null, se.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( TableNotFoundException te ) {
                        JOptionPane.showMessageDialog( null, te.getMessage(), "Table Not Found", JOptionPane.ERROR_MESSAGE );
                    }

                    main.setTitle( "CSV Loader 2.2" );

                    disposeFileChooser();
                }
                else if( task == SAVE_SQL_MERGE ) {
                    setVisible( false );

                    main.setTitle( "CSV Loader 2.2 - Loading..." );

                    try {
                        new SQLWriter( main, SQLWriter.MERGE );
                    }
                    catch( ClassNotFoundException ce ) {
                        JOptionPane.showMessageDialog( null, ce.getMessage(), "JDBC Driver Error", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( ColumnNotConfiguredException colE ) {
                        JOptionPane.showMessageDialog( null, colE.getMessage(), "Column Config Error", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( IOException ie ) {
                        JOptionPane.showMessageDialog( null, ie.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( SQLException se ) {
                        JOptionPane.showMessageDialog( null, se.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( TableNotFoundException te ) {
                        JOptionPane.showMessageDialog( null, te.getMessage(), "Table Not Found", JOptionPane.ERROR_MESSAGE );
                    }

                    main.setTitle( "CSV Loader 2.2" );

                    disposeFileChooser();
                }
                else if( task == SAVE_IMAGE ) {
                    try {
                        new CSVPanelImageWriter( main );
                    }
                    catch( IOException ie ) {
                        JOptionPane.showMessageDialog( null, ie.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE );
                    }

                    disposeFileChooser();
                }
                else if( task == UPLOAD_IMAGE ) {
                    try {
                        CSVPanelImageReader imageReader = new CSVPanelImageReader( main );

                        int csvLoaderColCount = main.csvPanel.currentImage.sqlArrayList.size();
                        int imageReaderColCount = imageReader.sqlArrayList.size();

                        if( csvLoaderColCount == imageReaderColCount ) {
                            main.csvPanel.currentImage.sqlArrayList = new ArrayList<ArrayList<String>>( imageReader.sqlArrayList );
                            main.csvPanel.currentImage.includeArrayList = new ArrayList<Boolean>( imageReader.includeArrayList );
                            main.csvPanel.reloadCol();
                        }
                        else {
                            JOptionPane.showMessageDialog( null, "CSV Loader=" + csvLoaderColCount + " Configuration=" + imageReaderColCount, "Column Count Mismatch", JOptionPane.ERROR_MESSAGE );
                        }
                    }
                    catch( FileNotFoundException fe ) {
                        JOptionPane.showMessageDialog( null, fe.getMessage(), "File Not Found", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( IOException ie ) {
                        JOptionPane.showMessageDialog( null, ie.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE );
                    }

                    disposeFileChooser();
                }
                else if( task == SAVE_PLAN ) {
                    setVisible( false );

                    main.setTitle( "CSV Loader 2.2 - Loading..." );

                    try {
                        new PlanWriter( main );
                    }
                    catch( IOException ie ) {
                        JOptionPane.showMessageDialog( null, ie.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE );
                    }
                    catch( SQLException se ) {
                        JOptionPane.showMessageDialog( null, se.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE );
                    }

                    main.setTitle( "CSV Loader 2.2" );

                    disposeFileChooser();
                }
                else {
                    setVisible( false );
                    main.setTitle( "CSV Loader 2.2 - Loading..." );

                    try {
                        new CSVMetaDataWriter( main );
                    }
                    catch( IOException ie ) {
                        JOptionPane.showMessageDialog( null, ie.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE );
                    }

                    main.setTitle( "CSV Loader 2.2" );

                    disposeFileChooser();
                }
            }
        }
        else {
            disposeFileChooser();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JFileChooser fileChooser;
    // End of variables declaration//GEN-END:variables

    private int task;
    private JButton approveButton;
    private JButton cancelButton;
    private JTextField fileTextField;
    private Main main;
    public DatabaseConfiguration databaseConfigurationReader;
    static final int UPLOAD_DATABASE_CONFIGURATION = 0;
    static final int APPEND_DATABASE_CONFIGURATION = 1;
    static final int UPLOAD_CSV = 2;
    public static final int SAVE_CSV = 3;
    public static final int SAVE_DATA_COMPARISON = 4;
    public static final int SAVE_SQL_INSERT = 5;
    public static final int SAVE_SQL_MERGE = 6;
    public static final int SAVE_IMAGE = 7;
    static final int UPLOAD_IMAGE = 8;
    public static final int SAVE_PLAN = 9;
    public static final int SAVE_METADATA = 10;
}
