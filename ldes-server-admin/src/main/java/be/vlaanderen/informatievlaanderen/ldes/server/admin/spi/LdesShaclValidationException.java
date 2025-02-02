package be.vlaanderen.informatievlaanderen.ldes.server.admin.spi;

import org.apache.jena.rdf.model.Model;

public class LdesShaclValidationException extends RuntimeException {

	private final String validationReport;
	private final transient Model validationReportModel;

	public LdesShaclValidationException(String validationReport, Model validationReportModel) {
		super();
		this.validationReportModel = validationReportModel;
		this.validationReport = validationReport;
	}

	@Override
	public String getMessage() {
		return "Shacl validation failed: \n\n" + validationReport;
	}

	public Model getValidationReportModel() {
		return validationReportModel;
	}

}
