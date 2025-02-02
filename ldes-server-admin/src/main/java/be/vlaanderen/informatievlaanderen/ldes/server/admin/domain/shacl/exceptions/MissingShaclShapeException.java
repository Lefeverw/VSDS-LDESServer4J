package be.vlaanderen.informatievlaanderen.ldes.server.admin.domain.shacl.exceptions;

public class MissingShaclShapeException extends RuntimeException {
	private final String collectionName;

	public MissingShaclShapeException(String collectionName) {
		this.collectionName = collectionName;
	}

	@Override
	public String getMessage() {
		return "No shacl shape configured for collection " + collectionName;
	}
}
