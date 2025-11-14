package schneiderlab.tools.radialprojection.views.userinterfacecomponents;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;
import org.jdesktop.swingx.*;
import javax.swing.table.DefaultTableModel;

import ij.process.AutoThresholder;
import net.miginfocom.swing.*;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.convertczitotif.RotateDirection;
/*
 * Created by JFormDesigner on Fri Nov 14 16:45:10 CET 2025
 */



/**
 * @author anhminh
 */
public class MainView extends JFrame {
	private JFrame parentFrame;

	public MainView(Context context, JFrame parentFrame) {
		initComponents();
		this.parentFrame=parentFrame;
//		this.getComboBoxRoateDirectionConvertCzi2Tif().setSelectedIndex(0);
	}
	public JFrame getParentFrame() {
		return parentFrame;
	}

	public JButton getButtonTabCzi2Tif(){ return buttonTabCzi2Tif;}
	public JButton getButtonTabVesselSegmentation(){ return buttonTabVesselSegmentation;}
	public JButton getButtonTabRadialProjection(){ return buttonTabRadialProjection;}
	public JButton getButtonTabAnalysis(){ return buttonTabAnalysis;}
	public JButton getButtonTabBatchMode(){ return buttonTabBatchMode;}

	public JPanel getPanelMainRight(){
		return panelMainRight;
	}

	public CardLayout getMainPanelCardLayout() {
		return (CardLayout) panelMainRight.getLayout();
	}

	public JPanel getPanelConvertCzi2Tif() {
		return panelConvertCzi2Tif;
	}

	public JButton getButtonBrowseConvertCzi2Tif() {
		return buttonBrowseConvertCzi2Tif;
	}

	public JTextField getTextFieldConvertCzi2Tif() {
		return textFieldConvertCzi2Tif;
	}

	public JCheckBox getCheckBoxBgSubConvertCzi2Tif() {
		return checkBoxBgSubConvertCzi2Tif;
	}

	public JLabel getLabelRollingConvertCzi2Tif() {
		return labelRollingConvertCzi2Tif;
	}

	public JSpinner getSpinnerRollingConvertCzi2Tif() {
		return spinnerRollingConvertCzi2Tif;
	}

	public JLabel getLabelEnhanceConstConvertCzi2Tif() {
		return labelEnhanceConstConvertCzi2Tif;
	}

	public JLabel getLabelSaturateConvertCzi2Tif() {
		return labelSaturateConvertCzi2Tif;
	}

	public JSpinner getSpinnerSaturateConvertCzi2Tif() {
		return spinnerSaturateConvertCzi2Tif;
	}

	public JLabel getLabelpercentSignConvertCzi2Tif() {
		return labelpercentSignConvertCzi2Tif;
	}

	public JCheckBox getCheckBoxRotateConvertCzi2Tif() {
		return checkBoxRotateConvertCzi2Tif;
	}

	public JComboBox<String> getComboBoxRoateDirectionConvertCzi2Tif() {
		return comboBoxRoateDirectionConvertCzi2Tif;
	}

	public JButton getButtonOkConvertCzi2Tif() {
		return buttonOkConvertCzi2Tif;
	}

	public JTextField getTextFieldStatusConvertCzi2Tif() {
		return textFieldStatusConvertCzi2Tif;
	}

	public JProgressBar getProgressBarConvertCzi2Tif() {
		return progressBarConvertCzi2Tif;
	}

	public JTabbedPane getTabPanelVesselsSegmentation() {
		return tabbedPaneVesselSegmentation;
	}

	public JTabbedPane getTabbedPaneVesselSegmentation() {
		return tabbedPaneVesselSegmentation;
	}

	public JPanel getPanelImageListVesselSegmentation() {
		return panelImageListVesselSegmentation;
	}

	public JButton getButtonAddFile() {
		return buttonAddFile;
	}

	public JButton getButtonAddFolder() {
		return buttonAddFolder;
	}

	public JButton getButtonRemove() {
		return buttonRemove;
	}

	public JButton getButtonClear() {
		return buttonClear;
	}

	public JScrollPane getScrollPaneVesselSegmentation() {
		return scrollPaneVesselSegmentation;
	}

	public JPanel getPanelParametersVesselSegmentation() {
		return panelParametersVesselSegmentation;
	}

	public JLabel getLabelTargetXYPixelSize() {
		return labelTargetXYPixelSize;
	}

	public JSpinner getSpinnerXYPixelSizeCreateSideView() {
		return spinnerXYPixelSizeCreateSideView;
	}

	public JLabel getLabelTargetZPixelSize() {
		return labelTargetZPixelSize;
	}

	public JSpinner getSpinnerZPixelSizeCreateSideView() {
		return spinnerZPixelSizeCreateSideView;
	}

	public JLabel getLabelAnalysisWindow() {
		return labelAnalysisWindow;
	}

	public JSpinner getSpinnerAnalysisWindow() {
		return spinnerAnalysisWindow;
	}

	public JLabel getLabelPreWatershedSmoothing() {
		return labelPreWatershedSmoothing;
	}

	public JSpinner getSpinnerPreWatershedSmoothing() {
		return spinnerPreWatershedSmoothing;
	}

//	public JLabel getLabelSliceIndexforTuning() {
//		return labelSliceIndexforTuning;
//	}
//
//	public JSpinner getSpinnerSliceIndexForTuning() {
//		return spinnerSliceIndexForTuning;
//	}

	public JLabel getLabelInnerVesselRadius() {
		return labelInnerVesselRadius;
	}

	public JSpinner getSpinnerInnerVesselRadius() {
		return spinnerInnerVesselRadius;
	}

	public JLabel getLabelHybridWeight() {
		return labelHybridWeight;
	}

	public JLabel getLabelLigninHybridWeight() {
		return labelLigninHybridWeight;
	}

	public JSlider getSliderHybridWeight() {
		return sliderHybridWeight;
	}

	public JLabel getLabelCelluloseHybridWeight() {
		return labelCelluloseHybridWeight;
	}

	public JButton getButtonCreateSideView() {
		return buttonCreateSideView;
	}

	public JButton getButtonProjAndSmooth() {
		return buttonProjAndSmooth;
	}

	public JButton getButtonSelectCentroid() {
		return buttonSelectCentroid;
	}

	public JButton getButtonWatershed() {
		return buttonWatershed;
	}

	public JButton getButtonProcessWholeStack() {
		return buttonProcessWholeStack;
	}

	public JButton getButtonMoveToRadialProjection() {
		return buttonMoveToRadialProjection;
	}

	public JTextField getTextField2StatusVesselSegmentation() {
		return textField2StatusVesselSegmentation;
	}

	public JButton getButtonRunRadialProjection() {
		return buttonRunRadialProjection;
	}

	public JTable getTableAddedFileVesselSegmentation() {
		return tableAddedFileVesselSegmentation;
	}

	public JTextField getTextFieldRadialProjection() {return textFieldRadialProjection;}

	public JProgressBar getProgressBarVesselSegmentation(){return progressBarVesselSegmentation;}

	public JPanel getPanel3RadialProjection() { return panel3RadialProjection;}

//	public JButton getButtonUnrollVessel() { return buttonUnrollVessel; }

	public JButton getButtonLegacyBandMeasurement() { return buttonLegacyBandMeasurement;}

//	public JButton getButtonSegmentationBySplitting() { return buttonSegmentationBySplitting;}
//
//	public JButton getButtonCustomSkeletonize() { return buttonCustomSkeletonize;}
//
//	public JSpinner getSpinnerPercentageForSplitting() {return spinnerPercentageForSplitting;}
//
//	public JComboBox<AutoThresholder.Method> getComboboxAutoThresholdingMethod (){return comboBoxAutoThresholdingMethod;}

	public JTextField getTextFieldStatusRadialProjection(){return textFieldStatusRadialProjection;}

	public JProgressBar getProgressBarRadialProjection(){return progressBarRadialProjection;}

	public JButton getButtonMoveToAnalysis() {return buttonMoveToAnalysis;}

	public JTabbedPane getTabbedPaneAnalysis() { return  tabbedPaneAnalysis;}

	public JPanel getBandsAndGapsPanel() {return  bandsAndGapsPanel;}

	public JSpinner getSpinnerNumberOfLineScan() {return  spinnerNumberOfLineScan;}
	public JSpinner getSpinnerLineScanLength() {return  spinnerLineScanLength;}
	public JButton getButtonBrowseOutputPath(){ return  buttonBrowseOutputPath;}
	public JTextField getTextFieldOutputPath(){ return  textFieldOutputPath;}
	public JButton getButtonComputeAnisotropy(){return buttonComputeAnisotropy;}
	public JButton getButtonExportResultToCSV(){return buttonExportResultToCSV;}
	public JCheckBox getCheckBoxCombineResultCSV(){return checkBoxCombineResultCSV;}
	public JTextArea getTextAreaBandGapResult(){return textAreaBandGapResult;}
	public JTextArea getTextAreaAnisotropyResult(){return textAreaAnisotropyResult;}
	public JTable getTableAnalysisInputImage(){return tableAnalysisInputImage;}
	public JSpinner getSpinnerNoRandomBoxes(){return spinnerNumberRandomBoxes;}
	public JSpinner getSpinnerRandomBoxWidth(){return spinnerRandomBoxWidth;}
	public JButton getButtonExportToXLSX(){return  buttonExportToXLSX;}
	public JCheckBox getCheckBoxCombineResultXLSX(){return  checkBoxCombineResultXLSX;}
	public JTextField getTextFieldCurrentFileSegmentation(){return textFieldCurrentFileSegmentation;}
	public JPanel getPanelOrientationAndAnisotropy(){return panelOrientationAndAnisotropy;}
	public JTable getTableFileCziToTiff(){return tableFileCziToTiff;}
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Educational license - Anh Minh Do
		panelLeftMenu = new JPanel();
		panelGroupButtonTab = new JPanel();
		buttonTabCzi2Tif = new JButton();
		separator1 = new JSeparator();
		label1 = new JLabel();
		buttonTabVesselSegmentation = new JButton();
		buttonTabRadialProjection = new JButton();
		buttonTabAnalysis = new JButton();
		separator2 = new JSeparator();
		label2 = new JLabel();
		buttonTabBatchMode = new JButton();
		panelMainRight = new JPanel();
		panelConvertCzi2Tif = new JPanel();
		buttonBrowseConvertCzi2Tif = new JButton();
		textFieldConvertCzi2Tif = new JTextField();
		checkBoxBgSubConvertCzi2Tif = new JCheckBox();
		labelRollingConvertCzi2Tif = new JLabel();
		spinnerRollingConvertCzi2Tif = new JSpinner();
		labelEnhanceConstConvertCzi2Tif = new JLabel();
		labelSaturateConvertCzi2Tif = new JLabel();
		spinnerSaturateConvertCzi2Tif = new JSpinner();
		labelpercentSignConvertCzi2Tif = new JLabel();
		checkBoxRotateConvertCzi2Tif = new JCheckBox();
		comboBoxRoateDirectionConvertCzi2Tif = new JComboBox();
		buttonOkConvertCzi2Tif = new JButton();
		textFieldStatusConvertCzi2Tif = new JTextField();
		progressBarConvertCzi2Tif = new JProgressBar();
		scrollPaneTableFileCziToTiff = new JScrollPane();
		tableFileCziToTiff = new JTable();
		tabbedPaneVesselSegmentation = new JTabbedPane();
		panelImageListVesselSegmentation = new JPanel();
		buttonAddFile = new JButton();
		buttonAddFolder = new JButton();
		buttonRemove = new JButton();
		buttonClear = new JButton();
		labelOutputPath = new JLabel();
		buttonBrowseOutputPath = new JButton();
		textFieldOutputPath = new JTextField();
		scrollPaneVesselSegmentation = new JScrollPane();
		tableAddedFileVesselSegmentation = new JTable();
		panelParametersVesselSegmentation = new JPanel();
		labelCurrentFileVesselSegmentation = new JLabel();
		textFieldCurrentFileSegmentation = new JTextField();
		labelTargetXYPixelSize = new JLabel();
		spinnerXYPixelSizeCreateSideView = new JSpinner();
		labelTargetZPixelSize = new JLabel();
		spinnerZPixelSizeCreateSideView = new JSpinner();
		labelAnalysisWindow = new JLabel();
		spinnerAnalysisWindow = new JSpinner();
		labelPreWatershedSmoothing = new JLabel();
		spinnerPreWatershedSmoothing = new JSpinner();
		labelInnerVesselRadius = new JLabel();
		spinnerInnerVesselRadius = new JSpinner();
		labelHybridWeight = new JLabel();
		labelLigninHybridWeight = new JLabel();
		sliderHybridWeight = new JSlider();
		labelCelluloseHybridWeight = new JLabel();
		buttonCreateSideView = new JButton();
		buttonProjAndSmooth = new JButton();
		buttonSelectCentroid = new JButton();
		buttonWatershed = new JButton();
		buttonProcessWholeStack = new JButton();
		buttonMoveToRadialProjection = new JButton();
		separator3 = new JSeparator();
		textField2StatusVesselSegmentation = new JTextField();
		progressBarVesselSegmentation = new JProgressBar();
		panel3RadialProjection = new JPanel();
		labelFileNameRadialProjection = new JLabel();
		textFieldRadialProjection = new JTextField();
		buttonRunRadialProjection = new JButton();
		buttonMoveToAnalysis = new JButton();
		textFieldStatusRadialProjection = new JTextField();
		progressBarRadialProjection = new JProgressBar();
		tabbedPaneAnalysis = new JTabbedPane();
		panelImageForAnalysis = new JPanel();
		labelCurrentFileAnalysis = new JLabel();
		textFieldCurrentFileAnalysis = new JTextField();
		scrollPaneAnalysisInputImage = new JScrollPane();
		tableAnalysisInputImage = new JTable();
		bandsAndGapsPanel = new JPanel();
		labelLegacyBandMeasurement = new JLabel();
		buttonLegacyBandMeasurement = new JButton();
		labelNumberOfLineScan = new JLabel();
		spinnerNumberOfLineScan = new JSpinner();
		labelLineScanLength = new JLabel();
		spinnerLineScanLength = new JSpinner();
		scrollPaneBandGapResult = new JScrollPane();
		textAreaBandGapResult = new JTextArea();
		panelOrientationAndAnisotropy = new JPanel();
		labelNumberRandomBoxesAnisotropy = new JLabel();
		spinnerNumberRandomBoxes = new JSpinner();
		labelRandomboxWidth = new JLabel();
		spinnerRandomBoxWidth = new JSpinner();
		buttonComputeAnisotropy = new JButton();
		scrollPaneAnisotropy = new JScrollPane();
		textAreaAnisotropyResult = new JTextArea();
		panelExportResult = new JPanel();
		buttonSelectOutputAnalysis = new JButton();
		textFieldOutputAnalysis = new JTextField();
		buttonExportToXLSX = new JButton();
		checkBoxCombineResultXLSX = new JCheckBox();
		buttonExportResultToCSV = new JButton();
		checkBoxCombineResultCSV = new JCheckBox();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new HorizontalLayout());

		//======== panelLeftMenu ========
		{
			panelLeftMenu.setLayout(new VerticalLayout(4));

			//======== panelGroupButtonTab ========
			{
				panelGroupButtonTab.setLayout(new MigLayout(
					"hidemode 3,align center top",
					// columns
					"[fill]",
					// rows
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]"));

				//---- buttonTabCzi2Tif ----
				buttonTabCzi2Tif.setText("CZI to TIFF");
				buttonTabCzi2Tif.setBorderPainted(false);
				panelGroupButtonTab.add(buttonTabCzi2Tif, "cell 0 0");
				panelGroupButtonTab.add(separator1, "cell 0 1");

				//---- label1 ----
				label1.setText("<html>Process File<br>Sequentially</html>");
				label1.setHorizontalAlignment(SwingConstants.CENTER);
				label1.setFont(new Font("sansserif", Font.BOLD, 24));
				panelGroupButtonTab.add(label1, "cell 0 2");

				//---- buttonTabVesselSegmentation ----
				buttonTabVesselSegmentation.setText("<html>1. Vessel<br>Segmentation</html>");
				buttonTabVesselSegmentation.setBorderPainted(false);
				panelGroupButtonTab.add(buttonTabVesselSegmentation, "cell 0 3");

				//---- buttonTabRadialProjection ----
				buttonTabRadialProjection.setText("2. Radial Projection");
				buttonTabRadialProjection.setBorderPainted(false);
				panelGroupButtonTab.add(buttonTabRadialProjection, "cell 0 5");

				//---- buttonTabAnalysis ----
				buttonTabAnalysis.setText("3. Analysis");
				buttonTabAnalysis.setBorderPainted(false);
				panelGroupButtonTab.add(buttonTabAnalysis, "cell 0 7");
				panelGroupButtonTab.add(separator2, "cell 0 8");

				//---- label2 ----
				label2.setText("Batch Processing");
				label2.setHorizontalAlignment(SwingConstants.CENTER);
				label2.setFont(new Font("sansserif", Font.BOLD, 24));
				panelGroupButtonTab.add(label2, "cell 0 9");

				//---- buttonTabBatchMode ----
				buttonTabBatchMode.setText("Batch Mode");
				buttonTabBatchMode.setBorderPainted(false);
				panelGroupButtonTab.add(buttonTabBatchMode, "cell 0 10");
			}
			panelLeftMenu.add(panelGroupButtonTab);
		}
		contentPane.add(panelLeftMenu);

		//======== panelMainRight ========
		{
			panelMainRight.setLayout(new CardLayout());

			//======== panelConvertCzi2Tif ========
			{
				panelConvertCzi2Tif.setLayout(new MigLayout(
					"hidemode 3,align center top",
					// columns
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]",
					// rows
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]"));

				//---- buttonBrowseConvertCzi2Tif ----
				buttonBrowseConvertCzi2Tif.setText("Browse");
				panelConvertCzi2Tif.add(buttonBrowseConvertCzi2Tif, "cell 0 0");

				//---- textFieldConvertCzi2Tif ----
				textFieldConvertCzi2Tif.setEditable(false);
				panelConvertCzi2Tif.add(textFieldConvertCzi2Tif, "cell 1 0 6 1");

				//---- checkBoxBgSubConvertCzi2Tif ----
				checkBoxBgSubConvertCzi2Tif.setText("background substraction:");
				panelConvertCzi2Tif.add(checkBoxBgSubConvertCzi2Tif, "cell 0 1");

				//---- labelRollingConvertCzi2Tif ----
				labelRollingConvertCzi2Tif.setText("Rolling");
				panelConvertCzi2Tif.add(labelRollingConvertCzi2Tif, "cell 1 1");

				//---- spinnerRollingConvertCzi2Tif ----
				spinnerRollingConvertCzi2Tif.setModel(new SpinnerNumberModel(10, 1, null, 1));
				panelConvertCzi2Tif.add(spinnerRollingConvertCzi2Tif, "cell 2 1");

				//---- labelEnhanceConstConvertCzi2Tif ----
				labelEnhanceConstConvertCzi2Tif.setText("Enhance Constrast:");
				panelConvertCzi2Tif.add(labelEnhanceConstConvertCzi2Tif, "cell 0 2");

				//---- labelSaturateConvertCzi2Tif ----
				labelSaturateConvertCzi2Tif.setText("Saturation");
				panelConvertCzi2Tif.add(labelSaturateConvertCzi2Tif, "cell 1 2");

				//---- spinnerSaturateConvertCzi2Tif ----
				spinnerSaturateConvertCzi2Tif.setModel(new SpinnerNumberModel(35, 0, 100, 1));
				panelConvertCzi2Tif.add(spinnerSaturateConvertCzi2Tif, "cell 2 2");

				//---- labelpercentSignConvertCzi2Tif ----
				labelpercentSignConvertCzi2Tif.setText("%");
				panelConvertCzi2Tif.add(labelpercentSignConvertCzi2Tif, "cell 3 2");

				//---- checkBoxRotateConvertCzi2Tif ----
				checkBoxRotateConvertCzi2Tif.setText("Rotate:");
				panelConvertCzi2Tif.add(checkBoxRotateConvertCzi2Tif, "cell 0 3");
				panelConvertCzi2Tif.add(comboBoxRoateDirectionConvertCzi2Tif, "cell 1 3 2 1");

				//---- buttonOkConvertCzi2Tif ----
				buttonOkConvertCzi2Tif.setText("OK");
				panelConvertCzi2Tif.add(buttonOkConvertCzi2Tif, "cell 0 4");

				//---- textFieldStatusConvertCzi2Tif ----
				textFieldStatusConvertCzi2Tif.setEditable(false);
				panelConvertCzi2Tif.add(textFieldStatusConvertCzi2Tif, "cell 0 5");
				panelConvertCzi2Tif.add(progressBarConvertCzi2Tif, "cell 1 5 3 1");

				//======== scrollPaneTableFileCziToTiff ========
				{
					scrollPaneTableFileCziToTiff.setViewportView(tableFileCziToTiff);
				}
				panelConvertCzi2Tif.add(scrollPaneTableFileCziToTiff, "cell 0 6 3 1");
			}
			panelMainRight.add(panelConvertCzi2Tif, "card1");

			//======== tabbedPaneVesselSegmentation ========
			{

				//======== panelImageListVesselSegmentation ========
				{
					panelImageListVesselSegmentation.setLayout(new MigLayout(
						"hidemode 3,align center top",
						// columns
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]",
						// rows
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]"));

					//---- buttonAddFile ----
					buttonAddFile.setText("ADD");
					panelImageListVesselSegmentation.add(buttonAddFile, "cell 0 0");

					//---- buttonAddFolder ----
					buttonAddFolder.setText("ADD FOLDER");
					panelImageListVesselSegmentation.add(buttonAddFolder, "cell 1 0");

					//---- buttonRemove ----
					buttonRemove.setText("REMOVE");
					panelImageListVesselSegmentation.add(buttonRemove, "cell 2 0");

					//---- buttonClear ----
					buttonClear.setText("CLEAR");
					panelImageListVesselSegmentation.add(buttonClear, "cell 3 0");

					//---- labelOutputPath ----
					labelOutputPath.setText("Output Path");
					panelImageListVesselSegmentation.add(labelOutputPath, "cell 0 1");

					//---- buttonBrowseOutputPath ----
					buttonBrowseOutputPath.setText("Browse");
					panelImageListVesselSegmentation.add(buttonBrowseOutputPath, "cell 1 1");

					//---- textFieldOutputPath ----
					textFieldOutputPath.setEditable(false);
					panelImageListVesselSegmentation.add(textFieldOutputPath, "cell 2 1 2 1");

					//======== scrollPaneVesselSegmentation ========
					{
						scrollPaneVesselSegmentation.setViewportView(tableAddedFileVesselSegmentation);
					}
					panelImageListVesselSegmentation.add(scrollPaneVesselSegmentation, "cell 0 3 4 1");
				}
				tabbedPaneVesselSegmentation.addTab("Images List", panelImageListVesselSegmentation);

				//======== panelParametersVesselSegmentation ========
				{
					panelParametersVesselSegmentation.setLayout(new MigLayout(
						"hidemode 3,align center top",
						// columns
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]",
						// rows
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]"));

					//---- labelCurrentFileVesselSegmentation ----
					labelCurrentFileVesselSegmentation.setText("Current file:");
					panelParametersVesselSegmentation.add(labelCurrentFileVesselSegmentation, "cell 0 0");

					//---- textFieldCurrentFileSegmentation ----
					textFieldCurrentFileSegmentation.setEditable(false);
					panelParametersVesselSegmentation.add(textFieldCurrentFileSegmentation, "cell 1 0 3 1");

					//---- labelTargetXYPixelSize ----
					labelTargetXYPixelSize.setText("<html>target_xy pixel size(nm)</html>");
					panelParametersVesselSegmentation.add(labelTargetXYPixelSize, "cell 0 1");
					panelParametersVesselSegmentation.add(spinnerXYPixelSizeCreateSideView, "cell 1 1");

					//---- labelTargetZPixelSize ----
					labelTargetZPixelSize.setText("<html>target_z pixel size(nm)</html>");
					panelParametersVesselSegmentation.add(labelTargetZPixelSize, "cell 0 2");
					panelParametersVesselSegmentation.add(spinnerZPixelSizeCreateSideView, "cell 1 2");

					//---- labelAnalysisWindow ----
					labelAnalysisWindow.setText("<html>Analysis Window (\u03bcm)</html>");
					panelParametersVesselSegmentation.add(labelAnalysisWindow, "cell 0 3");
					panelParametersVesselSegmentation.add(spinnerAnalysisWindow, "cell 1 3");

					//---- labelPreWatershedSmoothing ----
					labelPreWatershedSmoothing.setText("<html>Pre-watershed <br>Smoothing</html>");
					panelParametersVesselSegmentation.add(labelPreWatershedSmoothing, "cell 0 4");
					panelParametersVesselSegmentation.add(spinnerPreWatershedSmoothing, "cell 1 4");

					//---- labelInnerVesselRadius ----
					labelInnerVesselRadius.setText("<html>Inner Vessel Radius (\u03bcm)</html>");
					panelParametersVesselSegmentation.add(labelInnerVesselRadius, "cell 0 5");
					panelParametersVesselSegmentation.add(spinnerInnerVesselRadius, "cell 1 5");

					//---- labelHybridWeight ----
					labelHybridWeight.setText("<html>Hybrid-weighting of <br> lignin-to-cellulose(%)</html>");
					panelParametersVesselSegmentation.add(labelHybridWeight, "cell 0 6");

					//---- labelLigninHybridWeight ----
					labelLigninHybridWeight.setText("Lignin 100%");
					panelParametersVesselSegmentation.add(labelLigninHybridWeight, "cell 1 6");

					//---- sliderHybridWeight ----
					sliderHybridWeight.setMajorTickSpacing(25);
					sliderHybridWeight.setPaintTicks(true);
					panelParametersVesselSegmentation.add(sliderHybridWeight, "cell 2 6");

					//---- labelCelluloseHybridWeight ----
					labelCelluloseHybridWeight.setText("Cellulose 0%");
					panelParametersVesselSegmentation.add(labelCelluloseHybridWeight, "cell 3 6");

					//---- buttonCreateSideView ----
					buttonCreateSideView.setText("Create Side View");
					panelParametersVesselSegmentation.add(buttonCreateSideView, "cell 0 7");

					//---- buttonProjAndSmooth ----
					buttonProjAndSmooth.setText("<html>Projection and <br> smoothing</html>");
					panelParametersVesselSegmentation.add(buttonProjAndSmooth, "cell 1 7");

					//---- buttonSelectCentroid ----
					buttonSelectCentroid.setText("Slect Centroid");
					panelParametersVesselSegmentation.add(buttonSelectCentroid, "cell 0 8");

					//---- buttonWatershed ----
					buttonWatershed.setText("Watershed");
					panelParametersVesselSegmentation.add(buttonWatershed, "cell 1 8");

					//---- buttonProcessWholeStack ----
					buttonProcessWholeStack.setText("Process Whole Stack");
					panelParametersVesselSegmentation.add(buttonProcessWholeStack, "cell 0 9");

					//---- buttonMoveToRadialProjection ----
					buttonMoveToRadialProjection.setText("<html>Move to <br> Radial Projection</html>");
					panelParametersVesselSegmentation.add(buttonMoveToRadialProjection, "cell 1 9");
					panelParametersVesselSegmentation.add(separator3, "cell 0 10 2 1");

					//---- textField2StatusVesselSegmentation ----
					textField2StatusVesselSegmentation.setEditable(false);
					panelParametersVesselSegmentation.add(textField2StatusVesselSegmentation, "cell 0 11 2 1");
					panelParametersVesselSegmentation.add(progressBarVesselSegmentation, "cell 2 11 2 1");
				}
				tabbedPaneVesselSegmentation.addTab("Parameters", panelParametersVesselSegmentation);
			}
			panelMainRight.add(tabbedPaneVesselSegmentation, "card2");

			//======== panel3RadialProjection ========
			{
				panel3RadialProjection.setLayout(new MigLayout(
					"hidemode 3,align left top",
					// columns
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]" +
					"[fill]",
					// rows
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]" +
					"[]"));

				//---- labelFileNameRadialProjection ----
				labelFileNameRadialProjection.setText("Current File: ");
				panel3RadialProjection.add(labelFileNameRadialProjection, "cell 0 0");

				//---- textFieldRadialProjection ----
				textFieldRadialProjection.setEditable(false);
				panel3RadialProjection.add(textFieldRadialProjection, "cell 1 0 5 1");

				//---- buttonRunRadialProjection ----
				buttonRunRadialProjection.setText("Radial Projection & Unrolling");
				panel3RadialProjection.add(buttonRunRadialProjection, "cell 0 1 2 1");

				//---- buttonMoveToAnalysis ----
				buttonMoveToAnalysis.setText("Move to Analysis");
				panel3RadialProjection.add(buttonMoveToAnalysis, "cell 0 2 2 1");
				panel3RadialProjection.add(textFieldStatusRadialProjection, "cell 0 4 2 1");
				panel3RadialProjection.add(progressBarRadialProjection, "cell 2 4 4 1");
			}
			panelMainRight.add(panel3RadialProjection, "card3");

			//======== tabbedPaneAnalysis ========
			{

				//======== panelImageForAnalysis ========
				{
					panelImageForAnalysis.setLayout(new MigLayout(
						"hidemode 3,align left top",
						// columns
						"[fill]" +
						"[fill]" +
						"[fill]",
						// rows
						"[]" +
						"[]" +
						"[]"));

					//---- labelCurrentFileAnalysis ----
					labelCurrentFileAnalysis.setText("Current File: ");
					panelImageForAnalysis.add(labelCurrentFileAnalysis, "cell 0 0");
					panelImageForAnalysis.add(textFieldCurrentFileAnalysis, "cell 1 0 2 1");

					//======== scrollPaneAnalysisInputImage ========
					{
						scrollPaneAnalysisInputImage.setViewportView(tableAnalysisInputImage);
					}
					panelImageForAnalysis.add(scrollPaneAnalysisInputImage, "cell 0 1 3 2");
				}
				tabbedPaneAnalysis.addTab("Image List", panelImageForAnalysis);

				//======== bandsAndGapsPanel ========
				{
					bandsAndGapsPanel.setLayout(new MigLayout(
						"hidemode 3,align left top",
						// columns
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]",
						// rows
						"[]" +
						"[]" +
						"[]"));

					//---- labelLegacyBandMeasurement ----
					labelLegacyBandMeasurement.setText("<html>Bands and Gaps <br> measurement</html>");
					bandsAndGapsPanel.add(labelLegacyBandMeasurement, "cell 0 0 2 1");

					//---- buttonLegacyBandMeasurement ----
					buttonLegacyBandMeasurement.setText("Measure");
					bandsAndGapsPanel.add(buttonLegacyBandMeasurement, "cell 2 0 2 1");

					//---- labelNumberOfLineScan ----
					labelNumberOfLineScan.setText("<html>Number of <br> line scan  </html>");
					bandsAndGapsPanel.add(labelNumberOfLineScan, "cell 4 0");
					bandsAndGapsPanel.add(spinnerNumberOfLineScan, "cell 5 0");

					//---- labelLineScanLength ----
					labelLineScanLength.setText("<html>Line Scan Length (\u00b5m)</html>");
					bandsAndGapsPanel.add(labelLineScanLength, "cell 6 0");
					bandsAndGapsPanel.add(spinnerLineScanLength, "cell 7 0");

					//======== scrollPaneBandGapResult ========
					{
						scrollPaneBandGapResult.setViewportView(textAreaBandGapResult);
					}
					bandsAndGapsPanel.add(scrollPaneBandGapResult, "cell 2 1 6 2");
				}
				tabbedPaneAnalysis.addTab("Bands & Gaps", bandsAndGapsPanel);

				//======== panelOrientationAndAnisotropy ========
				{
					panelOrientationAndAnisotropy.setLayout(new MigLayout(
						"hidemode 3,align left top",
						// columns
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[fill]",
						// rows
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]"));

					//---- labelNumberRandomBoxesAnisotropy ----
					labelNumberRandomBoxesAnisotropy.setText("<html>Number of <br>Random boxes</html>");
					panelOrientationAndAnisotropy.add(labelNumberRandomBoxesAnisotropy, "cell 1 0");
					panelOrientationAndAnisotropy.add(spinnerNumberRandomBoxes, "cell 2 0");

					//---- labelRandomboxWidth ----
					labelRandomboxWidth.setText("<html>Random box<br>width</html>");
					panelOrientationAndAnisotropy.add(labelRandomboxWidth, "cell 3 0");
					panelOrientationAndAnisotropy.add(spinnerRandomBoxWidth, "cell 4 0");

					//---- buttonComputeAnisotropy ----
					buttonComputeAnisotropy.setText("Compute Anisotropy");
					panelOrientationAndAnisotropy.add(buttonComputeAnisotropy, "cell 1 1");

					//======== scrollPaneAnisotropy ========
					{
						scrollPaneAnisotropy.setViewportView(textAreaAnisotropyResult);
					}
					panelOrientationAndAnisotropy.add(scrollPaneAnisotropy, "cell 1 2 4 3");
				}
				tabbedPaneAnalysis.addTab("Orientation & Anisotropy", panelOrientationAndAnisotropy);

				//======== panelExportResult ========
				{
					panelExportResult.setLayout(new MigLayout(
						"hidemode 3,align left top",
						// columns
						"[fill]" +
						"[fill]",
						// rows
						"[]" +
						"[]" +
						"[]" +
						"[]"));

					//---- buttonSelectOutputAnalysis ----
					buttonSelectOutputAnalysis.setText("Output");
					panelExportResult.add(buttonSelectOutputAnalysis, "cell 0 0");

					//---- textFieldOutputAnalysis ----
					textFieldOutputAnalysis.setEditable(false);
					panelExportResult.add(textFieldOutputAnalysis, "cell 1 0");

					//---- buttonExportToXLSX ----
					buttonExportToXLSX.setText("Export Result to XLSX");
					panelExportResult.add(buttonExportToXLSX, "cell 0 1");

					//---- checkBoxCombineResultXLSX ----
					checkBoxCombineResultXLSX.setText("Combine all images results");
					panelExportResult.add(checkBoxCombineResultXLSX, "cell 1 1");

					//---- buttonExportResultToCSV ----
					buttonExportResultToCSV.setText("Export Result To CSV");
					panelExportResult.add(buttonExportResultToCSV, "cell 0 2");

					//---- checkBoxCombineResultCSV ----
					checkBoxCombineResultCSV.setText("Combine all images results");
					panelExportResult.add(checkBoxCombineResultCSV, "cell 1 2");
				}
				tabbedPaneAnalysis.addTab("Export Result", panelExportResult);
			}
			panelMainRight.add(tabbedPaneAnalysis, "card4");
		}
		contentPane.add(panelMainRight);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Educational license - Anh Minh Do
	private JPanel panelLeftMenu;
	private JPanel panelGroupButtonTab;
	private JButton buttonTabCzi2Tif;
	private JSeparator separator1;
	private JLabel label1;
	private JButton buttonTabVesselSegmentation;
	private JButton buttonTabRadialProjection;
	private JButton buttonTabAnalysis;
	private JSeparator separator2;
	private JLabel label2;
	private JButton buttonTabBatchMode;
	private JPanel panelMainRight;
	private JPanel panelConvertCzi2Tif;
	private JButton buttonBrowseConvertCzi2Tif;
	private JTextField textFieldConvertCzi2Tif;
	private JCheckBox checkBoxBgSubConvertCzi2Tif;
	private JLabel labelRollingConvertCzi2Tif;
	private JSpinner spinnerRollingConvertCzi2Tif;
	private JLabel labelEnhanceConstConvertCzi2Tif;
	private JLabel labelSaturateConvertCzi2Tif;
	private JSpinner spinnerSaturateConvertCzi2Tif;
	private JLabel labelpercentSignConvertCzi2Tif;
	private JCheckBox checkBoxRotateConvertCzi2Tif;
	private JComboBox comboBoxRoateDirectionConvertCzi2Tif;
	private JButton buttonOkConvertCzi2Tif;
	private JTextField textFieldStatusConvertCzi2Tif;
	private JProgressBar progressBarConvertCzi2Tif;
	private JScrollPane scrollPaneTableFileCziToTiff;
	private JTable tableFileCziToTiff;
	private JTabbedPane tabbedPaneVesselSegmentation;
	private JPanel panelImageListVesselSegmentation;
	private JButton buttonAddFile;
	private JButton buttonAddFolder;
	private JButton buttonRemove;
	private JButton buttonClear;
	private JLabel labelOutputPath;
	private JButton buttonBrowseOutputPath;
	private JTextField textFieldOutputPath;
	private JScrollPane scrollPaneVesselSegmentation;
	private JTable tableAddedFileVesselSegmentation;
	private JPanel panelParametersVesselSegmentation;
	private JLabel labelCurrentFileVesselSegmentation;
	private JTextField textFieldCurrentFileSegmentation;
	private JLabel labelTargetXYPixelSize;
	private JSpinner spinnerXYPixelSizeCreateSideView;
	private JLabel labelTargetZPixelSize;
	private JSpinner spinnerZPixelSizeCreateSideView;
	private JLabel labelAnalysisWindow;
	private JSpinner spinnerAnalysisWindow;
	private JLabel labelPreWatershedSmoothing;
	private JSpinner spinnerPreWatershedSmoothing;
	private JLabel labelInnerVesselRadius;
	private JSpinner spinnerInnerVesselRadius;
	private JLabel labelHybridWeight;
	private JLabel labelLigninHybridWeight;
	private JSlider sliderHybridWeight;
	private JLabel labelCelluloseHybridWeight;
	private JButton buttonCreateSideView;
	private JButton buttonProjAndSmooth;
	private JButton buttonSelectCentroid;
	private JButton buttonWatershed;
	private JButton buttonProcessWholeStack;
	private JButton buttonMoveToRadialProjection;
	private JSeparator separator3;
	private JTextField textField2StatusVesselSegmentation;
	private JProgressBar progressBarVesselSegmentation;
	private JPanel panel3RadialProjection;
	private JLabel labelFileNameRadialProjection;
	private JTextField textFieldRadialProjection;
	private JButton buttonRunRadialProjection;
	private JButton buttonMoveToAnalysis;
	private JTextField textFieldStatusRadialProjection;
	private JProgressBar progressBarRadialProjection;
	private JTabbedPane tabbedPaneAnalysis;
	private JPanel panelImageForAnalysis;
	private JLabel labelCurrentFileAnalysis;
	private JTextField textFieldCurrentFileAnalysis;
	private JScrollPane scrollPaneAnalysisInputImage;
	private JTable tableAnalysisInputImage;
	private JPanel bandsAndGapsPanel;
	private JLabel labelLegacyBandMeasurement;
	private JButton buttonLegacyBandMeasurement;
	private JLabel labelNumberOfLineScan;
	private JSpinner spinnerNumberOfLineScan;
	private JLabel labelLineScanLength;
	private JSpinner spinnerLineScanLength;
	private JScrollPane scrollPaneBandGapResult;
	private JTextArea textAreaBandGapResult;
	private JPanel panelOrientationAndAnisotropy;
	private JLabel labelNumberRandomBoxesAnisotropy;
	private JSpinner spinnerNumberRandomBoxes;
	private JLabel labelRandomboxWidth;
	private JSpinner spinnerRandomBoxWidth;
	private JButton buttonComputeAnisotropy;
	private JScrollPane scrollPaneAnisotropy;
	private JTextArea textAreaAnisotropyResult;
	private JPanel panelExportResult;
	private JButton buttonSelectOutputAnalysis;
	private JTextField textFieldOutputAnalysis;
	private JButton buttonExportToXLSX;
	private JCheckBox checkBoxCombineResultXLSX;
	private JButton buttonExportResultToCSV;
	private JCheckBox checkBoxCombineResultCSV;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
