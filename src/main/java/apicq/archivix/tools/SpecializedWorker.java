package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

/**
 * A Worker with a Dialog for monitoring progression.
 * Adds SQL request support. All workers must subclass this class.
 */
public class SpecializedWorker extends SwingWorker<Void, String> {


    public static final Logger log = Logger.getLogger("Archivix");
    private static Connection con;
    protected final MainFrame mainFrame;
    private final ProgressDialog progressDialog;
    private boolean error = false;
    private StringBuilder errorBuilder = new StringBuilder();

    /**
     * Constructor
     *
     * @param mainFrame
     * @param subject
     */
    public SpecializedWorker(MainFrame mainFrame,
                             String subject) {

        this.mainFrame = mainFrame;

        // configure progressDialog :
        progressDialog = new ProgressDialog(mainFrame, subject);
        progressDialog.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel(true);
            }
        });
        addPropertyChangeListener(progressDialog);
    }


    /**
     * Sets the value of the progress string
     * @param s : the value of the progress string
     */
    public void setString(String s){
        progressDialog.getProgressBar().setString(s);
    }


    /**
     * sets the maximum value for the progress bar
     * @param n
     */
    public void setMaximum(int n){
        progressDialog.getProgressBar().setMaximum(n);
    }

    /**
     *
     * @param b
     */
    public void setIndeterminate(boolean b){
        progressDialog.getProgressBar().setIndeterminate(b);
    }

    public boolean isError() {
        return error;
    }

    /**
     * method used to build sql strings.
     *
     * @param prefix    prefix
     * @param suffix    suffix
     * @param separator separator
     * @param args      args
     * @return a string
     */
    public static String stringify(String prefix,
                                   String suffix,
                                   String separator,
                                   String... args) {
        if (args == null) return "";
        StringBuilder sb = new StringBuilder();
        boolean isSecond = false;
        for (String arg : args) {
            String trimmedArg = arg.trim();
            if (trimmedArg.length() > 0) {
                if (isSecond) {
                    sb.append(separator + prefix + trimmedArg + suffix);
                } else {
                    isSecond = true;
                    sb.append(prefix + trimmedArg + suffix);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Getter
     *
     * @return
     */
    protected ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    /**
     * Returns a preparedStatement, prevents connection problem
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public PreparedStatement pStatement(String sql) throws SQLException {
        if (con == null || con.isClosed()) {
            con = DriverManager.getConnection("jdbc:sqlite:" +
                    mainFrame.databaseFile());
        }
        return con.prepareStatement(sql);
    }

    /**
     * Start worker, show dialog,manage errors:
     */
    public final void start() {
        try {
            execute();
        } catch (Exception e) {
            error = true;
            addError(e.toString());
            log.warning(e.toString());
        }
        progressDialog.setVisible(true);
    }

    @Override
    protected void done() {
        progressDialog.setVisible(false);
        // Try to close connection :
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.warning(e.toString());
                error = true;
                addError("Impossible de fermer la connection");
            }
        }

        // In case of error, show a dialog with all errors
        if (error) {
            final JDialog dialog = new JDialog(mainFrame);
            dialog.setLayout(new MigLayout());
            dialog.setModal(true);
            JTextArea textArea = new JTextArea(20, 30);
            textArea.setText(errorBuilder.toString());
            dialog.add(new JScrollPane(textArea),"wrap");
            JButton quitButton = new JButton("Fermer");
            quitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            });
            dialog.add(quitButton);
            dialog.setMinimumSize(new Dimension(400, 300));
            dialog.pack();
            dialog.setVisible(true);
            return;
        }
    }

    @Override
    protected void process(List<String> chunks) {
        for (String str : chunks) progressDialog.getProgressBar().setString(str);
    }

    /**
     * Report errors:
     *
     * @param errorString
     */
    protected void addError(String errorString) {
        error = true; // an error string means there is an error.
        errorBuilder.append(errorString + "\n");
        log.warning(errorString);
    }


    /**
     * Override in subclasses
     *
     * @return
     * @throws Exception
     */
    @Override
    protected Void doInBackground() throws Exception {
        return null;
    }

    /**
     * dialog to show and watch result :
     * pickup progress bound property from swingworker
     */
    class ProgressDialog extends JDialog implements PropertyChangeListener {

        private final JProgressBar progressBar;
        private final JButton cancelButton;
        private final JLabel messageField;

        /**
         * Constructor
         *
         * @param mainFrame
         * @param subject
         */
        ProgressDialog(MainFrame mainFrame, String subject) {
            super(mainFrame);
            setModal(true);
            setLayout(new MigLayout());
            setTitle("Tâche en cours...");
            messageField = new JLabel(subject);
            add(messageField, "wrap");
            progressBar = new JProgressBar();
            progressBar.setMaximum(100);
            progressBar.setIndeterminate(false);
            progressBar.setStringPainted(true);
            add(progressBar, "grow");
            cancelButton = new JButton("Annuler");
            add(cancelButton, "");
            pack();
        }

        public JProgressBar getProgressBar() {
            return progressBar;
        }

        public JButton getCancelButton() {
            return cancelButton;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        }
    }//class


}