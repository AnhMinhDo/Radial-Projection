package schneiderlab.tools.radialprojection.controllers.controllers;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.*;
import ij.plugin.filter.BackgroundSubtracter;
import ij.process.AutoThresholder;
import ij.process.ByteProcessor;
import ij.process.ShortProcessor;
import net.imagej.DatasetService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;
import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.ui.UIService;
import schneiderlab.tools.radialprojection.CurrentOSSystem;
import schneiderlab.tools.radialprojection.controllers.uiaction.*;
import schneiderlab.tools.radialprojection.controllers.uiaction.czitotif.BrowseButtonCZIToTif;
import schneiderlab.tools.radialprojection.controllers.uiaction.mainwindow.AddSavingActionWhenMainWindowClosed;
import schneiderlab.tools.radialprojection.controllers.workers.*;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.bandgapmeasurement.Tile;
import schneiderlab.tools.radialprojection.imageprocessor.core.bandgapmeasurement.Utils;
import schneiderlab.tools.radialprojection.imageprocessor.core.convertczitotif.RotateDirection;
import schneiderlab.tools.radialprojection.imageprocessor.core.io.SaveVesselResultToCSV;
import schneiderlab.tools.radialprojection.imageprocessor.core.io.SaveVesselResultToXLSX;
import schneiderlab.tools.radialprojection.imageprocessor.core.segmentation.Reconstruction;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;
import schneiderlab.tools.radialprojection.models.czitotifmodel.CziToTifModel;
import schneiderlab.tools.radialprojection.models.radialprojection.AnalysisModel;
import schneiderlab.tools.radialprojection.models.radialprojection.RadialProjectionModel;
import schneiderlab.tools.radialprojection.models.radialprojection.VesselsSegmentationModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.ImageWindowGroupController;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.Radical_Projection_Tool;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {
    private final Radical_Projection_Tool mainView;
    private List<Path> processedFileInCreateSideView;
    private final Context context;
    private ImagePlus finalSegmentation;
    private LogService logService;
    private DatasetService datasetService;
    private UIService uiService;
    private CurrentOSSystem currentOSSystem;

    public MainController(Radical_Projection_Tool mainView,
                          Context context,
                          CurrentOSSystem currentOSSystem) {
        this.mainView = mainView;
        this.context= context;
        logService = context.getService(LogService.class);
        datasetService = context.getService(DatasetService.class);
        uiService = context.getService(UIService.class);
        this.currentOSSystem=currentOSSystem;

        //----------Create the Model each step in pipeline--------------------
        // create an instance of the czi to TIF model
        CziToTifModel cziToTifModel = new CziToTifModel();
        // create an instance of the Vessel segmentation model
        VesselsSegmentationModel vesselsSegmentationModel = new VesselsSegmentationModel();
        // initial the model for radial projection step
        RadialProjectionModel radialProjectionModel = new RadialProjectionModel();
        // initial the model for Analysis step
        AnalysisModel analysisModel = new AnalysisModel();
        mainView.getTableAnalysisInputImage().setModel(new DefaultTableModel(new String[]{"Image Path"}, 0));

        //-----------0.CZI to TIF converting Steps-------------------------------

        // get initial values from properties file
        cziToTifModel.initValues("/properties_files/initValues.properties");
        // Action for Browse button in Converting step
        mainView.getButtonBrowseConvertCzi2Tif().addActionListener(new BrowseButtonCZIToTif(
                mainView.getTextFieldConvertCzi2Tif(),
                mainView.getParentFrame(),
                mainView.getTableFileCziToTiff()
        ));
        // starting dir for text field
        mainView.getTextFieldConvertCzi2Tif().getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        cziToTifModel.setDirPath(mainView.getTextFieldConvertCzi2Tif().getText());
                    }
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                    }
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }
                }
        );
        // Action for checkbox background Subtraction in Converting step
        mainView.getCheckBoxBgSubConvertCzi2Tif().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mainView.getSpinnerRollingConvertCzi2Tif().setEnabled(mainView.getCheckBoxBgSubConvertCzi2Tif().isSelected());
                cziToTifModel.setIsbgSub(mainView.getCheckBoxBgSubConvertCzi2Tif().isSelected());
            }
        });
        //add action for the rolling value spinner
        mainView.getSpinnerRollingConvertCzi2Tif().addChangeListener(
                new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        int value = (int) mainView.getSpinnerRollingConvertCzi2Tif().getValue();
                        cziToTifModel.setRollingValue(value);
                    }
                }
        );
        // add action to the saturate value spinner
        mainView.getSpinnerSaturateConvertCzi2Tif().addChangeListener(
                new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        int value = (int) mainView.getSpinnerSaturateConvertCzi2Tif().getValue();
                        cziToTifModel.setSaturationValue(value);
                    }
                }
        );
        // Action for checkbox Rotate in Converting step
        mainView.getCheckBoxRotateConvertCzi2Tif().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mainView.getComboBoxRoateDirectionConvertCzi2Tif()
                        .setEnabled(mainView.getCheckBoxRotateConvertCzi2Tif().isSelected());
                cziToTifModel.setRotate(mainView.getCheckBoxRotateConvertCzi2Tif().isSelected());
            }
        });
        // Update the model when another item in the combobox is selected
        mainView.getComboBoxRoateDirectionConvertCzi2Tif().addItemListener(
                new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        RotateDirection rotateDirectionString = (RotateDirection) mainView.getComboBoxRoateDirectionConvertCzi2Tif()
                                .getSelectedItem();
                        cziToTifModel.setRotateDirection(rotateDirectionString);
                    }
                }
        );
        // add the values from the czitotif model to the view
        mainView.getTextFieldConvertCzi2Tif().setText(cziToTifModel.getDirPath());// only at application innitialization
        mainView.getCheckBoxBgSubConvertCzi2Tif().setSelected(cziToTifModel.isBgSub());// only at application innitialization
        mainView.getSpinnerRollingConvertCzi2Tif().setValue(cziToTifModel.getRollingValue());// only at application innitialization
        mainView.getSpinnerSaturateConvertCzi2Tif().setValue(cziToTifModel.getSaturationValue());// only at application innitialization
        mainView.getCheckBoxRotateConvertCzi2Tif().setSelected(cziToTifModel.isRotate());// only at application innitialization
        mainView.getComboBoxRoateDirectionConvertCzi2Tif().setSelectedItem(cziToTifModel.getRotateDirection());
        // prepare the model for the file table in czitotif step
        mainView.getTableFileCziToTiff().setModel(new DefaultTableModel(new String[]{"File Name"}, 0));
        // tooltip to view full file path in the table of cziToTif
        mainView.getTableFileCziToTiff().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = mainView.getTableFileCziToTiff().rowAtPoint(e.getPoint());
                int col = mainView.getTableFileCziToTiff().columnAtPoint(e.getPoint());
                if (row > -1 && col > -1) {
                    String value = (String) mainView.getTableFileCziToTiff().getValueAt(row, col);
                    mainView.getTableFileCziToTiff().setToolTipText(value);
                }
            }
        });
        // Action for OK button in Converting step
        mainView.getButtonOkConvertCzi2Tif().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String folderPath = cziToTifModel.getDirPath();
                int rolling = cziToTifModel.getRollingValue();
                int saturated = cziToTifModel.getSaturationValue();
                RotateDirection rotateDirection = cziToTifModel.getRotateDirection();
                boolean isRotate = cziToTifModel.isRotate();
                boolean isBackgroundSubtraction = cziToTifModel.isBgSub();
                mainView.getTextFieldStatusConvertCzi2Tif().setText("Converting...");
                mainView.getProgressBarConvertCzi2Tif().setValue(0);
                Czi2TifWorker czi2TifWorker = new Czi2TifWorker(folderPath,
                        isBackgroundSubtraction,
                        rolling,
                        saturated,
                        isRotate,
                        rotateDirection);
                czi2TifWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress".equals(evt.getPropertyName())){
                            mainView.getProgressBarConvertCzi2Tif().setValue((int)evt.getNewValue());
                        } else if ("state".equals(evt.getPropertyName()) &&
                                evt.getNewValue() == SwingWorker.StateValue.DONE) {
                            mainView.getProgressBarConvertCzi2Tif().setValue(100);
                            mainView.getTextFieldStatusConvertCzi2Tif().setText("Complete");
                        }
                    }
                });
                czi2TifWorker.execute();
            }
        });
        //----------- 1.Vessel segmentation -------------------------------
        // get initial values from properties file
        vesselsSegmentationModel.initValues("/properties_files/initValues.properties");
        mainView.getTableAddedFileVesselSegmentation().setModel(new DefaultTableModel(new String[]{"File Path"}, 0));
        // Action for ADD button in segmentation step
        mainView.getButtonAddFile().addActionListener(new AddFilePathToTableVesselSegmentation(
                mainView.getTableAddedFileVesselSegmentation(),mainView));

        // Action for REMOVE Button in segmentation step
        mainView.getButtonRemove().addActionListener(new RemoveFilePathFromTable(mainView.getTableAddedFileVesselSegmentation()));

        // tooltip to view full file path in segmentation step
        mainView.getTableAddedFileVesselSegmentation().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = mainView.getTableAddedFileVesselSegmentation().rowAtPoint(e.getPoint());
                int col = mainView.getTableAddedFileVesselSegmentation().columnAtPoint(e.getPoint());
                if (row > -1 && col > -1) {
                    Object value = mainView.getTableAddedFileVesselSegmentation().getValueAt(row, col);
                    mainView.getTableAddedFileVesselSegmentation().setToolTipText(value != null ? value.toString() : null);
                }
            }
        });
        // button add folder for segmentation step
        mainView.getButtonAddFolder().addActionListener(new AddFilePathFromDirToTableVesselSegmentation(mainView.getTableAddedFileVesselSegmentation(), mainView, mainView));
        // button clear all in table for segmentation step
        mainView.getButtonClear().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) mainView.getTableAddedFileVesselSegmentation().getModel();
                model.setRowCount(0);
            }
        });
        // button add output Path
        mainView.getButtonBrowseOutputPath().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(mainView);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedDir = chooser.getSelectedFile();
                    mainView.getTextFieldOutputPath().setText(selectedDir.getAbsolutePath());
                }
            }
        });

        // textField show current file
        mainView.getTextFieldCurrentFileSegmentation().getDocument().addDocumentListener(new DocumentListener() {
            void update() {
                mainView.getTextFieldCurrentFileSegmentation().setToolTipText(mainView.getTextFieldCurrentFileSegmentation().getText()); // or any custom logic
            }
            @Override
            public void insertUpdate(DocumentEvent e) {update();}
            @Override
            public void removeUpdate(DocumentEvent e) {update();}
            @Override
            public void changedUpdate(DocumentEvent e) {update();}
        });

        // spinner xy pixel size
        mainView.getSpinnerXYPixelSizeCreateSideView().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) mainView.getSpinnerXYPixelSizeCreateSideView().getValue();
                vesselsSegmentationModel.setXyPixelSize(value);
            }
        });
        // spinner xy pixel size
        mainView.getSpinnerXYPixelSizeCreateSideView().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) mainView.getSpinnerXYPixelSizeCreateSideView().getValue();
                vesselsSegmentationModel.setXyPixelSize(value);
            }
        });
        // spinner z pixel size
        mainView.getSpinnerZPixelSizeCreateSideView().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) mainView.getSpinnerZPixelSizeCreateSideView().getValue();
                vesselsSegmentationModel.setzPixelSize(value);
            }
        });
        // spinner analysis window
        mainView.getSpinnerAnalysisWindow().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) mainView.getSpinnerAnalysisWindow().getValue();
                vesselsSegmentationModel.setAnalysisWindow(value);
            }
        });
        // spinner pre watershed smoothing
        mainView.getSpinnerPreWatershedSmoothing().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = (double) mainView.getSpinnerPreWatershedSmoothing().getValue();
                vesselsSegmentationModel.setSmoothingSigma(value);
            }
        });
        // spinner slice index for tuning
        mainView.getSpinnerSliceIndexForTuning().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) mainView.getSpinnerSliceIndexForTuning().getValue();
                vesselsSegmentationModel.setSliceIndexForTuning(value);
            }
        });
        // spinner vessel radius
        mainView.getSpinnerInnerVesselRadius().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = (double) mainView.getSpinnerInnerVesselRadius().getValue();
                vesselsSegmentationModel.setInnerVesselRadius(value);
            }
        });
        // Create Side view button
        mainView.getButtonCreateSideView().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainView.getTextField2StatusVesselSegmentation().setText("Creating Side View...");
                mainView.getButtonProjAndSmooth().setEnabled(false);
                mainView.getButtonSelectCentroid().setEnabled(false);
                mainView.getButtonWatershed().setEnabled(false);
                mainView.getButtonProcessWholeStack().setEnabled(false);
                mainView.getButtonMoveToRadialProjection().setEnabled(false);
                int rowCount = mainView.getTableAddedFileVesselSegmentation().getModel().getRowCount();
                if(rowCount ==0){
                    mainView.getButtonAddFile().doClick();
                    return;
                }
                String fileToProcess = (String) mainView.getTableAddedFileVesselSegmentation()
                        .getModel()
                        .getValueAt(0, 0);
                vesselsSegmentationModel.setFilePath(Paths.get(fileToProcess));
                ImageData<UnsignedShortType, FloatType> imageData = new ImageData<>();
                vesselsSegmentationModel.setImageData(imageData);
                vesselsSegmentationModel.getImageData().setImagePath(vesselsSegmentationModel.getFilePath());
                vesselsSegmentationModel.getImageData().setOutputDirPath(Paths.get(mainView.getTextFieldOutputPath().getText()));
                logService.info("image path: " + vesselsSegmentationModel.getImageData().getImagePath().toAbsolutePath().toString());
                CreateSideViewWorker createSideViewWorker = new CreateSideViewWorker(
                        (int) mainView.getSpinnerXYPixelSizeCreateSideView().getValue(),
                        (int) mainView.getSpinnerZPixelSizeCreateSideView().getValue(),
                        Paths.get(fileToProcess),
                        context,
                        mainView
                );
                createSideViewWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress".equals(evt.getPropertyName())){
                                    mainView.getProgressBarVesselSegmentation().setValue((int)evt.getNewValue());
                        }
                    }
                });
                createSideViewWorker.addPropertyChangeListener(propChangeEvent -> {
                    if ("state".equals(propChangeEvent.getPropertyName()) &&
                            propChangeEvent.getNewValue() == SwingWorker.StateValue.DONE) {
                        vesselsSegmentationModel.setSideView(createSideViewWorker.getSideViewImgPlus());
                        vesselsSegmentationModel.getImageData().setSideView(createSideViewWorker.getSideViewImgPlus());
                        ImagePlus sideViewDisplay = ImageJFunctions.wrapUnsignedShort(vesselsSegmentationModel.getImageData().getSideView(), "Side View");
                        sideViewDisplay.getProcessor().resetMinAndMax();
                        vesselsSegmentationModel.setSideViewDisplay(sideViewDisplay);
                        mainView.getTextField2StatusVesselSegmentation().setText("Side View Created");
                        mainView.getButtonProjAndSmooth().setEnabled(true);
                        mainView.getTextFieldCurrentFileSegmentation().setText(vesselsSegmentationModel.getFilePath().getFileName().toString());
                        sideViewDisplay.show();
                    }
                });
                createSideViewWorker.execute();
            }
        });

        // Slider update the percentage when the value change
        mainView.getSliderHybridWeight().addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                int currentValue = mainView.getSliderHybridWeight().getValue();
                mainView.getLabelLigninHybridWeight().setText("Lignin " + (100-currentValue) + "%");
                mainView.getLabelCelluloseHybridWeight().setText("Cellulose " + currentValue + "%");
                vesselsSegmentationModel.setCelluloseToLigninRatio(currentValue);
            }
        });

        // button projection and smoothing in segmentation step
        mainView.getButtonProjAndSmooth().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    mainView.getTextField2StatusVesselSegmentation().setText("Creating hybrid stack and smoothing...");
                    mainView.getButtonSelectCentroid().setEnabled(false);
                    mainView.getButtonWatershed().setEnabled(false);
                    mainView.getButtonProcessWholeStack().setEnabled(false);
                    mainView.getButtonMoveToRadialProjection().setEnabled(false);
                ProjectionAndSmoothingWorker pasw = new ProjectionAndSmoothingWorker(vesselsSegmentationModel.getImageData().getSideView(),
                        mainView.getSliderHybridWeight().getValue(),
                        (int)mainView.getSpinnerAnalysisWindow().getValue(),
                        (double) mainView.getSpinnerPreWatershedSmoothing().getValue(),
                        (double) mainView.getSpinnerInnerVesselRadius().getValue(),
                        context);
                pasw.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if("progress".equals(evt.getPropertyName())){
                            int currentProgress = (int)evt.getNewValue();
                            mainView.getProgressBarVesselSegmentation().setValue(currentProgress);
                            mainView.getProgressBarVesselSegmentation().setToolTipText(String.valueOf(currentProgress));
                        }
                        if ("state".equals(evt.getPropertyName()) &&
                                evt.getNewValue() == SwingWorker.StateValue.DONE){
                                vesselsSegmentationModel.setHybridStackNonSmoothed(pasw.getHybridStackNonSmoothed());
                                vesselsSegmentationModel.setHybridStackSmoothed(pasw.getHybridStackSmoothed());
                                vesselsSegmentationModel.setHybridStackSmoothedWidth(pasw.getWidth());
                                vesselsSegmentationModel.setHybridStackSmoothedHeight(pasw.getHeight());
                                vesselsSegmentationModel.setCellulose(pasw.getCellulose());
                                vesselsSegmentationModel.setLignin(pasw.getLignin());
                                vesselsSegmentationModel.setHybridStackSmoothedSlicesNumber(pasw.getSlicesNumber());
                                // set Field for ImageData object
                                vesselsSegmentationModel.getImageData().setHybridStackNonSmoothed(pasw.getHybridStackNonSmoothed());
                                vesselsSegmentationModel.getImageData().setHybridStackSmoothed(pasw.getHybridStackSmoothed());
                                vesselsSegmentationModel.getImageData().setHybridStackSmoothedWidth(pasw.getWidth());
                                vesselsSegmentationModel.getImageData().setHybridStackSmoothedHeight(pasw.getHeight());
                                vesselsSegmentationModel.getImageData().setCellulose(pasw.getCellulose());
                                vesselsSegmentationModel.getImageData().setLignin(pasw.getLignin());
                                vesselsSegmentationModel.getImageData().setHybridStackSmoothedSlicesNumber(pasw.getSlicesNumber());
                                logService.info("Set output to ImageData object");
                                 ImagePlus hybridStackNonSmoothedDisplay = ImageJFunctions.show(vesselsSegmentationModel.getImageData().getHybridStackNonSmoothed(),"Raw Hybrid");
                                 vesselsSegmentationModel.setHybridStackNonSmoothedDisplay(hybridStackNonSmoothedDisplay);
                            ImagePlus hybridStackSmoothedDisplay = ImageJFunctions.show(vesselsSegmentationModel.getImageData().getHybridStackSmoothed(), "Smoothed Hybrid");
                            vesselsSegmentationModel.setHybridStackSmoothedDisplay(hybridStackSmoothedDisplay);
                                // update UI
                                mainView.getTextField2StatusVesselSegmentation().setText("Complete Projection and Smoothing");
                                mainView.getButtonSelectCentroid().setEnabled(true);
                        }
                    }
                });
                pasw.execute();
            }
        });
        //TODO: separate the below actionListener to its own class
        mainView.getButtonSelectCentroid().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainView.getButtonWatershed().setEnabled(false);
                mainView.getButtonProcessWholeStack().setEnabled(false);
                mainView.getButtonMoveToRadialProjection().setEnabled(false);
                RandomAccessibleInterval<FloatType>	smoothedStack = vesselsSegmentationModel.getImageData().getHybridStackSmoothed();
                int slideForTuning = (int)mainView.getSpinnerSliceIndexForTuning().getValue();
                RandomAccessibleInterval<FloatType> just1Slide = Views.hyperSlice(smoothedStack,2,slideForTuning);
                // Copy the view to a new Img<FloatType>
                // Create copy using cursors
                Img<FloatType> copy = ArrayImgs.floats(Intervals.dimensionsAsLongArray(just1Slide));
                net.imglib2.Cursor<FloatType> srcCursor = Views.flatIterable(just1Slide).cursor();
                net.imglib2.Cursor<FloatType> dstCursor = copy.cursor();
                while (srcCursor.hasNext()) {
                    dstCursor.next().set(srcCursor.next());
                }
                // Convert to ImagePlus
                ImagePlus impFloat = ImageJFunctions.wrap(copy, "smoothed Side View");
                impFloat.resetDisplayRange();
                vesselsSegmentationModel.setImpInByte(new ImagePlus("1st slice", impFloat.getProcessor().convertToByte(true)));
                impFloat.resetDisplayRange();
                vesselsSegmentationModel.getImpInByte().show();
                // Create a new PointRoi to collect points
                PointRoi pointRoi = new PointRoi();
                vesselsSegmentationModel.getImpInByte().setRoi(pointRoi);
                ImageCanvas canvas = vesselsSegmentationModel.getImpInByte().getCanvas();
                double magnificationLevel = 4.0;
                canvas.setMagnification(magnificationLevel);
                // Get screen dimensions
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                // Calculate window size for zoom
                int imgWidth = vesselsSegmentationModel.getImpInByte().getWidth() * (int) magnificationLevel;
                int imgHeight = vesselsSegmentationModel.getImpInByte().getHeight() * (int) magnificationLevel;
                // position the window at  bottom left
                int xlocation = 10;
                int ylocation = screenHeight-imgHeight-(screenHeight *4/100); // screenHeight*4/100 to create a little bit space
                ImageWindow window = vesselsSegmentationModel.getImpInByte().getWindow();
                window.setLocationAndSize( xlocation,ylocation ,imgWidth,imgHeight);
                // add eventListener to canvas
                canvas.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Add point to the PointRoi
                        int x = canvas.offScreenX(e.getX());
                        int y = canvas.offScreenY(e.getY());
                        Point pointLatest = new Point(x,y);
                        vesselsSegmentationModel.getCoordinates().add(pointLatest);
                        IJ.log(vesselsSegmentationModel.getCoordinates().toString());
                        mainView.getButtonWatershed().setEnabled(true);
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        IJ.setTool(Toolbar.POINT);
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        IJ.setTool(Toolbar.RECTANGLE);
                    }

                });
            }
        });
        // button Watershed to segment the image
        mainView.getButtonWatershed().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, Void> segmentationWorker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        int slideForTuning = (int)mainView.getSpinnerSliceIndexForTuning().getValue();
                        Reconstruction reconstruction = new Reconstruction(
                                vesselsSegmentationModel.getImageData().getHybridStackSmoothed(),
                                vesselsSegmentationModel.getImageData().getHybridStackSmoothedWidth(),
                                vesselsSegmentationModel.getImageData().getHybridStackSmoothedHeight(),
                                (double)mainView.getSpinnerInnerVesselRadius().getValue(),
                                vesselsSegmentationModel.getCoordinates(),
                                (int)mainView.getSpinnerSliceIndexForTuning().getValue(),
                                (int)mainView.getSpinnerXYPixelSizeCreateSideView().getValue());
                        vesselsSegmentationModel.setOverlaySegmentation(reconstruction.process1Slide());
                        return null;
                    }
                };
                segmentationWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if("state".equals(evt.getPropertyName()) &&
                                evt.getNewValue() == SwingWorker.StateValue.DONE){
                            vesselsSegmentationModel.getCoordinatesBatch().clear();
                            vesselsSegmentationModel.getCoordinatesBatch().addAll(vesselsSegmentationModel.getCoordinates());
                            vesselsSegmentationModel.getCoordinates().clear();
                            mainView.getButtonProcessWholeStack().setEnabled(true);
                            mainView.getButtonWatershed().setEnabled(false);
                            vesselsSegmentationModel.getImpInByte().setOverlay(vesselsSegmentationModel.getOverlaySegmentation());
                            vesselsSegmentationModel.getImpInByte().updateAndDraw();
                        }
                    }
                });
                segmentationWorker.execute();
            }
        });
        // button processing wholeStack
        mainView.getButtonProcessWholeStack().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vesselsSegmentationModel.getImpInByte().close();
                mainView.getTextField2StatusVesselSegmentation().setText("Processing whole stack...");
                mainView.getProgressBarVesselSegmentation().setValue(0);
                SegmentWholeStackWorker batchSegmentationWorker = new SegmentWholeStackWorker(
                        vesselsSegmentationModel.getImageData().getHybridStackSmoothed(),
                        vesselsSegmentationModel.getImageData().getHybridStackSmoothedWidth(),
                        vesselsSegmentationModel.getImageData().getHybridStackSmoothedHeight(),
                        (double)mainView.getSpinnerInnerVesselRadius().getValue(),
                        vesselsSegmentationModel.getCoordinatesBatch(),
                        (int)mainView.getSpinnerSliceIndexForTuning().getValue(),
                        (int)mainView.getSpinnerXYPixelSizeCreateSideView().getValue()
                );
                batchSegmentationWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                        if("progress".equals(propertyChangeEvent.getPropertyName())){
                            int currentProgressValue = (int) propertyChangeEvent.getNewValue();
                            mainView.getProgressBarVesselSegmentation().setValue(currentProgressValue);
                            mainView.getProgressBarVesselSegmentation().setToolTipText(currentProgressValue + "%");
                        }
                        if ("state".equals(propertyChangeEvent.getPropertyName()) &&
                                propertyChangeEvent.getNewValue() == SwingWorker.StateValue.DONE){
                            finalSegmentation=batchSegmentationWorker.getFinalSegmentation();
                            vesselsSegmentationModel.getImageData().setEdgeBinaryMaskImagePlus(batchSegmentationWorker.getEdgeBinaryMaskImagePlus());
                            vesselsSegmentationModel.getImageData().setCentroidHashMap(batchSegmentationWorker.getCentroidHashMap());
                            vesselsSegmentationModel.getImageData().setVesselList(batchSegmentationWorker.getVesselArrayList());
                            vesselsSegmentationModel.getImageData().setRawSegmentation(batchSegmentationWorker.getFinalSegmentation());
                            vesselsSegmentationModel.getImageData().setEdgeCentroidMaskImagePlus(batchSegmentationWorker.getEdgeCentroidMaskImagePlus());
//                            ImagePlus hybridStackWithEdgeCentroidOverlay = batchSegmentationWorker.getStackWithVesselEdgeCentroidOverlay();
                            mainView.getTextField2StatusVesselSegmentation().setText("Complete processing whole stack ");
                            mainView.getButtonMoveToRadialProjection().setEnabled(true);
                            mainView.getProgressBarVesselSegmentation().setValue(100);
                            mainView.getProgressBarVesselSegmentation().setToolTipText(100+"%");
                            // show the results to users
                             ImagePlus segmentedStack = batchSegmentationWorker.getFinalSegmentation().duplicate();
                            segmentedStack.setTitle(batchSegmentationWorker.getFinalSegmentation().getTitle());
                            vesselsSegmentationModel.setRawSegmentation(segmentedStack);
                            ImagePlus edgeCentroidMask = batchSegmentationWorker.getEdgeCentroidMaskImagePlus().duplicate();
                            edgeCentroidMask.setTitle(batchSegmentationWorker.getEdgeCentroidMaskImagePlus().getTitle());
                            vesselsSegmentationModel.setEdgeCentroidMaskImagePlus(edgeCentroidMask);
                            segmentedStack.show();
                            edgeCentroidMask.show();
                        }
                    }
                });
                batchSegmentationWorker.execute();
            }
        });

        // button move to radial projection step
        mainView.getButtonMoveToRadialProjection().addActionListener(new MoveCurrentFileToRadialProjectionStep(
                mainView.getTableAddedFileVesselSegmentation(),
                mainView.getTextFieldRadialProjection(),
                mainView.getTabbedPaneMainPane(),
                mainView.getPanel3RadialProjection(),
                mainView.getButtonRunRadialProjection(),
                mainView.getButtonUnrollVessel(),
                mainView.getTextFieldOutputPath(),
                vesselsSegmentationModel,
                Math.round((float) (vesselsSegmentationModel.getAnalysisWindow() * 1000) /vesselsSegmentationModel.getXyPixelSize()),
                this.context,
                mainView,
                radialProjectionModel
                ));

        // add the initial values from the vesselSegmentation model to the view
        mainView.getSpinnerXYPixelSizeCreateSideView().setValue(vesselsSegmentationModel.getXyPixelSize());
        mainView.getSpinnerZPixelSizeCreateSideView().setValue(vesselsSegmentationModel.getzPixelSize());
        mainView.getSpinnerAnalysisWindow().setValue(vesselsSegmentationModel.getAnalysisWindow());
        mainView.getSpinnerPreWatershedSmoothing().setValue(vesselsSegmentationModel.getSmoothingSigma());
        mainView.getSpinnerSliceIndexForTuning().setValue(vesselsSegmentationModel.getSliceIndexForTuning());
        mainView.getSpinnerInnerVesselRadius().setValue(vesselsSegmentationModel.getInnerVesselRadius());
        mainView.getSliderHybridWeight().setValue(vesselsSegmentationModel.getCelluloseToLigninRatio());
        //---------- - 3.Radial Projection and Unrolling -------------------------------
        // perform Radial Projection
        mainView.getButtonRunRadialProjection().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // update the status bar
                mainView.getTextFieldStatusRadialProjection().setText("Radial Projection...");
                mainView.getProgressBarRadialProjection().setValue(0);
                // Create copy of hybrid using cursors
                 ImagePlus hybridNonSmoothed = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                         radialProjectionModel.getImageData().getHybridStackNonSmoothed(), "Non Smoothed Hybrid Stack");
                ImagePlus hybridSmoothed = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                        radialProjectionModel.getImageData().getHybridStackSmoothed(), "Non Smoothed Hybrid Stack");
                // Create copy of Lignin using cursors
                ImagePlus lignin = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                        radialProjectionModel.getImageData().getLignin(), "Non Smoothed Lignin Stack");
                // Create copy of cellulose using cursors
                ImagePlus cellulose = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                        radialProjectionModel.getImageData().getCellulose(), "Non Smoothed Cellulose Stack");
                RadialProjectionWorker polarProjection = new RadialProjectionWorker(
                        hybridNonSmoothed,
                        hybridSmoothed,
                        cellulose,
                        lignin,
                        radialProjectionModel.getImageData().getEdgeBinaryMaskImagePlus(),
                        radialProjectionModel.getImageData().getVesselList(),
                        (int)mainView.getSpinnerXYPixelSizeCreateSideView().getValue()/1000.0,
                        true,
                        context
                );
                polarProjection.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if("progress".equals(evt.getPropertyName())){
                            mainView.getProgressBarRadialProjection().setValue((int)evt.getNewValue());
                        }
                        if ("state".equals(evt.getPropertyName()) &&
                                evt.getNewValue() == SwingWorker.StateValue.DONE){
                            mainView.getTextFieldStatusRadialProjection().setText("Radial Projection Complete");
                            mainView.getProgressBarRadialProjection().setValue(100);
                            // show the results to users
                            Toolbar toolbar = new Toolbar();
                            int toolIdForCropping = Toolbar.RECTANGLE;
                            for (int i = 0; i < radialProjectionModel.getImageData().getVesselList().size(); i++) { // for each vessel
                                ImagePlus rpCellulose = radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionCellulose().duplicate();
                                rpCellulose.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionCellulose().getTitle());
                                ImagePlus rpHybrid = radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionHybrid().duplicate();
                                rpHybrid.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionHybrid().getTitle());
                                ImagePlus rpLignin = radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionLignin().duplicate();
                                rpLignin.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionLignin().getTitle());
                                List<ImagePlus> imagePlusList = new ArrayList<>(Arrays.asList(rpLignin, rpCellulose, rpHybrid));
                                ImageWindowGroupController iwgc = new ImageWindowGroupController(imagePlusList,
                                        radialProjectionModel.getImageData().getVesselList().get(i),
                                        toolIdForCropping);
                                radialProjectionModel.addVesselRadialProjectionImageWindowGroup(iwgc);
                            }
                            mainView.getButtonMoveToAnalysis().setEnabled(true);
                        }
                    }
                });
                polarProjection.execute();
            }
        });
        // Unrolling Vessels
        mainView.getButtonUnrollVessel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // update the status bar
                mainView.getTextFieldStatusRadialProjection().setText("Unrolling...");
                mainView.getProgressBarRadialProjection().setValue(0);
                // create copy of Hybrid using cursors
                ImagePlus hybrid = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                        radialProjectionModel.getImageData().getHybridStackNonSmoothed(), "Non Smoothed Hybrid Stack");
                // create copy of Smoothed Hybrid using cursors
                ImagePlus hybridSmoothed = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                        radialProjectionModel.getImageData().getHybridStackSmoothed(), "Smoothed Hybrid Stack");
                // Create copy of Lignin using cursors
                ImagePlus lignin = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                        radialProjectionModel.getImageData().getLignin(), "Non Smoothed Lignin Stack");
                // Create copy of Cellulose using cursors
                ImagePlus cellulose = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                        radialProjectionModel.getImageData().getCellulose(), "Non Smoothed Cellulose Stack");
                UnrollVesselWorker unrollVesselWorker = new UnrollVesselWorker(hybridSmoothed,
                        hybrid,
                        cellulose,
                        lignin,
                        radialProjectionModel.getImageData().getEdgeBinaryMaskImagePlus(),
                        radialProjectionModel.getImageData().getVesselList(),
                        true
                );
                unrollVesselWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if("progress".equals(evt.getPropertyName())){
                            mainView.getProgressBarRadialProjection().setValue((int)evt.getNewValue());
                        }
                        if ("state".equals(evt.getPropertyName()) &&
                                evt.getNewValue() == SwingWorker.StateValue.DONE){
                            mainView.getTextFieldStatusRadialProjection().setText("Unrolling Complete");
                            mainView.getProgressBarRadialProjection().setValue(100);
                            // show the results to the users
                            for (int i = 0; i < radialProjectionModel.getImageData().getVesselList().size(); i++) {
                                ImagePlus rpCellulose = radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselCellulose().duplicate();
                                rpCellulose.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselCellulose().getTitle());
                                ImagePlus rpHybrid = radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselHybrid().duplicate();
                                rpHybrid.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselHybrid().getTitle());
                                ImagePlus rpLignin = radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselLignin().duplicate();
                                rpLignin.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselLignin().getTitle());
                                ImagePlus rpContour = radialProjectionModel.getImageData().getVesselList().get(i).getContour().duplicate();
                                rpContour.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getContour().getTitle());
                                List<ImagePlus> imagePlusList = new ArrayList<>(Arrays.asList(rpLignin, rpCellulose, rpHybrid, rpContour));
                                int toolIdForCropping = Toolbar.RECTANGLE;
                                ImageWindowGroupController iwgc = new ImageWindowGroupController(imagePlusList,
                                        radialProjectionModel.getImageData().getVesselList().get(i),
                                        toolIdForCropping);
                                radialProjectionModel.addVesselUnrollImageWindowGroup(iwgc);
                            }
                        }
                    }
                });
                unrollVesselWorker.execute();
            }
        });

        mainView.getButtonMoveToAnalysis().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveRadialProjectionOutputWorker srpow = new SaveRadialProjectionOutputWorker(radialProjectionModel.getImageData());
                srpow.execute();
                SaveUnrollingOutputWorker suow = new SaveUnrollingOutputWorker(radialProjectionModel.getImageData());
                suow.execute();
                radialProjectionModel.closeAllImageWindowGroup();
                analysisModel.setImageData(radialProjectionModel.getImageData());
                mainView.getButtonRunRadialProjection().setEnabled(false);
                mainView.getButtonUnrollVessel().setEnabled(false);
                mainView.getButtonLegacyBandMeasurement().setEnabled(true);
                mainView.getButtonSegmentationBySplitting().setEnabled(true);
                mainView.getButtonCustomSkeletonize().setEnabled(true);
                DefaultTableModel model = (DefaultTableModel) mainView.getTableAnalysisInputImage().getModel();
                model.addRow(new String[]{analysisModel.getImageData().getImagePath().toAbsolutePath().toString()});
                SpinnerNumberModel spinnerNumberModelRandomBoxWidth = (SpinnerNumberModel) mainView.getSpinnerRandomBoxWidth().getModel();
                spinnerNumberModelRandomBoxWidth.setMaximum(analysisModel.getImageData().getVesselList().get(0).getRadialProjectionHybrid().getWidth());
                mainView.getTabbedPaneMainPane().setSelectedComponent(mainView.getPanel4Analysis());
            }
        });
        //-------------------Analysis----------------------------------------------
        mainView.getButtonLegacyBandMeasurement().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RandomLineScanWorker randomLineScanWorker = new RandomLineScanWorker(
                        (Integer)mainView.getSpinnerNumberOfLineScan().getValue(),
                        (Integer) mainView.getSpinnerLineScanLength().getValue(),
                        analysisModel.getImageData().getXyPixelSize(),
                        analysisModel.getImageData().getVesselList()
                );
                randomLineScanWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("state".equals(evt.getPropertyName()) &&
                                evt.getNewValue() == SwingWorker.StateValue.DONE){
                            int index = 0;
                            mainView.getTextAreaBandGapResult().setText("");
                            for (Vessel vessel:analysisModel.getImageData().getVesselList()){
                                index++;
                                ImagePlus bandBinary = vessel.getBandHybridMaskImagePlus().duplicate();
                                bandBinary.setTitle(vessel.getBandHybridImagePlus().getTitle());
                                bandBinary.show();
                                mainView.getTextAreaBandGapResult().append("Vessel " + index + ": " + System.lineSeparator());
                                mainView.getTextAreaBandGapResult().append("number of bands: " + vessel.getNoOfBands()+ System.lineSeparator());
                                mainView.getTextAreaBandGapResult().append(String.format("Mean band width: %.3f ± %.3f",vessel.getMeanBandWidth(),vessel.getSdBandWidth())+System.lineSeparator());
                                mainView.getTextAreaBandGapResult().append("number of gaps: " + vessel.getNoOfGaps()+ System.lineSeparator());
                                mainView.getTextAreaBandGapResult().append(String.format("Mean gap width: %.3f ± %.3f",vessel.getMeanGapWidth(),vessel.getSdGapWidth())+System.lineSeparator());
                                mainView.getTextAreaBandGapResult().append(System.lineSeparator());
                            }
                        }
                    }
                });
                randomLineScanWorker.execute();
            }
        });

        mainView.getButtonSegmentationBySplitting().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Vessel> vesselArrayList = vesselsSegmentationModel.getVesselArrayList();
                Vessel vessel1 = vesselArrayList.get(0);
                ImagePlus vessel1Img = vessel1.getRadialProjectionLignin();
                int percentageForSplitting = (int)mainView.getSpinnerPercentageForSplitting().getValue();
                List<Tile> tileArrayList = Tile.divideIntoEqualSize(vessel1Img.getWidth(),
                                                                    vessel1Img.getHeight(),
                                                                            percentageForSplitting);
                Tile.splitImage(vessel1Img,tileArrayList);
                for (Tile t: tileArrayList){
                    t.autoThreshold((AutoThresholder.Method) mainView.getComboboxAutoThresholdingMethod().getSelectedItem(),1);
                }
                ByteProcessor thresholdedProcessor = Tile.combineTiles(tileArrayList,vessel1Img.getWidth(),vessel1Img.getHeight());
                ImagePlus thresholdedRadialProjection = new ImagePlus("Segmented radial projection", thresholdedProcessor);
                ByteProcessor skeletonizedRadialProjectionProcessor = (ByteProcessor) thresholdedProcessor.duplicate();
                skeletonizedRadialProjectionProcessor.skeletonize(255);
                ImagePlus skeletonizedRadialProjection = new ImagePlus("Skeletonized Radial Projection", skeletonizedRadialProjectionProcessor);
                thresholdedRadialProjection.show();
                skeletonizedRadialProjection.show();
            }
        });

        mainView.getButtonCustomSkeletonize().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Vessel> vesselArrayList = vesselsSegmentationModel.getVesselArrayList();
                Vessel vessel1 = vesselArrayList.get(0);
                ImagePlus vessel1Img = vessel1.getRadialProjectionLignin();
                // apply custom bandPath detect function
                ShortProcessor bandPathShortProcessor = (ShortProcessor) vessel1Img.getProcessor().duplicate();
                BackgroundSubtracter backgroundSubtracter = new BackgroundSubtracter();
                backgroundSubtracter.subtractBackround(bandPathShortProcessor,50);
                bandPathShortProcessor.blurGaussian(1);
                ByteProcessor bandPathByteProcessor = Utils.detectBandPath((ShortProcessor) vessel1Img.getProcessor(),vessel1Img.getWidth(),vessel1Img.getHeight());
                ImagePlus bandPathImagePlus = new ImagePlus("apply custom skeletonize function",bandPathByteProcessor);
                bandPathImagePlus.show();
                // apply mask to hybrid image
                ShortProcessor applyMaskedHybrid = Utils.applyMask(bandPathByteProcessor, (ShortProcessor) vessel1.getRadialProjectionHybrid().getProcessor());
                ImagePlus applyMaskedHybridImagePlus = new ImagePlus("appliedMaskedHybrid", applyMaskedHybrid);
                applyMaskedHybridImagePlus.show();
                // apply mask to cellulose image
                ShortProcessor applyMaskedCellulose = Utils.applyMask(bandPathByteProcessor, (ShortProcessor) vessel1.getRadialProjectionCellulose().getProcessor());
                ImagePlus applyMaskedCelluloseImagePlus = new ImagePlus("appliedMaskedCellulose", applyMaskedCellulose);
                applyMaskedCelluloseImagePlus.show();
            }
        });

        // Compute anisotropy
        mainView.getSpinnerRandomBoxWidth().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) mainView.getSpinnerRandomBoxWidth().getValue();
                analysisModel.setRandomBoxWidth(value);
            }
        });
        mainView.getSpinnerNoRandomBoxes().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) mainView.getSpinnerNoRandomBoxes().getValue();
                analysisModel.setNumberOfRandomBoxes(value);
            }
        });

        mainView.getButtonComputeAnisotropy().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnisotropyWorker anisotropyWorker = new AnisotropyWorker(analysisModel.getImageData().getVesselList(),
                        (int)mainView.getSpinnerRandomBoxWidth().getValue(),(int) mainView.getSpinnerNoRandomBoxes().getValue());
                anisotropyWorker.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("state".equals(evt.getPropertyName()) &&
                                evt.getNewValue() == SwingWorker.StateValue.DONE){
                            List<Vessel> vesselList = analysisModel.getImageData().getVesselList();
                            mainView.getTextAreaAnisotropyResult().setText("");
                            int idx = 0;
                            for(Vessel vessel: vesselList){
                                idx+=1;
                                mainView.getTextAreaAnisotropyResult().append("Vessel " +idx+ System.lineSeparator());
                                mainView.getTextAreaAnisotropyResult().append(String.format("Mean orientation: %.3f ± %.3f",vessel.getMeanBandOrientation(),vessel.getSdBandOrientation())+System.lineSeparator());
                                mainView.getTextAreaAnisotropyResult().append(String.format("Mean anisotropy: %.3f ± %.3f",vessel.getMeanAnisotropy(),vessel.getSdAnisotropy())+ System.lineSeparator());
                                mainView.getTextAreaAnisotropyResult().append(System.lineSeparator());
                            }
                        }
                    }
                });
                anisotropyWorker.execute();
            }
        });

        // save Analysis Result to CSV file
        mainView.getButtonExportResultToCSV().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean combine = mainView.getCheckBoxCombineResultCSV().isSelected();
                SaveVesselResultToCSV saveVesselResultToCSV = new SaveVesselResultToCSV(analysisModel.getImageDataList(), combine);
                try {
                    saveVesselResultToCSV.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // save Analysis Result to XLSX file
        mainView.getButtonExportToXLSX().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean combine = mainView.getCheckBoxCombineResultXLSX().isSelected();
                SaveVesselResultToXLSX saveVesselResultToXLSX = new SaveVesselResultToXLSX(analysisModel.getImageDataList(), combine);
                try {
                    saveVesselResultToXLSX.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        //--------------MAIN WINDOW-----------------------------------------
        // TODO:Save all the parameters of current session to imagej/Fiji Prefs
        mainView.getParentFrame().addWindowListener(
                new AddSavingActionWhenMainWindowClosed(cziToTifModel,
                                                        vesselsSegmentationModel));
        // TODO: save parameters when the user close the main Fiji/Imagej menu

    }



}

