package schneiderlab.tools.radialprojection.views.userinterfacecomponents;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;
import org.jdesktop.swingx.*;

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
		this.getComboBoxRotateDirectionConvertCzi2Tif().setSelectedIndex(0);
	}
	public JFrame getParentFrame() {
		return parentFrame;
	}

	public JButton getButtonTabCzi2Tif(){ return buttonTabCzi2Tif;}
	public JButton getButtonTabVesselSegmentation(){ return buttonTabVesselSegmentation;}
	public JLabel getLabelIconArrow1(){return labelIconArrow1;}
	public JLabel getLabelIconArrow2(){return labelIconArrow2;}
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

	public JComboBox<String> getComboBoxRotateDirectionConvertCzi2Tif() {
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
	public JTextField getTextFieldCurrentFileAnalysis(){return textFieldCurrentFileAnalysis;}
	public JTextField getTextFieldOutputAnalysis(){return textFieldOutputAnalysis;}
	public JButton getButtonSelectOutputAnalysis(){return buttonSelectOutputAnalysis;}

	// batch mode components
	public JPanel getPanelBatch() {
		return panelBatch;
	}

	public JButton getButtonStartBatch() {
		return buttonStartBatch;
	}

	public JLabel getLabel1RightArrow() {
		return label1RightArrow;
	}

	public JButton getButtonCentroidSelectionBatch() {
		return buttonCentroidSelectionBatch;
	}

	public JLabel getLabel2RightArrow() {
		return label2RightArrow;
	}

	public JButton getButtonWaterShedBatch() {
		return buttonWaterShedBatch;
	}


	public JLabel getLabelStartQueueCounter() {
		return labelStartQueueCounter;
	}

	public JLabel getLabelCentroidSelectionCounter() {
		return labelCentroidSelectionCounter;
	}

	public JLabel getLabelWatershedCounter() {
		return labelWatershedCounter;
	}

	public JButton getButtonRadialProjectionBatch() {
		return buttonRadialProjectionBatch;
	}

	public JLabel getLabelRadialProjectionCounter() {
		return labelRadialProjectionCounter;
	}


	public JButton getButtonAnalysisBatch() {
		return buttonAnalysisBatch;
	}

	public JLabel getLabel2LeftArrow() {
		return label2LeftArrow;
	}

	public JButton getButtonRefineVesselBatch() {
		return buttonRefineVesselBatch;
	}


	public JLabel getLabelAnalysisCounter() {
		return labelAnalysisCounter;
	}

	public JLabel getLabelRefineVesselCounter() {
		return labelRefineVesselCounter;
	}

	public JLabel getLabelXYbatch() {
		return labelXYbatch;
	}

	public JSpinner getSpinnerXYBatch() {
		return spinnerXYBatch;
	}

	public JLabel getLabelZbatch() {
		return labelZbatch;
	}

	public JSpinner getSpinnerZbatch() {
		return spinnerZbatch;
	}

	public JLabel getLabelAnalysisWindowBatch() {
		return labelAnalysisWindowBatch;
	}

	public JSpinner getSpinnerAnalysisWindowBatch() {
		return spinnerAnalysisWindowBatch;
	}

	public JLabel getLabelSmoothingBatch() {
		return labelSmoothingBatch;
	}

	public JSpinner getSpinnerSmoothing() {
		return spinnerSmoothingBatch;
	}

	public JLabel getLabelInnerVesselRadiusBatch() {
		return labelInnerVesselRadiusBatch;
	}

	public JSpinner getSpinnerInnerVesselRadiusBatch() {
		return spinnerInnerVesselRadiusBatch;
	}

	public JLabel getLabelLigninPercentageBatch() {
		return labelLigninPercentageBatch;
	}

	public JSlider getSliderLigninCelluoseBatch() {
		return sliderLigninCelluoseBatch;
	}

	public JLabel getLabelCellulosePercentageBatch() {
		return labelCellulosePercentageBatch;
	}

	public JSpinner getSpinnerNumberLinescanBatch() {
		return spinnerNumberLinescanBatch;
	}

	public JSpinner getSpinnerLinescanLengthBatch() {
		return spinnerLinescanLengthBatch;
	}

	public JSpinner getSpinnerNumberRandomBoxesBatch() {
		return spinnerNumberRandomBoxesBatch;
	}

	public JSpinner getSpinnerRandomBoxWidthBatch() {
		return spinnerRandomBoxWidthBatch;
	}

	public JButton getButtonSelectionDirPathBatch() {
		return buttonSelectionDirPathBatch;
	}

	public JTextField getTextFieldDirPathBatch() {
		return textFieldDirPathBatch;
	}

	public JProgressBar getProgressBarStartButtonBatch() {
		return progressBarStartButtonBatch;
	}

	public JProgressBar getProgressBarCentroidSelectionBatch() {
		return progressBarCentroidSelectionBatch;
	}

	public JProgressBar getProgressBarWatershedBatch() {
		return progressBarWatershedBatch;
	}

	public JProgressBar getProgressBarRadialProjectionBatch() {
		return progressBarRadialProjectionBatch;
	}

	public JProgressBar getProgressBarAnalysisBatch() {
		return progressBarAnalysisBatch;
	}

	public JProgressBar getProgressBarRefineVesselBatch() {
		return progressBarRefineVesselBatch;
	}

	public JLabel getLabel1DownArrow() {
		return label1DownArrow;
	}

	public JLabel getLabel1LeftArrow() {
		return label1LeftArrow;
	}

	public JSpinner getSpinnerSmoothingBatch(){ return spinnerSmoothingBatch;}

	public JSpinner getSpinnerNumberRandomBoxes(){return spinnerNumberRandomBoxesBatch;}


	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Educational license - Anh Minh Do
		panelLeftMenu = new JPanel();
		panelGroupButtonTab = new JPanel();
		buttonTabCzi2Tif = new JButton();
		separator1 = new JSeparator();
		label1 = new JLabel();
		buttonTabVesselSegmentation = new JButton();
		labelIconArrow1 = new JLabel();
		buttonTabRadialProjection = new JButton();
		labelIconArrow2 = new JLabel();
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
		comboBoxRoateDirectionConvertCzi2Tif = new JComboBox<>(RotateDirection.values());
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
		panelBatch = new JPanel();
		buttonSelectionDirPathBatch = new JButton();
		textFieldDirPathBatch = new JTextField();
		buttonStartBatch = new JButton();
		label1RightArrow = new JLabel();
		buttonCentroidSelectionBatch = new JButton();
		label2RightArrow = new JLabel();
		buttonWaterShedBatch = new JButton();
		labelStartQueueCounter = new JLabel();
		labelCentroidSelectionCounter = new JLabel();
		labelWatershedCounter = new JLabel();
		progressBarStartButtonBatch = new JProgressBar();
		progressBarCentroidSelectionBatch = new JProgressBar();
		progressBarWatershedBatch = new JProgressBar();
		label1DownArrow = new JLabel();
		buttonAnalysisBatch = new JButton();
		label1LeftArrow = new JLabel();
		buttonRefineVesselBatch = new JButton();
		label2LeftArrow = new JLabel();
		buttonRadialProjectionBatch = new JButton();
		labelAnalysisCounter = new JLabel();
		labelRefineVesselCounter = new JLabel();
		labelRadialProjectionCounter = new JLabel();
		progressBarAnalysisBatch = new JProgressBar();
		progressBarRefineVesselBatch = new JProgressBar();
		progressBarRadialProjectionBatch = new JProgressBar();
		labelXYbatch = new JLabel();
		spinnerXYBatch = new JSpinner();
		labelZbatch = new JLabel();
		spinnerZbatch = new JSpinner();
		labelAnalysisWindowBatch = new JLabel();
		spinnerAnalysisWindowBatch = new JSpinner();
		labelSmoothingBatch = new JLabel();
		spinnerSmoothingBatch = new JSpinner();
		labelInnerVesselRadiusBatch = new JLabel();
		spinnerInnerVesselRadiusBatch = new JSpinner();
		label21 = new JLabel();
		labelLigninPercentageBatch = new JLabel();
		sliderLigninCelluoseBatch = new JSlider();
		labelCellulosePercentageBatch = new JLabel();
		label23 = new JLabel();
		spinnerNumberLinescanBatch = new JSpinner();
		label24 = new JLabel();
		spinnerLinescanLengthBatch = new JSpinner();
		label25 = new JLabel();
		spinnerNumberRandomBoxesBatch = new JSpinner();
		label26 = new JLabel();
		spinnerRandomBoxWidthBatch = new JSpinner();

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
				panelGroupButtonTab.add(buttonTabCzi2Tif, "cell 0 0");
				panelGroupButtonTab.add(separator1, "cell 0 1");

				//---- label1 ----
				label1.setText("<html>Process File<br>Sequentially</html>");
				label1.setHorizontalAlignment(SwingConstants.CENTER);
				label1.setFont(new Font("sansserif", Font.BOLD, 24));
				panelGroupButtonTab.add(label1, "cell 0 2");

				//---- buttonTabVesselSegmentation ----
				buttonTabVesselSegmentation.setText("<html>1. Vessel<br>   Segmentation</html>");
				buttonTabVesselSegmentation.setHorizontalAlignment(SwingConstants.LEFT);
				panelGroupButtonTab.add(buttonTabVesselSegmentation, "cell 0 3");

				//---- labelIconArrow1 ----
				labelIconArrow1.setIcon(null);
				labelIconArrow1.setHorizontalAlignment(SwingConstants.CENTER);
				panelGroupButtonTab.add(labelIconArrow1, "cell 0 4");

				//---- buttonTabRadialProjection ----
				buttonTabRadialProjection.setText("2. Radial Projection");
				buttonTabRadialProjection.setHorizontalAlignment(SwingConstants.LEFT);
				panelGroupButtonTab.add(buttonTabRadialProjection, "cell 0 5");

				//---- labelIconArrow2 ----
				labelIconArrow2.setIcon(null);
				labelIconArrow2.setHorizontalAlignment(SwingConstants.CENTER);
				panelGroupButtonTab.add(labelIconArrow2, "cell 0 6");

				//---- buttonTabAnalysis ----
				buttonTabAnalysis.setText("3. Analysis");
				buttonTabAnalysis.setHorizontalAlignment(SwingConstants.LEFT);
				panelGroupButtonTab.add(buttonTabAnalysis, "cell 0 7");
				panelGroupButtonTab.add(separator2, "cell 0 8");

				//---- label2 ----
				label2.setText("Batch Processing");
				label2.setHorizontalAlignment(SwingConstants.CENTER);
				label2.setFont(new Font("sansserif", Font.BOLD, 24));
				label2.setVisible(false);
				panelGroupButtonTab.add(label2, "cell 0 9");

				//---- buttonTabBatchMode ----
				buttonTabBatchMode.setText("Batch Mode");
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
				panelConvertCzi2Tif.add(scrollPaneTableFileCziToTiff, "cell 0 6 4 1");
			}
			panelMainRight.add(panelConvertCzi2Tif, "card1");

			//======== tabbedPaneVesselSegmentation ========
			{

				//======== panelImageListVesselSegmentation ========
				{
					panelImageListVesselSegmentation.setLayout(new MigLayout(
						"hidemode 3,align left top",
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
					textFieldOutputPath.setText("unselected");
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
					buttonProjAndSmooth.setEnabled(false);
					panelParametersVesselSegmentation.add(buttonProjAndSmooth, "cell 1 7");

					//---- buttonSelectCentroid ----
					buttonSelectCentroid.setText("Select Centroid");
					buttonSelectCentroid.setEnabled(false);
					panelParametersVesselSegmentation.add(buttonSelectCentroid, "cell 0 8");

					//---- buttonWatershed ----
					buttonWatershed.setText("Watershed");
					buttonWatershed.setEnabled(false);
					panelParametersVesselSegmentation.add(buttonWatershed, "cell 1 8");

					//---- buttonProcessWholeStack ----
					buttonProcessWholeStack.setText("Process Whole Stack");
					buttonProcessWholeStack.setEnabled(false);
					panelParametersVesselSegmentation.add(buttonProcessWholeStack, "cell 0 9");

					//---- buttonMoveToRadialProjection ----
					buttonMoveToRadialProjection.setText("<html>Move to <br> Radial Projection</html>");
					buttonMoveToRadialProjection.setEnabled(false);
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
				buttonRunRadialProjection.setEnabled(false);
				panel3RadialProjection.add(buttonRunRadialProjection, "cell 0 1 2 1");

				//---- buttonMoveToAnalysis ----
				buttonMoveToAnalysis.setText("Move to Analysis");
				buttonMoveToAnalysis.setEnabled(false);
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

					//---- textFieldCurrentFileAnalysis ----
					textFieldCurrentFileAnalysis.setEditable(false);
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
					buttonLegacyBandMeasurement.setEnabled(false);
					bandsAndGapsPanel.add(buttonLegacyBandMeasurement, "cell 2 0 2 1");

					//---- labelNumberOfLineScan ----
					labelNumberOfLineScan.setText("<html>Number of <br> line scan  </html>");
					bandsAndGapsPanel.add(labelNumberOfLineScan, "cell 4 0");

					//---- spinnerNumberOfLineScan ----
					spinnerNumberOfLineScan.setModel(new SpinnerNumberModel(100, 1, null, 1));
					bandsAndGapsPanel.add(spinnerNumberOfLineScan, "cell 5 0");

					//---- labelLineScanLength ----
					labelLineScanLength.setText("<html>Line Scan Length (\u00b5m)</html>");
					bandsAndGapsPanel.add(labelLineScanLength, "cell 6 0");

					//---- spinnerLineScanLength ----
					spinnerLineScanLength.setModel(new SpinnerNumberModel(25, 1, null, 1));
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
					panelOrientationAndAnisotropy.add(labelNumberRandomBoxesAnisotropy, "cell 0 0");

					//---- spinnerNumberRandomBoxes ----
					spinnerNumberRandomBoxes.setModel(new SpinnerNumberModel(100, 1, null, 1));
					panelOrientationAndAnisotropy.add(spinnerNumberRandomBoxes, "cell 1 0");

					//---- labelRandomboxWidth ----
					labelRandomboxWidth.setText("<html>Random box<br>width</html>");
					panelOrientationAndAnisotropy.add(labelRandomboxWidth, "cell 2 0");

					//---- spinnerRandomBoxWidth ----
					spinnerRandomBoxWidth.setModel(new SpinnerNumberModel(70, 2, null, 1));
					panelOrientationAndAnisotropy.add(spinnerRandomBoxWidth, "cell 3 0");

					//---- buttonComputeAnisotropy ----
					buttonComputeAnisotropy.setText("Compute Anisotropy");
					buttonComputeAnisotropy.setEnabled(false);
					panelOrientationAndAnisotropy.add(buttonComputeAnisotropy, "cell 0 1");

					//======== scrollPaneAnisotropy ========
					{
						scrollPaneAnisotropy.setViewportView(textAreaAnisotropyResult);
					}
					panelOrientationAndAnisotropy.add(scrollPaneAnisotropy, "cell 0 2 4 3");
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

			//======== panelBatch ========
			{
				panelBatch.setLayout(new MigLayout(
					"hidemode 3,align center top",
					// columns
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

				//---- buttonSelectionDirPathBatch ----
				buttonSelectionDirPathBatch.setText("Browse");
				panelBatch.add(buttonSelectionDirPathBatch, "cell 0 0");

				//---- textFieldDirPathBatch ----
				textFieldDirPathBatch.setEditable(false);
				panelBatch.add(textFieldDirPathBatch, "cell 1 0 4 1");

				//---- buttonStartBatch ----
				buttonStartBatch.setText("Start");
				buttonStartBatch.setEnabled(false);
				panelBatch.add(buttonStartBatch, "cell 0 1,alignx center,grow 0 100");
				panelBatch.add(label1RightArrow, "cell 1 1,alignx center,growx 0");

				//---- buttonCentroidSelectionBatch ----
				buttonCentroidSelectionBatch.setText("<html>Centroid  <br> Selection<html/>");
				buttonCentroidSelectionBatch.setEnabled(false);
				panelBatch.add(buttonCentroidSelectionBatch, "cell 2 1,alignx center,grow 0 100");
				panelBatch.add(label2RightArrow, "cell 3 1,alignx center,growx 0");

				//---- buttonWaterShedBatch ----
				buttonWaterShedBatch.setText("<html>Watershed &<br>Radial Projection<html/>");
				buttonWaterShedBatch.setEnabled(false);
				panelBatch.add(buttonWaterShedBatch, "cell 4 1,alignx center,grow 0 100");

				//---- labelStartQueueCounter ----
				labelStartQueueCounter.setText("0/0");
				labelStartQueueCounter.setHorizontalAlignment(SwingConstants.CENTER);
				panelBatch.add(labelStartQueueCounter, "cell 0 2");

				//---- labelCentroidSelectionCounter ----
				labelCentroidSelectionCounter.setText("0");
				labelCentroidSelectionCounter.setHorizontalAlignment(SwingConstants.CENTER);
				panelBatch.add(labelCentroidSelectionCounter, "cell 2 2");

				//---- labelWatershedCounter ----
				labelWatershedCounter.setText("0");
				labelWatershedCounter.setHorizontalAlignment(SwingConstants.CENTER);
				panelBatch.add(labelWatershedCounter, "cell 4 2");
				panelBatch.add(progressBarStartButtonBatch, "cell 0 3");
				panelBatch.add(progressBarCentroidSelectionBatch, "cell 2 3");
				panelBatch.add(progressBarWatershedBatch, "cell 4 3");
				panelBatch.add(label1DownArrow, "cell 4 4,alignx center,growx 0");

				//---- buttonAnalysisBatch ----
				buttonAnalysisBatch.setText("<html>Analysis<html/>");
				buttonAnalysisBatch.setEnabled(false);
				panelBatch.add(buttonAnalysisBatch, "cell 0 5,alignx center,grow 0 100");
				panelBatch.add(label1LeftArrow, "cell 1 5,alignx center,growx 0");

				//---- buttonRefineVesselBatch ----
				buttonRefineVesselBatch.setText("<html> Refine <br> Vessel<html/>");
				buttonRefineVesselBatch.setEnabled(false);
				panelBatch.add(buttonRefineVesselBatch, "cell 2 5,alignx center,grow 0 100");
				panelBatch.add(label2LeftArrow, "cell 3 5,alignx center,growx 0");

				//---- buttonRadialProjectionBatch ----
				buttonRadialProjectionBatch.setText("<html>Radial <br> Projection<html/>");
				buttonRadialProjectionBatch.setEnabled(false);
				buttonRadialProjectionBatch.setVisible(false);
				panelBatch.add(buttonRadialProjectionBatch, "cell 4 5,alignx center,grow 0 100");

				//---- labelAnalysisCounter ----
				labelAnalysisCounter.setText("0");
				labelAnalysisCounter.setHorizontalAlignment(SwingConstants.CENTER);
				panelBatch.add(labelAnalysisCounter, "cell 0 6");

				//---- labelRefineVesselCounter ----
				labelRefineVesselCounter.setText("0");
				labelRefineVesselCounter.setHorizontalAlignment(SwingConstants.CENTER);
				panelBatch.add(labelRefineVesselCounter, "cell 2 6");

				//---- labelRadialProjectionCounter ----
				labelRadialProjectionCounter.setText("0");
				labelRadialProjectionCounter.setHorizontalAlignment(SwingConstants.CENTER);
				labelRadialProjectionCounter.setVisible(false);
				panelBatch.add(labelRadialProjectionCounter, "cell 4 6");
				panelBatch.add(progressBarAnalysisBatch, "cell 0 7");
				panelBatch.add(progressBarRefineVesselBatch, "cell 2 7");

				//---- progressBarRadialProjectionBatch ----
				progressBarRadialProjectionBatch.setVisible(false);
				panelBatch.add(progressBarRadialProjectionBatch, "cell 4 7");

				//---- labelXYbatch ----
				labelXYbatch.setText("xy pixel size(nm)");
				panelBatch.add(labelXYbatch, "cell 0 9,alignx right,growx 0");

				//---- spinnerXYBatch ----
				spinnerXYBatch.setModel(new SpinnerNumberModel(200, 1, null, 1));
				panelBatch.add(spinnerXYBatch, "cell 1 9");

				//---- labelZbatch ----
				labelZbatch.setText("z pixel size(nm)");
				panelBatch.add(labelZbatch, "cell 2 9,alignx right,growx 0");

				//---- spinnerZbatch ----
				spinnerZbatch.setModel(new SpinnerNumberModel(200, 1, null, 1));
				panelBatch.add(spinnerZbatch, "cell 3 9");

				//---- labelAnalysisWindowBatch ----
				labelAnalysisWindowBatch.setText("<html>Analysis<br>window(\u00b5m)</html>");
				panelBatch.add(labelAnalysisWindowBatch, "cell 0 10,alignx right,growx 0");

				//---- spinnerAnalysisWindowBatch ----
				spinnerAnalysisWindowBatch.setModel(new SpinnerNumberModel(15, 1, null, 1));
				panelBatch.add(spinnerAnalysisWindowBatch, "cell 1 10");

				//---- labelSmoothingBatch ----
				labelSmoothingBatch.setText("<html>Pre-watershed<br>Smoothing</html>");
				panelBatch.add(labelSmoothingBatch, "cell 2 10,alignx right,growx 0");

				//---- spinnerSmoothingBatch ----
				spinnerSmoothingBatch.setModel(new SpinnerNumberModel(2.0, 0.0, null, 0.1));
				panelBatch.add(spinnerSmoothingBatch, "cell 3 10");

				//---- labelInnerVesselRadiusBatch ----
				labelInnerVesselRadiusBatch.setText("<html>Inner Vessel<br>Radius (\u00b5m)</html>");
				panelBatch.add(labelInnerVesselRadiusBatch, "cell 4 10,alignx right,growx 0");

				//---- spinnerInnerVesselRadiusBatch ----
				spinnerInnerVesselRadiusBatch.setModel(new SpinnerNumberModel(1.0, 0.1, null, 0.05));
				panelBatch.add(spinnerInnerVesselRadiusBatch, "cell 5 10");

				//---- label21 ----
				label21.setText("<html>Hybrid-weighting of<br>lignin-to-cellulose(%)</html>");
				panelBatch.add(label21, "cell 0 11,alignx right,growx 0");

				//---- labelLigninPercentageBatch ----
				labelLigninPercentageBatch.setText("Lignin 75%");
				panelBatch.add(labelLigninPercentageBatch, "cell 1 11,alignx right,growx 0");

				//---- sliderLigninCelluoseBatch ----
				sliderLigninCelluoseBatch.setPaintTicks(true);
				sliderLigninCelluoseBatch.setMajorTickSpacing(25);
				sliderLigninCelluoseBatch.setValue(25);
				panelBatch.add(sliderLigninCelluoseBatch, "cell 2 11 3 1");

				//---- labelCellulosePercentageBatch ----
				labelCellulosePercentageBatch.setText("Cellulose 25%");
				panelBatch.add(labelCellulosePercentageBatch, "cell 5 11,alignx left,growx 0");

				//---- label23 ----
				label23.setText("Number of line scan");
				panelBatch.add(label23, "cell 0 12,alignx right,growx 0");

				//---- spinnerNumberLinescanBatch ----
				spinnerNumberLinescanBatch.setModel(new SpinnerNumberModel(100, 1, null, 1));
				panelBatch.add(spinnerNumberLinescanBatch, "cell 1 12");

				//---- label24 ----
				label24.setText("Linescan length(\u00b5m)");
				panelBatch.add(label24, "cell 2 12,alignx right,growx 0");

				//---- spinnerLinescanLengthBatch ----
				spinnerLinescanLengthBatch.setModel(new SpinnerNumberModel(25, 0, null, 1));
				panelBatch.add(spinnerLinescanLengthBatch, "cell 3 12");

				//---- label25 ----
				label25.setText("<html>Number of<br>random boxes</html>");
				panelBatch.add(label25, "cell 0 13,alignx right,growx 0");

				//---- spinnerNumberRandomBoxesBatch ----
				spinnerNumberRandomBoxesBatch.setModel(new SpinnerNumberModel(100, 1, null, 1));
				panelBatch.add(spinnerNumberRandomBoxesBatch, "cell 1 13");

				//---- label26 ----
				label26.setText("Random box width");
				panelBatch.add(label26, "cell 2 13,alignx right,growx 0");

				//---- spinnerRandomBoxWidthBatch ----
				spinnerRandomBoxWidthBatch.setModel(new SpinnerNumberModel(70, 1, null, 1));
				panelBatch.add(spinnerRandomBoxWidthBatch, "cell 3 13");
			}
			panelMainRight.add(panelBatch, "card5");
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
	private JLabel labelIconArrow1;
	private JButton buttonTabRadialProjection;
	private JLabel labelIconArrow2;
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
	private JPanel panelBatch;
	private JButton buttonSelectionDirPathBatch;
	private JTextField textFieldDirPathBatch;
	private JButton buttonStartBatch;
	private JLabel label1RightArrow;
	private JButton buttonCentroidSelectionBatch;
	private JLabel label2RightArrow;
	private JButton buttonWaterShedBatch;
	private JLabel labelStartQueueCounter;
	private JLabel labelCentroidSelectionCounter;
	private JLabel labelWatershedCounter;
	private JProgressBar progressBarStartButtonBatch;
	private JProgressBar progressBarCentroidSelectionBatch;
	private JProgressBar progressBarWatershedBatch;
	private JLabel label1DownArrow;
	private JButton buttonAnalysisBatch;
	private JLabel label1LeftArrow;
	private JButton buttonRefineVesselBatch;
	private JLabel label2LeftArrow;
	private JButton buttonRadialProjectionBatch;
	private JLabel labelAnalysisCounter;
	private JLabel labelRefineVesselCounter;
	private JLabel labelRadialProjectionCounter;
	private JProgressBar progressBarAnalysisBatch;
	private JProgressBar progressBarRefineVesselBatch;
	private JProgressBar progressBarRadialProjectionBatch;
	private JLabel labelXYbatch;
	private JSpinner spinnerXYBatch;
	private JLabel labelZbatch;
	private JSpinner spinnerZbatch;
	private JLabel labelAnalysisWindowBatch;
	private JSpinner spinnerAnalysisWindowBatch;
	private JLabel labelSmoothingBatch;
	private JSpinner spinnerSmoothingBatch;
	private JLabel labelInnerVesselRadiusBatch;
	private JSpinner spinnerInnerVesselRadiusBatch;
	private JLabel label21;
	private JLabel labelLigninPercentageBatch;
	private JSlider sliderLigninCelluoseBatch;
	private JLabel labelCellulosePercentageBatch;
	private JLabel label23;
	private JSpinner spinnerNumberLinescanBatch;
	private JLabel label24;
	private JSpinner spinnerLinescanLengthBatch;
	private JLabel label25;
	private JSpinner spinnerNumberRandomBoxesBatch;
	private JLabel label26;
	private JSpinner spinnerRandomBoxWidthBatch;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
