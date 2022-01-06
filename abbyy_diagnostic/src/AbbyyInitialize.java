// (c) 2018 ABBYY Production LLC
// SAMPLES code is property of ABBYY, exclusive rights are reserved. 
//
// DEVELOPER is allowed to incorporate SAMPLES into his own APPLICATION and modify it under 
// the  terms of  License Agreement between  ABBYY and DEVELOPER.


// ABBYY FineReader Engine 12 Sample

// This sample shows basic steps of ABBYY FineReader Engine usage:
// Initializing, opening image file, recognition and export.

import com.abbyy.FREngine.*;
import java.nio.file.*;

public class AbbyyInitialize {

	public static void main( String[] args ) {
		try {
			AbbyyInitialize application = new AbbyyInitialize();
			application.Run();
		} catch( Exception ex ) {
			displayMessage( ex.getMessage() );
		}
	}

	public void Run() throws Exception {
		// Load ABBYY FineReader Engine
		loadEngine();
		try{
			// Process with ABBYY FineReader Engine
//			processWithEngine();
			displayMessage("Entering custom code...");

		} finally {
			// Unload ABBYY FineReader Engine
			unloadEngine();
		}
	}

	private void loadEngine() throws Exception {
		displayMessage( "Initializing Engine..." );
		engine = Engine.InitializeEngine( SamplesConfig.GetDllFolder(), SamplesConfig.GetCustomerProjectId(), 
			SamplesConfig.GetLicensePath(), SamplesConfig.GetLicensePassword(), "", "", false );
	}

	private void processWithEngine() {
		try {
			// Setup FREngine
			setupFREngine();

			// Process sample image
			processImage();
		} catch( Exception ex ) {
			displayMessage( ex.getMessage() );
		}
	}

	private void setupFREngine() {
		displayMessage( "Loading predefined profile..." );
		engine.LoadPredefinedProfile( "DocumentConversion_Accuracy" );
		// Possible profile names are:
		//   "DocumentConversion_Accuracy", "DocumentConversion_Speed",
		//   "DocumentArchiving_Accuracy", "DocumentArchiving_Speed",
		//   "BookArchiving_Accuracy", "BookArchiving_Speed",
		//   "TextExtraction_Accuracy", "TextExtraction_Speed",
		//   "FieldLevelRecognition",
		//   "BarcodeRecognition_Accuracy", "BarcodeRecognition_Speed",
		//   "HighCompressedImageOnlyPdf",
		//   "BusinessCardsProcessing",
		//   "EngineeringDrawingsProcessing",
		//   "Version9Compatibility",
		//   "Default"
	}

	private void processImage() {
		String imagePath = Paths.get( SamplesConfig.GetSamplesFolder(), "SampleImages", "Demo.tif" ).toString();

		try {
			// Don't recognize PDF file with a textual content, just copy it
			if( engine.IsPdfWithTextualContent( imagePath, null ) ) {
				displayMessage( "Copy results..." );
				Path resultPath = Paths.get( SamplesConfig.GetSamplesFolder(), "SampleImages", "Demo_copy.pdf" );
				Files.copy( Paths.get( imagePath ), resultPath, StandardCopyOption.REPLACE_EXISTING );
				return;
			}

			// Create document
			IFRDocument document = engine.CreateFRDocument();

			try {
				// Add image file to document
				displayMessage( "Loading image..." );
				document.AddImageFile( imagePath, null, null );

				// Process document
				displayMessage( "Process..." );
				document.Process( null );

				// Save results
				displayMessage( "Saving results..." );

				// Save results to rtf with default parameters
				String rtfExportPath = SamplesConfig.GetSamplesFolder() + "\\SampleImages\\Demo.rtf";
				document.Export( rtfExportPath, FileExportFormatEnum.FEF_RTF, null );

				// Save results to pdf using 'balanced' scenario
				IPDFExportParams pdfParams = engine.CreatePDFExportParams();
				pdfParams.setScenario( PDFExportScenarioEnum.PES_Balanced );

				String pdfExportPath = SamplesConfig.GetSamplesFolder() + "\\SampleImages\\Demo.pdf";
				document.Export( pdfExportPath, FileExportFormatEnum.FEF_PDF, pdfParams );
			} finally {
				// Close document
				document.Close();
				document.Release();
			}
		} catch( Exception ex ) {
			displayMessage( ex.getMessage() );
		}
	}

	private void unloadEngine() throws Exception {
		displayMessage( "Deinitializing Engine..." );
		engine = null;
		Engine.DeinitializeEngine();
	}

	private static void displayMessage( String message ) {
		System.out.println( message );
	}

	private IEngine engine = null;
}
