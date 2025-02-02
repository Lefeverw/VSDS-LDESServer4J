package be.vlaanderen.informatievlaanderen.ldes.server.admin.domain.dcat.dcatserver.exceptions;

public class MissingDcatServerException extends RuntimeException {

	public MissingDcatServerException(String id) {
		super("No dcat is configured on the server with id %s".formatted(id));
	}

	public MissingDcatServerException() {
		super("No dcat is configured on the server");
	}

}
