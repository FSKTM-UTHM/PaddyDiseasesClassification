package uthm.paddy;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.FileUtils;
import javax.swing.JTextArea;

public class MainForm {

	private JFrame frmPaddyDiseaseClassification;
	private JTextField tf_datafile;
	private JTextArea ta_results;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frmPaddyDiseaseClassification.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unused")
	private void initialize() {
		frmPaddyDiseaseClassification = new JFrame();
		frmPaddyDiseaseClassification.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frmPaddyDiseaseClassification.dispose();
			}
		});
		frmPaddyDiseaseClassification.setResizable(false);
		frmPaddyDiseaseClassification.setTitle("Paddy Disease Classification");
		frmPaddyDiseaseClassification.setBounds(100, 100, 600, 550);
		frmPaddyDiseaseClassification.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPaddyDiseaseClassification.getContentPane().setLayout(null);
		
		JPanel imageprocessing_panel = new JPanel();
		JPanel classification_panel = new JPanel();
		JLabel lblNewLabel = new JLabel("Select the Data File");
		final JTextPane tp_results = new JTextPane();
		JButton tbn_classification = new JButton("Classify Data");
		
		imageprocessing_panel.setBounds(10, 11, 564, 82);
		FlowLayout fl_imageprocessing_panel = (FlowLayout) imageprocessing_panel.getLayout();
		imageprocessing_panel.setBackground(Color.LIGHT_GRAY);
		imageprocessing_panel.setBorder(new TitledBorder(null, "Image to CSV Parser", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmPaddyDiseaseClassification.getContentPane().add(imageprocessing_panel);
		
		
		classification_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		classification_panel.setBackground(Color.WHITE);
		classification_panel.setBounds(10, 104, 564, 406);
		frmPaddyDiseaseClassification.getContentPane().add(classification_panel);
		classification_panel.setLayout(null);
		
		ta_results = new JTextArea();
		ta_results.setEditable(false);
		ta_results.setBounds(20, 84, 523, 304);
		classification_panel.add(ta_results);
		
		
		lblNewLabel.setBounds(10, 15, 93, 14);
		classification_panel.add(lblNewLabel);
		
		
		tp_results.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		tp_results.setBounds(10, 77, 544, 318);
		classification_panel.add(tp_results);
		
		tf_datafile = new JTextField();
		tf_datafile.setEditable(false);
		tf_datafile.setBounds(113, 12, 327, 20);
		tf_datafile.setColumns(10);
		classification_panel.add(tf_datafile);
		
		
		JButton btn_browse = new JButton("Browse");
		btn_browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				//String path = "No File Selected";
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(null);
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
			    	File fileTarget = fileChooser.getSelectedFile();
			        tf_datafile.setText(fileTarget.getAbsolutePath()) ;			        
//			        tp_results.setText(System.getProperty("user.dir")+"\\src\\main\\resources\\"+file.getName());			        
			    } else {
			        System.out.println("File access cancelled by user.");
			    }
			    
			}
		});
		btn_browse.setBounds(450, 11, 104, 23);
		classification_panel.add(btn_browse);
		
		
		tbn_classification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String result = "";
				File dataFile = new File(tf_datafile.getText());
//				String destinationFilepath = System.getProperty("user.dir")+"\\src\\main\\resources\\"+dataFile.getName();
				boolean flag  = copyFileToResources(dataFile.getAbsolutePath());
				
				if(flag) {
					try {
						result = MainClassification.classifiyData(dataFile.getName());
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					ta_results.setText(result);
				}
				
			}
		});
		tbn_classification.setBounds(190, 43, 167, 23);
		classification_panel.add(tbn_classification);
		
		
	}
	
	private boolean copyFileToResources(String filepath) {
		boolean flag = false;
		File source = new File(filepath);
		File dest = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\"+source.getName());
		try {
		    FileUtils.copyFile(source, dest);
		    if(dest.isFile())
		    	flag = true;
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return flag;
	}
}
