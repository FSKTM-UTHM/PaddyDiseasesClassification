package uthm.paddy;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MainForm {

	private JFrame frmPaddyDiseaseClassification;
	private JTextField tf_datafile;
	private JTextArea ta_results;
	File fileTarget;
	private JTextField featuresNumber;
	private JTextField classesNumber;

	// for datafile
	private int FeaturesNum = 0, ClassNum = 0;

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
		frmPaddyDiseaseClassification.setBounds(100, 100, 600, 580);
		frmPaddyDiseaseClassification.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPaddyDiseaseClassification.getContentPane().setLayout(null);

		JPanel imageprocessing_panel = new JPanel();
		// for later use
		// imageprocessing_panel.hide();
		JPanel classification_panel = new JPanel();

		JLabel lblNewLabel = new JLabel("Select the Data File");
		final JTextPane tp_results = new JTextPane();
		JButton tbn_classification = new JButton("Classify Data");

		imageprocessing_panel.setBounds(10, 11, 564, 111);
		// FlowLayout fl_imageprocessing_panel = (FlowLayout)
		// imageprocessing_panel.getLayout();
		imageprocessing_panel.setBackground(Color.LIGHT_GRAY);
		imageprocessing_panel.setBorder(
				new TitledBorder(null, "Image to CSV Parser", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmPaddyDiseaseClassification.getContentPane().add(imageprocessing_panel);

		classification_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		classification_panel.setBackground(Color.WHITE);
		classification_panel.setBounds(10, 133, 564, 407);
		frmPaddyDiseaseClassification.getContentPane().add(classification_panel);
		classification_panel.setLayout(null);

		ta_results = new JTextArea();
		ta_results.setEditable(false);
		ta_results.setBounds(20, 45, 523, 351);
		classification_panel.add(ta_results);
		imageprocessing_panel.setLayout(null);

		lblNewLabel.setBounds(22, 25, 93, 14);
		imageprocessing_panel.add(lblNewLabel);

		tp_results.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		tp_results.setBounds(10, 45, 544, 351);
		classification_panel.add(tp_results);

		tf_datafile = new JTextField();
		tf_datafile.setEditable(false);
		tf_datafile.setBounds(125, 22, 321, 20);
		tf_datafile.setColumns(10);
		imageprocessing_panel.add(tf_datafile);

		JButton btn_browse = new JButton("Browse");
		btn_browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				// String path = "No File Selected";
				JFileChooser fileChooser = new JFileChooser();
				// fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", ".csv"));
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileTarget = fileChooser.getSelectedFile();
					tf_datafile.setText(fileTarget.getAbsolutePath());
//			        tp_results.setText(System.getProperty("user.dir")+"\\src\\main\\resources\\"+file.getName());			        
				} else {
					System.out.println("File access cancelled by user.");
				}

			}
		});
		btn_browse.setBounds(456, 21, 98, 23);
		imageprocessing_panel.add(btn_browse);

		JLabel lblNumberOfFeatures = new JLabel("Number of Features");
		lblNumberOfFeatures.setBounds(96, 56, 134, 14);
		imageprocessing_panel.add(lblNumberOfFeatures);

		JLabel lblNumberOfClasses = new JLabel("Number of Classes");
		lblNumberOfClasses.setBounds(96, 81, 134, 14);
		imageprocessing_panel.add(lblNumberOfClasses);

		featuresNumber = new JTextField();
		featuresNumber.setBounds(238, 53, 123, 20);
		imageprocessing_panel.add(featuresNumber);
		featuresNumber.setColumns(10);

		classesNumber = new JTextField();
		classesNumber.setBounds(238, 78, 123, 20);
		imageprocessing_panel.add(classesNumber);
		classesNumber.setColumns(10);

		JButton btnValidate = new JButton("Validate");
		btnValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tf_datafile.getText() != "") {
					Set<String> classes = new HashSet<String>();
					BufferedReader csvReader;
					try {
						csvReader = new BufferedReader(new FileReader(tf_datafile.getText()));
						FeaturesNum = csvReader.readLine().split(",").length - 1;
						String row;
						while ((row = csvReader.readLine()) != null) {
							String[] data = row.split(",");
							classes.add(data[FeaturesNum]);
						}
						csvReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					ClassNum = classes.size();
					featuresNumber.setText("" + FeaturesNum);
					classesNumber.setText("" + ClassNum);
				}
			}
		});
		btnValidate.setBounds(381, 53, 89, 42);
		imageprocessing_panel.add(btnValidate);

		tbn_classification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String result = "";
//				File dataFile = new File(tf_datafile.getText());
//				String destinationFilepath = System.getProperty("user.dir")+"\\src\\main\\resources\\"+dataFile.getName();
//				boolean flag  = copyFileToResources(fileTarget.getAbsolutePath());

//				if(flag) {
				try {
					result = uthm.paddy.MainClassification.classifiyData(fileTarget, FeaturesNum, ClassNum);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				ta_results.setText(result);
			}

//			}
		});
		tbn_classification.setBounds(201, 11, 167, 23);
		classification_panel.add(tbn_classification);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(136, 227, 167, -97);
		classification_panel.add(scrollPane);

	}
}
